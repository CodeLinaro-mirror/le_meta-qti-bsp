#
# Common bitbake recipe information for QTI meta layers.
# Below are common values, statements and functions.
#
inherit autotools-brokensep pkgconfig

PACKAGE_ARCH    ?= "${MACHINE_ARCH}"
