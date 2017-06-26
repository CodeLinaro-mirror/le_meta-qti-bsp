#Include the robot settings

# Get base product settings
require apq8009-base-image.inc
# These two lines below is a workaround to remove init-mss,
# since packagegroup-ss-mgr just include reboot-daemon and init-mss
IMAGE_INSTALL_remove = "packagegroup-ss-mgr"
IMAGE_INSTALL += "reboot-daemon"

IMAGE_INSTALL += "chrony"
IMAGE_INSTALL += "chronyc"

IMAGE_INSTALL += "openssh"
IMAGE_INSTALL += "${@base_conditional('PRODUCT','robot-rome', 'qcacld-hl', '', d)}"
IMAGE_INSTALL += "${@base_conditional('PRODUCT','robot-rome', 'iw', '', d)}"
IMAGE_INSTALL_remove = "${@base_conditional('PRODUCT','robot-rome', 'wlan-opensource', '', d)}"
IMAGE_INSTALL += "${@base_conditional('PRODUCT','robot-rome', 'sigma-dut', '', d)}"
IMAGE_INSTALL += "${@base_conditional('PRODUCT','robot-rome', 'data', '', d)}"
IMAGE_INSTALL += "${@base_conditional('PRODUCT','robot-rome', 'wlan-services', '', d)}"
IMAGE_INSTALL += "${@base_conditional('PRODUCT','robot-rome', 'libcap', '', d)}"
IMAGE_INSTALL += "${@base_conditional('PRODUCT','robot-rome', 'attr', '', d)}"
IMAGE_INSTALL += "${@base_conditional('PRODUCT','robot-rome', 'iputils', '', d)}"
