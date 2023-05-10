SRC_URI = "${CLO_LE_GIT}/genivi/dlt-daemon;protocol=${CLO_PROTOCOL};nobranch=1;name=dltd \
    file://0002-Don-t-execute-processes-as-a-specific-user.patch \
    file://0004-Modify-systemd-config-directory.patch \
    "
SRCREV_dltd = "f5095cf33d806de1061652fa79f8ddb215c46ac4"

do_install() {
    cmake_do_install
    cp ${D}${bindir}/dlt-test-* ${D}/
}

do_install_test() {
    mv ${D}/dlt-test-* ${D}${bindir}/
}

addtask do_install_test before do_package after do_install

