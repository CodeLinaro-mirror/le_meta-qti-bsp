FILESEXTRAPATHS_append := ":${THISDIR}/${PN}"

PACKAGECONFIG[orc] = "--enable-orc,--disable-orc,orc"

DEPENDS += " bzip2 "

PACKAGECONFIG_GL_append ?= " \
   ${@bb.utils.contains('DISTRO_FEATURES', 'opengles2', 'gles2', '', d)} \
"
PACKAGECONFIG_append ??= " \
    uvch264 libmms \
    "
PACKAGECONFIG[gles2]           = "--enable-gles2 --enable-egl,--disable-gles2 --disable-egl,virtual/libgles2 virtual/egl"
PACKAGECONFIG[wayland]         = "--enable-wayland,--disable-wayland,wayland"
PACKAGECONFIG[uvch264]         = "--enable-uvch264,--disable-uvch264,libusb1 udev"

EXTRA_OECONF += " \
    --disable-resindvd \
    --disable-voaacenc \
    --disable-voamrwbenc \
    --disable-yadif \
    ${GSTREAMER_1_0_ORC} \
    "

SRC_URI_append += " \
   file://0002-check-for-wayland-egl-1.0.0.patch \
   file://0003-libEGL-fix-missing-libs.patch \
   file://0004-remove-wayland-scanner-check.patch \
   file://0005-Disable-wl_scaler-if-not-supported-by-compositor.patch \
   file://0006-gst-plugins-bad-waylandsink-window-tile.patch \
   file://0007-gstwaylandsink-fix-OMX-compatible-caps-for-Venus.patch \
   file://0008-gstwaylandsink-read-ionBufFd-metadata.patch \
   file://0009-gstwaylandsink-add-memory-Venus-to-sink-pad-caps.patch \
   file://0001-ionbuf-add-new-ionbuf-library-for-ionbuf-meta-data.patch \
   file://0011-ionbuf-rename-meta-functions-to-be-exported.patch \
   file://0012-gstwaylandsink-skip-wayland-pool-creation-and-buffer.patch \
   file://0013-gstionbuf_meta-add-pointer-to-GstOMXBuffer-to-metada.patch \
   file://0014-waylandsink-post-wl_buffer-created-from-ionbuf.patch \
   file://0015-waylandsink-listen-for-wl_buffer.release-event.patch \
   file://0016-waylandsink-cleanup-formatting-and-error-messages.patch \
   file://0017-waylandsink-clean-buffer_table-only-if-it-has-been-c.patch \
   file://0018-waylandsink-relax-sink-pad-caps.patch \
   file://0019-gstionbuf_meta-add-pointer-to-GstOMXBuffer-to-metada.patch \
   file://0020-gstwaylandsink-push-buffer-release-event-upstream-to.patch \
   file://0021-waylandsink-store-ionbuf-pointer-in-wl_buffer-info.patch \
   file://0022-gstwaylandsink-note-buffer-dropping-by-waylandsink.patch \
   file://0023-waylandsink-improve-buffer-status-messages.patch \
   file://0024-waylandsink-don-t-drop-buffers-even-when-framecallba.patch \
   file://0025-waylandsink-wait-for-framecallback-in-preroll.patch \
   file://0026-waylandsink-wl_omx_buffer_release-cleanup.patch \
   file://0001-Updated-to-correct-type-of-IonBuffFd-and-alignment-r.patch \
   file://0001-Egldisplay-should-get-destroyed-before-clearing-the-.patch \
   file://0001-Increase-the-rank-of-gstwaylandsink.patch \
   file://0027-gstreamer-plugins-bad-Fix-video-playback-issue-due-t.patch \
"


