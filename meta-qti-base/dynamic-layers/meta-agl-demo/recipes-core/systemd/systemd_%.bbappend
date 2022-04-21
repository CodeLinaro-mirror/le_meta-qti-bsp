FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append_with-lsm-smack = " \
    file://zz-qseecom.rules \
"
