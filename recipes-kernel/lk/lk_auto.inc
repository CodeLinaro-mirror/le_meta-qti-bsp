inherit qperf
DEPENDS += " openssl-native"

S         =  "${WORKDIR}/bootable/bootloader/lk"
LIBGCC = "${STAGING_LIBDIR}/${TARGET_SYS}/${GCCVERSION}/libgcc.a"

CFLAGS += " -Wno-error"
