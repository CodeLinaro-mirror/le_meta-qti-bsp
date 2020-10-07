#RPM package is fetch from CAF location
SRC_URI = "git://source.codeaurora.org/quic/le/rpm.git;protocol=https;branch=rpm/rpm-4.14.x"

SRC_URI += " \
           file://0001-Do-not-add-an-unsatisfiable-dependency-when-building.patch \
           file://0001-Do-not-read-config-files-from-HOME.patch \
           file://0001-When-cross-installing-execute-package-scriptlets-wit.patch \
           file://0001-Do-not-reset-the-PATH-environment-variable-before-ru.patch \
           file://0002-Add-support-for-prefixing-etc-from-RPM_ETCCONFIGDIR-.patch \
           file://0001-When-nice-value-cannot-be-reset-issue-a-notice-inste.patch \
           file://0001-Do-not-hardcode-lib-rpm-as-the-installation-path-for.patch \
           file://0001-Fix-build-with-musl-C-library.patch \
           file://0001-Add-a-color-setting-for-mips64_n32-binaries.patch \
           file://0001-Add-PYTHON_ABI-when-searching-for-python-libraries.patch \
           file://0011-Do-not-require-that-ELF-binaries-are-executable-to-b.patch \
           file://0012-Use-conditional-to-access-_docdir-in-macros.in.patch \
           file://0013-Add-a-new-option-alldeps-to-rpmdeps.patch \
           file://0001-Split-binary-package-building-into-a-separate-functi.patch \
           file://0002-Run-binary-package-creation-via-thread-pools.patch \
           file://0003-rpmstrpool.c-make-operations-over-string-pools-threa.patch \
           file://0004-build-pack.c-remove-static-local-variables-from-buil.patch \
           file://0001-perl-disable-auto-reqs.patch \
           "

