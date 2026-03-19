FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://0001-del-crypto-selftests-pk.c.patch"

EXTRA_OECONF += "  \
    --disable-tests \
"
