FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

# Fetch code from github
SRCREV = "18e0cdba48feeccea2429b3b0b2691f4314d1062"
MAKEDUMPFILE_VER = "1.6.8"
SRC_URI_remove = "${SOURCEFORGE_MIRROR}/makedumpfile/${BPN}-${PV}.tar.gz"
SRC_URI_remove = "file://0001-makedumpfile-replace-hardcode-CFLAGS.patch"
SRC_URI_remove = "file://0002-mem_section-Support-only-46-bit-for-MAX_PHYSMEM_BITS.patch"
SRC_URI_append = "\
	https://github.com/makedumpfile/makedumpfile/releases/download/${MAKEDUMPFILE_VER}/makedumpfile-${MAKEDUMPFILE_VER}.tar.gz \
	file://0001-PATCH-2-3-arm64-Make-use-of-NUMBER-VA_BITS-in-vmcore.patch \
	file://0002-PATCH-3-3-arm64-support-flipped-VA-and-52-bit-kernel.patch \
	file://0003-makedumpfile-arm64-hardcode-vabits-value-and-add-sec.patch \
	https://raw.githubusercontent.com/openembedded/meta-openembedded/master/meta-oe/recipes-kernel/makedumpfile/makedumpfile/0001-makedumpfile-replace-hardcode-CFLAGS.patch?h=openembedded/meta-openembedded;downloadfilename=0001-makedumpfile-replace-hardcode-CFLAGS.patch \
	https://raw.githubusercontent.com/openembedded/meta-openembedded/master/meta-oe/recipes-kernel/makedumpfile/makedumpfile/0002-mem_section-Support-only-46-bit-for-MAX_PHYSMEM_BITS.patch?h=openembedded/meta-openembedded;downloadfilename=0002-mem_section-Support-only-46-bit-for-MAX_PHYSMEM_BITS.patch \
	"
SRC_URI[md5sum] = ""
SRC_URI[sha256sum] = ""

DEPENDS += " xz"

S = "${WORKDIR}/makedumpfile-${MAKEDUMPFILE_VER}"
