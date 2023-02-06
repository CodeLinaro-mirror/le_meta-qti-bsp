#Package is fetch from the codelinaro

SRC_URI:remove = "git://salsa.debian.org/debian/ca-certificates.git;protocol=https;branch=master"
SRC_URI:prepend = "${CLO_LE_GIT}/ca-certificates.git;protocol=https;branch=debian/master"
