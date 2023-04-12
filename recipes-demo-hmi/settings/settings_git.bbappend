FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI = "${CLO_LE_GIT}/AGL/apps/settings.git;protocol=${CLO_PROTOCOL};nobranch=1;name=settings"

SRC_URI_chinook += "file://0001-Fix-memory-corruption-issue.patch"

SRCREV_settings = "40865da672a6391da43cecb156484f3f4fe5c6a7"

REMOVE_LIBTOOL_LA = "0"
