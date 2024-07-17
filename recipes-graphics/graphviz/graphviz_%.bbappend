SRC_URI:remove:class-target = "\
           file://0001-Set-use_tcl-to-be-empty-string-if-tcl-is-disabled.patch \
"

EXTRA_OECONF:append = " --enable-tcl=no"
