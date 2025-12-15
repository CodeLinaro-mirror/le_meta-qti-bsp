FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}/1.20.7:"
SRC_URI:append = " file://0001-gstwaylandsink-add-P010_10LE-support-statement.patch \
                   file://0002-waylandsink-support-ubwc-modifier-and-zwp_linux_dmab.patch \
                 "
