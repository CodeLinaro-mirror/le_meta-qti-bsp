FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " \
        file://autodev.sh \
        file://config \
        file://file_contexts \
        file://dbus_contexts \
        file://0001-fix-error-msg-Unsupported-config-key-lxc.seccomp.patch \
        file://0002-lxc-support-deny-device-by-devpth.patch \
        file://0003-lxc-handle-cgroup-device-not-available-gracefully.patch \
        file://0005-add-lxc-daemon-tool.patch \
        file://0006-cgroup-hybrid-mode-refine.patch \
"

FILES:${PN} += "lxc/bin"
FILES:${PN} += "lxc/lxc-conf"
FILES:${PN} += "/vendor_early_services/vendor/vm-system"

EXTRA_OEMESON += "-Ddata-path=/vendor_early_services/vendor/vm-system/lxc/lxc-conf"
EXTRA_OEMESON += "-Druntime-path=/vendor_early_services/run/lxc/run"
EXTRA_OEMESON += "-Drootfs-mount-path=/vendor_early_services/vendor/vm-system"
CFLAGS:append = " -Wno-incompatible-pointer-types -Wno-stringop-overread"

do_install:append() {
    for file in ${D}/usr/bin/*; do
        if [ -f "$file" ] && [[ $(basename "$file") == lxc* ]]; then
            file_output=$(file "$file")
            if [[ "$file_output" == *"ELF"* ]]; then
                ${STRIP} --strip-debug "$file"
            fi
        fi
    done

    install -d ${D}/lxc/lxc-conf/lv
    install -d ${D}/etc/selinux/mls/contexts/files
    install -d ${D}/etc/selinux/targeted/contexts/files
    mv ${D}/usr/bin ${D}/lxc
    cp ${WORKDIR}/config ${D}/lxc/lxc-conf/lv
    install -m 755 ${WORKDIR}/autodev.sh ${D}/lxc/lxc-conf
    install -m 644 ${WORKDIR}/file_contexts ${D}/etc/selinux/mls/contexts/files
    install -m 644 ${WORKDIR}/file_contexts ${D}/etc/selinux/targeted/contexts/files
    install -m 644 ${WORKDIR}/dbus_contexts ${D}/etc/selinux/targeted/contexts

    # getenforce can't work if no /etc/selinux/config file
    touch ${D}/etc/selinux/config
}

RM_WORK_EXCLUDE += "${PN}"

