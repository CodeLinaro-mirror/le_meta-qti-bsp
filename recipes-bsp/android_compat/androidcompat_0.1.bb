inherit androidcompat androidmk_base

SUMMARY = "Android compat build framework"
SECTION = "adaptors"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS = "liblog libcutils system-core"

FILESPATH =+ "${WORKSPACE}:"

# use a local cloned repo
SRC_URI   = "file://android_compat"

SRCREV = "${AUTOREV}"
S = "${WORKDIR}/android_compat"

PROVIDES += "virtual/androidcompat"

EXTRA_OECONF = " --with-core-includes=${WORKSPACE}/system/core/include --with-glib"
CFLAGS += "-I${STAGING_INCDIR}/cutils"
LDFLAGS += "-lcutils"

# Including the file depends on chipset
INCSUFFIX = "${@base_conditional('BASEMACHINE', '8x96auto', 'androidcompat_auto', 'none',d)}"
include ${INCSUFFIX}.inc
INCSUFFIX = "${@base_conditional('BASEMACHINE', '8x96autofusion', 'androidcompat_auto', 'none',d)}"
include ${INCSUFFIX}.inc
INCSUFFIX = "${@base_conditional('BASEMACHINE', '8x96auto44', 'androidcompat_auto', 'none',d)}"
include ${INCSUFFIX}.inc