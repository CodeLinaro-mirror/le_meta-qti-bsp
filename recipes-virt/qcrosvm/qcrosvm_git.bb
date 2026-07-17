SUMMARY = "QCrosVM Support"
DESCRIPTION = "It is based on google CrosVM to launch Linux based GVM on QTI platforms."
HOMEPAGE = "https://git.codelinaro.org"
LICENSE = "BSD-3-Clause-Clear & BSD-3-Clause & (Apache-2.0 | MIT) & Apache-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause-Clear;md5=7a434440b651f4a472ca93716d01033a \
                    file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9 \
                    file://${COREBASE}/meta/files/common-licenses/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
                    file://${COREBASE}/meta/files/common-licenses/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS += "cargo-native libcap rust-native rust-llvm-native pkgconfig-native"
RDEPENDS:${PN} += "la-bootloader-mount"

FILESPATH = "${WORKSPACE}:"
SRC_URI = "\
    file://vendor/qcom/opensource/crosvm-gunyah \
    file://external/crosvm \
    file://external/minijail \
    file://external/rust/crates/android_logger \
    file://external/rust/crates/simplelog \
    file://external/rust/crates/vmm_vhost \
"

SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/crosvm-gunyah"

inherit cargo systemd cargo-update-recipe-crates

require ${BPN}-crates.inc

CARGO_BUILD_FLAGS += "--features=vhost-user-generic"

CFLAGS:append = " -Wno-error=stringop-overflow="

SYSTEMD_SERVICE:${PN} = "qcrosvm.service"

EXTRA_OECMAKE += "\
    -DENABLE_TARGET=${BASEMACHINE} \
"

VM_CONFIG_XML ?= "vm_config.xml"
QCROSVM_SERVICE ?= "qcrosvm.service"
SOCKET_WAIT_SCRIPT = "wait_for_sockets.sh"

do_install:append() {
    install -d ${D}${sysconfdir}
    install -m 0644 ${S}/vm_config_xml/${VM_CONFIG_XML} ${D}${sysconfdir}/vm_config.xml

    install -d ${D}${systemd_unitdir}/system/
    install -m 0644 ${S}/${QCROSVM_SERVICE} ${D}/${systemd_unitdir}/system/qcrosvm.service

    install -d ${D}${bindir}
    install -m 0755 ${S}/${SOCKET_WAIT_SCRIPT} ${D}${bindir}/${SOCKET_WAIT_SCRIPT}
}

INSANE_SKIP:${PN}-dbg += "buildpaths"
