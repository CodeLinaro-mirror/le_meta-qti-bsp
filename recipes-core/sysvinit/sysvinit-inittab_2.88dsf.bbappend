FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

SRC_URI += "\
           file://${BASEMACHINE}/inittab \
"

SERIAL_CONSOLE = "115200 console"
TERMINAL = "${@base_contains('BASEMACHINE', 'apq8098', 'ttyMSM0', 'ttyHSL0', d)}"
USE_VT = "0"
SYSVINIT_ENABLED_GETTYS = ""

do_install() {
    install -d ${D}${sysconfdir}
    install -m 0644 ${WORKDIR}/${BASEMACHINE}/inittab ${D}${sysconfdir}/inittab

    if [ "${BASEMACHINE}" == "mdm9607" ]; then
        if [ -d "${WORKSPACE}/security/securemsm-noship/qwes" ]; then
            ## add entry for qwesd after fs-scrub-daemon
            perl -0777pe 's/fs-scrub-daemon\n/fs-scrub-daemon\nqlst:2345:wait:\/usr\/bin\/start_license_store\nqwes:2345:respawn:\/usr\/bin\/qwesd\n/s' -i ${D}${sysconfdir}/inittab
        fi

        if [ -d "${WORKSPACE}/poky/meta-qti-location" ]; then
            ## add entry for location daemons
            echo "m4:5:respawn:/usr/bin/location_hal_daemon" >> ${D}${sysconfdir}/inittab
            echo "m5:5:respawn:/usr/bin/loc_launcher" >> ${D}${sysconfdir}/inittab
            echo >> ${D}${sysconfdir}/inittab
        else
            ## older PLs has only loc_launcher
            echo "m4:5:respawn:/usr/bin/loc_launcher" >> ${D}${sysconfdir}/inittab
            echo >> ${D}${sysconfdir}/inittab
        fi
    fi


    if [ ! -z "${SERIAL_CONSOLE}" ]; then
        echo "S:023456:respawn:${base_sbindir}/getty -L ${TERMINAL} ${SERIAL_CONSOLE}" >> ${D}${sysconfdir}/inittab
    fi

    idx=0
#   tmp="${SERIAL_CONSOLES}"
    tmp=""
    for i in $tmp
    do
        j=`echo ${i} | sed s/\;/\ /g`
        echo "${idx}:2345:respawn:${base_sbindir}/getty ${j}" >> ${D}${sysconfdir}/inittab
        idx=`expr $idx + 1`
    done

    if [ "${USE_VT}" = "1" ]; then
        cat <<EOF >>${D}${sysconfdir}/inittab
# ${base_sbindir}/getty invocations for the runlevels.
#
# The "id" field MUST be the same as the last
# characters of the device (after "tty").
#
# Format:
#  <id>:<runlevels>:<action>:<process>
#

EOF

        for n in ${SYSVINIT_ENABLED_GETTYS}
        do
            echo "$n:2345:respawn:${base_sbindir}/getty 38400 tty$n" >> ${D}${sysconfdir}/inittab
        done
        echo "" >> ${D}${sysconfdir}/inittab
    fi
}
