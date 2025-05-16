# Support systemd-user-session
DISTRO_FEATURES:append = " pam"
#SA8797 RUMI bringup remove pam.
DISTRO_FEATURES:remove:sa8797 = "pam"

# Video
DISTRO_FEATURES:append:sa6155 = " qti-carcamsrc qti-omx"
DISTRO_FEATURES:append:sa81x5-rt = " qti-carcamsrc qti-omx"
DISTRO_FEATURES:append:sa81x5 = " qti-carcamsrc qti-fastcv qti-omx"
DISTRO_FEATURES:append:sa8295 = " qti-codec2"
DISTRO_FEATURES:append:qtiquingvm = " qti-omx qti-gstqeavb"
DISTRO_FEATURES:append:qtiquingvm8295 = " qti-codec2 qti-gstdeinterlace qti-gstqeavb qti-gstqvrate qti-gstqvais"
DISTRO_FEATURES:append:quin-gvm-gen4 = " qti-codec2 qti-gstqeavb qti-gstdeinterlace"
DISTRO_FEATURES:append:quin-gvm-gen4-2 = " qti-codec2 qti-gstdeinterlace qti-gstqeavb qti-gstqvrate qti-gstqvais"
DISTRO_FEATURES:append:quin-gvm-lemans = " qti-codec2 qti-gstdeinterlace qti-gstqeavb qti-gstqvrate qti-gstqvais qti-gstqcarcamsrc"
DISTRO_FEATURES:append:quin-gvm-monaco = " qti-codec2 qti-gstdeinterlace qti-gstqeavb"

# Camera
DISTRO_FEATURES:append:quin-gvm-lemans = " qti-qcx"
DISTRO_FEATURES:append:quin-gvm-monaco = " qti-qcx"

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
DISTRO_FEATURES:append = " systemd ${@bb.utils.contains('LAYERSERIES_CORENAMES', 'scarthgap', 'usrmerge', '', d)}"
DISTRO_FEATURES_BACKFILL_CONSIDERED += "sysvinit"

DISTRO_FEATURES:append = " vulkan"

# WLAN
DISTRO_FEATURES:append = " qti-wlan wifi"
DISTRO_FEATURES:remove:sa8797 = "wifi"

# BT
DISTRO_FEATURES:append:qtiquingvm8295 = " qti-bluetooth"
DISTRO_FEATURES:append:quin-gvm-gen4 = " qti-bluetooth"
DISTRO_FEATURES:append:quin-gvm-gen4-2 = " qti-bluetooth"
DISTRO_FEATURES:append:quin-gvm-lemans = " qti-bluetooth"
DISTRO_FEATURES:append:quin-gvm-monaco = " qti-bluetooth"

# SELinux
DISTRO_FEATURES:append:sa8775 = " selinux"
DISTRO_FEATURES:append:sa7255 = " selinux"
DISTRO_FEATURES:append:quin-gvm-lemans = " selinux"
DISTRO_FEATURES_NATIVE:append:sa8775 = " selinux"
DISTRO_FEATURES_NATIVE:append:sa7255 = " selinux"
DISTRO_FEATURES_NATIVE:append:quin-gvm-lemans = " selinux"

# Preferred refpolicy
PREFERRED_PROVIDER_virtual/refpolicy:sa8775 = "selinux-policy"
PREFERRED_PROVIDER_virtual/refpolicy:sa7255 = "selinux-policy"
PREFERRED_PROVIDER_virtual/refpolicy:quin-gvm-lemans = "selinux-policy"
DEFAULT_ENFORCING:sa8775 = "enforcing"
DEFAULT_ENFORCING:sa7255 = "enforcing"
DEFAULT_ENFORCING:quin-gvm-lemans = "enforcing"
PREFERRED_VERSION_selinux-policy:sa8775 = "1.0"
PREFERRED_VERSION_selinux-policy:sa7255 = "1.0"
PREFERRED_VERSION_selinux-policy:quin-gvm-lemans = "2.0"
