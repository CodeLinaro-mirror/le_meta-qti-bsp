SUMMARY = "display Library"
DESCRIPTION = "Provide display HAL (Hardware Abstraction Layer) \
libraries. These libraries serves as an abstraction layer between \
physical hardware and software. They provide display driver interfaces, \
allowing program to communicate with the hardware."
HOMEPAGE = "https://www.codeaurora.org/"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

DEPENDS += "binder \
            display-commonsys-intf-linux \
            drm \
            gbm-headers \
            libdrm \
            libhardware \
            linux-msm-headers \
            system-core \
"

PR = "r8"

SRC_URI = "${PATH_TO_REPO}/display/display-hal/.git;protocol=${PROTO};destsuffix=display/display-hal;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/display/display-hal"

inherit autotools-brokensep pkgconfig

EXTRA_OECONF += "--with-sanitized-headers=${STAGING_INCDIR}/linux-msm"
EXTRA_OECONF += "--enable-sdmhaldrm"

LDFLAGS += "-llog -lhardware -lutils -lcutils"

CPPFLAGS += "-DCOMPILE_DRM"
CPPFLAGS += "-DTARGET_HEADLESS"
CPPFLAGS += "-DVENUS_COLOR_FORMAT"
CPPFLAGS += "-DPAGE_SIZE=4096"
CPPFLAGS += "-I${WORKDIR}/display/display-hal/libdrmutils"
CPPFLAGS += "-I${WORKDIR}/display/display-hal/gpu_tonemapper"
CPPFLAGS += "-I${WORKDIR}/display/display-hal/libqdutils"
CPPFLAGS += "-I${WORKDIR}/display/display-hal/libqservice"
CPPFLAGS += "-I${WORKDIR}/display/display-hal/sdm/include"
CPPFLAGS += "-I${WORKDIR}/display/display-hal/include"
CPPFLAGS += "-I${WORKDIR}/display/display-hal/libdebug"
CPPFLAGS += "-I${STAGING_INCDIR}/libdrm"

# fix for uapi msm_drm.h header file related compilation issue
CPPFLAGS += "-fno-operator-names"

# add display techpack headers
CPPFLAGS += "-I${STAGING_INCDIR}/linux-msm/display"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
