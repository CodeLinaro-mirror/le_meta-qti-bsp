FILESEXTRAPATHS_prepend = "${WORKSPACE}:"
SRC_URI = "\
           file://external/connman/ \
          "

S = "${WORKDIR}/external/connman"
PV = "1.31"
PR = "r20"

DEPENDS += "strongswan"
SUMMARY_${PN}-plugin-vpn-ipsecvici = "A Ipsecvici plugin for ConnMan VPN"
DESCRIPTION_${PN}-plugin-vpn-ipsecvici = "The Ipsecvici plugin uses ipsecvici-linux client \
to create a VPN connection to Ipsecvici server."
FILES_${PN}-plugin-vpn-ipsecvici += "${libdir}/connman/plugins-vpn/ipsecvici.so"
RDEPENDS_${PN}-plugin-vpn-ipsecvici += "${PN}-vpn strongswan-dev"
RRECOMMENDS_${PN} += "${@bb.utils.contains('PACKAGECONFIG','ipsecvici','${PN}-plugin-vpn-ipsecvici', '', d)}"
INSANE_SKIP_${PN}-plugin-vpn-ipsecvici += "dev-deps"
INSANE_SKIP_${PN} += "dev-deps"

PACKAGECONFIG[systemd] = "--with-systemdunitdir=${systemd_unitdir}/system/ --with-tmpfilesdir=${sysconfdir}/tmpfiles.d/,--with-systemdunitdir='' --with-tmpfilesdir=''"
SYSTEMD_SERVICE_${PN}-wait-online = "connman-wait-online.service"

FILES_${PN} =+ " ${sysconfdir}/tmpfiles.d/connman_resolvconf.conf"
PACKAGES =+ " ${PN}-wait-online"
SUMMARY_${PN}-wait-online = "A program that will return once ConnMan has connected to a network"
DESCRIPTION_${PN}-wait-online = "A service that can be enabled so that \
the system waits until a network connection is established."
FILES_${PN}-wait-online += "${sbindir}/connmand-wait-online \
                            ${systemd_unitdir}/system/connman-wait-online.service"

