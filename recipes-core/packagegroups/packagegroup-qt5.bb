LICENSE     = "MIT"

SUMMARY = "Qt5 for GUI framework"
DESCRIPTION = "A set of Qt5 packages.It enables developers to develop applications"

inherit packagegroup

ALLOW_EMPTY_${PN} = "1"
PACKAGES = "${PN}"

RDEPENDS_${PN} = " \
    qtbase \
    qtbase-dev \
    qtbase-fonts \
    qtbase-fonts-pfa \
    qtbase-fonts-pfb \
    qtbase-fonts-qpf \
    qtbase-fonts-ttf-dejavu \
    qtbase-fonts-ttf-vera \
    qtbase-plugins \
    qtbase-examples \
    qtbase-staticdev \
    qtbase-tools \
    qtdeclarative \
    qtdeclarative-plugins \
    qtdeclarative-qmlplugins \
    qtdeclarative-tools \
    qtquick1 \
    qtquick1-plugins \
    qtquick1-tools \
    qtwayland \
    qtwayland-examples \
    qtwayland-plugins \
    qtwayland-tools \
    qtgraphicaleffects-qmlplugins \
    qml-execscript-plugin \
    \
    qtconnectivity \
    qtconnectivity-qmlplugins \
    \
    qtlocation \
    qtlocation-plugins \
    qtlocation-qmlplugins \
    \
    qtmultimedia \
    qtmultimedia-plugins \
    qtmultimedia-qmlplugins \
    \
    qtwebkit \
    qtwebkit-examples-examples \
"

