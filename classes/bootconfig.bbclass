# Bootconfig integration - runs as a do_makeboot prefunc.
# Builds the bootconfig host tool from the kernel source tree and applies
# recipes-kernel/bootconfig/boot_trace.conf to the ramdisk.
#
# Usage: inherit bootconfig

BOOTCONFIG_ENABLE ??= "1"

# Locate the layer that ships both this bbclass and boot_trace.conf.
# Override with an absolute path for a custom conf.
BOOTCONFIG_CONF_FILE ??= "${@next( \
    (c for f in d.getVar('BBINCLUDED').split() \
       if f.endswith('/bootconfig.bbclass') \
       for c in [os.path.join(os.path.dirname(os.path.dirname(f)), \
                              'recipes-kernel/bootconfig/boot_trace.conf')] \
       if os.path.exists(c)), \
    '')}"

python do_bootconfig_prepare() {
    import subprocess, shutil, os

    if d.getVar('BOOTCONFIG_ENABLE') != '1':
        bb.note("Bootconfig: disabled, skipping")
        return

    workdir = d.getVar('WORKDIR')

    kernel_ver = d.getVar('PREFERRED_VERSION_linux-msm')
    workspace  = d.getVar('WORKSPACE')
    if not kernel_ver or not workspace:
        bb.warn("Bootconfig: WORKSPACE or PREFERRED_VERSION_linux-msm not set — skipping")
        return

    kernel_tools   = os.path.join(workspace, 'kernel-' + kernel_ver,
                                  'kernel_platform/common/tools')
    build_dir      = os.path.join(workdir, 'bootconfig_native')
    bootconfig_bin = os.path.join(build_dir, 'bootconfig')

    if not os.path.exists(os.path.join(kernel_tools, 'bootconfig')):
        bb.warn("Bootconfig: source not found at %s/bootconfig — skipping" % kernel_tools)
        return
    # makedirs before make: Makefile.include checks OUTPUT dir exists.
    os.makedirs(build_dir, exist_ok=True)
    # Always rebuild; isolated env prevents cross-compiler vars leaking in;
    # explicit target skips the 'test' sub-target in the default 'all' rule.
    host_env = {'PATH': os.environ.get('PATH', '/usr/bin:/bin'),
                'HOME': os.environ.get('HOME', '/')}
    subprocess.check_call(
        ['make', '-C', 'bootconfig', 'CC=gcc',
         'OUTPUT=' + build_dir + '/', bootconfig_bin],
        cwd=kernel_tools, env=host_env
    )
    bb.note("Bootconfig: built tool at %s" % bootconfig_bin)

    conf_file = os.path.realpath(d.getVar('BOOTCONFIG_CONF_FILE') or '')
    if not conf_file or not os.path.exists(conf_file):
        bb.fatal("Bootconfig: conf file not found: %s" % conf_file)
    bb.note("Bootconfig: using conf %s" % conf_file)

    # Copy ramdisk to WORKDIR to avoid modifying the shared deploy artifact.
    ramdisk_orig = d.getVar('RAMDISK_PATH')
    if not ramdisk_orig or not os.path.exists(ramdisk_orig):
        bb.note("Bootconfig: no ramdisk at %s — skipping" % ramdisk_orig)
        return

    work_ramdisk = os.path.join(workdir, 'ramdisk_bootconfig')
    shutil.copy2(ramdisk_orig, work_ramdisk)

    # bootconfig -l exits 0 for both present/absent; non-zero means corrupt.
    check = subprocess.run([bootconfig_bin, '-l', work_ramdisk],
                           capture_output=True, text=True)
    if check.returncode != 0:
        bb.fatal("Bootconfig: 'bootconfig -l' failed (rc=%d): %s"
                 % (check.returncode, check.stderr.strip()))
    if check.stdout.strip():
        bb.warn("Bootconfig: payload already present, skipping apply:\n%s"
                % check.stdout.strip())
    else:
        subprocess.check_call([bootconfig_bin, '-a', conf_file, work_ramdisk])
        bb.note("Bootconfig: applied %s" % conf_file)

    d.setVar('RAMDISK_PATH', work_ramdisk)

    # 'bootconfig' keyword in cmdline tells the kernel to parse the appended block.
    params = d.getVar('KERNEL_CMD_PARAMS') or ''
    if 'bootconfig' not in params.split():
        d.setVar('KERNEL_CMD_PARAMS', params + ' bootconfig')
        bb.note("Bootconfig: injected 'bootconfig' keyword into KERNEL_CMD_PARAMS")
    else:
        bb.note("Bootconfig: 'bootconfig' keyword already present")
}

do_makeboot[prefuncs]       += "do_bootconfig_prepare"
# file-checksums: editing boot_trace.conf auto-triggers a rebuild.
do_makeboot[vardeps]        += "BOOTCONFIG_ENABLE BOOTCONFIG_CONF_FILE"
do_makeboot[file-checksums] += "${BOOTCONFIG_CONF_FILE}:True"
