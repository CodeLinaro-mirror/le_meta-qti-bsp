SRC_URI = "${CLO_LE_GIT}/genivi/lifecycle/node-startup-controller;nobranch=1;protocol=https;branch=caf_migration/genivi/node-startup-controller/master \
           file://use-systemd-unit-dir.patch \
           file://fix-no-libsystemd-daemon.patch \
          "
