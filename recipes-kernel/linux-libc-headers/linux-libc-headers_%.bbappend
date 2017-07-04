FILESEXTRAPATHS_prepend := "${WORKSPACE}/:${THISDIR}"


SRC_URI_8x96autofusion  =  "file://kernel/msm-4.4"
S_8x96autofusion =  "${WORKDIR}/kernel/msm-4.4"

SRC_URI_8x96autonapier =  "file://kernel/msm-3.18"
S_8x96autonapier =  "${WORKDIR}/kernel/msm-3.18"

SRC_URI_8x96autogvmquin =  "file://kernel/msm-3.18"
S_8x96autogvmquin =  "${WORKDIR}/kernel/msm-3.18"

SRC_URI_8x96autogvmquin44 =  "file://kernel/msm-4.4"
S_8x96autogvmquin44 =  "${WORKDIR}/kernel/msm-4.4"

SRC_URI_8x96autogvmred =  "file://kernel/msm-3.18"
S_8x96autogvmred =  "${WORKDIR}/kernel/msm-3.18"

FILESEXTRAPATHS_append := ":${THISDIR}/${PN}"

SRC_URI_8x96autofusion += "file://Fix-for-glibc-compilation-with-kernel4.4.patch"
SRC_URI_8x96autonapier += "file://Fix-for-glibc-compilation.patch"
SRC_URI_8x96autogvmquin += "file://Fix-for-glibc-compilation.patch"
SRC_URI_8x96autogvmquin44 += "file://Fix-for-glibc-compilation-with-kernel4.4.patch"
SRC_URI_8x96autogvmred += "file://Fix-for-glibc-compilation.patch"


do_install_append(){
        rm -f ${D}${exec_prefix}/include/scsi/sg.h
        rm -f ${D}${exec_prefix}/include/scsi/scsi_ioctl.h 
}

# Including the file depends on MACHINE
INCSUFFIX = "${@base_conditional('BASEMACHINE', '8x96auto', 'linux-libc-headers-8x96auto', 'none',d)}"
include ${INCSUFFIX}.inc
