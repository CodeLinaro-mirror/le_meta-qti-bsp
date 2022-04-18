# Add eth0 in NetworkInterfaceBlacklist to improve Ethernet KPI.
do_install_append () {
  sed -i '/^NetworkInterfaceBlacklist/s/$/,eth0/' ${WORKDIR}/main.conf
}
