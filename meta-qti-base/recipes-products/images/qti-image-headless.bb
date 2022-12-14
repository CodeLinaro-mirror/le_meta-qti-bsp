SUMMARY = "QTI headless image"
DESCRIPTION = "QTI headless image for basic platform support without Multimedia/Connectivity modules"
LICENSE = "BSD-3-Clause-Clear"

DEPENDS += "ext4-utils-native mkbootimg-native"

inherit core-image

IMAGE_ROOTFS_SIZE = "716800"

IMAGE_INSTALL += " \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-core', 'packagegroup-qti-core-minimal', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-security', 'packagegroup-qti-security', '', d)} \
    ${@bb.utils.contains('COMBINED_FEATURES', 'qti-extra', 'packagegroup-qti-extra', '', d)} \
    bridge-utils \
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
    xinetd \
    tcp-wrappers \
    netkit-telnet \
    proftpd \
    openssh \
"

IMAGE_LINGUAS = ""

EXTRA_IMAGECMD:ext4 = "-i 4096 -b 4096"

remove_data_bind() {
    sed -i '/^\/data/d' ${IMAGE_ROOTFS}/etc/fstab
}

ROOTFS_POSTPROCESS_COMMAND:append = " remove_data_bind;"
