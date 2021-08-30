# Common defines applicable for all location BB files
#Enable syslogging for sa8155
CFLAGS_append = " -DUSE_SYSLOG_LOGGING"
CPPFLAGS_append = " -DUSE_SYSLOG_LOGGING"
EXTRA_OECONF = "--with-locpla-includes=${STAGING_INCDIR}/loc-pla \
                --with-core-includes=${SRC_DIR_ROOT}/system/core/include \
                --with-glib"

