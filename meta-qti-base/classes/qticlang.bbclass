inherit clang

CC_toolchain-clang = "${STAGING_BINDIR_NATIVE}/clang -target ${TARGET_SYS} ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS}"
CXX_toolchain-clang = "${STAGING_BINDIR_NATIVE}/clang++ -target ${TARGET_SYS} ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS}"
CPP_toolchain-clang = "${STAGING_BINDIR_NATIVE}/clang -target ${TARGET_SYS} -E ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS}"
CCLD_toolchain-clang = "${STAGING_BINDIR_NATIVE}/clang -target ${TARGET_SYS} ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS}"
LD_toolchain-clang  = "${STAGING_BINDIR_NATIVE}/ld.lld"

# Override the necessary toolchain environment variables for kernel/kernel module build
KERNEL_LD_toolchain-clang  = "${STAGING_BINDIR_NATIVE}/ld.lld"

# Clang does not yet support big.LITTLE performance tunes, so use the LITTLE for tunes
TUNE_CCARGS_remove_toolchain-clang = "-mcpu=cortex-a57.cortex-a53 -mcpu=cortex-a72.cortex-a53 -mcpu=cortex-a15.cortex-a7 -mcpu=cortex-a17.cortex-a7 -mcpu=cortex-a72.cortex-a35 -mcpu=cortex-a73.cortex-a53 -mcpu=cortex-a75.cortex-a55 -mcpu=cortex-a76.cortex-a55"
TUNE_CCARGS_append_toolchain-clang = "${@bb.utils.contains_any("TUNE_FEATURES", "cortexa72-cortexa53 cortexa57-cortexa53 cortexa73-cortexa53", " -mcpu=cortex-a53", "", d)}"
TUNE_CCARGS_append_toolchain-clang = "${@bb.utils.contains_any("TUNE_FEATURES", "cortexa15-cortexa7 cortexa17-cortexa7", " -mcpu=cortex-a7", "", d)}"
TUNE_CCARGS_append_toolchain-clang = "${@bb.utils.contains_any("TUNE_FEATURES", "cortexa72-cortexa35", " -mcpu=cortex-a35", "", d)}"
TUNE_CCARGS_append_toolchain-clang = "${@bb.utils.contains_any("TUNE_FEATURES", "cortexa75-cortex-a55 cortexa76-cortexa55", " -mcpu=cortex-a55", "", d)}"
