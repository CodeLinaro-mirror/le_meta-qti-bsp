FILESEXTRAPATHS_append_class-native := ":${THISDIR}/${PN}"

#rb1.4 SRC_URI_append_class-native = " \
#rb1.4    https://source.codeaurora.org/quic/le/AGL/meta-agl-extra/plain/meta-app-framework/recipes-support/libcap/libcap/removing-capability-enforcement.patch?h=meta-agl-extra/chinook;downloadfilename=removing-capability-enforcement.patch;md5sum=853ba2f7fb7e71049ebf21569b7ee9dc;sha256sum=51ba6572eeccf24dd3e20c4fa2b8774003e39488fe616e68396adef375eb9ad5 \
#rb1.4 "

PACKAGECONFIG_class-native ?= "attr"
DEPENDS_append_class-native = " attr-native"

