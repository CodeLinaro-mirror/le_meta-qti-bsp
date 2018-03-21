PACKAGE_ARCH = "${MACHINE_ARCH}"

#FILESEXTRAPATHS_append := ":${THISDIR}/${PN}"

inherit systemd 

python do_getpatches() {
    import os

    cmd = "wget https://source.codeaurora.org/quic/ype/external/yoctoproject.org/poky/plain/meta/recipes-multimedia/pulseaudio/pulseaudio/0001-Revert-module-switch-on-port-available-Route-to-pref.patch?h=yocto/krogoth -O ${WORKSPACE}/poky/meta-qti-bsp/recipes-multimedia/pulseaudio/pulseaudio/0001-Revert-module-switch-on-port-available-Route-to-pref.patch"
    os.system(cmd)
    cmd = "wget https://source.codeaurora.org/quic/ype/external/yoctoproject.org/poky/plain/meta/recipes-multimedia/pulseaudio/pulseaudio/0001-bluetooth-don-t-create-the-HSP-HFP-profile-twice.patch?h=yocto/krogoth -O ${WORKSPACE}/poky/meta-qti-bsp/recipes-multimedia/pulseaudio/pulseaudio/0001-bluetooth-don-t-create-the-HSP-HFP-profile-twice.patch"
    os.system(cmd)
    cmd = "wget https://source.codeaurora.org/quic/ype/external/yoctoproject.org/poky/plain/meta/recipes-multimedia/pulseaudio/pulseaudio/0001-card-add-pa_card_profile.ports.patch?h=yocto/krogoth -O ${WORKSPACE}/poky/meta-qti-bsp/recipes-multimedia/pulseaudio/pulseaudio/0001-card-add-pa_card_profile.ports.patch"
    os.system(cmd)
    cmd = "wget https://source.codeaurora.org/quic/ype/external/yoctoproject.org/poky/plain/meta/recipes-multimedia/pulseaudio/pulseaudio/0001-client-conf-Add-allow-autospawn-for-root.patch?h=yocto/krogoth -O ${WORKSPACE}/poky/meta-qti-bsp/recipes-multimedia/pulseaudio/pulseaudio/0001-client-conf-Add-allow-autospawn-for-root.patch"
    os.system(cmd)
    cmd = "wget https://source.codeaurora.org/quic/ype/external/yoctoproject.org/poky/plain/meta/recipes-multimedia/pulseaudio/pulseaudio/0001-padsp-Make-it-compile-on-musl.patch?h=yocto/krogoth -O ${WORKSPACE}/poky/meta-qti-bsp/recipes-multimedia/pulseaudio/pulseaudio/0001-padsp-Make-it-compile-on-musl.patch"
    os.system(cmd)
    cmd = "wget https://source.codeaurora.org/quic/ype/external/yoctoproject.org/poky/plain/meta/recipes-multimedia/pulseaudio/pulseaudio/0002-alsa-bluetooth-fail-if-user-requested-profile-doesn-.patch?h=yocto/krogoth -O ${WORKSPACE}/poky/meta-qti-bsp/recipes-multimedia/pulseaudio/pulseaudio/0002-alsa-bluetooth-fail-if-user-requested-profile-doesn-.patch"
    os.system(cmd)
    cmd = "wget https://source.codeaurora.org/quic/ype/external/yoctoproject.org/poky/plain/meta/recipes-multimedia/pulseaudio/pulseaudio/0003-card-move-profile-selection-after-pa_card_new.patch?h=yocto/krogoth -O ${WORKSPACE}/poky/meta-qti-bsp/recipes-multimedia/pulseaudio/pulseaudio/0003-card-move-profile-selection-after-pa_card_new.patch"
    os.system(cmd)
    cmd = "wget https://source.codeaurora.org/quic/ype/external/yoctoproject.org/poky/plain/meta/recipes-multimedia/pulseaudio/pulseaudio/0004-alsa-set-availability-for-some-unavailable-profiles.patch?h=yocto/krogoth -O ${WORKSPACE}/poky/meta-qti-bsp/recipes-multimedia/pulseaudio/pulseaudio/0004-alsa-set-availability-for-some-unavailable-profiles.patch"
    os.system(cmd)
    cmd = "wget https://source.codeaurora.org/quic/ype/external/yoctoproject.org/poky/plain/meta/recipes-multimedia/pulseaudio/pulseaudio/volatiles.04_pulse?h=yocto/krogoth -O ${WORKSPACE}/poky/meta-qti-bsp/recipes-multimedia/pulseaudio/pulseaudio/volatiles.04_pulse"
    os.system(cmd)
}
addtask getpatches before do_fetch

LICENSE = "GPLv2+ & LGPLv2.1"
LIC_FILES_CHKSUM = "file://GPL;md5=4325afd396febcb659c36b49533135d4 \
                    file://LGPL;md5=2d5025d4aa3495befef8f17206a5b0a1 \
                    file://src/pulsecore/resampler.h;beginline=4;endline=21;md5=09794012ae16912c0270f3280cc8ff84"

FILESEXTRAPATHS_prepend := "${THISDIR}/pulseaudio:"
FILESEXTRAPATHS_prepend := "${TOPDIR}/../../meta-agl/meta-ivi-common/recipes-multimedia/pulseaudio/pulseaudio-10.0:"

PACKAGECONFIG ??= "${@bb.utils.contains('DISTRO_FEATURES', 'zeroconf', 'avahi', '', d)} \
                   ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'x11', '', d)} \
                   ${@bb.utils.contains('DISTRO_FEATURES', '3g', 'ofono', '', d)} \
                   ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd', '', d)} \
                   "
BP = "pulseaudio-8.0"
PV = "8.0"
SRC_URI = "http://freedesktop.org/software/pulseaudio/releases/${BP}.tar.xz \
           file://0001-padsp-Make-it-compile-on-musl.patch \
           file://0001-client-conf-Add-allow-autospawn-for-root.patch \
           file://volatiles.04_pulse \
           file://0001-card-add-pa_card_profile.ports.patch \
           file://0002-alsa-bluetooth-fail-if-user-requested-profile-doesn-.patch \
           file://0003-card-move-profile-selection-after-pa_card_new.patch \
           file://0004-alsa-set-availability-for-some-unavailable-profiles.patch \
           file://0001-Revert-module-switch-on-port-available-Route-to-pref.patch \
           file://0001-bluetooth-don-t-create-the-HSP-HFP-profile-twice.patch \
"
SRC_URI[md5sum] = "8678442ba0bb4b4c33ac6f62542962df"
SRC_URI[sha256sum] = "690eefe28633466cfd1ab9d85ebfa9376f6b622deec6bfee5091ac9737cd1989"

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

DEPENDS += "json-c gdbm"

EXTRA_OECONF += "--disable-xen"
EXTRA_OECONF_remove += "--disable-adrian-aec"

PACKAGECONFIG ??= "${@bb.utils.contains('DISTRO_FEATURES', 'bluetooth', '${BLUEZ}', '', d)} \
                   ${@bb.utils.contains('DISTRO_FEATURES', 'zeroconf', 'avahi', '', d)} \
                   ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'x11', '', d)} \
                   ${@bb.utils.contains('DISTRO_FEATURES', '3g', 'ofono', '', d)} \
                   ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd', '', d)} \
                   dbus \
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

do_install_append() {
       # Install pulseaudio systemd service
       if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
              install -m 644 -p -D ${WORKDIR}/build/src/pulseaudio.service ${D}${systemd_user_unitdir}/pulseaudio.service
              install -m 644 -p -D ${WORKDIR}/pulseaudio-${PV}/src/daemon/systemd/user/pulseaudio.socket ${D}${systemd_user_unitdir}/pulseaudio.socket

              # Execute these manually on behalf of systemctl script (from systemd-systemctl-native.bb)
              # because it does not support systemd's user mode.
              install -d ${D}${systemd_user_unitdir}/sockets.target.wants/
              ln -sf ${systemd_user_unitdir}/pulseaudio.socket ${D}${systemd_user_unitdir}/sockets.target.wants/

              install -d ${D}${systemd_user_unitdir}/default.target.wants/
              ln -sf ${systemd_user_unitdir}/pulseaudio.service ${D}${systemd_user_unitdir}/default.target.wants/
       fi
       mkdir -p ${D}/${bindir}
       install -m 755 -p -D ${WORKDIR}/build/src/.libs/pacat ${D}/${bindir}/
}
