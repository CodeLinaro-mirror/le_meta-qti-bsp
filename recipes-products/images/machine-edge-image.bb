inherit qimage

#  Defined in qimage.bbclass. Following is the order of priority.
#  P1: <basemachine>/<basemachine>-<distro>-base-image.inc
#  P2: <basemachine>/<basemachine>-base-image.inc
#  P3: common/common-base-image.inc
# include ${@get_bblayer_img_inc('base', d)}
include ${BASEMACHINE}/${BASEMACHINE}-edge-image.inc

require include/mdm-bootimg.inc
DEPENDS += " mkbootimg-native"

MULTILIBRE_ALLOW_REP =. "/usr/include/python2.7/*|${base_bindir}|${base_sbindir}|${bindir}|${sbindir}|${libexecdir}|${sysconfdir}|${nonarch_base_libdir}/udev|/lib/modules/[^/]*/modules.*|"
