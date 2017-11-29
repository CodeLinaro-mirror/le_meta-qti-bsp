SRC_URI = "${CAF_GIT}/genivi/lifecycle/node-health-monitor;protocol=git;branch=genivi/node-health-monitor/master \
           file://fix-no-libsystemd-daemon.patch \
           file://0001-change-service-name.patch \
          "

DEPENDS += " glib-2.0-native"
