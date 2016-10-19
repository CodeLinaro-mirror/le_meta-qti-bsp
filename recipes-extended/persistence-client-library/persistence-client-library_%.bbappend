
do_install_append() {
    if [ "${TARGET_ARCH}" = "aarch64" ];then
        sed -i -e 's/\ \/usr\/lib/\ \/usr\/lib64/' ${D}${sysconfdir}/pclCustomLibConfigFile.cfg
    fi
}

