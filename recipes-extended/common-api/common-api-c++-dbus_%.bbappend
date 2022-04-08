DEPENDS += "gtest"

SRC_URI_remove = "git://git.projects.genivi.org/ipc/common-api-dbus-runtime.git;protocol=http"
SRC_URI_append = " ${CLO_LE_GIT}/genivi/ipc/common-api-dbus-runtime;protocol=https;branch=caf_migration/genivi/common-api-dbus-runtime/master "

