FILESEXTRAPATHS_append := ":${THISDIR}/${PN}"

SRC_URI_append = "\
    file://weston.ini_qc \
    file://0001-outpub_fbdev-follow-the-work-flow-of-MSM8996.patch \
    file://0001-configure-don-t-control-egl-version.patch \
    "
CFLAGS += "-idirafter ${STAGING_KERNEL_DIR}/include/"

do_install_append() {
    install -m 0644 ${WORKDIR}/weston.ini_qc ${D}${WESTON_INI_CONFIG}/weston.ini
}
