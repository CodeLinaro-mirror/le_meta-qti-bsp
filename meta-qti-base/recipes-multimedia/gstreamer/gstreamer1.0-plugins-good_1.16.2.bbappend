DEFAULT_PREFERENCE = "-1"

SRC_URI   =  "${PATH_TO_REPO}/gstreamer/gst-plugins-good/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-good;usehead=1"
SRC_URI_append = " ${CAF_GIT}/gstreamer/common;destsuffix=gstreamer/gst-plugins-good/common;branch=gstreamer/common/master;name=common"

SRCREV = "${AUTOREV}"
SRCREV_common = "59cb678164719ff59dcf6c8b93df4617a1075d11"
SRCREV_FORMAT = "good_common"

S = "${WORKDIR}/gstreamer/gst-plugins-good"

PACKAGECONFIG ??= " \
    ${GSTREAMER_ORC} \
    ${@bb.utils.filter('DISTRO_FEATURES', 'pulseaudio x11', d)} \
    bz2 cairo flac gdk-pixbuf gudev jpeg-turbo lame libpng mpg123 soup speex taglib v4l2 \
"
PACKAGECONFIG[v4l2]       = "-Dv4l2=enabled -Dv4l2-probe=false,-Dv4l2=false"

RPROVIDES_${PN}-souphttpsrc = "${PN}-soup"
