SRC_URI = "\
    https://github.com/google/googletest/archive/release-1.7.0.zip \
    file://0001-Add-install-command-for-libraries-and-headers.patch \
    file://0002-CMakeLists-gtest.pc.in-Add-pkg-config-support-to-gte.patch \
"
SRC_URI[md5sum] = "ef5e700c8a0f3ee123e2e0209b8b4961"
SRC_URI[sha256sum] = "b58cb7547a28b2c718d1e38aee18a3659c9e3ff52440297e965f5edffe34b6d0"

S="${WORKDIR}/googletest-release-1.7.0"
