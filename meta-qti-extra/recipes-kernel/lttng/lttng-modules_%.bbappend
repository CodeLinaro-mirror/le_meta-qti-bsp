FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " file://0001-lttng-module-fix-lttng-module-compile-issue.patch"

inherit qti-kernel-arch-clang

FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/kernel/${PN}/modules.order*"
