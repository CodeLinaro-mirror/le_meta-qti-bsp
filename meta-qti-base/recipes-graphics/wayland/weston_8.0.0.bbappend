DEPENDS += "display-hal-headers display-hal-linux display-noship-linux display-ship-linux \
            gbm gbm-headers \
            libion libsync \
            ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'libuhab', '', d)} \
            linux-msm-headers \
"

FILESEXTRAPATHS_append := " :${THISDIR}/weston/"
SRC_URI = "${PATH_TO_REPO}/graphics/weston/.git;protocol=${PROTO};destsuffix=graphics/weston;usehead=1"
#Remove community patch which is conflict with Weston SDM optimization
SRC_URI_remove = "file://0001-compositor-drm.c-Launch-without-input-devices.patch"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/graphics/weston"

inherit systemd

UPSTREAM_CHECK_URI_remove = "https://wayland.freedesktop.org/releases.html"

REQUIRED_DISTRO_FEATURES_remove = "opengl"

TARGET_CFLAGS += "-I${STAGING_INCDIR}/libdrm"
TARGET_CFLAGS += "-I${STAGING_INCDIR}/sdm"
TARGET_CFLAGS += "-I${STAGING_INCDIR}/sdm/core"
TARGET_CFLAGS += "-I${STAGING_INCDIR}/linux-msm"
TARGET_CFLAGS += "-I${STAGING_INCDIR}/linux-msm/display"
TARGET_CPPFLAGS += "-I${STAGING_INCDIR}/qcom/display"
TARGET_CPPFLAGS += "-I${STAGING_INCDIR}/sdm"
TARGET_CPPFLAGS += "-I${STAGING_INCDIR}/sdm/core"

#Overwrite Packageconfig
PACKAGECONFIG = "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'kms fbdev wayland egl clients', '', d)} \
                 ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'x11', '', d)} \
                 ${@bb.utils.contains('DISTRO_FEATURES', 'pam', 'launch', '', d)} \
                 image-jpeg \
                 screenshare \
                 shell-desktop \
                 shell-fullscreen \
                 shell-ivi \
                "
# pam
PACKAGECONFIG[pam] = ",,libpam"

EXTRA_OECONF_append_qemux86 = " \
        WESTON_NATIVE_BACKEND=fbdev-backend.so \
        "
EXTRA_OECONF_append_qemux86-64 = " \
        WESTON_NATIVE_BACKEND=fbdev-backend.so \
        "

EXTRA_OECONF_append = "${@bb.utils.contains("DISTRO_FEATURES", "early-ethernet", " --enable-early-boot", "" ,d)}"

do_install_append() {
    # expose weston protocol to /usr/share/weston as video may use it
    install ${WORKDIR}/graphics/weston/protocol/*.xml ${D}${datadir}/weston
}

FILES_${PN} += "${libdir}/lib*${SOLIBS} ${libdir}/libweston-${WESTON_MAJOR_VERSION}/*.so"
FILES_${PN} += "${systemd_unitdir}/system/ ${sysconfdir}/"
FILES_${PN}-staticdev += "${libdir}/libweston-${WESTON_MAJOR_VERSION}/*.a"
