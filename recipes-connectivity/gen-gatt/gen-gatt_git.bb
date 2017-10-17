inherit autotools pkgconfig qcommon

DESCRIPTION = "Bluetooth Generic Gatt Interface"
LICENSE = "BSD"
HOMEPAGE = "https://www.codeaurora.org/"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

DEPENDS += "glib-2.0 system-core"
LDFLAGS_append = " -llog "

SRC_URI =" \
    ${CAF_LE_GIT}/platform/qcom-opensource/bt.git;protocol=git;nobranch=1;tag=${CAF_TAG};destsuffix=qcom-opensource/bt/gatt;subpath=gatt \
    ${CAF_LE_GIT}/platform/qcom-opensource/bt.git;protocol=git;nobranch=1;tag=${CAF_TAG};destsuffix=qcom-opensource/bt/bt-app;subpath=bt-app \
    ${CAF_LA_GIT}/platform/vendor/qcom-opensource/bluetooth.git;protocol=git;nobranch=1;tag=${CAF_TAG};destsuffix=vendor/qcom/opensource/bluetooth/hal/include;subpath=hal/include \
    ${CAF_LA_GIT}/platform/vendor/qcom-opensource/bluetooth.git;protocol=git;nobranch=1;tag=${CAF_TAG};destsuffix=vendor/qcom/opensource/bluetooth/vhal/include;subpath=vhal/include \
"

S = "${WORKDIR}/qcom-opensource/bt/gatt/"

EXTRA_OECONF = "--with-glib"
