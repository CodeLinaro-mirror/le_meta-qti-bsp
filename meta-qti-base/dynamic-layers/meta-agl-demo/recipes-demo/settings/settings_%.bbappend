FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# Remove qt-qrcode dependency and patch out usage to avoid
# needing a GPLv3 dependency.  Since the Alexa voiceagent
# demo is unlikely to be used, the removed functionality is
# unlikely to be a problem.

DEPENDS:remove = "qt-qrcode"

SRC_URI += "file://0001-remove-qt-qrcode-usage.patch"

RDEPENDS:${PN}:remove = "qt-qrcode"
