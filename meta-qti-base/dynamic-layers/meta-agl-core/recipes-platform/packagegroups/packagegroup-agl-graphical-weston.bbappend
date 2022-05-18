# Honor the meta-qti-bsp removal of the recommendation of upstream
# Yocto's weston-init, unless building the full AGL demo platform,
# where it is expected to be used by the application framework.
RDEPENDS:${PN}:remove = "${@bb.utils.contains('AGL_FEATURES', 'agldemo', '', 'weston-init', d)}"
