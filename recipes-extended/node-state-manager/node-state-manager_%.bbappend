FILESPATH =+ "${WORKSPACE}/poky/meta-qti-bsp/recipes-extended/node-state-manager/files/:"

SRC_URI = "${CLO_LE_GIT}/genivi/lifecycle/node-state-manager;protocol=${CLO_PROTOCOL};nobranch=1;name=nsm \
           file://nsm-fix-systemd-service-dep.patch \
           file://nsm-fix-no-libsystemd-daemon.patch \
           file://link-with-gio.patch \
          "
SRC_URI += "file://focussed.target"
SRC_URI += "file://unfocussed.target"
SRC_URI += "file://lazy.target"

SRCREV_nsm = "0894ea63e0b86afcee3a45baa10abc6b8be4ad44"

do_install_append() {
    install -m 644 ${WORKDIR}/*.target ${D}/${systemd_unitdir}/system
    rm ${D}/etc/dbus-1/system.d/org.genivi.NodeStateManager.conf
}

FILES_${PN} += "${systemd_unitdir}/system/focussed.target \
    ${systemd_unitdir}/system/unfocussed.target \
    ${systemd_unitdir}/system/lazy.target \
    "

DEPENDS += " glib-2.0-native"
