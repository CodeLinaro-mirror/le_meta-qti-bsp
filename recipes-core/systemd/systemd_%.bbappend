#Add qti specific changes only when qt-disro is enabled.
QTI_SYSTEMD_INC = ""
QTI_SYSTEMD_INC:qti-distro-base = "${THISDIR}/qti-systemd.inc"
include ${QTI_SYSTEMD_INC}

SRC_URI:append = " file://platform.rules"



# Remove optional heavy systemd components
PACKAGECONFIG:remove = "resolve"
PACKAGECONFIG:remove = "timesyncd"
PACKAGECONFIG:remove = "networkd"
PACKAGECONFIG:remove = "rfkill"
PACKAGECONFIG:remove = "tmpfiles"
PACKAGECONFIG:remove = "sysusers"
PACKAGECONFIG:remove = "logind"
PACKAGECONFIG:remove = "hostnamed"
PACKAGECONFIG:remove = "localed"
PACKAGECONFIG:remove = "machined"
PACKAGECONFIG:remove = "timedated"
PACKAGECONFIG:remove = "quotacheck"
PACKAGECONFIG:remove = "smack"
PACKAGECONFIG:remove = "utmp"
PACKAGECONFIG:remove = "vconsole"
PACKAGECONFIG:remove = "backlight"
PACKAGECONFIG:remove = "binfmt"
PACKAGECONFIG:remove = "coredump"
PACKAGECONFIG:remove = "firstboot"
PACKAGECONFIG:remove = "hibernate"
PACKAGECONFIG:remove = "ima"
PACKAGECONFIG:remove = "ldconfig"

# Ensure journald is built, but minimal mode will keep RAM small
PACKAGECONFIG:append = " kmod"

# Install our minimal journald configuration
SRC_URI += "file://journald-minimal.conf"
SRC_URI += "file://system.conf"

#SYSTEMD_AUTO_ENABLE = "disable"
#SYSTEMD_PACKAGES = "${PN}"
#SYSTEMD_SERVICE:${PN}:append = " systemd-udev-settle.service"


do_install:append() {
    sed -i '/group:wheel/d' ${D}${exec_prefix}/lib/tmpfiles.d/systemd.conf
    if ${@bb.utils.contains_any('MACHINE_FEATURES', 'qti-vm', 'true', 'false', d)}; then
        sed -i 's/#children_max=/children_max=2/' ${D}/etc/udev/udev.conf
    fi
    install -m 0644 ${WORKDIR}/platform.rules -D ${D}${sysconfdir}/udev/rules.d/platform.rules

    install -d ${D}${sysconfdir}/systemd/journald.conf.d
    install -m 0644 ${WORKDIR}/journald-minimal.conf \
        ${D}${sysconfdir}/systemd/journald.conf.d/minimal.conf

    install -d ${D}${sysconfdir}/systemd/system.conf.d
    install -m 0644 ${WORKDIR}/system.conf \
        ${D}${sysconfdir}/systemd/system.conf.d/lowmem.conf

    ln -sf /dev/null ${D}${systemd_unitdir}/system/systemd-udev-settle.service
}

do_install:append:mdm9607() {
   #  Mask journaling services by default.
   #  'systemctl unmask' can be used on device to enable them if needed.
   ln -sf /dev/null ${D}${systemd_unitdir}/system/systemd-journald.service
   ln -sf /dev/null ${D}${systemd_unitdir}/system/sysinit.target.wants/systemd-journal-flush.service
   ln -sf /dev/null ${D}${systemd_unitdir}/system/sysinit.target.wants/systemd-journal-catalog-update.service
}

do_install:append:qcs610-odk-64() {
   #  For QCS610-odk-64 Oneshot should be replace as simple
   #  Boot performance improvement for Talos SP 
    sed -i 's/^Type=oneshot$/Type=simple/' ${D}${systemd_unitdir}/system/systemd-modules-load.service
}
