#This common include file contains modules common
#to the mctm and AGL project. The project specific
#modules needs to added in specific machine related
#{MACHINE}-image.inc file

require target/${MACHINE}-image.inc
require target/common-auto-image.inc

include machine-image.bb
