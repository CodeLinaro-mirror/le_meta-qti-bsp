EXTRA_OEMAKE:append = " UAPIDIR=${includedir}"

do_install:append() {
    oe_runmake install_uapi_headers
}

BBCLASSEXTEND = "native nativesdk"
