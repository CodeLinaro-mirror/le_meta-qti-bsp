inherit native utils

DESCRIPTION = "Tool from Android to validate and merge dtbo files before creating dtbo.img"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

BBCLASSEXTEND = "native"

FILESEXTRAPATHS:prepend := "${KERNEL_PREBUILT_PATH}:"
FILESEXTRAPATHS:prepend := "${KERNEL_PLATFORM_PATH}:"

SRC_URI   =  "file://host \
            file://build/ \
            file://prebuilts/kernel-build-tools/linux-x86/"

S = "${WORKDIR}/host"

INHIBIT_SYSROOT_STRIP = "1"
do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    # Install  bin
    install -d ${D}${bindir}/merge_dtbs/
    if [ -e ${S}/bin/merge_dtbs.py ]; then
        install -m 0755 ${S}/bin/merge_dtbs.py ${D}${bindir}/merge_dtbs/
    else
        install -m 0755 ${S}/../build/kernel/android/merge_dtbs.py ${D}${bindir}/merge_dtbs/
    fi
    if [ -e ${S}/bin/ufdt_apply_overlay ]; then
        install -m 0755 ${S}/bin/ufdt_apply_overlay  ${D}${bindir}/merge_dtbs/
    else
        install -m 0755 ${S}/../prebuilts/kernel-build-tools/linux-x86/bin/ufdt_apply_overlay  ${D}${bindir}/merge_dtbs/
    fi

    install -m 0755 ${S}/bin/fdtget  ${D}${bindir}/merge_dtbs/
    install -m 0755 ${S}/bin/fdtoverlaymerge  ${D}${bindir}/merge_dtbs/
    install -m 0755 ${S}/bin/fdtdump  ${D}${bindir}/merge_dtbs/
    install -m 0755 ${S}/bin/fdtoverlay  ${D}${bindir}/merge_dtbs/
    install -m 0755 ${S}/bin/fdtput ${D}${bindir}/merge_dtbs/
    # Copy libfdt libs
    install -d ${D}${bindir}/merge_dtbs/lib/
    install -m 0644 ${S}/lib/* ${D}${bindir}/merge_dtbs/lib/

    create_wrapper ${D}${bindir}/merge_dtbs/merge_dtbs.py \
        LD_LIBRARY_PATH=${STAGING_BINDIR_NATIVE}/merge_dtbs/lib:$LD_LIBRARY_PATH \
        PATH=${STAGING_BINDIR_NATIVE}/merge_dtbs/:$PATH

}
