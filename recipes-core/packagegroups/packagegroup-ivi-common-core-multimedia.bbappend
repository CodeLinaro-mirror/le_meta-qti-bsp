BASEMACHINE = "${@d.getVar('MACHINE', True).replace('-perf', '')}"

RDEPENDS_${PN}_remove = "\
        pulseaudio-module-bluetooth-discover \
        pulseaudio-module-bluetooth-policy \
        pulseaudio-module-bluez5-discover \
        pulseaudio-module-bluez5-device \
        pulseaudio-module-switch-on-connect \
        pulseaudio-module-loopback \
"

RDEPENDS_${PN}_remove += "${@ 'agl-audio-plugin' if d.getVar('BASEMACHINE', True) == '8x96autogvmquintcu' else ''}"
RDEPENDS_${PN}_remove += "${@ 'pulseaudio-server' if d.getVar('BASEMACHINE', True) == '8x96autogvmquintcu' else ''}"
RDEPENDS_${PN}_remove += "${@ 'pulseaudio-misc' if d.getVar('BASEMACHINE', True) == '8x96autogvmquintcu' else ''}"