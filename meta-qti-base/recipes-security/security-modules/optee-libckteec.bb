SUMMARY = "PKCS11 OP-TEE Client library"
DESCRIPTION = "OP-TEE PKCS#11 (Public-Key Cryptography Standard #11) Client-side library."
HOMEPAGE = "https://github.com/OP-TEE/optee_client"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;beginline=7;endline=27;md5=f6c51da2169a596879f7e3714c4e47b3"

DEPENDS = "securemsm"

SRC_URI = "git://github.com/OP-TEE/optee_client.git;protocol=https;branch=master"
SRC_URI:append = " file://0001-libckteec-Replace-libteec-library-with-libgptee.patch file://0001-securemsm-ship-Add-support-for-HKDF_DERIVE-mechanism.patch file://0002-fix-memory-leak-in-serialize_indirect_attribute.patch file://0003-libckteec-Add-GVM-specific-PKCS11-UUID-support.patch"

SRCREV = "3eac340a781c00ccd61b151b0e9c22a8c6e9f9f0"

S = "${WORKDIR}/git"

inherit pkgconfig

CFLAGS:append:gvm-gen4-5 = " -DQC_ADD_ONS -DPKCS11_TA_UUID_LSB=0xF0"

CFLAGS:append:quin-gvm-gen4-5 = " -DQC_ADD_ONS -DPKCS11_TA_UUID_LSB=0xF0"

do_compile() {
    oe_runmake -C ${S}/libckteec
}

do_install:append() {
    install -d ${D}${libdir}
    install -m 755 ${S}/out/libckteec/libckteec.* ${D}${libdir}/
    install -d ${D}/${includedir}
    install -m 0755 ${S}/libckteec/include/* ${D}${includedir}/
}

FILES:${PN} += "${libdir}/libckteec.so*"
SOLIBS = ".so"
FILES_SOLIBSDEV = ""
