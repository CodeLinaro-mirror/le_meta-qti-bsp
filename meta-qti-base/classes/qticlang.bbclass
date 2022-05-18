inherit clang

CC:toolchain-clang = "${STAGING_BINDIR_NATIVE}/clang -target ${TARGET_SYS} ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS}"
CXX:toolchain-clang = "${STAGING_BINDIR_NATIVE}/clang++ -target ${TARGET_SYS} ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS}"
CPP:toolchain-clang = "${STAGING_BINDIR_NATIVE}/clang -target ${TARGET_SYS} -E ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS}"
CCLD:toolchain-clang = "${STAGING_BINDIR_NATIVE}/clang -target ${TARGET_SYS} ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS}"
LD:toolchain-clang  = "${STAGING_BINDIR_NATIVE}/ld.lld"

# Override the necessary toolchain environment variables for kernel/kernel module build
KERNEL_LD:toolchain-clang  = "${STAGING_BINDIR_NATIVE}/ld.lld"
