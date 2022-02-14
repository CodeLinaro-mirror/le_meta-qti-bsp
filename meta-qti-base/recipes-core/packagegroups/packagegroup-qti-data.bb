SUMMARY = "QTI package group for data service"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-data \
    "

ALLOW_EMPTY_${PN} = "1"

RDEPENDS_${PN} += "\
    bridge-utils \
    ${@bb.utils.contains("DISTRO_FEATURES", "qti-headless", "", "connman", d)} \
    ${@bb.utils.contains("DISTRO_FEATURES", "qti-headless", "", "connman-client", d)} \
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
    ${@bb.utils.contains("DISTRO_FEATURES", "qti-headless", "", "xinetd", d)} \
    tcp-wrappers \
    netkit-telnet \
    proftpd \
    ${@bb.utils.contains("DISTRO_FEATURES", "qti-headless", "", "openssh", d)} \
"
