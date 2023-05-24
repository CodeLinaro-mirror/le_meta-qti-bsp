# coreutils whose license is gplv2 don't support selinux, by reducing the priority of coreutils, busybox is
# preferred as basic utils.
ALTERNATIVE_PRIORITY = "40"

# Need od.coreutils for post boot script
ALTERNATIVE_PRIORITY[od] = "100"

# Need head.coreutils for post boot script
ALTERNATIVE_PRIORITY[head] = "100"

# Need cat.coreutils to show camera first frame in bootkpi
ALTERNATIVE_PRIORITY[cat] = "100"
