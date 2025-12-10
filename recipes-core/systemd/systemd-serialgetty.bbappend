do_install:append() {

   if ${@bb.utils.contains('MACHINE_FEATURES', 'qti-vm', 'true', 'false', d)}; then
      sed -i 's|ExecStart=-/sbin/agetty -8 -L %I 115200 \$TERM|ExecStart=-/sbin/agetty --autologin root -8 -L %I 115200 \$TERM|' ${D}/usr/lib/systemd/system/serial-getty@.service
   fi
}
