
FILESEXTRAPATHS_append := ":${THISDIR}/libinput"

SRC_URI_append = " file://0002-libinput-fix-libinput-udev-cold-plug-race-issue.patch"
