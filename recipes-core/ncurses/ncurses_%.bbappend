#Fetch the packages from the codelinaro
SRC_URI:remove = "git://salsa.debian.org/debian/ncurses.git;protocol=https;branch=master"
SRC_URI:prepend = "${CLO_LE_GIT}/ncurses.git;protocol=https;branch=debian/master"
