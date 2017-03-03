#This common include file contains modules common
#to the mctm and AGL project. The project specific
#modules needs to added in specific machine related
#{MACHINE}-image.inc file

require ${MACHINE}/${MACHINE}-image.inc
require common-auto-image.inc

#include machine-auto-image.bb


#IMAGE_INSTALL += "packagegroup-agl-ivi"
