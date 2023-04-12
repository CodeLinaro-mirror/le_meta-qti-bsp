SRC_URI = "${CLO_LE_GIT}/AGL/apps/radio.git;protocol=${CLO_PROTOCOL};nobranch=1;name=radio \
           file://presets-ALS.conf \
           file://presets-CES.conf \
           file://presets-FOSDEM.conf \
"
SRCREV_radio = "3ebb0c952333caf715a9e2a6c23360db93e54f75"


REMOVE_LIBTOOL_LA = "0"
