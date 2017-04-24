#AUDIO HAL
AUDIO_PACKAGES = ""
python() {
    TARGET = d.getVar('MACHINE', "")
    AUDIO_PKG = ""
    if (TARGET == "apq8009"):
        AUDIO_PKG = "audiohal encoders media usbaudio"
    elif (TARGET == "apq8017" or TARGET == "apq8053" or TARGET == "apq8096"):
        AUDIO_PKG = "lib32-tinyalsa lib32-audiohal lib32-encoders lib32-media"
    elif (TARGET == "apq8098"):
        AUDIO_PKG = "lib32-tinyalsa lib32-audiohal"
    elif (TARGET == "mdm9607"):
        AUDIO_PKG = "audiohal alsa-intf"
    elif (TARGET == "mdm9650" or TARGET == "sdx20"):
        AUDIO_PKG = "alsa-intf"
    d.setVar("AUDIO_PACKAGES", AUDIO_PKG)
}
IMAGE_INSTALL += "${AUDIO_PACKAGES}"
