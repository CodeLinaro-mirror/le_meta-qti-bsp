#Include the robot settings

# Get base product settings
require apq8009-base-image.inc
# These two lines below is a workaround to remove init-mss,
# since packagegroup-ss-mgr just include reboot-daemon and init-mss
IMAGE_INSTALL_remove = "packagegroup-ss-mgr"
IMAGE_INSTALL += "reboot-daemon"

IMAGE_INSTALL += "rb-camera"
