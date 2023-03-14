SUMMARY = "Initramfs image for early-ramdisk-init"
LICENSE = "BSD-3-Clause-Clear"

IMAGE_CLASSES:remove = "qimage qimage-boot"

inherit image

DEPENDS += "mkbootimg-native virtual/kernel"

IMAGE_FSTYPES = "cpio"

# avoid circular dependencies
EXTRA_IMAGEDEPENDS = ""
KERNELDEPMODDEPEND = ""

KERNEL_VERSION = "${@oe.utils.read_file('${STAGING_KERNEL_BUILDDIR}/kernel-abiversion')}"

# We really need just kexecboot, kexec and ubiattach
IMAGE_INSTALL = "\
                 early-ramdisk-init libgcc kmod \
                 kernel-module-spidev-${KERNEL_VERSION} \
                 kernel-module-spi-msm-geni-${KERNEL_VERSION} \
                 kernel-module-qcom-smd-${KERNEL_VERSION} \
                 kernel-module-rproc-qcom-common-${KERNEL_VERSION} \
                 kernel-module-qcom-ramdump-${KERNEL_VERSION} \
                 kernel-module-ns-${KERNEL_VERSION} \
                 kernel-module-qrtr-${KERNEL_VERSION} \
                 kernel-module-qmi-helpers-${KERNEL_VERSION} \
                 kernel-module-qcom-sysmon-${KERNEL_VERSION} \
                 kernel-module-qcom-q6v5-${KERNEL_VERSION} \
                 kernel-module-qcom-pil-info-${KERNEL_VERSION} \
                 kernel-module-mdt-loader-${KERNEL_VERSION} \
                 kernel-module-smp2p-${KERNEL_VERSION} \
                 kernel-module-qcom-q6v5-pas-${KERNEL_VERSION} \
                 kernel-module-arm-smmu-${KERNEL_VERSION} \
                 kernel-module-cmd-db-${KERNEL_VERSION} \
                 kernel-module-iommu-logger-${KERNEL_VERSION} \
                 kernel-module-llcc-qcom-${KERNEL_VERSION} \
                 kernel-module-mem-buf-${KERNEL_VERSION} \
                 kernel-module-minidump-${KERNEL_VERSION} \
                 kernel-module-msm-qmp-${KERNEL_VERSION} \
                 kernel-module-msm-dma-iommu-mapping-${KERNEL_VERSION} \
                 kernel-module-ns-${KERNEL_VERSION} \
                 kernel-module-pinctrl-slpi-${KERNEL_VERSION} \
                 kernel-module-pinctrl-sm8150-${KERNEL_VERSION} \
                 kernel-module-pinctrl-sdmshrike-${KERNEL_VERSION} \
                 kernel-module-pinctrl-spmi-gpio-${KERNEL_VERSION} \
                 kernel-module-qcom-dload-mode-${KERNEL_VERSION} \
                 kernel-module-qcom-aoss-${KERNEL_VERSION} \
                 kernel-module-qcom-hwspinlock-${KERNEL_VERSION} \
                 kernel-module-qcom-pdc-${KERNEL_VERSION} \
                 kernel-module-qcom-scm-${KERNEL_VERSION} \
                 kernel-module-qcom-cpu-vendor-hooks-${KERNEL_VERSION} \
                 kernel-module-qcom-dma-heaps-${KERNEL_VERSION} \
                 kernel-module-qcom-rpmh-${KERNEL_VERSION} \
                 kernel-module-qcom-wdt-core-${KERNEL_VERSION} \
                 kernel-module-qcom-soc-wdt-${KERNEL_VERSION} \
                 kernel-module-qcom-iommu-util-${KERNEL_VERSION} \
                 kernel-module-qrtr-${KERNEL_VERSION} \
                 kernel-module-secure-buffer-${KERNEL_VERSION} \
                 kernel-module-smem-${KERNEL_VERSION} \
                 kernel-module-proxy-consumer-${KERNEL_VERSION} \
                 kernel-module-debug-regulator-${KERNEL_VERSION} \
                 kernel-module-rpmh-regulator-${KERNEL_VERSION} \
                 kernel-module-qti-fixed-regulator-${KERNEL_VERSION} \
                 kernel-module-regmap-spmi-${KERNEL_VERSION} \
                 kernel-module-spmi-pmic-arb-${KERNEL_VERSION} \
                 kernel-module-qcom-spmi-pmic-${KERNEL_VERSION} \
                 kernel-module-reboot-mode-${KERNEL_VERSION} \
                 kernel-module-qcom-pon-${KERNEL_VERSION} \
                 kernel-module-nvmem-qcom-spmi-sdam-${KERNEL_VERSION} \
                 kernel-module-clk-rpmh-${KERNEL_VERSION} \
                 kernel-module-clk-qcom-${KERNEL_VERSION} \
                 kernel-module-clk-dummy-${KERNEL_VERSION} \
                 kernel-module-gdsc-regulator-${KERNEL_VERSION} \
                 kernel-module-gcc-sm8150-${KERNEL_VERSION} \
                 kernel-module-gcc-sc8180x-${KERNEL_VERSION} \
                 kernel-module-camcc-sc8180x-${KERNEL_VERSION} \
                 kernel-module-camcc-sm8150-${KERNEL_VERSION} \
                 kernel-module-gpucc-sm8150-${KERNEL_VERSION} \
                 kernel-module-dispcc-sm8250-${KERNEL_VERSION} \
                 kernel-module-icc-bcm-voter-${KERNEL_VERSION} \
                 kernel-module-icc-rpmh-${KERNEL_VERSION} \
                 kernel-module-qnoc-qos-${KERNEL_VERSION} \
                 kernel-module-qnoc-sm8150-${KERNEL_VERSION} \
                 kernel-module-qnoc-sc8180x-${KERNEL_VERSION} \
                 kernel-module-icc-debug-${KERNEL_VERSION} \
                 kernel-module-qcom-cpufreq-hw-${KERNEL_VERSION} \
                 kernel-module-socinfo-${KERNEL_VERSION} \
                 kernel-module-qcom-ipc-logging-${KERNEL_VERSION} \
                 kernel-module-sg-${KERNEL_VERSION} \
                 kernel-module-phy-qcom-ufs-${KERNEL_VERSION} \
                 kernel-module-phy-qcom-ufs-qmp-v4-${KERNEL_VERSION} \
                 kernel-module-ufs-qcom-${KERNEL_VERSION} \
                 kernel-module-crypto-qti-common-${KERNEL_VERSION} \
                 kernel-module-ufshcd-crypto-qti-${KERNEL_VERSION} \
                 kernel-module-crypto-qti-tz-${KERNEL_VERSION} \
                 kernel-module-qcom-logbuf-vh-${KERNEL_VERSION} \
                 kernel-module-soc-sleep-stats-${KERNEL_VERSION} \
                 kernel-module-boot-stats-${KERNEL_VERSION} \
                 kernel-module-pcs-xpcs-${KERNEL_VERSION} \
                 kernel-module-dcvs-fp-${KERNEL_VERSION} \
                 kernel-module-qcom-dcvs-${KERNEL_VERSION} \
                 kernel-module-mdt-loader-${KERNEL_VERSION} \
                 kernel-module-nvmem-qfprom-${KERNEL_VERSION} \
                 graphicsdlkm \
                 kernel-module-i2c-msm-geni-${KERNEL_VERSION} \
                 kernel-module-pinctrl-sx150x-${KERNEL_VERSION} \
                 kernel-module-i2c-mux-${KERNEL_VERSION} \
                 kernel-module-i2c-mux-pca954x-${KERNEL_VERSION} \
                 kernel-module-anx7625-${KERNEL_VERSION} \
                 kernel-module-qseecom-proxy-${KERNEL_VERSION} \
                 kernel-module-qcom-apcs-ipc-mailbox-${KERNEL_VERSION} \
                 securemsmdlkm \
                 displaydlkm \
                 kernel-module-pcs-xpcs-${KERNEL_VERSION} \
                 kernel-module-micrel-${KERNEL_VERSION} \
                 kernel-module-stmmac-platform-${KERNEL_VERSION} \
                 kernel-module-dwmac-qcom-eth-${KERNEL_VERSION} \
                 kernel-module-videocc-sm8150-${KERNEL_VERSION} \
                 kernel-module-mdt-loader-${KERNEL_VERSION} \
                 videodlkm \
                 kernel-module-stub-regulator-${KERNEL_VERSION} \
                 cameradlkm \
"

# Do not pollute the initrd image with rootfs features
IMAGE_FEATURES = ""

IMAGE_LINGUAS = ""

FEED_DEPLOYDIR_BASE_URI = ""
LDCONFIGDEPEND = ""
IMAGE_ROOTFS_EXTRA_SPACE = "0"

# disable runtime dependency on run-postinsts -> update-rc.d
ROOTFS_BOOTSTRAP_INSTALL = ""

