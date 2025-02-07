# Package is fetch from Codelinaro to avoid downtime
SRC_URI:remove = "git://github.com/ThomasDickey/ncurses-snapshots.git;protocol=https;branch=master"
SRC_URI:prepend = "${CLO_LE_GIT}/ncurses.git;protocol=https;branch=debian/master "

# Reference: https://pages.codelinaro.org/clo/le/ncurses/-/commit/fd59fc8888518f21afe139394e05cfdbd113259c (v6.4)
# scarthgap supports ncurses v6.4
SRCREV = "fd59fc8888518f21afe139394e05cfdbd113259c"
LIC_FILES_CHKSUM = "file://COPYING;md5=c5a4600fdef86384c41ca33ecc70a4b8;endline=27"

