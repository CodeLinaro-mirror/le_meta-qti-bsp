FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"
DEPENDS = "base-passwd"

SRC_URI += "file://fstab"
SRC_URI_append_mdm9607 +="file://${BASEMACHINE}/fstab"
SRC_URI_append_8x96auto += "file://${BASEMACHINE}/fstab"
SRC_URI_append_8x96autofusion += "file://${BASEMACHINE}/fstab"
SRC_URI_append_8x96auto44 += "file://${BASEMACHINE}/fstab"

dirs755 += "/media/cf /media/net /media/ram \
            /media/union /media/realroot /media/hdd \
            /media/mmc1"
dirs755_append_apq8009 +="/persist"

do_install_append(){
  export REGEXP="^(8x96auto|8x96autofusion|8x96auto44)$"
  if [[ "${BASEMACHINE}" =~ $REGEXP ]]; then
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
