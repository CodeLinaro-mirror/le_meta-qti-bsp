FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI += " file://0001-systemd-add-slotselect-support-in-fstab.patch \
    file://0002-udev-trigger-only-enable-must-part-while-leave-other.patch \
    file://systemd-udev-trigger-full.service"
#SRC_URI += " file://0033-systemd-Make-root-s-home-directory-configurable-2.patch "
SRC_URI += " ${@bb.utils.contains('DISTRO_FEATURES', 'early_init', 'file://0034-systemd-add-handover-support-for-early-service.patch', '', d)}"

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
PACKAGECONFIG_remove = " backlight ldconfig "

CFLAGS_append = " -fPIC"

# In aarch64 targets systemd is not booting with -finline-functions -finline-limit=64 optimizations
# So temporarily revert to default optimizations for systemd.
FULL_OPTIMIZATION = "-O2 -fexpensive-optimizations -frename-registers -fomit-frame-pointer -ftree-vectorize"

do_patch_append () {
    bb.build.exec_func('do_fix_root_home', d)
    if bb.utils.contains('DISTRO_FEATURES','qti-lxc','True','False',d)=="True":
        bb.build.exec_func('do_fix_suspend', d)
}
# workaroud for suspend, suspend is not allowd in systemd-sleep, LA will trigger suspend
do_fix_suspend () {
    sed -i 's/#AllowSuspend=yes/AllowSuspend=no/' ${S}/src/sleep/sleep.conf
}

do_fix_root_home () {
    sed -i 's/\*home = "\/root"/\*home = "\/home\/root"/' ${S}/src/basic/user-util.c
    sed -i 's/h = strdup("\/root")/h = strdup("\/home\/root")/' ${S}/src/basic/user-util.c
}

do_install_append () {
    # Use kernel rules for network iface name
    sed -i  's/^NamePolicy.*/NamePolicy=kernel/g' ${D}/lib/systemd/network/99-default.link

    #Remove privatetmp=true from hostname service
    sed -i  '/^PrivateTmp.*/d' ${D}/lib/systemd/system/systemd-hostnamed.service

    # Remove orignal 60-persistent-v4l.rules which is not applicable for QTI video
    rm ${D}/lib/udev/rules.d/60-persistent-v4l.rules

    # Divide the original systemd-udev-trigger.service into two services.The dependent
    # part is executed first(system-udev-trigger.service,and the non-dependent part is
    # executed later(systemd-udev-trigger-full.service).Improve performance.
    install -d ${D}${systemd_unitdir}/system/multi-user.target.wants
    install -m 0644 ${WORKDIR}/systemd-udev-trigger-full.service ${D}${systemd_unitdir}/system/
    ln -sf ${systemd_unitdir}/system/systemd-udev-trigger-full.service ${D}${systemd_unitdir}/system/multi-user.target.wants/systemd-udev-trigger-full.service

    # When enable early init, weston no longer trigger login operation,so XDG_RUNTIME_DIR won't be
    # created unless we adb shell/ssh to login to the board. this will cause issue when we try to
    # pass XDG_RUNTIME_DIR to container during bootup.
    # Trigger a login operations for this case.
    # if lxc can support dynamic mount injection fuction later, this part can be removed.
    if ${@bb.utils.contains('DISTRO_FEATURES', 'early_init', 'true', 'false', d)}; then
        sed -i  's/^ExecStart.*/ExecStart=-\/sbin\/agetty --autologin root --noclear %I 38400 linux/g' ${D}/lib/systemd/system/getty@.service
        sed -i  's/^Type=.*/Type=simple/g' ${D}/lib/systemd/system/getty@.service
    fi
}

