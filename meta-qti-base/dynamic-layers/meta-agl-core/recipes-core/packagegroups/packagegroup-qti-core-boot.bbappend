# Switch packagegroup-core-boot for AGL's replacement

RDEPENDS_${PN} += "packagegroup-agl-core-boot"

RDEPENDS_${PN}_remove = "packagegroup-core-boot"
