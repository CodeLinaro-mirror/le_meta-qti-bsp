#Fetch from the CAF location
SRC_URI = "git://source.codeaurora.org/quic/le/ncurses.git;protocol=https;branch=debian/master"
SRC_URI += "file://0001-tic-hang.patch \
            file://0002-configure-reproducible.patch \
            file://config.cache \
"
