SUMMARY = "QTI package group for data service"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-data \
    "

ALLOW_EMPTY_${PN} = "1"

RDEPENDS_${PN} += "\
    bridge-utils \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', '', 'connman', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', '', 'connman-client', d)} \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'setup-network', '', d)} \
    net-tools \
    emac-dwc-eqos \
    ethtool \
    iperf2 \
    iperf3 \
    iproute2 \
    iproute2-ss \
    iproute2-tc \
    tcpdump \
    vlan \
    strongswan \
    xinetd \
    tcp-wrappers \
    netkit-telnet \
    openssh \
    eavb-fe \
"
