# Fetch project from a codelinaro Mirror
SRC_URI = "${CLO_LE_GIT}/x264.git;branch=caf_migration/x264/stable;protocol=https"
SRC_URI += "\
              file://don-t-default-to-cortex-a9-with-neon.patch \
              file://Fix-X32-build-by-disabling-asm.patch \
              "
