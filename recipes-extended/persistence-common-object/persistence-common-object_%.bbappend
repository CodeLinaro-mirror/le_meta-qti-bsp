TAG = "1.0.3"
SRCREV = "${AUTOREV}"
SRC_URI = " \
    git://github.com/GENIVI/persistence-common-object.git;branch=master;protocol=git \
    file://configure.ac-fix-typo.patch \
    file://B251_typedef_uint64_t.patch \
    "
BB_STRICT_CHECKSUM = "0"

do_fix_commit () {
       cd ${S}
       git checkout ${TAG}
}

do_unpack_append() {
    bb.build.exec_func('do_fix_commit', d)
}
