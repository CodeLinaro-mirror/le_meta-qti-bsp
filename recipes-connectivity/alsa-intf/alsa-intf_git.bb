inherit qcommon

DESCRIPTION = "ALSA Framework Library"
LICENSE = "Apache-2.0"
PR = "r5"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "acdbloader glib-2.0"

SRC_URI = " \
    ${CAF_LA_GIT}/platform/vendor/qcom-opensource/kernel-tests/mm-audio.git;protocol=git;nobranch=1;tag=${CAF_TAG};destsuffix=qcom-opensource/mm-audio \
"
prefix="/etc"

S = "${WORKDIR}/qcom-opensource/mm-audio/"

EXTRA_OECONF += "--prefix=/etc \
                 --with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include \
                 --with-glib \
                 --with-acdb"

FILES_${PN} += "${prefix}/snd_soc_msm/*"
