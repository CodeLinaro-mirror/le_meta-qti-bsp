# Add eth0 in NetworkInterfaceBlacklist to improve Ethernet KPI.
# FIXME: wlan driver will report wlan and p2p both as wlan in uevent
# which will mess connman, just ignore the p2p device by name
# the others are default one
do_install:append () {
  sed -i '/^NetworkInterfaceBlacklist/s/$/,veth,vb-,p2p,lxcbr,eth0,eth1/' ${WORKDIR}/main.conf
}
