#Glibc pakage is fetch from the CAF 
SRC_URI = "git://source.codeaurora.org/quic/le/glibc.git;protocol=https;branch=drains/${SRCBRANCH}"
SRC_URI += " \
           file://etc/ld.so.conf \
           file://generate-supported.mk \
           \
           ${NATIVESDKFIXES} \
           file://0005-fsl-e500-e5500-e6500-603e-fsqrt-implementation.patch \
           file://0006-readlib-Add-OECORE_KNOWN_INTERPRETER_NAMES-to-known-.patch \
           file://0007-ppc-sqrt-Fix-undefined-reference-to-__sqrt_finite.patch \
           file://0008-__ieee754_sqrt-f-are-now-inline-functions-and-call-o.patch \
           file://0009-Quote-from-bug-1443-which-explains-what-the-patch-do.patch \
           file://0010-eglibc-run-libm-err-tab.pl-with-specific-dirs-in-S.patch \
           file://0011-__ieee754_sqrt-f-are-now-inline-functions-and-call-o.patch \
           file://0012-sysdeps-gnu-configure.ac-handle-correctly-libc_cv_ro.patch \
           file://0013-Add-unused-attribute.patch \
           file://0014-yes-within-the-path-sets-wrong-config-variables.patch \
           file://0015-timezone-re-written-tzselect-as-posix-sh.patch \
           file://0016-Remove-bash-dependency-for-nscd-init-script.patch \
           file://0017-eglibc-Cross-building-and-testing-instructions.patch \
           file://0018-eglibc-Help-bootstrap-cross-toolchain.patch \
           file://0019-eglibc-Clear-cache-lines-on-ppc8xx.patch \
           file://0020-eglibc-Resolve-__fpscr_values-on-SH4.patch \
           file://0021-eglibc-Install-PIC-archives.patch \
           file://0022-eglibc-Forward-port-cross-locale-generation-support.patch \
           file://0023-Define-DUMMY_LOCALE_T-if-not-defined.patch \
           file://0024-elf-dl-deps.c-Make-_dl_build_local_scope-breadth-fir.patch \
           file://0025-locale-fix-hard-coded-reference-to-gcc-E.patch \
           file://0027-glibc-reset-dl-load-write-lock-after-forking.patch \
           file://0028-Bug-4578-add-ld.so-lock-while-fork.patch \
           file://0029-bits-siginfo-consts.h-enum-definition-for-TRAP_HWBKP.patch \
"

NATIVESDKFIXES ?= ""
NATIVESDKFIXES_class-nativesdk = "\
           file://0001-nativesdk-glibc-Look-for-host-system-ld.so.cache-as-.patch \
           file://0002-nativesdk-glibc-Fix-buffer-overrun-with-a-relocated-.patch \
           file://0003-nativesdk-glibc-Raise-the-size-of-arrays-containing-.patch \
           file://0004-nativesdk-glibc-Allow-64-bit-atomics-for-x86.patch \
           file://relocate-locales.patch \
"

# glibc expects -fstack-protector optimization passed as a configuration option
# instead of a top level build flag. 

python __anonymous () {
    sel_opt = d.getVar("SELECTED_OPTIMIZATION", True).split()

    for opt in sel_opt:
        if opt in ("-fstack-protector", "-fstack-protector-all", "-fstack-protector-strong"):
            # bb.note("%s can't be built with %s" % (d.getVar('PN'), sel_opt))
            sel_opt.remove(opt)
    d.setVar('SELECTED_OPTIMIZATION', ' '.join(sel_opt))
}

EXTRA_OECONF += "${@bb.utils.contains('FULL_OPTIMIZATION', '-fstack-protector', '--enable-stack-protector=yes', '', d)}"
EXTRA_OECONF += "${@bb.utils.contains('FULL_OPTIMIZATION', '-fstack-protector-all', '--enable-stack-protector=all', '', d)}"
EXTRA_OECONF += "${@bb.utils.contains('FULL_OPTIMIZATION', '-fstack-protector-strong', '--enable-stack-protector=strong', '', d)}"

EXTRA_OECONF += "${@bb.utils.contains('DEBUG_OPTIMIZATION', '-fstack-protector', '--enable-stack-protector=yes', '', d)}"
EXTRA_OECONF += "${@bb.utils.contains('DEBUG_OPTIMIZATION', '-fstack-protector-all', '--enable-stack-protector=all', '', d)}"
EXTRA_OECONF += "${@bb.utils.contains('DEBUG_OPTIMIZATION', '-fstack-protector-strong', '--enable-stack-protector=strong', '', d)}"
