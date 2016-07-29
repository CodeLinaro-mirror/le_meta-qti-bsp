inherit native deploy

DESCRIPTION = "Boot image creation tool from Android"
LICENSE     = "Apache-2.0"
PR          = "r2"
S           = "${WORKDIR}/git"

SRC_URI          = "git://codeaurora.org/platform/system/core.git;protocol=git;branch=LA.HB.1.1.2_rb1.12"
SRCREV           = "aa3b520addb3bfa0292be7e942914dcc1368ff4e"
LIC_FILES_CHKSUM = " \
   file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
   file://${S}/NOTICE;md5=c1a3ff0b97f199c7ebcfdd4d3fed238e \
"
HOMEPAGE = "http://android.git.kernel.org/?p=platform/system/core.git"

do_compile() {
   ${CC} -std=gnu99 -o ${S}/mkbootimg/mkbootimg \
      ${S}/mkbootimg/mkbootimg.c \
      ${S}/libmincrypt/sha.c \
      -I ${S}/include
}

do_install() {
   install -d ${D}${bindir}
   install -m 0755 ${S}/mkbootimg/mkbootimg ${D}${bindir}/
}

do_deploy() {
   install -m 0755 ${S}/mkbootimg/mkbootimg ${DEPLOYDIR}/
}

addtask deploy before do_build after do_install

