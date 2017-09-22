SUMMARY = "Early_init bash script for early_init feature"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI  = "file://early_init.c"
SRC_URI += "file://early_init.conf"
SRC_URI += "file://early_init_eth.conf"

S = "${WORKDIR}"

inherit systemd

do_compile() {
        ${CC} ${CFLAGS} ${LDFLAGS} -static -o ${S}/early_init ${S}/early_init.c
}

do_install() {
    # Add early_init script for early_init feature
    if ${@bb.utils.contains('DISTRO_FEATURES', 'early_init', 'true', 'false', d)} || ${@bb.utils.contains('DISTRO_FEATURES', 'early-ethernet', 'true', 'false', d)}; then
        install -d ${D}${sbindir}
        install -m 0755 ${S}/early_init  ${D}${sbindir}/early_init
        install -d ${D}${sysconfdir}
        if ${@bb.utils.contains('DISTRO_FEATURES', 'early-ethernet', 'true', 'false', d)}; then
            install -m 0644 ${S}/early_init_eth.conf  ${D}${sysconfdir}/early_init.conf
        else
            install -m 0644 ${S}/early_init.conf  ${D}${sysconfdir}/early_init.conf
        fi
    fi
}

FILES_${PN} += " ${sbindir}/early_init"
