inherit sdllvm

DEPENDS += "llvm-arm-toolchain-native"

# compile with sdllvm.
KERNEL_CC = "${CC} -fuse-ld=bfd"
TOOLCHAIN = "sdllvm"

do_kernel_configme[depends] += "llvm-arm-toolchain-native:do_populate_sysroot"
