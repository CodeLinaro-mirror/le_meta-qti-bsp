SUMMARY = "SELinux targeted policy"
DESCRIPTION = "\
This is the targeted variant of the SELinux reference policy.  Most service \
domains are locked down. Users and admins will login in with unconfined_t \
domain, so they have the same access to the system as if SELinux was not \
enabled. \
"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${QTI_LICENSE_DIR}/${LICENSE};md5=b796c0007db682166a1721da80267bb2"

DEPENDS += "bzip2-replacement-native checkpolicy-native m4-native policycoreutils-native semodule-utils-native"

PROVIDES = "virtual/refpolicy"

SRC_URI = "git://git.codelinaro.org/clo/yocto-mirrors/github/fedora-selinux/seinux-policy.git;protocol=https;branch=c9s;name=refpolicy;destsuffix=refpolicy \
        git://github.com/containers/container-selinux.git;protocol=https;branch=main;name=container-selinux;destsuffix=container-selinux \
        git://gitlab.com/redhat/centos-stream/rpms/selinux-policy.git;protocol=https;branch=c9s;name=selinux-policy;destsuffix=selinux-policy \
"

SRCREV_refpolicy = "52f34fc4881b78295aaf4eef595ad381e53796ac"
SRCREV_container-selinux = "a8e389dbcd3f9b6ed0a7e495c6f559c0383dc49e"
SRCREV_selinux-policy = "eab0528813179badce642bf8c4682ebbc8687b7c"
SRCREV_FORMAT = "refpolicy_container-selinux_selinux-policy"

S = "${WORKDIR}/refpolicy"

RDEPENDS:${PN}-dev =+ "\
        python3-core \
"

RPROVIDES:${PN} = "refpolicy"

PACKAGE_ARCH = "${MACHINE_ARCH}"

require refpolicy-hgy-lv.inc

FILES:${PN} += "\
    ${sysconfdir}/selinux/${POLICY_NAME}/ \
    ${datadir}/selinux/${POLICY_NAME}/*.pp \
    ${datadir}/selinux/${POLICY_NAME}/include/ \
    /data/lib/selinux/${POLICY_NAME}/ \
    /data/lib/selinux/final/ \
"

DEFAULT_ENFORCING ??= "enforcing"

POLICY_NAME = "targeted"
POLICY_TYPE = "mcs"
POLICY_DISTRO ?= "rhel4"
POLICY_UBAC ?= "n"
POLICY_UNK_PERMS ?= "allow"
POLICY_DIRECT_INITRC ?= "n"
POLICY_MONOLITHIC ?= "n"
POLICY_MLS_SENS ?= "0"
POLICY_MLS_CATS ?= "1024"
POLICY_MCS_CATS ?= "1024"

EXTRA_OEMAKE += "NAME=${POLICY_NAME} \
    TYPE=${POLICY_TYPE} \
    DISTRO=${POLICY_DISTRO} \
    UBAC=${POLICY_UBAC} \
    UNK_PERMS=${POLICY_UNK_PERMS} \
    DIRECT_INITRC=${POLICY_DIRECT_INITRC} \
    MONOLITHIC=${POLICY_MONOLITHIC} \
    MLS_SENS=${POLICY_MLS_SENS} \
    MLS_CATS=${POLICY_MLS_CATS} \
    MCS_CATS=${POLICY_MCS_CATS} \
"

EXTRA_OEMAKE += "tc_usrbindir=${STAGING_BINDIR_NATIVE}"
EXTRA_OEMAKE += "OUTPUT_POLICY=`${STAGING_BINDIR_NATIVE}/checkpolicy -V | cut -d' ' -f1`"
EXTRA_OEMAKE += "CC='${BUILD_CC}' CFLAGS='${BUILD_CFLAGS}' PYTHON='${PYTHON}'"

EXTRA_CONTRIB_POLICY_MODULES = "networkmanager dbus virt irqbalance logrotate mta rpm pcp"

extract_lv_sepolicy() {
    input_file="${S}/policy/modules.conf"
    output_file="${S}/policy/modules-lv-sepolicy.conf"
    pattern1="qti_.* = module"
    pattern2="rh_.* = module"
    temp_file=$(mktemp)
    grep -B 5 -E "$pattern1|$pattern2" "$input_file" > "$temp_file"
    awk '{print; if ($0 ~ /'"$pattern1|$pattern2"'/) print ""}' "$temp_file" > "$output_file"
    rm "$temp_file"
}

generate_selinux_conf() {
    if [ -f "${S}/policy/modules.conf" ] ; then
        cp -f ${S}/policy/modules.conf ${S}/policy/modules_backup.conf
    fi

    if [ -f "${WORKDIR}/selinux-policy/booleans-targeted.conf" ] ; then
        cp -f ${WORKDIR}/selinux-policy/booleans-targeted.conf ${S}/policy/booleans.conf
    fi

    if [ -f "${WORKDIR}/selinux-policy/users-targeted" ] ; then
        cp -f ${WORKDIR}/selinux-policy/users-targeted ${S}/policy/users
    fi

    if [ -f "${WORKDIR}/selinux-policy/modules-targeted-base.conf" ] ; then
        cp -f ${WORKDIR}/selinux-policy/modules-targeted-base.conf ${S}/policy/modules-base.conf
        cp -f ${WORKDIR}/selinux-policy/modules-targeted-base.conf ${S}/policy/modules.conf
    fi

    if [ -f "${WORKDIR}/selinux-policy/modules-targeted-contrib.conf" ] ; then
        cp -f ${WORKDIR}/selinux-policy/modules-targeted-contrib.conf ${S}/policy/modules-contrib.conf
        sed -i 's/= *module/= off/g' ${S}/policy/modules-contrib.conf
        for module in ${EXTRA_CONTRIB_POLICY_MODULES} ; do
            sed -i "s/^\(\<${module}\>\) *= *.*$/\1 = module/" ${S}/policy/modules-contrib.conf
        done
        cat ${S}/policy/modules-contrib.conf >> ${S}/policy/modules.conf
    fi

    if [ -f "${S}/policy/modules-lv-sepolicy.conf" ] ; then
        cat ${S}/policy/modules-lv-sepolicy.conf >> ${S}/policy/modules.conf
    fi
}

makeCmds() {
    oe_runmake bare
    rm -f ${S}/policy/modules.conf

    oe_runmake conf
    cp -f ${WORKDIR}/selinux-policy/booleans-targeted.conf ${S}/policy/booleans.conf
    cp -f ${WORKDIR}/selinux-policy/users-targeted ${S}/policy/users
    sed -i '/genfs_seclabel_symlinks/s/^/#/' ${S}/policy/policy_capabilities
}

do_compile() {
    makeCmds
    extract_lv_sepolicy
    generate_selinux_conf
    oe_runmake base.pp
    oe_runmake modules
}

prepare_policy_store () {
    oe_runmake 'DESTDIR=${D}' 'prefix=${D}${prefix}' install
    POL_PRIORITY=100
    POL_SRC=${D}${datadir}/selinux/${POLICY_NAME}
    POL_STORE=${D}/data/lib/selinux/${POLICY_NAME}
    POL_ACTIVE_MODS=${POL_STORE}/active/modules/${POL_PRIORITY}

    # Prepare to create policy store
    mkdir -p ${POL_STORE}
    mkdir -p ${POL_ACTIVE_MODS}

    # get hll type from suffix on base policy module
    HLL_TYPE=$(echo ${POL_SRC}/base.* | awk -F . '{if (NF>1) {print $NF}}')
    HLL_BIN=${STAGING_DIR_NATIVE}${prefix}/libexec/selinux/hll/${HLL_TYPE}

    for i in ${POL_SRC}/*.${HLL_TYPE}; do
        MOD_NAME=$(basename $i | sed "s/\.${HLL_TYPE}$//")
        MOD_DIR=${POL_ACTIVE_MODS}/${MOD_NAME}
        mkdir -p ${MOD_DIR}
        echo -n "${HLL_TYPE}" > ${MOD_DIR}/lang_ext
        if ! bzip2 -t $i >/dev/null 2>&1; then
            ${HLL_BIN} $i | bzip2 --stdout > ${MOD_DIR}/cil
            bzip2 -f $i && mv -f $i.bz2 $i
        else
            bunzip2 --stdout $i | \
                ${HLL_BIN} | \
                bzip2 --stdout > ${MOD_DIR}/cil
        fi
        cp $i ${MOD_DIR}/hll
    done
}

rebuild_policy () {
    cat <<-EOF > ${D}${sysconfdir}/selinux/semanage.conf
module-store = direct
store-root=/data/lib/selinux
[setfiles]
path = ${STAGING_DIR_NATIVE}${base_sbindir_native}/setfiles
args = -q -c \$@ \$<
[end]
[sefcontext_compile]
path = ${STAGING_DIR_NATIVE}${sbindir_native}/sefcontext_compile
args = \$@
[end]

policy-version = 33
EOF

    # Create policy store and build the policy
    semodule -p ${D} -s ${POLICY_NAME} -n -B
    rm -f ${D}${sysconfdir}/selinux/semanage.conf
    # no need to leave final dir created by semanage laying around
    rm -rf ${D}${localstatedir}/lib/selinux/final
}

install_misc_files() {
    install -m0644 ${WORKDIR}/selinux-policy/securetty_types-targeted ${D}/${sysconfdir}/selinux/targeted/contexts/securetty_types
    install -m0644 ${WORKDIR}/selinux-policy/file_contexts.subs_dist ${D}/${sysconfdir}/selinux/targeted/contexts/files
    install -m0644 ${WORKDIR}/selinux-policy/setrans-targeted.conf ${D}/${sysconfdir}/selinux/targeted/setrans.conf
    install -m0644 ${WORKDIR}/selinux-policy/customizable_types ${D}/${sysconfdir}/selinux/targeted/contexts/customizable_types

    echo "\
# busybox aliases
# quickly match up the busybox built-in tree to the base filesystem tree
/usr/lib/busybox/bin /usr/bin
/usr/lib/busybox/sbin /usr/sbin
/usr/lib/busybox/usr /usr
" >> ${D}/${sysconfdir}/selinux/targeted/contexts/files/file_contexts.subs_dist

    # install policy headers
    oe_runmake 'DESTDIR=${D}' 'prefix=${D}${prefix}' install-headers
}

install_config() {
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

installCmds() {
    prepare_policy_store
    mkdir -p ${D}/${sysconfdir}/selinux/${POLICY_NAME}/logins
    touch ${D}/${sysconfdir}/selinux/${POLICY_NAME}/contexts/files/file_contexts.subs
    install -m0644 ${WORKDIR}/selinux-policy/securetty_types-targeted ${D}/${sysconfdir}/selinux/targeted/contexts/securetty_types
    install -m0644 ${WORKDIR}/selinux-policy/file_contexts.subs_dist ${D}/${sysconfdir}/selinux/targeted/contexts/files
    install -m0644 ${WORKDIR}/selinux-policy/setrans-targeted.conf ${D}/${sysconfdir}/selinux/targeted/setrans.conf
    install -m0644 ${WORKDIR}/selinux-policy/customizable_types ${D}/${sysconfdir}/selinux/targeted/contexts/customizable_types
    touch ${D}/${sysconfdir}/selinux/${POLICY_NAME}/contexts/files/file_contexts.bin
    touch ${D}/${sysconfdir}/selinux/${POLICY_NAME}/contexts/files/file_contexts.local
    touch ${D}/${sysconfdir}/selinux/${POLICY_NAME}/contexts/files/file_contexts.local.bin
    cp ${WORKDIR}/selinux-policy/booleans.subs_dist ${D}/${sysconfdir}/selinux/${POLICY_NAME}
    rebuild_policy
}

do_install() {
    installCmds
    install_misc_files
    install_config
}

# Don't increment compilation because compile often fail if unused policy files are left in pw server
do_fetch[nostamp] = "1"
do_patch[nostamp] = "1"
do_compile[nostamp] = "1"
