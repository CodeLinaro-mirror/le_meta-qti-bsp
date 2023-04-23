DEFAULT_PREFERENCE = "-1"

SRC_URI_remove = "https://gstreamer.freedesktop.org/src/gst-plugins-bad/gst-plugins-bad-${PV}.tar.xz"
SRC_URI += "${PATH_TO_REPO}/gstreamer/gst-plugins-bad/.git;protocol=${PROTO};destsuffix=gstreamer/gst-plugins-bad;usehead=1"
SRC_URI += "${CLO_LE_GIT}/gstreamer/common;destsuffix=gstreamer/gst-plugins-bad/common;nobranch=1;name=common"

GI_DATA_ENABLED="0"
SRCREV = "${AUTOREV}"
SRCREV_common = "59cb678164719ff59dcf6c8b93df4617a1075d11"
SRCREV_FORMAT = "bad_common"
S = "${WORKDIR}/gstreamer/gst-plugins-bad"
DEPENDS += "gbm"
DEPENDS += "wayland-native"
DEPENDS += "weston"

PACKAGECONFIG = " \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wayland', 'wayland', '', d)} \
    orc \
    hls \
    dash \
    "
PACKAGECONFIG_GL = "${@bb.utils.contains('DISTRO_FEATURES', 'opengl', 'gles2 egl', '', d)}"

PACKAGECONFIG[dc1394]          = "-Ddc1394=enabled,-Ddc1394=disabled,libdc1394"
PACKAGECONFIG[wayland] = "-Dwayland=enabled -Degl=enabled,-Dwayland=disabled -Degl=disabled,wayland virtual/egl"
PACKAGECONFIG[hls]             = "-Dhls=enabled -Dhls-crypto=nettle,-Dhls=disabled,nettle"
ACKAGECONFIG[kms]             = "-Dkms=enabled,-Dkms=disabled,libdrm"
PACKAGECONFIG[openjpeg]        = "-Dopenjpeg=enabled,-Dopenjpeg=disabled,openjpeg"
PACKAGECONFIG[vulkan]          = "-Dvulkan=enabled,-Dvulkan=disabled,vulkan"
PACKAGECONFIG[wayland]         = "-Dwayland=enabled,-Dwayland=disabled,wayland-native wayland wayland-protocols libdrm"

EXTRA_OEMESON = " \
                -Daccurip=disabled \
                -Dadpcmdec=disabled \
                -Dadpcmenc=disabled \
                -Daiff=disabled \
                -Dasfmux=disabled \
                -Dassrender=disabled \
                -Daudiofxbad=disabled \
                -Daudiofxbad=disabled \
                -Daudiovisualizers=disabled \
                -Dautoconvert=disabled \
                -Dbayer=disabled \
                -Dbluez=disabled \
                -Dbs2b=disabled \
                -Dbz2=disabled \
                -Dcamerabin2=disabled \
                -Dchromaprint=disabled \
                -Dcoloreffects=disabled \
                -Dcurl=disabled \
                -Ddash=enabled \
                -Ddc1394=disabled \
                -Ddebugutils=disabled \
                -Ddecklink=disabled \
                -Ddirectfb=disabled \
                -Ddirectsound=disabled \
                -Ddtls=disabled \
                -Ddts=disabled \
                -Ddvb=disabled \
                -Ddvdspu=disabled \
                -Ddvbsuboverlay=disabled \
                -Dfaac=disabled \
                -Dfaad=disabled \
                -Dfaceoverlay=disabled \
                -Dfbdev=disabled \
                -Dfestival=disabled \
                -Dfieldanalysis=disabled \
                -Dflite=disabled \
                -Dfluidsynth=disabled \
                -Dfreeverb=disabled \
                -Dfrei0r=disabled \
                -Dgaudieffects=disabled \
                -Dgdp=disabled \
                -Dgeometrictransform=disabled \
                -Dgl=disabled \
                -Dgme=disabled \
                -Dgsm=disabled \
                -Did3tag=enabled \
                -Dinter=disabled \
                -Dinterlace=disabled \
                -Divfparse=disabled \
                -Divtc=disabled\
                -Djp2kdecimator=disabled \
                -Djpegformat=disabled \
                -Dladspa=disabled \
                -Dlibde265=disabled \
                -Dlibmms=disabled \
                -Dlibrfb=disabled \
                -Dlv2=disabled \
                -Dkate=disabled \
                -Dmidi=enabled \
                -Dmodplug=disabled \
                -Dmpeg2enc=disabled \
                -Dmpegdemux=enabled \
                -Dmpegtsdemux=enabled \
                -Dmpegtsmux=enabled \
                -Dmpegpsmux=enabled \
                -Dmplex=disabled \
                -Dmusepack=disabled \
                -Dmxf=disabled \
                -Dneon=disabled \
                -Dnetsim=disabled \
                -Dnvenc=disabled \
                -Dofa=disabled \
                -Donvif=disabled \
                -Dopenal=disabled \
                -Dopencv=disabled \
                -Dopenexr=disabled \
                -Dopenh264=disabled \
                -Dopenjpeg=disabled \
                -Dopenni2=disabled \
                -Dopensles=disabled \
                -Dopus=disabled \
                -Dpcapparse=disabled \
                -Dpnm=disabled \
                -Drawparse=enabled \
                -Dremovesilence=disabled \
                -Dresindvd=disabled \
                -Drsvg=disabled \
                -Drtmp=disabled \
                -Dsbc=disabled \
                -Dsdp=disabled \
                -Dsegmentclip=disabled \
                -Dsiren=disabled \
                -Dsmooth=disabled \
                -Dsmoothstreaming=disabled \
                -Dsndfile=disabled \
                -Dsoundtouch=disabled \
                -Dspandsp=disabled \
                -Dspeed=disabled \
                -Dsrtp=disabled \
                -Dsubenc=disabled \
                -Dtinyalsa=disabled \
                -Duvch264=disabled \
                -Dvdpau=disabled \
                -Dvideofilters=disabled \
                -Dvideoframe_audiolevel=disabled \
                -Dvideoparsers=enabled \
                -Dvideosignal=disabled \
                -Dvmnc=disabled \
                -Dvoaacenc=disabled \
                -Dvoamrwbenc=disabled \
                -Dvulkan=disabled \
                -Dwebp=disabled \
                -Dwasapi=disabled \
                -Dwildmidi=disabled \
                -Dwinks=disabled \
                -Dwinscreencap=disabled \
                -Dx11=disabled \
                -Dx265=disabled \
                -Dy4m=disabled \
                -Dyadif=disabled \
                -Dzbar=disabled \
                -Dintrospection=disabled \
                "
EXTRA_OECONF_append =" --with-protocal-xml-path=${STAGING_DATADIR}/weston"
EXTRA_OEMESON_append = " \
			   -Dkernel_path=${STAGING_KERNEL_BUILDDIR}/usr/include \
              "
CPPFLAGS += "-I${STAGING_KERNEL_BUILDDIR}/usr/include"
do_configure_prepend() {
	cd ${S}
	./autogen.sh --noconfigure
	cd ${B}
}
do_compile_prepend() {
    export GIR_EXTRA_LIBS_PATH="${B}/gst-libs/gst/allocators/.libs"
}

