inherit selinux

PACKAGECONFIG_append = " ${@target_selinux(d, 'selinux')} "
