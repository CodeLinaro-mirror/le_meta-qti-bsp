SRC_URI = "git://github.com/GENIVI/${PN}.git;tag=${PN}-${PV};branch=genivi-excalibur;protocol=git \
           file://use-systemd-unit-dir.patch \
           file://fix-no-libsystemd-daemon.patch \
          "
