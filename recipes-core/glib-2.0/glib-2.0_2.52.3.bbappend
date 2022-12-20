FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "\
            file://CVE-2019-12450.patch \
            file://0001-gdbus-avoid-printing-null-strings.patch \
"
