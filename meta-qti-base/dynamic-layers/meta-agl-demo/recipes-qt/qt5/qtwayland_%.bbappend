# qtwayland autodetects Vulkan support, but then fails to build.
# Disable for now.
EXTRA_QMAKEVARS_CONFIGURE += "-no-feature-wayland-vulkan-server-buffer"
