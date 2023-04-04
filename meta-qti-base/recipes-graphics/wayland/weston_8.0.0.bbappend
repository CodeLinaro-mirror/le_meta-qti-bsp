DEPENDS += "display-hal-headers display-hal-linux display-noship-linux display-ship-linux \
            gbm gbm-headers \
            ${@bb.utils.contains('PREFERRED_VERSION_linux-msm', '5.4', 'libion', '', d)} \
            libsync \
            ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'libuhab', '', d)} \
            virtual/kernel-headers \
"

FILESEXTRAPATHS:append := " :${THISDIR}/weston/"
SRC_URI = "${PATH_TO_REPO}/graphics/weston/.git;protocol=${PROTO};destsuffix=graphics/weston;usehead=1"
#Remove community patch which is conflict with Weston SDM optimization
SRC_URI:remove = "file://0001-compositor-drm.c-Launch-without-input-devices.patch"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/graphics/weston"

inherit systemd

UPSTREAM_CHECK_URI:remove = "https://wayland.freedesktop.org/releases.html"

REQUIRED_DISTRO_FEATURES:remove = "opengl"

TARGET_CFLAGS += "-I${STAGING_INCDIR}/libdrm"
TARGET_CFLAGS += "-I${STAGING_INCDIR}/sdm"
TARGET_CFLAGS += "-I${STAGING_INCDIR}/sdm/core"
TARGET_CFLAGS += "-I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel}"
TARGET_CFLAGS += "-I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel}/display"
TARGET_CPPFLAGS += "-I${STAGING_INCDIR}/qcom/display"
TARGET_CPPFLAGS += "-I${STAGING_INCDIR}/sdm"
TARGET_CPPFLAGS += "-I${STAGING_INCDIR}/sdm/core"

#Overwrite Packageconfig
PACKAGECONFIG = "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'kms fbdev wayland egl clients', '', d)} \
                 ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'x11', '', d)} \
                 ${@bb.utils.contains('DISTRO_FEATURES', 'pam', 'launch', '', d)} \
                 ${@bb.utils.contains('DISTRO_FEATURES', 'early_init', 'early', '', d)} \
                 image-jpeg \
                 screenshare \
                 shell-desktop \
                 shell-fullscreen \
                 shell-ivi \
                "
# pam
PACKAGECONFIG[pam] = ",,libpam"
# early-init
PACKAGECONFIG[early] = "-Denable-early-boot=true,-Denable-early-boot=false"

EXTRA_OECONF:append:qemux86 = " \
        WESTON_NATIVE_BACKEND=fbdev-backend.so \
        "
EXTRA_OECONF:append:qemux86-64 = " \
        WESTON_NATIVE_BACKEND=fbdev-backend.so \
        "

EXTRA_OECONF:append = "${@bb.utils.contains("DISTRO_FEATURES", "early-ethernet", " --enable-early-boot", "" ,d)}"

# expose weston protocol to /usr/share/weston as video may use it
do_install:append() {
    install ${WORKDIR}/graphics/weston/protocol/*.xml ${D}${datadir}/weston
}

FILES:${PN} += "${libdir}/lib*${SOLIBS} ${libdir}/libweston-${WESTON_MAJOR_VERSION}/*.so"
FILES:${PN} += "${systemd_unitdir}/system/ ${sysconfdir}/"
FILES:${PN}-staticdev += "${libdir}/libweston-${WESTON_MAJOR_VERSION}/*.a"
