#
#   Copyright (C) 2015 Pelagicore AB
#
#   SPDX-License-Identifier: MIT
#

SUMMARY = "ivi-logging"
DESCRIPTION = "ivi-logging software"
HOMEPAGE = "http://www.pelagicore.com"
LICENSE = "MPL-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=815ca599c9df247a0c7f619bab123dad"

DEPENDS = "glib-2.0"

PV = "0.1+git${SRCREV}"

GIT_REPO = "git://github.com/Pelagicore/ivi-logging.git;protocol=https"

SRC_URI = "${GIT_REPO};branch=master"
SRCREV = "7ec74df57744189bdccb77a184beceefaf446ba3"
S = "${WORKDIR}/git"

inherit cmake pkgconfig

# Make DLT support optional
# To enable, create a .bbappend with PACKAGECONFIG:append = "dlt"
# in your project layer
PACKAGECONFIG ??= ""
PACKAGECONFIG[dlt] = "-DENABLE_DLT_BACKEND=ON,,dlt-daemon,"

FILES:${PN}-dev += "\
       ${libdir}/cmake/ \
       ${docdir}/ \
       "
