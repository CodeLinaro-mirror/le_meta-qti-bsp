# Common defines applicable for all location BB files

EXTRA_OECONF = "--with-locpla-includes=${STAGING_INCDIR}/loc-pla \
                --with-core-includes=${SRC_DIR_ROOT}/system/core/include \
                --with-glib"
