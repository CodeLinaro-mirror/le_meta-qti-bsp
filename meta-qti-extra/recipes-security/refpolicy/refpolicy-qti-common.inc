FILESEXTRAPATHS:append := "${THISDIR}/files:"
LICENSE += "& BSD-3-Clause-Clear"

SRC_URI += "${PATH_TO_REPO}/lv-sepolicy/.git;protocol=${PROTO};destsuffix=lv-sepolicy;usehead=1 \
            file://Update-busybox-substitution-paths.patch \
            file://0001-fc-subs-persist-set-aliases-for-var-and-data.patch \
            file://0001-refpolicy-update-booleans.patch \
            file://Move-root-user-to-unconfined-context.patch \
"
SRCREV = "${AUTOREV}"

# Modules outside of 'services' and 'apps' layer to be removed
PURGE_POLICY_MODULES += "cgmanager automount xguest amanda portage"
# Modules inside 'services' and 'apps' layers to keep. All others in these layers are turned off
CONTRIB_MODULES ?= "chronyd networkmanager shutdown gnome dhcp avahi apache minidlna mta dbus \
                    pulseaudio ppp dnsmasq ddclient bluetooth tinyproxy hostapd xserver nscd \
                    ais_server qcarcam_test qcarcam_rvc"

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
    os.renames(src_path, dst_path)

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

do_patch:append() {
    install_device_policy(d, "lv-sepolicy")
}

python update_modules_conf () {
    contrib_modules = d.getVar("CONTRIB_MODULES").split()
    module_conf_name = os.path.join(d.getVar("S"), "policy", "modules.conf")
    new_file = ""

    with open(module_conf_name, 'r') as modules_file:
        layer = None
        for line in modules_file.readlines():
            if line.startswith('# Layer'):
                layer = line.split(':')[-1].strip()
            elif '=' in line:
                module_name = line.split('=')[0].strip()
                if (layer == "services" or layer == "apps") and module_name not in contrib_modules:
                    new_file += module_name + ' = off\n'
                    continue
            new_file += line

    # Save the updated modules.conf
    with open(module_conf_name, 'w') as modules_file:
        modules_file.write(new_file)
}
do_compile[postfuncs] += "update_modules_conf"

do_install:append() {
    rm -rf ${D}/${datadir}/selinux/${POLICY_NAME}/*.pp
}
