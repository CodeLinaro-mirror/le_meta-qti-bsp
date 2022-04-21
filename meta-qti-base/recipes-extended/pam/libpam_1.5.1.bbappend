FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " file://0001-do-not-use-crypt_checksalt-when-checking-for-password-expired.patch"
