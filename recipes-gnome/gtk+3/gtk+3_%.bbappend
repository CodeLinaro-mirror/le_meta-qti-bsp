DEPENDS += "glib-2.0-native"
PACKAGECONFIG[wayland] = "--enable-wayland-backend,--disable-wayland-backend,wayland wayland-protocols libxkbcommon wayland-native adreno"

