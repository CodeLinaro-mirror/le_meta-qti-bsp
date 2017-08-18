SUMMARY = "Early_init bash script for early_init feature"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

SRC_URI = "file://early_init"
SRC_URI += "file://early_init_early_eth"

S = "${WORKDIR}"

inherit systemd

do_install() {
    # Add early_init script for early_init feature
    if ${@bb.utils.contains('DISTRO_FEATURES', 'early_init', 'true', 'false', d)} || ${@bb.utils.contains('DISTRO_FEATURES', 'early-ethernet', 'true', 'false', d)}; then
        install -d ${D}${sbindir}
        if ${@bb.utils.contains('DISTRO_FEATURES', 'early-ethernet', 'true', 'false', d)}; then
            install -m 0755 ${S}/early_init_early_eth  ${D}${sbindir}/early_init
        else
            install -m 0755 ${S}/early_init  ${D}${sbindir}/early_init
        fi
        # Disable normal weston.service
        if ${@bb.utils.contains('DISTRO_FEATURES','systemd','true','false',d)}; then
            if [ -n "$D" ]; then
                OPTS="--root=$D"
            fi
            systemctl $OPTS mask weston.service
        fi
    fi
}

FILES_${PN} += " ${sbindir}/early_init"
