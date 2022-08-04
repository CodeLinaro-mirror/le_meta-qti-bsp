inherit autotools pkgconfig

DESCRIPTION = "atrace utility for tracing"
HOMEPAGE = "http://developer.android.com/"

LICENSE  = "Apache-2.0 & BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10"

LIC_FILES_CHKSUM += "file://${COREBASE}/meta-qti-bsp/files/common-licenses/\
BSD-3-Clause-Clear;md5=3771d4920bd6cdb8cbdf1e8344489ee0"

DEPENDS = "libbase libcutils libutils zlib"
DEPENDS += "liblog"

ANDROID_MIRROR = "android.googlesource.com"
S = "${WORKDIR}/git"
# matches with android11-mainline-release release
SRCREV_native = "7fcf4360299b4ef13d5ac60ce792ebce619a1c7f"
SRC_URI = "git://${ANDROID_MIRROR}/platform/frameworks/native;name=native;protocol=https;nobranch=1;destsuffix=git \
           file://atrace_le.patch;patchdir=${S} \
          "

LDFLAGS += "-lpthread -lbase -llog -lutils -lcutils -lz"
do_compile() {
 ${CXX} ${S}/cmds/atrace/atrace.cpp ${CFLAGS} ${CPPFLAGS} ${LDFLAGS} -o atrace
}

do_install() {
     install -d ${D}${bindir}
     install -m 0755 atrace ${D}${bindir}
}
