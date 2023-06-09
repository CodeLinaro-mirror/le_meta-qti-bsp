FILESEXTRAPATHS_prepend := "${THISDIR}/systemd:"

# Fetch systemd.git from CAF.
SRC_URI  = "git://git.codelinaro.org/clo/le/systemd.git;protocol=https;branch=caf_migration/systemd/main"


SRC_URI += "file://touchscreen.rules \
            file://00-create-volatile.conf \
            file://init \
            file://0001-binfmt-Don-t-install-dependency-links-at-install-tim.patch \
            file://0002-use-lnr-wrapper-instead-of-looking-for-relative-opti.patch \
            file://0003-implment-systemd-sysv-install-for-OE.patch \
            file://0004-rules-whitelist-hd-devices.patch \
            file://0005-Make-root-s-home-directory-configurable.patch \
            file://0006-remove-nobody-user-group-checking.patch \
            file://0007-rules-watch-metadata-changes-in-ide-devices.patch \
            file://0008-Do-not-enable-nss-tests-if-nss-systemd-is-not-enable.patch \
            file://0009-nss-mymachines-Build-conditionally-when-ENABLE_MYHOS.patch \
            file://0001-login-use-parse_uid-when-unmounting-user-runtime-dir.patch \
            file://0001-sd-bus-make-BUS_DEFAULT_TIMEOUT-configurable.patch \
            file://0022-build-sys-Detect-whether-struct-statx-is-defined-in-.patch \
            file://0023-resolvconf-fixes-for-the-compatibility-interface.patch \
            file://0001-core-when-deserializing-state-always-use-read_line-L.patch \
            file://0001-chown-recursive-let-s-rework-the-recursive-logic-to-.patch \
            file://0001-dhcp6-make-sure-we-have-enough-space-for-the-DHCP6-o.patch \
            file://0001-Revert-sysctl.d-request-ECN-on-both-in-and-outgoing-.patch \
            file://0001-timesync-changes-type-of-drift_freq-to-int64_t.patch \
            file://0001-sysctl-Don-t-pass-null-directive-argument-to-s.patch \
            file://0002-core-Fix-use-after-free-case-in-load_from_path.patch \
            file://0001-meson-rename-Ddebug-to-Ddebug-extra.patch \
            file://0024-journald-do-not-store-the-iovec-entry-for-process-co.patch \
            file://0025-journald-set-a-limit-on-the-number-of-fields.patch \
            file://0026-journal-fix-out-of-bounds-read-CVE-2018-16866.patch \
            file://CVE-2019-6454.patch \
            file://sd-bus-if-we-receive-an-invalid-dbus-message-ignore-.patch \
            "

# QTI specific patches.
SRC_URI += " \
           file://Disable-unused-mount-points.patch \
           file://mountpartitions.rules \
           file://systemd-udevd.service \
           file://ffbm.target \
           file://mtpserver.rules \
           file://ion.rules \
           file://kgsl.rules \
           file://set-usb-nodes.rules \
           file://sysctl.conf \
           file://platform.conf \
           ${@bb.utils.contains('MACHINE_FEATURES', 'qti-vm', 'file://trusted_touch.conf', '',d)} \
           file://sd-bus-Allow-extra-users-to-communicate.patch \
           file://systemd-namespace-mountflags-fix.patch \
           file://set-mhi-nodes.rules \
           "

# Custom setup for PACKAGECONFIG to get a slimmer systemd.
# Removed following:
#   * backlight - Loads/Saves Screen Backlight Brightness, not required.
#   * firstboot - initializes the most basic system settings interactively
#                  on the first boot if /etc is empty, not required.
#   * hostname  - No need to change the system's hostname
#   * ldconfig  - configures dynamic linker run-time bindings.
#                 ldconfig  creates  the  necessary links and cache to the most
#                 recent shared libraries found in the directories specified on
#                 the command line, in the file /etc/ld.so.conf, and in the
#                 trusted directories (/lib and /usr/lib).  The cache (created
#                 at /etc/ld.so.cache) is used by the run-time linker ld-linux.so.
#                 system-ldconfig.service runs "ldconfig -X", but as / is RO
#                 cache may not be created. Disabling this may introduce app
#                 start time latency.
#   * localed   - Service used to change the system locale settings, not needed.
#   * machined  - For tracking local Virtual Machines and Containers, not needed.
#   * networkd  - Manages network configurations, custom solution is used.
#   * polkit    - Not used.
#   * quotacheck- Not using Quota.
#   * resolvd   - Use custom network name resolution manager.
#   * smack     - Not used.
#   * timesyncd - Chronyd is being used instead for NTP timesync.
#                 Also timesyncd was resulting in higher boot KPI.
#   * utmp      - No back fill for SysV runlevel changes needed.
#   * vconsole  - Not used.
PACKAGECONFIG = " \
    ${@bb.utils.filter('DISTRO_FEATURES', 'selinux', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'rfkill', '', d)} \
    acl \
    binfmt \
    coredump \
    hibernate \
    ima \
    kmod \
    logind \
    randomseed \
    timedated \
    xz \
"
# Don't support coredump generation for user builds.
PACKAGECONFIG_remove_qti-distro-user = "coredump"

EXTRA_OEMESON += " -Defi=false"
EXTRA_OEMESON += " -Dhwdb=false"

CFLAGS_append = " -fPIC"

# In aarch64 targets systemd is not booting with -finline-functions -finline-limit=64 optimizations
# So temporarily revert to default optimizations for systemd.
SELECTED_OPTIMIZATION = "-O2 -fexpensive-optimizations -frename-registers -fomit-frame-pointer -ftree-vectorize"

MACHINE_SUPPORT_BLOCK_DEVICES = "${@bb.utils.contains_any('BASEMACHINE', 'qcs403-som2 sdxprairie', 'false', 'true', d)}"

do_install_append () {
   install -d ${D}/etc/systemd/system/
   install -d ${D}/lib/systemd/system/ffbm.target.wants
   install -d ${D}/etc/systemd/system/ffbm.target.wants
   rm ${D}/lib/udev/rules.d/60-persistent-v4l.rules

   # Place systemd-udevd.service in /etc/systemd/system
   install -m 0644 ${WORKDIR}/systemd-udevd.service \
       -D ${D}/etc/systemd/system/systemd-udevd.service
   install -m 0644 ${WORKDIR}/ffbm.target \
       -D ${D}/etc/systemd/system/ffbm.target

   # Enable logind/getty/password-wall service in FFBM mode
   ln -sf /lib/systemd/system/systemd-logind.service ${D}/lib/systemd/system/ffbm.target.wants/systemd-logind.service
   ln -sf /lib/systemd/system/getty.target ${D}/lib/systemd/system/ffbm.target.wants/getty.target
   ln -sf /lib/systemd/system/systemd-ask-password-wall.path ${D}/lib/systemd/system/ffbm.target.wants/systemd-ask-password-wall.path
   install -d /etc/sysctl.d/
   install -m 0644 ${WORKDIR}/sysctl.conf -D ${D}/etc/sysctl.d/sysctl.conf
   install -m 0644 ${WORKDIR}/platform.conf -D ${D}/etc/tmpfiles.d/platform.conf

   if ${@bb.utils.contains('MACHINE_FEATURES', 'qti-vm', 'true', 'false', d)}; then
       install -m 0644 ${WORKDIR}/trusted_touch.conf -D ${D}/etc/tmpfiles.d/trusted_touch.conf
   fi

   #  Mask journaling services by default.
   #  'systemctl unmask' can be used on device to enable them if needed.
   if [ "${SYSTEMD_ENABLE_JOURNAL}" != "1" ]; then
       ln -sf /dev/null ${D}/etc/systemd/system/systemd-journald.service
       ln -sf /dev/null ${D}${systemd_unitdir}/system/sysinit.target.wants/systemd-journal-flush.service
       ln -sf /dev/null ${D}${systemd_unitdir}/system/sysinit.target.wants/systemd-journal-catalog-update.service
       ln -sf /dev/null ${D}${systemd_unitdir}/system/sockets.target.wants/systemd-journald-audit.socket
       ln -sf /dev/null ${D}${systemd_unitdir}/system/sockets.target.wants/systemd-journald-dev-log.socket
       ln -sf /dev/null ${D}${systemd_unitdir}/system/sockets.target.wants/systemd-journald.socket
   fi
   install -d ${D}${sysconfdir}/udev/rules.d/
   install -m 0644 ${WORKDIR}/ion.rules -D ${D}${sysconfdir}/udev/rules.d/ion.rules
   install -m 0644 ${WORKDIR}/kgsl.rules -D ${D}${sysconfdir}/udev/rules.d/kgsl.rules
   install -m 0644 ${WORKDIR}/set-mhi-nodes.rules -D ${D}${sysconfdir}/udev/rules.d/set-mhi-nodes.rules

   # Mask dev-ttyS0.device
   ln -sf /dev/null ${D}/etc/systemd/system/dev-ttyS0.device

   # Remove rules to automount block devices.
   if [ "${MACHINE_SUPPORT_BLOCK_DEVICES}" == "false" ]; then
       sed -i '/SUBSYSTEM=="block", TAG+="systemd"/d' ${D}/lib/udev/rules.d/99-systemd.rules
       sed -i '/SUBSYSTEM=="block", ACTION=="add", ENV{DM_UDEV_DISABLE_OTHER_RULES_FLAG}=="1", ENV{SYSTEMD_READY}="0"/d' ${D}/lib/udev/rules.d/99-systemd.rules

       # Remove generator binaries and ensure that we don't rely on generators for mount or service files.
       rm -rf ${D}/lib/systemd/system-generators/systemd-debug-generator
       rm -rf ${D}/lib/systemd/system-generators/systemd-fstab-generator
       rm -rf ${D}/lib/systemd/system-generators/systemd-getty-generator
       rm -rf ${D}/lib/systemd/system-generators/systemd-gpt-auto-generator
       rm -rf ${D}/lib/systemd/system-generators/systemd-hibernate-resume-generator
       rm -rf ${D}/lib/systemd/system-generators/systemd-rc-local-generator
       rm -rf ${D}/lib/systemd/system-generators/systemd-system-update-generator
       rm -rf ${D}/lib/systemd/system-generators/systemd-sysv-generator
   fi

   # Update persistent-storage.rules to create bootdevice/by-name symlinks
   sed -i 's/LABEL="persistent_storage_end"/# block\/bootdevice\/by-name links'"\n"'LABEL="persistent_storage_end"/g' \
       ${D}/lib/udev/rules.d/60-persistent-storage.rules
   sed -i 's/LABEL="persistent_storage_end"/ENV{ID_PART_ENTRY_SCHEME}=="gpt", ENV{ID_PART_ENTRY_NAME}=="?*", SYMLINK+="block\/bootdevice\/by-name\/$env{ID_PART_ENTRY_NAME}"'"\n\n"'LABEL="persistent_storage_end"/g' \
       ${D}/lib/udev/rules.d/60-persistent-storage.rules
}

# Run fsck as part of local-fs-pre.target instead of local-fs.target
do_install_append () {
   # remove from After
   sed -i '/After/s/local-fs-pre.target//' ${D}${systemd_unitdir}/system/systemd-fsck@.service
   # Add to Before
   sed -i '/Before/s/$/ local-fs-pre.target/' ${D}${systemd_unitdir}/system/systemd-fsck@.service
}

RRECOMMENDS_${PN}_remove += "systemd-extra-utils"
PACKAGES_remove += "${PN}-extra-utils"

FILES_${PN} += "/etc/initscripts \
                ${sysconfdir}/udev/rules.d ${userfsdatadir}/*"
