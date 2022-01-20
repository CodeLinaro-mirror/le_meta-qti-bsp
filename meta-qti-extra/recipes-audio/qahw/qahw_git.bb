SUMMARY = "Qahw (qti api for hardware) Library"
DESCRIPTION = "Qahw (qti api for hardware) library for audiohal."
HOMEPAGE = "https://www.codeaurora.org"
LICENSE = "BSD-3-Clause & Apache-2.0"
LIC_FILES_CHKSUM = "\
    file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9 \
    file://${COREBASE}/meta/files/common-licenses/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
"
DEPENDS += "glib-2.0 libcutils libhardware libhardware-headers liblog system-media"
SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/audio-hal/primary-hal/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/audio-hal/primary-hal/qahw;subpath=qahw;usehead=1"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/audio-hal/primary-hal/qahw"

inherit autotools pkgconfig

EXTRA_OECONF = "\
    --with-glib \
    BOARD_SUPPORTS_SVA_AUDIO_CONCURRENCY=true \
"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
