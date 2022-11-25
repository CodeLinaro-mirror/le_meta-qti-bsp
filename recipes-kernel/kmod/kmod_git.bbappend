#Package is fetching from the codelinaro
SRC_URI = "${CLO_LE_GIT}/kmod.git;branch=kmod/master;protocol=https"
 
SRC_URI += " \
           file://depmod-search.conf \
           file://0001-build-Stop-using-dolt.patch \
           file://avoid_parallel_tests.patch \
          "


FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
