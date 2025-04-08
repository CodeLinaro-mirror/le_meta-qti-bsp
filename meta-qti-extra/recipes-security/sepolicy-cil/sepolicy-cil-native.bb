SUMMARY = "Build sepolicy cil files"
DESCRIPTION = "Fetch sepolicy te files from la code repository and compile them into cil format"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "PD & BSD-3-Clause & GPL-2.0 & LGPL-2.1"
LIC_FILES_CHKSUM = "file://${WORKDIR}/system-sepolicy/NOTICE;md5=6553f4761e321f456cf3df6e01369579 \
                    file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9 \
                    file://${WORKDIR}/selinux/NOTICE;md5=1ee45b8edf86ad26da8ae8b7f497e69e \
                    file://${WORKDIR}/selinux/libsepol/LICENSE;md5=a6f89e2100d9b6cdffcea4f398e37343"

DEPENDS += "checkpolicy-native glib-2.0-native"

SRC_URI = "git://git.codelinaro.org/clo/la/platform/system/sepolicy.git;protocol=https;branch=automotive-aosp-va.lnx.15.0.r1-rel;name=system-sepolicy;destsuffix=system-sepolicy \
           git://git.codelinaro.org/clo/la/device/qcom/sepolicy.git;protocol=https;branch=auto-sepolicy-sysintf.lnx.15.0.r1-rel;name=device-sepolicy;destsuffix=device-sepolicy \
           git://git.codelinaro.org/clo/la/platform/external/selinux.git;protocol=https;branch=aosp.lnx.15.0.r5-rel;name=selinux;destsuffix=selinux \
           git://git.codelinaro.org/clo/la/platform/packages/services/Car.git;protocol=https;branch=automotive-aosp-va.lnx.15.0.r1-rel;name=packages-sepolicy;destsuffix=packages-sepolicy \
           git://git.codelinaro.org/clo/la/device/qcom/sepolicy_vndr.git;protocol=https;branch=sepolicy.vndr.lnx.13.0.r29-rel;name=device-sepolicyvndr;destsuffix=device-sepolicyvndr \
           file://Makefile \
           "
SRCREV_FORMAT = "system-sepolicy"
SRCREV_system-sepolicy = "d47885e3487fb6fcee5c76452ed71b903e17d21e"
SRCREV_device-sepolicy = "453886de8df478af55e1ca61ec50a04d383b87ac"
SRCREV_selinux = "8d5c7f06d074449dbb3dad7fcb531ec02ff0c0d1"
SRCREV_packages-sepolicy = "2077c72ba04c06e39daf0cb3d626ed9c5d94faf1"
SRCREV_device-sepolicyvndr = "7701c5e1f4113e43c37e4ec8df6f9945a285624a"

inherit native


EXTRA_OEMAKE = "'VERSION_POLICY_INCLUDES=-I${B}/sepolicy-cil'\
                'VERSION_POLICY_LIB_PATH=-L${B}/libsepol/src'\
                'VERSION_POLICY_LIB_NAME=${B}/libsepol/src/libsepol.a'\
                'M4=${STAGING_BINDIR_NATIVE}/m4'\
                'CHECKPOLICY=${STAGING_BINDIR_NATIVE}/checkpolicy'\
                'SYSTEM_SEPOLICY=${WORKDIR}/system-sepolicy'\
                'DEVICE_SEPOLICY=${WORKDIR}/device-sepolicy'\
                'PACKAGES_POLICY=${WORKDIR}/packages-sepolicy'\
                'DEVICE_SEPOLICYVNDR=${WORKDIR}/device-sepolicyvndr'\
                'TARGET_BUILD_VARIANT=${@bb.utils.contains_any('VARIANT', 'perf user', 'user', 'userdebug', d)}'\
                'CIL_OUTPUT_DIR=${B}/sepolicy-cil' \
                'TARGET_INSTALL_DIR=${D}${datadir}'"

insert_include() {
    te_file="$2"
    include_file="$1"

    if ! grep -q "include(\`${include_file}')" "${te_file}"; then
        sed -i '1iinclude('${include_file}')' ${te_file}
        bbnote "Added include(\`${include_file}') to ${te_file}"
    else
        bbnote "include(\`${include_file}') already exists in ${te_file}"
    fi
}

do_configure() {
    cp -rf ${WORKDIR}/selinux/libsepol --no-preserve=ownership ${B}
    install -m 0755 -d ${B}/sepolicy-cil
    install -m 0755 ${WORKDIR}/system-sepolicy/tools/version_policy.c ${B}/sepolicy-cil
    install -m 0755 ${WORKDIR}/Makefile ${B}/sepolicy-cil
    cp -rf ${B}/libsepol/include/sepol --no-preserve=ownership ${B}/sepolicy-cil
    cp -rf ${B}/libsepol/cil/include/cil --no-preserve=ownership ${B}/sepolicy-cil
    insert_include "${WORKDIR}/system-sepolicy/flagging/flagging_macros" "${WORKDIR}/system-sepolicy/private/access_vectors"
    insert_include "${WORKDIR}/packages-sepolicy/cpp/watchdog/sepolicy/public/attributes" "${WORKDIR}/packages-sepolicy/cpp/watchdog/sepolicy/public/te_macros"
    insert_include "${WORKDIR}/packages-sepolicy/cpp/watchdog/sepolicy/public/te_macros" "${WORKDIR}/packages-sepolicy/car_product/sepolicy/private/carservice_app.te"
    insert_include "${WORKDIR}/packages-sepolicy/cpp/watchdog/sepolicy/public/te_macros" "${WORKDIR}/packages-sepolicy/cpp/powerpolicy/sepolicy/public/attributes"
    sed -i '1iattribute carpowerpolicycallback_domain;' ${WORKDIR}/packages-sepolicy/cpp/watchdog/sepolicy/public/te_macros
    insert_include "${WORKDIR}/device-sepolicyvndr/generic/vendor/common/attribute/attributes" "${WORKDIR}/device-sepolicyvndr/generic/vendor/common/cnd.te"
    insert_include "${WORKDIR}/packages-sepolicy/cpp/powerpolicy/sepolicy/public/te_macros" "${WORKDIR}/device-sepolicyvndr/generic/vendor/common/hal_audiocontrol_default.te"
    insert_include "${WORKDIR}/packages-sepolicy/cpp/powerpolicy/sepolicy/public/te_macros" "${WORKDIR}/packages-sepolicy/car_product/sepolicy/private/carservice_app.te"
    sed -i '1isystem_internal_prop(carwatchdog_config_prop)' ${WORKDIR}/packages-sepolicy/cpp/watchdog/sepolicy/public/attributes
    insert_include "${WORKDIR}/system-sepolicy/microdroid/system/public/te_macros" "${WORKDIR}/packages-sepolicy/cpp/watchdog/sepolicy/public/attributes"
    sed -i '1itype carpowerpolicyd_service, service_manager_type;' ${WORKDIR}/packages-sepolicy/cpp/watchdog/sepolicy/private/property.te
    sed -i '1itype carpowerpolicyd_service, service_manager_type;' ${WORKDIR}/packages-sepolicy/cpp/watchdog/sepolicy/public/attributes
    sed -i '1iattribute carpowerpolicycallback_domain;' ${WORKDIR}/device-sepolicyvndr/generic/vendor/common/hal_audiocontrol_default.te
    sed -i '31iattribute vendor_service;' ${WORKDIR}/device-sepolicyvndr/generic/vendor/common/service.te
    insert_include "${WORKDIR}/packages-sepolicy/cpp/powerpolicy/sepolicy/public/carpowerpolicy.te" "${WORKDIR}/device-sepolicyvndr/generic/vendor/common/carpowerpolicy.te"
    insert_include "${WORKDIR}/packages-sepolicy/cpp/watchdog/sepolicy/public/attributes" "${WORKDIR}/packages-sepolicy/cpp/powerpolicy/sepolicy/public/carpowerpolicy.te"
    insert_include "${WORKDIR}/device-sepolicyvndr/qva/vendor/test/sysmonapp/sysmonapp_app_test.te" "${WORKDIR}/device-sepolicyvndr/generic/vendor/common/domain.te"
}

do_compile() {
    oe_runmake LDFLAGS="-L${B}/libsepol/src ${LDFLAGS}" -C ${B}/libsepol
    prepare_libsepol
    cd ${B}/sepolicy-cil
    oe_runmake -j1 -f ${B}/sepolicy-cil/Makefile
}

prepare_libsepol() {
    install -m 0644 ${B}/libsepol/src/libsepol.a ${B}/sepolicy-cil
}

do_install() {
    oe_runmake -f ${B}/sepolicy-cil/Makefile install
}

do_compile[nostamp] = "1"

B = "${WORKDIR}/build"

