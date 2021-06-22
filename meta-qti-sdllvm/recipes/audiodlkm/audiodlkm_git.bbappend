DEPENDS += "llvm-arm-toolchain-native"

inherit sdllvm

# compile with sdllvm.
KERNEL_CC = "${CC} -fuse-ld=bfd"
TOOLCHAIN = "sdllvm"
