FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " \
        file://autodev.sh \
        file://config \
        file://0002-lxc-support-deny-device-by-devpth.patch \
        file://0003-lxc-handle-cgroup-device-not-available-gracefully.patch \
        file://0004-skip-fetch-seccomp-config-on-lxc-without-seccomp.patch \
"

FILES:${PN} += "lxc/bin"
FILES:${PN} += "lxc/lxc-conf"
FILES:${PN} += "vendor/vm-system"

EXTRA_OECONF += " --enable-static-binaries"
EXTRA_OECONF += " --with-config-path=/vendor/vm-system/lxc/lxc-conf"
EXTRA_OECONF += " --with-runtime-path=/mnt/lxc/run"
EXTRA_OECONF += " --with-rootfs-path=/vendor/vm-system"

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
    mv ${D}/usr/bin ${D}/lxc
    cp ${WORKDIR}/config ${D}/lxc/lxc-conf/lv
    install -m 755 ${WORKDIR}/autodev.sh ${D}/lxc/lxc-conf
}

RM_WORK_EXCLUDE += "${PN}"
