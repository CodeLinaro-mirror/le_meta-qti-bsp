inherit qticlang

TOOLCHAIN = "clang"
DEPENDS += "clang-native"

KERNEL_CC = "${CC} -fuse-ld=bfd"
KERNEL_LD = "${LD}"

do_generate_gki_defconfig[depends] += "clang-native:do_populate_sysroot"
