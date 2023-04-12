SRC_URI = " \
     ${CLO_LE_GIT}/genivi/persistence/persistence-common-object;protocol=${CLO_PROTOCOL};nobranch=1;name=pco \
     "
SRCREV_pco = "395b0f504e05ee6c59c1b9d9267c793e072a19ba"
DEPENDS += " glib-2.0-native"
