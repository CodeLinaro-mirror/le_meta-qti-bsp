SRC_URI = "${CLO_LE_GIT}/genivi/lifecycle/node-health-monitor;protocol=${CLO_PROTOCOL};nobranch=1;name=nhm \
           file://fix-no-libsystemd-daemon.patch \
           file://0001-change-service-name.patch \
          "
SRCREV_nhm = "6aa24c04080c3cd0389934841fae5ac502b8e13a"

DEPENDS += " glib-2.0-native"
