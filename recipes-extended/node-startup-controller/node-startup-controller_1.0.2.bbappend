SRC_URI = "git://git.projects.genivi.org/lifecycle/${PN}.git;tag=${PN}-${PV};branch=genivi-excalibur;protocol=http \
           file://use-systemd-unit-dir.patch \
           file://fix-no-libsystemd-daemon.patch \
          "