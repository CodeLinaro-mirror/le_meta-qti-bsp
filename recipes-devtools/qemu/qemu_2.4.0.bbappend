FILESEXTRAPATHS_prepend := "${THISDIR}/qemu:"

SRC_URI += "\
        file://texi2pod.patch \
        file://0001-Restore-pre-generated-hex-files.patch \
"
