SUMMARY = "The Python Programming Language"
HOMEPAGE = "http://www.python.org"
DESCRIPTION = "Python is a programming language that lets you work more quickly and integrate your systems more effectively."
LICENSE = "PSF-2.0 & BSD-0-Clause"
SECTION = "devel/python"

LIC_FILES_CHKSUM = "file://LICENSE;md5=c84eccf626bb6fde43e6ea5e28d8feb5"

BASE_URL = "https://git.yoctoproject.org/poky/plain/meta/recipes-devtools/python/python3"
BRANCH = "?h=dunfell"

SRC_URI = "http://www.python.org/ftp/python/${PV}/Python-${PV}.tar.xz;md5sum=c4b7100dcaace9d33ab1fda9a3a038d6;sha256sum=6f309077012040aa39fe8f0c61db8c0fa1c45136763299d375c9e5756f09cf57 \
           file://python3-manifest.json \
           ${BASE_URL}/run-ptest${BRANCH};sha256sum=4e789902f48f4ff8f432eb3d3bc4fdc6bf785ea4f5fd3ceb0764d92cb2a2410d;downloadfilename=run-ptest \
           ${BASE_URL}/create_manifest3.py${BRANCH};sha256sum=cc451c19240fa45e54a1eef0633f3c170c7cb1073665efd661b756afdbed0fac;downloadfilename=create_manifest3.p \
           ${BASE_URL}/get_module_deps3.py${BRANCH};sha256sum=76d9226c82cd962b4a5b76cbe7baf551faf709051be15a76c5b020962c77c872;downloadfilename=get_module_deps3.py \
           ${BASE_URL}/check_build_completeness.py${BRANCH};sha256sum=e7ace527d1415a48ffa23a3ecd4449a5f70990bcd6deb214fef5d7e3c8f95e98;downloadfilename=check_build_completeness.py \
           ${BASE_URL}/cgi_py.patch${BRANCH};sha256sum=02d9f4d619cd693d6cdd45150ef31c58f91375566cdf7e86f296642225e144eb;downloadfilename=cgi_py.patch \
           ${BASE_URL}/0001-Do-not-add-usr-lib-termcap-to-linker-flags-to-avoid-.patch${BRANCH};sha256sum=f65768159be36a7dbefbf5a33904c6b4b1553a420d602492cad2c93642b31ba5;downloadfilename=0001-Do-not-add-usr-lib-termcap-to-linker-flags-to-avoid-.patch \
           ${@bb.utils.contains('PACKAGECONFIG', 'tk', '', '${BASE_URL}/avoid_warning_about_tkinter.patch${BRANCH};sha256sum=c30ea0bf16f2479a4c4fb87077f49841c27384e5d6c13291841f6cf29408f81a;downloadfilename=avoid_warning_about_tkinter.patch', d)} \
           ${BASE_URL}/0001-Do-not-use-the-shell-version-of-python-config-that-w.patch${BRANCH};sha256sum=bfbaa926a8e4bad4b7aa79ae3dcd416ad2d6ed3b516544bd20f437f1a790a9f3;downloadfilename=0001-Do-not-use-the-shell-version-of-python-config-that-w.patch \
           ${BASE_URL}/python-config.patch${BRANCH};sha256sum=91d41af85820f61be94776c3a7d981903476facf5f6f70da7245beb9f7cfc439;downloadfilename=python-config.patch \
           ${BASE_URL}/0001-Makefile.pre-use-qemu-wrapper-when-gathering-profile.patch${BRANCH};sha256sum=7005e1b662da6f7aff6080f947046aed9447d29139fbdcefe52822ed26aa780b;downloadfilename=0001-Makefile.pre-use-qemu-wrapper-when-gathering-profile.patch \
           ${BASE_URL}/0001-Do-not-hardcode-lib-as-location-for-site-packages-an.patch${BRANCH};sha256sum=9ae7b8f0acae90899bad0b8fc4b0c125010d4d29b5fb0cd6ddc5a4cc119b5633;downloadfilename=0001-Do-not-hardcode-lib-as-location-for-site-packages-an.patch \
           ${BASE_URL}/0001-python3-use-cc_basename-to-replace-CC-for-checking-c.patch${BRANCH};sha256sum=5a2103340023c112ffddcbc44f35dd7e67373dcf12260613ca62c83e3d203d79;downloadfilename=0001-python3-use-cc_basename-to-replace-CC-for-checking-c.patch \
           ${BASE_URL}/0001-Lib-sysconfig.py-fix-another-place-where-lib-is-hard.patch${BRANCH};sha256sum=9bd47c67b45c84eabd67f37210d3e68aa515746351a52dedb59d3ac4425cc5c8;downloadfilename=0001-Lib-sysconfig.py-fix-another-place-where-lib-is-hard.patch \
           ${BASE_URL}/0001-Makefile-fix-Issue36464-parallel-build-race-problem.patch${BRANCH};sha256sum=f00bd87b2a71e10cb0de952cdb37a925bde7a104a8a5db3be218381a4c0a7b5c;downloadfilename=0001-Makefile-fix-Issue36464-parallel-build-race-problem.patch \
           ${BASE_URL}/0001-bpo-36852-proper-detection-of-mips-architecture-for-.patch${BRANCH};sha256sum=b40b98cfbff9fcad7702c2377c3cfbb169360e446d9ff28f8d4b916ff7b60919;downloadfilename=0001-bpo-36852-proper-detection-of-mips-architecture-for-.patch \
           ${BASE_URL}/crosspythonpath.patch${BRANCH};sha256sum=f0a2b89e78ee4aaf26681e38c2ef90a75023ad2d46e217828c7611ee74ff64a3;downloadfilename=crosspythonpath.patch \
           ${BASE_URL}/reformat_sysconfig.py${BRANCH};sha256sum=910108c4554b681e596d6fc1e02401c5931ca0a24990bc6581a5dce680a68405;downloadfilename=reformat_sysconfig.py \
           ${BASE_URL}/0001-Use-FLAG_REF-always-for-interned-strings.patch${BRANCH};sha256sum=aca8d006743419f95db5920872feba311bfd98813d9d8e960f85e070ec81d918;downloadfilename=0001-Use-FLAG_REF-always-for-interned-strings.patch \
           ${BASE_URL}/0001-test_locale.py-correct-the-test-output-format.patch${BRANCH};sha256sum=d22805c6e88a2c9df76ae8c30e32fd6a1623a567415a603591fe1d9522512a9f;downloadfilename=0001-test_locale.py-correct-the-test-output-format.patch \
           ${BASE_URL}/0017-setup.py-do-not-report-missing-dependencies-for-disa.patch${BRANCH};sha256sum=3b683e21787635aaef9b1877ee418d24524f92cf4c43e9d4d5bc78ff02e49410;downloadfilename=0017-setup.py-do-not-report-missing-dependencies-for-disa.patch \
           ${BASE_URL}/0001-setup.py-pass-missing-libraries-to-Extension-for-mul.patch${BRANCH};sha256sum=5eb0d60710331aec29c201e087d03375edcb53513f1902e55fe76a15d9cebc44;downloadfilename=0001-setup.py-pass-missing-libraries-to-Extension-for-mul.patch \
           ${BASE_URL}/0001-Makefile-do-not-compile-.pyc-in-parallel.patch${BRANCH};sha256sum=4d37fc7b5c3cf2398c876fa49d6daf353cd80792d739edc9131e9ff48f53ab54;downloadfilename=0001-Makefile-do-not-compile-.pyc-in-parallel.patch \
           ${BASE_URL}/0001-configure.ac-fix-LIBPL.patch${BRANCH};sha256sum=07fd5b3c87613d3f0a0e8d4fb35e5d78d1ade3f25615586d39b903c142d41844;downloadfilename=0001-configure.ac-fix-LIBPL.patch \
           ${BASE_URL}/0001-python3-Do-not-hardcode-lib-for-distutils.patch${BRANCH};sha256sum=52c6e6734d6db13df65e620b294f4ae852d42659f45bd379f0865445e5dffbb9;downloadfilename=0001-python3-Do-not-hardcode-lib-for-distutils.patch \
           ${BASE_URL}/0020-configure.ac-setup.py-do-not-add-a-curses-include-pa.patch${BRANCH};sha256sum=c6902cf468daa3390d36ebafb430422adb2e60a4c059f446bfd39abbe66cb1f3;downloadfilename=0020-configure.ac-setup.py-do-not-add-a-curses-include-pa.patch \
           ${BASE_URL}/makerace.patch${BRANCH};sha256sum=834e6a9c812b07a43ba951a30cb1a76610c504c4060d83d4bbe7b685d380652f;downloadfilename=makerace.patch \
           "

SRC_URI_append_class-native = " \
           ${BASE_URL}/0001-distutils-sysconfig-append-STAGING_LIBDIR-python-sys.patch${BRANCH};sha256sum=dc090cde5d5fd5777cb9126e3447e67a73ef16e427a96f1a7674c157ce2583dc;downloadfilename=0001-distutils-sysconfig-append-STAGING_LIBDIR-python-sys.patch \
           ${BASE_URL}/12-distutils-prefix-is-inside-staging-area.patch${BRANCH};sha256sum=c4e86dee745c729baf3ed33b0c6c123eff2f6ad670ab55bb55fe5e5f385b5519;downloadfilename=12-distutils-prefix-is-inside-staging-area.patch \
           ${BASE_URL}/0001-Don-t-search-system-for-headers-libraries.patch${BRANCH};sha256sum=3015efa88b11a72981ae4c8db40b63ed00c3f5fc4f140cd789a02393b3e5bd44;downloadfilename=0001-Don-t-search-system-for-headers-libraries.patch \
           "

# exclude pre-releases for both python 2.x and 3.x
UPSTREAM_CHECK_REGEX = "[Pp]ython-(?P<pver>\d+(\.\d+)+).tar"

CVE_PRODUCT = "python"

# Upstream consider this expected behaviour
CVE_CHECK_WHITELIST += "CVE-2007-4559"
# This is not exploitable when glibc has CVE-2016-10739 fixed.
CVE_CHECK_WHITELIST += "CVE-2019-18348"

# This is windows only issue.
CVE_CHECK_WHITELIST += "CVE-2020-15523 CVE-2022-26488"
# The mailcap module is insecure by design, so this can't be fixed in a meaningful way.
# The module will be removed in the future and flaws documented.
CVE_CHECK_WHITELIST += "CVE-2015-20107"

PYTHON_MAJMIN = "3.8"

S = "${WORKDIR}/Python-${PV}"

BBCLASSEXTEND = "native nativesdk"

inherit autotools pkgconfig qemu ptest multilib_header update-alternatives

MULTILIB_SUFFIX = "${@d.getVar('base_libdir',1).split('/')[-1]}"

ALTERNATIVE_${PN}-dev = "python3-config"
ALTERNATIVE_LINK_NAME[python3-config] = "${bindir}/python${PYTHON_MAJMIN}-config"
ALTERNATIVE_TARGET[python3-config] = "${bindir}/python${PYTHON_MAJMIN}-config-${MULTILIB_SUFFIX}"


DEPENDS = "bzip2-replacement-native libffi bzip2 openssl sqlite3 zlib virtual/libintl xz virtual/crypt util-linux libtirpc libnsl2 autoconf-archive"
DEPENDS_append_class-target = " python3-native"
DEPENDS_append_class-nativesdk = " python3-native"

EXTRA_OECONF = " --without-ensurepip --enable-shared"
EXTRA_OECONF_append_class-native = " --bindir=${bindir}/${PN}"

export CROSSPYTHONPATH="${STAGING_LIBDIR_NATIVE}/python${PYTHON_MAJMIN}/lib-dynload/"

EXTRANATIVEPATH += "python3-native"

CACHED_CONFIGUREVARS = " \
                ac_cv_file__dev_ptmx=yes \
                ac_cv_file__dev_ptc=no \
                ac_cv_working_tzset=yes \
"
python() {
    # PGO currently causes builds to not be reproducible, so disable it for
    # now. See YOCTO #13407
    if bb.utils.contains('MACHINE_FEATURES', 'qemu-usermode', True, False, d) and d.getVar('BUILD_REPRODUCIBLE_BINARIES') != '1':
        d.setVar('PACKAGECONFIG_PGO', 'pgo')
    else:
        d.setVar('PACKAGECONFIG_PGO', '')
}

PACKAGECONFIG_class-target ??= "readline ${PACKAGECONFIG_PGO} gdbm"
PACKAGECONFIG_class-native ??= "readline gdbm"
PACKAGECONFIG_class-nativesdk ??= "readline gdbm"
PACKAGECONFIG[readline] = ",,readline"
# Use profile guided optimisation by running PyBench inside qemu-user
PACKAGECONFIG[pgo] = "--enable-optimizations,,qemu-native"
PACKAGECONFIG[tk] = ",,tk"
PACKAGECONFIG[gdbm] = ",,gdbm"

do_configure_prepend () {
    mkdir -p ${B}/Modules
    cat > ${B}/Modules/Setup.local << EOF
*disabled*
${@bb.utils.contains('PACKAGECONFIG', 'gdbm', '', '_gdbm _dbm', d)}
${@bb.utils.contains('PACKAGECONFIG', 'readline', '', 'readline', d)}
EOF
}

CPPFLAGS_append = " -I${STAGING_INCDIR}/ncursesw -I${STAGING_INCDIR}/uuid"

EXTRA_OEMAKE = '\
  STAGING_LIBDIR=${STAGING_LIBDIR} \
  STAGING_INCDIR=${STAGING_INCDIR} \
  LIB=${baselib} \
'

do_compile_prepend_class-target() {
       if ${@bb.utils.contains('PACKAGECONFIG', 'pgo', 'true', 'false', d)}; then
                qemu_binary="${@qemu_wrapper_cmdline(d, '${STAGING_DIR_TARGET}', ['${B}', '${STAGING_DIR_TARGET}/${base_libdir}'])}"
                cat >pgo-wrapper <<EOF
#!/bin/sh
cd ${B}
$qemu_binary "\$@"
EOF
                chmod +x pgo-wrapper
        fi
}

do_install_prepend() {
        chmod a+x ${WORKDIR}/check_build_completeness.py
        ${WORKDIR}/check_build_completeness.py ${T}/log.do_compile
}

do_install_append_class-target() {
        oe_multilib_header python${PYTHON_MAJMIN}/pyconfig.h
}

do_install_append_class-native() {
        # Make sure we use /usr/bin/env python
        for PYTHSCRIPT in `grep -rIl ${bindir}/${PN}/python ${D}${bindir}/${PN}`; do
                sed -i -e '1s|^#!.*|#!/usr/bin/env python3|' $PYTHSCRIPT
        done
        # Add a symlink to the native Python so that scripts can just invoke
        # "nativepython" and get the right one without needing absolute paths
        # (these often end up too long for the #! parser in the kernel as the
        # buffer is 128 bytes long).
        ln -s python3-native/python3 ${D}${bindir}/nativepython3
}

do_install_append() {
        mkdir -p ${D}${libdir}/python-sysconfigdata
        sysconfigfile=`find ${D} -name _sysconfig*.py`
        cp $sysconfigfile ${D}${libdir}/python-sysconfigdata/_sysconfigdata.py

        sed -i  \
                -e "s,^ 'LIBDIR'.*, 'LIBDIR': '${STAGING_LIBDIR}'\,,g" \
                -e "s,^ 'INCLUDEDIR'.*, 'INCLUDEDIR': '${STAGING_INCDIR}'\,,g" \
                -e "s,^ 'CONFINCLUDEDIR'.*, 'CONFINCLUDEDIR': '${STAGING_INCDIR}'\,,g" \
                -e "/^ 'INCLDIRSTOMAKE'/{N; s,/usr/include,${STAGING_INCDIR},g}" \
                -e "/^ 'INCLUDEPY'/s,/usr/include,${STAGING_INCDIR},g" \
                ${D}${libdir}/python-sysconfigdata/_sysconfigdata.py
}

do_install_append_class-nativesdk () {
    create_wrapper ${D}${bindir}/python${PYTHON_MAJMIN} TERMINFO_DIRS='${sysconfdir}/terminfo:/etc/terminfo:/usr/share/terminfo:/usr/share/misc/terminfo:/lib/terminfo' PYTHONNOUSERSITE='1'
}

SSTATE_SCAN_FILES += "Makefile _sysconfigdata.py"
PACKAGE_PREPROCESS_FUNCS += "py_package_preprocess"

py_package_preprocess () {
        # Remove references to buildmachine paths in target Makefile and _sysconfigdata
        sed -i -e 's:--sysroot=${STAGING_DIR_TARGET}::g' -e s:'--with-libtool-sysroot=${STAGING_DIR_TARGET}'::g \
                -e 's|${DEBUG_PREFIX_MAP}||g' \
                -e 's:${HOSTTOOLS_DIR}/::g' \
                -e 's:${RECIPE_SYSROOT_NATIVE}::g' \
                -e 's:${RECIPE_SYSROOT}::g' \
                -e 's:${BASE_WORKDIR}/${MULTIMACH_TARGET_SYS}::g' \
                ${PKGD}/${libdir}/python${PYTHON_MAJMIN}/config-${PYTHON_MAJMIN}${PYTHON_ABI}*/Makefile \
                ${PKGD}/${libdir}/python${PYTHON_MAJMIN}/_sysconfigdata*.py \
                ${PKGD}/${bindir}/python${PYTHON_MAJMIN}-config

        # Reformat _sysconfigdata after modifying it so that it remains
        # reproducible
        for c in ${PKGD}/${libdir}/python${PYTHON_MAJMIN}/_sysconfigdata*.py; do
            python3 ${WORKDIR}/reformat_sysconfig.py $c
        done

        # Recompile _sysconfigdata after modifying it
        cd ${PKGD}
        sysconfigfile=`find . -name _sysconfigdata_*.py`
        ${STAGING_BINDIR_NATIVE}/python3-native/python3 \
             -c "from py_compile import compile; compile('$sysconfigfile')"
        ${STAGING_BINDIR_NATIVE}/python3-native/python3 \
             -c "from py_compile import compile; compile('$sysconfigfile', optimize=1)"
        ${STAGING_BINDIR_NATIVE}/python3-native/python3 \
             -c "from py_compile import compile; compile('$sysconfigfile', optimize=2)"
        cd -

        mv ${PKGD}/${bindir}/python${PYTHON_MAJMIN}-config ${PKGD}/${bindir}/python${PYTHON_MAJMIN}-config-${MULTILIB_SUFFIX}
        
        #Remove the unneeded copy of target sysconfig data
        rm -rf ${PKGD}/${libdir}/python-sysconfigdata
}

# We want bytecode precompiled .py files (.pyc's) by default
# but the user may set it on their own conf
INCLUDE_PYCS ?= "1"

python(){
    import collections, json

    filename = os.path.join(d.getVar('THISDIR'), 'python3', 'python3-manifest.json')
    # This python changes the datastore based on the contents of a file, so mark
    # that dependency.
    bb.parse.mark_dependency(d, filename)

    with open(filename) as manifest_file:
        manifest_str =  manifest_file.read()
        json_start = manifest_str.find('# EOC') + 6
        manifest_file.seek(json_start)
        manifest_str = manifest_file.read()
        python_manifest = json.loads(manifest_str, object_pairs_hook=collections.OrderedDict)

    # First set RPROVIDES for -native case
    # Hardcoded since it cant be python3-native-foo, should be python3-foo-native
    pn = 'python3'
    rprovides = d.getVar('RPROVIDES').split()

    # ${PN}-misc-native is not in the manifest
    rprovides.append(pn + '-misc-native')

    for key in python_manifest:
        pypackage = pn + '-' + key + '-native'
        if pypackage not in rprovides:
              rprovides.append(pypackage)

    d.setVar('RPROVIDES_class-native', ' '.join(rprovides))

    # Then work on the target
    include_pycs = d.getVar('INCLUDE_PYCS')

    packages = d.getVar('PACKAGES').split()
    pn = d.getVar('PN')

    newpackages=[]
    for key in python_manifest:
        pypackage = pn + '-' + key

        if pypackage not in packages:
            # We need to prepend, otherwise python-misc gets everything
            # so we use a new variable
            newpackages.append(pypackage)

        # "Build" python's manifest FILES, RDEPENDS and SUMMARY
        d.setVar('FILES_' + pypackage, '')
        for value in python_manifest[key]['files']:
            d.appendVar('FILES_' + pypackage, ' ' + value)

        # Add cached files
        if include_pycs == '1':
            for value in python_manifest[key]['cached']:
                    d.appendVar('FILES_' + pypackage, ' ' + value)

        for value in python_manifest[key]['rdepends']:
            # Make it work with or without $PN
            if '${PN}' in value:
                value=value.split('-', 1)[1]
            d.appendVar('RDEPENDS_' + pypackage, ' ' + pn + '-' + value)

        for value in python_manifest[key].get('rrecommends', ()):
            if '${PN}' in value:
                value=value.split('-', 1)[1]
            d.appendVar('RRECOMMENDS_' + pypackage, ' ' + pn + '-' + value)

        d.setVar('SUMMARY_' + pypackage, python_manifest[key]['summary'])

    # Prepending so to avoid python-misc getting everything
    packages = newpackages + packages
    d.setVar('PACKAGES', ' '.join(packages))
    d.setVar('ALLOW_EMPTY_${PN}-modules', '1')
    d.setVar('ALLOW_EMPTY_${PN}-pkgutil', '1')
}

# Files needed to create a new manifest

do_create_manifest() {
    # This task should be run with every new release of Python.
    # We must ensure that PACKAGECONFIG enables everything when creating
    # a new manifest, this is to base our new manifest on a complete
    # native python build, containing all dependencies, otherwise the task
    # wont be able to find the required files.
    # e.g. BerkeleyDB is an optional build dependency so it may or may not
    # be present, we must ensure it is.

    cd ${WORKDIR}
    # This needs to be executed by python-native and NOT by HOST's python
    nativepython3 create_manifest3.py ${PYTHON_MAJMIN}
    cp python3-manifest.json.new ${THISDIR}/python3/python3-manifest.json
}

# bitbake python -c create_manifest
# Make sure we have native python ready when we create a new manifest
addtask do_create_manifest after do_patch do_prepare_recipe_sysroot

# manual dependency additions
RRECOMMENDS_${PN}-core_append_class-nativesdk = " nativesdk-python3-modules"
RRECOMMENDS_${PN}-crypt_append_class-target = " openssl ca-certificates"
RRECOMMENDS_${PN}-crypt_append_class-nativesdk = " openssl ca-certificates"

# For historical reasons PN is empty and provided by python3-modules
FILES_${PN} = ""
RPROVIDES_${PN}-modules = "${PN}"

FILES_${PN}-pydoc += "${bindir}/pydoc${PYTHON_MAJMIN} ${bindir}/pydoc3"
FILES_${PN}-idle += "${bindir}/idle3 ${bindir}/idle${PYTHON_MAJMIN}"

# provide python-pyvenv from python3-venv
RPROVIDES_${PN}-venv += "python3-pyvenv"

# package libpython3
PACKAGES =+ "libpython3 libpython3-staticdev"
FILES_libpython3 = "${libdir}/libpython*.so.*"
FILES_libpython3-staticdev += "${libdir}/python${PYTHON_MAJMIN}/config-${PYTHON_MAJMIN}-*/libpython${PYTHON_MAJMIN}.a"
INSANE_SKIP_${PN}-dev += "dev-elf"
INSANE_SKIP_${PN}-ptest += "dev-deps"

# catch all the rest (unsorted)
PACKAGES += "${PN}-misc"
RDEPENDS_${PN}-misc += "python3-core python3-email python3-codecs python3-pydoc python3-pickle python3-audio"
RDEPENDS_${PN}-modules_append_class-target = " python3-misc"
RDEPENDS_${PN}-modules_append_class-nativesdk = " python3-misc"
FILES_${PN}-misc = "${libdir}/python${PYTHON_MAJMIN} ${libdir}/python${PYTHON_MAJMIN}/lib-dynload"

# catch manpage
PACKAGES += "${PN}-man"
FILES_${PN}-man = "${datadir}/man"

# See https://bugs.python.org/issue18748 and https://bugs.python.org/issue37395
RDEPENDS_libpython3_append_libc-glibc = " libgcc"
#RDEPENDS_${PN}-ctypes_append_libc-glibc = " ${MLPREFIX}ldconfig"
RDEPENDS_${PN}-ptest = "${PN}-modules ${PN}-tests ${PN}-dev unzip bzip2 libgcc tzdata-europe coreutils sed"
RDEPENDS_${PN}-ptest_append_libc-glibc = " locale-base-tr-tr.iso-8859-9"
RDEPENDS_${PN}-tkinter += "${@bb.utils.contains('PACKAGECONFIG', 'tk', 'tk tk-lib', '', d)}"
RDEPENDS_${PN}-dev = ""

RDEPENDS_${PN}-tests_append_class-target = " bash"
RDEPENDS_${PN}-tests_append_class-nativesdk = " bash"

# Python's tests contain large numbers of files we don't need in the recipe sysroots
SYSROOT_PREPROCESS_FUNCS += " py3_sysroot_cleanup"
py3_sysroot_cleanup () {
	rm -rf ${SYSROOT_DESTDIR}${libdir}/python${PYTHON_MAJMIN}/test
}
