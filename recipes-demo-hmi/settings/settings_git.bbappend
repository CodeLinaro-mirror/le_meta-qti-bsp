FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI = "git://source.codeaurora.org/quic/le/AGL/apps/settings.git;protocol=https;branch=apps/settings/eel"

SRC_URI_chinook += "file://0001-Fix-memory-corruption-issue.patch"

REMOVE_LIBTOOL_LA = "0"
