FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

# Fetch code from github
SRCREV = "18e0cdba48feeccea2429b3b0b2691f4314d1062"
SRC_URI_remove = "${SOURCEFORGE_MIRROR}/makedumpfile/${BPN}-${PV}.tar.gz"
SRC_URI_remove = "file://0001-makedumpfile-replace-hardcode-CFLAGS.patch"
SRC_URI_remove = "file://0002-mem_section-Support-only-46-bit-for-MAX_PHYSMEM_BITS.patch"
SRC_URI_append = "\
    git://github.com/makedumpfile/${BPN}.git;protocol=http;branch=master \
	file://0001-makedumpfile-arm64-Add-support-for-ARMv8.2-LVA-52-bi.patch \
	file://0002-makedumpfile-arm64-hardcode-vabits-value-and-add-sec.patch \
	https://raw.githubusercontent.com/openembedded/meta-openembedded/master/meta-oe/recipes-kernel/makedumpfile/makedumpfile/0001-makedumpfile-replace-hardcode-CFLAGS.patch?h=openembedded/meta-openembedded;downloadfilename=0001-makedumpfile-replace-hardcode-CFLAGS.patch \
	https://raw.githubusercontent.com/openembedded/meta-openembedded/master/meta-oe/recipes-kernel/makedumpfile/makedumpfile/0002-mem_section-Support-only-46-bit-for-MAX_PHYSMEM_BITS.patch?h=openembedded/meta-openembedded;downloadfilename=0002-mem_section-Support-only-46-bit-for-MAX_PHYSMEM_BITS.patch \
	"
SRC_URI[md5sum] = ""
SRC_URI[sha256sum] = ""

DEPENDS += " xz"

S = "${WORKDIR}/git"
