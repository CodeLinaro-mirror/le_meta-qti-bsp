# Fix build error caused by upgrading for yocto.
# This fix can be removed when libselinux-python upgrade to 3.1
inherit python3native python3targetconfig
