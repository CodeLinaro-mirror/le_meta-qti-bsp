FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI     = "${CAF_GIT}/AGL/DemoApps/CES2017;protocol=https \
               file://presets-ALS.conf \
               file://presets-CES.conf \
               file://presets-FOSDEM.conf \
              "

# Pinned branch/SRCREV for Charming Chinook
SRC_URI_chinook = "${CAF_GIT}/AGL/DemoApps/CES2017;protocol=https;branch=DemoApps/CES2017/chinook \
                   file://presets-ALS.conf \
                   file://presets-CES.conf \
                   file://presets-FOSDEM.conf \
                  "

INSANE_SKIP_${PN} += "installed-vs-shipped"

FILES_${PN} += "${libdir}/"

DEPENDS_remove = "homescreen"
