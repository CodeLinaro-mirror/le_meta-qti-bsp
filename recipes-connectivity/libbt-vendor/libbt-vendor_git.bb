inherit qcommon

DESCRIPTION = "Bluetooth Vendor Library"
HOMEPAGE = "http://codeaurora.org/"
LICENSE = "Apache-2.0"

LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

DEPENDS = "common system-core hci-qcomm-init glib-2.0"

RDEPENDS_${PN} = "libcutils"

SRC_URI=" \
    ${CAF_LA_GIT}/platform/hardware/qcom/bt.git;protocol=${CAF_PROT};nobranch=1;tag=${CAF_TAG};destsuffix=hardware/qcom/bt \
    ${CAF_LA_GIT}/platform/system/bt.git;protocol=${CAF_PROT};nobranch=1;tag=${CAF_TAG};subpath=hci/include;destsuffix=system/bt/hci/include \
"

S = "${WORKDIR}/hardware/qcom/bt/libbt-vendor/"

CFLAGS_append = " -DUSE_ANDROID_LOGGING "
LDFLAGS_append = " -llog "

CPPFLAGS += " -I${WORKDIR}/system/bt/hci/include"

BASEPRODUCT = "${@d.getVar('PRODUCT', False)}"

EXTRA_OECONF = "--with-common-includes="${WORKDIR}/system/bt/hci/include" \
                --with-lib-path=${STAGING_LIBDIR} \
                --enable-target=${BASEMACHINE} \
                --enable-rome=${BASEPRODUCT} \
                --with-glib=yes \
               "

FILES_${PN} += "${userfsdatadir}/misc/bluetooth/*"

do_install_append () {
    install -d ${D}${userfsdatadir}/misc/bluetooth
    install -m 755 ${S}init.msm.bt.sh ${D}${userfsdatadir}/misc/bluetooth/
}
