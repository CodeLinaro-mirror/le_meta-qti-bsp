FILESBBAPPENDPATH := "${THISDIR}"
FILESEXTRAPATHS =. "${FILESBBAPPENDPATH}/${BP}:${FILESBBAPPENDPATH}/${BPN}:"

SRC_URI:append = " \
    file://60-misc.rules \
    file://power-switch.rules \
    file://qti_sleep.sh \
"
SRC_URI:append = " ${@bb.utils.contains("PREFERRED_VERSION_linux-msm", "5.15", "file://platform_load.conf", "", d)}"

# Disable close_range in systemd v250.4 as it doesn't work with linux-msm 5.4
SRC_URI:append = " ${@oe.utils.conditional("PV", "250.4", "file://0001-Disable-close_range.patch", "", d)}"

# Remove ldconfig
#   * ldconfig  - configures dynamic linker run-time bindings.
#                 ldconfig  creates  the  necessary links and cache to the most
#                 recent shared libraries found in the directories specified on
#                 the command line, in the file /etc/ld.so.conf, and in the
#                 trusted directories (/lib and /usr/lib).  The cache (created
#                 at /etc/ld.so.cache) is used by the run-time linker ld-linux.so.
#                 system-ldconfig.service runs "ldconfig -X", but as / is read-only
#                 cache may not be created. Disabling this may introduce app
#                 start time latency.
PACKAGECONFIG:remove = "ldconfig"

do_install:append () {
    # Use kernel rules for network iface name
    sed -i  's/^NamePolicy.*/NamePolicy=kernel/g' ${D}${systemd_unitdir}/network/99-default.link

    # Add platform_load.conf to /etc/modules-load.d/, systemd will load modules in this file.
    if ${@bb.utils.contains("PREFERRED_VERSION_linux-msm", "5.15", "true", "false", d)}; then
        install -m 0664 ${WORKDIR}/platform_load.conf ${D}${sysconfdir}/modules-load.d/
    fi

	install -d ${D}/${base_libdir}/systemd/system-sleep
	install -m 0755 ${WORKDIR}/qti_sleep.sh -D ${D}/${base_libdir}/systemd/system-sleep/qti_sleep.sh
}
