DESCRIPTION = "Hostap Daemon"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"
DEPENDS = "pkgconfig libnl openssl"
SRCREV = "${AUTOREV}"
PR = "${INC_PR}.2"
INC_PR = "r4"

SRC_URI = "${PATH_TO_REPO}/external/wpa_supplicant_8/.git;protocol=${PROTO};destsuffix=external/wpa_supplicant_8;usehead=1"
SRC_URI_append = " file://defconfig-qcacld"

S = "${WORKDIR}/external/wpa_supplicant_8/hostapd/"

inherit autotools-brokensep linux-kernel-base pkgconfig

do_configure() {
    install -m 0644 ${WORKDIR}/defconfig-qcacld .config
    echo "CFLAGS +=\"-I${STAGING_INCDIR}/libnl3\"" >> .config
}
do_install() {
    install -d ${D}${sysconfdir}
    install -m 0644 ${S}/hostapd.conf ${D}${sysconfdir}/hostapd.conf
    make install DESTDIR=${D} BINDIR=${sbindir}/
}

FILES_${PN} += "\
        ${bindir} \
        "