
FILESEXTRAPATHS:append := ":${THISDIR}/libinput"

SRC_URI:append = " file://0002-libinput-fix-libinput-udev-cold-plug-race-issue.patch"
