inherit qcommon pkgconfig systemd

PR = "r0"

SRC_URI = " \
    ${CAF_LA_GIT}/device/qcom/common.git;protocol=${CAF_PROT};tag=${CAF_TAG};nobranch=1;destsuffix=android_compat/device/qcom/common \
    ${CAF_LA_GIT}/platform/vendor/qcom/ferrum.git;protocol=${CAF_PROT};tag=${CAF_TAG};nobranch=1;destsuffix=android_compat/device/qcom/msm8909 \
"
SRC_URI  += "file://persist-prop.sh"
SRC_URI  += "file://setbt-prop.sh"
SRC_URI  += "file://setbt-prop.service"


S="${WORKDIR}/android_compat/device/qcom/${SOC_FAMILY}"
DESCRIPTION = "Script to populate system properties"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

do_compile() {
    # Remove empty lines and lines starting with '#'
    sed -e 's/#.*$//' -e '/^$/d' ${WORKDIR}/android_compat/device/qcom/${SOC_FAMILY}/system.prop >> ${S}/build.prop
}


INITSCRIPT_NAME   = "persist-prop.sh"
INITSCRIPT_PARAMS = "start 50 2 3 4 5 ."


do_install() {
    install -d ${D}
    install ${S}/build.prop ${D}/build.prop
    install -m 0755 ${WORKDIR}/persist-prop.sh -D ${D}${sysconfdir}/init.d/persist-prop
    install -m 0755 ${WORKDIR}/setbt-prop.sh -D ${D}${sysconfdir}/init.d/setbt-prop
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -d ${D}${systemd_unitdir}/system/
        install -d ${D}${systemd_unitdir}/system/multi-user.target.wants/
        install -m 0644 ${WORKDIR}/setbt-prop.service  -D ${D}${systemd_unitdir}/system/setbt-prop.service
        ln -sf ${D}${systemd_unitdir}/system/setbt-prop.service ${D}${systemd_unitdir}/system/multi-user.target.wants/setbt-prop.service
    fi
}

pkg_postinst_${PN} () {
    if ${@bb.utils.contains('DISTRO_FEATURES','systemd','false','true',d)}; then
        update-alternatives --install ${sysconfdir}/init.d/persist-prop.sh persist-prop.sh  persist-prop 50
        [ -n "$D" ] && OPT="-r $D" || OPT="-s"
        # remove all rc.d-links potentially created from alternatives
        update-rc.d $OPT -f persist-prop.sh remove
        update-rc.d $OPT persist-prop.sh multiuser
    fi
}
PACKAGES = "${PN}"
FILES_${PN} = "${sysconfdir}/init.d/persist-prop  /build.prop"

PACKAGES += "${PN}-bt"
FILES_${PN}-bt = "/etc/init.d/ ${sysconfdir}/init.d/setbt-prop.sh ${systemd_unitdir}/system/setbt-prop.service ${systemd_unitdir}/system/multi-user.target.wants/setbt-prop.service "
