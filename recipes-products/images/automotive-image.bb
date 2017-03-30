#This common include file contains modules common
#to the mctm and AGL project. The project specific
#modules needs to added in specific machine related
#{MACHINE}-image.inc file

MACHINE ??= "8x96auto"

inherit qperf

require ${MACHINE}/${MACHINE}-image.inc
include machine-auto-image.bb
#IMAGE_INSTALL += "packagegroup-agl-ivi"
