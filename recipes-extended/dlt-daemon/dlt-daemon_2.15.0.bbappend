SRC_URI = "${CLO_LE_GIT}/genivi/dlt-daemon;protocol=https;branch=caf_migration/github/master \
    file://0002-Don-t-execute-processes-as-a-specific-user.patch \
    file://0004-Modify-systemd-config-directory.patch \
    "

do_install() {
    cmake_do_install
    cp ${D}${bindir}/dlt-test-* ${D}/
}

do_install_test() {
    mv ${D}/dlt-test-* ${D}${bindir}/
}

addtask do_install_test before do_package after do_install

