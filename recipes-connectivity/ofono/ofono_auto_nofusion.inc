FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://Revert-test-Convert-to-Python-3.patch"
SRC_URI += "file://0002-ofono-Added-a-change-RIL-to-use-root-uid.patch"
SRC_URI += "file://0003-ofono-Export-RIL-variables-while-starting-ofono-serv.patch"

LDFLAGS += "-lcutils"

CFLAGS_append_libc-uclibc = " -D_GNU_SOURCE"
CFLAGS_append += "-DRIL_USE_ROOT_UID"

INITSCRIPT_NAME = "ofono"
INITSCRIPT_PARAMS = "start 99 5 3 2 . stop 01 0 1 6 ."

do_install[noexec] = "1"
do_default_install() {
	autotools_do_install
}
addtask default_install after do_compile before do_install
