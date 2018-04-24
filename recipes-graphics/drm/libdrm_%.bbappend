FILESEXTRAPATHS_prepend := "${WORKSPACE}/graphics/:"
SRC_DIR = "${WORKSPACE}/graphics/libdrm/"
SRC_URI = "file://${@d.getVar('SRC_DIR', True).replace('${WORKSPACE}/graphics/', '')}"
REPO_SRC_URI = "file://${@d.getVar('SRC_DIR', True).replace('${WORKSPACE}/graphics/', '')}"
S = "${WORKDIR}/libdrm"

EXTRA_OECONF += "${@base_conditional('BASEMACHINE', '8x96autogvmquin', '--enable-drm_fe=yes', '', d)}"
EXTRA_OECONF += "${@base_conditional('BASEMACHINE', '8x96autogvmgh', '--enable-drm_fe=yes', '', d)}"

CFLAGS += "${@base_conditional('BASEMACHINE', '8x96auto', '-DUSE_ION', '', d)}"

FILESEXTRAPATHS_append := ":${THISDIR}/${PN}"
