DEPENDS += "glib-2.0"

CFLAGS += "-I${STAGING_INCDIR}/cutils"
CFLAGS_append_pn-lib32-androidcompat = " -Dstrlcpy=g_strlcpy "
CFLAGS_append_pn-lib32-androidcompat = " -Dstrlcat=g_strlcat "

LDFLAGS_append_pn-lib32-androidcompat = " -lglib-2.0 -shared "
LDFLAGS_prepend = " -lcutils "

PACKAGES = "${PN} ${PN}-dev"

EXTRA_OEMAKE += "-e MAKEFLAGS="

python do_patch_append() {
    import re,os
    if d.getVar('PN', True).find("lib32") >= 0:
        WORKDIR = "%s/android_compat/device/qcom/msm8996/BoardConfig.mk" % d.getVar('WORKDIR', True)
        f=open(WORKDIR, "r+")
        c = f.read()
        for keystring in ["TARGET_ARCH := arm64", "TARGET_ARCH_VARIANT := armv8-a", "TARGET_CPU_ABI := arm64-v8a", "TARGET_CPU_ABI2 :=", "TARGET_CPU_VARIANT := generic"]:
            c = c.replace(keystring, "#" + keystring)
        f.seek(0)
        f.write(c)
        f.close()
}
