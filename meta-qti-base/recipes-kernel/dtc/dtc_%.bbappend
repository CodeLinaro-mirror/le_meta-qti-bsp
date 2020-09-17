LIC_FILES_CHKSUM = "file://GPL;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://libfdt/libfdt.h;beginline=3;endline=52;md5=fb360963151f8ec2d6c06b055bcbb68c"

# change the SRC dir
SRC_URI = "${PATH_TO_REPO}/external/dtc.git;protocol=${PROTO};destsuffix=external/dtc;nobranch=1" 

S = "${WORKDIR}/external/dtc"
EXTRA_OEMAKE_append = " NO_PYTHON=1"

SRCREV = "2b1d2a367990cdccd90b2c672033d6c29477fd72"

FILESEXTRAPATHS_append := ":${THISDIR}/files"
SRC_URI_append = " file://0001-dtc-fix-Android-dtc-compile-issue.patch"

do_fetch_prepend(){
    bb.warn('NOTE: bbappend overrides dtc recipe\'s version and sets it to Android 1.4.4')
}

