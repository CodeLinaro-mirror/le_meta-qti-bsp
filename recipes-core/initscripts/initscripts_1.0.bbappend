PR = "r157"

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

SRC_URI += "file://0001-Mount-var-volatile-in-populate-volatile.patch"
SRC_URI += "file://umountfs"
SRC_URI += "file://bsp_paths.sh"
SRC_URI += "file://set_core_pattern.sh"
SRC_URI += "file://bsp_paths.service"
SRC_URI += "file://set_core_pattern.service"
SRC_URI += "file://logging-restrictions.sh"
SRC_URI += "file://populate_volatile_bg.sh"

do_install_append() {
        update-rc.d -f -r ${D} mountnfs.sh remove
        update-rc.d -f -r ${D} urandom remove
        update-rc.d -f -r ${D} checkroot.sh remove

        # Start populate-volatile.sh in rc5 instead of rcS.
        update-rc.d -f -r ${D} populate-volatile.sh remove
        #update-rc.d -r ${D} populate-volatile.sh start 22 5 .

        if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
         rm  ${D}${sysconfdir}/init.d/halt
         rm  ${D}${sysconfdir}/init.d/reboot
         rm  ${D}${sysconfdir}/init.d/save-rtc.sh
         rm  ${D}${sysconfdir}/init.d/sendsigs
         rm  ${D}${sysconfdir}/init.d/single
         rm  ${D}${sysconfdir}/init.d/sysfs.sh
         rm  ${D}${sysconfdir}/init.d/umountfs
         rm  ${D}${sysconfdir}/init.d/umountnfs.sh
         if [ "${TARGET_ARCH}" = "arm" ]; then
               rm  ${D}${sysconfdir}/init.d/alignment.sh
         fi
         install -d ${D}${sysconfdir}/initscripts
         install -m 0755 ${WORKDIR}/bsp_paths.sh  ${D}${sysconfdir}/initscripts/bsp_paths.sh
         install -m 0755 ${WORKDIR}/set_core_pattern.sh  ${D}${sysconfdir}/initscripts/set_core_pattern.sh
         if [ "${VARIANT}" = "user" ]; then
                install -m 0755 ${WORKDIR}/logging-restrictions.sh  ${D}${sysconfdir}/initscripts/logging-restrictions.sh
         fi
         install -d ${D}/etc/systemd/system/
         install -m 0644 ${WORKDIR}/bsp_paths.service -D ${D}/etc/systemd/system/bsp_paths.service
         install -d ${D}/etc/systemd/system/multi-user.target.wants/
         # enable the service for multi-user.target
         ln -sf /etc/systemd/system/bsp_paths.service \
              ${D}/etc/systemd/system/multi-user.target.wants/bsp_paths.service

         install -m 0644 ${WORKDIR}/set_core_pattern.service -D ${D}/etc/systemd/system/set_core_pattern.service
         # enable the service for multi-user.target
         ln -sf /etc/systemd/system/set_core_pattern.service \
              ${D}/etc/systemd/system/multi-user.target.wants/set_core_pattern.service
        else
         install -m 0755 ${WORKDIR}/bsp_paths.sh  ${D}${sysconfdir}/init.d
         install -m 0755 ${WORKDIR}/populate_volatile_bg.sh  ${D}${sysconfdir}/init.d
         update-rc.d -r ${D} bsp_paths.sh start 15 2 3 4 5 .
         update-rc.d -r ${D} populate_volatile_bg.sh start 37 S .
         install -m 0755 ${WORKDIR}/set_core_pattern.sh  ${D}${sysconfdir}/init.d
         update-rc.d -r ${D} set_core_pattern.sh start 01 S 2 3 4 5 S .
         if [ "${VARIANT}" = "user" ]; then
                install -m 0755 ${WORKDIR}/logging-restrictions.sh  ${D}${sysconfdir}/init.d/logging-restrictions.sh
                update-rc.d -r ${D} logging-restrictions.sh start 39 S .
         fi

        fi

        # Remove recursive restorecon calls from populate_volatile.sh
        sed -i '/^test ! -x \/sbin\/restorecon/ d' ${D}${sysconfdir}/init.d/populate-volatile.sh
        # read_only_rootfs_hook does not mount fstab and therefore will not
        #  have the correct context when writing to /var/log/lastlog. Attempt
        #  to label this, but do not abort on failure.
        echo "/sbin/restorecon -F /var/log || true" >> ${D}${sysconfdir}/init.d/populate-volatile.sh
}
