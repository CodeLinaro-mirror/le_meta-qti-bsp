SUMMARY = "Node.js is a JavaScript runtime built on Chrome's V8 JavaScript engine"
HOMEPAGE = "http://nodejs.org"
DESCRIPTION = "Support NPM build for Qualcomm Platform"

LICENSE = "MIT"

SRC_URI = "https://nodejs.org/download/release/v${PV}/node-v${PV}.tar.gz"

#v18.20.6
LIC_FILES_CHKSUM = "file://LICENSE;md5=ce095b5cae771b11878190eaea818d59"
SRC_URI[sha256sum] = "e7ddfeabea3d1f7cc622cc9861d2fb0955b9e60940dbbedbed6f2f821ab3e4c7"

S = "${WORKDIR}/node-v${PV}"

DEPENDS = "openssl"

PACKAGECONFIG[zlib] = "--shared-zlib,,zlib,"
PACKAGECONFIG[openssl] = "--shared-openssl,,openssl,"
PACKAGECONFIG[v8-inspector] = ",--without-inspector,,"

do_configure() {
	./configure --prefix="${prefix}" \
		    --dest-os=linux \
		    --without-snapshot \
		    --with-intl=full-icu \
	            --shared-openssl \
		    ${EXTRA_OECONF}
}


do_compile() {
	oe_runmake BUILDTYPE=Release
}


do_install() {
	oe_runmake install DESTDIR="${D}"
}

FILES:${PN} =+ "${exec_prefix}/lib/node_modules ${bindir}npm"

BBCLASSEXTEND = "native nativesdk"
