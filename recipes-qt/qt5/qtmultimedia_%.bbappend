PACKAGE_ARCH = "${MACHINE_ARCH}"

PACKAGECONFIG_append = " gstreamer"

#rb1.4 SRC_URI_prepend = " \
#rb1.4    https://source.codeaurora.org/quic/le/AGL/meta-agl-demo/plain/recipes-qt/qt5/qtmultimedia/0001-GStreamer-support-date-time-type-in-metadata.patch?h=automotivelinux/chinook;downloadfilename=0001-GStreamer-support-date-time-type-in-metadata.patch;md5sum=70b5b3627afff63c20440c25964e1bd4;sha256sum=ec78e4f9a19e30bf2a042b559b1843677daee677f5dd768c3ffdb4343122f580 \
#rb1.4    https://source.codeaurora.org/quic/le/AGL/meta-agl-demo/plain/recipes-qt/qt5/qtmultimedia/0001-GStreamer-support-cover-art.patch?h=automotivelinux/chinook;downloadfilename=0001-GStreamer-support-cover-art.patch;md5sum=3367c92be25981e8fd4338dd51ca3ba1;sha256sum=263c33db0465f33acc134df9deb51c3704014b88acfa09092bd2ea9082032a0d \
#rb1.4    "
