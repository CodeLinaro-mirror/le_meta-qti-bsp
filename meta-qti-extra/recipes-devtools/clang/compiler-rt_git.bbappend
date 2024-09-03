EXTRA_OECMAKE:remove = "${@bb.utils.contains('DISTRO_FEATURES', 'asan', '-DCOMPILER_RT_BUILD_SANITIZERS=OFF', '', d)} "
EXTRA_OECMAKE:append = "${@bb.utils.contains('DISTRO_FEATURES', 'asan', ' -DCOMPILER_RT_BUILD_SANITIZERS=ON', '', d)} "
DEPENDS += "${@bb.utils.contains('DISTRO_FEATURES', 'asan', 'libxcrypt', '', d)} "
