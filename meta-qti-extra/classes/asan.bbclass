#Copyright (c) 2023 Qualcomm Innovation Center, Inc. All rights reserved.
#SPDX-License-Identifier: BSD-3-Clause-Clear

# Add AddressSanitizer function for recipes.
python __anonymous() {
    # Determine whether this function needs to be turned on
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

    if "native" in recipe_name or "linux" in recipe_name or "packagegroup" in recipe_name or "kernel" in recipe_name:
        return

    if recipe_name in ['synergy', 'system-core-adbd', 'libuhab', 'wayland-ivi-extension', 'btcli', 'qcrosvm', 'audio-chime']:
        return

    #tmp for hgy
    if recipe_name in ['compute-resmgr', 'safetylibs', 'libtraceevent', 'cntvct-log', 'glink-service-lrm', 'video-driver']:
        return

    d.appendVar('DEPENDS', ' gcc-sanitizers')
    d.appendVar('LDFLAGS', ' -lasan')

    # Inherit meson, needs add compile flags at meson-configure file additionally
    if recipe_name in ['gstreamer1.0-plugin-qvais', 'gstreamer1.0-plugins-qvrate', 'weston-sdm-extension', 'gstreamer1.0-qvconv', 'gstreamer1.0-plugins-drmdecryptor', 'gstreamer1.0-plugins-vesdeliver', 'gstreamer1.0-plugins-qvdeinterlace', 'gstreamer1.0-plugins-codec2', 'gstreamer1.0-plugins-qeavb']:
        d.appendVar('EXTRA_OEMESON', ' -DASAN=true')
        return

    if recipe_name in ['fastcv', 'fastcv-noship']:
        d.appendVar('EXTRA_OECONF', ' --enable-asan')
        return

    d.appendVar('CFLAGS', ' -fsanitize=address')
    d.appendVar('CPPFLAGS', ' -fsanitize=address')

    # Using Makefile, needs add link library additionally
    if recipe_name in ['wlan-sigma-dut', 'libnpu', 'wpa-supplicant', 'hostap-daemon-qcacld', 'hsi2s-qmi-test']:
        d.appendVar('EXTRA_OEMAKE', ' ASAN=y')

    # Using CMakefile.txt, needs add link library additionally
    if recipe_name in ['ais', 'softsku-daemon', 'safetymonitor', 'libkiumd', 'fadas', 'compute-resmon', 'camera-qcx']:
        d.appendVar('EXTRA_OECMAKE', ' -DASAN=ON')

    # TOOLCHAIN is clang, need libclang_rt.asan.a
    if recipe_name in ['libtpp', 'media-external', 'media-codec2', 'media-noship', 'codec2', 'codec2-app', 'codec2-service', 'vidc-test-app', 'vidc-enc-test', 'vidc-dec-test']:
        d.appendVar('DEPENDS', ' compiler-rt')
}

ROOTFS_POSTPROCESS_COMMAND:append = " ${@bb.utils.contains("DISTRO_FEATURES", "asan", "add_asan_preload;", "", d)}"
#add some asan option for service
add_asan_preload() {
service_etc_list="\
 safetymonitor.service \
 apss_stl.service\
"
service_lib_list="\
 ab-updater.service \
 eva.service \
 evastl.service \
 ssgtz-daemon.service \
 qcx_be_server.service \
 gsl_hab_server.service \
 kgsl.service \
 glink-service-lrm.service \
 weston.service \
 qcx_server.service \
 video-driver.service \
 vhost-device-ssr.service \
 hyp-video-be.service \
 gptp.service \
 audio-chime.service \
 compute-resmon.service \
 compute-resmgr.service\
"
service_odr_list="\
 ${IMAGE_ROOTFS}${systemd_unitdir}/system/pdmapper.service \
 ${IMAGE_ROOTFS}/etc/systemd/system/thermal-engine.service \
 ${IMAGE_ROOTFS}${systemd_unitdir}/system/openwfd_server_@.service\
"

    for service_etc in $service_etc_list; do
       service_etc_file="${IMAGE_ROOTFS}/etc/systemd/system/${service_etc}"
       if [ -f "$service_etc_file" ]; then
           sed -i '/^\[Service\]/a Environment="LD_PRELOAD=/usr/lib/libasan.so.6"' "$service_etc_file"
       fi
    done

    for service_lib in $service_lib_list; do
       service_lib_file="${IMAGE_ROOTFS}${systemd_unitdir}/system/${service_lib}"
       if [ -f "$service_lib_file" ]; then
           sed -i '/^\[Service\]/a Environment="LD_PRELOAD=/usr/lib/libasan.so.6"' "$service_lib_file"
       fi
    done

    # Since all service failed issues have not been resolved yet, in order to prevent them from not running，we will not detect them first and will delete them later.
    for service_path in $service_odr_list; do
       if [ -f "$service_path" ]; then
           sed -i '/^\[Service\]/a Environment="ASAN_OPTIONS=detect_odr_violation=0"' "$service_path"
       fi
    done
}