load("//build/bazel_common_rules/dist:dist.bzl", "copy_to_dist_dir")

load(
    "//build/kernel/kleaf:kernel.bzl",
    "ddk_headers",
    "ddk_module",
)

def define_modules(target, variant):
    kernel_build_variant = "{}_{}".format(target, variant)

    kernel_build = select({
        "//build/qcom_build_extensions:qtisocrepo_true": "//soc-repo:{}_base_kernel".format(kernel_build_variant),
        "//build/qcom_build_extensions:qtisocrepo_false": "//msm-kernel:{}".format(kernel_build_variant),
    })

    ddk_deps = select({
        "//build/qcom_build_extensions:qtisocrepo_true": [
            "//soc-repo:all_headers",
        ],
        "//build/qcom_build_extensions:qtisocrepo_false": [
            "//msm-kernel:all_headers",
        ],
    })

    ddk_module(
        name = "{}_rtc_host".format(kernel_build_variant),
        kernel_build = kernel_build,
        deps = ddk_deps,
        srcs = [
            "virtio_rtc_host_driver.c",
        ],
        out = "virtio_rtc_host.ko",
        hdrs = [
            "include/uapi/linux/virtio_rtc_host_ioctl.h",
        ],
        includes = [
            "include",
        ],
    )

    copy_to_dist_dir(
        name = "{}_rtc_host_dist".format(kernel_build_variant),
        data = [
            ":{}_rtc_host".format(kernel_build_variant),
        ],
        dist_dir = "out/target/product/{}/dlkm/lib/modules/".format(target),
        flat = True,
        wipe_dist_dir = False,
        allow_duplicate_filenames = False,
        mode_overrides = {"**/*": "644"},
    )

def define_uapi_headers():
    ddk_headers(
        name = "rtc_host_uapi_headers",
        hdrs = ["include/uapi/linux/virtio_rtc_host_ioctl.h"],
        visibility = ["//visibility:public"],
    )
