inherit autotools pkgconfig qcommon

DESCRIPTION = "Bluetooth application layer"
LICENSE = "Apache-2.0"
HOMEPAGE = "https://www.codeaurora.org/"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"

SRC_URI =" \
    ${CAF_LE_GIT}/platform/qcom-opensource/bt.git;protocol=${CAF_PROT};nobranch=1;tag=${CAF_TAG};destsuffix=qcom-opensource/bt \
    ${CAF_LA_GIT}/platform/vendor/qcom-opensource/bluetooth.git;protocol=${CAF_PROT};nobranch=1;tag=${CAF_TAG};destsuffix=vendor/qcom/opensource/bluetooth/hal/include;subpath=hal/include \
    ${CAF_LA_GIT}/platform/vendor/qcom-opensource/bluetooth.git;protocol=${CAF_PROT};nobranch=1;tag=${CAF_TAG};destsuffix=vendor/qcom/opensource/bluetooth/vhal/include;subpath=vhal/include \
"
SRC_URI += "file://0001-Use-srcdir-relative-paths-in-place-of-WORKSPACE.patch"

S = "${WORKDIR}/qcom-opensource/bt/bt-app/"

def get_depends():
    if "$(BASEMACHINE)" == "mdm9607":
        return  "btvendorhal gen-gatt glib-2.0 btobex"
    else:
        return   "btvendorhal gen-gatt glib-2.0 btobex audiohal"

DEPENDS  += "${@get_depends()}"

CPPFLAGS += " -I${STAGING_INCDIR}/mm-audio/qahw_api/inc"
CPPFLAGS += " -DUSE_ANDROID_LOGGING -DUSE_BT_OBEX -DUSE_LIBHW_AOSP"

CFLAGS_append = " -DUSE_ANDROID_LOGGING "
LDFLAGS_append = " -llog "

EXTRA_OECONF += " \
                --with-common-includes="${STAGING_INCDIR}/mm-audio/qahw_api/inc" \
                --with-glib \
                --with-lib-path=${STAGING_LIBDIR} \
                --with-btobex \
               "
EXTRA_OECONF += "--enable-target=${BASEMACHINE}"

FILES_${PN} += "${userfsdatadir}/misc/bluetooth/*"

do_install_append() {
        install -d ${D}${userfsdatadir}/misc/bluetooth/

        if [ -f ${S}conf/bt_app.conf ]; then
           install -m 0660 ${S}conf/bt_app.conf ${D}${userfsdatadir}/misc/bluetooth/
        fi

        if [ -f ${S}conf/ext_to_mimetype.conf ]; then
           install -m 0660 ${S}conf/ext_to_mimetype.conf ${D}${userfsdatadir}/misc/bluetooth/
        fi
}
