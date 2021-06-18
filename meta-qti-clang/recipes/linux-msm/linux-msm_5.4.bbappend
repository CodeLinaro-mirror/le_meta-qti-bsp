DEPENDS += "clang-native"

inherit qticlang

TOOLCHAIN = "clang"

KERNEL_CC = "${CC} -fuse-ld=bfd"
