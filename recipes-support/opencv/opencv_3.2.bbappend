FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI_append +=  " \
       file://0001-Fix-compile-error-of-fp16.patch \
"

