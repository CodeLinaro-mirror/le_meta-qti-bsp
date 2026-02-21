SUMMARY = "Dataeth drivers"
DESCRIPTION = "Build Dataeth drivers as kernel module"
HOMEPAGE = "https://git.codelinaro.org/"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${QTI_LICENSE_DIR}/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/data-eth/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/data-eth;usehead=1"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/data-eth"

EXT_MODULE = "vendor/qcom/opensource/data-eth"

TECHPACK_MODULE_OUT = "${WORKDIR}/dataeth-dlkm"
TECHPACK_MODULES = "${@bb.utils.contains("PREFERRED_VERSION_linux-msm", "6.12", "emac_ctrl_fe_virtio.ko emac_thin.ko", \
                    "drivers/emac_ctrl_fe/emac_ctrl_fe_virtio.ko drivers/emac_shim/emac_thin.ko", d)}"
TECHPACK_MAKE_ARGS = "\
          CONFIG_EMAC_SHIM=m \
          CONFIG_EMAC_CTRL_FE=m \
          "

inherit qti-techpack

RPROVIDES:${PN} += "kernel-module-emac-ctrl-fe-virtio-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-emac-thin-${KERNEL_VERSION}"

FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/*"
