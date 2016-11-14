PACKAGE_ARCH = "${MACHINE_ARCH}"

FILESEXTRAPATHS_prepend := "${THISDIR}/agl-audio-plugin:"

SRC_URI += " \
             file://0005-node-fixup-default-string-for-agl_player-nodes.patch \
             file://0008-config-fixup-type-for-demo-effect.patch \
             file://0001-classify-Modify-default-media.role-as-music.patch \
             file://0002-agl-audio-plugin-Duck-behavior-support-on-audio-poli.patch \
             file://0003-agl-audio-plugin-UCM-support-on-audio-policy-manager.patch \
             file://0004-support-hfp-usecase.patch \
            file://0006-support-adjust-hfp-volume.patch \
            file://0007-enable-nullsource-for-capture.patch \
"
