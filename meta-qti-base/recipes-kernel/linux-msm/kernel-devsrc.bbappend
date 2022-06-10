# kernel version < 5.8 does not need gcc-plugins libmpc-dev
RDEPENDS:${PN}:remove = "gcc-plugins libmpc-dev"

do_install:append() {
  rm -rf $kerneldir/build/scripts/basic/fixdep
  rm -rf $kerneldir/build/scripts/kconfig/conf
  rm -rf $kerneldir/build/scripts/kconfig/*.o
}
