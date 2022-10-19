#Copyright (c) 2022 Qualcomm Innovation Center, Inc. All rights reserved.
#SPDX-License-Identifier: BSD-3-Clause-Clear

inherit deploy linux-kernel-base module-base

TECHPACK_MODULE_OUT ?= ""
TECHPACK_HEADERS ?= ""
TECHPACK_MODULES ?= ""
TECHPACK_DTBS ?= ""

do_compile() {
    TECHPACK_MODULE_SRC="${@os.path.relpath(d.getVar('S'), d.getVar('SRC_DIR_ROOT'))}"
    cd ${SRC_DIR_ROOT}/kernel/kernel-${PREFERRED_VERSION_linux-msm}/kernel_platform && \
    flock ../out/msm-kernel-${KERNEL_ARCH}-${KERNEL_VARIANT}defconfig -c \
    "EXT_MODULES=../../../${TECHPACK_MODULE_SRC} \
    BUILD_CONFIG=${KERNEL_BUILD_CONFIG} \
    VARIANT=${KERNEL_VARIANT}defconfig \
    OUT_DIR=../out/msm-kernel-${KERNEL_ARCH}-${KERNEL_VARIANT}defconfig/ \
    MODULE_OUT=${TECHPACK_MODULE_OUT} \
    KERNEL_UAPI_HEADERS_DIR=${STAGING_KERNEL_BUILDDIR} \
    INSTALL_MODULE_HEADERS=${TECHPACK_HEADERS} \
    ./build/build_module.sh"
}

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
