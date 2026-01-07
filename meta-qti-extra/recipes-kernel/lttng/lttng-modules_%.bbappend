FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " \
    file://0001-lttng-module-fix-lttng-module-compile-issue.patch \
    file://0002-lttng-modules-fix-compile-issue-for-msm-kernel-6-1.patch \
    file://0001-Fix-the-error-of-incompatible-pointer-types.patch \
    https://git.codelinaro.org/clo/yocto-mirrors/poky/-/raw/86705a293ce4f2106381cee339d291d50fca90ac/meta/recipes-kernel/lttng/lttng-modules/0001-fix-close_on_exec-pass-files_struct-instead-of-fdtab.patch;name=patch1 \
    https://git.codelinaro.org/clo/yocto-mirrors/poky/-/raw/86705a293ce4f2106381cee339d291d50fca90ac/meta/recipes-kernel/lttng/lttng-modules/0003-fix-net-udp-add-IP-port-data-to-the-tracepoint-udp-u.patch;name=patch2 \
    https://git.codelinaro.org/clo/yocto-mirrors/poky/-/raw/5f273feeba21661fd8038ad58c96f4030fae4d96/meta/recipes-kernel/lttng/lttng-modules/0001-Fix-kfree_skb-changed-in-6.11-rc1.patch;name=patch3 \
    https://git.codelinaro.org/clo/yocto-mirrors/poky/-/raw/5f273feeba21661fd8038ad58c96f4030fae4d96/meta/recipes-kernel/lttng/lttng-modules/0002-Fix-ext4_da_reserve_space-changed-in-6.11-rc1.patch;name=patch4 \
    https://git.codelinaro.org/clo/yocto-mirrors/poky/-/raw/5f273feeba21661fd8038ad58c96f4030fae4d96/meta/recipes-kernel/lttng/lttng-modules/0006-Fix-scsi-sd-Atomic-write-support-added-in-6.11-rc1.patch;name=patch5 \
    https://git.codelinaro.org/clo/yocto-mirrors/poky/-/raw/b1cfc7fbc22764bf3a3335c83f30f48c943b1c6c/meta/recipes-kernel/lttng/lttng-modules/0001-Fix-uprobes-make-uprobe_register-return-struct-uprob.patch;name=patch6 \
    https://git.codelinaro.org/clo/yocto-mirrors/poky/-/raw/b1cfc7fbc22764bf3a3335c83f30f48c943b1c6c/meta/recipes-kernel/lttng/lttng-modules/0001-fix-writeback-Refine-the-show_inode_state-macro-defi.patch;name=patch7 \
"
SRC_URI[patch1.sha256sum] = "05c5d579041f6cecf6a3b00287c8bb98d6a2b3aab4a44c9f64738fbddd32df2b"
SRC_URI[patch2.sha256sum] = "1a69eeac5f5a98ed25508472a7d1e88bcefc29c4dcb911c5efcf09c55c3e35b3"
SRC_URI[patch3.sha256sum] = "c95aafc9670b6e9a83edd86eca8c61c0e5a1ed57f8a3d4cc3117247ae6140b5e"
SRC_URI[patch4.sha256sum] = "53dc5a5e49c251b9f10cdf3a6d22903ff34e101ef98cf7392da01586b0ca98bb"
SRC_URI[patch5.sha256sum] = "0e59a87baa8296e10d49e5a5daab967d3ac4c8b795ce528aac90aa87e76f6039"
SRC_URI[patch6.sha256sum] = "cdb02474bc9f955c31290e5173684569ca9ebfd91f0bbf498c11fcab302b1885"
SRC_URI[patch7.sha256sum] = "d385f40834aa369b76eee7aa82eccc03d7380911fef36861f82ba0de0473a6af"

inherit qti-kernel-arch-clang

# lock to avoid parallel compiling with techpack
do_compile[lockfiles] += "${TMPDIR}/qti-techpack.lock"

FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/kernel/${PN}/modules.order*"

do_compile:prepend(){
    export KCFLAGS="-Wno-cast-function-type-strict"
}
