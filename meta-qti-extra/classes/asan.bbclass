#Copyright (c) 2023 Qualcomm Innovation Center, Inc. All rights reserved.
#SPDX-License-Identifier: BSD-3-Clause-Clear

# Add AddressSanitizer function for recipes.
python __anonymous() {
    #Determine whether this function needs to be turned on
    is_turn = bb.utils.contains('DISTRO_FEATURES', 'asan', True, False, d)
    if not is_turn:
        return

    # Determine whether it is a bb file
    recipe_file = d.getVar('FILE', True)
    is_bb = recipe_file.endswith('.bb')
    if not is_bb:
        return

    # Determine whether it is under meta-qti-xxx path
    pre_pathname = d.getVar('PATH_TO_REPO')
    repo_path = pre_pathname.replace("git://", "")

    is_qti_path = recipe_file.startswith(repo_path + "/meta-qti-bsp")
    if not is_qti_path:
        return

    # Determine whether it is a native build type, if it is, it will not inherit
    is_native = bb.utils.contains('BBCLASSEXTEND', 'native', True, False, d)
    if is_native:
        return

    recipe_name = d.getVar('PN', True)

    if "native" in recipe_name or "linux-msm" in recipe_name or "packagegroup" in recipe_name:
        return

    is_clang = bb.utils.contains('TOOLCHAIN', 'clang', True, False, d)
    if is_clang:
        return

    #Because all dependent libraries cannot link to asan, these recipes are temporarily blocked.
    if recipe_name in ['wlan-sigma-dut', 'qcacld32-ll-hmt', 'qcacld32-ll-hasting-cnss2', 'qcacld32-ll-hasting-cnss0', 'qcacld32-ll-hsp', 'gstreamer1.0-plugins-vesdeliver', 'btcli', 'system-core-adbd', 'libuhab']:
        return

    if recipe_name in ['adreno', 'gbm-headers', 'gbm', 'wpa-supplicant', 'hostap-daemon-qcacld', 'libnpu', 'libion', 'libsync', 'libdmabufheap', 'gstreamer1.0-qvconv', 'wayland-ivi-extension']:
        return

    if recipe_name in ['libutils', 'libhardware', 'weston-sdm-extension', 'weston-sdm-extension-headers', 'display-hal-linux', 'display-hal-headers', 'display-commonsys-intf-linux', 'acdbloaderservice']:
        return

    d.appendVar('DEPENDS', ' gcc-sanitizers')
    d.appendVar('CFLAGS', ' -fsanitize=address')
    d.appendVar('LDFLAGS', ' -lasan')
    d.appendVar('CPPFLAGS', ' -fsanitize=address')
}

ROOTFS_POSTPROCESS_COMMAND:append = " ${@bb.utils.contains("DISTRO_FEATURES", "asan", "add_asan_preload;", "", d)}"

#add some asan option for service
add_asan_preload() {
    if [ -f "${IMAGE_ROOTFS}${systemd_unitdir}/system/agm.service" ]; then
        sed -i '/^\[Service\]/a Environment="ASAN_OPTIONS=detect_odr_violation=0"' ${IMAGE_ROOTFS}${systemd_unitdir}/system/agm.service
        sed -i '/^\[Service\]/a Environment="LD_PRELOAD=/usr/lib/libasan.so.6"' ${IMAGE_ROOTFS}${systemd_unitdir}/system/agm.service
    fi
    if [ -f "${IMAGE_ROOTFS}${systemd_unitdir}/system/init_codec2.service" ]; then
        sed -i '/^\[Service\]/a Environment="LD_PRELOAD=/usr/lib/libasan.so.6"' ${IMAGE_ROOTFS}${systemd_unitdir}/system/init_codec2.service
    fi
    if [ -f "${IMAGE_ROOTFS}${systemd_unitdir}/system/pdmapper.service" ]; then
        sed -i '/^\[Service\]/a Environment="ASAN_OPTIONS=detect_odr_violation=0"' ${IMAGE_ROOTFS}${systemd_unitdir}/system/pdmapper.service
    fi
    if [ -f "${IMAGE_ROOTFS}${systemd_unitdir}/system/ab-updater.service" ]; then
        sed -i '/^\[Service\]/a Environment="LD_PRELOAD=/usr/lib/libasan.so.6"' ${IMAGE_ROOTFS}${systemd_unitdir}/system/ab-updater.service
    fi
    if [ -f "${IMAGE_ROOTFS}${systemd_unitdir}/system/ais_server.service" ]; then
        sed -i '/^\[Service\]/a Environment="LD_PRELOAD=/usr/lib/libasan.so.6"' ${IMAGE_ROOTFS}${systemd_unitdir}/system/ais_server.service
    fi
    if [ -f "${IMAGE_ROOTFS}/etc/systemd/system/thermal-engine.service" ]; then
        sed -i '/^\[Service\]/a Environment="ASAN_OPTIONS=detect_odr_violation=0"' ${IMAGE_ROOTFS}/etc/systemd/system/thermal-engine.service
    fi
}
