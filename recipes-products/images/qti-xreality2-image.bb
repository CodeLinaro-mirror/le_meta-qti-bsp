# Provides packages required to build
# QTI Linux eXtended Reality image with splitxr support.

require qti-xreality2-base-image.bb

CORE_IMAGE_EXTRA_INSTALL:append:seraph = " diag-router"

IMAGE_INSTALL:append:seraph = " display-fw"

CORE_IMAGE_EXTRA_INSTALL:remove:seraph = " \
    packagegroup-qti-bluetooth \
    packagegroup-qti-securemsm \
    packagegroup-qti-sensors-see \
    packagegroup-qti-test-sensors-see \
    packagegroup-qti-wifi \
    packagegroup-qti-qesdk-core \
    packagegroup-qti-dcf-lib \
    packagegroup-qti-dcf-hal \
    powerapp \
    powerapp-powerconfig \
    libmeminfo \
    kernel-modules \
"

CORE_IMAGE_EXTRA_INSTALL:append:seraph = " packagegroup-qcom-sensors"
