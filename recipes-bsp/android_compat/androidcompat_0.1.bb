inherit androidcompat androidmk_base

SUMMARY = "Android compat build framework"
SECTION = "adaptors"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS = "liblog libcutils system-core glib-2.0"

FILESPATH =+ "${WORKSPACE}:"

CFLAGS += "-I${STAGING_INCDIR}/cutils"
CFLAGS_append_pn-lib32-androidcompat = " -Dstrlcpy=g_strlcpy "
CFLAGS_append_pn-lib32-androidcompat = " -Dstrlcat=g_strlcat "

LDFLAGS_append_pn-lib32-androidcompat = " -lglib-2.0 -shared "
LDFLAGS += " -lcutils "

# use a local cloned repo
SRC_URI   = "file://android_compat"

SRCREV = "${AUTOREV}"
S = "${WORKDIR}/android_compat"

PROVIDES += "virtual/androidcompat"

PACKAGES = "${PN} andorid_compat"

EXTRA_OECONF = " --with-core-includes=${WORKSPACE}/system/core/include --with-glib"
CFLAGS += "-I${STAGING_INCDIR}/cutils"
LDFLAGS_prepend = " -lcutils "
