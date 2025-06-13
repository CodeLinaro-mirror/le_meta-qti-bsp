FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " \
    file://0001-wayland-protocols-add-custom-position-support-in-xdg.patch \
"

do_install:append:sun() {
  install -d ${D}${datadir}/wayland-protocols/stable/gbm-buffer-backend/
  cp ${WORKSPACE}/display/vendor/qcom/opensource/display/weston/protocol/gbm-buffer-backend.xml ${D}${datadir}/wayland-protocols/stable/gbm-buffer-backend
}
