inherit qlicense
DESCRIPTION = "Start up script for hwfde_service"

SRC_URI +="file://fde_invoke.sh"

PR = "r3"

INITSCRIPT_NAME = "fdeinvoke"
INITSCRIPT_PARAMS = "start 10 2 3 4 5 . stop 20 0 1 6 ."

inherit update-rc.d

do_install_append() {
	install -m 0755 ${WORKDIR}/fde_invoke.sh -D ${D}${sysconfdir}/init.d/${INITSCRIPT_NAME}
}
