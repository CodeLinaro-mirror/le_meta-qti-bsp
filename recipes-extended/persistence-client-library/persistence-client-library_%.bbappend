SRC_URI = " \
    ${CLO_LE_GIT}/genivi/persistence/persistence-client-library;protocol=${CLO_PROTOCOL};nobranch=1;name=pclib \
    file://0001-load-correct-version-of-libpers_common.patch \
    file://0001-fix-exec-path.patch \
    "
SRCREV_pclib = "fe4b73dcc282932ae3ebb8805e0b617a0016dc9a" 

do_install_append() {
    if [ "${TARGET_ARCH}" = "aarch64" ];then
        sed -i -e 's/\ \/usr\/lib/\ \/usr\/lib64/' ${D}${sysconfdir}/pclCustomLibConfigFile.cfg
    fi
}
