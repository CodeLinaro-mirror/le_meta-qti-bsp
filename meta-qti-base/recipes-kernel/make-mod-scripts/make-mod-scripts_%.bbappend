inherit qti-kernel-arch-clang

# It will remove the dynamic library related kernel moduel signing before build done and
#cause compile error if enable rm_workm hence the exclusion below.
RM_WORK_EXCLUDE += "${PN}"
