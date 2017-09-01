
FILESEXTRAPATHS_prepend := "${WORKSPACE}:"
SRC_URI = "file://external/ofono/"

S = "${WORKDIR}/external/ofono"

PV = "1.19"
PR = "r1"
DEPENDS += "multimodule-mgr-oss"

DEPENDS_remove_8x96autogvmquin = "multimodule-mgr-oss"

INCSUFFIX = "${@base_conditional('BASEMACHINE', '8x96auto', 'ofono_auto_nofusion', 'none',d)}"
include ${INCSUFFIX}.inc

INCSUFFIX = "${@base_conditional('BASEMACHINE', '8x96autofusion', 'ofono_auto_fusion', 'none',d)}"
include ${INCSUFFIX}.inc

INCSUFFIX = "${@base_conditional('BASEMACHINE', '8x96autogvmquin', 'ofono_auto_nofusion', 'none',d)}"
include ${INCSUFFIX}.inc

INCSUFFIX = "${@base_conditional('BASEMACHINE', '8x96autogvmquin44', 'ofono_auto_nofusion', 'none',d)}"
include ${INCSUFFIX}.inc

INCSUFFIX = "${@base_conditional('BASEMACHINE', '8x96mizar', 'ofono_auto_nofusion', 'none',d)}"
include ${INCSUFFIX}.inc
