SRC_URI = "${CLO_LE_GIT}/genivi/lifecycle/node-health-monitor;protocol=${CLO_PROTOCOL};nobranch=1;name=nhm \
           file://fix-no-libsystemd-daemon.patch \
           file://0001-change-service-name.patch \
          "
