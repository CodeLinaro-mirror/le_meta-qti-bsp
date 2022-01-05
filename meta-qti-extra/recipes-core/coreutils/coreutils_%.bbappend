# coreutils whose license is gplv2 don't support selinux, by reducing the priority of coreutils, busybox is
# preferred as basic utils.
ALTERNATIVE_PRIORITY = "40"
