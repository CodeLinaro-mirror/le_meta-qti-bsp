SUMMARY = "CNSS2 & WLAN Driver Module Load Service"
DESCRIPTION = "This recipes used to install the wlan related service named \
               init_qti_wlan_auto.service, which will try to load cnss2 \
               module and wlan host driver module.\
               "
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "BSD-3-Clause & BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9 \
                    file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause-Clear;md5=7a434440b651f4a472ca93716d01033a"

SRC_URI = "\
           file://init_qti_wlan_auto.service \
           file://init.qti.wlan_on.sh \
           file://init.gh.qti.wlan_on.sh \
           file://init.qti.wlan_off.sh \
           file://blacklist-pcie-qcom-ecam.conf \
           file://blacklist-cnss2.conf \
           "

inherit systemd useradd

SYSTEMD_SERVICE:${PN} = "init_qti_wlan_auto.service"

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM:${PN} = "cnss-wlan"
USERADD_PARAM:${PN} = "--no-create-home -g cnss-wlan --shell /bin/false cnss-wlan"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}${bindir}
    if ${@bb.utils.contains('MACHINE_FEATURES', 'qti-gunyah', 'true', 'false', d)}; then
        install -D -m 0755 ${WORKDIR}/init.gh.qti.wlan_on.sh ${D}${bindir}/init.qti.wlan_on.sh
        install -d ${D}${sysconfdir}/modprobe.d
        install -m 0644 ${WORKDIR}/blacklist-pcie-qcom-ecam.conf -D ${D}${sysconfdir}/modprobe.d/
        install -m 0644 ${WORKDIR}/blacklist-cnss2.conf -D ${D}${sysconfdir}/modprobe.d/
    else
        install -D -m 0755 ${WORKDIR}/init.qti.wlan_on.sh ${D}${bindir}/init.qti.wlan_on.sh
    fi
    install -D -m 0755 ${WORKDIR}/init.qti.wlan_off.sh ${D}${bindir}/init.qti.wlan_off.sh
    install -d ${D}${systemd_unitdir}/system/
    install -m 0644 ${WORKDIR}/init_qti_wlan_auto.service -D ${D}${systemd_unitdir}/system/init_qti_wlan_auto.service
    if ${@bb.utils.contains('DISTRO_FEATURES', 'smack', 'true', 'false', d)}; then
        #Add CAP_MAC_OVERRIDE capability for init_qti_wlan_auto.service to ignore Smack checks
         sed -i "/^AmbientCapabilities/s/$/ CAP_MAC_OVERRIDE/" ${D}${systemd_unitdir}/system/init_qti_wlan_auto.service
    fi
}

FILES:${PN} += "\
                ${systemd_unitdir}/system/* \
                ${sysconfdir}/modprobe.d/* \
                ${bindir}/init.qti.wlan_on.sh \
                ${bindir}/init.qti.wlan_off.sh \
"
