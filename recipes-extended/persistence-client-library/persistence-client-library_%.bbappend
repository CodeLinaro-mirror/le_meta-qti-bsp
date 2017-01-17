SRC_URI = " \
    git://git.projects.genivi.org/persistence/persistence-client-library.git;tag=v${PV};protocol=http \
    file://0001-load-correct-version-of-libpers_common.patch \
    file://0001-fix-exec-path.patch \
    "
do_install_append() {
    if [ "${TARGET_ARCH}" = "aarch64" ];then
        sed -i -e 's/\ \/usr\/lib/\ \/usr\/lib64/' ${D}${sysconfdir}/pclCustomLibConfigFile.cfg
    fi
}

