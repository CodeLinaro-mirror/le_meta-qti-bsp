HOMEPAGE = "http://mesonbuild.com"
SUMMARY = "A high performance build system"
DESCRIPTION = "Meson is a build system designed to increase programmer \
productivity. It does this by providing a fast, simple and easy to use \
interface for modern software development tools and practices."

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://COPYING;md5=3b83ef96387f14655fc854ddc3c6bd57"

BASE_URL = "https://git.yoctoproject.org/poky/plain/meta/recipes-devtools/meson/meson"
BRANCH = "?h=dunfell"
SRC_URI = "https://github.com/mesonbuild/meson/releases/download/${PV}/meson-${PV}.tar.gz;sha256sum=3e8f830f33184397c2eb0b651ec502adb63decb28978bdc84b3558d71284c21f;md5sum=80303535995fcae72bdb887df102b421 \
           ${BASE_URL}/0001-gtkdoc-fix-issues-that-arise-when-cross-compiling.patch${BRANCH};sha256sum=b93dc94feb9b20d95a084f7436d30750aef177bb4c8cf72b420bb00bb0e24a46;downloadfilename=0001-gtkdoc-fix-issues-that-arise-when-cross-compiling.patch \
           ${BASE_URL}/0002-gobject-introspection-determine-g-ir-scanner-and-g-i.patch${BRANCH};sha256sum=abf3ff14a9c816010f60f8ef5c88fbcdd1af7356cd852ed6276f126bea8925af;downloadfilename=0002-gobject-introspection-determine-g-ir-scanner-and-g-i.patch \
           ${BASE_URL}/0003-native_bindir.patch${BRANCH};sha256sum=95ba3730e7cc159392dc43892be733a1542dc177cfd2ca10dcd52a99802332a4;downloadfilename=0003-native_bindir.patch \
           ${BASE_URL}/0001-python-module-do-not-manipulate-the-environment-when.patch${BRANCH};sha256sum=6400716d52cd0b32e77f428cb19bef05bc544c39b6c8113a0012494e0fa849c8;downloadfilename=0001-python-module-do-not-manipulate-the-environment-when.patch \
           ${BASE_URL}/disable-rpath-handling.patch${BRANCH};sha256sum=73eeaba28dd4de2e5fad39338c39b1fb66d6d92bf6e2a8c714b1a8d319c66748;downloadfilename=disable-rpath-handling.patch \
           ${BASE_URL}/cross-prop-default.patch${BRANCH};sha256sum=2e09c022a7c07c7d3f7a1778acfa600c7d08757dbd4a4e715d3fbf58618e3343;downloadfilename=cross-prop-default.patch \
           ${BASE_URL}/0001-mesonbuild-environment.py-check-environment-for-vari.patch${BRANCH};sha256sum=46b20e5865215f9d6afc61a7af9b61f87550b3f98706f717ccd4a8a55a13d40e;downloadfilename=0001-mesonbuild-environment.py-check-environment-for-vari.patch \
           ${BASE_URL}/0001-modules-python.py-do-not-substitute-python-s-install.patch${BRANCH};sha256sum=112061cb62a7a3cf503a229ab0ae6a432e44e6a66fe5c6344943d8d963b71bd9;downloadfilename=0001-modules-python.py-do-not-substitute-python-s-install.patch \
           "
SRC_URI_append_class-native = " \
    ${BASE_URL}/0001-Make-CPU-family-warnings-fatal.patch${BRANCH};sha256sum=76123d43a70e66aa3325b8b37355a4046fe32f195a046b0e72eebeebb0ab5e3d;downloadfilename=0001-Make-CPU-family-warnings-fatal.patch \
    ${BASE_URL}/0002-Support-building-allarch-recipes-again.patch${BRANCH};sha256sum=8ee2fa268816044d69b3e66e0c7ec2499d856f64c6283a598f1989bc3d618208;downloadfilename=0002-Support-building-allarch-recipes-again.patch \
"

UPSTREAM_CHECK_URI = "https://github.com/mesonbuild/meson/releases"
UPSTREAM_CHECK_REGEX = "meson-(?P<pver>\d+(\.\d+)+)\.tar"

inherit setuptools3

RDEPENDS_${PN} = "ninja python3-modules python3-pkg-resources"

FILES_${PN} += "${datadir}/polkit-1"
