#packages installed to root file system of PSM build
require mdm9607-base-image.inc

PACKAGE_EXCLUDE += "ddclient"
PACKAGE_EXCLUDE += "minidlna"
PACKAGE_EXCLUDE += "openssl"
PACKAGE_EXCLUDE += "system-conf"

PACKAGE_EXCLUDE += "qcacld-hl"
PACKAGE_EXCLUDE += "wireless-tools"
PACKAGE_EXCLUDE += "wpa-supplicant-qcacld"
PACKAGE_EXCLUDE += "hostap-daemon-qcacld"

#Excluding Flouride BT packages
PACKAGE_EXCLUDE += "packagegroup-bt"
PACKAGE_EXCLUDE += "bt-app"
PACKAGE_EXCLUDE += "bt-property"
PACKAGE_EXCLUDE+= "fluoride"
PACKAGE_EXCLUDE += "libbt-vendor"

PACKAGE_EXCLUDE += "libsensors"
PACKAGE_EXCLUDE += "start-scripts-init-can"
