inherit autotools-brokensep

PR = "r0"

FILESPATH =+ "${WORKSPACE}/android_compat/device/qcom/:"
SRC_URI   = "file://${SOC_FAMILY}"
SRC_URI  += "file://persist-prop.sh"
SRC_URI  += "file://persist-prop.service"

DESCRIPTION = "Script to populate system properties"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

do_compile() {
    # Remove empty lines and lines starting with '#'
    sed -e 's/#.*$//' -e '/^$/d' ${WORKDIR}/${SOC_FAMILY}/system.prop >> ${S}/build.prop
}

inherit update-rc.d systemd

SYSTEMD_SERVICE_${PN} = " persist-prop.service "
SYSTEMD_AUTO_ENABLE_${pn} = "enable"

INITSCRIPT_NAME   = "persist-prop.sh"
INITSCRIPT_PARAMS = "start 50 2 3 4 5 ."

do_install() {
    install -d ${D}
    install ${S}/build.prop ${D}/build.prop
    install -m 0755 ${WORKDIR}/persist-prop.sh -D ${D}${sysconfdir}/init.d/persist-prop
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -d ${D}${bindir}
        install -m 0755 ${WORKDIR}/persist-prop.sh -D ${D}${bindir}/persist-prop
        install -d ${D}${systemd_unitdir}/system/
        install -m 0644 ${WORKDIR}/persist-prop.service -D ${D}${systemd_unitdir}/system/persist-prop.service
    fi
}

pkg_postinst_${PN} () {
        # Cleanup sysvinitscript postinst
        if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        else
        update-alternatives --install ${sysconfdir}/init.d/persist-prop.sh persist-prop.sh  persist-prop 50
        [ -n "$D" ] && OPT="-r $D" || OPT="-s"
        # remove all rc.d-links potentially created from alternatives
        update-rc.d $OPT -f persist-prop.sh remove
        update-rc.d $OPT persist-prop.sh multiuser
        fi
}

PACKAGES = "${PN}"
FILES_${PN} += "/build.prop"
FILES_${PN} += "${systemd_unitdir}/system/"
