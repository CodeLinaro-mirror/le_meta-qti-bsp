

# Including the file depends on chipset
INCSUFFIX = "${@base_conditional('MACHINEGROUP', 'auto', 'ofono_auto', 'none',d)}"
include ${INCSUFFIX}.inc
