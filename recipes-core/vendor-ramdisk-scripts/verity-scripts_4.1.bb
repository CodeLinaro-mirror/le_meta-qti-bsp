SUMMARY = "Scripts to perform verity checks on paritions from vendor RAM Disk"

LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta-qti-bsp/files/common-licenses/\
${LICENSE};md5=3771d4920bd6cdb8cbdf1e8344489ee0"

SRC_URI  +=  "file://init-verity.sh"
SRC_URI  +=  "file://veritysetup.service.in"

# Tied to systemd. Build it only when systemd is also building.
inherit features_check
REQUIRED_DISTRO_FEATURES = "systemd"

do_configure[noexec] = "1"

do_compile[dirs] = "${WORKDIR}"
do_compile() {
    # Need to wait for bootable disk dev nodes creation before attempting verity check.
    # As selective wait by reading environment is not possible in [Unit] Section, wait
    # for required dev nodes i.e. _a & _b when a/b machine feature is enabled
    # (and for _c when a/b/c is enabled) before executing veritysetup.
    devdiskstr="dev-disk-by\\\x2dpartlabel-"
    vdlkmdevice="${devdiskstr}vendor_dlkm.device"
    systemdevice="${devdiskstr}system.device"
    if [[ "${MACHINE_FEATURES}" =~ .*qti-ab-boot* ]]; then
        vdlkmdevice="${devdiskstr}vendor_dlkm_a.device ${devdiskstr}vendor_dlkm_b.device"
        systemdevice="${devdiskstr}system_a.device ${devdiskstr}system_b.device"
    fi
    if [[ "${MACHINE_FEATURES}" =~ .*qti-abc-boot* ]]; then
        vdlkmdevice="$vdlkmdevice ${devdiskstr}vendor_dlkm_c.device"
        systemdevice="$systemdevice ${devdiskstr}system_c.device"
    fi

    # Create seperate unit file for each parition to check
    sed -e "s#@DEVICE@#$vdlkmdevice#g; s#@MAPDEVICE@#vendor_dlkm#g;" \
               veritysetup.service.in >veritysetup-vendor-dlkm.service
    sed -e "s#@DEVICE@#$systemdevice#g; s#@MAPDEVICE@#system#g" \
               veritysetup.service.in >veritysetup-system.service
}

do_install () {
  install -d ${D}/verity/
  install -m 755 ${WORKDIR}/init-verity.sh ${D}/verity/init-verity.sh
  install -d ${D}${systemd_unitdir}/system/
  install -m 0644 ${WORKDIR}/veritysetup-vendor-dlkm.service \
      ${D}${systemd_unitdir}/system/veritysetup-vendor-dlkm@.service
  install -m 0644 ${WORKDIR}/veritysetup-system.service \
      ${D}${systemd_unitdir}/system/veritysetup-system@.service
  # enable the services
  install -d ${D}${systemd_unitdir}/system/sysinit.target.wants/
  ln -sf ${systemd_unitdir}/system/veritysetup-vendor-dlkm@.service \
      ${D}${systemd_unitdir}/system/sysinit.target.wants/veritysetup-vendor-dlkm@vdlkm.service
  ln -sf ${systemd_unitdir}/system/veritysetup-system@.service \
      ${D}${systemd_unitdir}/system/sysinit.target.wants/veritysetup-system@root.service
}

PACKAGE_ARCH = "${MACHINE_ARCH}"
FILES_${PN} += " /verity/* ${systemd_unitdir}/system/* "
