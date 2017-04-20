#This common include file contains modules common
#to the mctm and AGL project. The project specific
#modules needs to added in specific machine related
#{MACHINE}-image.inc file

MACHINE ??= "8x96auto"

inherit qperf

include machine-auto-image.bb
require ${MACHINE}/${MACHINE}-image.inc
#IMAGE_INSTALL += "packagegroup-agl-ivi"
