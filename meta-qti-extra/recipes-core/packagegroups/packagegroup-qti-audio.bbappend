RDEPENDS:${PN} += "\
    agl-audio-plugin \
    pulseaudio-misc \
    pulseaudio-module-null-source \
    pulseaudio-server \
    ${@bb.utils.contains('MACHINE_FEATURES', 'qti-audio-ar', bb.utils.contains('MACHINE_FEATURES','qti-hypervisor','', 'pulseaudio-module-codec-control',d), 'pulseaudio-module-acdb pulseaudio-module-codec-control' ,d)} \
"
