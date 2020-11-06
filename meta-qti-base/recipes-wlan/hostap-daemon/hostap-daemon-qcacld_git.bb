DESCRIPTION = "Hostap Daemon"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${WORKDIR}/external/wpa_supplicant_8/COPYING;md5=279b4f5abb9c153c285221855ddb78cc"
DEPENDS = "pkgconfig libnl openssl"
SRCREV = "${AUTOREV}"
PR = "r4.2"

SRC_URI = "${PATH_TO_REPO}/external/wpa_supplicant_8/.git;protocol=${PROTO};destsuffix=external/wpa_supplicant_8;usehead=1 \
           file://defconfig-qcacld"

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
