FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append_with-lsm-smack = " \
    file://zz-qseecom.rules \
"
