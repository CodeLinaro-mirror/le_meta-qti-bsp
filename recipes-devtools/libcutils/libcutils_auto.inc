INSANE_SKIP_${PN} += "installed-vs-shipped"

EXTRA_OECONF += "${@base_conditional('BASEMACHINE', '8x96auto', ' LE_PROPERTIES_ENABLED=true', '', d)}"
EXTRA_OECONF += "${@base_conditional('BASEMACHINE', '8x96autofusion', ' LE_PROPERTIES_ENABLED=true', '', d)}"
EXTRA_OECONF += "${@base_conditional('BASEMACHINE', '8x96auto44', ' LE_PROPERTIES_ENABLED=true', '', d)}"
