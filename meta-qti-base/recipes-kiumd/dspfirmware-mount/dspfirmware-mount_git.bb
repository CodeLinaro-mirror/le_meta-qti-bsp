SUMMARY = "Mount dsp and firmware patitions"
DESCRIPTION = "Create dsp and firmware mount targets and install relevant cfg files"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${QTI_LICENSE_DIR}/${LICENSE};md5=b796c0007db682166a1721da80267bb2"

SRC_URI = "\
    ${PATH_TO_REPO}/vendor/qcom/opensource/kiumd/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/kiumd;usehead=1 \
    file://mnt_fs.conf \
"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/kiumd/dspfirmware-mount"

do_compile[noexec] = "1"

do_install:append() {
    install -d -p ${D}${systemd_unitdir}/system/multi-user.target.wants/

    install -d -p ${D}/firmware/qcom/sa8775p
    install -d -p ${D}/vendor/dsp

    install -m 0755 ${WORKDIR}/mnt_fs.conf -D ${D}${libdir}/modules-load.d/mnt_fs.conf
    install -m 0777 ${S}/firmware-qcom-sa8775p.mount -D ${D}${systemd_unitdir}/system/firmware-qcom-sa8775p.mount
    install -m 0777 ${S}/vendor-dsp.mount -D ${D}${systemd_unitdir}/system/vendor-dsp.mount
    install -m 0777 ${S}/vendor-dsp.automount -D ${D}${systemd_unitdir}/system/vendor-dsp.automount

    if ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'true', 'false', d)}; then
        sed -i '/^Options=/s/defaults/&,context=system_u:object_r:dsp_file_t:s0/' ${D}${systemd_unitdir}/system/vendor-dsp.mount
    fi

    ln -sf ${systemd_unitdir}/system/firmware-qcom-sa8775p.mount \
        ${D}${systemd_unitdir}/system/multi-user.target.wants/firmware-qcom-sa8775p.mount
    ln -sf ${systemd_unitdir}/system/vendor-dsp.mount \
        ${D}${systemd_unitdir}/system/multi-user.target.wants/vendor-dsp.mount
    ln -sf ${systemd_unitdir}/system/vendor-dsp.automount \
        ${D}${systemd_unitdir}/system/multi-user.target.wants/vendor-dsp.automount

    if ${@bb.utils.contains('MACHINE_FEATURES', 'qti-vmm', 'true', 'false', d)}; then
        install -d -p ${D}/firmware/vm/boot/autoghgvm
        install -m 0777 ${S}/firmware-vm-boot-autoghgvm-mount.service -D ${D}${systemd_unitdir}/system/firmware-vm-boot-autoghgvm-mount.service

        if ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'true', 'false', d)}; then
            sed -i '/^Options=/s/defaults/&,context=system_u:object_r:qcrosvm_boot_t:s0/' ${D}${systemd_unitdir}/system/firmware-vm-boot-autoghgvm-mount.service
        fi

        ln -sf ${systemd_unitdir}/system/firmware-vm-boot-autoghgvm-mount.service \
            ${D}${systemd_unitdir}/system/multi-user.target.wants/firmware-vm-boot-autoghgvm-mount.service
    fi

    install -m 0777 ${S}/bluetooth-mount.service -D ${D}${systemd_unitdir}/system/bluetooth-mount.service
    ln -sf ${systemd_unitdir}/system/bluetooth-mount.service \
        ${D}${systemd_unitdir}/system/multi-user.target.wants/bluetooth-mount.service

    if [ -f ${S}/99-persist-storage-ab.rules ]; then
        install -m 0644 ${S}/99-persist-storage-ab.rules -D ${D}${sysconfdir}/udev/rules.d/99-persist-storage-ab.rules
    fi

    install -d ${D}${sysconfdir}/sysconfig/
    install -m 0777 ${S}/lpass_cfg ${D}${sysconfdir}/sysconfig/lpass_cfg
    install -m 0777 ${S}/cdsp0_cfg ${D}${sysconfdir}/sysconfig/cdsp0_cfg
    install -m 0777 ${S}/cdsp1_cfg ${D}${sysconfdir}/sysconfig/cdsp1_cfg
    install -m 0777 ${S}/gpdsp0_cfg ${D}${sysconfdir}/sysconfig/gpdsp0_cfg
    install -m 0777 ${S}/gpdsp1_cfg ${D}${sysconfdir}/sysconfig/gpdsp1_cfg
}

do_install:append:sa8255-ivi() {
    if ${@bb.utils.contains('MACHINE_FEATURES', 'qti-vmm', 'true', 'false', d)}; then
        install -d -p ${D}/firmware/vm/boot/autoghgvmlv
        install -m 0777 ${S}/firmware-vm-boot-autoghgvmlv-mount.service ${D}${systemd_unitdir}/system/firmware-vm-boot-autoghgvmlv-mount.service

            if ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'true', 'false', d)}; then
                sed -i '/^Options=/s/defaults/&,context=system_u:object_r:qcrosvm_boot_t:s0/' ${D}${systemd_unitdir}/system/firmware-vm-boot-autoghgvmlv-mount.service
            fi

        ln -sf ${systemd_unitdir}/system/firmware-vm-boot-autoghgvmlv-mount.service \
                ${D}${systemd_unitdir}/system/multi-user.target.wants/firmware-vm-boot-autoghgvmlv-mount.service
    fi
}

do_install:append:sa7255-ivi() {
    if ${@bb.utils.contains('MACHINE_FEATURES', 'qti-vmm', 'true', 'false', d)}; then
        install -d -p ${D}/firmware/vm/boot/autoghgvmlv
        install -m 0777 ${S}/firmware-vm-boot-autoghgvmlv-mount.service ${D}${systemd_unitdir}/system/firmware-vm-boot-autoghgvmlv-mount.service

        if ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'true', 'false', d)}; then
            sed -i '/^Options=/s/defaults/&,context=system_u:object_r:qcrosvm_boot_t:s0/' ${D}${systemd_unitdir}/system/firmware-vm-boot-autoghgvmlv-mount.service
        fi

        ln -sf ${systemd_unitdir}/system/firmware-vm-boot-autoghgvmlv-mount.service \
            ${D}${systemd_unitdir}/system/multi-user.target.wants/firmware-vm-boot-autoghgvmlv-mount.service
    fi
}

do_install:append:sa8775-flex() {
    if ${@bb.utils.contains('MACHINE_FEATURES', 'qti-vmm', 'true', 'false', d)}; then
        install -d -p ${D}/firmware/vm/boot/autoghgvmlv
        install -m 0644 ${S}/firmware-vm-boot-autoghgvmlv-mount.service ${D}${systemd_unitdir}/system/firmware-vm-boot-autoghgvmlv-mount.service

        if ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'true', 'false', d)}; then
            sed -i '/^Options=/s/defaults/&,context=system_u:object_r:qcrosvm_boot_t:s0/' ${D}${systemd_unitdir}/system/firmware-vm-boot-autoghgvmlv-mount.service
        fi

        ln -sf ${systemd_unitdir}/system/firmware-vm-boot-autoghgvmlv-mount.service \
            ${D}${systemd_unitdir}/system/multi-user.target.wants/firmware-vm-boot-autoghgvmlv-mount.service
    fi
}

PACKAGES =+ "${PN}-lvgvm ${PN}-bt"

FILES:${PN}-bt += "\
    ${systemd_system_unitdir}/bluetooth-mount.service \
    ${systemd_system_unitdir}/multi-user.target.wants/bluetooth-mount.service \
"

FILES:${PN}-lvgvm += "\
    ${systemd_system_unitdir}/firmware-vm-boot-autoghgvmlv-mount.service \
    ${systemd_system_unitdir}/multi-user.target.wants/firmware-vm-boot-autoghgvmlv-mount.service \
"

FILES:${PN} += "${systemd_unitdir}/*"
FILES:${PN} += "${sysconfdir}/*"
FILES:${PN} += "${libdir}/modules-load.d/*"
FILES:${PN} += "/firmware/*"
FILES:${PN} += "/vendor/*"
