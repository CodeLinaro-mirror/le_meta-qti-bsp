inherit autotools-brokensep update-rc.d systemd pkgconfig
require ../include/common-location-defines.inc

DESCRIPTION = "location hal daemon service"
PR = "r1"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI = "${PATH_TO_REPO}/qcom-opensource/location/.git;protocol=${PROTO};destsuffix=qcom-opensource/location/location_hal_daemon;subpath=location_hal_daemon;usehead=1"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/qcom-opensource/location/location_hal_daemon"

DEPENDS += "loc-pla-hdr loc-hal gps-utils location-api-iface location-api location-api-msg-proto"

EXTRA_OECONF += "--with-locationapi-includes=${STAGING_INCDIR}/location-api-iface"
EXTRA_OECONF += "--enable-target=${BASEMACHINE}"

# location-hal-daemon_git.bbappend has additional configs
EXTRA_OECONF += "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '--with-systemd', '', d)}"
EXTRA_OECONF += "${@oe.utils.conditional('DISTRO', 'auto', '--with-auto_feature', '', d)}"

INITSCRIPT_NAME = "location_hal_initializer"
INITSCRIPT_PARAMS = "start 98 2 3 4 5 . stop 2 0 1 6 ."

SRC_URI +="file://location_hal_daemon.service"
SRC_URI +="file://location_hal_daemon-tmpfilesd.conf"

PACKAGES = "${PN}"
FILES_${PN} += "${libdir}/*"
FILES_${PN} += "/usr/include/*"

INSANE_SKIP_${PN} = "dev-deps"

do_install_append () {

    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        ## Install systemd-tmpfiles config file
        install -d ${D}${sysconfdir}/tmpfiles.d/
        install -m 0644 ${WORKDIR}/location_hal_daemon-tmpfilesd.conf ${D}${sysconfdir}/tmpfiles.d/${BPN}.conf

        ## Install systemd service unit file
        install -d ${D}${sysconfdir}/systemd/system/
        install -m 0644 ${WORKDIR}/location_hal_daemon.service -D ${D}${sysconfdir}/systemd/system/location_hal_daemon.service


        # Enable the service for multi-user.target
        install -d ${D}${sysconfdir}/systemd/system/multi-user.target.wants/
        ln -sf /etc/systemd/system/location_hal_daemon.service \
                ${D}/etc/systemd/system/multi-user.target.wants/location_hal_daemon.service
    else
        install -m 0755 ${S}/location_hal_initializer -D ${D}${sysconfdir}/init.d/location_hal_initializer
    fi
}

