inherit qcommon

DESCRIPTION = "Data Services Open Source"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

PR = "r4"

DEPENDS += "virtual/kernel glib-2.0"

EXTRA_OECONF = "--with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include --with-glib"

SRC_URI="${CAF_LA_GIT}/platform/vendor/qcom-opensource/dataservices.git;protocol=git;nobranch=1;tag=${CAF_TAG};destsuffix=data-oss" 

S = "${WORKDIR}/data-oss"
