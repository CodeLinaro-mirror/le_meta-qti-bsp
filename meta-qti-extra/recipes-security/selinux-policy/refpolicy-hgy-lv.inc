FILESEXTRAPATHS:append := "${THISDIR}/files:"

SRC_URI += "${PATH_TO_REPO}/lv-sepolicy/.git;protocol=${PROTO};destsuffix=lv-sepolicy;usehead=1 \
"
SRCREV = "${AUTOREV}"

def copy_files_with_extensions(source_dir, target_dir, extensions):
    import shutil
    if not os.path.exists(target_dir):
        os.makedirs(target_dir)
    for root, _, files in os.walk(source_dir):
        for file in files:
            _, ext = os.path.splitext(file)
            if ext in extensions:
                src_file = os.path.join(root, file)
                shutil.copy(src_file, target_dir)
                print(f"Copied {src_file} to {target_dir}")

def generate_if_file(te_file_path):
    base_filename, _ = os.path.splitext(te_file_path)
    if_file_path = base_filename + ".if"
    if not os.path.exists(if_file_path):
        with open(if_file_path, "w") as if_file:
            if_file.write(f"## <summary> This is a blank interface file for compiling </summary>\n")

def generate_fc_file(te_file_path):
    base_filename, _ = os.path.splitext(te_file_path)
    fc_file_path = base_filename + ".fc"
    if not os.path.exists(fc_file_path):
        with open(fc_file_path, "w") as fc_file:
            fc_file.write(f"## <summary> This is a blank file_context file for compiling </summary>\n")

# Becase refpolicy requires a module to have an .if file
def normalize_module_format(src_dir):
    for root, _, files in os.walk(src_dir):
        for filename in files:
            if filename.endswith(".te"):
                te_file_path = os.path.join(root, filename)
                generate_if_file(te_file_path)
                generate_fc_file(te_file_path)

def install_device_policy(d, src_root):
    src_dir = os.path.join(d.getVar("WORKDIR"), src_root)
    dst_dir = os.path.join(d.getVar("S"), "policy", "modules", "hgy_lv")
    file_extensions = ['.te', '.if', '.fc']
    copy_files_with_extensions(src_dir, dst_dir, file_extensions)
    hgy_lv_metadata_path = os.path.join(dst_dir, "metadata.xml")
    with open(hgy_lv_metadata_path, 'w') as metadata:
        metadata.write(f"<summary>\n     Policy modules for hgy_lv\n</summary>\n")
    normalize_module_format(dst_dir)

# delete the line of including user_namespace is because no user_namespace class in selinux-policy
# detail is here:
# https://github.com/containers/container-selinux/blob/a8e389dbcd3f9b6ed0a7e495c6f559c0383dc49e/rpm/container-selinux.spec#L90
def apply_container_selinux(d, src_root):
    import subprocess
    src_dir = os.path.join(d.getVar("WORKDIR"), src_root)
    dst_dir = os.path.join(d.getVar("S"), "policy", "modules", "contrib")
    file_extensions = ['.te', '.if', '.fc']
    copy_files_with_extensions(src_dir, dst_dir, file_extensions)
    subprocess.run(["sed", "-i", "/user_namespace/d", os.path.join(dst_dir, "container.te")])

do_patch:append() {
    install_device_policy(d, "lv-sepolicy")
    apply_container_selinux(d, "container-selinux")
}

do_install:append() {
    rm -rf ${D}/${datadir}/selinux/${POLICY_NAME}/*.pp
}
