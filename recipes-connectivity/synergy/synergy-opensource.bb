DESCRIPTION = "QTI Synergy opensource MSM8996 AGL Platform"

PACKAGE_ARCH = "${MACHINE_ARCH}"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

DEPENDS = "dbus synergy"

FILESEXTRAPATHS_prepend := "${WORKSPACE}/:"
SRC_URI = "file://synergy"

SRCREV = "${AUTOREV}"

# Remove -Wl,--hash-style=gnu from it to avoid qa error for the prebuilt lib
LDFLAGS = "-Wl,-O1"

S = "${WORKDIR}/synergy"

# compile synergy-opensource
do_compile_opensource () {
    export CC="${CC}"
    export CROSS_COMPILE=${TARGET_PREFIX}

# Build synergy-opensource for qca
if [ -f "${B}/synergy-opensource/platform/msm/makefile" ]; then
    make -C ${B} -f ${B}/synergy-opensource/platform/msm/makefile all IMGRFS=${STAGING_DIR_HOST} CROSS_COMPILE=${TARGET_PREFIX} V=1 CC="${CC}" CHIP_TYPE="QCA"
fi
}


# Install synergy-opensource
do_install_opensource () {
    install -d ${D}${bindir}
    install -d ${D}/lib/systemd
    install -d ${D}/lib/systemd/system
    install -d ${D}/lib/systemd/system/multi-user.target.wants

# Install synergy-bt-service.target to start synergy related service
if [ -f "${B}/synergy-opensource/platform/msm/prebuilt/synergy-bt-service.target" ]; then
    install ${B}/synergy-opensource/platform/msm/prebuilt/synergy-bt-service.target ${D}/lib/systemd/system/multi-user.target.wants/
fi

# [TODO] Install bt_audio_service which can support HF and A2DP audio. Currently, HF audio is verified through "bt_hf_audio.sh".
if [ -f "${B}/synergy-opensource/platform/msm/app/bt_audio_service/bt_audio_service" ]; then
    install ${B}/synergy-opensource/platform/msm/app/bt_audio_service/bt_audio_service ${D}${bindir}
fi
}

#####################################################################################################

do_compile () {

    do_compile_opensource
}

do_install () {

    do_install_opensource
}

FILES_${PN} += "usr/bin \
                lib/systemd/system/* \
                etc/dbus-1/system.d/"
