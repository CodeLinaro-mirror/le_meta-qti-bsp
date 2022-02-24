DEPENDS += "llvm-arm-toolchain-native"

inherit sdllvm

# compile with sdllvm.
KERNEL_CC = "${CC} -fuse-ld=bfd"
KERNEL_LD = "${LD}"
TOOLCHAIN = "sdllvm"

# LTO_CLANG produces LLVM IR instead of object files.
# Use llvm-ar and llvm-nm.
EXTRA_OEMAKE_append_toolchain-sdllvm = " AR='${AR}' LLVM_NM='${NM}'"

do_generate_gki_defconfig[depends] += "llvm-arm-toolchain-native:do_populate_sysroot"
do_kernel_configme[depends] += "llvm-arm-toolchain-native:do_populate_sysroot"
