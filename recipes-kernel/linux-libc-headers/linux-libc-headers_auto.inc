INCSUFFIX = "${@base_conditional('BASEMACHINE', '8x96auto', 'linux-libc-headers_auto_8x96auto', 'none',d)}"
include ${INCSUFFIX}.inc

INCSUFFIX = "${@base_conditional('BASEMACHINE', '8x96autofusion', 'linux-libc-headers_auto_kernel4.4', 'none',d)}"
include ${INCSUFFIX}.inc

INCSUFFIX = "${@base_conditional('BASEMACHINE', '8x96autonapier', 'linux-libc-headers_auto_8x96autonapier', 'none',d)}"
include ${INCSUFFIX}.inc

INCSUFFIX = "${@base_conditional('BASEMACHINE', '8x96autogvmred', 'linux-libc-headers_auto_kernel3.18', 'none',d)}"
include ${INCSUFFIX}.inc

INCSUFFIX = "${@base_conditional('BASEMACHINE', '8x96autogvmquin', 'linux-libc-headers_auto_kernel3.18', 'none',d)}"
include ${INCSUFFIX}.inc

INCSUFFIX = "${@base_conditional('BASEMACHINE', '8x96autogvmquintcu', 'linux-libc-headers-8x96autogvmquintcu', 'none',d)}"
include ${INCSUFFIX}.inc

INCSUFFIX = "${@base_conditional('BASEMACHINE', '8x96autocv2x', 'linux-libc-headers_auto_kernel4.4', 'none',d)}"
include ${INCSUFFIX}.inc

INCSUFFIX = "${@base_conditional('BASEMACHINE', '8x96mizar', 'linux-libc-headers_auto_8x96auto', 'none',d)}"
include ${INCSUFFIX}.inc
