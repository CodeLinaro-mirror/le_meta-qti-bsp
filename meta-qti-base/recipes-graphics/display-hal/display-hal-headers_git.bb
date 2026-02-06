SUMMARY = "Provide display-hal Headers"
DESCRIPTION = "Provide display Hardware Abstraction Layer header \
files. See display-hal-linux_git.bb for more information."
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

CODE_DIR = "display/display-hal"
CODE_DIR:gvm-gen5 = "vendor/qcom/opensource/display-core"
SRC_URI = "${PATH_TO_REPO}/${CODE_DIR}/.git;protocol=${PROTO};destsuffix=${CODE_DIR};usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/${CODE_DIR}"

do_configure[noexec] = "1"
do_compile[noexec] = "1"
do_install() {
    install -d ${D}${includedir}
    install -m 644 ${S}/include/*.h ${D}${includedir}
    if ls ${S}/libqservice/*.h >/dev/null 2>&1; then
        install -m 644 ${S}/libqservice/*.h ${D}${includedir}
    fi
}

ALLOW_EMPTY:${PN} = "1"
