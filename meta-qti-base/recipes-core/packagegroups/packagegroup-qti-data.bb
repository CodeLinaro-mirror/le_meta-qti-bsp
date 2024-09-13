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
    ${@bb.utils.contains_any('PREFERRED_PROVIDER_virtual/kernel', 'linux-ark linux-qcom', 'setup-qos', '', d)} \
"
RDEPENDS:${PN}:append:quin-gvm-lemans = " dataeth-dlkm"
