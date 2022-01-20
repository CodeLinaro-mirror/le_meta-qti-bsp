SUMMARY = "Audiohal Module"
DESCRIPTION = "Abstraction layer for audio board hardware, serves as an interface between the user application and the hardware driver."
HOMEPAGE = "https://www.codeaurora.org"
LICENSE = "BSD-3-Clause & Apache-2.0"
LIC_FILES_CHKSUM = "\
    file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9 \
    file://${COREBASE}/meta/files/common-licenses/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
"
DEPENDS += "acdbloader expat glib-2.0 libcutils libhardware libhardware-headers qahw system-core system-media tinyalsa tinycompress"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/audio-hal/primary-hal/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/audio-hal/primary-hal;subpath=hal;usehead=1"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/audio-hal/primary-hal"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit autotools pkgconfig

EXTRA_OECONF += "\
    --enable-target=${BASEMACHINE} \
    --with-glib --program-suffix=${HALBINSUFFIX} \
    --with-audio-kernel-headers=${AUDIO_KERNEL_HEADERS} \
    AUDIO_FEATURE_ENABLED_INSTANCE_ID=true \
    AUDIO_FEATURE_ENABLED_ACDBLOADER_SERVICE=true \
    AUDIO_FEATURE_ENABLED_SSR=false \
    BOARD_SUPPORTS_QAHW=true \
    AUDIO_FEATURE_ENABLED_HDMI_EDID=false \
    AUDIO_FEATURE_ENABLED_FM_POWER_OPT=false \
    AUDIO_FEATURE_ENABLED_USBAUDIO=true \
    AUDIO_FEATURE_ENABLED_HFP=true \
    AUDIO_FEATURE_ENABLED_MULTI_VOICE_SESSIONS=true \
    AUDIO_FEATURE_ENABLED_COMPRESS_VOIP=true \
    AUDIO_FEATURE_ENABLED_SPKR_PROTECTION=false \
    MULTIPLE_HW_VARIANTS_ENABLED=true \
    AUDIO_FEATURE_ENABLED_COMPRESS_CAPTURE=false \
    AUDIO_FEATURE_ENABLED_DTS_EAGLE=false \
    DOLBY_DDP=false \
    DS1_DOLBY_DAP=false \
    AUDIO_FEATURE_ENABLED_DEV_ARBI=false \
    AUDIO_FEATURE_ENABLED_SOURCE_TRACKING=true \
    AUDIO_FEATURE_ENABLED_LISTEN=false \
    BOARD_SUPPORTS_SOUND_TRIGGER=false \
    AUDIO_FEATURE_ENABLED_PM_SUPPORT=false \
    AUDIO_FEATURE_ENABLED_EXTN_FLAC_DECODER=true \
    AUDIO_FEATURE_ENABLED_EXTN_ALAC_DECODER=true \
    AUDIO_FEATURE_ENABLED_EXTN_VORBIS_DECODER=true \
    AUDIO_FEATURE_ENABLED_EXTN_WMA_DECODER=true \
    AUDIO_FEATURE_ENABLED_EXTN_AMR_DECODER=true \
    AUDIO_FEATURE_ENABLED_MULTI_RECORD=true \
    AUDIO_FEATURE_ENABLED_PROXY_DEVICE=true \
    AUDIO_FEATURE_ENABLED_COMPRESS_INPUT=true \
    AUDIO_FEATURE_ENABLED_HDMI_PASSTHROUGH=false \
    AUDIO_FEATURE_ENABLED_KEEP_ALIVE=true \
    AUDIO_FEATURE_ENABLED_APTX_DECODER=false \
    AUDIO_FEATURE_ENABLED_GEF_SUPPORT=true \
    AUDIO_FEATURE_ENABLED_EXTN_FORMATS=true \
    AUDIO_FEATURE_ENABLED_EXTN_RESAMPLER=true \
    AUDIO_FEATURE_ENABLED_FM_POWER_OPT=true \
    AUDIO_FEATURE_ENABLED_HDMI_SPK=true \
    AUDIO_FEATURE_ENABLED_PCM_OFFLOAD=true \
    AUDIO_FEATURE_ENABLED_PCM_OFFLOAD_24=true \
    AUDIO_FEATURE_ENABLED_FLAC_OFFLOAD=true \
    AUDIO_FEATURE_ENABLED_VORBIS_OFFLOAD=true \
    AUDIO_FEATURE_ENABLED_WMA_OFFLOAD=true \
    AUDIO_FEATURE_ENABLED_ALAC_OFFLOAD=true \
    AUDIO_FEATURE_ENABLED_APE_OFFLOAD=true \
    AUDIO_FEATURE_ENABLED_AAC_ADTS_OFFLOAD=true \
    AUDIO_FEATURE_ENABLED_DTS_EAGLE=false \
    BOARD_USES_SRS_TRUEMEDIA=false \
    DTS_CODEC_M_=true \
    MM_AUDIO_ENABLED_SAFX=true \
    AUDIO_FEATURE_ENABLED_HW_ACCELERATED_EFFECTS=false \
    AUDIO_FEATURE_ENABLED_DS2_DOLBY_DAP=false \
    AUDIO_FEATURE_ENABLED_AUDIOSPHERE=true \
    DOLBY_ENABLE=false \
    AUDIO_FEATURE_ENABLED_EXT_HW_PLUGIN=true \
    AUDIO_FEATURE_ENABLED_AUTO_HAL=true \
"
CFLAGS += "\
    -I${STAGING_INCDIR}/audio-kernel/audio \
    -I${STAGING_KERNEL_BUILDDIR}/usr/include \
"
do_configure[depends] += "virtual/kernel:do_shared_workdir"

do_install_append() {
    install -m 0664 ${S}/configs/msmnile_au/mixer_paths_adp.xml -D ${D}${sysconfdir}/mixer_paths.xml
    install -m 0664 ${S}/configs/msmnile_au/audio_platform_info.xml -D ${D}${sysconfdir}/audio_platform_info.xml
}

FILES_${PN} += "${libdir}/audio.primary.default.so"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
