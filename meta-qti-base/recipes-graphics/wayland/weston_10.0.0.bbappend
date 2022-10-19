DEPENDS += "gbm gbm-headers \
            display-commonsys-intf-linux \
            ${@bb.utils.contains('PREFERRED_VERSION_linux-msm', '5.4', 'libion', '', d)} \
            libsync \
            ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'libuhab', '', d)} \
            libcutils \
            linux-msm-headers \
            weston-sdm-extension-headers \
"

REQUIRED_DISTRO_FEATURES:remove = "opengl"

FILESEXTRAPATHS:append := " :${THISDIR}/weston/"
SRC_URI = "${PATH_TO_REPO}/graphics/weston/.git;protocol=${PROTO};destsuffix=graphics/weston;usehead=1 \
           file://weston.png \
           file://weston.desktop \
           file://xwayland.weston-start \
           file://systemd-notify.weston-start \
"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/graphics/weston"

UPSTREAM_CHECK_URI:remove = "https://wayland.freedesktop.org/releases.html"

# Disable systemd-logind D-Bus protocol
PACKAGECONFIG:remove = "systemd"

# Enable support for the deprecated wl_shell interface
# This is a workaround for outdated GFX Benchmark tool
PACKAGECONFIG:append = " wl-shell"
PACKAGECONFIG[wl-shell] = "-Ddeprecated-wl-shell=true,-Ddeprecated-wl-shell=false"

RRECOMMENDS_${PN}:remove = "weston-init"

FILES:${PN}-dev = "${includedir} \
                ${libdir}/pkgconfig ${datadir}/pkgconfig \
                ${libdir}/${BPN}/libexec_weston.so \
                ${libdir}/libweston-10.so \
                ${libdir}/libweston-desktop-10.so"
# Some libraries on which sdm-backend depends
FILES:libweston-${WESTON_MAJOR_VERSION} += "${libdir}/libsession-helper.so \
                                            ${libdir}/liblibinput-backend.so \
                                            ${libdir}/libbacklight.so"
