FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " ${@bb.utils.contains("PREFERRED_PROVIDER_virtual/kernel", "linux-msm", "file://0001-lttng-module-fix-lttng-module-compile-issue.patch ", "", d)}"
SRC_URI:append = " ${@bb.utils.contains("PREFERRED_PROVIDER_virtual/kernel", "linux-msm", "file://0002-lttng-modules-fix-compile-issue-for-msm-kernel-6-1.patch", "", d)}"
SRC_URI:append = " ${@bb.utils.contains_any('PREFERRED_PROVIDER_virtual/kernel', 'linux-qcom-custom linux-qcom-custom-rt', "file://fix-btrfs-pass-find_free_extent_ctl-to-allocator-tracepoints.patch", "" ,d)}"
SRC_URI:append = " ${@bb.utils.contains_any('PREFERRED_PROVIDER_virtual/kernel', 'linux-qcom-custom linux-qcom-custom-rt', "file://fix-mm-introduce-vma-vm_flags-wrapper-functions.patch","" ,d)}"
SRC_URI:append = " ${@bb.utils.contains_any('PREFERRED_PROVIDER_virtual/kernel', 'linux-qcom-custom linux-qcom-custom-rt', "file://fix-uuid-Decouple-guid_t-and-uuid_le-types-and-respective-macros.patch","" ,d)}"
SRC_URI:append = " ${@bb.utils.contains_any('PREFERRED_PROVIDER_virtual/kernel', 'linux-qcom-custom linux-qcom-custom-rt', "file://fix-net-add-location-to-trace_consume_skb.patch","" ,d)}"

inherit ${@bb.utils.contains('PREFERRED_PROVIDER_virtual/kernel', 'linux-msm', "qti-kernel-arch-clang", "", d)}

# lock to avoid parallel compiling with techpack
do_compile[lockfiles] += "${TMPDIR}/qti-techpack.lock"

FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/kernel/${PN}/modules.order*"
