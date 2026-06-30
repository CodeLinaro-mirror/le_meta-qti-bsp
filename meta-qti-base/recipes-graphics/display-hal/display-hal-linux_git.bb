SUMMARY = "display Library"
DESCRIPTION = "Provide display HAL (Hardware Abstraction Layer) \
libraries. These libraries serves as an abstraction layer between \
physical hardware and software. They provide display driver interfaces, \
allowing program to communicate with the hardware."
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

DEPENDS += "display-commonsys-intf-linux \
            drm \
            gbm-headers \
            libdrm \
            libhardware \
            virtual/kernel-headers \
            system-core \
            ${@bb.utils.contains_any("PREFERRED_VERSION_linux-msm", '5.15 6.1 6.12', 'displaydlkm', '', d)} \
            ${@bb.utils.contains('MACHINE_FEATURES', 'qti-umd', 'display-kernel-headers', '', d)} \
"
DEPENDS:append:gvm-gen5 = " display-intf-headers"
PR = "r8"

CODE_DIR = "display/display-hal"
CODE_DIR:gvm-gen5 = "vendor/qcom/opensource/display-core"
SRC_URI = "${PATH_TO_REPO}/${CODE_DIR}/.git;protocol=${PROTO};destsuffix=${CODE_DIR};usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/${CODE_DIR}"

inherit autotools-brokensep pkgconfig

EXTRA_OECONF += "--with-sanitized-headers=${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel}"
EXTRA_OECONF += "--enable-sdmhaldrm"

LDFLAGS += "-llog -lhardware -lutils -lcutils"

CPPFLAGS += "-DCOMPILE_DRM"
CPPFLAGS += "-DTARGET_HEADLESS"
CPPFLAGS += "-DVENUS_COLOR_FORMAT"
CPPFLAGS += "-DPAGE_SIZE=4096"
CPPFLAGS += "-I${WORKDIR}/${CODE_DIR}/libdrmutils"
CPPFLAGS += "-I${WORKDIR}/${CODE_DIR}/gpu_tonemapper"
CPPFLAGS += "-I${WORKDIR}/${CODE_DIR}/libqdutils"
CPPFLAGS += "-I${WORKDIR}/${CODE_DIR}/libqservice"
CPPFLAGS += "-I${WORKDIR}/${CODE_DIR}/sdm/include"
CPPFLAGS += "-I${WORKDIR}/${CODE_DIR}/include"
CPPFLAGS += "-I${WORKDIR}/${CODE_DIR}/libdebug"
CPPFLAGS += "-I${STAGING_INCDIR}/libdrm"
CPPFLAGS:append:gvm-gen5 = " -DTRUSTED_VM"
CPPFLAGS:remove:gvm-gen5 = "-DTARGET_HEADLESS"

# fix for uapi msm_drm.h header file related compilation issue
CPPFLAGS += "-fno-operator-names"

# add display techpack headers
CPPFLAGS += "-I${STAGING_INCDIR}/${PREFERRED_PROVIDER_virtual/kernel}/display"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
