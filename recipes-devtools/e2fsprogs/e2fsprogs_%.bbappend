#Below Package is fetch from CAF 
SRC_URI = "git://source.codeaurora.org/quic/le/e2fsprogs.git;branch=ext2/master;protocol=https"

SRC_URI += "file://remove.ldconfig.call.patch \
            file://run-ptest \
            file://ptest.patch \
            file://Revert-mke2fs-enable-the-metadata_csum-and-64bit-fea.patch \
            file://mkdir_p.patch \
            file://0001-misc-create_inode.c-set-dir-s-mode-correctly.patch \
            file://0001-create_inode-fix-copying-large-files.patch \
            "

