DESCRIPTION = "lxc aims to use these new functionnalities to provide an userspace container object"
SECTION = "console/utils"
LICENSE = "LGPL-2.1-only & GPL-2.0-only"
LIC_FILES_CHKSUM = "file://LICENSE.LGPL2.1;md5=4fbd65380cdd255951079008b364516c \
                    file://LICENSE.GPL2;md5=751419260aa954499f7abaabaa882bbe \
"

DEPENDS = "libxml2 libcap"
RDEPENDS:${PN} = " \
		rsync \
		curl \
		gzip \
		xz \
		tar \
		libcap-bin \
		bridge-utils \
		dnsmasq \
		perl-module-strict \
		perl-module-getopt-long \
		perl-module-vars \
		perl-module-exporter \
		perl-module-constant \
		perl-module-overload \
		perl-module-exporter-heavy \
		gmp \
		libidn \
		gnutls \
		nettle \
		util-linux-mountpoint \
		util-linux-getopt \
"

RDEPENDS:${PN}:append:libc-glibc = " glibc-utils"

RDEPENDS:${PN}-ptest += "file make gmp nettle gnutls bash libgcc"

RDEPENDS:${PN}-networking += "iptables"

LXC_META_VIRT_BASE = "https://git.codelinaro.org/clo/femto5g/external/yoctoproject.org/meta-virtualization/-/raw/caf-ype/auto-yocto-scarthgap-upstream.lnx.1.0.c1/recipes-containers/lxc/files"

SRC_URI = "git://github.com/lxc/lxc.git;branch=stable-5.0;protocol=https \
    ${LXC_META_VIRT_BASE}/lxc-1.0.0-disable-udhcp-from-busybox-template.patch;name=disable_udhcp;downloadfilename=lxc-1.0.0-disable-udhcp-from-busybox-template.patch;apply=yes \
    ${LXC_META_VIRT_BASE}/run-ptest;name=run_ptest;downloadfilename=run-ptest;apply=no \
    ${LXC_META_VIRT_BASE}/templates-actually-create-DOWNLOAD_TEMP-directory.patch;name=create_download_temp;downloadfilename=templates-actually-create-DOWNLOAD_TEMP-directory.patch;apply=yes \
    ${LXC_META_VIRT_BASE}/template-make-busybox-template-compatible-with-core-.patch;name=busybox_template_core;downloadfilename=template-make-busybox-template-compatible-with-core-.patch;apply=yes \
    ${LXC_META_VIRT_BASE}/templates-use-curl-instead-of-wget.patch;name=templates_use_curl;downloadfilename=templates-use-curl-instead-of-wget.patch;apply=yes \
    ${LXC_META_VIRT_BASE}/0001-download-don-t-try-compatbility-index.patch;name=download_compat_index;downloadfilename=0001-download-don-t-try-compatbility-index.patch;apply=yes \
    ${LXC_META_VIRT_BASE}/tests-our-init-is-not-busybox.patch;name=tests_init_not_busybox;downloadfilename=tests-our-init-is-not-busybox.patch;apply=yes \
    ${LXC_META_VIRT_BASE}/0001-template-if-busybox-contains-init-use-it-in-containe.patch;name=busybox_contains_init;downloadfilename=0001-template-if-busybox-contains-init-use-it-in-containe.patch;apply=yes \
    ${LXC_META_VIRT_BASE}/dnsmasq.conf;name=dnsmasq_conf;downloadfilename=dnsmasq.conf;apply=no \
    ${LXC_META_VIRT_BASE}/lxc-net;name=lxc_net;downloadfilename=lxc-net;apply=no \
    ${LXC_META_VIRT_BASE}/0001-lxc-test-usernic-drop-cgroup-handling.patch;name=usernic_drop_cgroup;downloadfilename=0001-lxc-test-usernic-drop-cgroup-handling.patch;apply=yes \
    ${LXC_META_VIRT_BASE}/0001-tests-remove-old-and-broken-cgroup-handling-code-fro.patch;name=tests_remove_old_cgroup;downloadfilename=0001-tests-remove-old-and-broken-cgroup-handling-code-fro.patch;apply=yes \
"

SRC_URI[disable_udhcp.sha256sum] = "5687d3df8673411e073155fbbc1e7d26292499743a57e9adae6c3451b23cba59"
SRC_URI[run_ptest.sha256sum] = "cedc46654b59a15ef0da49467f74fdd30e694da2eccdfa41985119383f105b4c"
SRC_URI[create_download_temp.sha256sum] = "cce3ac7aa94f4679fd47657c1677c8378d8c1cebdc978fe2a626044c1055c4f0"
SRC_URI[busybox_template_core.sha256sum] = "0ab2e5284bd55b5cc208487c51917ff5e45e395f66413228acf8b603f19150a2"
SRC_URI[templates_use_curl.sha256sum] = "f769108fc5f9d9b1fd30f6181fdbf711240781c9dc7c6eca6c0ba8260c4936f1"
SRC_URI[download_compat_index.sha256sum] = "89ea527fa61ed2a2b0f0810e9c2ed9b1b53ef5292c3bbca4a1ae419a98273801"
SRC_URI[tests_init_not_busybox.sha256sum] = "5b251e5c29e0f6dbcbe676fdd638ba0ec6aeff2fcd06b1a5b6ac32ae9d95d015"
SRC_URI[busybox_contains_init.sha256sum] = "b8347d05b341b50a9fd533a2ca34b6ee77ec975afd6f68c4d8e54d810654632d"
SRC_URI[dnsmasq_conf.sha256sum] = "82dffd8fda7d8b73de04393b8c6fb55c43dd3e91e500784a8f3d9d9fa6a8408a"
SRC_URI[lxc_net.sha256sum] = "98f62b6bc42cc1192c2182d88e9cb0dfd67071b20eff069a614b1c0f712d5873"
SRC_URI[usernic_drop_cgroup.sha256sum] = "2b0cec748c836334be73f47bbfa61150263be8be0a5a7d9bc62f1a96bd4109fc"
SRC_URI[tests_remove_old_cgroup.sha256sum] = "06b728cbb2bf8a08dd1f836c1a939c053b7ee93b65036d9b11c9afc898a53828"


SRCREV = "cb8e38aca27a23964941f0f011a8919aab8bebab"
PV = "5.0.3+git"

S = "${WORKDIR}/git"

# Let's not configure for the host distro.
#
PTEST_CONF = "${@bb.utils.contains('DISTRO_FEATURES', 'ptest', '-Dtests=true', '', d)}"

# No meson equivalent for --with-distro
# EXTRA_OECONF += "--with-distro=${DISTRO} ${PTEST_CONF}"
EXTRA_OEMESON += "${PTEST_CONF} -Ddistrosysconfdir=${sysconfdir}/default"
# No meson equivalent for these yet
# EXTRA_OECONF += "--enable-log-src-basename --disable-werror"

PACKAGECONFIG ??= "templates \
    ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'selinux', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'seccomp', 'seccomp', '', d)} \
"

# Meson doesn't seem to be as fine grained as the autotools releases
# PACKAGECONFIG[doc] = "--enable-doc --enable-api-docs,--disable-doc --disable-api-docs,,"
PACKAGECONFIG[doc] = "-Dman=true,-Dman=false,,"
# No meson equiv found for rpath yet
# PACKAGECONFIG[rpath] = "--enable-rpath,--disable-rpath,,"
PACKAGECONFIG[apparmor] = "-Dapparmor=true,-Dapparmor=false,apparmor,apparmor"
PACKAGECONFIG[templates] = ",,, ${PN}-templates"
PACKAGECONFIG[selinux] = "-Dselinux=true,-Dselinux=false,libselinux,libselinux"
PACKAGECONFIG[seccomp] ="-Dseccomp=true,-Dseccomp=false,libseccomp,libseccomp"
PACKAGECONFIG[systemd] = "-Dsystemd-unitdir=${sysconfdir}/systemd/system/, -Dsystemd-unitdir=, systemd,"
PACKAGECONFIG[systemd] = "-Dinit-script=systemd,-Dinit-script=sysvinit,systemd,"

# required by python3 to run setup.py
export BUILD_SYS
export HOST_SYS
export STAGING_INCDIR
export STAGING_LIBDIR

inherit meson pkgconfig ptest update-rc.d systemd python3native

SYSTEMD_PACKAGES = "${PN} ${PN}-networking"
SYSTEMD_SERVICE:${PN} = "lxc.service lxc-monitord.service"
SYSTEMD_AUTO_ENABLE:${PN} = "disable"
SYSTEMD_SERVICE:${PN}-networking = "lxc-net.service"
SYSTEMD_AUTO_ENABLE:${PN}-networking = "enable"

INITSCRIPT_PACKAGES = "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '', '${PN}', d)} ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '', '${PN}-networking',d)}"
INITSCRIPT_NAME:${PN} = "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '', 'lxc-containers', d)}"
INITSCRIPT_PARAMS:${PN} = "defaults"
INITSCRIPT_NAME:${PN}-networking = "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', '', 'lxc-net', d)}"
INITSCRIPT_PARAMS:${PN}-networking = "defaults"

FILES:${PN}-doc = "${mandir} ${infodir}"
# For LXC the docdir only contains example configuration files and should be included in the lxc package
FILES:${PN} += "${docdir}"
FILES:${PN} += "${libdir}/python3*"
FILES:${PN} += "${datadir}/bash-completion"
FILES:${PN}-dbg += "${libexecdir}/lxc/.debug"
FILES:${PN}-dbg += "${libexecdir}/lxc/hooks/.debug"
PACKAGES =+ "${PN}-templates ${PN}-networking ${PN}-lua"
FILES:lua-${PN} = "${datadir}/lua ${libdir}/lua"
FILES:lua-${PN}-dbg += "${libdir}/lua/lxc/.debug"
FILES:${PN}-templates += "${datadir}/lxc/templates"
RDEPENDS:${PN}-templates += "bash"

FILES:${PN}-networking += " \
    ${sysconfdir}/init.d/lxc-net \
    ${sysconfdir}/default/lxc-net \
"

# Not needed for meson
# CACHED_CONFIGUREVARS += " \
#     ac_cv_path_PYTHON='${STAGING_BINDIR_NATIVE}/python3-native/python3' \
#     am_cv_python_pyexecdir='${PYTHON_SITEPACKAGES_DIR}' \
#     am_cv_python_pythondir='${PYTHON_SITEPACKAGES_DIR}' \
#"

do_install:append() {
	# The /var/cache/lxc directory created by the Makefile
	# is wiped out in volatile, we need to create this at boot.
	rm -rf ${D}${localstatedir}/cache
	install -d ${D}${sysconfdir}/default/volatiles
	echo "d root root 0755 ${localstatedir}/cache/lxc none" \
	     > ${D}${sysconfdir}/default/volatiles/99_lxc

	for i in `grep -l "#! */bin/bash" ${D}${datadir}/lxc/hooks/*`; do \
	    sed -e 's|#! */bin/bash|#!/bin/sh|' -i $i; done

	if "${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}"; then
	    # nothing special for systemd at the moment
	    true
	else
	    # with meson, these aren't built unless sysvinit is the enabled
	    # init system.
	    install -d ${D}${sysconfdir}/init.d
	    install -m 755 config/init/sysvinit/lxc* ${D}${sysconfdir}/init.d
	fi

	# since python3-native is used for install location this will not be
	# suitable for the target and we will have to correct the package install
	if ${@bb.utils.contains('PACKAGECONFIG', 'python', 'true', 'false', d)}; then
	    if [ -d ${D}${exec_prefix}/lib/python* ]; then mv ${D}${exec_prefix}/lib/python* ${D}${libdir}/; fi
	    rmdir --ignore-fail-on-non-empty ${D}${exec_prefix}/lib
	fi

	# /etc/default/lxc sources lxc-net, this allows lxc bridge when lxc-networking
	# is not installed this results in no lxcbr0, but when lxc-networking is installed
	# lxcbr0 will be fully configured.
	install -m 644 ${WORKDIR}/lxc-net ${D}${sysconfdir}/default/

	# Force the main dnsmasq instance to bind only to specified interfaces and
	# to not bind to virbr0. Libvirt will run its own instance on this interface.
	install -d ${D}/${sysconfdir}/dnsmasq.d
	install -m 644 ${WORKDIR}/dnsmasq.conf ${D}/${sysconfdir}/dnsmasq.d/lxc
}

EXTRA_OEMAKE += "TEST_DIR=${D}${PTEST_PATH}/src/tests"

do_install_ptest() {
	# Move tests to the "ptest directory"
	install -d ${D}/${PTEST_PATH}/tests
	mv ${D}/usr/bin/lxc-test-* ${D}/${PTEST_PATH}/tests/.
}

pkg_postinst:${PN}() {
	if [ -z "$D" ] && [ -e /etc/init.d/populate-volatile.sh ] ; then
		/etc/init.d/populate-volatile.sh update
	fi
}

pkg_postinst:${PN}-networking() {
if ${@bb.utils.contains('DISTRO_FEATURES', 'sysvinit', 'true', 'false', d)}; then
cat >> $D/etc/network/interfaces << EOF

auto lxcbr0
iface lxcbr0 inet dhcp
	bridge_ports eth0
	bridge_fd 0
	bridge_maxwait 0
EOF

cat<<EOF>$D/etc/network/if-pre-up.d/lxcbr0
#! /bin/sh

if test "x\$IFACE" = xlxcbr0 ; then
        brctl show |grep lxcbr0 > /dev/null 2>/dev/null
        if [ \$? != 0 ] ; then
                brctl addbr lxcbr0
                brctl addif lxcbr0 eth0
                ip addr flush eth0
                ifconfig eth0 up
        fi
fi
EOF
chmod 755 $D/etc/network/if-pre-up.d/lxcbr0
fi
}
