FILESEXTRAPATHS_prepend := "${THISDIR}/${BPN}:"

WIFILES_append = " \
    file://zz-kgsl.rules.in \
    file://zz-ion.rules.in \
"
