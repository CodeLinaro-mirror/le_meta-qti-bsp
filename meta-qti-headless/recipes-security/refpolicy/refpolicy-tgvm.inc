FILESEXTRAPATHS:append := "${THISDIR}/patches:"
FILESEXTRAPATHS:prepend := "${THISDIR}/tgvm_selinux_policies:"
FILESEXTRAPATHS:prepend := "${THISDIR}/tgvm_selinux_policies/common_policy:"
LICENSE += "& BSD-3-Clause-Clear"

SRC_URI:append := "\
    file://0001-policy-modules-system-logging-Fix-busybox-sh-wrapper.patch \
    file://0004-logging-recover-the-context-of-dev-log.patch \
    file://0005-refpolicy-update-booleans.patch \
    file://0006-init-Add-support-for-initrc_t-to-nnp-domtrans-to-the.patch \
    file://qcom_policy/ \
    file://system/;subdir=common_policy \
    file://kernel/;subdir=common_policy \
    file://admin/;subdir=common_policy \
    file://services/;subdir=common_policy \
    file://apps/;subdir=common_policy \
"

SRCREV = "${AUTOREV}"

def append_policy_file(src_path, dst_path):
    # Append our policy fragment to the end of the upstream file
    with open(src_path, 'r') as src_file:
        with open(dst_path, 'a') as dst_file:
            dst_file.write('\n')
            for line in src_file.readlines():
                if not line.startswith('#'):
                    dst_file.write(line)
    os.remove(src_path)

def copy_policy_file(src_path, dst_path):
    # Copy the new file into the sepolicy compilation directory
    import shutil
    shutil.copy(src_path, dst_path)

def install_device_policy(d, src_root):
    src_dir = os.path.join(d.getVar("WORKDIR"), src_root)
    dst_dir = os.path.join(d.getVar("S"), "policy", "modules")
    for root, dirs, files in os.walk(src_dir):
        for file in files:
            policy_file = os.path.relpath(os.path.join(root, file), src_dir)
            dst_path = os.path.join(dst_dir, policy_file)
            if os.path.exists(dst_path):
                append_policy_file(os.path.join(src_dir, policy_file), dst_path)
            else:
                copy_policy_file(os.path.join(src_dir, policy_file), dst_path)

python install_policy() {
    install_device_policy(d, "common_policy")
}

do_compile[prefuncs] += "install_policy"

do_configure:append() {
    mkdir -p ${S}/policy/modules/qcom

    cp -r ${WORKDIR}/qcom_policy/* ${S}/policy/modules/qcom/
}
