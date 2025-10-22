# kernel version < 5.8 does not need gcc-plugins libmpc-dev
RDEPENDS:${PN}:remove = "gcc-plugins libmpc-dev"
# Fix error: nothing provides /bin/sed needed by kernel-devsrc-1.0-r0
# scripts/mksysmap depends on sed
RDEPENDS:${PN}:append = " sed"

do_install:append() {
  rm -rf $kerneldir/build/scripts/basic/fixdep
  rm -rf $kerneldir/build/scripts/kconfig/conf
  rm -rf $kerneldir/build/scripts/kconfig/*.o
}
