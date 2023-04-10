SUMMARY = "QTI package group for camera"
LICENSE = "BSD-3-Clause-Clear"

PACKAGE_ARCH = "${TUNE_PKGARCH}"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-camera \
    "

ALLOW_EMPTY:${PN} = "1"

RDEPENDS:${PN} += "\
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', '', \
        bb.utils.contains('PREFERRED_VERSION_${PREFERRED_PROVIDER_virtual/kernel}', '5.15', 'cameradlkm', '', d), d)} \
    "
