
FILESEXTRAPATHS_prepend := "${WORKSPACE}:"
SRC_URI = "\
           file://external/ofono/ \
          "

S = "${WORKDIR}/external/ofono"

PV = "1.19"
PR = "r1"

do_install_prepend() {
    touch ${WORKDIR}/ofono
}

