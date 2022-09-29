SRC_URI = "git://git.codelinaro.org/clo/le/external/oracle/gtest;protocol=https;branch=caf_migration/soppiest/main"

SRCREV = "d850e144710e330070b756c009749dc7a7302301"

EXTRA_OECMAKE = "-DBUILD_SHARED_LIBS=ON"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
