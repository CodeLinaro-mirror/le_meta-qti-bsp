FILESEXTRAPATHS:append := "${THISDIR}/libsemanage:"

#Patches
SRC_URI += "file://0001-libsemanage-Change-sepolicy-store-root-path.patch"
