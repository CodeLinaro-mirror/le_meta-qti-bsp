# Pull in the default security flags
require ${COREBASE}/meta/conf/distro/include/security_flags.inc

# Modify the primary security flag definitions
SECURITY_CFLAGS = "-fstack-protector-strong -pie -fpie -Wa,--noexecstack"
SECURITY_PIC_CFLAGS = "-fstack-protector-strong -fPIC -Wa,--noexecstack"
SECURITY_LDFLAGS = "-Wl,-z,relro,-z,now,-z,noexecstack"

# GPS OSS packages to use -fPIC in security flags instead of -fpie
SECURITY_CFLAGS_pn-loc-hal = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-loc-pla = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-loc-stub = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-gps-utils = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-loc-core = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-loc-api-v02 = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-loc-ds = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-loc-net-iface = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-location-api = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-loc-base-util = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-loc-mq-client = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-izat-core = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-lowi-client = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-lbs-core = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-drplugin-client = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-drplugin = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-ulp2 = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-location-flp = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-location-service = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-loc-glue = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-location-batching = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-location-geofence = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-location-integration-api = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-location-qapi = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-loc-socket = "${SECURITY_PIC_CFLAGS}"
