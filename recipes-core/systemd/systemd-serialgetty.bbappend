PR="r6"

FILESEXTRAPATHS_append := ":${THISDIR}/${PN}"

SRC_URI_append = "\
	file://qc-serial-getty@.service \
"

do_getty_install() {
install -m 0644 ${WORKDIR}/qc-serial-getty@.service ${WORKDIR}/serial-getty@.service
}

addtask do_getty_install after do_unpack before do_configure



