DEPENDS += "glib-2.0"

CFLAGS += "-I${STAGING_INCDIR}/cutils"
CFLAGS_append_pn-lib32-androidcompat = " -Dstrlcpy=g_strlcpy "
CFLAGS_append_pn-lib32-androidcompat = " -Dstrlcat=g_strlcat "

LDFLAGS_append_pn-lib32-androidcompat = " -lglib-2.0 -shared "
LDFLAGS_prepend = " -lcutils "

PACKAGES = "${PN} andorid_compat"
