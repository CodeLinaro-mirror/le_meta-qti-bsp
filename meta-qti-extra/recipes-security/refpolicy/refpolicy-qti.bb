SUMMARY = "HOST SELinux policy build"
DESCRIPTION = "build GNU/Linux SELinux policies for LV HOST"
HOMEPAGE = "https://selinuxproject.org/"
SECTION = "admin"
LICENSE = "GPLv2 & BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=393a5ca445f6965873eca0259a17f833 \
                    file://${COREBASE}/meta/files/common-licenses/BSD;md5=3775480a712fc46a69647678acb234cb"

DEPENDS += "bzip2-replacement-native secilc-native checkpolicy-native policycoreutils-native semodule-utils-native m4-native sepolicy-cil-native"

SRCREV = "ad7217f906e89c49835fdc305110a97b56865442"

SRC_URI = "\
    git://github.com/SELinuxProject/refpolicy.git;protocol=http;branch=master;name=refpolicy;destsuffix=refpolicy \
    file://vendor-modules \
    file://host-refpolicy \
"

S = "${WORKDIR}/refpolicy"
S_GIT_REFPOLICY = "${WORKDIR}/refpolicy"
S_HOST_REFPOLICY = "${WORKDIR}/host-refpolicy"
S_HOST_MODULES = "${WORKDIR}/vendor-modules"
S_ANDROID_CILS = "${STAGING_DATADIR_NATIVE}/android_cils"
S_PRECOMBINED_CILS = "${WORKDIR}/precombined_cils"

EXTRA_OEMAKE += "\
    NAME=${POLICY_NAME} \
    TYPE=${POLICY_TYPE} \
    DISTRO=redhat \
    UBAC=n \
    UNK_PERMS=allow \
    DIRECT_INITRC=n \
    SYSTEMD=${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'y', 'n', d)} \
    MONOLITHIC=n \
    QUIET=y \
    MLS_SENS=16 \
    MLS_CATS=1024 \
    MCS_CATS=1024 \
    tc_usrbindir=${STAGING_BINDIR_NATIVE} \
    OUTPUT_POLICY=`${STAGING_BINDIR_NATIVE}/checkpolicy -V | cut -d' ' -f1` \
"

PACKAGE_ARCH = "${MACHINE_ARCH}"

PROVIDES += "virtual/refpolicy"
RPROVIDES_${PN} += "refpolicy"

FILES_${PN} += "\
    ${sysconfdir}/selinux/${POLICY_NAME}/ \
    ${datadir}/selinux/${POLICY_NAME}/*.pp \
    ${localstatedir}/lib/selinux/${POLICY_NAME}/ \
"
FILES_${PN}-dev += "\
    ${datadir}/selinux/${POLICY_NAME}/include/ \
    ${sysconfdir}/selinux/sepolgen.conf \
"

DEFAULT_ENFORCING ??= "permissive"
POLICY_TYPE = "qti"
POLICY_NAME = "qti"
# Need add module name into here once add a new module in vendor-modules.
# we want to get the cil file of vendor-modules with module defined in modules.conf,
# we can't config module which we added in vendor-modules as base in modules.conf because it would re-declaration when we combine host cil with android cils,
# so set module such as "host = module" in modules.conf is neccesary.
HOST_POLICY_MODULES += "host fix"

fakeroot do_configure() {
    install -d ${S_GIT_REFPOLICY}/config/appconfig-qti
    cp -rf ${S_GIT_REFPOLICY}/config/appconfig-mcs/* ${S_GIT_REFPOLICY}/config/appconfig-qti/
    echo "r:sshd_t:s0     r:unconfined_t:s0" > ${S_GIT_REFPOLICY}/config/appconfig-qti/default_type
    echo "r:unconfined_t:s0" > ${S_GIT_REFPOLICY}/config/appconfig-qti/failsafe_context
    echo "r:sshd_t:s0     r:unconfined_t:s0" > ${S_GIT_REFPOLICY}/config/appconfig-qti/u_default_contexts
    echo "root:u" > ${S_GIT_REFPOLICY}/config/appconfig-qti/seusers
    echo "__default__:u" >> ${S_GIT_REFPOLICY}/config/appconfig-qti/seusers
    echo "<summary>Policy modules for the Qti selinux.</summary>" > ${S_HOST_MODULES}/metadata.xml
    cp -rf ${S_HOST_MODULES} ${S_GIT_REFPOLICY}/policy/modules/
    sed -i 's/class system (ipc_info syslog_read syslog_mod syslog_console module_request module_load /&halt reboot status start stop enable disable reload/' ${S_ANDROID_CILS}/system/plat_sepolicy.cil
    sed -i 's/keystore_key drmservice /&service dbus passwd/' ${S_ANDROID_CILS}/system/plat_sepolicy.cil
    sed -i '$a (class passwd ( passwd chfn chsh rootok crontab ))' ${S_ANDROID_CILS}/system/plat_sepolicy.cil
}

fakeroot do_compile() {
    oe_runmake conf
    oe_runmake policy
}

prepare_policy_store () {
    oe_runmake 'DESTDIR=${D}' 'prefix=${D}${prefix}' install
    POL_PRIORITY=100
    POL_SRC=${D}${datadir}/selinux/${POLICY_NAME}
    POL_STORE=${D}${localstatedir}/lib/selinux/${POLICY_NAME}
    POL_ACTIVE_MODS=${POL_STORE}/active/modules/${POL_PRIORITY}
    # Prepare to create policy store
    mkdir -p ${POL_STORE}
    mkdir -p ${POL_ACTIVE_MODS}
    mkdir -p ${S_PRECOMBINED_CILS}
    # get hll type from suffix on base policy module
    HLL_TYPE=$(echo ${POL_SRC}/base.* | awk -F . '{if (NF>1) {print $NF}}')
    HLL_BIN=${STAGING_DIR_NATIVE}${prefix}/libexec/selinux/hll/${HLL_TYPE}
    for i in base ${HOST_POLICY_MODULES}; do
        MOD_FILE=${POL_SRC}/${i}.${HLL_TYPE}
        MOD_DIR=${POL_ACTIVE_MODS}/${i}
        mkdir -p ${MOD_DIR}
        echo -n "${HLL_TYPE}" > ${MOD_DIR}/lang_ext
        # don't need to compress because we would apply cil file to compile policy file with android cils
        ${HLL_BIN} ${MOD_FILE} > ${MOD_DIR}/cil
        cp ${MOD_DIR}/cil ${S_PRECOMBINED_CILS}/${i}.cil
        cp ${MOD_FILE} ${MOD_DIR}/hll
        # By default, below big file will be installed to persist,this may make
        # persist run out of space
        # remove it temporarily as it is useless when system bootup.
        rm ${MOD_DIR}/hll
        rm ${MOD_DIR}/cil
    done
}

# some statements can't be recognized by sepolicy when compile sepolicy and refpolicy
compatibility_fix() {
    sed -i '/roleattributeset cil_gen_require system_r/d' ${S_PRECOMBINED_CILS}/host.cil
}

rebuild_policy () {
    install -d ${D}/${sysconfdir}/selinux/${POLICY_NAME}/policy
    SECILC_BIN=${STAGING_DIR_NATIVE}${prefix}/bin/secilc
    ${SECILC_BIN} ${S_PRECOMBINED_CILS}/host.cil ${S_PRECOMBINED_CILS}/fix.cil ${S_ANDROID_CILS}/system/plat_sepolicy.cil ${S_ANDROID_CILS}/system/30.0.cil ${S_ANDROID_CILS}/vendor/vendor_sepolicy.cil ${S_ANDROID_CILS}/vendor/plat_pub_versioned.cil ${S_ANDROID_CILS}/product/30.0.cil ${S_ANDROID_CILS}/product/product_sepolicy.cil ${S_ANDROID_CILS}/system_ext/30.0.cil ${S_ANDROID_CILS}/system_ext/system_ext_sepolicy.cil ${S_HOST_REFPOLICY}/host_append.cil -m -M true -G -N -c 30 -o ${D}${sysconfdir}/selinux/${POLICY_NAME}/policy/policy.30
}

install_misc_files () {
    echo "user_tty_device_t" > \
        ${D}${sysconfdir}/selinux/${POLICY_NAME}/contexts/customizable_types
    # install setrans.conf for mls/mcs policy
    if [ -f ${S_HOST_REFPOLICY}/setrans-${POLICY_TYPE}.conf ]; then
        install -m 0644 ${S_HOST_REFPOLICY}/setrans-${POLICY_TYPE}.conf \
        ${D}${sysconfdir}/selinux/${POLICY_NAME}/setrans.conf
    fi
    # install policy headers
    oe_runmake 'DESTDIR=${D}' 'prefix=${D}${prefix}' install-headers
}

install_config () {
    echo "\
# This file controls the state of SELinux on the system.
# SELINUX= can take one of these three values:
#     enforcing - SELinux security policy is enforced.
#     permissive - SELinux prints warnings instead of enforcing.
#     disabled - No SELinux policy is loaded.
SELINUX=${DEFAULT_ENFORCING}
# SELINUXTYPE= can take one of these values:
#     minimum - Minimum Security protection.
#     standard - Standard Security protection.
#     mls - Multi Level Security protection.
#     targeted - Targeted processes are protected.
#     mcs - Multi Category Security protection.
SELINUXTYPE=${POLICY_NAME}
" > ${WORKDIR}/config
    install -d ${D}/${sysconfdir}/selinux
    install -m 0644 ${WORKDIR}/config ${D}/${sysconfdir}/selinux/
}

install_file_contexts() {
    if [ -e ${D}/${sysconfdir}/selinux/${POLICY_NAME}/contexts/files/file_contexts ]; then
        rm -rf ${D}/${sysconfdir}/selinux/${POLICY_NAME}/contexts/files/file_contexts
    fi
    install -m 0644 ${S_HOST_REFPOLICY}/file_contexts ${D}/${sysconfdir}/selinux/${POLICY_NAME}/contexts/files/
}

do_install () {
    prepare_policy_store
    compatibility_fix
    rebuild_policy
    install_misc_files
    install_config
    install_file_contexts
}

do_install_append(){
    # While building policies on target, Makefile will be searched from SELINUX_DEVEL_PATH
    echo "SELINUX_DEVEL_PATH=${datadir}/selinux/${POLICY_NAME}/include" > ${D}${sysconfdir}/selinux/sepolgen.conf
}

sysroot_stage_all_append () {
    sysroot_stage_dir ${D}${sysconfdir} ${SYSROOT_DESTDIR}${sysconfdir}
}
