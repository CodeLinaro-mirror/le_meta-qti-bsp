FILESEXTRAPATHS:append := "${THISDIR}/libselinux:"

SRC_URI:append = " \
                file://0001-libselinux-Load-policy-before-set-enforcing.patch \
"
