inherit qticlang

TOOLCHAIN = "clang"
DEPENDS += "clang-native"

KERNEL_CC = "${CC} -fuse-ld=bfd"

FULL_OPTIMIZATION_remove = "-fexpensive-optimizations -frename-registers -finline-limit=64 -Wno-error=maybe-uninitialized"
