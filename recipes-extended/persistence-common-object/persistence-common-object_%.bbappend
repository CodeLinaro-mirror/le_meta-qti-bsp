SRC_URI = " \
    git://git.projects.genivi.org/persistence/persistence-common-object.git;tag=${PV};protocol=http \
    file://configure.ac-fix-typo.patch \
    file://B251_typedef_uint64_t.patch \
    "