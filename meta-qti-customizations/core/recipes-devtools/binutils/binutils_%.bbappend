#Below Package is Fetch from the Codelinaro
BINUTILS_GIT_URI:append:rbx210-rbx = "{CLO_LE_GIT}/binutils-gdb.git;branch=binutils-gdb/binutils-${BINUPV}-branch;protocol=https"
BINUTILS_GIT_URI ?= "${CLO_LE_GIT}/binutils-gdb.git;protocol=https;branch=caf_migration/binutils-gdb/binutils-${BINUPV}-branch"
