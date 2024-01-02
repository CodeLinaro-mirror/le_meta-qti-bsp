FILESEXTRAPATHS:append := "${THISDIR}/patches:"
FILESEXTRAPATHS:prepend := "${THISDIR}/tgvm_selinux_policies:"
FILESEXTRAPATHS:prepend := "${THISDIR}/tgvm_selinux_policies/common_policy/system:"
FILESEXTRAPATHS:prepend := "${THISDIR}/tgvm_selinux_policies/common_policy/kernel:"
FILESEXTRAPATHS:prepend := "${THISDIR}/tgvm_selinux_policies/common_policy/admin:"
FILESEXTRAPATHS:prepend := "${THISDIR}/tgvm_selinux_policies/common_policy/services:"
LICENSE += "& BSD-3-Clause-Clear"

SRC_URI:append := "\
    file://0001-policy-modules-system-logging-Fix-busybox-sh-wrapper.patch \
    file://0002-policy-modules-system-systemd-Drop-systemd_det.patch \
    file://0003-kernel-filesystem-Add-functionfs-rules.patch \
    file://0004-logging-recover-the-context-of-dev-log.patch \
    file://0005-refpolicy-update-booleans.patch \
    file://qcom_policy/ \
    file://filesystem.if \
    file://dmesg.te \
    file://systemd.te \
    file://logging.te \
    file://selinuxutil.te \
    file://netutils.te \
    file://ntp.te \
"

SRCREV = "${AUTOREV}"

# Modules outside of 'services' and 'apps' layer to be removed
PURGE_POLICY_MODULES += "cgmanager automount xguest amanda portage"
# Modules inside 'services' and 'apps' layers to keep. All others in these layers are turned off
CONTRIB_MODULES ?= "chronyd networkmanager shutdown gnome dhcp avahi apache minidlna mta dbus \
                    pulseaudio ppp dnsmasq ddclient bluetooth tinyproxy hostapd xserver nscd \
                    qti_ais_server vendor_bluetooth vendor_dbus vendor_hostapd vendor_networkmanager \
                    vendor_pulseaudio qti_qcarcam_rvc qti_qcarcam_test \
                    "

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
do_compile[prefuncs] += "install_policy"
#do_compile[postfuncs] += "update_modules_conf"

do_configure:append() {
    mkdir -p ${S}/policy/modules/qcom
    mkdir -p ${WORKDIR}/common_policy/kernel
    mkdir -p ${WORKDIR}/common_policy/admin
    mkdir -p ${WORKDIR}/common_policy/system
    mkdir -p ${WORKDIR}/common_policy/services

    cp -r ${WORKDIR}/qcom_policy/* ${S}/policy/modules/qcom/
    cp ${WORKDIR}/filesystem.if ${WORKDIR}/common_policy/kernel/
    cp ${WORKDIR}/dmesg.te ${WORKDIR}/common_policy/admin/
    cp ${WORKDIR}/netutils.te ${WORKDIR}/common_policy/admin/
    cp ${WORKDIR}/systemd.te ${WORKDIR}/common_policy/system/
    cp ${WORKDIR}/logging.te ${WORKDIR}/common_policy/system/
    cp ${WORKDIR}/selinuxutil.te ${WORKDIR}/common_policy/system/
    cp ${WORKDIR}/ntp.te ${WORKDIR}/common_policy/services/
}
