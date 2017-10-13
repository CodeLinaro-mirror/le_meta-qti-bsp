
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += " \
   file://low-level-can-service-build-error-fix-for-gcc-5.3.patch \
   file://low-can-write-for-dab-400.patch \
"

OECMAKE_CXX_FLAGS_append = " -std=c++11 "