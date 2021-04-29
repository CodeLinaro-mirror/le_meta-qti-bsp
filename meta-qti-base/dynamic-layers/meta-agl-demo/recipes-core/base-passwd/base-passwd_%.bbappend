# Switch root user back to AGL app framework expected location
do_install_append () {
    sed -i 's|:/home/root:|:/home/0:|' ${D}${datadir}/base-passwd/passwd.master
}
