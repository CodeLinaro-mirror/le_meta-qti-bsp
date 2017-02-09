SRC_URI = "git://github.com/GENIVI/${BPN}.git;protocol=git \
           file://nsm-fix-systemd-service-dep.patch \
           file://nsm-fix-no-libsystemd-daemon.patch \
           file://link-with-gio.patch \
          "