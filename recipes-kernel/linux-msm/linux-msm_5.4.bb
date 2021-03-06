require recipes-kernel/linux-msm/linux-msm.inc
COMPATIBLE_MACHINE = "genericarmv8|sdxlemur"

SRC_DIR   =  "${WORKSPACE}/kernel/msm-5.4"
S         =  "${WORKDIR}/kernel/msm-5.4"
PR = "r0"

DEPENDS += "llvm-arm-toolchain-native dtc-native rsync-native"

LDFLAGS_aarch64 = "-O1 --hash-style=gnu --as-needed"
TARGET_CXXFLAGS += "-Wno-format"
KERNEL_CC = "${STAGING_BINDIR_NATIVE}/llvm-arm-toolchain/bin/clang -target ${TARGET_ARCH}${TARGET_VENDOR}-${TARGET_OS}"

LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"
DYNAMIC_DEFCONFIG_SUPPORT = "sdxlemur"

do_configure_prepend() {
        if ${@bb.utils.contains('DYNAMIC_DEFCONFIG_SUPPORT', '${MACHINE}', 'true', 'false', d)}; then
                ARCH=${ARCH} CROSS_COMPILE=${CROSS_COMPILE} REAL_CC=${STAGING_BINDIR_NATIVE}/llvm-arm-toolchain/bin/clang \
                LD=arm-oe-linux-gnueabi-ld KERN_OUT=${STAGING_KERNEL_BUILDDIR} \
                ${STAGING_KERNEL_DIR}/scripts/gki/generate_defconfig.sh ${KERNEL_CONFIG}
        fi
}

do_install (){
	#
	# First install the modules
	#
	unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS MACHINE
	if (grep -q -i -e '^CONFIG_MODULES=y$' .config); then
                # Strip the debug symbols in modules before signing for VMs.
		if ${@bb.utils.contains('MACHINE_FEATURES', 'qti-vm', 'true', 'false', d)}; then
                      oe_runmake DEPMOD=echo INSTALL_MOD_STRIP=1 MODLIB=${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION} INSTALL_FW_PATH=${D}${nonarch_base_libdir}/firmware modules_install
                else
                      oe_runmake DEPMOD=echo MODLIB=${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION} INSTALL_FW_PATH=${D}${nonarch_base_libdir}/firmware modules_install
                fi
		rm "${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/build"
		rm "${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/source"
		# If the kernel/ directory is empty remove it to prevent QA issues
		rmdir --ignore-fail-on-non-empty "${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/kernel"
	else
		bbnote "no modules to install"
	fi

	#
	# Install various kernel output (zImage, map file, config, module support files)
	#
	install -d ${D}/${KERNEL_IMAGEDEST}
	install -d ${D}/boot
	for imageType in ${KERNEL_IMAGETYPES} ; do
		install -m 0644 ${KERNEL_OUTPUT_DIR}/${imageType} ${D}/${KERNEL_IMAGEDEST}/${imageType}-${KERNEL_VERSION}
		if [ "${KERNEL_PACKAGE_NAME}" = "kernel" ]; then
			ln -sf ${imageType}-${KERNEL_VERSION} ${D}/${KERNEL_IMAGEDEST}/${imageType}
		fi
	done
	install -m 0644 System.map ${D}/boot/System.map-${KERNEL_VERSION}
	install -m 0644 .config ${D}/boot/config-${KERNEL_VERSION}
	install -m 0644 vmlinux ${D}/boot/vmlinux-${KERNEL_VERSION}
	[ -e Module.symvers ] && install -m 0644 Module.symvers ${D}/boot/Module.symvers-${KERNEL_VERSION}
	install -d ${D}${sysconfdir}/modules-load.d
	install -d ${D}${sysconfdir}/modprobe.d
}

do_shared_workdir_append () {
        cp Makefile $kerneldir/
        cp -fR usr $kerneldir/

        cp include/config/auto.conf $kerneldir/include/config/auto.conf

        if [ -d arch/${ARCH}/include ]; then
                mkdir -p $kerneldir/arch/${ARCH}/include/
                cp -fR arch/${ARCH}/include/* $kerneldir/arch/${ARCH}/include/
        fi

        if [ -d arch/${ARCH}/boot ]; then
                mkdir -p $kerneldir/arch/${ARCH}/boot/
                cp -fR arch/${ARCH}/boot/* $kerneldir/arch/${ARCH}/boot/
        fi

        mkdir -p $kerneldir/scripts

        cp ${STAGING_KERNEL_DIR}/usr/gen_initramfs_list.sh $kerneldir/scripts/

        # Generate kernel headers
        oe_runmake_call -C ${STAGING_KERNEL_DIR} ARCH=${ARCH} CC="${KERNEL_CC}" LD="${KERNEL_LD}" headers_install O=${STAGING_KERNEL_BUILDDIR}
}

do_deploy_append () {
         # Copy vmlinux and zImage into deplydir for boot.img creation
         install -d ${DEPLOYDIR}/build-artifacts
         install -d ${DEPLOYDIR}/build-artifacts/kernel_scripts/scripts
         install -d ${DEPLOYDIR}/build-artifacts/dtb
         cp  ${STAGING_KERNEL_DIR}/usr/gen_initramfs_list.sh ${DEPLOYDIR}/build-artifacts/kernel_scripts/scripts
         cp -a ${STAGING_KERNEL_BUILDDIR}/usr/ ${DEPLOYDIR}/build-artifacts/kernel_scripts/
         cp -a ${STAGING_KERNEL_BUILDDIR}/arch/${ARCH}/boot/dts/vendor/qcom/*.dtb ${DEPLOYDIR}/build-artifacts/dtb
         install -m 0644 ${KERNEL_OUTPUT_DIR}/${KERNEL_IMAGETYPE} ${DEPLOYDIR}/${KERNEL_IMAGETYPE}
         install -m 0644 vmlinux ${DEPLOYDIR}
         install -m 0644 System.map ${DEPLOYDIR}

         if ${@bb.utils.contains('MACHINE_FEATURES', 'qti-vm', 'true', 'false', d)}; then
                 mkdtimg create ${DEPLOYDIR}/${DTB_TARGET} --page_size=${PAGE_SIZE} ${DEPLOYDIR}/build-artifacts/dtb/*.dtb
         fi
}
