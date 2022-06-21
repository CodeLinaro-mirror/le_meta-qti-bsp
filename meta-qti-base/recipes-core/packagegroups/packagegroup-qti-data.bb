SUMMARY = "QTI package group for data service"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-data \
    "

ALLOW_EMPTY:${PN} = "1"

RDEPENDS:${PN} += "\
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
    ${@bb.utils.contains("DISTRO_FEATURES", "qti-headless", "", "strongswan", d)} \
    xinetd \
    tcp-wrappers \
    netkit-telnet \
    proftpd \
    openssh \
"
