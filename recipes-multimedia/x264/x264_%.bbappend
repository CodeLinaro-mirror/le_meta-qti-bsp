# Fetch project from a CAF Mirror
SRC_URI = "git://source.codeaurora.org/quic/le/x264.git;branch=x264/stable;protocol=https"
SRC_URI += "\
              file://don-t-default-to-cortex-a9-with-neon.patch \
              file://Fix-X32-build-by-disabling-asm.patch \
              "
