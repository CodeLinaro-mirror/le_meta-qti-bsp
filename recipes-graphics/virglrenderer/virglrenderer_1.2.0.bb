SUMMARY = "VirGL virtual OpenGL renderer"
DESCRIPTION = "Virgil is a research project to investigate the possibility of \
creating a virtual 3D GPU for use inside qemu virtual machines, that allows \
the guest operating system to use the capabilities of the host GPU to \
accelerate 3D rendering."
HOMEPAGE = "https://virgil3d.github.io/"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=c81c08eeefd9418fca8f88309a76db10"

DEPENDS = "libepoxy libdrm python3-pyyaml-native"

FILESEXTRAPATHS:prepend := "${WORKSPACE}:"

VIRGLRENDERER_PATH = "external/virglrenderer"
SRC_URI = "file://${VIRGLRENDERER_PATH}/"

SRCREV = "${AUTOREV}"
S = "${WORKDIR}/${VIRGLRENDERER_PATH}"

inherit meson pkgconfig features_check

BBCLASSEXTEND = "native nativesdk"

REQUIRED_DISTRO_FEATURES = "opengl"
REQUIRED_DISTRO_FEATURES_class-native = ""
REQUIRED_DISTRO_FEATURES_class-nativesdk = ""

PACKAGECONFIG ??= "${@bb.utils.filter('DISTRO_FEATURES', 'vulkan', d)}"
PACKAGECONFIG[vulkan] = "-Dvenus=true,-Dvenus=false,vulkan-loader vulkan-headers vulkan-headers-vk-video"

EXTRA_OEMESON:append = " \
            -Dplatforms=egl \
            "

# This should be removed once qcvirtio changes for format definition are merged
CFLAGS:append = " -DGBM_FORMAT_GR88=GBM_FORMAT_RG88 -DGBM_FORMAT_XBGR16161616F=GBM_FORMAT_ABGR16161616F"
