
python do_getpatches() {
    import os
    cmd = "wget https://source.codeaurora.org/quic/la/platform/external/kmod/patch/?id=4c30a11d5fa84ebfdd3a8f05fa4ba1c16c074f43 -O 0001_depmod.patch"
    os.system(cmd)
    os.rename("0001_depmod.patch", "../0001_depmod.patch")
}

python do_applypatches() {
    import os
    os.chdir(d.getVar("S", "not found"))
    cmd = "git apply ../0001_depmod.patch"
    os.system(cmd)
}

addtask getpatches before do_fetch
addtask applypatches after do_patch
addtask applypatches before do_configure
