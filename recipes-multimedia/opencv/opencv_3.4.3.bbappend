SRCREV_FORMAT = "opencv_contrib"

PV = "3.4.3"

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI_remove = " \
    git://github.com/opencv/opencv_3rdparty.git;branch=ippicv/master_20180518;destsuffix=ipp;name=ipp \
    https://github.com/tiny-dnn/tiny-dnn/archive/v1.0.0a3.tar.gz;destsuffix=git/3rdparty/tinydnn/tiny-dnn-1.0.0a3;name=tinydnn;unpack=false \
    file://0001-3rdparty-ippicv-Use-pre-downloaded-ipp.patch \
    file://0001-Add-missing-multi-line-separator.patch;patchdir=../contrib/ \
    file://0002-Make-opencv-ts-create-share-library-intead-of-static.patch \
    file://0003-To-fix-errors-as-following.patch \
    file://0001-Temporarliy-work-around-deprecated-ffmpeg-RAW-functi.patch \
    file://0001-Dont-use-isystem.patch \
    file://0001-Check-for-clang-before-using-isystem.patch \
"
SRC_URI += " \
    file://0001-enable-hdf-configuration.patch;patchdir=../contrib/ \
"
do_unpack_extra() {
    rm -rf ${WORKDIR}/3rdparty
    cp ${WORKDIR}/vgg/*.i ${WORKDIR}/contrib/modules/xfeatures2d/src
    cp ${WORKDIR}/boostdesc/*.i ${WORKDIR}/contrib/modules/xfeatures2d/src
}
DEPENDS_append = " hdf5 libgfortran"
PACKAGECONFIG_append = "${@bb.utils.contains("DISTRO_FEATURES", "wayland", " gtk", "", d)}"
PACKAGECONFIG_remove = " gphoto2 v4l libv4l gstreamer ffmpeg libav"
PACKAGECONFIG_append = " dnn"
EXTRA_OECMAKE_remove = "${@oe.utils.conditional("libdir", "/usr/lib64", "-DLIB_SUFFIX=64", "", d)} \
			${@oe.utils.conditional("libdir", "/usr/lib32", "-DLIB_SUFFIX=32", "", d)} "

DEPENDS += "libcutils"
EXTRA_OECMAKE_append = " -DWITH_TESTS=ON"
LICENSE_FLAGS_WHITELIST_append = " commercial"
