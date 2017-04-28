inherit autotools pkgconfig update-rc.d systemd bluetooth

DEPENDS  = "dbus glib-2.0 udev mobile-broadband-provider-info"

FILESPATH =+ "${WORKSPACE}:"

LICENSE  = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a \
		    file://src/ofono.h;beginline=1;endline=20;md5=3ce17d5978ef3445def265b98899c2ee"

SRC_URI = "file://external/ofono/"
SRC_URI += "file://Revert-test-Convert-to-Python-3.patch"
SRC_URI += "file://0002-ofono-Added-a-change-RIL-to-use-root-uid.patch"
SRC_URI += "file://0003-ofono-Export-RIL-variables-while-starting-ofono-serv.patch"

S = "${WORKDIR}/external/ofono"

EXTRA_OECONF += "--enable-test"

SYSTEMD_SERVICE_${PN} = "ofono.service"

PACKAGES =+ "${PN}-tests"

RDEPENDS_${PN} += "dbus"

FILES_${PN} += "${base_libdir}/udev ${systemd_unitdir}"
FILES_${PN}-tests = "${libdir}/${BPN}/test"
RDEPENDS_${PN}-tests = "python python-pygobject python-dbus"

LDFLAGS += "-lcutils"

CFLAGS_append_libc-uclibc = " -D_GNU_SOURCE"
CFLAGS_append += "-DRIL_USE_ROOT_UID"

INITSCRIPT_NAME = "ofono"
INITSCRIPT_PARAMS = "start 99 5 3 2 . stop 01 0 1 6 ."
