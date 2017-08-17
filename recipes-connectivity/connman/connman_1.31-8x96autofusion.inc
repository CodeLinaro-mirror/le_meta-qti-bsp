FILESEXTRAPATHS_prepend = "${WORKSPACE}:"
SRC_URI_remove = "${KERNELORG_MIRROR}/linux/network/${BPN}/${BP}.tar.xz"
SRC_URI += "\
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
PACKAGECONFIG[ipsecvici] = "--enable-ipsecvici"
