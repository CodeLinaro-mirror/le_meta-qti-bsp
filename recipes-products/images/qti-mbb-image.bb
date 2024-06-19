# QTI Linux mbb image file.
# Provides packages required to build an mbb image with
# boot to console with connectivity support.

require qti-mbb-minimal-image.bb

IMAGE_INSTALL_append = "\
${@bb.utils.contains('BBFILE_COLLECTIONS', 'qti-rdkb', 'packagegroup-rdkb', '', d)} \
"
CORE_IMAGE_EXTRA_INSTALL += "\
               ${@bb.utils.contains('BBFILE_COLLECTIONS', 'ipq-prop', \
                     bb.utils.contains('MACHINE_FEATURES', 'qti-wifi', 'packagegroup-qti-wifi', '', d), '', d)} \
		 ${@bb.utils.contains('BBFILE_COLLECTIONS', 'qti-ss-mgr-prop', \
                    bb.utils.contains('MACHINE_SUPPORTS_PDMAPPER', 'True', 'ss-services', '', d), '', d)} \
               ${@bb.utils.contains('BBFILE_COLLECTIONS', 'qti-ss-mgr-prop', \
                    bb.utils.contains('MACHINE_SUPPORTS_SSR', 'True', 'subsystem-ramdump', '', d), '', d)} \
"
