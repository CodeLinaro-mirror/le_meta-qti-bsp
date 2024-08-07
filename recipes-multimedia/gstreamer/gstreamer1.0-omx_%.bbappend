FILESEXTRAPATHS_append := ":${THISDIR}/${PN}"

SRC_URI_append = " file://0001-gst-omx-enc-Support-GstForceKeyUnit-event-on-QTI-pla.patch"
SRC_URI_append = " file://0002-Revert-Optimize-the-latency-of-resolution-change.patch"
SRC_URI_append = " file://0005-gst-omx-dec-Fix-potential-thread-lock-unlock-issue.patch"
