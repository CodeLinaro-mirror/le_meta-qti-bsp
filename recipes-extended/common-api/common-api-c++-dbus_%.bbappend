DEPENDS += "gtest"

SRC_URI_remove = "git://git.projects.genivi.org/ipc/common-api-dbus-runtime.git;protocol=http"
SRC_URI_append = " ${CLO_LE_GIT}/genivi/ipc/common-api-dbus-runtime;protocol=${CLO_PROTOCOL};nobranch=1;name=cadr"

SRCREV_cadr = "3348a422ffc756b63de5890356383858a898e8b1"

