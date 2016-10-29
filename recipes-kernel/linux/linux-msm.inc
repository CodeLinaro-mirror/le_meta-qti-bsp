DESCRIPTION = "Linux kernel"
SECTION     = "kernel"
LICENSE     = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

FILESEXTRAPATHS_prepend := "${WORKSPACE}/:${THISDIR}/linux-msm:"
SRC_URI   =  "file://kernel"
SRC_URI += " \
	https://raw.githubusercontent.com/saskencommunications/DRM-4.4-to-3.18-backport-patches/master/0001-drm-backport-from-v4.2-drm-next-pull-request.patch;name=0001 \
	https://raw.githubusercontent.com/saskencommunications/DRM-4.4-to-3.18-backport-patches/master/0002-drm-backport-from-Linux-4.2-rc3-msm-next.patch;name=0002 \
	file://0003-drm-Fixup-locking-WARNINGs-in-drm_mode_config_reset.patch \
	file://0004-drm-msm-change-kconfig-for-msm-3.18-kernel.patch \
	file://0005-drm-msm-hdmi-don-t-call-clk_round_rate-for-HDMI-driv.patch \
	file://0006-drm-msm-adjust-ocmem.h-path.patch \
	file://0007-drm-msm-fix-msm-bus-usage.patch \
	file://0008-drm-msm-rename-MSM-drm-platform-driver-s-name-into-m.patch \
	file://0009-drm-msm-Enable-clocks-wherever-accessing-register.patch \
	file://0010-drm-msm-Switch-to-qcom-KGSL-driver.patch \
	file://0011-drm-Force-drm_gem_prime_export-to-use-old-dma_buf-AP.patch \
	file://0012-drm-msm-Use-old-regulator_set_optimum_mode-API.patch \
	file://0013-drm-Fix-compilation-in-msm-3.18-kernel.patch \
	file://0014-clk-mdss-Keep-MSM-MDSS-clock-driver.patch \
	file://0015-drm-msm-use-CONFIG_MSM_BUS_SCALING-macro.patch \
	file://0016-drm-msm-disable-mdp4-driver-in-drm-kms.patch \
	file://0017-drm-msm-Update-generated-headers.patch \
	file://0018-drm-msm-hdmi-standardize-on-lead-chip-for-compatible.patch \
	file://0019-drm-msm-mdp5-remove-the-cfg-pointer-from-SMP-struct.patch \
	file://0020-drm-msm-mdp5-Disable-msm8996-hardware-translation-ta.patch \
	file://0021-drm-msm-Fix-IOMMU-clean-up-path-in-case-msm_iommu_ne.patch \
	file://0022-drm-msm-mdp5-Avoid-printing-error-messages-for-optio.patch \
	file://0023-drm-msm-mdp5-Vote-for-SMMU-power-when-performing-tra.patch \
	file://0024-drm-msm-hdmi-Add-basic-HDMI-support-for-msm8996.patch \
	file://0025-drm-msm-mdp-Update-generated-headers-for-Pixel-Exten.patch \
	file://0026-drm-msm-mdp5-Use-the-newly-introduced-enum-mdp_compo.patch \
	file://0027-drm-msm-mdp-Add-Software-Pixel-Extension-support.patch \
	file://0028-drm-msm-mdp5-Basic-support-for-MDP5-v1.7-MSM8996.patch \
	file://0029-drm-msm-add-GDSC-regulators-support-for-MSM8996.patch \
	file://0030-drm-msm-hdmi-Enable-HDMI-HPD-on-MTP8994.patch \
	file://0031-ARM-dts-qcom-Enable-MDSS-for-msm8996-on-MTP-board.patch \
	file://0032-drm-msm-mdp5-remove-wb-registers-until-it-is-availab.patch \
	file://0033-drm-kms-remove-compilation-of-hdcp_hdmi.patch \
	file://0034-drm-msm-add-msm_iounmap-to-unmap-io-region.patch \
	file://0035-break-kconfig-dependency-loop.patch \
	file://0036-ARM-dts-add-hdmi-support-for-drm-kms-in-8996-dragon-.patch \
	file://0037-drm-kms-change-interrupt-handling.patch \
	file://0038-drm-kms-change-to-use-mdp-smmu.patch \
	file://0039-drm-kms-update-atomic-commit-support.patch \
	file://0040-ARM-dts-support-drm-sde-in-mojave-board.patch \
	file://0041-defconfig-enable-drm-kms-driver.patch \
	file://0042-base-firmware-add-lib64-firmware-to-firmware-loading.patch \
	file://0043-gpu-drm-add-cursor-support-for-mdss-1.7.patch \
	file://0001-drm-Make-msm_drm.h-uapi-header-safe-for-C.patch \
	file://0002-drm-Make-drm.h-uapi-header-safe-for-C.patch \
	file://0003-drm-Make-uapi-headers-C89-pendantic-compliant.patch \
	file://0004-drm-msm-add-gpu-domain-in-smmu.patch \
	file://0005-drm-msm-move-GEM-back-buffer-from-SHMEM-to-DMA.patch \
	file://0006-drm-msm-fix-kernel-hang-issue-when-free-gem-obj.patch \
	file://0007-drm-msm-add-iommu-fault-handler-function.patch \
	file://0008-drm-msm-add-mechanism-to-check-status-per-draw.patch \
	file://0009-drm-msm-add-hang-dump-function.patch \
	file://0010-drm-msm-add-interface-to-query-gpu-address.patch \
	file://0011-drm-msm-Fix-issue-iommu-unmap-failed.patch \
	file://0100-ARM-dts-support-drm-render-in-mojave-dragonboard.patch \
	file://0101-drm-adreno-Move-letency-api-out-of-global-file.patch \
	file://0102-drm-adreno-Support-processor-commands-of-A5.patch \
	file://0103-drm-adreno-Support-resource-parsing-for-Adreno5.patch \
	file://0104-drm-adreno-Fix-submit-crashing-issue.patch \
	file://0105-drm-adreno-Enable-Adreno-5.patch \
	file://0106-drm-adreno-Fix-issue-of-zero-addrass-writing.patch \
	file://0107-drm-adreno-Fix-issue-of-wait-too-long.patch \
	file://0001-drm-msm-add-modifier-support-in-kms-driver.patch \
	file://0001-ARM-dts-msm-Add-support-for-msm8996-auto-mctm.patch \
	file://0002-defconfig-msm-Enable-CONFIG_MSM8996_MCTM-for-MCTM.patch \
"

SRC_URI[0001.md5sum] = "7fdc9ffea731349324901598d8e34b48"
SRC_URI[0001.sha256sum] = "c3b21de9b5bdab47b865dc4a9473a131148f7185c3dd1d9e9958f63fefe82c50"
SRC_URI[0002.md5sum] = "11300c533a6f57a3eda18f76b5375b79"
SRC_URI[0002.sha256sum] = "b61b28d0989d979d28356bc86d570c39fa3fa61fba3dcda485551990f5eccb0f"

S =  "${WORKDIR}/kernel"
