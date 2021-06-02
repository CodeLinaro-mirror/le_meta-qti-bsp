SUMMARY = "Android utils library for C"
DESCRIPTION = "This library provides set of fundamental routines which are \
essential to basically any Unix utility or daemon application written in C."
HOMEPAGE = "http://developer.android.com/"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://NOTICE;md5=9645f39e9db895a4aa6e02cb57294595"

DEPENDS += "liblog"

PR = "r1"

SRCREV = "${AUTOREV}"
SRC_URI = "\
    ${PATH_TO_REPO}/system/core/.git;protocol=${PROTO};destsuffix=system/core;usehead=1 \
    file://0001-libcutils-Remove-autotools-support.patch;patchdir=../ \
    https://source.codeaurora.org/quic/la/platform/system/core/patch/?id=c2d8aad8d70aeb4d50f077f552044b85ef6c64b9;downloadfilename=0001-libcutils-ashmem-fortify-and-comply-with-Android-cod.patch;patchdir=../;name=patch1 \
    https://source.codeaurora.org/quic/la/platform/system/core/patch/?id=1186f3a5ad6581fae6e284fef4bfcefe50462cda;downloadfilename=0002-libcutils-ashmem-check-fd-validity.patch;patchdir=../;name=patch2 \
    https://source.codeaurora.org/quic/la/platform/system/core/patch/?id=e37111d7516827489232c6c894e114a58952fe4a;downloadfilename=0003-libcutils-ashmem-print-error-message-for-invalid-fd.patch;patchdir=../;name=patch3 \
    https://source.codeaurora.org/quic/la/platform/system/core/patch/?id=53c0ca6520528f53aa9ed3368e5c6fcbd3152851;downloadfilename=0004-libcutils-abort-for-invalid-fd.patch;patchdir=../;name=patch4 \
    https://source.codeaurora.org/quic/la/platform/system/core/patch/?id=ee431112ff0d9bab8b4bd4259adc361d46f130cc;downloadfilename=0005-libcutils-Add-ashmem_valid-function.patch;patchdir=../;name=patch5 \
    file://0001-libcutils-Add-autotools-support.patch;patchdir=../ \
"
SRC_URI[patch1.md5sum] = "e84ac5eb35c16ec10dd537393b7f0cd5"
SRC_URI[patch1.sha256sum] = "88d5e213db61aa65c3e4def24722d2bc24af27d0ecefe5ae9e389819a4026f11"
SRC_URI[patch2.md5sum] = "23e846e5f788d8354f22bfcca1b97dfa"
SRC_URI[patch2.sha256sum] = "090299fe5acb998918d0a970318f4a8a3d6ffd8511bcff7ae50fa27cbdd92399"
SRC_URI[patch3.md5sum] = "0e1e2636aa36114362293103c8da5224"
SRC_URI[patch3.sha256sum] = "c1b52d57dc6a04653c19669238cc7ad9dd833cc77631c90d4ef409f3caa35132"
SRC_URI[patch4.md5sum] = "8a70aab2809b83730b48369c37f291ba"
SRC_URI[patch4.sha256sum] = "a382117538b0a2fb1cfecb0f2dfd4ee6a22a6baff5a710853dedfe72203b5d1c"
SRC_URI[patch5.md5sum] = "6eaf69fbe8a793c46442ea36682a9a7c"
SRC_URI[patch5.sha256sum] = "bd1b2192105aec07f7d8388beefb5e7200da69c9d50ab1cd7789eadb090c090b"

S = "${WORKDIR}/system/core/libcutils"

inherit autotools pkgconfig

EXTRA_OECONF += "\
    --with-core-includes=${WORKDIR}/system/core/include \
    --with-host-os=${HOST_OS} \
    --disable-static \
    LE_PROPERTIES_ENABLED=true \
"

do_install_append() {
    ln -sf ../private/android_filesystem_capability.h ${D}${includedir}/cutils/android_filesystem_capability.h
    ln -sf ../private/android_filesystem_config.h ${D}${includedir}/cutils/android_filesystem_config.h
}

BBCLASSEXTEND = "native"
