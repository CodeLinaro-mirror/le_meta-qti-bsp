FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = " \
        file://autodev.sh \
        file://config \
"

FILES:${PN} += "lxc/bin"
FILES:${PN} += "lxc/lxc-conf"
FILES:${PN} += "vendor/vm-system"

EXTRA_OEMESON += "--default-library=static -Dc_link_args=-static"
EXTRA_OEMESON += "-Ddata-path=/vendor/vm-system/lxc/lxc-conf"
EXTRA_OEMESON += "-Druntime-path=/mnt/lxc/run"
EXTRA_OEMESON += "-Drootfs-mount-path=/vendor/vm-system"
CFLAGS:append = " -Wno-incompatible-pointer-types -Wno-stringop-overread"

do_configure:prepend() {
    sed -i 's/dependency(\x27threads\x27)/dependency(\x27threads\x27, static: true)/' ${S}/meson.build
    sed -i 's/dependency(\x27libseccomp\x27, required: false)/dependency(\x27libseccomp\x27, required: false, static: true)/' ${S}/meson.build
    sed -i 's/dependency(\x27libselinux\x27, required: false)/dependency(\x27libselinux\x27, required: false, static: true)/' ${S}/meson.build
    sed -i 's/dependency(\x27libapparmor\x27, required: false)/dependency(\x27libapparmor\x27, required: false, static: true)/' ${S}/meson.build
    sed -i 's/dependency(\x27openssl\x27, required: false)/dependency(\x27openssl\x27, required: false, static: true)/' ${S}/meson.build
    sed -i 's/dependency(\x27libcap\x27, required: false)/dependency(\x27libcap\x27, required: false, static: true)/' ${S}/meson.build
    sed -i 's/liblxc = shared_library/liblxc = static_library/' ${S}/meson.build
    sed -i '/version: liblxc_version/d' ${S}/meson.build
}

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

