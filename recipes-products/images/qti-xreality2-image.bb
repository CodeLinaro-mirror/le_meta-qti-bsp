# Provides packages required to build
# QTI Linux eXtended Reality image with splitxr support.

require qti-xreality2-base-image.bb

CORE_IMAGE_EXTRA_INSTALL:append:seraph = " diag-router"

CORE_IMAGE_EXTRA_INSTALL:remove:seraph = " \
    packagegroup-qti-audio \
    packagegroup-qti-bluetooth \
    packagegroup-qti-camera \
    packagegroup-qti-camera-kernel \
    packagegroup-qti-core \
    packagegroup-qti-display \
    packagegroup-qti-eva \
    packagegroup-qti-dsp \
    packagegroup-qti-fastcv \
    packagegroup-qti-gfx \
    packagegroup-qti-mmframeworks \
    packagegroup-qti-ppat \
    packagegroup-qti-securemsm \
    packagegroup-qti-sensors-see \
    packagegroup-qti-ss-mgr \
    packagegroup-qti-test-sensors-see \
    packagegroup-qti-video \
    packagegroup-qti-wifi \
    packagegroup-qti-perf \
    packagegroup-qti-qesdk-core \
    packagegroup-qti-dcf-lib \
    packagegroup-qti-dcf-hal \
    powerapp \
    powerapp-powerconfig \
    libmeminfo \
    kernel-modules \
"
