PV="1.8.3"

LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://COPYING.LIB;md5=6762ed442b3822387a51c92d928ead0d \
                    file://ext/libav/gstav.h;beginline=1;endline=18;md5=a752c35267d8276fd9ca3db6994fca9c \
                    file://gst-libs/ext/libav/COPYING.LGPLv2.1;md5=bd7a443320af8c812e4c18d1b79df004"

SRC_URI[md5sum] = "b51a736147bacb40f85827a4e0ae0d2c"
SRC_URI[sha256sum] = "9006a05990089f7155ee0e848042f6bb24e52ab1d0a59ff8d1b5d7e33001a495"

EXTRA_OECONF += "--without-system-libav"
LIBAV_EXTRA_CONFIGURE_COMMON_ARG += "--disable-everything \
                                     --enable-decoder=aac,mp3 \
                                     "
