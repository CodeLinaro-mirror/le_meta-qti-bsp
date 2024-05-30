FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " file://0001-lttng-module-fix-lttng-module-compile-issue.patch \
                   file://0002-lttng-modules-fix-compile-issue-for-msm-kernel-6-1.patch"

inherit qti-kernel-arch-clang

# lock to avoid parallel compiling with techpack
do_compile[lockfiles] += "${TMPDIR}/qti-techpack.lock"

FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/kernel/${PN}/modules.order*"
