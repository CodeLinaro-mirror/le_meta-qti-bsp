DEPENDS += "gbm gbm-headers \
            ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'libuhab', '', d)} \
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

do_install:append() {
    # expose weston protocol to /usr/share/weston as video may use it
    install ${WORKDIR}/graphics/weston/protocol/*.xml ${D}${datadir}/weston
    # expose some static libraries on which sdm-backend depends
    install -m 0644 ${WORKDIR}/build/libweston/liblibinput-backend.a ${D}${libdir}/
    install -m 0644 ${WORKDIR}/build/libweston/libsession-helper.a ${D}${libdir}/
    install -m 0644 ${WORKDIR}/build/libweston/backend-drm/libbacklight.a ${D}${libdir}/
}

RRECOMMENDS_${PN}:remove = "weston-init"
