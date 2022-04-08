SRC_URI = " \
    ${CLO_LE_GIT}/genivi/persistence/persistence-client-library;protocol=https;branch=caf_migration/genivi/persistence-client-library/master \
    file://0001-load-correct-version-of-libpers_common.patch \
    file://0001-fix-exec-path.patch \
    "
do_install_append() {
    if [ "${TARGET_ARCH}" = "aarch64" ];then
        sed -i -e 's/\ \/usr\/lib/\ \/usr\/lib64/' ${D}${sysconfdir}/pclCustomLibConfigFile.cfg
    fi
}
