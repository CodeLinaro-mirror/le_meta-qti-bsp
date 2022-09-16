SUMMARY = "Tools for Flash-Friendly File System (F2FS)"
HOMEPAGE = "https://git.kernel.org/pub/scm/linux/kernel/git/jaegeuk/f2fs-tools.git"

LICENSE = "LGPL-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=362b4b2594cd362b874a97718faa51d3"

# to provide libuuid
DEPENDS = "util-linux"

# Tag for android-mainline-10.0.0_r10
SRC_URI[sha256sum] = "f2be6581034c1152c310a8568cc529514a63f3f8f1c464c4c49d50a2f9f88554"
SRC_URI = "https://git.kernel.org/pub/scm/linux/kernel/git/jaegeuk/f2fs-tools.git/snapshot/f2fs-tools-android-mainline-10.0.0_r10.tar.gz \
           file://0001-add-android-sparse-image-support.patch \
          "

S = "${WORKDIR}/f2fs-tools-android-mainline-10.0.0_r10"

inherit pkgconfig autotools

PACKAGECONFIG ?= "android"
PACKAGECONFIG[android] = "--with-android,--without-android,libsparse"

BBCLASSEXTEND = "native"
