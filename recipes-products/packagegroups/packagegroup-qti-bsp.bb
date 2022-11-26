SUMMARY = "Basic programs and scripts required by LE system"
DESCRIPTION = "Package group to bring in all basic packages for LE system"
LICENSE = "BSD-3-Clause"

PACKAGE_ARCH = "${TUNE_PKGARCH}"
inherit packagegroup

PROVIDES = "${PACKAGES}"
USB_SUPPORT = "${@d.getVar('MACHINE_SUPPORTS_USB') or "True"}"
PROPERTIES_SUPPORT = "${@d.getVar('MACHINE_SUPPORTS_ANDROID_PROPERTIES') or "True"}"

PACKAGES = ' \
    packagegroup-android-utils \
    packagegroup-support-utils \
    packagegroup-startup-scripts \
    '

# Android Core Image and Debugging utilities
RDEPENDS:packagegroup-android-utils = "\
    packagegroup-android-utils-base \
    "

# Startup scripts needed during device bootup
RDEPENDS:packagegroup-startup-scripts = "\
    packagegroup-startup-scripts-base \
    "
# Other essential utilites
CHRONY ?= "chrony"
CHRONY:sa410m = ""
CHRONY:kalama = ""

RDEPENDS:packagegroup-support-utils = "\
    ${CHRONY} \
    libinput \
    libinput-bin \
    libnl \
    libxml2 \
    "
