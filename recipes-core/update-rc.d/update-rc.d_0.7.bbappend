SRC_URI = "${CAF_LE_GIT}/platform/external/update-rc.d;protocol=${CAF_PROT};branch=github/master \
           file://add-verbose.patch \
           file://check-if-symlinks-are-valid.patch \
          "

PR = "r6"
