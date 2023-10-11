#Fetch the package from the codelinaro
SRC_URI = "${CLO_LE_GIT}/ncurses.git;protocol=https;branch=debian/master"
SRC_URI += "file://0001-tic-hang.patch \
           file://0002-configure-reproducible.patch \
           file://0003-gen-pkgconfig.in-Do-not-include-LDFLAGS-in-generated.patch \
           file://CVE-2021-39537.patch \
           file://CVE-2022-29458.patch \
           "
