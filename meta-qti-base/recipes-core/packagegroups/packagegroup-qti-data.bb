SUMMARY = "QTI package group for data service"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-data \
    "

ALLOW_EMPTY_${PN} = "1"

RDEPENDS_${PN} += "\
    bridge-utils \
    connman \
    connman-client \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-hypervisor', 'setup-network', '', d)} \
    net-tools \
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
    proftpd \
    openssh \
"
