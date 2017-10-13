inherit autotools-brokensep linux-kernel-base qcommon

DESCRIPTION = "Wi-Fi Protected Access(WPA) Supplicant"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

FILES_${PN} += "${sbindir}/*"
FILES_${PN} += "${libdir}/lib*.so"

PACKAGES = "${PN}"

INC_PR = "r4"

DEPENDS = "openssl libnl"

SRC_URI="${CAF_LA_GIT}/platform/external/hostap.git;tag=${CAF_TAG};protocol=git;nobranch=1;destsuffix=external/hostap"
S = "${WORKDIR}/external/hostap/wpa_supplicant"

do_install() {
    make install DESTDIR=${D} BINDIR=${sbindir} LIBDIR=${libdir} INCDIR=${includedir}
}
