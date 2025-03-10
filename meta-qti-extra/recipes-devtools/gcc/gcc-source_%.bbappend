FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI += "\
    file://0001-Fix-sanitizer-compile-error.patch \
    file://0001-asan-forbid-allocator64.patch \
"
