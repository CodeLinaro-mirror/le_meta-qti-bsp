SRC_URI = " \
    ${CLO_LE_GIT}/genivi/persistence/persistence-administrator;protocol=${CLO_PROTOCOL};nobranch=1;name=pa \
    "
SRCREV_pa = "47e5a424c8ef6e33565737f20d3bd81f69c8efc9"

DEPENDS += " glib-2.0-native"
