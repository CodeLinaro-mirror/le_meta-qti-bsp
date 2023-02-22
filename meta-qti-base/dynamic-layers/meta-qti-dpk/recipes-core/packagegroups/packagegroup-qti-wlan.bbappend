# Remove QTI hostapd package which has conflict with DPK hostapd
RDEPENDS:${PN}:remove = "hostap-daemon-qcacld"
