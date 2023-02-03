FILESBBAPPENDPATH := "${THISDIR}"
FILESEXTRAPATHS =. "${FILESBBAPPENDPATH}/${BP}:${FILESBBAPPENDPATH}/${BPN}:"

# Add glib-2.0 dependency to support g_strlcat
DEPENDS += "glib-2.0"

SRC_URI:append = " \
    file://0001-systemd-add-slotselect-support-in-fstab.patch \
    file://0033-systemd-Make-root-s-home-directory-configurable-2.patch \
    file://0001-systemd-skip-smack-copy-issue-in-systemd.patch \
    file://60-misc.rules \
"
SRC_URI:append = " ${@bb.utils.contains("PREFERRED_VERSION_linux-msm", "5.15", "file://platform_load.conf", "", d)}"

# Disable close_range in systemd v250.4 as it doesn't work with linux-msm 5.4
SRC_URI:append = " ${@oe.utils.conditional("PV", "250.4", "file://0001-Disable-close_range.patch", "", d)}"

# Remove backlight ldconfig
#   * backlight - Loads/Saves Screen Backlight Brightness, not required.
#   * ldconfig  - configures dynamic linker run-time bindings.
#                 ldconfig  creates  the  necessary links and cache to the most
#                 recent shared libraries found in the directories specified on
#                 the command line, in the file /etc/ld.so.conf, and in the
#                 trusted directories (/lib and /usr/lib).  The cache (created
#                 at /etc/ld.so.cache) is used by the run-time linker ld-linux.so.
#                 system-ldconfig.service runs "ldconfig -X", but as / is read-only
#                 cache may not be created. Disabling this may introduce app
#                 start time latency.
PACKAGECONFIG:remove = " backlight ldconfig "

# Use glib-2.0 for g_strlcat
CFLAGS:append = " \
    -fPIC \
    -DUSE_GLIB \
    -I${STAGING_INCDIR}/glib-2.0 \
    -I${STAGING_LIBDIR}/glib-2.0/include \
    -I${STAGING_LIBDIR}/glib-2.0/glib \
"

LDFLAGS:append = " -lglib-2.0"

# In aarch64 targets systemd is not booting with -finline-functions -finline-limit=64 optimizations
# So temporarily revert to default optimizations for systemd.
FULL_OPTIMIZATION = "-O2 -fexpensive-optimizations -frename-registers -fomit-frame-pointer -ftree-vectorize"

do_install:append:sa81x5() {
    # workaroud for suspend, suspend is not allowd in systemd-sleep, LA will trigger suspend
    if ${@bb.utils.contains('DISTRO_FEATURES', 'qti-lxc', 'true', 'false', d)}; then
        sed -i 's/#AllowSuspend=yes/AllowSuspend=no/' ${D}${sysconfdir}/systemd/sleep.conf
    fi

}

do_install:append () {
    # Use kernel rules for network iface name
    sed -i  's/^NamePolicy.*/NamePolicy=kernel/g' ${D}${systemd_unitdir}/network/99-default.link

    #Remove privatetmp=true from hostname service
    sed -i  '/^PrivateTmp.*/d' ${D}${systemd_system_unitdir}/systemd-hostnamed.service

    # Remove orignal 60-persistent-v4l.rules which is not applicable for QTI video
    rm ${D}${nonarch_base_libdir}/udev/rules.d/60-persistent-v4l.rules

    # Add platform_load.conf to /etc/modules-load.d/, systemd will load modules in this file.
    if ${@bb.utils.contains("PREFERRED_VERSION_linux-msm", "5.15", "true", "false", d)}; then
        install -m 0664 ${WORKDIR}/platform_load.conf ${D}${sysconfdir}/modules-load.d/
    fi
}

