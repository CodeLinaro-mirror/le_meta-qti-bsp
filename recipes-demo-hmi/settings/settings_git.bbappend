FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI = "${CLO_LE_GIT}/AGL/apps/settings.git;protocol=https;branch=caf_migration/apps/settings/dab"

SRC_URI_chinook += "file://0001-Fix-memory-corruption-issue.patch"

REMOVE_LIBTOOL_LA = "0"
