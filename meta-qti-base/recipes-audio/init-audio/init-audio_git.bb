SUMMARY = "Audio Initialization Scripts"
DESCRIPTION = "init_qcom_audio (init_audio.service) sends command to kernel to boot adsp. \
audio_early.sh loads audio kernel modules, boots adsp and plays audio early chime. \
audio.sh boots adsp and plays audio early chime."
HOMEPAGE = "https://www.codeaurora.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://NOTICE;md5=31d831381767a5740249540fe63ea013"
PR = "r5"

SRC_URI = "file://NOTICE"
SRC_URI_append = " file://init_qcom_audio"
SRC_URI_append = " file://init_audio.service"
SRC_URI_append = " file://init_audio_early.service"
SRC_URI_append = " file://audio.sh"
SRC_URI_append = " file://audio_early.sh"
SRC_URI_append = " file://msm-audio-node.rules"

S = "${WORKDIR}"

inherit update-rc.d systemd

do_compile[noexec] = "1"

do_install() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -m 0644 ${S}/msm-audio-node.rules -D ${D}${sysconfdir}/udev/rules.d/msm-audio-node.rules
        if ${@bb.utils.contains('DISTRO_FEATURES', 'early_init', 'true', 'false', d)}; then
            if ${@bb.utils.contains('DISTRO_FEATURES', 'early_userspace', 'true', 'false', d)}; then
                install -m 0755 ${S}/audio_early.sh -D ${D}${sbindir}/audio.sh
            else
                install -m 0644 ${S}/init_audio_early.service -D ${D}${systemd_unitdir}/system/init_audio.service
                install -m 0755 ${S}/audio.sh -D ${D}${sbindir}/audio.sh
            fi
        else
            install -m 0644 ${S}/init_audio.service -D ${D}${systemd_unitdir}/system/init_audio.service
        fi
        install -d ${D}/${systemd_unitdir}/system/sysinit.target.wants
        ln -sf ${systemd_unitdir}/system/init_audio.service ${D}${systemd_unitdir}/system/sysinit.target.wants/init_audio.service
    else
        install -m 0755 ${S}/init_qcom_audio -D ${D}${sysconfdir}/init.d/init_qcom_audio
    fi
}

FILES_${PN} += "${systemd_unitdir}/system/*"

INITSCRIPT_NAME = "init_qcom_audio"
INITSCRIPT_PARAMS = "start 99 2 3 4 5 . stop 1 0 1 6 ."
