SRC_URI = "git://source.codeaurora.org/quic/le/e2fsprogs.git;branch=ext2/master;protocol=https"

SRC_URI += "file://acinclude.m4 \
            file://remove.ldconfig.call.patch \
            file://quiet-debugfs.patch \
            file://run-ptest \
            file://ptest.patch \
            file://mkdir.patch \
            file://Revert-mke2fs-enable-the-metadata_csum-and-64bit-fea.patch \
            file://mkdir_p.patch \
            file://reproducible-doc.patch \
            file://0001-misc-create_inode.c-set-dir-s-mode-correctly.patch \
            file://0001-misc-rename-copy_file_range-to-copy_file_chunk.patch \
"


