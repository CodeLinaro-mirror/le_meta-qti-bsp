RDEPENDS:${PN}:remove = "${@bb.utils.contains("DISTRO_FEATURES", "bluetooth", "", "bluez-alsa-pipewire", d)}"
