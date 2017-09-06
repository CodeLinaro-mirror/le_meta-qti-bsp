SRC_URI  =  "file://kernel/msm-4.4"
S =  "${WORKDIR}/kernel/msm-4.4"

SRC_URI += "file://Fix-for-glibc-compilation-with-kernel4.4.patch"
SRC_URI += "file://binder.h"

do_compile_append() {
        cp -f ${WORKDIR}/binder.h ${S}/include/uapi/linux/android/
}
