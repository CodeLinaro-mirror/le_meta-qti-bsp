require recipes-kernel/linux/linux-msm.inc

FILESEXTRAPATHS_append := ":${THISDIR}/${PN}"

SRC_URI += "\
file://Fix-for-glibc-compilation.patch \
"

do_install_append(){
        rm -f ${D}${exec_prefix}/include/scsi/sg.h
        rm -f ${D}${exec_prefix}/include/scsi/scsi_ioctl.h 

}
