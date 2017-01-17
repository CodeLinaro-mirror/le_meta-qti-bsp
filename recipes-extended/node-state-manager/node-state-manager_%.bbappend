FILESPATH =+ "${WORKSPACE}/poky/meta-qti-bsp/recipes-extended/node-state-manager/files/:"

SRC_URI = "git://git.projects.genivi.org/lifecycle/${PN}.git;tag=${PV};protocol=http \
           file://nsm-fix-systemd-service-dep.patch \
           file://nsm-fix-no-libsystemd-daemon.patch \
           file://link-with-gio.patch \
          "

SRC_URI += "file://focussed.target"
SRC_URI += "file://unfocussed.target"
SRC_URI += "file://lazy.target"

do_install_append() {
    install -m 644 ${WORKDIR}/*.target ${D}/${systemd_unitdir}/system
}

FILES_${PN} += "${systemd_unitdir}/system/focussed.target \
    ${systemd_unitdir}/system/unfocussed.target \
    ${systemd_unitdir}/system/lazy.target \
    "
