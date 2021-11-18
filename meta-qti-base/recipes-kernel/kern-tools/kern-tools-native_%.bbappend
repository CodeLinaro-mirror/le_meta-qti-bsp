FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI_append = " ${@bb.utils.contains('LAYERSERIES_COMPAT_yocto', 'dunfell', 'file://0001-merge_config.sh-Translate-some-env-variables-to-make.patch', '', d)}"
