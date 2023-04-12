PACKAGE_ARCH = "${MACHINE_ARCH}"


inherit systemd 

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
SOURCE_PULSEAUDIO_PATCHES = "https://git.codelinaro.org/clo/ype/external/yoctoproject.org/poky/-/raw/0271b3ab00cec06e510c04d651f77c2210bda1a3/meta/recipes-multimedia/pulseaudio/pulseaudio"

SRC_URI = "http://freedesktop.org/software/pulseaudio/releases/${BP}.tar.xz \
           ${SOURCE_PULSEAUDIO_PATCHES}/0001-padsp-Make-it-compile-on-musl.patch;downloadfilename=0001-padsp-Make-it-compile-on-musl.patch;name=patch1\
           ${SOURCE_PULSEAUDIO_PATCHES}/0001-client-conf-Add-allow-autospawn-for-root.patch;downloadfilename=0001-client-conf-Add-allow-autospawn-for-root.patch;name=patch2\
           ${SOURCE_PULSEAUDIO_PATCHES}/volatiles.04_pulse;downloadfilename=volatiles.04_pulse;name=patch3\
           ${SOURCE_PULSEAUDIO_PATCHES}/0001-card-add-pa_card_profile.ports.patch;downloadfilename=0001-card-add-pa_card_profile.ports.patch;name=patch4\
           ${SOURCE_PULSEAUDIO_PATCHES}/0002-alsa-bluetooth-fail-if-user-requested-profile-doesn-.patch;downloadfilename=0002-alsa-bluetooth-fail-if-user-requested-profile-doesn-.patch;name=patch5\
           ${SOURCE_PULSEAUDIO_PATCHES}/0003-card-move-profile-selection-after-pa_card_new.patch;downloadfilename=0003-card-move-profile-selection-after-pa_card_new.patch;name=patch6\
           ${SOURCE_PULSEAUDIO_PATCHES}/0004-alsa-set-availability-for-some-unavailable-profiles.patch;downloadfilename=0004-alsa-set-availability-for-some-unavailable-profiles.patch;name=patch7\
           ${SOURCE_PULSEAUDIO_PATCHES}/0001-Revert-module-switch-on-port-available-Route-to-pref.patch;downloadfilename=0001-Revert-module-switch-on-port-available-Route-to-pref.patch;name=patch8\
           ${SOURCE_PULSEAUDIO_PATCHES}/0001-bluetooth-don-t-create-the-HSP-HFP-profile-twice.patch;downloadfilename=0001-bluetooth-don-t-create-the-HSP-HFP-profile-twice.patch;name=patch9\
"
SRC_URI[patch1.md5sum] = "ac9d7f4c6cca22d349aaa7649d47a6e3"
SRC_URI[patch1.sha256sum] = "4c4af58f0f5f00230407f71e6ee3fa33b923feb12dfbc5ddad2075ab6a3ed234"

SRC_URI[patch2.md5sum] = "0a93eaffa1f5a833c50eeedacf38ec8e"
SRC_URI[patch2.sha256sum] = "5e8fc1b273fec9d343e2e1173cc32b67de7a003db6d2e083976d46a71df736a9"

SRC_URI[patch3.md5sum] = "ff24d150a47395ca134cff38692c0d5c"
SRC_URI[patch3.sha256sum] = "0aa90530c58f73ac24be9f86b2cc947537fad691cc171a05f8949a4488c16eca"

SRC_URI[patch4.md5sum] = "8b585eb4216726dd326911ecdb160a95"
SRC_URI[patch4.sha256sum] = "8f6d1e2bfe6463133a7c5fea1713d6d30b0bb2619e3f2a1d2ce1a7b57427de22"

SRC_URI[patch5.md5sum] = "e1b0de69081d9f41e44417a485fc8eca"
SRC_URI[patch5.sha256sum] = "1f6d72a3e9532b13e03841978ccc1422802d1cdd6defbc9017f787b2daff2da6"

SRC_URI[patch6.md5sum] = "ecd2e4f3f1cfaf30cb228265793c02bd"
SRC_URI[patch6.sha256sum] = "e58f88ab0dc73b463bd9abb92b578b7185a5de0c6dcde9b2baa3beede89565e9"

RC_URI[patch7.md5sum] = "141dc064fd488a4a0e2029c19ce0c8a6"
SRC_URI[patch7.sha256sum] = "10f4db8688a8f4315a56403e8a01ba6c253feae5dc292b67c73f4209e845e92d"

RC_URI[patch8.md5sum] = "11e0bd5d2f37ed86b32b6695e16f9356"
SRC_URI[patch8.sha256sum] = "d92c57fdf45e001b8a63ec3b9b60841ff7f808f79103fa56aa081439d38d1e5b"

RC_URI[patch9.md5sum] = "6a606bcbadd86ec39674a66a7f8d86a0"
SRC_URI[patch9.sha256sum] = "54c03434d73a1288cf6a3992ad63abb8796b0900067586c64093c022df86b62c"

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
