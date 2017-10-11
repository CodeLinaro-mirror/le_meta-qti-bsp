inherit autotools-brokensep pkgconfig qcommon

DESCRIPTION = "GPS Loc HAL"
PR = "r5"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

SRC_URI = " \
          ${CAF_LA_GIT}/platform/hardware/qcom/gps.git;protocol=git;nobranch=1;tag=${CAF_TAG};destsuffix=hardware/qcom/gps; \
          ${CAF_LA_GIT}/platform/system/core.git;protocol=git;nobranch=1;tag=${CAF_TAG};destsuffix=system/core/include;subpath=include \
"

S = "${WORKDIR}/hardware/qcom/gps"
DEPENDS = "glib-2.0 gps-utils qmi qmi-framework data loc-pla loc-flp-hdr"
EXTRA_OECONF = "--with-core-includes=${STAGING_INCDIR} \
                --with-locflp-includes=${STAGING_INCDIR}/loc-flp-hdr \
                --with-glib"


CPPFLAGS += "-I${STAGING_INCDIR}/base/include"

PACKAGES = "${PN}"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
FILES_${PN} = "${libdir}/* ${sysconfdir}"
# The loc-hal package contains symlinks that trip up insane
INSANE_SKIP_${PN} = "dev-so"

do_install_append() {
   install -m 0644 -D ${S}/etc/gps.conf ${D}${sysconfdir}/gps.conf
}
