DEPENDS += "gbm gbm-headers \
            display-commonsys-intf-linux \
            libion libsync \
            ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'libuhab', '', d)} \
            libcutils \
            linux-msm-headers \
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
