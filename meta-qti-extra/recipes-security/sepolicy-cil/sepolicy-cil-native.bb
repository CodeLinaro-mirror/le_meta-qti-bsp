SUMMARY = "Build sepolicy cil files"
DESCRIPTION = "Fetch sepolicy te files from la code repository and compile them into cil format"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "PD & BSD-3-Clause & GPL-2.0 & LGPL-2.1"
LIC_FILES_CHKSUM = "file://${WORKDIR}/system-sepolicy/NOTICE;md5=6553f4761e321f456cf3df6e01369579 \
                    file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9 \
                    file://${WORKDIR}/selinux/NOTICE;md5=1ee45b8edf86ad26da8ae8b7f497e69e \
                    file://${WORKDIR}/selinux/libsepol/LICENSE;md5=a6f89e2100d9b6cdffcea4f398e37343"

DEPENDS += "checkpolicy-native glib-2.0-native"

#===================================================================== QSSI15 and Vendor13 selinux gits ===============================================================================
# Mon Jun  9 15:47:58 CST 2025
# updated to AU_LINUX_ANDROID_LA_AU.QSSI.15.0.0.R1.11.00.00.1198.045

CODESER = "git://git.codelinaro.org/clo/la"
SRC_URI = "${CODESER}/platform/system/sepolicy.git;protocol=https;branch=automotive-aosp-va.lnx.15.0.r1-rel;name=system-sepolicy;destsuffix=system-sepolicy \
           ${CODESER}/device/qcom/sepolicy.git;protocol=https;branch=auto-sepolicy-sysintf.lnx.15.0.r1-rel;name=device-sepolicy;destsuffix=device-sepolicy \
           ${CODESER}/platform/external/selinux.git;protocol=https;branch=aosp.lnx.15.2.r1-rel;name=selinux;destsuffix=selinux \
           ${CODESER}/platform/packages/services/Car.git;protocol=https;branch=automotive-aosp-va.lnx.15.0.r1-rel;name=packages-sepolicy;destsuffix=packages-sepolicy \
           ${CODESER}/device/qcom/sepolicy_vndr.git;protocol=https;branch=sepolicy.vndr.lnx.13.0.c1;name=device-sepolicyvndr;destsuffix=device-sepolicyvndr \
           file://Makefile \
           "
SRCREV_FORMAT = "system-sepolicy"
SRCREV_system-sepolicy = "92e8e684261dabdff858124be40283ff834cee5b"
SRCREV_device-sepolicy = "c54892dd3f54f2b9a2536787ec6e6d3367de4abb"
SRCREV_selinux = "2d543d20722ada2425b5bdab9d0d1d29470e7bba"
SRCREV_packages-sepolicy = "6868770c69f9ce6d7358704f510460941a4e60ac"
SRCREV_device-sepolicyvndr = "9b6a08e34c27c627cb3e993dba410829f8b2467a"
#=======================================================================================================================================================================================

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

do_configure() {
    cp -rf ${WORKDIR}/selinux/libsepol --no-preserve=ownership ${B}
    install -m 0755 -d ${B}/sepolicy-cil
    install -m 0755 ${WORKDIR}/system-sepolicy/tools/version_policy.c ${B}/sepolicy-cil
    install -m 0755 ${WORKDIR}/Makefile ${B}/sepolicy-cil
    cp -rf ${B}/libsepol/include/sepol --no-preserve=ownership ${B}/sepolicy-cil
    cp -rf ${B}/libsepol/cil/include/cil --no-preserve=ownership ${B}/sepolicy-cil
    sed -i '1iattribute vendor_service;' "${WORKDIR}/device-sepolicyvndr/generic/vendor/common/service.te"
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

