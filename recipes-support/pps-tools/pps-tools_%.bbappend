#Package is fetching from codelinaro
SRC_URI:remove = " git://github.com/ago/pps-tools.git;branch=master;protocol=https "
SRC_URI:prepend = " ${CLO_LE_GIT}/pps-tools;protocol=https;branch=caf_migration/ago/master "
