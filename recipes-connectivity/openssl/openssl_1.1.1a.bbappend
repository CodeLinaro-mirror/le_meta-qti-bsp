FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "\
        file://0001-Revert-Reduce-stack-usage-in-tls13_hkdf_expand.patch \
        file://0001-Fix-some-SSL_export_keying_material-issues.patch \
"
