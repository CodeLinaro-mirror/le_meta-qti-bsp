DEPENDS += "llvm-arm-toolchain-native"

inherit sdllvm

# compile with sdllvm.
KERNEL_CC = "${CC} -fuse-ld=bfd"
TOOLCHAIN = "sdllvm"

do_kernel_configme[depends] += "llvm-arm-toolchain-native:do_populate_sysroot"
