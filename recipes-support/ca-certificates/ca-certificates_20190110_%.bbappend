#ca-certificates package is fetch from CLO

SRC_URI = "git://git.codelinaro.org/clo/le/ca-certificates.git;protocol=https;branch=caf_migration/syphilitics/master"

SRC_URI += " \
            file://0002-update-ca-certificates-use-SYSROOT.patch \
            file://0001-update-ca-certificates-don-t-use-Debianisms-in-run-p.patch \
            file://update-ca-certificates-support-Toybox.patch \
            file://default-sysroot.patch \
            file://sbindir.patch \
            file://0003-update-ca-certificates-use-relative-symlinks-from-ET.patch \
            "
