FILESEXTRAPATHS_prepend := "${WORKSPACE}/:${THISDIR}"
SRC_URI =  "file://kernel/msm-3.18"
S =  "${WORKDIR}/kernel/msm-3.18"
SRC_URI_8x96auto44  =  "file://kernel/msm-4.4"
S_8x96auto44 =  "${WORKDIR}/kernel/msm-4.4"

FILESEXTRAPATHS_append := ":${THISDIR}/${PN}"

SRC_URI +=  "\
    file://Fix-for-glibc-compilation.patch\
    "
SRC_URI_8x96auto44 += "\
    file://Fix-for-glibc-compilation-with-kernel4.4.patch\
    "


do_install_append(){
        rm -f ${D}${exec_prefix}/include/scsi/sg.h
        rm -f ${D}${exec_prefix}/include/scsi/scsi_ioctl.h 
}