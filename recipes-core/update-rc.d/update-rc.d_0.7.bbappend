SRC_URI = "${CAF_GIT}/platform/external/update-rc.d;protocol=https;branch=github/master \
           file://add-verbose.patch \
           file://check-if-symlinks-are-valid.patch \
          "

PR = "r6"
