#ca-certificates package is fetch from CAF

SRC_URI = "git://source.codeaurora.org/quic/le/ca-certificates.git;protocol=https;branch=syphilitics/master"

SRC_URI += " \
            file://0002-update-ca-certificates-use-SYSROOT.patch \
            file://0001-update-ca-certificates-don-t-use-Debianisms-in-run-p.patch \
            file://update-ca-certificates-support-Toybox.patch \
            file://default-sysroot.patch \
            file://sbindir.patch \
            file://0003-update-ca-certificates-use-relative-symlinks-from-ET.patch \
            "
