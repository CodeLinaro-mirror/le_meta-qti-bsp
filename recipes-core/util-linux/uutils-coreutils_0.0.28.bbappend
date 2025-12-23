#Remove selinux-related dependencies – clang-native is not needed in our build environment when selinux is enabled
DEPENDS:remove = "${@bb.utils.contains('PACKAGECONFIG', 'selinux', 'clang-native', '', d)}"
