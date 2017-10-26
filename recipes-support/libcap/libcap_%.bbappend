FILESEXTRAPATHS_append_class-native := ":${THISDIR}/${PN}"
SRC_URI_append_class-native = " file://removing-capability-enforcement.patch"
PACKAGECONFIG_class-native ?= "attr"
DEPENDS_append_class-native = " attr-native"

python do_getpatches() {
    import os

    cmd = "mkdir -p ${WORKSPACE}/poky/meta-qti-bsp/recipes-support/libcap/libcap && (wget https://source.codeaurora.org/quic/le/AGL/meta-agl-extra/plain/meta-app-framework/recipes-support/libcap/libcap/removing-capability-enforcement.patch?h=meta-agl-extra/chinook -O ${WORKSPACE}/poky/meta-qti-bsp/recipes-support/libcap/libcap/removing-capability-enforcement.patch || pwd)"

    os.system(cmd)
}

addtask getpatches before do_fetch
