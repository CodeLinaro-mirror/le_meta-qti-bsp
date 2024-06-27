require glib.inc

PE = "1"

SHRT_VER = "${@oe.utils.trim_version("${PV}", 2)}"

BASE_URL = "https://git.yoctoproject.org/poky/plain/meta/recipes-core/glib-2.0/glib-2.0"
BRANCH = "?h=kirkstone"

SRC_URI = "${GNOME_MIRROR}/glib/${SHRT_VER}/glib-${PV}.tar.xz;sha256sum=4a39a2f624b8512d500d5840173eda7fa85f51c109052eae806acece85d345f0 \
           ${BASE_URL}/run-ptest${BRANCH};sha256sum=40c2a95d38e622d5dd0cac49276dc760aec2b930c703f700289a4601813196d2;downloadfilename=run-ptest \
           ${BASE_URL}/0001-Fix-DATADIRNAME-on-uclibc-Linux.patch${BRANCH};sha256sum=0cb5204b8b0bd5934933291682714ccf859d613acc55f3b73203185a325015d4;downloadfilename=0001-Fix-DATADIRNAME-on-uclibc-Linux.patch \
           ${BASE_URL}/Enable-more-tests-while-cross-compiling.patch${BRANCH};sha256sum=0b9c7bc4cc9c5c91561706df0bdfbbf5f4f9a6d47520ac12939db215f204965b;downloadfilename=Enable-more-tests-while-cross-compiling.patch \
           ${BASE_URL}/0001-Remove-the-warning-about-deprecated-paths-in-schemas.patch${BRANCH};sha256sum=22b46f1735fd7ecd47996fc4805a7ea977d4c50d972f76505b61999625ede4ef;downloadfilename=0001-Remove-the-warning-about-deprecated-paths-in-schemas.patch \
           ${BASE_URL}/0001-Install-gio-querymodules-as-libexec_PROGRAM.patch${BRANCH};sha256sum=365dfb78c40c98741e03e22810a5b8c792402024c7bb78be49f18457475ccb7a;downloadfilename=0001-Install-gio-querymodules-as-libexec_PROGRAM.patch \
           ${BASE_URL}/0001-Do-not-ignore-return-value-of-write.patch${BRANCH};sha256sum=537e92b021ea919d7bdcf93446e153af6734f4ea30fab10138c57ae10fbbaa45;downloadfilename=0001-Do-not-ignore-return-value-of-write.patch \
           ${BASE_URL}/0010-Do-not-hardcode-python-path-into-various-tools.patch${BRANCH};sha256sum=febf5df4dbcf4c2bf0b219c39b1202e67a6e07c1908d4c424ed5130a879c00cf;downloadfilename=0010-Do-not-hardcode-python-path-into-various-tools.patch \
           ${BASE_URL}/0001-Set-host_machine-correctly-when-building-with-mingw3.patch${BRANCH};sha256sum=d02ca899f967449fdffdfd3ac5565c752e594334a1d7bdafe205a3e18cc39c30;downloadfilename=0001-Set-host_machine-correctly-when-building-with-mingw3.patch \
           ${BASE_URL}/0001-Do-not-write-bindir-into-pkg-config-files.patch${BRANCH};sha256sum=cb430e53a1795d48958b64e7e50b4d8fdd45cf5ecea4ddbe1e6c04067e27cc7e;downloadfilename=0001-Do-not-write-bindir-into-pkg-config-files.patch \
           ${BASE_URL}/0001-meson-Run-atomics-test-on-clang-as-well.patch${BRANCH};sha256sum=b300f68e89c2963cf0ff90c72125d8c11769c5eec656cdb2bad6aae98ce4f78e;downloadfilename=0001-meson-Run-atomics-test-on-clang-as-well.patch \
           ${BASE_URL}/0001-gio-tests-resources.c-comment-out-a-build-host-only-.patch${BRANCH};sha256sum=404bc5a03522ae0998562f8d4e8dd07089bb548f05db08c3f2e6415df01069ce;downloadfilename=0001-gio-tests-resources.c-comment-out-a-build-host-only-.patch \
           ${BASE_URL}/0001-gio-tests-g-file-info-don-t-assume-million-in-one-ev.patch${BRANCH};sha256sum=96dc8e3f232e4256a0e39c3f87ee5066dd1805b49d7d6a7a2c1d00571fe8ff82;downloadfilename=0001-gio-tests-g-file-info-don-t-assume-million-in-one-ev.patch \
           ${BASE_URL}/CVE-2023-32665-0001.patch${BRANCH};sha256sum=996a5e7440b8a0273a75c26865308306a0a6cda3ce27c15e25dd4b0bf9462cb4;downloadfilename=CVE-2023-32665-0001.patch \
           ${BASE_URL}/CVE-2023-32665-0002.patch${BRANCH};sha256sum=2f0f255fe840960206808ba44356db8ab33ded9130768f35b06c7495f2ce4bc5;downloadfilename=CVE-2023-32665-0002.patch \
           ${BASE_URL}/CVE-2023-32665-0003.patch${BRANCH};sha256sum=90b5c2cac8bb9b3d19a2624e6c342dc4b5e5236b91f78a596cd5fbd4a5477bd2;downloadfilename=CVE-2023-32665-0003.patch \
           ${BASE_URL}/CVE-2023-32665-0004.patch${BRANCH};sha256sum=0113780b249f49ae2505b1f3c5fb6b26ba41cc769549a01ebef8f9e95a1dfba7;downloadfilename=CVE-2023-32665-0004.patch \
           ${BASE_URL}/CVE-2023-32665-0005.patch${BRANCH};sha256sum=27b575fe3d1d627a8400cd388f52f84a09c8402c0d73973fdd665dbacb804ceb;downloadfilename=CVE-2023-32665-0005.patch \
           ${BASE_URL}/CVE-2023-32665-0006.patch${BRANCH};sha256sum=ea8fd888513e869b16e049014fc4e599ffc18483a5c50ea79c501bc3a202c525;downloadfilename=CVE-2023-32665-0006.patch \
           ${BASE_URL}/CVE-2023-32665-0007.patch${BRANCH};sha256sum=d0ca9377ed959bbae83aeb49b9494be989a17721826012a48d4d9e310c1d6a9e;downloadfilename=CVE-2023-32665-0007.patch \
           ${BASE_URL}/CVE-2023-32665-0008.patch${BRANCH};sha256sum=fb0811c50f3077569617e7adb08c0e0562c2911f2bce77a410b9ba97279882a9;downloadfilename=CVE-2023-32665-0008.patch \
           ${BASE_URL}/CVE-2023-32665-0009.patch${BRANCH};sha256sum=1a0ee954c37cf1ce6390ba01a47c4310eaf983cd6adc5a7e664a1928225b2e33;downloadfilename=CVE-2023-32665-0009.patch \
           ${BASE_URL}/CVE-2023-29499.patch${BRANCH};sha256sum=7877ee64e76dd62f9c3d88397b26b0f8c584cf86c53c3b019588686104447b9d;downloadfilename=CVE-2023-29499.patch \
           ${BASE_URL}/CVE-2023-32611-0001.patch${BRANCH};sha256sum=840878cf5fc3e9239cf22e0b0df1df8c28351b01c505ae18c372c06cd1dffc6b;downloadfilename=CVE-2023-32611-0001.patch \
           ${BASE_URL}/CVE-2023-32611-0002.patch${BRANCH};sha256sum=a581853667b5b9766f5a0d2f3a1703b8b4f4b27e25f988bab13af46c87795d36;downloadfilename=CVE-2023-32611-0002.patch \
           ${BASE_URL}/CVE-2023-32643.patch${BRANCH};sha256sum=bd65df9baee7c7550f4563f4ecddcb3ee02ca6ae258b53cd7e7dab7855cc40bf;downloadfilename=CVE-2023-32643.patch \
           ${BASE_URL}/CVE-2023-32636.patch${BRANCH};sha256sum=0f360dfb313450170b831ed8b365858d7d30029756f45fb23516eab54ef2fe49;downloadfilename=CVE-2023-32636.patch \
           "
SRC_URI_append_class-native = " ${BASE_URL}/relocate-modules.patch${BRANCH};sha256sum=ebb2390f9f93d10e9b129efff9a19e1e5113971f8d50a0076b026b49cb16587c"

# Find any meson cross files in FILESPATH that are relevant for the current
# build (using siteinfo) and add them to EXTRA_OEMESON.
inherit siteinfo
def find_meson_cross_files(d):
    if bb.data.inherits_class('native', d):
        return ""

    thisdir = os.path.normpath(d.getVar("THISDIR"))
    import collections
    sitedata = siteinfo_data(d)
    # filename -> found
    files = collections.OrderedDict()
    for path in d.getVar("FILESPATH").split(":"):
        for element in sitedata:
            filename = os.path.normpath(os.path.join(path, "meson.cross.d", element))
            sanitized_path = filename.replace(thisdir, "${THISDIR}")
            if sanitized_path == filename:
                if os.path.exists(filename):
                    bb.error("Cannot add '%s' to --cross-file, because it's not relative to THISDIR '%s' and sstate signature would contain this full path" % (filename, thisdir))
                continue
            files[filename.replace(thisdir, "${THISDIR}")] = os.path.exists(filename)

    items = ["--cross-file=" + k for k,v in files.items() if v]
    d.appendVar("EXTRA_OEMESON", " " + " ".join(items))
    items = ["%s:%s" % (k, "True" if v else "False") for k,v in files.items()]
    d.appendVarFlag("do_configure", "file-checksums", " " + " ".join(items))

python () {
    import os
    download_path = "${WORKSPACE}/poky/meta-qti-bsp/recipes-core/glib-2.0/glib-2.0/"
    cmd = "mkdir -p " + download_path + "meson.cross.d"
    os.system(cmd)
    files = [ "meson.cross.d/common", "meson.cross.d/common-glibc", "meson.cross.d/common-linux", "meson.cross.d/common-mingw", "meson.cross.d/common-musl"]
    for entry in files:
        cmd = "wget ${BASE_URL}/" + entry + "${BRANCH} -O " + download_path + entry
        os.system(cmd)

    find_meson_cross_files(d)
}
