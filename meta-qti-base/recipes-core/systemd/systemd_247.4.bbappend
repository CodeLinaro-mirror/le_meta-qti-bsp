FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}-${PV}:"

# Disable close_range which doesn't work with linux-msm 5.4
SRC_URI_append = " file://0001-Disable-close_range.patch"
