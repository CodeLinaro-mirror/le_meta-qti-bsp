SUMMARY = "AGM Plugin Library"
DESCRIPTION = "This is the AGM alsa plugin to support alsa lib APIs."
HOMEPAGE = "http://www.codeaurora.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"
DEPENDS += "agm-client agm-sndparser alsa-lib  ar-osal ar-util glib-2.0 gsl-fe-noship libuhab linux-msm-headers spf"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/agm/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/agm/plugins/alsalib;subpath=alsalib;usehead=1 \
           file://agm.conf \
          "
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/agm/plugins/alsalib"

inherit autotools pkgconfig

EXTRA_OECONF += "--with-glib"

do_install_append() {
    install -d ${D}${datadir}/alsa/alsa.conf.d
    install -d ${D}${sysconfdir}/alsa/conf.d
    install -m 0644 ${WORKDIR}/agm.conf ${D}${datadir}/alsa/alsa.conf.d/agm.conf
    ln -s ${datadir}/alsa/alsa.conf.d/agm.conf ${D}${sysconfdir}/alsa/conf.d/agm.conf

    install -d ${D}${libdir}/alsa-lib/
    for i in $(find ${D}${libdir}/. -name "*.so"); do
        mv ${i} ${D}${libdir}/alsa-lib/
    done
}

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
FILES_${PN} += "${libdir}/alsa-lib/*.so ${datadir}/alsa/alsa.conf.d/*"