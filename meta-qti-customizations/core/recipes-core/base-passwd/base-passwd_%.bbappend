
# Add 'diag' to passwd.master
do_compile:append() {
    sed -i '/^nobody/ i diag:*:53:53:diag:/nonexistent:/sbin/nologin' ${S}/passwd.master
    sed -i '/^nogroup/ i diag:*:53:' ${S}/group.master
    sed -i '/^nogroup/ i sdcard:*:1015:diag' ${S}/group.master
    sed -i '/^nogroup/ i rebooters:*:1301:diag' ${S}/group.master
    sed -i '/^nogroup/ i inet:x:3003:nobody,root' ${S}/group.master
}

PR = "r1"
