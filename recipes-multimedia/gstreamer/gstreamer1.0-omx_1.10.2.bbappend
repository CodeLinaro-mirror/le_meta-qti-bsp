
python do_getpatches() {
    import os
    #We are using wget as patches are not available in .patch format in a https URI
    #wget command is 'OR'ed with pwd as sometimes wget returns an error even though the patch is fetched succesfully. Hence pwd helps in ignoring 'false failure'
    cmd = "cd ${WORKSPACE}/poky/meta-qti-bsp/recipes-multimedia/gstreamer \
    && rm -rf gstreamer1.0-omx && mkdir gstreamer1.0-omx \
    && (wget https://cgit.freedesktop.org/gstreamer/gst-omx/patch/?id=a02f1e5c9b8f51f2240be9157b897128b2db0f81 -O gstreamer1.0-omx/0001-omxaudioenc-set-base-class-format-instead-of-just-so.patch || pwd) \
    && (wget https://cgit.freedesktop.org/gstreamer/gst-omx/patch/?id=4e01a6f7b12c4e32ee3b406b53143fa515c96680 -O gstreamer1.0-omx/0001-omxaacenc-let-encoder-know-about-incoming-rate-chann.patch || pwd) \
    && (wget https://cgit.freedesktop.org/gstreamer/gst-omx/patch/omx?id=2d1138f45cb482fcf0e2aba5ec40bd4c1cdb5853 -O gstreamer1.0-omx/0001-omx-fixed-type-error-in-printf-call.patch || pwd) "

    os.system(cmd)
}

addtask getpatches before do_fetch
