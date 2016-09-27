
DEPENDS += "opencore-amr"

PACKAGECONFIG ??= ""

EXTRA_OECONF += " \
    --disable-a52dec \
    --disable-cdio \
    --disable-dvdlpcmdec \
    --disable-dvdread \
    --disable-dvdsub \
    --disable-lame \
    --disable-mad \
    --disable-mpeg2dec \
    --disable-realmedia \
    --disable-sidplay \
    --disable-twolame \
    --disable-x264 \
    --disable-xingmux \
    --disable-dependency-tracking \
    --disable-FEATURE \
    --enable-amrnb \
    --enable-amrwb \
    "

INSANE_SKIP_${PN} += "installed-vs-shipped"


