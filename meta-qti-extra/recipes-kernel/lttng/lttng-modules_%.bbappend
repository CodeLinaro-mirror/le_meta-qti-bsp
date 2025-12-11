# Fix kernel modules parallel compile issue
do_compile[lockfiles] += "${TMPDIR}/qti-techpack.lock"
