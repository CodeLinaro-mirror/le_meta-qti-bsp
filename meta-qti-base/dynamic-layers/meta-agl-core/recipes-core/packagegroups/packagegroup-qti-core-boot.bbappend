# Switch packagegroup-core-boot for AGL's replacement

RDEPENDS:${PN} += "packagegroup-agl-core-boot"

RDEPENDS:${PN}:remove = "packagegroup-core-boot"
