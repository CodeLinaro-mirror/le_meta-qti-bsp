#
#   Copyright (C) 2016 - 2017 Pelagicore AB
#
#   SPDX-License-Identifier: MIT
#

DESCRIPTION = "The SoftwareContainer framework"
HOMEPAGE = "https://source.codeaurora.org/quic/le/softwarecontainer"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://LICENSE;md5=4fbd65380cdd255951079008b364516c"
FILESEXTRAPATHS_append := ":${THISDIR}/files"

PR = "r0"
PV = "1.0+git${SRCREV}"

SRC_URI = "${PATH_TO_REPO}/external/softwarecontainer/.git;protocol=git;branch=master;destsuffix=softwarecontainer"
SRC_URI += "\
    file://softwarecontainer-agent.service \
"
SRCREV = "${AUTOREV}"

DEPENDS = "ivi-logging glibmm lxc jansson dbus-glib"

inherit cmake systemd pkgconfig

S = "${WORKDIR}/external/softwarecontainer/"

PACKAGECONFIG[pulsegateway] = "-DENABLE_PULSEGATEWAY=ON,-DENABLE_PULSEGATEWAY=OFF,pulseaudio"
PACKAGECONFIG[filegateway] = "-DENABLE_FILEGATEWAY=ON,-DENABLE_FILEGATEWAY=OFF"
PACKAGECONFIG[networkgateway] = "-DENABLE_NETWORKGATEWAY=ON,-DENABLE_NETWORKGATEWAY=OFF,,iptables bridge-utils"
PACKAGECONFIG[devicenodegateway] = "-DENABLE_DEVICENODEGATEWAY=ON,-DENABLE_DEVICENODEGATEWAY=OFF"
PACKAGECONFIG[dbusgateway] = "-DENABLE_DBUSGATEWAY=ON,-DENABLE_DBUSGATEWAY=OFF"
PACKAGECONFIG[cgroupsgateway] = "-DENABLE_CGROUPSGATEWAY=ON,-DENABLE_CGROUPSGATEWAY=OFF"
PACKAGECONFIG[examples] = "-DENABLE_EXAMPLES=ON,-DENABLE_EXAMPLES=OFF"
PACKAGECONFIG[test] = "-DENABLE_TEST=ON,-DENABLE_TEST=OFF"
PACKAGECONFIG ?= "filegateway networkgateway devicenodegateway cgroupsgateway"

SYSTEMD_SERVICE_${PN} = "softwarecontainer-agent.service"
SYSTEMD_AUTO_ENABLE_${PN} = "enable"

PACKAGES = "${PN}-examples ${PN} ${PN}-dev ${PN}-dbg ${PN}-doc ${PN}-locale"

do_install_append() {
    install -d ${D}/lib/systemd/system/
    install -m 0644 ${S}/../../softwarecontainer-agent.service ${D}/lib/systemd/system/
}

FILES_${PN} += " \
    ${libdir}/libsoftwarecontainercommon.so \
    ${systemd_unitdir}/system \
    ${sysconfdir}/dbus-1 \
    /lib/systemd/system/softwarecontainer-agent.service \
"

FILES_${PN}-examples = " \
    ${datadir}/softwarecontainer/examples/ \
    ${datadir}/dbus-1/system-services/com.pelagicore.TemperatureService.service \
"

FILES_${PN}-dbg += "${datadir}/softwarecontainer/examples/*/.debug"

INSANE_SKIP_${PN} += "useless-rpaths"
INSANE_SKIP_${PN}-dev += "useless-rpaths"
