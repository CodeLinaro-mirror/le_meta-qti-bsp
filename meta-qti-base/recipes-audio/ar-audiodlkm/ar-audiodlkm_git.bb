SUMMARY = "Audio Drivers Kernel Modules for AudioReach"
DESCRIPTION = "This is the AudioReach based audio driver based on ASoC architecture, used to communicate with DSP."
HOMEPAGE = "https://www.codeaurora.org"
LICENSE = "GPL-2.0-only-WITH-Linux-syscall-note"
LIC_FILES_CHKSUM = "file://NOTICE;md5=53c09804050a00b1d27bd609c4e1fc5a"
DEPENDS += "virtual/kernel"
SRCREV = "${AUTOREV}"
PR = "r0"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/audio-kernel-ar/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/audio-kernel-ar;usehead=1"
SRC_URI_append = " file://audio_load.conf"

S = "${WORKDIR}/vendor/qcom/opensource/audio-kernel-ar"

inherit module module-sign qperf

EXTRA_OEMAKE += "TARGET_SUPPORT=${@bb.utils.contains('BASEMACHINE', 'sa81x5', 'sa8155', '${BASEMACHINE}', d)}"

do_configure() {
    cp -f ${WORKDIR}/vendor/qcom/opensource/audio-kernel-ar/Makefile.am ${WORKDIR}/vendor/qcom/opensource/audio-kernel-ar/Makefile
}

do_install_append() {
    install -d -p ${D}${includedir}/audio-kernel-ar/audio/linux
    install -d -p ${D}${includedir}/audio-kernel-ar/audio/linux/mfd/wcd9xxx
    install -d -p ${D}${includedir}/audio-kernel-ar/audio/sound

    process_headers "${S}/include/uapi/audio/linux" "${D}${includedir}/audio-kernel-ar/audio/linux"
    process_headers "${S}/include/uapi/audio/linux/mfd/wcd9xxx" "${D}${includedir}/audio-kernel-ar/audio/linux/mfd/wcd9xxx"
    process_headers "${S}/include/uapi/audio/sound" "${D}${includedir}/audio-kernel-ar/audio/sound"

    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -m 0755 ${WORKDIR}/audio_load.conf -D ${D}${sysconfdir}/modules-load.d/audio_load.conf
    else
        install -m 0755 ${WORKDIR}/audio_load.conf -D ${D}${sysconfdir}/modules/audio_load.conf
    fi

    install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra
    for i in $(find ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/. -name "*.ko"); do
        mv ${i} ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/
    done

    rm -fr ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/asoc
    rm -fr ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/dsp
    rm -fr ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/ipc
    rm -fr ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/soc
}

process_headers() {
    cd ${STAGING_KERNEL_BUILDDIR}
    for name in $(ls $1/*.h); do
        ${STAGING_KERNEL_DIR}/scripts/headers_install.sh $1/$(basename $name) $2/$(basename $name)
    done
}

# The inherit of module.bbclass will automatically name module packages with
# kernel-module-" prefix as required by the oe-core build environment. Also it
# replaces '_' with '-' in the module name.
RPROVIDES_${PN} += "${@'kernel-module-adsp-loader-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@'kernel-module-audio-pkt-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@' kernel-module-audio-prm-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@' kernel-module-audpkt-ion-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@' kernel-module-gpr-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@' kernel-module-spf-machine-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@' kernel-module-platform-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@' kernel-module-q6-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@' kernel-module-q6-notifier-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@' kernel-module-snd-event-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@' kernel-module-spf-core-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES_${PN} += "${@' kernel-module-stub-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"


# install subdirectories under ${sysconfdir}
FILES_${PN} += "${sysconfdir}/*"
FILES_${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/*"

KERNEL_CC += "-Wno-error=maybe-uninitialized"
