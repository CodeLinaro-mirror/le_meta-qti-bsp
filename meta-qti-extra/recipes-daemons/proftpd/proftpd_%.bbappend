FILESEXTRAPATHS_append := ":${THISDIR}/files"

SRC_URI_append = " file://disable-reverse-DNS-check.patch \
                   file://enable-RootLogin.patch"
