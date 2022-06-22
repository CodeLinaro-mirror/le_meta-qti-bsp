FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " file://0001-lttng-module-fix-lttng-module-compile-issue.patch"

inherit qti-kernel-arch-clang
