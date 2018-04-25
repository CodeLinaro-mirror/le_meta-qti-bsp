WESTONTTY ??= "1"
WESTONUSER ??= "display"
WESTONGROUP ??= "video"
WESTONARGS ?= "--idle-time=4294967  --tty=${WESTONTTY} --log=/tmp/weston.log"
WESTONLAUNCHARGS ??= "--tty /dev/tty${WESTONTTY} --user ${WESTONUSER}"
DISPLAY_XDG_RUNTIME_DIR ??= "/run/platform/weston"

AISSERVERUSER ??= "ais_server"
CVBSUSER  ??= "cvbs"
ETHCAMUSER ??= "ethcam"

