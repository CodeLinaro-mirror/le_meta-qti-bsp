#Fetch from the CLO location
SRC_URI = "git://git.codelinaro.org/clo/le/ncurses.git;protocol=https;branch=caf_migration/debian/master"
SRC_URI += "file://0001-tic-hang.patch \
            file://0002-configure-reproducible.patch \
            file://config.cache \
"
