#SRC_URI is pointing to CAF
SRC_URI = "git://source.codeaurora.org/quic/le/libsolv.git;protocol=https;branch=libsolv/master"

SRC_URI += " \
           file://0001-repo_rpmdb.c-increase-MAX_HDR_CNT-and-MAX_HDR_DSIZE.patch \
          "


