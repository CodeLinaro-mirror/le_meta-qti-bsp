RDEPENDS_${PN}_remove += "${@'packagegroup-ivi-common-core-graphics' if '${MACHINE}' == '8x96autogvmquintcu' else ''}"

RDEPENDS_${PN}_remove += "${@'packagegroup-ivi-common-core-multimedia' if '${MACHINE}' == '8x96autodvrs' else ''}"
