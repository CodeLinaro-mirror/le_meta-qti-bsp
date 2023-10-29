SUMMARY = "A small image just capable of allowing a device to boot."
LICENSE = "BSD-3-Clause-Clear"

require recipes-core/images/core-image-minimal.bb
require recipes-products/images/qti-image-minimal-prop.bb

IMAGE_INSTALL += "\
    packagegroup-qti-core-minimal \
    "
