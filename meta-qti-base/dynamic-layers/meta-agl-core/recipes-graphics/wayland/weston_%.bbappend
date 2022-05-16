# Add "weston-ini" RPROVIDES since a weston.ini file has been added
# via a meta-qti-base bbappend.  This makes things work with AGL's
# weston.ini provisioning scheme.
RPROVIDES:${PN} += "weston-ini"
