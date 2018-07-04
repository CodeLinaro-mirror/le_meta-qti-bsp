SRC_URI_remove = "git://git.projects.genivi.org/ipc/common-api-runtime.git;protocol=http"
SRC_URI_append = " ${CAF_GIT}/genivi/ipc/common-api-runtime;protocol=git;branch=genivi/common-api-runtime/master "

DEPENDS_remove = "dlt-daemon"
