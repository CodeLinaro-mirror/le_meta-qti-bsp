PACKAGECONFIG[libtraceevent] = ",NO_LIBTRACEEVENT=1,libtraceevent"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append = "file://0001-libbpf-increase-probe_name-buffer-size-to-avoid-form.patch"

addtask do_fetch before do_configure
addtask do_unpack before do_configure after do_fetch


do_configure:prepend() {
    if [ "${PREFERRED_VERSION_linux-msm}" = "6.12" ]; then
        bbnote "Manually applying libbpf snprintf patch for kernel 6.12"
        cd ${S}
        patch -p1 < ${WORKDIR}/0001-libbpf-increase-probe_name-buffer-size-to-avoid-form.patch || \
            (echo "Patch failed!" && exit 1)
    fi
}

do_populate_lic[noexec] = "1"

