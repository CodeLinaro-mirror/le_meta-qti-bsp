DEPENDS += "sectool5-native openssl-native"
PR = "r1signed"

SIGNING_TOOLS_DIR = "${TMPDIR}/work-shared/signing_tools"
SOC_HW_VERSION = "0x60020000"
SOC_VERS = "0x6002"
SIGNING_VERSION = "_eccv3"

EXTRA_OEMAKE:append = " SIGNED_KERNEL=1"
EXTRA_OEMAKE:append = " USER_BUILD_VARIANT=0"
EXTRA_OEMAKE:append = " DEVICE_STATUS=DEFAULT_UNLOCK=true"
EXTRA_OEMAKE:append = " TARGET_ARCHITECTURE=${T_ARCH}"

do_install[depends] += "sectool5-native:do_unpack"
# This install overrides the one in base recipe. In this we perform image signing
# using the tools located in signing_tools_dir.
#
do_install() {
    install -d ${D}/boot
    # This performs signing the image at lk/build folder. The final image
    # gets written in the folder image/boot/. Subsequently deploy stage in the
    # main recipe installs to the DEPLOYDIR where rest of the system images are located.
    #
    SECIMAGE_LOCAL_DIR=${SIGNING_TOOLS_DIR}/SecImage \
    USES_SEC_POLICY_MULTIPLE_DEFAULT_SIGN=1 \
    USES_SEC_POLICY_INTEGRITY_CHECK=1 \
    python ${SIGNING_TOOLS_DIR}/SecImage/sectools_builder.py \
        -i ${S}/../abl.elf \
        -t ${D}/boot \
        -g abl \
        --soc_hw_version ${SOC_HW_VERSION} \
        --soc_vers ${SOC_VERS} \
        --config=${SIGNING_TOOLS_DIR}/SecImage/config/integration/secimage${SIGNING_VERSION}.xml \
        --install_base_dir=${D}/boot \
            > ${S}/secimage.log 2>&1
    install -m 0644 ${D}/boot/abl.elf ${S}
}

FILES:${PN} = "/boot"

T_ARCH = "arm"
T_ARCH:aarch64 = "aarch64"
