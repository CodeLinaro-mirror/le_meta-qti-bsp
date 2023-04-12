require systemd.inc

PROVIDES = "udev"

PE = "1"

DEPENDS = "kmod intltool-native gperf-native acl readline libcap libcgroup util-linux"

SECTION = "base/shell"

inherit useradd pkgconfig autotools perlnative update-rc.d update-alternatives qemu systemd ptest gettext bash-completion
SOURCE_SYSTEMD_PATCHES = "https://git.codelinaro.org/clo/ype/external/yoctoproject.org/poky/-/raw/b7e7b5e294f944c27fb1d2be61c0cf38f6c81ba8/meta/recipes-core/systemd/systemd/"

SRC_URI_append += "${SOURCE_SYSTEMD_PATCHES}touchscreen.rules;downloadfilename=touchscreen.rules;name=touchscreen"
SRC_URI[touchscreen.md5sum] = "c4f9091e52415d87947975b6b659fac7"
SRC_URI[touchscreen.sha256sum] = "e85f922aa4636aae146c426cec72b66fc3511283b0e90d176bd5400f568bb3a4"

SRC_URI_append += "${SOURCE_SYSTEMD_PATCHES}/00-create-volatile.conf;downloadfilename=00-create-volatile.conf;name=cv-c"
SRC_URI[cv-c.md5sum] = "c7b13af0e99a8a07bc7fb261d3839dcb"
SRC_URI[cv-c.sha256sum] = "f40abf4b7ee1b0d09b62cdcb360b77aaf0e1f2da3dbc2324f579decc9984dfe5"

SRC_URI_append += "${SOURCE_SYSTEMD_PATCHES}/init;downloadfilename=init;name=init"
SRC_URI[init.md5sum] = "93e9a1eca70811c731fdcb34c82800b7"
SRC_URI[init.sha256sum] = "bfa59349069020d05d9c344a6f871e4e2e1b1a4e1aeeb5886c7f7e5d2dbbb841"

SRC_URI_append += "${SOURCE_SYSTEMD_PATCHES}/run-ptest;downloadfilename=run-ptest;name=rp"
SRC_URI[rp.md5sum] = "8c94b5e6cfd634725213e8ebdd9de64d"
SRC_URI[rp.sha256sum] = "5093b11e9ad55047c0b6ac00e32c0634586643d19560503026eabc7387fa3276"

SRC_URI_append += "${SOURCE_SYSTEMD_PATCHES}/0002-units-Prefer-getty-to-agetty-in-console-setup-system.patch;downloadfilename=0002-units-Prefer-getty-to-agetty-in-console-setup-system.patch;name=upga-p"
SRC_URI[upga-p.md5sum] = "5e915f1b0a98317ac4a865816a8c8672"
SRC_URI[upga-p.sha256sum] = "afe9a65ff940a4be038152f4e187b8b93686406ff90237642c73aa6ff18343c6"

SRC_URI_append += "${SOURCE_SYSTEMD_PATCHES}/0003-define-exp10-if-missing.patch;downloadfilename=0003-define-exp10-if-missing.patch;name=deim-p"
SRC_URI[deim-p.md5sum] = "c5c712980912f47726440c97ee69b9af"
SRC_URI[deim-p.sha256sum] = "bf10de90a5a6441f9a5cedbfed6c2a8cc67adff34bc2aabb99b905f912b18c0a"

SRC_URI_append += "${SOURCE_SYSTEMD_PATCHES}/0004-Use-getenv-when-secure-versions-are-not-available.patch;downloadfilename=0004-Use-getenv-when-secure-versions-are-not-available.patch;name=ugws-p"
SRC_URI[ugws-p.md5sum] = "ccae80fcbff696b58b39e46ef622256c"
SRC_URI[ugws-p.sha256sum] = "a5fe8ab77ed707cb2fe1c5755f5605ec8d9b44a7b5821aef67b006f7c037f89b"


SRC_URI_append += "${SOURCE_SYSTEMD_PATCHES}/0005-binfmt-Don-t-install-dependency-links-at-install-tim.patch;downloadfilename=0005-binfmt-Don-t-install-dependency-links-at-install-tim.patch;name=didl-p"
SRC_URI[didl-p.md5sum] = "78ab57a5e746495c004112fd1bec7322"
SRC_URI[didl-p.sha256sum] = "72cbc3478c76e0888beeb196cbcb3570aae2aa39a3173c18fe16d60c509eeca8"

SRC_URI_append += "${SOURCE_SYSTEMD_PATCHES}/0006-configure-Check-for-additional-features-that-uclibc-.patch;downloadfilename=0006-configure-Check-for-additional-features-that-uclibc-.patch;name=ccfa-p"
SRC_URI[ccfa-p.md5sum] = "70a624b40f8c79d1214e7eac35c8c3c9"
SRC_URI[ccfa-p.sha256sum] = "5bfc58b48bac34a44de9348905d5b49b41b561618dd38d82420f1a622e5094bc"

SRC_URI_append += "${SOURCE_SYSTEMD_PATCHES}/0007-use-lnr-wrapper-instead-of-looking-for-relative-opti.patch;downloadfilename=0007-use-lnr-wrapper-instead-of-looking-for-relative-opti.patch;name=0007-p"
SRC_URI[0007-p.md5sum] = "7f22617486e7e49323fd085f20a3b7b9"
SRC_URI[0007-p.sha256sum] = "f4b81ad3caa17c59dcb25c7686d55f09862ada64376a8641342c2260bd76b6ce"

SRC_URI_append += "${SOURCE_SYSTEMD_PATCHES}/0008-nspawn-Use-execvpe-only-when-libc-supports-it.patch;downloadfilename=0008-nspawn-Use-execvpe-only-when-libc-supports-it.patch;name=0008-p"
SRC_URI[0008-p.md5sum] = "68325a5e73052b1f65107b00d801aff5"
SRC_URI[0008-p.sha256sum] = "f8651b7838cc07e54723d4690522686f4dd61334346fee649004b0b158e90952"

SRC_URI_append += "${SOURCE_SYSTEMD_PATCHES}/0009-util-bypass-unimplemented-_SC_PHYS_PAGES-system-conf.patch;downloadfilename=0009-util-bypass-unimplemented-_SC_PHYS_PAGES-system-conf.patch;name=0009-p"
SRC_URI[0009-p.md5sum] = "fa3692177004531c1ebb3c4ef0b15b5a"
SRC_URI[0009-p.sha256sum] = "2d3a1ea4e23d78c0724a0de5506700134a11350ec5da29cea82182e0f8dc4bb4"

SRC_URI_append += "${SOURCE_SYSTEMD_PATCHES}/0010-implment-systemd-sysv-install-for-OE.patch;downloadfilename=0010-implment-systemd-sysv-install-for-OE.patch;name=0010-p"
SRC_URI[0010-p.md5sum] = "4b6717eb6aeb9f3fe8186deb0e41470b"
SRC_URI[0010-p.sha256sum] = "879cba9fbdc43725dfe64a03a429c9d20f2c753c0e4f97d9650ad312021e19b8"

SRC_URI_append += "${SOURCE_SYSTEMD_PATCHES}/0011-nss-mymachines-Build-conditionally-when-HAVE_MYHOSTN.patch;downloadfilename=0011-nss-mymachines-Build-conditionally-when-HAVE_MYHOSTN.patch.patch;name=0011-p"
SRC_URI[0011-p.md5sum] = "7a15cfc395770fbf59fa43194a559850"
SRC_URI[0011-p.sha256sum] = "4f136d1de322b480a317d8469575badb4500d043428b587c1be3ad28777a0d1b"

SRC_URI_append += "${SOURCE_SYSTEMD_PATCHES}/0012-rules-whitelist-hd-devices.patch;downloadfilename=0012-rules-whitelist-hd-devices.patch;name=0012-p"
SRC_URI[0012-p.md5sum] = "9bf93ad3f0224c84595ecf94ab1f3364"
SRC_URI[0012-p.sha256sum] = "093901adf34cb41a543efed127a0f5e04957f4b95698c89274dfea1a098049b8"

SRC_URI_append += "${SOURCE_SYSTEMD_PATCHES}/0013-Make-root-s-home-directory-configurable.patch;downloadfilename=0013-Make-root-s-home-directory-configurable.patch;name=0013-p"
SRC_URI[0013-p.md5sum] = "a3f2e8d0de013060815a62c286fc1118"
SRC_URI[0013-p.sha256sum] = "edb7e155b30891a22382eb42bde4011b574e4c6eb5c8355e15587344a66b9e2c"

SRC_URI_append += "${SOURCE_SYSTEMD_PATCHES}/0014-Revert-rules-remove-firmware-loading-rules.patch;downloadfilename=0014-Revert-rules-remove-firmware-loading-rules.patch;name=0014-p"
SRC_URI[0014-p.md5sum] = "f8dc87f8904d8c16704114b10f1d8904"
SRC_URI[0014-p.sha256sum] = "0c3b83d5ecd720058ec426986da581c4cf31eb8a193aaa3804581c3a4dbd9cb0"

SRC_URI_append += "${SOURCE_SYSTEMD_PATCHES}/0015-Revert-udev-remove-userspace-firmware-loading-suppor.patch;downloadfilename=0015-Revert-udev-remove-userspace-firmware-loading-suppor.patch;name=0015-p"
SRC_URI[0015-p.md5sum] = "71621696b34e1cc63ab0f3aee20727c3"
SRC_URI[0015-p.sha256sum] = "7681f004f2ce744bc15af1618c122e0c71b440df3900238f6680b4ffec630716"

SRC_URI_append += "${SOURCE_SYSTEMD_PATCHES}/0016-make-test-dir-configurable.patch;downloadfilename=0016-make-test-dir-configurable.patch;name=0016-p"
SRC_URI[0016-p.md5sum] = "cb042f2f01f0512a791c0f4e57b4ee7f"
SRC_URI[0016-p.sha256sum] = "0d719c048a35b112bc960810c44daa971893855f27078a344153c1a34c656214"

SRC_URI_append += "${SOURCE_SYSTEMD_PATCHES}/0017-remove-duplicate-include-uchar.h.patch;downloadfilename=0017-remove-duplicate-include-uchar.h.patch;name=0017-p"
SRC_URI[0017-p.md5sum] = "875cee9573dec01e7b901c0d923f6135"
SRC_URI[0017-p.sha256sum] = "2101e7378f4724590814a9bbd451548b9197529617a9b3ec08fb549bd1e3259d"

SRC_URI_append += "${SOURCE_SYSTEMD_PATCHES}/0018-check-for-uchar.h-in-configure.patch;downloadfilename=0018-check-for-uchar.h-in-configure.patch;name=0018-p"
SRC_URI[0018-p.md5sum] = "b77090b6ebfc4a61fb7ee590fbf69fd7"
SRC_URI[0018-p.sha256sum] = "2e5243ac0588de31805614e7b16de36f99fb973016e0b2bea8ea4abc123e78bc"

SRC_URI_append += "${SOURCE_SYSTEMD_PATCHES}/0019-socket-util-don-t-fail-if-libc-doesn-t-support-IDN.patch;downloadfilename=0019-socket-util-don-t-fail-if-libc-doesn-t-support-IDN.patch;name=0019-p"
SRC_URI[0019-p.md5sum] = "cb10493c7d0dc12f4804954e4437a4c0"
SRC_URI[0019-p.sha256sum] = "ed2e431a6dd8e171fe69f5678b4a31764f2dd5e6e8957a763ac343fc5e8553bb"

SRC_URI_append += "${SOURCE_SYSTEMD_PATCHES}/0020-back-port-233-don-t-use-the-unified-hierarchy-for-the-systemd.patch;downloadfilename=0020-back-port-233-don-t-use-the-unified-hierarchy-for-the-systemd.patch;name=0020-p"
SRC_URI[0020-p.md5sum] = "2ded3d8934d3f0498eee21e351db9625"
SRC_URI[0020-p.sha256sum] = "7a3942baeabc7c1a207554e70ee67f8b769e3fe604a02591bd56d8b3afd40441"

SRC_URI_append += "${SOURCE_SYSTEMD_PATCHES}/0001-core-load-fragment-refuse-units-with-errors-in-certa.patch;downloadfilename=0001-core-load-fragment-refuse-units-with-errors-in-certa.patch;name=0001-p"
SRC_URI[0001-p.md5sum] = "0368ab50ecc1f1749f6665cee59427bc"
SRC_URI[0001-p.sha256sum] = "d0e298826c82f289db468c575f29ca4a66a28068e8ca1f7cff42d1efa2d951dd"

SRC_URI_append_qemuall = "${SOURCE_SYSTEMD_PATCHES}/0001-core-device.c-Change-the-default-device-timeout-to-2.patch;downloadfilename=0001-core-device.c-Change-the-default-device-timeout-to-2.patch;name=0cdc-p"

PACKAGECONFIG ??= "xz \
                   ldconfig \
                   ${@bb.utils.contains('DISTRO_FEATURES', 'pam', 'pam', '', d)} \
                   ${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'xkbcommon', '', d)} \
                   ${@bb.utils.contains('DISTRO_FEATURES', 'selinux', 'selinux', '', d)} \
                   ${@bb.utils.contains('DISTRO_FEATURES', 'wifi', 'rfkill', '', d)} \
                   ${@bb.utils.contains('MACHINE_FEATURES', 'efi', 'efi', '', d)} \
                   binfmt \
                   randomseed \
                   machined \
                   backlight \
                   vconsole \
                   quotacheck \
                   hostnamed \
                   ${@bb.utils.contains('TCLIBC', 'glibc', 'myhostname sysusers', '', d)} \
                   hibernate \
                   timedated \
                   timesyncd \
                   localed \
                   ima \
                   smack \
                   logind \
                   firstboot \
                   utmp \
                   polkit \
"
# resolved
# networkd
PACKAGECONFIG_remove_libc-musl = "selinux"
PACKAGECONFIG_remove_libc-musl = "smack"

# remove timesyncd
PACKAGECONFIG_remove += "timesyncd"
#rb1.4 remove ima,machined,quotacheck,rfkill,backlight,binfmt
PACKAGECONFIG_8x96autogvmgh_remove += "ima machined quotacheck rfkill backlight binfmt"
PACKAGECONFIG_8x96autogvmga_remove += "ima machined quotacheck rfkill backlight binfmt"

# Use the upstream systemd serial-getty@.service and rely on
# systemd-getty-generator instead of using the OE-core specific
# systemd-serialgetty.bb - not enabled by default.
PACKAGECONFIG[serial-getty-generator] = ""

PACKAGECONFIG[journal-upload] = "--enable-libcurl,--disable-libcurl,curl"
# Sign the journal for anti-tampering
PACKAGECONFIG[gcrypt] = "--enable-gcrypt,--disable-gcrypt,libgcrypt"
PACKAGECONFIG[cryptsetup] = "--enable-libcryptsetup,--disable-libcryptsetup,cryptsetup"
PACKAGECONFIG[microhttpd] = "--enable-microhttpd,--disable-microhttpd,libmicrohttpd"
PACKAGECONFIG[elfutils] = "--enable-elfutils,--disable-elfutils,elfutils"
PACKAGECONFIG[resolved] = "--enable-resolved,--disable-resolved"
PACKAGECONFIG[networkd] = "--enable-networkd,--disable-networkd"
PACKAGECONFIG[machined] = "--enable-machined,--disable-machined"
PACKAGECONFIG[backlight] = "--enable-backlight,--disable-backlight"
PACKAGECONFIG[vconsole] = "--enable-vconsole,--disable-vconsole,,${PN}-vconsole-setup"
PACKAGECONFIG[quotacheck] = "--enable-quotacheck,--disable-quotacheck"
PACKAGECONFIG[hostnamed] = "--enable-hostnamed,--disable-hostnamed"
PACKAGECONFIG[myhostname] = "--enable-myhostname,--disable-myhostname"
PACKAGECONFIG[rfkill] = "--enable-rfkill,--disable-rfkill"
PACKAGECONFIG[hibernate] = "--enable-hibernate,--disable-hibernate"
PACKAGECONFIG[timedated] = "--enable-timedated,--disable-timedated"
PACKAGECONFIG[timesyncd] = "--enable-timesyncd,--disable-timesyncd"
PACKAGECONFIG[localed] = "--enable-localed,--disable-localed"
PACKAGECONFIG[efi] = "--enable-efi,--disable-efi"
PACKAGECONFIG[ima] = "--enable-ima,--disable-ima"
PACKAGECONFIG[smack] = "--enable-smack,--disable-smack"
# libseccomp is found in meta-security
PACKAGECONFIG[seccomp] = "--enable-seccomp,--disable-seccomp,libseccomp"
PACKAGECONFIG[logind] = "--enable-logind,--disable-logind"
PACKAGECONFIG[sysusers] = "--enable-sysusers,--disable-sysusers"
PACKAGECONFIG[firstboot] = "--enable-firstboot,--disable-firstboot"
PACKAGECONFIG[randomseed] = "--enable-randomseed,--disable-randomseed"
PACKAGECONFIG[binfmt] = "--enable-binfmt,--disable-binfmt"
PACKAGECONFIG[utmp] = "--enable-utmp,--disable-utmp"
PACKAGECONFIG[polkit] = "--enable-polkit,--disable-polkit"
# importd requires curl/xz/zlib/bzip2/gcrypt
PACKAGECONFIG[importd] = "--enable-importd,--disable-importd"
PACKAGECONFIG[libidn] = "--enable-libidn,--disable-libidn,libidn"
PACKAGECONFIG[audit] = "--enable-audit,--disable-audit,audit"
PACKAGECONFIG[manpages] = "--enable-manpages,--disable-manpages,libxslt-native xmlto-native docbook-xml-dtd4-native docbook-xsl-stylesheets-native"
PACKAGECONFIG[pam] = "--enable-pam,--disable-pam,libpam"
# Verify keymaps on locale change
PACKAGECONFIG[xkbcommon] = "--enable-xkbcommon,--disable-xkbcommon,libxkbcommon"
# Update NAT firewall rules
PACKAGECONFIG[iptc] = "--enable-libiptc,--disable-libiptc,iptables"
PACKAGECONFIG[ldconfig] = "--enable-ldconfig,--disable-ldconfig,,"
PACKAGECONFIG[selinux] = "--enable-selinux,--disable-selinux,libselinux"
PACKAGECONFIG[valgrind] = "ac_cv_header_valgrind_memcheck_h=yes ac_cv_header_valgrind_valgrind_h=yes ,ac_cv_header_valgrind_memcheck_h=no ac_cv_header_valgrind_valgrind_h=no ,valgrind"
PACKAGECONFIG[qrencode] = "--enable-qrencode,--disable-qrencode,qrencode"
PACKAGECONFIG[dbus] = "--enable-dbus,--disable-dbus,dbus"
PACKAGECONFIG[coredump] = "--enable-coredump,--disable-coredump"
PACKAGECONFIG[bzip2] = "--enable-bzip2,--disable-bzip2,bzip2"
PACKAGECONFIG[lz4] = "--enable-lz4,--disable-lz4,lz4"
PACKAGECONFIG[xz] = "--enable-xz,--disable-xz,xz"
PACKAGECONFIG[zlib] = "--enable-zlib,--disable-zlib,zlib"
PACKAGECONFIG[usrmerge] = "--disable-split-usr, --enable-split-usr"

CACHED_CONFIGUREVARS += "ac_cv_path_KILL=${base_bindir}/kill"
CACHED_CONFIGUREVARS += "ac_cv_path_KMOD=${base_bindir}/kmod"
CACHED_CONFIGUREVARS += "ac_cv_path_QUOTACHECK=${sbindir}/quotacheck"
CACHED_CONFIGUREVARS += "ac_cv_path_QUOTAON=${sbindir}/quotaon"
CACHED_CONFIGUREVARS += "ac_cv_path_SULOGIN=${base_sbindir}/sulogin"

# Helper variables to clarify locations.  This mirrors the logic in systemd's
# build system.
rootprefix ?= "${base_prefix}"
rootlibdir ?= "${base_libdir}"
rootlibexecdir = "${rootprefix}/lib"

CACHED_CONFIGUREVARS_class-target = "\
                         ac_cv_path_MOUNT_PATH=${base_bindir}/mount \
                         ac_cv_path_UMOUNT_PATH=${base_bindir}/umount \
                         ac_cv_path_KMOD=${base_bindir}/kmod \
                         ac_cv_path_KILL=${base_bindir}/kill \
                         ac_cv_path_SULOGIN=${base_sbindir}/sulogin \
                         ac_cv_path_KEXEC=${sbindir}/kexec \
                         ac_cv_path_QUOTACHECK=${sbindir}/quotacheck \
                         ac_cv_path_QUOTAON=${sbindir}/quotaon \
			 "

EXTRA_OECONF = " --with-rootprefix=${rootprefix} \
                 --with-rootlibdir=${rootlibdir} \
                 --with-roothomedir=${ROOT_HOME} \
                 --without-python \
                 --with-sysvrcnd-path=${sysconfdir} \
                 --with-firmware-path=${nonarch_base_libdir}/firmware \
                 --with-testdir=${PTEST_PATH} \
               "
# per the systemd README, define VALGRIND=1 to run under valgrind
CFLAGS .= "${@bb.utils.contains('PACKAGECONFIG', 'valgrind', ' -DVALGRIND=1', '', d)}"

# disable problematic GCC 5.2 optimizations [YOCTO #8291]
FULL_OPTIMIZATION_append_arm = " -fno-schedule-insns -fno-schedule-insns2"

# Avoid login failure on qemumips64 when pam is enabled
FULL_OPTIMIZATION_append_mips64 = " -fno-tree-switch-conversion -fno-tree-tail-merge"

COMPILER_NM ?= "${HOST_PREFIX}gcc-nm"
COMPILER_AR ?= "${HOST_PREFIX}gcc-ar"
COMPILER_RANLIB ?= "${HOST_PREFIX}gcc-ranlib"

do_configure_prepend() {
	export NM="${COMPILER_NM}"
	export AR="${COMPILER_AR}"
	export RANLIB="${COMPILER_RANLIB}"
	export KMOD="${base_bindir}/kmod"
	if [ -d ${S}/units.pre_sed ] ; then
		cp -r ${S}/units.pre_sed ${S}/units
	else
		cp -r ${S}/units ${S}/units.pre_sed
	fi
	sed -i -e 's:-DTEST_DIR=\\\".*\\\":-DTEST_DIR=\\\"${PTEST_PATH}/tests/test\\\":' ${S}/Makefile.am
	sed -i -e 's:-DCATALOG_DIR=\\\".*\\\":-DCATALOG_DIR=\\\"${PTEST_PATH}/tests/catalog\\\":' ${S}/Makefile.am
}

do_install() {
	autotools_do_install
	install -d ${D}/${base_sbindir}
	if ${@bb.utils.contains('PACKAGECONFIG', 'serial-getty-generator', 'false', 'true', d)}; then
		# Provided by a separate recipe
		rm ${D}${systemd_unitdir}/system/serial-getty* -f
	fi

	if ${@base_conditional('BASEMACHINE', '8x96autogvmquintcu', 'true', 'false', d)}; then
		# Do not need to fix the wlanocb0 to wlan0 for dsrc support on 8x96autogvmquintcu
		sed -i -e 's/KERNEL=="wlan\*"/KERNEL=="wlan0"/g' ${WORKDIR}/70-net-setup-link.rules
	fi

	# Provide support for initramfs
	[ ! -e ${D}/init ] && ln -s ${rootlibexecdir}/systemd/systemd ${D}/init
	[ ! -e ${D}/${base_sbindir}/udevd ] && ln -s ${rootlibexecdir}/systemd/systemd-udevd ${D}/${base_sbindir}/udevd

	# Create machine-id
	# 20:12 < mezcalero> koen: you have three options: a) run systemd-machine-id-setup at install time, b) have / read-only and an empty file there (for stateless) and c) boot with / writable
	touch ${D}${sysconfdir}/machine-id


	install -d ${D}${sysconfdir}/udev/rules.d/
	install -d ${D}${sysconfdir}/tmpfiles.d
	install -m 0644 ${WORKDIR}/*.rules ${D}${sysconfdir}/udev/rules.d/
	install -d ${D}${libdir}/pkgconfig
	install -m 0644 ${B}/src/udev/udev.pc ${D}${libdir}/pkgconfig/

	install -m 0644 ${WORKDIR}/00-create-volatile.conf ${D}${sysconfdir}/tmpfiles.d/

	if ${@bb.utils.contains('DISTRO_FEATURES','sysvinit','true','false',d)}; then
		install -d ${D}${sysconfdir}/init.d
		install -m 0755 ${WORKDIR}/init ${D}${sysconfdir}/init.d/systemd-udevd
		sed -i s%@UDEVD@%${rootlibexecdir}/systemd/systemd-udevd% ${D}${sysconfdir}/init.d/systemd-udevd
	fi

	chown root:systemd-journal ${D}/${localstatedir}/log/journal

	# Delete journal README, as log can be symlinked inside volatile.
	rm -f ${D}/${localstatedir}/log/README

	install -d ${D}${systemd_unitdir}/system/graphical.target.wants
	install -d ${D}${systemd_unitdir}/system/multi-user.target.wants
	install -d ${D}${systemd_unitdir}/system/poweroff.target.wants
	install -d ${D}${systemd_unitdir}/system/reboot.target.wants
	install -d ${D}${systemd_unitdir}/system/rescue.target.wants

	# Create symlinks for systemd-update-utmp-runlevel.service
	if ${@bb.utils.contains('PACKAGECONFIG', 'utmp', 'true', 'false', d)}; then
		ln -sf ../systemd-update-utmp-runlevel.service ${D}${systemd_unitdir}/system/graphical.target.wants/systemd-update-utmp-runlevel.service
		ln -sf ../systemd-update-utmp-runlevel.service ${D}${systemd_unitdir}/system/multi-user.target.wants/systemd-update-utmp-runlevel.service
		ln -sf ../systemd-update-utmp-runlevel.service ${D}${systemd_unitdir}/system/poweroff.target.wants/systemd-update-utmp-runlevel.service
		ln -sf ../systemd-update-utmp-runlevel.service ${D}${systemd_unitdir}/system/reboot.target.wants/systemd-update-utmp-runlevel.service
		ln -sf ../systemd-update-utmp-runlevel.service ${D}${systemd_unitdir}/system/rescue.target.wants/systemd-update-utmp-runlevel.service
	fi

	# Enable journal to forward message to syslog daemon
	sed -i -e 's/.*ForwardToSyslog.*/ForwardToSyslog=yes/' ${D}${sysconfdir}/systemd/journald.conf
	# Set the maximium size of runtime journal to 512k as default
	sed -i -e 's/.*RuntimeMaxUse.*/RuntimeMaxUse=512K/' ${D}${sysconfdir}/systemd/journald.conf

	# this file is needed to exist if networkd is disabled but timesyncd is still in use since timesyncd checks it
	# for existence else it fails
	if [ -s ${D}${exec_prefix}/lib/tmpfiles.d/systemd.conf ]; then
		${@bb.utils.contains('PACKAGECONFIG', 'networkd', ':', 'sed -i -e "\$ad /run/systemd/netif/links 0755 root root -" ${D}${exec_prefix}/lib/tmpfiles.d/systemd.conf', d)}
	fi
	if ! ${@bb.utils.contains('PACKAGECONFIG', 'resolved', 'true', 'false', d)}; then
		echo 'L! ${sysconfdir}/resolv.conf - - - - ../run/systemd/resolve/resolv.conf' >>${D}${exec_prefix}/lib/tmpfiles.d/etc.conf
		echo 'd /run/systemd/resolve 0755 root root -' >>${D}${exec_prefix}/lib/tmpfiles.d/systemd.conf
		echo 'f /run/systemd/resolve/resolv.conf 0644 root root' >>${D}${exec_prefix}/lib/tmpfiles.d/systemd.conf
		ln -s ../run/systemd/resolve/resolv.conf ${D}${sysconfdir}/resolv-conf.systemd
	else
		sed -i -e "s%^L! /etc/resolv.conf.*$%L! /etc/resolv.conf - - - - ../run/systemd/resolve/resolv.conf%g" ${D}${exec_prefix}/lib/tmpfiles.d/etc.conf
		ln -s ../run/systemd/resolve/resolv.conf ${D}${sysconfdir}/resolv-conf.systemd
	fi
	install -Dm 0755 ${S}/src/systemctl/systemd-sysv-install.SKELETON ${D}${systemd_unitdir}/systemd-sysv-install
}

do_install_ptest () {
       # install data files needed for tests
       install -d ${D}${PTEST_PATH}/tests/test
       cp -rfL ${S}/test/* ${D}${PTEST_PATH}/tests/test
       # python is disabled for systemd, thus removing these python testing scripts
       rm ${D}${PTEST_PATH}/tests/test/*.py
       sed -i 's/"tree"/"ls"/' ${D}${PTEST_PATH}/tests/test/udev-test.pl

       install -d ${D}${PTEST_PATH}/tests/catalog
       install ${S}/catalog/* ${D}${PTEST_PATH}/tests/catalog/

       install -D ${S}/build-aux/test-driver ${D}${PTEST_PATH}/tests/build-aux/test-driver

       install -d ${D}${PTEST_PATH}/tests/rules
       install ${B}/rules/* ${D}${PTEST_PATH}/tests/rules/

       # This directory needs to be there for udev-test.pl to work.
       install -d ${D}${libdir}/udev/rules.d

       # install actual test binaries
       install -m 0755 ${B}/test-* ${D}${PTEST_PATH}/tests/
       install -m 0755 ${B}/.libs/test-* ${D}${PTEST_PATH}/tests/

       install ${B}/Makefile ${D}${PTEST_PATH}/tests/
}

python populate_packages_prepend (){
    systemdlibdir = d.getVar("rootlibdir", True)
    do_split_packages(d, systemdlibdir, '^lib(.*)\.so\.*', 'lib%s', 'Systemd %s library', extra_depends='', allow_links=True)
}
PACKAGES_DYNAMIC += "^lib(udev|systemd|nss).*"

PACKAGES =+ "\
    ${PN}-gui \
    ${PN}-vconsole-setup \
    ${PN}-initramfs \
    ${PN}-analyze \
    ${PN}-kernel-install \
    ${PN}-rpm-macros \
    ${PN}-binfmt \
    ${PN}-pam \
    ${PN}-zsh-completion \
    ${PN}-xorg-xinitrc \
    ${PN}-container \
    ${PN}-extra-utils \
"

SUMMARY_${PN}-container = "Tools for containers and VMs"
DESCRIPTION_${PN}-container = "Systemd tools to spawn and manage containers and virtual machines."

SYSTEMD_PACKAGES = "${@bb.utils.contains('PACKAGECONFIG', 'binfmt', '${PN}-binfmt', '', d)}"
SYSTEMD_SERVICE_${PN}-binfmt = "systemd-binfmt.service"

USERADD_PACKAGES = "${PN} ${PN}-extra-utils"
USERADD_PARAM_${PN} += "${@bb.utils.contains('PACKAGECONFIG', 'microhttpd', '--system -d / -M --shell /bin/nologin systemd-journal-gateway;', '', d)}"
USERADD_PARAM_${PN} += "${@bb.utils.contains('PACKAGECONFIG', 'microhttpd', '--system -d / -M --shell /bin/nologin systemd-journal-remote;', '', d)}"
USERADD_PARAM_${PN} += "${@bb.utils.contains('PACKAGECONFIG', 'journal-upload', '--system -d / -M --shell /bin/nologin systemd-journal-upload;', '', d)}"
USERADD_PARAM_${PN} += "${@bb.utils.contains('PACKAGECONFIG', 'timesyncd', '--system -d / -M --shell /bin/nologin systemd-timesync;', '', d)}"
USERADD_PARAM_${PN} += "${@bb.utils.contains('PACKAGECONFIG', 'networkd', '--system -d / -M --shell /bin/nologin systemd-network;', '', d)}"
USERADD_PARAM_${PN} += "${@bb.utils.contains('PACKAGECONFIG', 'coredump', '--system -d / -M --shell /bin/nologin systemd-coredump;', '', d)}"
USERADD_PARAM_${PN} += "${@bb.utils.contains('PACKAGECONFIG', 'resolved', '--system -d / -M --shell /bin/nologin systemd-resolve;', '', d)}"
GROUPADD_PARAM_${PN} = "-r lock; -r systemd-journal"
USERADD_PARAM_${PN}-extra-utils += "--system -d / -M --shell /bin/nologin systemd-bus-proxy;"

FILES_${PN}-analyze = "${bindir}/systemd-analyze"

FILES_${PN}-initramfs = "/init"
RDEPENDS_${PN}-initramfs = "${PN}"

RDEPENDS_${PN}-ptest += "gawk make perl bash xz \
                         tzdata tzdata-americas tzdata-asia \
                         tzdata-europe tzdata-africa tzdata-antarctica \
                         tzdata-arctic tzdata-atlantic tzdata-australia \
                         tzdata-pacific tzdata-posix"

FILES_${PN}-ptest += "${libdir}/udev/rules.d"

FILES_${PN}-gui = "${bindir}/systemadm"

FILES_${PN}-vconsole-setup = "${rootlibexecdir}/systemd/systemd-vconsole-setup \
                              ${systemd_unitdir}/system/systemd-vconsole-setup.service \
                              ${systemd_unitdir}/system/sysinit.target.wants/systemd-vconsole-setup.service"

RDEPENDS_${PN}-kernel-install += "bash"
FILES_${PN}-kernel-install = "${bindir}/kernel-install \
                              ${sysconfdir}/kernel/ \
                              ${exec_prefix}/lib/kernel \
                             "
FILES_${PN}-rpm-macros = "${exec_prefix}/lib/rpm \
                         "

FILES_${PN}-xorg-xinitrc = "${sysconfdir}/X11/xinit/xinitrc.d/*"

FILES_${PN}-zsh-completion = "${datadir}/zsh/site-functions"

FILES_${PN}-binfmt = "${sysconfdir}/binfmt.d/ \
                      ${exec_prefix}/lib/binfmt.d \
                      ${rootlibexecdir}/systemd/systemd-binfmt \
                      ${systemd_unitdir}/system/proc-sys-fs-binfmt_misc.* \
                      ${systemd_unitdir}/system/systemd-binfmt.service"
RRECOMMENDS_${PN}-binfmt = "kernel-module-binfmt-misc"

RRECOMMENDS_${PN}-vconsole-setup = "kbd kbd-consolefonts kbd-keymaps"

FILES_${PN}-container = "${sysconfdir}/dbus-1/system.d/org.freedesktop.import1.conf \
                         ${sysconfdir}/dbus-1/system.d/org.freedesktop.machine1.conf \
                         ${base_bindir}/machinectl \
                         ${bindir}/systemd-nspawn \
                         ${nonarch_libdir}/systemd/import-pubring.gpg \
                         ${systemd_system_unitdir}/busnames.target.wants/org.freedesktop.import1.busname \
                         ${systemd_system_unitdir}/busnames.target.wants/org.freedesktop.machine1.busname \
                         ${systemd_system_unitdir}/local-fs.target.wants/var-lib-machines.mount \
                         ${systemd_system_unitdir}/machine.slice \
                         ${systemd_system_unitdir}/machines.target \
                         ${systemd_system_unitdir}/org.freedesktop.import1.busname \
                         ${systemd_system_unitdir}/org.freedesktop.machine1.busname \
                         ${systemd_system_unitdir}/systemd-importd.service \
                         ${systemd_system_unitdir}/systemd-machined.service \
                         ${systemd_system_unitdir}/dbus-org.freedesktop.machine1.service \
                         ${systemd_system_unitdir}/var-lib-machines.mount \
                         ${rootlibexecdir}/systemd/systemd-import \
                         ${rootlibexecdir}/systemd/systemd-importd \
                         ${rootlibexecdir}/systemd/systemd-journal-gatewayd \
                         ${rootlibexecdir}/systemd/systemd-journal-remote \
                         ${rootlibexecdir}/systemd/systemd-journal-upload \
                         ${rootlibexecdir}/systemd/systemd-machined \
                         ${rootlibexecdir}/systemd/systemd-pull \
                         ${exec_prefix}/lib/tmpfiles.d/systemd-nspawn.conf \
                         ${systemd_system_unitdir}/systemd-nspawn@.service \
                         ${libdir}/libnss_mymachines.so.2 \
                         ${datadir}/dbus-1/system-services/org.freedesktop.import1.service \
                         ${datadir}/dbus-1/system-services/org.freedesktop.machine1.service \
                         ${datadir}/polkit-1/actions/org.freedesktop.import1.policy \
                         ${datadir}/polkit-1/actions/org.freedesktop.machine1.policy \
                        "

FILES_${PN}-extra-utils = "\
                        ${base_bindir}/systemd-escape \
                        ${base_bindir}/systemd-inhibit \
                        ${bindir}/systemd-detect-virt \
                        ${bindir}/systemd-path \
                        ${bindir}/systemd-run \
                        ${bindir}/systemd-cat \
                        ${bindir}/systemd-delta \
                        ${bindir}/systemd-cgls \
                        ${bindir}/systemd-cgtop \
                        ${bindir}/systemd-stdio-bridge \
                        ${base_bindir}/systemd-ask-password \
                        ${base_bindir}/systemd-tty-ask-password-agent \
                        ${systemd_unitdir}/system/systemd-ask-password-console.path \
                        ${systemd_unitdir}/system/systemd-ask-password-console.service \
                        ${systemd_unitdir}/system/systemd-ask-password-wall.path \
                        ${systemd_unitdir}/system/systemd-ask-password-wall.service \
                        ${systemd_unitdir}/system/sysinit.target.wants/systemd-ask-password-console.path \
                        ${systemd_unitdir}/system/sysinit.target.wants/systemd-ask-password-wall.path \
                        ${systemd_unitdir}/system/multi-user.target.wants/systemd-ask-password-wall.path \
                        ${rootlibexecdir}/systemd/systemd-resolve-host \
                        ${rootlibexecdir}/systemd/systemd-ac-power \
                        ${rootlibexecdir}/systemd/systemd-activate \
                        ${rootlibexecdir}/systemd/systemd-bus-proxyd \
                        ${systemd_unitdir}/system/systemd-bus-proxyd.service \
                        ${systemd_unitdir}/system/systemd-bus-proxyd.socket \
                        ${rootlibexecdir}/systemd/systemd-socket-proxyd \
                        ${rootlibexecdir}/systemd/systemd-reply-password \
                        ${rootlibexecdir}/systemd/systemd-sleep \
                        ${rootlibexecdir}/systemd/system-sleep \
                        ${systemd_unitdir}/system/systemd-hibernate.service \
                        ${systemd_unitdir}/system/systemd-hybrid-sleep.service \
                        ${systemd_unitdir}/system/systemd-suspend.service \
                        ${systemd_unitdir}/system/sleep.target \
                        ${rootlibexecdir}/systemd/systemd-initctl \
                        ${systemd_unitdir}/system/systemd-initctl.service \
                        ${systemd_unitdir}/system/systemd-initctl.socket \
                        ${systemd_unitdir}/system/sockets.target.wants/systemd-initctl.socket \
                        ${rootlibexecdir}/systemd/system-generators/systemd-gpt-auto-generator \
                        ${rootlibexecdir}/systemd/systemd-cgroups-agent \
"

CONFFILES_${PN} = "${sysconfdir}/machine-id \
                ${sysconfdir}/systemd/coredump.conf \
                ${sysconfdir}/systemd/journald.conf \
                ${sysconfdir}/systemd/logind.conf \
                ${sysconfdir}/systemd/system.conf \
                ${sysconfdir}/systemd/user.conf"

FILES_${PN} = " ${base_bindir}/* \
                ${datadir}/dbus-1/services \
                ${datadir}/dbus-1/system-services \
                ${datadir}/polkit-1 \
                ${datadir}/${BPN} \
                ${datadir}/factory \
                ${sysconfdir}/dbus-1/ \
                ${sysconfdir}/machine-id \
                ${sysconfdir}/modules-load.d/ \
                ${sysconfdir}/pam.d/ \
                ${sysconfdir}/sysctl.d/ \
                ${sysconfdir}/systemd/ \
                ${sysconfdir}/tmpfiles.d/ \
                ${sysconfdir}/xdg/ \
                ${sysconfdir}/init.d/README \
                ${sysconfdir}/resolv-conf.systemd \
                ${rootlibexecdir}/systemd/* \
                ${systemd_unitdir}/* \
                ${base_libdir}/security/*.so \
                /cgroup \
                ${bindir}/systemd* \
                ${bindir}/busctl \
                ${bindir}/coredumpctl \
                ${bindir}/localectl \
                ${bindir}/hostnamectl \
                ${bindir}/timedatectl \
                ${bindir}/bootctl \
                ${bindir}/kernel-install \
                ${exec_prefix}/lib/tmpfiles.d/*.conf \
                ${exec_prefix}/lib/systemd \
                ${exec_prefix}/lib/modules-load.d \
                ${exec_prefix}/lib/sysctl.d \
                ${exec_prefix}/lib/sysusers.d \
                ${localstatedir} \
                ${nonarch_base_libdir}/udev/rules.d/70-uaccess.rules \
                ${nonarch_base_libdir}/udev/rules.d/71-seat.rules \
                ${nonarch_base_libdir}/udev/rules.d/73-seat-late.rules \
                ${nonarch_base_libdir}/udev/rules.d/99-systemd.rules \
               "

FILES_${PN}-dev += "${base_libdir}/security/*.la ${datadir}/dbus-1/interfaces/ ${sysconfdir}/rpm/macros.systemd"

RDEPENDS_${PN} += "kmod dbus util-linux-mount udev (= ${EXTENDPKGV})"
RDEPENDS_${PN} += "volatile-binds update-rc.d"

RRECOMMENDS_${PN} += "${@bb.utils.contains('PACKAGECONFIG', 'serial-getty-generator', '', 'systemd-serialgetty', d)} \
                      systemd-extra-utils \
                      systemd-compat-units udev-hwdb \
                      util-linux-agetty  util-linux-fsck e2fsprogs-e2fsck \
                      kernel-module-autofs4 kernel-module-unix kernel-module-ipv6 \
                      os-release \
"

INSANE_SKIP_${PN} += "dev-so libdir"
INSANE_SKIP_${PN}-dbg += "libdir"
INSANE_SKIP_${PN}-doc += " libdir"

PACKAGES =+ "udev udev-hwdb"

RPROVIDES_udev = "hotplug"

RDEPENDS_udev-hwdb += "udev"

FILES_udev += "${base_sbindir}/udevd \
               ${rootlibexecdir}/systemd/systemd-udevd \
               ${rootlibexecdir}/udev/accelerometer \
               ${rootlibexecdir}/udev/ata_id \
               ${rootlibexecdir}/udev/cdrom_id \
               ${rootlibexecdir}/udev/collect \
               ${rootlibexecdir}/udev/findkeyboards \
               ${rootlibexecdir}/udev/keyboard-force-release.sh \
               ${rootlibexecdir}/udev/keymap \
               ${rootlibexecdir}/udev/mtd_probe \
               ${rootlibexecdir}/udev/scsi_id \
               ${rootlibexecdir}/udev/v4l_id \
               ${rootlibexecdir}/udev/keymaps \
               ${rootlibexecdir}/udev/rules.d/*.rules \
               ${sysconfdir}/udev \
               ${sysconfdir}/init.d/systemd-udevd \
               ${systemd_unitdir}/system/*udev* \
               ${systemd_unitdir}/system/*.wants/*udev* \
               ${base_bindir}/udevadm \
               ${datadir}/bash-completion/completions/udevadm \
              "

FILES_udev-hwdb = "${rootlibexecdir}/udev/hwdb.d"

INITSCRIPT_PACKAGES = "udev"
INITSCRIPT_NAME_udev = "systemd-udevd"
INITSCRIPT_PARAMS_udev = "start 03 S ."

python __anonymous() {
    if not bb.utils.contains('DISTRO_FEATURES', 'sysvinit', True, False, d):
        d.setVar("INHIBIT_UPDATERCD_BBCLASS", "1")
}

# TODO:
# u-a for runlevel and telinit

ALTERNATIVE_${PN} = "init halt reboot shutdown poweroff runlevel resolv-conf"

ALTERNATIVE_TARGET[init] = "${rootlibexecdir}/systemd/systemd"
ALTERNATIVE_LINK_NAME[init] = "${base_sbindir}/init"
ALTERNATIVE_PRIORITY[init] ?= "300"

ALTERNATIVE_TARGET[halt] = "${base_bindir}/systemctl"
ALTERNATIVE_LINK_NAME[halt] = "${base_sbindir}/halt"
ALTERNATIVE_PRIORITY[halt] ?= "300"

ALTERNATIVE_TARGET[reboot] = "${base_bindir}/systemctl"
ALTERNATIVE_LINK_NAME[reboot] = "${base_sbindir}/reboot"
ALTERNATIVE_PRIORITY[reboot] ?= "300"

ALTERNATIVE_TARGET[shutdown] = "${base_bindir}/systemctl"
ALTERNATIVE_LINK_NAME[shutdown] = "${base_sbindir}/shutdown"
ALTERNATIVE_PRIORITY[shutdown] ?= "300"

ALTERNATIVE_TARGET[poweroff] = "${base_bindir}/systemctl"
ALTERNATIVE_LINK_NAME[poweroff] = "${base_sbindir}/poweroff"
ALTERNATIVE_PRIORITY[poweroff] ?= "300"

ALTERNATIVE_TARGET[runlevel] = "${base_bindir}/systemctl"
ALTERNATIVE_LINK_NAME[runlevel] = "${base_sbindir}/runlevel"
ALTERNATIVE_PRIORITY[runlevel] ?= "300"

ALTERNATIVE_TARGET[resolv-conf] = "${sysconfdir}/resolv-conf.systemd"
ALTERNATIVE_LINK_NAME[resolv-conf] = "${sysconfdir}/resolv.conf"
ALTERNATIVE_PRIORITY[resolv-conf] ?= "50"

pkg_postinst_${PN} () {
	sed -e '/^hosts:/s/\s*\<myhostname\>//' \
		-e 's/\(^hosts:.*\)\(\<files\>\)\(.*\)\(\<dns\>\)\(.*\)/\1\2 myhostname \3\4\5/' \
		-i $D${sysconfdir}/nsswitch.conf
}

pkg_prerm_${PN} () {
	sed -e '/^hosts:/s/\s*\<myhostname\>//' \
		-e '/^hosts:/s/\s*myhostname//' \
		-i $D${sysconfdir}/nsswitch.conf
}

PACKAGE_WRITE_DEPS += "qemu-native"
pkg_postinst_udev-hwdb () {
	if test -n "$D"; then
		${@qemu_run_binary(d, '$D', '${base_bindir}/udevadm')} hwdb --update \
			--root $D
		chown root:root $D${sysconfdir}/udev/hwdb.bin
	else
		udevadm hwdb --update
	fi
}

pkg_prerm_udev-hwdb () {
	rm -f $D${sysconfdir}/udev/hwdb.bin
}

# As this recipe builds udev, respect systemd being in DISTRO_FEATURES so
# that we don't build both udev and systemd in world builds.
python () {
    if not bb.utils.contains ('DISTRO_FEATURES', 'systemd', True, False, d):
        raise bb.parse.SkipPackage("'systemd' not in DISTRO_FEATURES")

    import re
    if re.match('.*musl*', d.getVar('TARGET_OS', True)) != None:
        raise bb.parse.SkipPackage("Not _yet_ supported on musl based targets")
}
