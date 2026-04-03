SUMMARY = "Global OpenMP runtime support"
DESCRIPTION = "Provide libgomp runtime for all OpenMP-enabled applications"
LICENSE = "BSD-3-Clause-Clear"

ALLOW_EMPTY:${PN} = "1"
RDEPENDS:${PN} = "\
    libgomp \
"
