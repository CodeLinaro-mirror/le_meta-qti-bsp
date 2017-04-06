inherit autotools pkgconfig

DESCRIPTION = "libRoboticsCamera"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

PR = "r0"

DEPENDS = "liblog"
DEPENDS += "libcutils"
DEPENDS += "native-frameworks"
DEPENDS += "system-core"
DEPENDS += "glib-2.0"
DEPENDS += "qmmf-sdk" 

EXTRA_OECONF_append = " --with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include"
EXTRA_OECONF += " --with-camerahal=${WORKSPACE}/camera/lib/QCamera2/HAL3"

#Buid libcamera using qmmf-sdk 
EXTRA_OECONF += " --with-qmmf_sdk"

#Libcamera can be built with either qmmf-sdk or camera-hal and not both
#Uncomment line below to build libcamera using camera HAL 1. 
#EXTRA_OECONF += " --with-camhal1"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI   = "file://vendor/qcom/opensource/libroboticscamera/"

SRCREV = "${AUTOREV}"
S      = "${WORKDIR}/vendor/qcom/opensource/libroboticscamera/"

PACKAGES = "lib-robotics-camera"
PACKAGES += "lib-robotics-camera-dev"
PACKAGES += "lib-robotics-camera-dbg"

FILES_lib-robotics-camera-dbg    = "${libdir}/.debug/libcamera.*"
FILES_lib-robotics-camera        = "${libdir}/libcamera.so.* ${bindir}/*"
FILES_lib-robotics-camera-dev    = "${libdir}/libcamera.* ${libdir}/libcamera.la ${includedir}"
