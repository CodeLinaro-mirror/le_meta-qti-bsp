MY_SRC = "${WORKDIR}/kernel/rh-kernel-5.14"
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"	
SRC_URI += "file://0001-centos-5.14-Fix-to-bypass-redhad-env.patch \
            file://0002-centos-5.14-build-fixes-while-porting-from-5.4.patch \
            file://0003-rh-5.14-Bringing-in-GKI-to-generate-defconfig-same-a.patch \
	    file://0001-Fix-for-build-issue.patch \
"

do_patch_more() {
    cd ${MY_SRC}
    patch -f -p1 < ${WORKDIR}/0001-centos-5.14-Fix-to-bypass-redhad-env.patch
    patch -f -p1 < ${WORKDIR}/0002-centos-5.14-build-fixes-while-porting-from-5.4.patch
    patch -f -p1 < ${WORKDIR}/0003-rh-5.14-Bringing-in-GKI-to-generate-defconfig-same-a.patch
    patch -f -p1 < ${WORKDIR}/0001-Fix-for-build-issue.patch
    cp -rf ${WORKDIR}/defconfig ${MY_SRC}/arch/arm64/configs/defconfig
    chmod a+x ${MY_SRC}/scripts/gki/*
}
addtask patch_more after do_unpack before do_generate_gki_defconfig 
