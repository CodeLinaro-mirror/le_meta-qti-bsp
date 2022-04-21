FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:with-lsm-smack = " \
    file://zz-qseecom.rules \
"
