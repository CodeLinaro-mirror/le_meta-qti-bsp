FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"
DEPENDS = "base-passwd"

SRC_URI += "file://fstab"
SRC_URI_append_mdm9607 +="file://${BASEMACHINE}/fstab"
SRC_URI_append_8x96auto += "file://${BASEMACHINE}/fstab"
SRC_URI_append_8x96autofusion += "file://${BASEMACHINE}/fstab"
SRC_URI_append_8x96autogvmquin += "file://${BASEMACHINE}/fstab"
SRC_URI_append_8x96autogvmquin44 += "file://${BASEMACHINE}/fstab"
SRC_URI_append_8x96autogvmred += "file://${BASEMACHINE}/fstab"
SRC_URI_append_8x96autonapier +="file://${BASEMACHINE}/fstab"

dirs755 += "/media/cf /media/net /media/ram \
            /media/union /media/realroot /media/hdd \
            /media/mmc1"
dirs755_append_apq8009 +="/persist"

do_install_append(){
  export REGEXP="^(8x96autogvmquin|8x96autogvmquin44|8x96autogvmred)$"
  if [ "${MACHINEGROUP}" == "auto" ] || [[ "${BASEMACHINE}" =~ $REGEXP ]]; then
    install -m 755 -o diag -g diag -d ${D}/media/card
    ln -s /media/card ${D}/sdcard
  else
    install -m 755 -o diag -g diag -d ${D}/media
    install -m 755 -o diag -g diag -d ${D}/mnt/sdcard
    ln -s /mnt/sdcard ${D}/sdcard
    rmdir ${D}/tmp
    ln -s /var/tmp ${D}/tmp
    ln -s /var/run/resolv.conf ${D}/etc/resolv.conf
  fi
}
