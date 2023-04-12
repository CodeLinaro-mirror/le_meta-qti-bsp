SRC_URI = "${CLO_LE_GIT}/AGL/apps/phone.git;protocol=${CLO_PROTOCOL};nobranch=1;name=phone"

SRCREV_phone = "3d8fed7bb32181c4065c556af1f0a407aa11a91a"

REMOVE_LIBTOOL_LA = "0"

DEPENDS_remove += "homescreen"
