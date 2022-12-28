#Copyright (c) 2022 Qualcomm Innovation Center, Inc. All rights reserved.
#SPDX-License-Identifier: BSD-3-Clause-Clear

DEPENDS += "dtc-native virtual/kernel kernel-toolchain-native rsync-native"

inherit deploy kernel-arch linux-kernel-base qti-kernel-toolchain

TECHPACK_MODULE_OUT ?= ""
TECHPACK_HEADERS ?= ""
TECHPACK_MODULES ?= ""
TECHPACK_DTBS ?= ""
TECHPACK_MAKE_ARGS ?= ""

KERNEL_VERSION = "${@oe.utils.read_file('${STAGING_KERNEL_BUILDDIR}/kernel-abiversion')}"

do_build_tools() {
    cd ${B}
    OUT_DIR=msm-kernel-${KERNEL_ARCH}-${KERNEL_VARIANT}defconfig
    ln -sf ${KERNEL_TOOLCHAIN_DIR}/prebuilts prebuilts

    rsync -a --include=arch/ --include=arch/${ARCH}/*** \
    --include=build.config* --include=*.*h \
    --include=drivers/ --include=drivers/clk/ \
    --include=drivers/clk/qcom/ \
    --include=drivers/devfreq/ \
    --include=drivers/iommu/ \
    --include=drivers/pinctrl/ \
    --include=include/*** \
    --include=Kconfig* \
    --include=Makefile* \
    --include=mm/ \
    --include=scripts/*** \
    --include=sound/ \
    --exclude=* ${STAGING_KERNEL_DIR}/ msm-kernel

    cp -R ${KERNEL_TOOLCHAIN_DIR}/build build

    install -d ${OUT_DIR}
    cp -R ${STAGING_KERNEL_BUILDDIR} ${OUT_DIR}/msm-kernel
}
do_build_tools[dirs] = "${WORKDIR}/build"
B = "${WORKDIR}/build"

addtask do_build_tools after do_prepare_recipe_sysroot before do_configure

do_compile() {
    cd ${B}
    TECHPACK_MODULE_SRC="${@os.path.relpath(d.getVar('S'), d.getVar('B'))}"
    KERNEL_DIR=msm-kernel \
    EXT_MODULES=${TECHPACK_MODULE_SRC} \
    BUILD_CONFIG=${KERNEL_BUILD_CONFIG} \
    VARIANT=${KERNEL_VARIANT}defconfig \
    OUT_DIR=msm-kernel-${KERNEL_ARCH}-${KERNEL_VARIANT}defconfig/ \
    MODULE_OUT=${TECHPACK_MODULE_OUT} \
    KERNEL_UAPI_HEADERS_DIR=${OUT_DIR}msm-kernel/kernel-build-artifacts \
    INSTALL_MODULE_HEADERS=${TECHPACK_HEADERS} \
    ./build/build_module.sh  ${TECHPACK_MAKE_ARGS}
}
do_compile[dirs] = "${WORKDIR}/build"

do_install() {
    # install modules
    if [ -n "${TECHPACK_MODULES}" ]; then
        install -d ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}
        install -d ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra

        for mod in ${TECHPACK_MODULES}; do
            if [ -f ${TECHPACK_MODULE_OUT}/$mod ]; then
                install -m 0644 ${TECHPACK_MODULE_OUT}/$mod ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra
            fi
        done
    fi

    # install headers
    if [ -n "${TECHPACK_HEADERS}" ]; then
        install -d ${D}/usr/include/linux-msm

        if [ -d ${TECHPACK_MODULE_OUT}/usr/include ]; then
            cp -r ${TECHPACK_MODULE_OUT}/usr/include/* ${D}/usr/include/linux-msm
        fi
    fi
}

do_deploy() {
    if [ -n "${TECHPACK_DTBS}" ]; then
        install -d ${DEPLOYDIR}/build-artifacts/techpack-dtbos

        for dtb in ${TECHPACK_DTBS}; do
            if [ -f ${TECHPACK_MODULE_OUT}/$dtb ]; then
                install -m 0644 ${TECHPACK_MODULE_OUT}/$dtb ${DEPLOYDIR}/build-artifacts/techpack-dtbos/
            fi
        done
    fi
}

addtask do_deploy after do_install
