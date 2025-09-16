# Limit packages need to be included as part of the default package.
# To add more extend bindir_progs, base_bindir_progs by referring
# original recipe. Accordingly update alternatives to avoid pkg warnings

python () {
        d.setVar("bindir_progs", "chcon")
        d.setVar("base_bindir_progs", "cp")
}

ALTERNATIVE:${PN} = "${bindir_progs} ${base_bindir_progs} ${sbindir_progs}"
ALTERNATIVE:${PN}-doc = ""

PACKAGE_PREPROCESS_FUNCS += "remove_extra_progs"
remove_extra_progs() {
    cd ${PKGD}${bindir}

    if ${@bb.utils.contains('DISTRO_FEATURES','usrmerge','true','false',d)}; then
       find . -type f ! -name '${bindir_progs}.${BPN}' -a ! -name '${base_bindir_progs}.${BPN}'  -delete
    else
       find . -type f ! -name '${bindir_progs}.${BPN}' -delete

       cd ${PKGD}${base_bindir}
       find . -type f ! -name '${base_bindir_progs}.${BPN}' -delete
    fi
}
