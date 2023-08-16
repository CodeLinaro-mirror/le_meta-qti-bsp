#SRC_URI is pointing to codelinaro
SRC_URI = "${CLO_LE_GIT}/libsolv.git;protocol=https;branch=caf_migration/libsolv/master"

SRC_URI += " \
           file://0001-repo_rpmdb.c-increase-MAX_HDR_CNT-and-MAX_HDR_DSIZE.patch \
          "


