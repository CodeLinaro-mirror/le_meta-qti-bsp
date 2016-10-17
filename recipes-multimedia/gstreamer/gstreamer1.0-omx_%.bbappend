FILESEXTRAPATHS_append := ":${THISDIR}/${PN}"

S = "${WORKDIR}/gst-omx-${PV}"

RDEPENDS_${PN} = "media"
GSTREAMER_1_0_OMX_TARGET ?= "generic"
GSTREAMER_1_0_OMX_CORE_NAME = "${libdir}/libOmxCore.so"

SRC_URI_append += " \
   file://gstomx.conf \
   file://0001-gstomx-find-buffer-private-data-in-FTB-and-ETB-metho.patch \
   file://0002-gstomx-add-memory-Venus-to-src-pad-caps.patch \
   file://0003-gstomxvideodec-look-up-ionBufInfo-in-fill_buffer.patch \
   file://0004-gstomxvideodec-define-format-of-GstMeta-object-to-ho.patch \
   file://0005-gstomxvideodec-associate-GstIonBufFdMeta-data-with-O.patch \
   file://0006-use-ionbuf-meta-info-from-new-gstreamer-ionbuf-lib.patch \
   file://0007-gstomxvideodec-skip-format-negotiation-and-buffer-al.patch \
   file://0008-gstomxvideodec-remove-release_buffer-from-dec_loop.patch \
   file://0009-gstomxvideodec-include-GstOMXBuffer-pointer-in-GstIo.patch \
   file://0010-gstomx-create-src-pad-event-handler-for-buffer-relea.patch \
   file://0011-Restore-limited-caps-negotiation.patch \
   file://0012-gstomx-recycle-OMXBuffer-on-wayland-release-event.patch \
   file://0013-gstomxvideodec-no-buffer-deallocation-for-deactivate.patch \
   file://0014-gstomx-fixup-comp-lock-handling.patch \
   file://0015-gstomxvideodec-initialize-intersection-caps.patch \
   file://0016-gstomx-handle-non-buffer-release-CUSTOM_UPSTREAM-eve.patch \
   file://0017-gstomxvideodec-relax-framerate-again.patch \
   file://0018-gstomxvideodec-add-debug-output-about-dropped-older-.patch \
   file://0019-Change-g_print-to-GST_DEBUG.patch \
   file://0020-gstomx-print-stats-about-released-buffers.patch \
   file://0021-hack-reuse-pushed-buffers-when-running-out-of-free-b.patch \
   file://0022-hack-wait-30ms-before-reusing-unreleased-buffers.patch \
   file://0023-free-wl_omx_buffer_release-event-in-omx-handler.patch \
   file://0001-msm-omx-interface-and-ionbuff-updates-added-with-wor.patch \
   file://0001-Disable-the-HACK-for-reuse-of-unreleased-buffer.patch \
   file://0026-gstomx-support-videoencoder.patch \
   file://0027-gstomx-increase-one-buffer-for-weston-atomic-modeset.patch \
   file://0028-gst-omx-Fix-video-hang-issue.patch \
"

do_configure_prepend() {
   # Replace default gstimx.conf with a custom version
   rm -rf ${S}/config/bellagio/gstomx.conf
   install ${WORKDIR}/gstomx.conf ${S}/config/bellagio/gstomx.conf
}

