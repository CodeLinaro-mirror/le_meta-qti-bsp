gio_module_cache_common() {

# qemu-aarch64 does not support 32-bit exe, ignore it

if [ ${MLPREFIX} == "lib32-" ]; then
    exit 0
fi

if [ "x$D" != "x" ]; then
    $INTERCEPT_DIR/postinst_intercept update_gio_module_cache ${PKG} \
            mlprefix=${MLPREFIX} \
            binprefix=${MLPREFIX} \
            libdir=${libdir} \
            base_libdir=${base_libdir} \
            bindir=${bindir}
else
    ${libexecdir}/${MLPREFIX}gio-querymodules ${libdir}/gio/modules/
fi

}
