# Disable jitter entropy source which is slow and consume cpu resources
PACKAGECONFIG_remove = " libjitterentropy"
