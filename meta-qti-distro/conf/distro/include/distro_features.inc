# Support systemd-user-session
DISTRO_FEATURES:append = " pam"

# Video
DISTRO_FEATURES:append:sa6155 = " qti-carcamsrc qti-omx"
DISTRO_FEATURES:append:sa81x5-rt = " qti-carcamsrc qti-omx"
DISTRO_FEATURES:append:sa81x5 = " qti-carcamsrc qti-fastcv qti-omx"
DISTRO_FEATURES:append:sa8295 = " qti-codec2"
DISTRO_FEATURES:append:lemans = " qti-codec2 qti-gstdeinterlace"
DISTRO_FEATURES:append:qtiquingvm = " qti-omx qti-gstqeavb"
DISTRO_FEATURES:append:qtiquingvm8295 = " qti-codec2 qti-gstdeinterlace qti-gstqvrate qti-gstqvrate"
DISTRO_FEATURES:append:quin-gvm-gen4 = " qti-codec2 qti-gstqeavb qti-gstdeinterlace qti-fadas qti-fastcv"
DISTRO_FEATURES:append:quin-gvm-gen4-2 = " qti-codec2 qti-gstdeinterlace qti-gstqeavb qti-gstqvrate"
DISTRO_FEATURES:append:quin-gvm-lemans = " qti-codec2 qti-gstdeinterlace"

# Display
DISTRO_FEATURES:append = " wayland"


# Use synergy, disable bluez
DISTRO_FEATURES_BACKFILL_CONSIDERED += "bluez5 bluetooth"

#Enable persist-image
DISTRO_FEATURES:append = " persist-image "

DISTRO_FEATURES:append = " qti-early-eth"
#DISTRO_FEATURES:append = " early_init"
#DISTRO_FEATURES:append = " early_userspace"


DISTRO_FEATURES:append = " hibernation"

# Use systemd init manager.
DISTRO_FEATURES:append = " systemd"
DISTRO_FEATURES_BACKFILL_CONSIDERED += "sysvinit"

DISTRO_FEATURES:append = " ipa3"
DISTRO_FEATURES:append = " vulkan"

# WLAN
DISTRO_FEATURES:append = " qti-wlan wifi"

# BT
DISTRO_FEATURES:append:qtiquingvm8295 = " qti-bluetooth"
DISTRO_FEATURES:append:quin-gvm-gen4 = " qti-bluetooth"
DISTRO_FEATURES:append:quin-gvm-gen4-2 = " qti-bluetooth"
DISTRO_FEATURES:append:quin-gvm-lemans = " qti-bluetooth"

