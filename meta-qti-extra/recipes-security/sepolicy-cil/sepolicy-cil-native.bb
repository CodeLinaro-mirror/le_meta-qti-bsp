SUMMARY = "Build sepolicy cil files"
DESCRIPTION = "Fetch sepolicy te files from la code repository and compile them into cil format"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "PD & BSD-3-Clause & GPL-2.0 & LGPL-2.1"
LIC_FILES_CHKSUM = "file://${WORKDIR}/system-sepolicy/NOTICE;md5=6553f4761e321f456cf3df6e01369579 \
                    file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9 \
                    file://${WORKDIR}/selinux/NOTICE;md5=1ee45b8edf86ad26da8ae8b7f497e69e \
                    file://${WORKDIR}/selinux/libsepol/COPYING;md5=a6f89e2100d9b6cdffcea4f398e37343"

DEPENDS += "checkpolicy-native glib-2.0-native"

SRC_URI = "git://git.codelinaro.org/clo/la/platform/system/sepolicy.git;protocol=https;branch=aosp.lnx.3.0.r7-rel;name=system-sepolicy;destsuffix=system-sepolicy \
           git://git.codelinaro.org/clo/la/device/qcom/sepolicy.git;protocol=https;branch=sepolicy.lnx.6.0.r28-rel;name=device-sepolicy;destsuffix=device-sepolicy \
           git://git.codelinaro.org/clo/la/platform/external/selinux.git;protocol=https;branch=aosp.lnx.3.0.r7-rel;name=selinux;destsuffix=selinux \
           git://git.codelinaro.org/clo/la/platform/packages/services/Car.git;protocol=https;branch=aosp.lnx.3.0.r7-rel;name=packages-sepolicy;destsuffix=packages-sepolicy \
           git://git.codelinaro.org/clo/la/device/qcom/sepolicy_vndr.git;protocol=https;branch=sepolicy.vndr.lnx.1.0.r31-rel;name=device-sepolicyvndr;destsuffix=device-sepolicyvndr \
           file://Makefile"
SRCREV_FORMAT = "system-sepolicy"
SRCREV_system-sepolicy = "d29c7fc4894839d6e30beea554579ee63a1be7bb"
SRCREV_device-sepolicy = "9b541cb1429e6e01bb0318b2a2c787cf65a06aee"
SRCREV_selinux = "8340fdb45281ef1459301d7c535b38f9e9bee598"
SRCREV_packages-sepolicy = "3726467174eda12b6091277aff58c158d45ecfc2"
SRCREV_device-sepolicyvndr = "f069bd8a885fa351ff7c1692cd0a47916703db14"

SYSROOT_DIRS_NATIVE += "${target_datadir}"

inherit native

EXTRA_OEMAKE = "'VERSION_POLICY_INCLUDES=-I${B}/sepolicy-cil'\
                'VERSION_POLICY_LIB_PATH=-L${B}/sepolicy-cil'\
                'VERSION_POLICY_LIB_NAME=-lsepol'\
                'M4=${STAGING_BINDIR_NATIVE}/m4'\
                'CHECKPOLICY=${STAGING_BINDIR_NATIVE}/checkpolicy'\
                'SYSTEM_SEPOLICY=${WORKDIR}/system-sepolicy'\
                'DEVICE_SEPOLICY=${WORKDIR}/device-sepolicy'\
                'PACKAGES_POLICY=${WORKDIR}/packages-sepolicy'\
                'DEVICE_SEPOLICYVNDR=${WORKDIR}/device-sepolicyvndr'"

do_configure() {
    cp -rf ${WORKDIR}/selinux/libsepol --no-preserve=ownership ${B}
    install -m 0755 -d ${B}/sepolicy-cil
    install -m 0755 ${WORKDIR}/system-sepolicy/tools/version_policy.c ${B}/sepolicy-cil
    install -m 0755 ${WORKDIR}/Makefile ${B}/sepolicy-cil
    cp -rf ${B}/libsepol/include/sepol --no-preserve=ownership ${B}/sepolicy-cil
    cp -rf ${B}/libsepol/cil/include/cil --no-preserve=ownership ${B}/sepolicy-cil
}

do_compile() {
    oe_runmake -C ${B}/libsepol
    prepare_libsepol
    cd ${B}/sepolicy-cil
    oe_runmake -f ${B}/sepolicy-cil/Makefile
}

prepare_libsepol() {
    install -m 0644 ${B}/libsepol/src/libsepol.a ${B}/sepolicy-cil
}

do_install() {
    install -d ${D}${datadir}/android_cils/product
    install -d ${D}${datadir}/android_cils/system
    install -d ${D}${datadir}/android_cils/system_ext
    install -d ${D}${datadir}/android_cils/vendor
    install -m 0644 ${B}/sepolicy-cil/product_sepolicy.cil ${D}${datadir}/android_cils/product
    install -m 0644 ${B}/sepolicy-cil/product_mapping_file.cil ${D}${datadir}/android_cils/product/30.0.cil
    install -m 0644 ${B}/sepolicy-cil/plat_sepolicy.cil ${D}${datadir}/android_cils/system
    install -m 0644 ${B}/sepolicy-cil/plat_mapping_file.cil ${D}${datadir}/android_cils/system/30.0.cil
    install -m 0644 ${B}/sepolicy-cil/system_ext_sepolicy.cil ${D}${datadir}/android_cils/system_ext
    install -m 0644 ${B}/sepolicy-cil/system_ext_mapping_file.cil ${D}${datadir}/android_cils/system_ext/30.0.cil
    install -m 0644 ${B}/sepolicy-cil/plat_pub_versioned.cil ${D}${datadir}/android_cils/vendor
    install -m 0644 ${B}/sepolicy-cil/vendor_sepolicy.cil ${D}${datadir}/android_cils/vendor
}

B = "${WORKDIR}/build"

