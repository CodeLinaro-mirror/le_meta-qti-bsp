do_install_append() {
  rm -rf $kerneldir/build/scripts/basic/fixdep
  rm -rf $kerneldir/build/scripts/kconfig/conf
  rm -rf $kerneldir/build/scripts/kconfig/*.o
}

