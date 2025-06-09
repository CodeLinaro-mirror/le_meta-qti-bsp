SUMMARY = "HOST SELinux policy build"
DESCRIPTION = "build GNU/Linux SELinux policies for LV HOST"
HOMEPAGE = "https://selinuxproject.org/"
SECTION = "admin"
LICENSE = "GPLv2 & BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=393a5ca445f6965873eca0259a17f833 \
                    file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause-Clear;md5=7a434440b651f4a472ca93716d01033a"
DEPENDS += "bzip2-replacement-native secilc-native checkpolicy-native policycoreutils-native semodule-utils-native m4-native sepolicy-cil-native"
SRCREV = "ad7217f906e89c49835fdc305110a97b56865442"
SRC_URI = "\
    git://github.com/SELinuxProject/refpolicy.git;protocol=http;branch=master;name=refpolicy;destsuffix=refpolicy \
    file://vendor-modules \
    file://host-refpolicy \
"
S = "${WORKDIR}/refpolicy"
S_GIT_REFPOLICY = "${WORKDIR}/refpolicy"
S_HOST_REFPOLICY = "${WORKDIR}/host-refpolicy"
S_HOST_MODULES = "${WORKDIR}/vendor-modules"
S_ANDROID_CILS = "${STAGING_DATADIR_NATIVE}/android_cils"
S_PRECOMBINED_CILS = "${WORKDIR}/precombined_cils"
EXTRA_OEMAKE += "\
    NAME=${POLICY_NAME} \
    TYPE=${POLICY_TYPE} \
    DISTRO=redhat \
    UBAC=n \
    UNK_PERMS=allow \
    DIRECT_INITRC=n \
    SYSTEMD=${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'y', 'n', d)} \
    MONOLITHIC=n \
    QUIET=y \
    MLS_SENS=16 \
    MLS_CATS=1024 \
    MCS_CATS=1024 \
    tc_usrbindir=${STAGING_BINDIR_NATIVE} \
    OUTPUT_POLICY=`${STAGING_BINDIR_NATIVE}/checkpolicy -V | cut -d' ' -f1` \
"
PACKAGE_ARCH = "${MACHINE_ARCH}"
PROVIDES += "virtual/refpolicy"
RPROVIDES:${PN} += "refpolicy"
FILES:${PN} += "\
    ${sysconfdir}/selinux/${POLICY_NAME}/ \
    ${datadir}/selinux/${POLICY_NAME}/*.pp \
    ${localstatedir}/lib/selinux/${POLICY_NAME}/ \
"
FILES:${PN}-dev += "\
    ${datadir}/selinux/${POLICY_NAME}/include/ \
    ${sysconfdir}/selinux/sepolgen.conf \
"
DEFAULT_ENFORCING ??= "permissive"
POLICY_TYPE = "qti"
POLICY_NAME = "qti"

fix_platcil() {
    cilfile="$1"
    sed -i '1i(type audio_property)' ${cilfile}
    sed -i '1i(type tunnel_audio_property)' ${cilfile}
    sed -i '1i(type av_property)' ${cilfile}
    sed -i '1i(type nfc_property)' ${cilfile}
    sed -i '1i(type camera_property)' ${cilfile}
    sed -i '1i(type debug_property)' ${cilfile}
    sed -i '1i(type lmk_property)' ${cilfile}
    sed -i '1i(type config_property)' ${cilfile}
    sed -i '1i(type systemd_journal)' ${cilfile}
    sed -i '1i(type system_bootstrap_lib_file)' ${cilfile}
    sed -i '1i(type adsprpcd_file)' ${cilfile}
    sed -i '1i(type bt_firmware_file)' ${cilfile}
    sed -i '1i(type vendor_ssgtzd_exec)' ${cilfile}
    sed -i '1i(type vendor_cdsprpcd_exec)' ${cilfile}
    sed -i '1i(type media_property)' ${cilfile}
    sed -i '1i(type persist_property)' ${cilfile}
    sed -i '1i(type vendor_buildprop)' ${cilfile}
    sed -i '1i(type carwatchdogd)' ${cilfile}
    sed -i '1i(type service)' ${cilfile}
    sed -i '1i(type dbus)' ${cilfile}
    sed -i '1i(type adsprpcd_file)' ${cilfile}
    sed -i '1i(type vendor_sysfs_memory_offline)' ${cilfile}
    sed -i '1i(type cgroup_bpf)' ${cilfile}
    sed -i '1i(type ais_v4l2_proxy)' ${cilfile}
    sed -i '1i(type vendor_hal_automotive_vehicle_qti)' ${cilfile}
    sed -i 's/(typeattributeset domain (/&carwatchdogd /' ${cilfile}
    sed -i 's/(class system (ipc_info syslog_read syslog_mod syslog_console module_request module_load /&halt reboot status start stop enable disable reload /' ${cilfile}
    sed -i 's/(common cap2 (mac_override mac_admin syslog wake_alarm block_suspend audit_read perfmon /&bpf /' ${cilfile}
    sed -i 's/keystore2_key diced drmservice /&service dbus passwd /' ${cilfile}
    sed -i '$a (class passwd ( passwd chfn chsh rootok crontab ))' ${cilfile}
}

fix_platcil_ecrm() {
    cilfile="$1"
    sed -i '1i(type qtidiagservices_app)' ${cilfile}
    sed -i '1i(type vendor_display_notch_prop)' ${cilfile}
    sed -i '1i(type vendor_display_prop)' ${cilfile}
    sed -i '1i(type vendor_dynamic_sensor_prop)' ${cilfile}
    sed -i '1i(type vendor_ese_prop)' ${cilfile}
    sed -i '1i(type vendor_ese_strongbox_prop)' ${cilfile}
    sed -i '1i(type vendor_fda_prop)' ${cilfile}
    sed -i '1i(type vendor_fst_prop)' ${cilfile}
    sed -i '1i(type vendor_gpu_prop)' ${cilfile}
    sed -i '1i(type vendor_hiber_prop)' ${cilfile}
    sed -i '1i(type vendor_hvdcp_opti_prop)' ${cilfile}
    sed -i '1i(type vendor_ims_prop)' ${cilfile}
    sed -i '1i(type vendor_initsvc_bootanim_prop)' ${cilfile}
    sed -i '1i(type vendor_iop_prop)' ${cilfile}
    sed -i '1i(type vendor_ipa_lnx_agent_prop)' ${cilfile}
    sed -i '1i(type vendor_ipacm-diag_prop)' ${cilfile}
    sed -i '1i(type vendor_ipacm_prop)' ${cilfile}
    sed -i '1i(type vendor_km_strongbox_version_prop)' ${cilfile}
    sed -i '1i(type vendor_location_prop)' ${cilfile}
    sed -i '1i(type vendor_lpm_prop)' ${cilfile}
    sed -i '1i(type vendor_media_performance_class)' ${cilfile}
    sed -i '1i(type vendor_mm_osal_prop)' ${cilfile}
    sed -i '1i(type vendor_mm_parser_prop)' ${cilfile}
    sed -i '1i(type vendor_modem_diag_prop)' ${cilfile}
    sed -i '1i(type vendor_modprobe_prop)' ${cilfile}
    sed -i '1i(type vendor_mpctl_prop)' ${cilfile}
    sed -i '1i(type vendor_mwqem_prop)' ${cilfile}
    sed -i '1i(type vendor_myapp_prop)' ${cilfile}
    sed -i '1i(type vendor_nfc_nq_prop)' ${cilfile}
    sed -i '1i(type vendor_pasr_prop)' ${cilfile}
    sed -i '1i(type vendor_pcie_prop)' ${cilfile}
    sed -i '1i(type vendor_pd_locater_dbg_prop)' ${cilfile}
    sed -i '1i(type vendor_persist_rcs_prop)' ${cilfile}
    sed -i '1i(type vendor_persist_tcm_prop)' ${cilfile}
    sed -i '1i(type vendor_procomp_prop)' ${cilfile}
    sed -i '1i(type vendor_qcc_prop)' ${cilfile}
    sed -i '1i(type vendor_qcom_wlan_prop)' ${cilfile}
    sed -i '1i(type vendor_qdcmss_prop)' ${cilfile}
    sed -i '1i(type vendor_qesdk_one_prop)' ${cilfile}
    sed -i '1i(type vendor_qesdk_ready_prop)' ${cilfile}
    sed -i '1i(type vendor_qms_prop)' ${cilfile}
    sed -i '1i(type vendor_qspm_prop)' ${cilfile}
    sed -i '1i(type vendor_qteeconnector_opti_prop)' ${cilfile}
    sed -i '1i(type vendor_qtr_prop)' ${cilfile}
    sed -i '1i(type vendor_qvirtmgr_prop)' ${cilfile}
    sed -i '1i(type vendor_qvr_persist_prop)' ${cilfile}
    sed -i '1i(type vendor_qvr_prop)' ${cilfile}
    sed -i '1i(type vendor_qvrd_persist_prop)' ${cilfile}
    sed -i '1i(type vendor_qvrd_prop)' ${cilfile}
    sed -i '1i(type vendor_radio_prop)' ${cilfile}
    sed -i '1i(type vendor_reschedule_service_prop)' ${cilfile}
    sed -i '1i(type vendor_ril_daemon_prop)' ${cilfile}
    sed -i '1i(type vendor_scroll_prop)' ${cilfile}
    sed -i '1i(type vendor_sensors_prop)' ${cilfile}
    sed -i '1i(type vendor_slm_prop)' ${cilfile}
    sed -i '1i(type vendor_soc_id_prop)' ${cilfile}
    sed -i '1i(type vendor_soc_model_prop)' ${cilfile}
    sed -i '1i(type vendor_soc_name_prop)' ${cilfile}
    sed -i '1i(type vendor_spcomlib_prop)' ${cilfile}
    sed -i '1i(type vendor_ssr_prop)' ${cilfile}
    sed -i '1i(type vendor_sxr_prop)' ${cilfile}
    sed -i '1i(type vendor_sys_video_prop)' ${cilfile}
    sed -i '1i(type vendor_sysboot_completed_prop)' ${cilfile}
    sed -i '1i(type vendor_system_prop)' ${cilfile}
    sed -i '1i(type vendor_tee_listener_prop)' ${cilfile}
    sed -i '1i(type vendor_time_service_prop)' ${cilfile}
    sed -i '1i(type vendor_usb_prop)' ${cilfile}
    sed -i '1i(type vendor_video_prop)' ${cilfile}
    sed -i '1i(type vendor_wfd_service_prop)' ${cilfile}
    sed -i '1i(type vendor_wfd_sys_debug_prop)' ${cilfile}
    sed -i '1i(type vendor_wfd_vendor_debug_prop)' ${cilfile}
    sed -i '1i(type vendor_wifi_prop)' ${cilfile}
    sed -i '1i(type vendor_wifi_version)' ${cilfile}
    sed -i '1i(type vendor_wigig_core_prop)' ${cilfile}
    sed -i '1i(type vendor_wigig_prop)' ${cilfile}
    sed -i '1i(type vendor_wlc_prop)' ${cilfile}
    sed -i '1i(type vendor_wlc_public_prop)' ${cilfile}
    sed -i '1i(type vendor_xlat_prop)' ${cilfile}
    sed -i '1i(type vendor_xrcb_prop)' ${cilfile}
    sed -i '1i(type verity_status_prop)' ${cilfile}
    sed -i '1i(type virtual_ab_prop)' ${cilfile}
    sed -i '1i(type virtual_face_prop)' ${cilfile}
    sed -i '1i(type virtual_fingerprint_prop)' ${cilfile}
    sed -i '1i(type virtualizationservice_prop)' ${cilfile}
    sed -i '1i(type wifi_config_prop)' ${cilfile}
    sed -i '1i(type wifi_hal_prop)' ${cilfile}
    sed -i '1i(type wifi_prop)' ${cilfile}
    sed -i '1i(type zram_config_prop)' ${cilfile}
    sed -i '1i(type zram_control_prop)' ${cilfile}
    sed -i '1i(type boottime_prop)' ${cilfile}
    sed -i '1i(type boottime_public_prop)' ${cilfile}
    sed -i '1i(type build_bootimage_prop)' ${cilfile}
    sed -i '1i(type build_config_prop)' ${cilfile}
    sed -i '1i(type camera2_extensions_prop)' ${cilfile}
    sed -i '1i(type camera_calibration_prop)' ${cilfile}
    sed -i '1i(type car_boot_prop)' ${cilfile}
    sed -i '1i(type carwatchdog_config_prop)' ${cilfile}
    sed -i '1i(type charger_config_prop)' ${cilfile}
    sed -i '1i(type charger_prop)' ${cilfile}
    sed -i '1i(type charger_status_prop)' ${cilfile}
    sed -i '1i(type codec2_config_prop)' ${cilfile}
    sed -i '1i(type cold_boot_done_prop)' ${cilfile}
    sed -i '1i(type composd_vm_art_prop)' ${cilfile}
    sed -i '1i(type composd_vm_vendor_prop)' ${cilfile}
    sed -i '1i(type cpu_variant_prop)' ${cilfile}
    sed -i '1i(type crashrecovery_prop)' ${cilfile}
    sed -i '1i(type ctl_adbd_prop)' ${cilfile}
    sed -i '1i(type ctl_apexd_prop)' ${cilfile}
    sed -i '1i(type ctl_artd_pre_reboot_prop)' ${cilfile}
    sed -i '1i(type ctl_bootanim_prop)' ${cilfile}
    sed -i '1i(type ctl_bugreport_prop)' ${cilfile}
    sed -i '1i(type ctl_console_prop)' ${cilfile}
    sed -i '1i(type ctl_dpmd_prop)' ${cilfile}
    sed -i '1i(type ctl_dumpstate_prop)' ${cilfile}
    sed -i '1i(type ctl_fuse_prop)' ${cilfile}
    sed -i '1i(type ctl_gsid_prop)' ${cilfile}
    sed -i '1i(type ctl_interface_restart_prop)' ${cilfile}
    sed -i '1i(type ctl_interface_start_prop)' ${cilfile}
    sed -i '1i(type ctl_interface_stop_prop)' ${cilfile}
    sed -i '1i(type ctl_mdnsd_prop)' ${cilfile}
    sed -i '1i(type ctl_mediatranscoding_prop)' ${cilfile}
    sed -i '1i(type ctl_odsign_prop)' ${cilfile}
    sed -i '1i(type ctl_restart_prop)' ${cilfile}
    sed -i '1i(type ctl_rildaemon_prop)' ${cilfile}
    sed -i '1i(type ctl_sigstop_prop)' ${cilfile}
    sed -i '1i(type ctl_snapuserd_prop)' ${cilfile}
    sed -i '1i(type ctl_start_prop)' ${cilfile}
    sed -i '1i(type ctl_stop_prop)' ${cilfile}
    sed -i '1i(type dck_prop)' ${cilfile}
    sed -i '1i(type debug_tracing_desktop_mode_visible_tasks_prop)' ${cilfile}
    sed -i '1i(type debugfs_restriction_prop)' ${cilfile}
    sed -i '1i(type fastbootd_protocol_prop)' ${cilfile}
    sed -i '1i(type ffs_control_prop)' ${cilfile}
    sed -i '1i(type fstype_prop)' ${cilfile}
    sed -i '1i(type hal_dumpstate_config_prop)' ${cilfile}
    sed -i '1i(type heapprofd_enabled_prop)' ${cilfile}
    sed -i '1i(type hibernation_config_prop)' ${cilfile}
    sed -i '1i(type hibernation_prop)' ${cilfile}
    sed -i '1i(type hidl_memory_prop)' ${cilfile}
    sed -i '1i(type high_barometer_quality_prop)' ${cilfile}
    sed -i '1i(type hwservicemanager_prop)' ${cilfile}
    sed -i '1i(type hypervisor_prop)' ${cilfile}
    sed -i '1i(type hypervisor_pvmfw_prop)' ${cilfile}
    sed -i '1i(type hypervisor_restricted_prop)' ${cilfile}
    sed -i '1i(type hypervisor_virtualizationmanager_prop)' ${cilfile}
    sed -i '1i(type init_perf_lsm_hooks_prop)' ${cilfile}
    sed -i '1i(type init_storage_prop)' ${cilfile}
    sed -i '1i(type init_svc_debug_prop)' ${cilfile}
    sed -i '1i(type input_device_config_prop)' ${cilfile}
    sed -i '1i(type kcmdline_prop)' ${cilfile}
    sed -i '1i(type keyguard_config_prop)' ${cilfile}
    sed -i '1i(type keystore_config_prop)' ${cilfile}
    sed -i '1i(type keystore_crash_prop)' ${cilfile}
    sed -i '1i(type keystore_listen_prop)' ${cilfile}
    sed -i '1i(type last_boot_reason_prop)' ${cilfile}
    sed -i '1i(type llkd_prop)' ${cilfile}
    sed -i '1i(type lmkd_prop)' ${cilfile}
    sed -i '1i(type logd_auditrate_prop)' ${cilfile}
    sed -i '1i(type lowpan_prop)' ${cilfile}
    sed -i '1i(type lpdumpd_prop)' ${cilfile}
    sed -i '1i(type media_config_prop)' ${cilfile}
    sed -i '1i(type media_variant_prop)' ${cilfile}
    sed -i '1i(type misctrl_prop)' ${cilfile}
    sed -i '1i(type mm_events_config_prop)' ${cilfile}
    sed -i '1i(type mmc_prop)' ${cilfile}
    sed -i '1i(type smart_idle_maint_enabled_prop)' ${cilfile}
    sed -i '1i(type snapshotctl_prop)' ${cilfile}
    sed -i '1i(type suspend_debug_prop)' ${cilfile}
    sed -i '1i(type suspend_prop)' ${cilfile}
    sed -i '1i(type system_adbd_prop)' ${cilfile}
    sed -i '1i(type system_audio_config_prop)' ${cilfile}
    sed -i '1i(type system_boot_reason_prop)' ${cilfile}
    sed -i '1i(type system_jvmti_agent_prop)' ${cilfile}
    sed -i '1i(type system_lmk_prop)' ${cilfile}
    sed -i '1i(type system_service_enable_prop)' ${cilfile}
    sed -i '1i(type system_trace_prop)' ${cilfile}
    sed -i '1i(type system_user_mode_emulation_prop)' ${cilfile}
    sed -i '1i(type test_boot_reason_prop)' ${cilfile}
    sed -i '1i(type test_harness_prop)' ${cilfile}
    sed -i '1i(type theme_prop)' ${cilfile}
    sed -i '1i(type threadnetwork_config_prop)' ${cilfile}
    sed -i '1i(type time_prop)' ${cilfile}
    sed -i '1i(type timezone_metadata_prop)' ${cilfile}
    sed -i '1i(type tombstone_config_prop)' ${cilfile}
    sed -i '1i(type traced_enabled_prop)' ${cilfile}
    sed -i '1i(type traced_lazy_prop)' ${cilfile}
    sed -i '1i(type traced_oome_heap_session_count_prop)' ${cilfile}
    sed -i '1i(type traced_perf_enabled_prop)' ${cilfile}
    sed -i '1i(type tuner_config_prop)' ${cilfile}
    sed -i '1i(type tuner_server_ctl_prop)' ${cilfile}
    sed -i '1i(type uprobestats_start_with_config_prop)' ${cilfile}
    sed -i '1i(type usb_uvc_enabled_prop)' ${cilfile}
    sed -i '1i(type vehicle_hal_prop)' ${cilfile}
    sed -i '1i(type vendor_AMSIPC_service_prop)' ${cilfile}
    sed -i '1i(type vendor_adsprpc_prop)' ${cilfile}
    sed -i '1i(type vendor_alarm_boot_prop)' ${cilfile}
    sed -i '1i(type vendor_audio_prop)' ${cilfile}
    sed -i '1i(type vendor_audio_ssr_prop)' ${cilfile}
    sed -i '1i(type vendor_bluetooth_prop)' ${cilfile}
    sed -i '1i(type vendor_msm_irqbalance_prop)' ${cilfile}
    sed -i '1i(type vendor_per_mgr_state_prop)' ${cilfile}
    sed -i '1i(type vendor_qspm_dbg_prop)' ${cilfile}
    sed -i '1i(type vendor_sensors_dbg_prop)' ${cilfile}
    sed -i '1i(type vendor_sys_bugreport_cpuinfo_disable)' ${cilfile}
    sed -i '1i(type ctl_default_prop)' ${cilfile}
    sed -i '1i(type ctl_tcmd_prop)' ${cilfile}
    sed -i '1i(type device_logging_prop)' ${cilfile}
    sed -i '1i(type dmesgd_start_prop)' ${cilfile}
    sed -i '1i(type drm_forcel3_prop)' ${cilfile}
    sed -i '1i(type drm_service_config_prop)' ${cilfile}
    sed -i '1i(type dumpstate_options_prop)' ${cilfile}
    sed -i '1i(type dynamic_system_prop)' ${cilfile}
    sed -i '1i(type enable_16k_pages_prop)' ${cilfile}
    sed -i '1i(type exported_bluetooth_prop)' ${cilfile}
    sed -i '1i(type exported_overlay_prop)' ${cilfile}
    sed -i '1i(type firstboot_prop)' ${cilfile}
    sed -i '1i(type framework_watchdog_config_prop)' ${cilfile}
    sed -i '1i(type future_pm_prop)' ${cilfile}
    sed -i '1i(type game_manager_config_prop)' ${cilfile}
    sed -i '1i(type gesture_prop)' ${cilfile}
    sed -i '1i(type gsid_prop)' ${cilfile}
    sed -i '1i(type incremental_prop)' ${cilfile}
    sed -i '1i(type logpersistd_logging_prop)' ${cilfile}
    sed -i '1i(type lower_kptr_restrict_prop)' ${cilfile}
    sed -i '1i(type mock_ota_prop)' ${cilfile}
    sed -i '1i(type net_464xlat_fromvendor_prop)' ${cilfile}
    sed -i '1i(type net_connectivity_prop)' ${cilfile}
    sed -i '1i(type net_dns_prop)' ${cilfile}
    sed -i '1i(type netd_stable_secret_prop)' ${cilfile}
    sed -i '1i(type nnapi_ext_deny_product_prop)' ${cilfile}
    sed -i '1i(type odsign_prop)' ${cilfile}
    sed -i '1i(type oem_unlock_prop)' ${cilfile}
    sed -i '1i(type ota_build_prop)' ${cilfile}
    sed -i '1i(type overlay_prop)' ${cilfile}
    sed -i '1i(type packagemanager_config_prop)' ${cilfile}
    sed -i '1i(type page_size_prop)' ${cilfile}
    sed -i '1i(type perf_drop_caches_prop)' ${cilfile}
    sed -i '1i(type persist_sysui_builder_extras_prop)' ${cilfile}
    sed -i '1i(type persist_sysui_ranking_update_prop)' ${cilfile}
    sed -i '1i(type persist_vendor_debug_wifi_prop)' ${cilfile}
    sed -i '1i(type persist_wm_debug_prop)' ${cilfile}
    sed -i '1i(type persistent_properties_ready_prop)' ${cilfile}
    sed -i '1i(type power_debug_prop)' ${cilfile}
    sed -i '1i(type profcollectd_etr_prop)' ${cilfile}
    sed -i '1i(type profcollectd_node_id_prop)' ${cilfile}
    sed -i '1i(type provisioned_prop)' ${cilfile}
    sed -i '1i(type qemu_hw_prop)' ${cilfile}
    sed -i '1i(type qemu_sf_lcd_density_prop)' ${cilfile}
    sed -i '1i(type quick_start_prop)' ${cilfile}
    sed -i '1i(type radio_cdma_ecm_prop)' ${cilfile}
    sed -i '1i(type rebootescrow_hal_prop)' ${cilfile}
    sed -i '1i(type recovery_config_prop)' ${cilfile}
    sed -i '1i(type recovery_usb_config_prop)' ${cilfile}
    sed -i '1i(type remote_prov_prop)' ${cilfile}
    sed -i '1i(type retaildemo_prop)' ${cilfile}
    sed -i '1i(type safemode_prop)' ${cilfile}
    sed -i '1i(type sendbug_config_prop)' ${cilfile}
    sed -i '1i(type sensors_config_prop)' ${cilfile}
    sed -i '1i(type serialno_prop)' ${cilfile}
    sed -i '1i(type snapuserd_prop)' ${cilfile}
    sed -i '1i(type storage_config_prop)' ${cilfile}
    sed -i '1i(type surfaceflinger_display_prop)' ${cilfile}
    sed -i '1i(type vendor_board_suffix_prop)' ${cilfile}
    sed -i '1i(type vendor_boot_mode_prop)' ${cilfile}
    sed -i '1i(type vendor_bootreceiver_prop)' ${cilfile}
    sed -i '1i(type vendor_bservice_prop)' ${cilfile}
    sed -i '1i(type vendor_camera_prop)' ${cilfile}
    sed -i '1i(type vendor_cap_configstore_dbg_prop)' ${cilfile}
    sed -i '1i(type vendor_cgroup_follow_prop)' ${cilfile}
    sed -i '1i(type vendor_cnd_prop)' ${cilfile}
    sed -i '1i(type vendor_cnd_vendor_prop)' ${cilfile}
    sed -i '1i(type vendor_confqmaa)' ${cilfile}
    sed -i '1i(type vendor_console_log_level_prop)' ${cilfile}
    sed -i '1i(type vendor_core_ctl_prop)' ${cilfile}
    sed -i '1i(type vendor_crash_cnt_prop)' ${cilfile}
    sed -i '1i(type vendor_crash_detect_prop)' ${cilfile}
    sed -i '1i(type vendor_ctl_netmgrd_prop)' ${cilfile}
    sed -i '1i(type vendor_ctl_port-bridge_prop)' ${cilfile}
    sed -i '1i(type vendor_ctl_qcrild_prop)' ${cilfile}
    sed -i '1i(type vendor_ctl_qmuxd_prop)' ${cilfile}
    sed -i '1i(type vendor_ctl_vendor_hbtp_prop)' ${cilfile}
    sed -i '1i(type vendor_ctl_vendor_imsrcsservice_prop)' ${cilfile}
    sed -i '1i(type vendor_ctl_vendor_mmid_prop)' ${cilfile}
    sed -i '1i(type vendor_ctl_vendor_rmt_storage_prop)' ${cilfile}
    sed -i '1i(type vendor_ctl_vendor_wigigsvc_prop)' ${cilfile}
    sed -i '1i(type vendor_data_ko_prop)' ${cilfile}
    sed -i '1i(type vendor_data_qmipriod_prop)' ${cilfile}
    sed -i '1i(type vendor_data_shsusr_prop)' ${cilfile}
    sed -i '1i(type vendor_dataadpl_prop)' ${cilfile}
    sed -i '1i(type vendor_dataqdp_prop)' ${cilfile}
    sed -i '1i(type vendor_dataqti_prop)' ${cilfile}
    sed -i '1i(type vendor_dbg_brkpoint_prop)' ${cilfile}
    sed -i '1i(type vendor_dcvs_prop)' ${cilfile}
    sed -i '1i(type vendor_default_prop)' ${cilfile}
    sed -i '1i(type vendor_disable_spu_prop)' ${cilfile}
    sed -i '1i(type vendor_mdm_helper_prop)' ${cilfile}
    sed -i '1i(type vendor_mm_video_prop)' ${cilfile}
    sed -i '1i(type vendor_mmi_prop)' ${cilfile}
    sed -i '1i(type vendor_persist_camera_prop)' ${cilfile}
    sed -i '1i(type vendor_ramdump_prop)' ${cilfile}
    sed -i '1i(type vendor_wfd_sys_prop)' ${cilfile}
    sed -i '1i(type vold_post_fs_data_prop)' ${cilfile}
    sed -i '1i(type aac_drc_prop)' ${cilfile}
    sed -i '1i(type ab_update_gki_prop)' ${cilfile}
    sed -i '1i(type adaptive_haptics_prop)' ${cilfile}
    sed -i '1i(type adbd_config_prop)' ${cilfile}
    sed -i '1i(type adbd_prop)' ${cilfile}
    sed -i '1i(type apexd_payload_metadata_prop)' ${cilfile}
    sed -i '1i(type apexd_prop)' ${cilfile}
    sed -i '1i(type apk_verity_prop)' ${cilfile}
    sed -i '1i(type audio_config_prop)' ${cilfile}
    sed -i '1i(type avf_virtualizationservice_prop)' ${cilfile}
    sed -i '1i(type bluetooth_a2dp_offload_prop)' ${cilfile}
    sed -i '1i(type bluetooth_audio_hal_prop)' ${cilfile}
    sed -i '1i(type bluetooth_finder_prop)' ${cilfile}
    sed -i '1i(type bluetooth_prop)' ${cilfile}
    sed -i '1i(type bootanim_config_prop)' ${cilfile}
    sed -i '1i(type bootanim_system_prop)' ${cilfile}
    sed -i '1i(type bootloader_boot_reason_prop)' ${cilfile}
    sed -i '1i(type bpf_progs_loaded_prop)' ${cilfile}
    sed -i '1i(type build_attestation_prop)' ${cilfile}
    sed -i '1i(type camerax_extensions_prop)' ${cilfile}
    sed -i '1i(type apexd_config_prop)' ${cilfile}
    sed -i '1i(type ctl_apex_load_prop)' ${cilfile}
}


# Need add module name into here once add a new module in vendor-modules.
# we want to get the cil file of vendor-modules with module defined in modules.conf,
# we can't config module which we added in vendor-modules as base in modules.conf because it would re-declaration when we combine host cil with android cils,
# so set module such as "host = module" in modules.conf is neccesary.
HOST_POLICY_MODULES += "host fix"
fakeroot do_configure() {
    install -d ${S_GIT_REFPOLICY}/config/appconfig-qti
    cp -rf ${S_GIT_REFPOLICY}/config/appconfig-mcs/* ${S_GIT_REFPOLICY}/config/appconfig-qti/
    echo "r:sshd_t:s0     r:unconfined_t:s0" > ${S_GIT_REFPOLICY}/config/appconfig-qti/default_type
    echo "r:unconfined_t:s0" > ${S_GIT_REFPOLICY}/config/appconfig-qti/failsafe_context
    echo "r:host_exec_t:s0 r:host_exec_t:s0" > ${S_GIT_REFPOLICY}/config/appconfig-qti/u_default_contexts
    echo "root:u" > ${S_GIT_REFPOLICY}/config/appconfig-qti/seusers
    echo "__default__:u" >> ${S_GIT_REFPOLICY}/config/appconfig-qti/seusers
    echo "<summary>Policy modules for the Qti selinux.</summary>" > ${S_HOST_MODULES}/metadata.xml
    cp -rf ${S_HOST_MODULES} ${S_GIT_REFPOLICY}/policy/modules/
    sed -i '1 i\r:host_exec_t:s0 r:host_exec_t:s0' ${S_GIT_REFPOLICY}/config/appconfig-qti/default_contexts
    fix_platcil "${S_ANDROID_CILS}/system/plat_sepolicy.cil"
    fix_platcil_ecrm "${S_ANDROID_CILS}/system/plat_sepolicy.cil"
}
fakeroot do_compile() {
    oe_runmake conf
    oe_runmake policy
}
prepare_policy_store () {
    oe_runmake 'DESTDIR=${D}' 'prefix=${D}${prefix}' install
    POL_PRIORITY=100
    POL_SRC=${D}${datadir}/selinux/${POLICY_NAME}
    POL_STORE=${D}${localstatedir}/lib/selinux/${POLICY_NAME}
    POL_ACTIVE_MODS=${POL_STORE}/active/modules/${POL_PRIORITY}
    # Prepare to create policy store
    mkdir -p ${POL_STORE}
    mkdir -p ${POL_ACTIVE_MODS}
    mkdir -p ${S_PRECOMBINED_CILS}
    # get hll type from suffix on base policy module
    HLL_TYPE=$(echo ${POL_SRC}/base.* | awk -F . '{if (NF>1) {print $NF}}')
    HLL_BIN=${STAGING_DIR_NATIVE}${prefix}/libexec/selinux/hll/${HLL_TYPE}
    for i in base ${HOST_POLICY_MODULES}; do
        MOD_FILE=${POL_SRC}/${i}.${HLL_TYPE}
        MOD_DIR=${POL_ACTIVE_MODS}/${i}
        mkdir -p ${MOD_DIR}
        echo -n "${HLL_TYPE}" > ${MOD_DIR}/lang_ext
        # don't need to compress because we would apply cil file to compile policy file with android cils
        ${HLL_BIN} ${MOD_FILE} > ${MOD_DIR}/cil
        cp ${MOD_DIR}/cil ${S_PRECOMBINED_CILS}/${i}.cil
        cp ${MOD_FILE} ${MOD_DIR}/hll
        # By default, below big file will be installed to persist,this may make
        # persist run out of space
        # remove it temporarily as it is useless when system bootup.
        rm ${MOD_DIR}/hll
        rm ${MOD_DIR}/cil
    done
}
# some statements can't be recognized by sepolicy when compile sepolicy and refpolicy
compatibility_fix() {
    sed -i '1i(roletype object_r vendor_sysfs_memory_offline)' ${S_PRECOMBINED_CILS}/host.cil
    sed -i '1i(type vendor_sysfs_memory_offline)' ${S_PRECOMBINED_CILS}/host.cil
    sed -i '1i(roletype object_r ais_v4l2_proxy)' ${S_PRECOMBINED_CILS}/host.cil
    sed -i '1i(type ais_v4l2_proxy)' ${S_PRECOMBINED_CILS}/host.cil
    sed -i '/roleattributeset cil_gen_require system_r/d' ${S_PRECOMBINED_CILS}/host.cil
}

rebuild_policy () {
    install -d ${D}/${sysconfdir}/selinux/${POLICY_NAME}/policy
    SECILC_BIN=${STAGING_DIR_NATIVE}${prefix}/bin/secilc
    ${SECILC_BIN} ${S_PRECOMBINED_CILS}/host.cil ${S_PRECOMBINED_CILS}/fix.cil \
    ${S_ANDROID_CILS}/product/product_sepolicy.cil \
    ${S_ANDROID_CILS}/product/33.0.cil \
    ${S_ANDROID_CILS}/system/plat_sepolicy.cil \
    ${S_ANDROID_CILS}/system/33.0.cil \
    ${S_ANDROID_CILS}/system_ext/system_ext_sepolicy.cil \
    ${S_ANDROID_CILS}/system_ext/33.0.cil \
    ${S_ANDROID_CILS}/vendor/plat_pub_versioned.cil \
    ${S_ANDROID_CILS}/vendor/vendor_sepolicy.cil \
    ${S_HOST_REFPOLICY}/host_append.cil \
    -m -M true -G -N -c 33 -o ${D}${sysconfdir}/selinux/${POLICY_NAME}/policy/policy.33
}

install_misc_files () {
    echo "user_tty_device_t" > \
        ${D}${sysconfdir}/selinux/${POLICY_NAME}/contexts/customizable_types
    # install setrans.conf for mls/mcs policy
    if [ -f ${S_HOST_REFPOLICY}/setrans-${POLICY_TYPE}.conf ]; then
        install -m 0644 ${S_HOST_REFPOLICY}/setrans-${POLICY_TYPE}.conf \
        ${D}${sysconfdir}/selinux/${POLICY_NAME}/setrans.conf
    fi
    # install policy headers
    oe_runmake 'DESTDIR=${D}' 'prefix=${D}${prefix}' install-headers
    # install seusers
    install -m 0644 ${S_GIT_REFPOLICY}/config/appconfig-qti/seusers \
        ${D}${sysconfdir}/selinux/${POLICY_NAME}/seusers
}
install_config () {
    echo "\
# This file controls the state of SELinux on the system.
# SELINUX= can take one of these three values:
#     enforcing - SELinux security policy is enforced.
#     permissive - SELinux prints warnings instead of enforcing.
#     disabled - No SELinux policy is loaded.
SELINUX=${DEFAULT_ENFORCING}
# SELINUXTYPE= can take one of these values:
#     minimum - Minimum Security protection.
#     standard - Standard Security protection.
#     mls - Multi Level Security protection.
#     targeted - Targeted processes are protected.
#     mcs - Multi Category Security protection.
SELINUXTYPE=${POLICY_NAME}
" > ${WORKDIR}/config
    install -d ${D}/${sysconfdir}/selinux
    install -m 0644 ${WORKDIR}/config ${D}/${sysconfdir}/selinux/
}
install_file_contexts() {
    if [ -e ${D}/${sysconfdir}/selinux/${POLICY_NAME}/contexts/files/file_contexts ]; then
        rm -rf ${D}/${sysconfdir}/selinux/${POLICY_NAME}/contexts/files/file_contexts
    fi
    install -m 0644 ${S_HOST_REFPOLICY}/file_contexts ${D}/${sysconfdir}/selinux/${POLICY_NAME}/contexts/files/
}
do_install () {
    prepare_policy_store
    compatibility_fix
    rebuild_policy
    install_misc_files
    install_config
    install_file_contexts
}
do_install:append () {
    # While building policies on target, Makefile will be searched from SELINUX_DEVEL_PATH
    echo "SELINUX_DEVEL_PATH=${datadir}/selinux/${POLICY_NAME}/include" > ${D}${sysconfdir}/selinux/sepolgen.conf
}
sysroot_stage_all:append () {
    sysroot_stage_dir ${D}${sysconfdir} ${SYSROOT_DESTDIR}${sysconfdir}
}

