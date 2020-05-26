EXTRA_OEMAKE = ""

FILES_${PN} += "/lib/lib*.so.*"
FILES_${PN} += "/lib/pkgconfig/*"
FILES_${PN}-dev += "/usr/share/*"
FILES_${PN}-dev += "/lib/lib*.so"

PACKAGECONFIG = "avdevice avfilter avcodec avformat swresample swscale postproc bzlib theora"

# Support multilib compilation for libav
PROVIDES += "${MLPREFIX}libav"

DEPENDS += "libion"

CFLAGS_append += " -I${STAGING_KERNEL_BUILDDIR}/usr/include -I${WORKDIR}/system/core/libion/include"

EXTRA_CFLAGS_append += " -fPIC"
EXTRA_CFLAGS_append += " ${@ bb.utils.contains('TUNE_FEATURES', 'callconvention-hard', '-mfloat-abi=hard', '', d)}"
EXTRA_CFLAGS_append += " ${@ bb.utils.contains('TUNE_FEATURES', 'neon', '-mfpu=neon', '', d)}"
EXTRA_CFLAGS_append += " ${@ bb.utils.contains('TUNE_FEATURES', 'armv7a', '-march=armv7-a', '', d)}"
EXTRA_CFLAGS_append += " ${@ bb.utils.contains('TUNE_FEATURES', 'cortexa8', '-mtune=cortex-a8', '', d)}"

EXTRA_OECONF_append += " \
    --target-os=linux --sysroot=${STAGING_DIR_TARGET} --arch=${TARGET_ARCH} --disable-mmx \
    --enable-shared --disable-doc --disable-htmlpages --disable-manpages --disable-podpages \
    --enable-small --disable-debug --enable-avresample --enable-protocol=udp \
    --enable-protocol=tcp --enable-protocol=rtp --enable-protocol=pipe --enable-protocol=http \
    --extra-cflags="${EXTRA_CFLAGS}" --enable-network --disable-zlib --disable-libx264 \
    --disable-altivec --enable-fft --libdir=${base_libdir} --shlibdir=${base_libdir} \
    --prefix=${base_libdir} --incdir=${includedir} --enable-libion \
"

do_install() {
    oe_runmake 'DESTDIR=${D}' install
    # Info dir listing isn't interesting at this point so remove it if it exists.
    if [ -e "${D}${infodir}/dir" ]; then
    rm -f ${D}${infodir}/dir
    fi
}
