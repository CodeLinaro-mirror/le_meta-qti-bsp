FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:sa8775 = " \
    file://sh_login.patch \
"
SRC_URI:append:sa7255 = " \
    file://sh_login.patch \
"
