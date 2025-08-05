SRC_URI = "${CLO_LE_GIT}/genivi/lifecycle/node-startup-controller;nobranch=1;protocol=${CLO_PROTOCOL};name=nsc \
           file://use-systemd-unit-dir.patch \
           file://fix-no-libsystemd-daemon.patch \
          "
SRCREV_nsc = "717e743c84ef9c168501dcbc012c4212f1903581"
