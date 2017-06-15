inherit androidcompat androidmk_base

SUMMARY = "Android compat build framework"
SECTION = "adaptors"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"

DEPENDS = "liblog libcutils system-core"

FILESPATH =+ "${WORKSPACE}:"

# use a local cloned repo
SRC_URI   = "file://android_compat"
SRC_URI += "file://0001-build-Support-building-x86_64-host-binaries.patch"
SRC_URI += "file://0002-build-Allow-passing-CPPFLAGS-from-bitbake.patch"
SRC_URI += "file://0003-build-Update-protobuf-autogen-to-Android-N-version.patch"
SRC_URI += "file://0007-build-Stub-out-clang-and-java-packages.patch"

SRCREV = "${AUTOREV}"
S = "${WORKDIR}/android_compat"

PROVIDES += "virtual/androidcompat"

EXTRA_OECONF = " --with-core-includes=${WORKSPACE}/system/core/include --with-glib"
CFLAGS += "-I${STAGING_INCDIR}/cutils"
LDFLAGS += "-lcutils"

# Including the file depends on chipset
INCSUFFIX = "${@base_conditional('MACHINEGROUP', 'auto', 'androidcompat_auto', 'none',d)}"
include ${INCSUFFIX}.inc

