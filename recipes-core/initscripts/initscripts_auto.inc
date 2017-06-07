# mask redundant public initscript

MASKED_SCRIPTS += " \
save-rtc \
reboot \
single \
umountfs \
umountnfs \
sendsigs \
halt \
single \
bsp_paths \
set_core_pattern \
"
pkg_postinst_${PN}_append () {
        if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
                if [ -n "$D" ]; then
                        OPTS="--root=$D"
                fi
                for SERVICE in ${MASKED_SCRIPTS}; do
                        systemctl $OPTS mask $SERVICE.service
                done
        fi
}

