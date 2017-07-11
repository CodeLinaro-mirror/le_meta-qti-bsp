FILESEXTRAPATHS_prepend := "${WORKSPACE}/:${THISDIR}"

FILESEXTRAPATHS_append := ":${THISDIR}/${PN}"

do_install_append(){
        rm -f ${D}${exec_prefix}/include/scsi/sg.h
        rm -f ${D}${exec_prefix}/include/scsi/scsi_ioctl.h 
}

# Including the file depends on MACHINE
INCSUFFIX = "${@base_conditional('MACHINEGROUP', 'auto', 'linux-libc-headers_auto', 'none',d)}"
include ${INCSUFFIX}.inc
