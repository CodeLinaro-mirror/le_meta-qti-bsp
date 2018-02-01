FILESEXTRAPATHS_append := ":${THISDIR}/files"

SRC_URI_append += "file://0009-fix-systemd-logind-kicked-off-by-dbus.patch"

do_install_append () {
   # dbus-1.8.x depends on this dir
   if [ ! -d "${D}/etc/dbus-1/session.d" ]; then
       mkdir -p "${D}/etc/dbus-1/session.d"
   fi
}
