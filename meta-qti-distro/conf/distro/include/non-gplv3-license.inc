# Eliminate gpl3 gdbm dependency
PACKAGECONFIG:remove:pn-perl = "gdbm"
PACKAGECONFIG:remove:pn-python3 = "gdbm"

# Eliminate gpl3 kbd-keymaps-pine dependency
RRECOMMENDS:${PN}-keymaps:remove:pn-kbd = "${PN}-keymaps-pine"

EXTRA_IMAGE_FEATURES:remove:pn-machine-image = "package-management"
