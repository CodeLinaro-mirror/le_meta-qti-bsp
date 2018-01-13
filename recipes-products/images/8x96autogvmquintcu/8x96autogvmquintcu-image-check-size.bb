python do_check_rootfs_size () {
    import subprocess
    rootfs_req_size = int(d.getVar('IMAGE_ROOTFS_SIZE', True))

    output = subprocess.check_output(['du', '-ks', d.getVar('IMAGE_ROOTFS', True)])
    rootfs_file_size = int(output.split()[0])

    if rootfs_req_size - rootfs_file_size < 2048:
        bb.fatal ("Too large rootfs image size\n")
}

addtask do_check_rootfs_size after do_rootfs before do_image
