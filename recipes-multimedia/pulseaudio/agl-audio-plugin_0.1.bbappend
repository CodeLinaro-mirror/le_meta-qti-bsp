PACKAGE_ARCH = "${MACHINE_ARCH}"

FILESEXTRAPATHS_prepend := "${THISDIR}/agl-audio-plugin:"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI = "file://audio/agl-audio-plugin"
S = "${WORKDIR}/audio/agl-audio-plugin/"

GVM_TYPE_AUTO="8x96auto"

EXTRA_OECMAKE += "${@base_conditional('MACHINE', '8x96autogvmquin', ' -DGVM_TYPE:STRING=${GVM_TYPE_AUTO}', '', d)} "
EXTRA_OECMAKE += "${@base_conditional('MACHINE', '8x96autogvmquin44', ' -DGVM_TYPE:STRING=${GVM_TYPE_AUTO}', '', d)} "
EXTRA_OECMAKE += "${@base_conditional('MACHINE', '8x96autogvmred', ' -DGVM_TYPE:STRING=${GVM_TYPE_AUTO}', '', d)} "
EXTRA_OECMAKE += "${@base_conditional('MACHINE', '8x96autogvmgh', ' -DGVM_TYPE:STRING=${GVM_TYPE_AUTO}', '', d)} "

DEPENDS += "${@base_conditional('MACHINE', '8x96autogvmquin', 'vapm-lib', '', d)}"
DEPENDS += "${@base_conditional('MACHINE', '8x96autogvmred', 'vapm-lib', '', d)}"
DEPENDS += "${@base_conditional('MACHINE', '8x96autogvmgh', 'vapm-lib', '', d)}"
