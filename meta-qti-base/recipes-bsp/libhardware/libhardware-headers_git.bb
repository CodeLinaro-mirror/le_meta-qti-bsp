SUMMARY = "Android libhardware library headers"
DESCRIPTION = "Headers files for the Android libhardware HAL(Hardware Abstraction Layer) interfaces"
HOMEPAGE = "http://www.codeaurora.org/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://NOTICE;md5=9645f39e9db895a4aa6e02cb57294595"

DEPENDS += "system-core-headers"

SRC_URI = "\
    ${PATH_TO_REPO}/hardware/libhardware/.git;protocol=${PROTO};destsuffix=hardware/libhardware;usehead=1 \
    https://source.codeaurora.org/quic/la/platform/hardware/libhardware/plain/include/hardware/gralloc1.h?h=keystone/p-keystone-qcom-release;downloadfilename=gralloc1.h;md5sum=5171fc33c1299824ede5756a4da57507 \
"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/hardware/libhardware"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${includedir}/hardware/
    install -m 0644 ${WORKDIR}/gralloc1.h ${D}${includedir}/hardware/
    install -m 0644 ${S}/include/hardware/*.h ${D}${includedir}/hardware/
}
