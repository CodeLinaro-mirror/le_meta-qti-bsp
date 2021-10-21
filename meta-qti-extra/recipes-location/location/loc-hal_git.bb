inherit autotools-brokensep pkgconfig
require ../include/common-location-defines.inc

DESCRIPTION = "GPS Loc HAL"
PR = "r5"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

FILESPATH =+ "${WORKSPACE}:"

SRC_URI = "${PATH_TO_REPO}/hardware/qcom/gps/.git;protocol=${PROTO};destsuffix=hardware/qcom/gps;subpath=gps;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/hardware/qcom/gps"

DEPENDS = "loc-core"

CPPFLAGS += "-I${WORKSPACE}/base/include"

SRC_URI +="file://confappend/"
do_install_append() {
    #Install default gps.conf file
    install -m 0644 -D ${S}/etc/gps.conf ${D}${sysconfdir}/gps.conf
    # Auto specific changes for gps.conf
    if ${@oe.utils.conditional('DISTRO', 'auto', 'true', 'false', d)}; then
        # Change default DEBUG_LEVEL to 1
        sed -i 's/DEBUG_LEVEL = 3/DEBUG_LEVEL = 1/' \
                ${D}${sysconfdir}/gps.conf
        # Append target specific confs
       gpsConfAppendFile='${WORKDIR}/confappend/auto/etc/gps.conf_append'
       if [ -e ${gpsConfAppendFile} ]; then
           echo "Appending file: ${gpsConfAppendFile} to gps.conf"
           cat ${gpsConfAppendFile} >> ${D}${sysconfdir}/gps.conf
       fi
    fi
}

PACKAGES = "${PN}"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
FILES_${PN} = "${libdir}/* ${sysconfdir}"
INSANE_SKIP_${PN} = "dev-so"
