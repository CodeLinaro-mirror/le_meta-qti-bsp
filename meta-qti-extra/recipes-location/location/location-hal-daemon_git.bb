require ../include/common-location-defines.inc
SUMMARY = "location-hal-daemon"
DESCRIPTION = "location hal daemon service"
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

DEPENDS += "gps-utils loc-hal loc-pla-hdr location-api location-api-iface location-api-msg-proto"

SRC_URI = "\
    ${PATH_TO_REPO}/qcom-opensource/location/.git;protocol=${PROTO};destsuffix=qcom-opensource/location/location_hal_daemon;subpath=location_hal_daemon;usehead=1 \
    file://location_hal_daemon.service \
    file://location_hal_daemon-tmpfilesd.conf \
"
SRCREV = "${AUTOREV}"
S = "${WORKDIR}/qcom-opensource/location/location_hal_daemon"

inherit autotools-brokensep update-rc.d systemd pkgconfig useradd

EXTRA_OECONF += "--enable-target=${BASEMACHINE}"
# location-hal-daemon_git.bbappend has additional configs
EXTRA_OECONF += "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '--with-systemd', '', d)}"

INITSCRIPT_NAME = "location_hal_initializer"
INITSCRIPT_PARAMS = "start 98 2 3 4 5 . stop 2 0 1 6 ."

USERADD_PACKAGES = "${PN}"

GROUPADD_PARAM:${PN} = "system; locclient; gps; diag; inet; radio"
USERADD_PARAM:${PN} = "\
    --no-create-home -g locclient --shell /bin/false locclient; \
    --no-create-home -g gps -G system,locclient --shell /bin/false gps; \
"

do_install:append () {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        ## Install systemd-tmpfiles config file
        install -d ${D}${sysconfdir}/tmpfiles.d/
        install -m 0644 ${WORKDIR}/location_hal_daemon-tmpfilesd.conf ${D}${sysconfdir}/tmpfiles.d/${BPN}.conf

        ## Install systemd service unit file
        install -d ${D}${sysconfdir}/systemd/system/
        install -m 0644 ${WORKDIR}/location_hal_daemon.service -D ${D}${sysconfdir}/systemd/system/location_hal_daemon.service


        # Enable the service for multi-user.target
        install -d ${D}${sysconfdir}/systemd/system/multi-user.target.wants/
        ln -sf ${sysconfdir}/systemd/system/location_hal_daemon.service \
                ${D}${sysconfdir}/systemd/system/multi-user.target.wants/location_hal_daemon.service
    else
        install -m 0755 ${S}/location_hal_initializer -D ${D}${sysconfdir}/init.d/location_hal_initializer
    fi
}

