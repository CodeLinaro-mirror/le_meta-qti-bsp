FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI = "git://github.com/GENIVI/${BPN}.git;protocol=http;branch=master \
    file://0001-Change-header-file-path-to-adapt-to-weston-8.0.0.patch \
    file://0001-Fix-link-error-of-variables-multiple-definition.patch \
    "
SRCREV = "cfdb7a7402a12600daedddb46da2e360359cc05b"
