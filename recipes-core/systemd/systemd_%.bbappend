#systemd package is fetch from CAF
SRC_URI = "git://source.codeaurora.org/quic/le/systemd.git;protocol=https;branch=systemd/main"
SRC_URI += " \
           file://touchscreen.rules \
           file://00-create-volatile.conf \
           file://init \
           file://run-ptest \
           file://0004-Use-getenv-when-secure-versions-are-not-available.patch \
           file://0005-binfmt-Don-t-install-dependency-links-at-install-tim.patch \
           file://0007-use-lnr-wrapper-instead-of-looking-for-relative-opti.patch \
           file://0010-implment-systemd-sysv-install-for-OE.patch \
           file://0011-nss-mymachines-Build-conditionally-when-HAVE_MYHOSTN.patch \
           file://0012-rules-whitelist-hd-devices.patch \
           file://0013-Make-root-s-home-directory-configurable.patch \
           file://0014-Revert-rules-remove-firmware-loading-rules.patch \
           file://0015-Revert-udev-remove-userspace-firmware-loading-suppor.patch \
           file://0017-remove-duplicate-include-uchar.h.patch \
           file://0018-check-for-uchar.h-in-configure.patch \
           file://0019-socket-util-don-t-fail-if-libc-doesn-t-support-IDN.patch \
           file://0001-add-fallback-parse_printf_format-implementation.patch \
           file://0002-src-basic-missing.h-check-for-missing-strndupa.patch \
           file://0003-don-t-fail-if-GLOB_BRACE-and-GLOB_ALTDIRFUNC-is-not-.patch \
           file://0004-src-basic-missing.h-check-for-missing-__compar_fn_t-.patch \
           file://0006-Include-netinet-if_ether.h.patch \
           file://0007-check-for-missing-canonicalize_file_name.patch \
           file://0008-Do-not-enable-nss-tests.patch \
           file://0009-test-hexdecoct.c-Include-missing.h-form-strndupa.patch \
           file://0010-test-sizeof.c-Disable-tests-for-missing-typedefs-in-.patch \
           file://0011-don-t-use-glibc-specific-qsort_r.patch \
           file://0012-don-t-pass-AT_SYMLINK_NOFOLLOW-flag-to-faccessat.patch \
           file://0013-comparison_fn_t-is-glibc-specific-use-raw-signature-.patch \
           file://0001-Define-_PATH_WTMPX-and-_PATH_UTMPX-if-not-defined.patch \
           file://0001-Use-uintmax_t-for-handling-rlim_t.patch \
           file://0001-core-evaluate-presets-after-generators-have-run-6526.patch \
           file://0001-main-skip-many-initialization-steps-when-running-in-.patch \
           "
SRC_URI_append_qemuall = " file://0001-core-device.c-Change-the-default-device-timeout-to-2.patch"


FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI += "file://Disable-unused-mount-points.patch"
SRC_URI += "file://mountpartitions.rules"
SRC_URI += "file://systemd-udevd.service"
SRC_URI += "file://ffbm.target"
SRC_URI += "file://mtpserver.rules"
SRC_URI += "file://sysctl-core.conf"
SRC_URI += "file://limit-core.conf"
SRC_URI += "file://logind.conf"
SRC_URI += "file://ion.rules"
SRC_URI += "file://kgsl.rules"
SRC_URI += "file://platform.conf"
SRC_URI += "file://ashmem.rules"
# Custom setup for PACKAGECONFIG to get a slimmer systemd.
# Removed following:
#   * timesyncd - Chronyd is being used instead for NTP timesync
#                 Also timesyncd was resulting in higher boot KPI.
#   * ldconfig  - configures dynamic linker run-time bindings.
#                 ldconfig  creates  the  necessary links and cache to the most
#                 recent shared libraries found in the directories specified on
#                 the command line, in the file /etc/ld.so.conf, and in the
#                 trusted directories (/lib and /usr/lib).  The cache (created
#                 at /etc/ld.so.cache) is used by the run-time linker ld-linux.so.
#                 system-ldconfig.service runs "ldconfig -X", but as / is RO
#                 cache may not be created. Disabling this may introduce app
#                 start time latency.
#   * backlight - Loads/Saves Screen Backlight Brightness, not required.
#   * localed   - systemd-localed is a system service that may be used as
#                 mechanism to change the system locale settings
#   * quotacheck  Not using Quota
#   * vconsole  - Not used
#   * hostname  - No need to change the system's hostname
#   * smack     - Not used
#   * utmp      - No back fill for SysV runlevel changes needed
#   * resolvd   - Use own network name resolution manager
PACKAGECONFIG = " \
    ${@bb.utils.filter('DISTRO_FEATURES', 'selinux', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'rfkill', '', d)} \
    binfmt \
    firstboot \
    hibernate \
    hostnamed \
    ima \
    logind \
    machined \
    networkd \
    polkit \
    randomseed \
    sysusers \
    timedated \
    xz \
"
EXTRA_OECONF += " --disable-efi"
EXTRA_OECONF += " --disable-hwdb"

# In aarch64 targets systemd is not booting with -finline-functions -finline-limit=64 optimizations
# So temporarily revert to default optimizations for systemd.
SELECTED_OPTIMIZATION = "-O2 -fexpensive-optimizations -frename-registers -fomit-frame-pointer -ftree-vectorize"

MACHINE_COREDUMP_ENABLE = "${@bb.utils.contains_any('BASEMACHINE', 'qcs605 sdmsteppe', 'true', 'false', d)}"

# Place systemd-udevd.service in /etc/systemd/system
do_install_append () {

   if [ "${MACHINE_COREDUMP_ENABLE}" == "true" ]; then
       sed -i "s#var\/tmp#data\/coredump#g" ${WORKDIR}/sysctl-core.conf
       #create coredump folder in data
       install -d ${D}${userfsdatadir}/coredump
   fi
   install -d ${D}/etc/systemd/system/
   install -d ${D}/lib/systemd/system/ffbm.target.wants
   install -d ${D}/etc/systemd/system/ffbm.target.wants
   rm ${D}/lib/udev/rules.d/60-persistent-v4l.rules
   install -m 0644 ${WORKDIR}/systemd-udevd.service \
       -D ${D}/etc/systemd/system/systemd-udevd.service
   install -m 0644 ${WORKDIR}/ffbm.target \
       -D ${D}/etc/systemd/system/ffbm.target
   # Enable logind/getty/password-wall service in FFBM mode
   ln -sf /lib/systemd/system/systemd-logind.service ${D}/lib/systemd/system/ffbm.target.wants/systemd-logind.service
   ln -sf /lib/systemd/system/getty.target ${D}/lib/systemd/system/ffbm.target.wants/getty.target
   ln -sf /lib/systemd/system/systemd-ask-password-wall.path ${D}/lib/systemd/system/ffbm.target.wants/systemd-ask-password-wall.path
   install -d ${D}/etc/security/limits.d/
   install -m 0644 ${WORKDIR}/limit-core.conf -D ${D}/etc/security/limits.d/core.conf
   install -d /etc/sysctl.d/
   install -m 0644 ${WORKDIR}/sysctl-core.conf -D ${D}/etc/sysctl.d/core.conf
   install -m 0644 ${WORKDIR}/logind.conf -D ${D}/etc/systemd/logind.conf
   install -m 0644 ${WORKDIR}/platform.conf -D ${D}/etc/tmpfiles.d/platform.conf
   #  Mask journaling services by default.
   #  'systemctl unmask' can be used on device to enable them if needed.
   ln -sf /dev/null ${D}/etc/systemd/system/systemd-journald.service
   ln -sf /dev/null ${D}${systemd_unitdir}/system/sysinit.target.wants/systemd-journal-flush.service
   ln -sf /dev/null ${D}${systemd_unitdir}/system/sysinit.target.wants/systemd-journal-catalog-update.service
   install -d ${D}${sysconfdir}/udev/rules.d/
   install -m 0644 ${WORKDIR}/ion.rules -D ${D}${sysconfdir}/udev/rules.d/ion.rules
   install -m 0644 ${WORKDIR}/kgsl.rules -D ${D}${sysconfdir}/udev/rules.d/kgsl.rules
   install -m 0644 ${WORKDIR}/ashmem.rules -D ${D}${sysconfdir}/udev/rules.d/ashmem.rules
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

do_install_append_robot-som-ros () {
    rm ${D}/etc/sysctl.d/core.conf
}

PACKAGES +="${PN}-coredump"
FILES_${PN} += "/etc/initscripts \
                ${sysconfdir}/udev/rules.d ${userfsdatadir}/*"
FILES_${PN}-coredump = "/etc/sysctl.d/core.conf /etc/security/limits.d/core.conf  ${userfsdatadir}/coredump"
