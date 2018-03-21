require ${COREBASE}/meta/conf/distro/include/security_flags.inc

# Modify the primary security flag definitions
SECURITY_CFLAGS = "-fstack-protector-strong -pie -fpie -Wa,--noexecstack"
SECURITY_PIC_CFLAGS = "-fstack-protector-strong -fPIC -Wa,--noexecstack"
SECURITY_LDFLAGS = "-Wl,-z,relro,-z,now,-z,noexecstack"


SECURITY_CFLAGS_pn-liblog = ""
SECURITY_CFLAGS_pn-ext4-utils = ""
SECURITY_CFLAGS_pn-system-core = ""

SECURITY_CFLAGS_pn-media = "${SECURITY_NO_PIE_CFLAGS}"
SECURITY_LDFLAGS_pn-iptables = "${SECURITY_X_LDFLAGS}"
SECURITY_CFLAGS_pn-gstreamer1.0-qcarcamsrc = "${SECURITY_NO_PIE_CFLAGS}"

SECURITY_CFLAGS_pn-lib32-androidcompat = ""
SECURITY_LDFLAGS_pn-androidcompat = ""

SECURITY_CFLAGS_pn-dbus-c++ = ""

