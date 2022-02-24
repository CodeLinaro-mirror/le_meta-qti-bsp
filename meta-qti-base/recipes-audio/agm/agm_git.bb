SUMMARY = "Audio Graph Manager"
DESCRIPTION = "This is the audio graph manager (AGM) service, it provides interfaces to allow mixer control and pcm plugins to interact and enable verious audio usecases."
HOMEPAGE = "http://www.codeaurora.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"
DEPENDS += "alsa-lib ar-acdb ar-osal ar-util ats glib-2.0 gsl-fe-noship libuhab linux-msm-headers spf"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/agm/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/agm/service;subpath=service;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/agm/service"

inherit autotools pkgconfig

EXTRA_OECONF += "\
    --with-glib \
    --enable-target-hypervisor \
"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
