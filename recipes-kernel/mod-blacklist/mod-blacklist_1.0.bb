DESCRIPTION = "Kernel module blacklist configuration"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

FILESEXTRAPATHS:prepend := "${KERNEL_PREBUILT_PATH}:${KERNEL_PLATFORM_PATH}/msm-kernel:"

python __anonymous () {
    blacklist = d.getVar('KERNEL_MODULES_BLACKLIST') or ""
    blacklist_files = blacklist.split()

    for file in blacklist_files :
       d.setVar('SRC_URI:append', "file://" + file + " ")
}

do_compile() {
   for file in ${KERNEL_MODULES_BLACKLIST} ; do
       cat ${WORKDIR}/${file} | sed -e '/^ *#/d;/^ *$/d' >> ${B}/blacklist.conf
   done

   if [ -e ${B}/blacklist.conf ]; then
      sed -i 's/^blocklist /blacklist /g' ${B}/blacklist.conf
   fi

   for mod in ${EXTRA_BLACKLIST_MOD}; do
       echo "blacklist ${mod}" >> ${B}/blacklist.conf
   done

   if [ ! -e ${B}/blacklist.conf ]; then
        echo "## Empty file ##" > ${B}/blacklist.conf
   fi
}

do_install() {
    install -Dm644 "${B}/blacklist.conf" "${D}${sysconfdir}/modprobe.d/blacklist.conf"
}

PACKAGES = "${PN}"
FILES:${PN} += " \
    ${sysconfdir}/modprobe.d/blacklist.conf \
"
