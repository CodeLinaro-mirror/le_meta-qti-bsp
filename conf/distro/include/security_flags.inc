# Pull in the default security flags
require ${COREBASE}/meta/conf/distro/include/security_flags.inc
# Modify the primary security flag definitions
SECURITY_PIE_CFLAGS = "-fstack-protector-strong -pie -fpie -Wa,--noexecstack -D_FORTIFY_SOURCE=2"
SECURITY_PIC_CFLAGS = "-fstack-protector-strong -fPIC -Wa,--noexecstack -D_FORTIFY_SOURCE=2"

SECURITY_CFLAGS = "${SECURITY_PIE_CFLAGS}"
SECURITY_LDFLAGS = "-Wl,-z,relro,-z,now,-z,noexecstack"

# GPS OSS packages to use -fPIC in security flags instead of -fpie
SECURITY_CFLAGS_pn-loc-core = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-location-api = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-location-client-api = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-loc-socket = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-location-api-iface = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-location-integration-api = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-location-api-msg-proto = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-asn1c-rtx = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-asn1c-crt = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-loc-diag-iface = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-asn1c-cper = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-location-qapi = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-loc-api-v02 = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-loc-hal = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-loc-pla = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-loc-stub = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-gps-utils = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-loc-net-iface = "${SECURITY_PIC_CFLAGS}"
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
SECURITY_CFLAGS_pn-nlp-client-api = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-xt-adapter = "${SECURITY_PIC_CFLAGS}"

# packages to use -fPIC in security flags instead of -fpie
SECURITY_CFLAGS_pn-gtest = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-libcutils  = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-liblog  = "${SECURITY_PIC_CFLAGS}"

# libutils to use -fPIC in security flags instead of -fpie
SECURITY_CFLAGS_pn-system-core = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-lib32-system-core = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-libbase = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-lib32-libbase = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-event-observer = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-data-items = "${SECURITY_PIC_CFLAGS}"
# libbinder to use -fPIC in security flags instead of -fpie
SECURITY_CFLAGS_pn-native-frameworks  = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-av-frameworks  = "${SECURITY_PIC_CFLAGS}"

# BT OSS packages to use -fPIC in security flags instead of -fpie
SECURITY_CFLAGS_pn-bt-property_mdm = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-bt-app_mdm = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-libbt-vendor_mdm = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-fluoride = "${SECURITY_PIC_CFLAGS}"

SECURITY_CFLAGS_pn-display-hal = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-media = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-lib32-media = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-qmmf-sdk = "${SECURITY_PIC_CFLAGS}"
SECURITY_CFLAGS_pn-lib32-qmmf-sdk = "${SECURITY_PIC_CFLAGS}"
