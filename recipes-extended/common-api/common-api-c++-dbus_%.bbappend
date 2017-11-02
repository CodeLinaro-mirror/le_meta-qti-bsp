DEPENDS += "gtest"

SRC_URI_remove = "git://git.projects.genivi.org/ipc/common-api-dbus-runtime.git;protocol=http"
SRC_URI_append = " ${CAF_GIT}/genivi/ipc/common-api-dbus-runtime;protocol=git;branch=genivi/common-api-dbus-runtime/master "

