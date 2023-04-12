SRC_URI = "${CLO_LE_GIT}/genivi/lifecycle/node-startup-controller;nobranch=1;protocol=${CLO_PROTOCOL};nobranch=1 \
           file://use-systemd-unit-dir.patch \
           file://fix-no-libsystemd-daemon.patch \
          "
