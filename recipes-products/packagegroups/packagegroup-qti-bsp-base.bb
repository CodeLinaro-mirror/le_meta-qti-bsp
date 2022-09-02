SUMMARY = "Basic programs and scripts required by LE system"
DESCRIPTION = "Package group to bring in all basic packages for LE system"
LICENSE = "BSD-3-Clause"

inherit packagegroup

PROVIDES = "${PACKAGES}"

USB_SUPPORT = "${@d.getVar('MACHINE_SUPPORTS_USB') or "True"}"
DISABLE_USBD_SUPPORT = "${@d.getVar('USB_PERIPHERAL_ONLY_MODE') or "False"}"

PROPERTIES_SUPPORT = "${@d.getVar('MACHINE_SUPPORTS_ANDROID_PROPERTIES') or "True"}"

PACKAGES = ' \
    packagegroup-android-utils-base \
    packagegroup-startup-scripts-base \
    '
ENABLE_ADB ?= "True"
ENABLE_ADB_qti-distro-base-user ?= "False"

# Android Core Image and Debugging utilities
RDEPENDS_packagegroup-android-utils-base = "\
    ${@oe.utils.conditional('ENABLE_ADB', 'True', 'adbd', '', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-sdx', '', 'binder', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-sdx', '', 'leproperties', d)} \
    logcat \
    logd \
    libsync \
    ${@oe.utils.conditional('PROPERTIES_SUPPORT', 'True', 'system-prop', '', d)} \
    "

ADDON_SCRIPTS ?= ""
ADDON_SCRIPTS_neo = "helios-start"

# Startup scripts needed during device bootup
RDEPENDS_packagegroup-startup-scripts-base = "\
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-ab-boot', 'ab-slot-util', '', d)} \
    ${@oe.utils.conditional('USB_SUPPORT', 'True', 'usb-composition', '', d)} \
    ${@oe.utils.conditional('DISABLE_USB_SUPPORT', 'True', '', 'usb-composition-usbd', d)} \
    post-boot \
    sdcard-scripts-automount \
    ${ADDON_SCRIPTS} \
    "
