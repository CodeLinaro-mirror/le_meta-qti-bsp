FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "\
        file://0001-Remove-Wextra-to-ignore-implicit-fallthrough-errors.patch \
        file://0002-Fixed-misleading-indentation-error-guarding-if-claus.patch \
        file://0003-Fixed-nonnull-compare-errors.patch \
"

# Only when building native
SRC_URI_append_class-native = " file://0004-Ignore-Wformat-truncation-errors-for-snprintf.patch"
