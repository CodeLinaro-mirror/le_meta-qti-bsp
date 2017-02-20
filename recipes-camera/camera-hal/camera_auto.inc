DESCRIPTION = "only provide header files reference for camera stack"

FILESPATH =+ "${WORKSPACE}/camera:"
SRC_URI   = "file://lib"
SRC_URI_remove   = "file://camera/lib"

DEPENDS = ""

do_configure[noexec] = "1"
do_compile[noexec]   = "1"
do_install[noexec]   = "1"