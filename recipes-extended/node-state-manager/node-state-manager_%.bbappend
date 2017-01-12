FILESPATH =+ "${WORKSPACE}/poky/meta-qti-bsp/recipes-extended/node-state-manager/files/:"

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
