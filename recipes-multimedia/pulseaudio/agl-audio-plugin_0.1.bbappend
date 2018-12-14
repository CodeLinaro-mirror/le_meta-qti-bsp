PACKAGE_ARCH = "${MACHINE_ARCH}"

FILESEXTRAPATHS_prepend := "${THISDIR}/agl-audio-plugin:"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI = "file://audio/agl-audio-plugin"
S = "${WORKDIR}/audio/agl-audio-plugin/"

SRC_URI += "file://8x96autogvmga/pulseaudio-agl.cfg"

GVM_TYPE_AUTO="8x96auto"

EXTRA_OECMAKE += "${@base_conditional('MACHINE', '8x96autogvmquin', ' -DGVM_TYPE:STRING=${GVM_TYPE_AUTO}', '', d)} "
EXTRA_OECMAKE += "${@base_conditional('MACHINE', '8x96autogvmquin44', ' -DGVM_TYPE:STRING=${GVM_TYPE_AUTO}', '', d)} "
EXTRA_OECMAKE += "${@base_conditional('MACHINE', '8x96autogvmred', ' -DGVM_TYPE:STRING=${GVM_TYPE_AUTO}', '', d)} "
EXTRA_OECMAKE += "${@base_conditional('MACHINE', '8x96autogvmgh', ' -DGVM_TYPE:STRING=${GVM_TYPE_AUTO}', '', d)} "
EXTRA_OECMAKE += "${@base_conditional('MACHINE', '8x96autogvmga', ' -DGVM_TYPE:STRING=${GVM_TYPE_AUTO}', '', d)} "

DEPENDS += "${@base_conditional('MACHINE', '8x96autogvmquin', 'vapm-lib', '', d)}"
DEPENDS += "${@base_conditional('MACHINE', '8x96autogvmred', 'vapm-lib', '', d)}"
DEPENDS += "${@base_conditional('MACHINE', '8x96autogvmgh', 'vapm-lib', '', d)}"
DEPENDS += "${@base_conditional('MACHINE', '8x96autogvmga', 'vapm-lib', '', d)}"

PULSE_PV="8.0"

do_install_append_8x96autogvmga() {
         # override with the machine type specific config
         install -m 0755 ${WORKDIR}/8x96autogvmga/pulseaudio-agl.cfg -D ${D}${sysconfdir}/pulse/
}
