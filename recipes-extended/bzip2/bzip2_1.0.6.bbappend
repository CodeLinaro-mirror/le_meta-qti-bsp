FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
ALTERNATIVE_${PN} := " "

SRC_URI += "\
            file://CVE-2019-12900.patch \
"
