
# Move the audio drivers into folder extra
do_install_append() {
    install -d ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra
    for i in $(find ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/kernel/techpack/audio/ -name "*.ko"); do
        mv ${i} ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/
    done
    rm -fr ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/kernel/techpack
}

do_module_signing() {
    for i in $(find ${PKGDEST}/kernel-module*/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/ -name "*.ko"); do
	echo ${i}
        ${STAGING_KERNEL_BUILDDIR}/scripts/sign-file sha512 ${STAGING_KERNEL_BUILDDIR}/certs/signing_key.pem ${STAGING_KERNEL_BUILDDIR}/certs/signing_key.x509 ${i}
    done
}

addtask do_module_signing after do_package before do_package_write_ipk

