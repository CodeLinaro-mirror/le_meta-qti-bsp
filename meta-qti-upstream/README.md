The recipes and files in meta-qti-upstream are from upstream without any change.

recipe: recipes-graphics/wayland/wayland-ivi-extension_2.2.0.bb
source: meta-ivi/recipes-graphics/wayland/wayland-ivi-extension_git.bb
        http://git.yoctoproject.org/cgit/cgit.cgi/meta-ivi/tree/meta-ivi/recipes-graphics/wayland/wayland-ivi-extension_git.bb?h=master

recipe: recipes-multimedia/pulseaudio/agl-audio-plugin_0.1.bb
source: meta-agl/meta-agl/recipes-multimedia/pulseaudio/agl-audio-plugin_0.1.bb
        https://git.codelinaro.org/clo/le/AGL/meta-agl/-/tree/automotivelinux/eel/meta-agl/recipes-multimedia/pulseaudio

file: recipes-core/systemd/systemd-systemctl/systemctl
source: meta/recipes-core/systemd/systemd-systemctl/systemctl
        https://git.openembedded.org/openembedded-core/tree/meta/recipes-core/systemd/systemd-systemctl/systemctl?h=scarthgap

recipe: recipes-kernel/libtraceevent/libtraceevent_1.7.3.bb
source: meta/recipes-kernel/libtracefs/libtraceevent_1.7.3.bb
        https://git.openembedded.org/openembedded-core/tree/meta/recipes-kernel/libtraceevent?h=scarthgap

recipe: recipes-kernel/libtracefs/libtracefs_1.7.0.bb
source: meta-oe/recipes-kernel/libtracefs/libtracefs_1.7.0.bb
        https://git.openembedded.org/meta-openembedded/tree/meta-oe/recipes-kernel/libtracefs?h=scarthgap
