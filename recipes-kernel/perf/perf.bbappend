PACKAGECONFIG[libtraceevent] = ",NO_LIBTRACEEVENT=1,libtraceevent"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append:vienna = "file://0001-libbpf-increase-probe_name-buffer-size-to-avoid-form.patch"

addtask do_fetch before do_configure
addtask do_unpack before do_configure after do_fetch


do_configure:prepend:vienna() {
    bbnote "Manually applying libbpf snprintf patch"
    cd ${S}
    patch -p1 < ${WORKDIR}/0001-libbpf-increase-probe_name-buffer-size-to-avoid-form.patch || \
        (echo "Patch failed!" && exit 1)
}

do_populate_lic[noexec] = "1"

