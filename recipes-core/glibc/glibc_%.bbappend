# Adjust branch & URI to fetch from CAF.
SRCBRANCH = "drains/release/${PV}/master"
GLIBC_GIT_URI = "git://source.codeaurora.org/quic/le/glibc.git;protocol=https"

# glibc expects -fstack-protector optimization passed as a configuration option
# instead of a top level build flag.
python __anonymous () {
    sel_opt = d.getVar("SELECTED_OPTIMIZATION", True).split()

    for opt in sel_opt:
        if opt in ("-fstack-protector", "-fstack-protector-all", "-fstack-protector-strong"):
            # bb.note("%s can't be built with %s" % (d.getVar('PN'), sel_opt))
            sel_opt.remove(opt)
    d.setVar('SELECTED_OPTIMIZATION', ' '.join(sel_opt))
}

EXTRA_OECONF += "${@bb.utils.contains('FULL_OPTIMIZATION', '-fstack-protector', '--enable-stack-protector=yes', '', d)}"
EXTRA_OECONF += "${@bb.utils.contains('FULL_OPTIMIZATION', '-fstack-protector-all', '--enable-stack-protector=all', '', d)}"
EXTRA_OECONF += "${@bb.utils.contains('FULL_OPTIMIZATION', '-fstack-protector-strong', '--enable-stack-protector=strong', '', d)}"

EXTRA_OECONF += "${@bb.utils.contains('DEBUG_OPTIMIZATION', '-fstack-protector', '--enable-stack-protector=yes', '', d)}"
EXTRA_OECONF += "${@bb.utils.contains('DEBUG_OPTIMIZATION', '-fstack-protector-all', '--enable-stack-protector=all', '', d)}"
EXTRA_OECONF += "${@bb.utils.contains('DEBUG_OPTIMIZATION', '-fstack-protector-strong', '--enable-stack-protector=strong', '', d)}"

do_poststash_install_cleanup_lib64() {
	# Remove all files which do_stash_locale would remove (mv)
	# since that task could have come from sstate and not get run.
	for i in ${bashscripts}; do
	    rm -f ${D}${bindir}/$i
	done
	rm -f ${D}${bindir}/localedef
	rm -rf ${D}${datadir}/i18n
	rm -rf ${D}${libdir}/gconv
	rm -rf ${D}/${localedir}
	rm -rf ${D}${datadir}/locale
	if [ "${nonarch_libdir}" != "${exec_prefix}/lib" ]; then
		if [ -d ${D}${exec_prefix}/lib ]; then
			# error out if directory isn't empty
			# this dir should only contain locale dir
			# which has been deleted in the previous step
			rmdir ${D}${exec_prefix}/lib
		fi
	fi
}

deltask do_poststash_install_cleanup
addtask do_poststash_install_cleanup_lib64 after do_stash_locale do_install before do_populate_sysroot do_package
