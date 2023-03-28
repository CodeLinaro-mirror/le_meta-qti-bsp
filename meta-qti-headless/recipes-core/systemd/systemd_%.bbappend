FILESBBAPPENDPATH := "${THISDIR}"
FILESEXTRAPATHS =. "${FILESBBAPPENDPATH}/${BP}:${FILESBBAPPENDPATH}/${BPN}:"

SRC_URI:append = " \
    file://60-misc.rules \
"
SRC_URI:append = " ${@bb.utils.contains("PREFERRED_VERSION_linux-msm", "5.15", "file://platform_load.conf", "", d)}"

# Disable close_range in systemd v250.4 as it doesn't work with linux-msm 5.4
SRC_URI:append = " ${@oe.utils.conditional("PV", "250.4", "file://0001-Disable-close_range.patch", "", d)}"

do_install:append () {
    # Use kernel rules for network iface name
    sed -i  's/^NamePolicy.*/NamePolicy=kernel/g' ${D}${systemd_unitdir}/network/99-default.link

    # Add platform_load.conf to /etc/modules-load.d/, systemd will load modules in this file.
    if ${@bb.utils.contains("PREFERRED_VERSION_linux-msm", "5.15", "true", "false", d)}; then
        install -m 0664 ${WORKDIR}/platform_load.conf ${D}${sysconfdir}/modules-load.d/
    fi
}
