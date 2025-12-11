SUMMARY = "QTI package group for data service"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-data \
    "

ALLOW_EMPTY:${PN} = "1"

RDEPENDS:${PN} += "\
    bridge-utils \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'setup-network', '', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-gunyah', 'setup-network-host', '', d)} \
    net-tools \
    ethtool \
    iperf2 \
    iperf3 \
    iproute2 \
    iproute2-ss \
    iproute2-tc \
    tcpdump \
    phytool \
    vlan \
    strongswan \
    tcp-wrappers \
    ${@bb.utils.contains('LAYERSERIES_CORENAMES', 'scarthgap', '', 'netkit-telnet', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', '', 'proftpd', d)} \
    ${@bb.utils.contains_any('MACHINE_FEATURES', 'qti-umd', 'setup-qos', '', d)} \
    ${@bb.utils.contains_any('MACHINE_FEATURES', 'qti-umd', 'early-eth', '', d)} \
    ${@bb.utils.contains_any('MACHINE_FEATURES', 'qti-umd', 'netlink-service-infra', '', d)} \
"
RDEPENDS:${PN}:append:quin-gvm-monaco = " dataeth-dlkm early-eth-gvm"
RDEPENDS:${PN}:append:quin-gvm-lemans = " dataeth-dlkm early-eth-gvm"
RDEPENDS:${PN}:remove:sa8650-adas = "netlink-service-infra"
RDEPENDS:${PN}:remove:sa8620-adas = "netlink-service-infra"
