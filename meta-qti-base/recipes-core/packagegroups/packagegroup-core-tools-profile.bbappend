RRECOMMENDS:${PN}:remove = "\
    ${PERF} \
    blktrace \
    ${PROFILE_TOOLS_X} \
    ${PROFILE_TOOLS_SYSTEMD} \
    "

RDEPENDS:${PN}:remove = "\
    ${PROFILETOOLS} \
    ${BABELTRACE} \
    ${BABELTRACE2} \
    ${SYSTEMTAP} \
    ${VALGRIND} \
    "

