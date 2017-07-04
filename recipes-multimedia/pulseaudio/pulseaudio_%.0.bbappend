PACKAGE_ARCH = "${MACHINE_ARCH}"

FILESEXTRAPATHS_prepend := "${THISDIR}/pulseaudio:"
FILESEXTRAPATHS_prepend := "${TOPDIR}/../../meta-agl/meta-ivi-common/recipes-multimedia/pulseaudio/pulseaudio-9.0:"

PACKAGECONFIG ??= "${@bb.utils.contains('DISTRO_FEATURES', 'zeroconf', 'avahi', '', d)} \
                   ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'x11', '', d)} \
                   ${@bb.utils.contains('DISTRO_FEATURES', '3g', 'ofono', '', d)} \
                   ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd', '', d)} \
                   "

SRC_URI += " \
        file://0001-install-files-for-a-module-development.patch \
        file://0002-volume-ramp-additions-to-the-low-level-infra.patch \
        file://0003-volume-ramp-adding-volume-ramping-to-sink-input.patch \
        file://0004-sink-input-Code-cleanup-regarding-volume-ramping.patch \
        file://0005-sink-input-volume-Add-support-for-volume-ramp-factor.patch \
        file://0006-sink-input-Remove-pa_sink_input_set_volume_ramp.patch;apply=no \
"

SRC_URI += " \
             file://0001-disable-timer-based-scheduling.patch \
             file://0002-default.pa-Load-acdb-and-codec-control-modules.patch \
             file://0003-default.pa-Load-agl-audio-plugin-module.patch \
             file://0004-udev-Add-rules-for-QTI-MSM8996.patch \
             file://0006-Support-PulseAudio-Client-API-for-Module-Codec-Control.patch \
             file://0007-stream-event-extension.patch \
             file://0008-Pulseaudio-service-need-to-wait-for-sound-card-ready.patch \
           "

RDEPENDS_pulseaudio-server += "\
         ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '\
                 pulseaudio-module-systemd-login\
         ', '', d)}"

RDEPENDS_pulseaudio-server += "pulseaudio-module-null-source"

# Move the symlinks to the pulseaudio-server package to make sure pulseaudio always be installed
FILES_${PN}-server += " \
        ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '${systemd_user_unitdir}/pulseaudio.socket', '', d)} \
        ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '/home/root/.config/systemd/user/sockets.target.wants/pulseaudio.socket', '', d)} \
        ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '${systemd_user_unitdir}/sockets.target.wants/pulseaudio.socket', '', d)} \
        ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '${systemd_user_unitdir}/pulseaudio.service', '', d)} \
        ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '/home/root/.config/systemd/user/default.target.wants/pulseaudio.service', '', d)} \
        ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '${systemd_user_unitdir}/default.target.wants/pulseaudio.service', '', d)} \
 "

PACKAGES =+ " pulseaudio-module-dev"

FILES_pulseaudio-module-dev = "${includedir}/pulsemodule/* ${libdir}/pkgconfig/pulseaudio-module-devel.pc"

