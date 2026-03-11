# Fetch code from github
# SRCREV = "eaf3c66b93102dd7c093b942443407fbb1a6445f"
SRCREV = "b185e523fc43538b7f9cc5aba2db230e112c6bc4"
SRC_URI_remove= "http://linuxcontainers.org/downloads/${BPN}-${PV}.tar.gz \
		file://lxc-fix-B-S.patch \
		file://lxc-doc-upgrade-to-use-docbook-3.1-DTD.patch \
		file://logs-optionally-use-base-filenames-to-report-src-fil.patch \
		file://tests-add-no-validate-when-using-download-template.patch \
		file://configure-skip-libseccomp-tests-if-it-is-disabled.patch \
		file://commands-fix-check-for-seccomp-notify-support.patch \
		file://templates-use-curl-instead-of-wget.patch \
"

FILESEXTRAPATHS:prepend := "${THISDIR}/files"

SRC_URI_append = "git://github.com/lxc/lxc.git;branch=stable-6.0;protocol=https \
		file://0001-lxc-support-deny-device-by-devpth.patch \
		file://0002-lxc-handle-cgroup-device-not-available-gracefully.patch \
		file://templates-use-curl-instead-of-wget.patch \
		file://0001-download-don-t-try-compatbility-index.patch \
		file://0001-template-if-busybox-contains-init-use-it-in-containe.patch \
		file://dnsmasq.conf \
		file://lxc-net \
		file://0001-Skip-seccomp-config-while-lxc-doesn-t-enable-seccomp.patch \
                file://0001-lxc-lxc-is-started-after-the-user-service.patch \
    "

DEPENDS_remove = "meson-native"
DEPENDS_append += "meson-lxc-native dbus"
MESON = "${STAGING_BINDIR_NATIVE/meson-lxc}"

inherit meson

PV = "v6.0.4"
SRC_URI[md5sum] = "8ddebe17ef04044cfb66a89ede43dd72"
SRC_URI[sha256sum] = "872d26ce8512b9f993d194816e336bf9f3ad8326f22dc24ef0f01f85599fa8b9"

PACKAGECONFIG[apparmor] = "-Dapparmor=true,-Dapparmor=false,apparmor,apparmor"
PACKAGECONFIG[doc] = "-Dman=true,-Dman=false,,"
PACKAGECONFIG[selinux] = "-Dselinux=true,-Dselinux=false,libselinux,libselinux"
PACKAGECONFIG[seccomp] = "-Dseccomp=true,-Dseccomp=false,libseccomp,libseccomp"
PACKAGECONFIG_remove = "rpath"
EXTRA_OEMESON_remove = "--disable-rpath"
PACKAGECONFIG[systemd] = "-Dsystemd-unitdir=${sysconfdir}/systemd/system/, -Dsystemd-unitdir=, systemd,"
PACKAGECONFIG[systemd] = "-Dinit-script=systemd,-Dinit-script=sysvinit,systemd,"

SYSTEMD_SERVICE:${PN} = "lxc.service lxc-monitord.service"

S = "${WORKDIR}/git"

# Enable container launching automatically
SYSTEMD_AUTO_ENABLE_${PN} = "enable"

# Skip wget as license conflicts
RDEPENDS_${PN}_remove = " wget "
# Disable dnsmasq.service in lxc
RDEPENDS_${PN}_remove = "dnsmasq"

EXTRA_OECONF = " "

EXTRA_OEMESON += "${PTEST_CONF} -Ddistrosysconfdir=${sysconfdir}/default"

do_install() {
	meson_do_install
	cd "${WORKDIR}/git"
}

do_install_postapend() {
	cd "${WORKDIR}/build"
}

