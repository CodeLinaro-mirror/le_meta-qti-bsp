
VOLATILE_PATH = "${@bb.utils.contains('DISTRO_FEATURES', 'volatiled-var','persist','var',d)}"
VOLATILE_BINDS = "\
/${VOLATILE_PATH}/build.prop /etc/build.prop\n\
/${VOLATILE_PATH}/data /etc/data/\n\
/${VOLATILE_PATH}/usb /etc/usb/\n\
/${VOLATILE_PATH}/misc/wifi /etc/misc/wifi/\n\
/${VOLATILE_PATH}/bluetooth /etc/bluetooth/\n\
/${VOLATILE_PATH}/allplay /etc/allplay/\n\
/${VOLATILE_PATH}/smack/accesses.d /etc/smack/accesses.d/\n\
"

