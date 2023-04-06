The recipes in meta-qti-upstream are from upstream without any change.

recipe: recipes-graphics/wayland/wayland-ivi-extension_2.2.0.bb
source: meta-ivi/recipes-graphics/wayland/wayland-ivi-extension_git.bb
        http://git.yoctoproject.org/cgit/cgit.cgi/meta-ivi/tree/meta-ivi/recipes-graphics/wayland/wayland-ivi-extension_git.bb?h=master

recipe: recipes-multimedia/pulseaudio/agl-audio-plugin_0.1.bb
source: meta-agl/meta-agl/recipes-multimedia/pulseaudio/agl-audio-plugin_0.1.bb
        https://git.codelinaro.org/clo/le/AGL/meta-agl/-/tree/automotivelinux/eel/meta-agl/recipes-multimedia/pulseaudio

recipe: recipes-multimedia/gstreamer/*
source: https://git.codelinaro.org/clo/ype/external/yoctoproject.org/poky/-/tree/81f9e815d36848761a9dfa94b00ad998bb39a4a6/meta/recipes-multimedia/gstreamer

recipe: recipes-support/boost/*
source: https://git.codelinaro.org/clo/ype/external/yoctoproject.org/poky/-/tree/f82ede63633de6c7c3d1c3a29215f88c44174e11/meta/recipes-support/boost
