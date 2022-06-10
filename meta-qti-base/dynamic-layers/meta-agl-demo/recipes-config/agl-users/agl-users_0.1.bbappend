LOGIN_USER ?= "1001"
PASSENGER_USER ?= "${@${LOGIN_USER} + 1}"

GROUPADD_PARAM:${PN} = "\
        --system display ; \
        --system weston-launch ; \
        -g ${LOGIN_USER} agl-driver ; \
        -g ${PASSENGER_USER} agl-passenger \
"

USERADD_PARAM:${PN} = "\
  -g ${LOGIN_USER} -u ${LOGIN_USER} -o -d /home/${LOGIN_USER} -m -K PASS_MAX_DAYS=-1 agl-driver ; \
  -g ${PASSENGER_USER} -u ${PASSENGER_USER} -o -d /home/${PASSENGER_USER} -m -K PASS_MAX_DAYS=-1 agl-passenger ; \
  --gid display --groups weston-launch,video,input --home-dir /run/platform/display --shell /bin/false --comment \"Display daemon\" --key PASS_MAX_DAYS=-1 display \
"
