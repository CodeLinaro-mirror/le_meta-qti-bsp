
TAG =  "v2.11.1"

SRCREV = "${AUTOREV}"
SRC_URI = "git://git.projects.genivi.org/${PN}.git;branch=master;protocol=http \
           file://0001-Fix-build-with-systemd-209.patch \
           file://0002-Don-t-execute-processes-as-a-specific-user.patch \
           file://0003-systemd-unit-type-should-be-in-lowercase-so-use-Type.patch \
           file://0004-Modify-systemd-config-directory.patch"



DEPENDS += " systemd "

SYSTEMD_AUTO_ENABLE_${PN}-systemd = "enable"

EXTRA_OECMAKE += " \
    -DLIB_SUFFIX=64 \
    "

do_fix_commit () {
       cd ${S}
       git checkout ${TAG}
}

do_unpack_append() {
    bb.build.exec_func('do_fix_commit', d)
}

FILES_${PN} += " \
    ${sysconfdir} \
    ${usrlibdir} \
    ${usrbindir} \
    "

