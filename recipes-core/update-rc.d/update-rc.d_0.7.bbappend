SRC_URI = "${GIT_URI}/platform/external/update-rc.d;protocol=${PROTOCOL};branch=${BRANCH_PREFIX}github/master \
           file://add-verbose.patch \
           file://check-if-symlinks-are-valid.patch \
          "

PR = "r6"
