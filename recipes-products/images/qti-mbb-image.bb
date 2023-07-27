# QTI Linux mbb image file.
# Provides packages required to build an mbb image with
# boot to console with connectivity support.

require qti-mbb-minimal-image.bb

IMAGE_INSTALL_append = "\
${@bb.utils.contains('BBFILE_COLLECTIONS', 'qti-rdkb', 'packagegroup-rdkb', '', d)} \
"
