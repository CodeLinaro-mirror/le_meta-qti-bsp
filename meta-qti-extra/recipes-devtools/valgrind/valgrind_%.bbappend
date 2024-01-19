FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " \
            file://0001-Support-Linux-syscall-434-pidfd_open.patch \
            file://0002-Support-Linux-syscall-438-pidfd_getfd.patch \
            "

