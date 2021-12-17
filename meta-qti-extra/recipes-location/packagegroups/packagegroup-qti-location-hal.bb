SUMMARY = "QTI package group for Location modules"

inherit packagegroup

PACKAGES = "\
    packagegroup-qti-location-hal \
    "

ALLOW_EMPTY_${PN} = "1"

RDEPENDS_${PN} += "\
    gps-utils \
    location-hal-daemon \
    loc-core \
    loc-hal \
    location-api \
    location-api-msg-proto \
    synergy-loc-api \
    "
