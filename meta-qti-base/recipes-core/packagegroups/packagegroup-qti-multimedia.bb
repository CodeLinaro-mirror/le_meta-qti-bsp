
SUMMARY = "multimedia framework"
DESCRIPTION = "packages for multimedia"
LICENSE = "GPLv2+ & LGPLv2+"

PACKAGE_ARCH = "${TUNE_PKGARCH}"

inherit packagegroup

ALLOW_EMPTY:${PN} = "1"
PACKAGES = "${PN}"


RDEPENDS:${PN} = " \
        gstreamer1.0 \
        gstreamer1.0-plugins-base \
        gstreamer1.0-plugins-good \
        gstreamer1.0-plugins-bad \
        gstreamer1.0-plugins-ugly \
        gstreamer1.0-libav \
        ${@bb.utils.contains('DISTRO_FEATURES', 'qti-gstqeavb', 'gstreamer1.0-plugins-qeavb', '', d)} \
        ${@bb.utils.contains('DISTRO_FEATURES', 'qti-omx', 'gstreamer1.0-omx mm-vdec-omx-test-lite mm-venc-omx-test-lite', '', d)} \
        ${@bb.utils.contains('DISTRO_FEATURES', 'qti-codec2', 'codec2 gstreamer1.0-plugins-codec2 secure-video-app codec2-app', '', d)} \
"
