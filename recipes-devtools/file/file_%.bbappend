#Fetch file package from the CAF 
SRC_URI = "git://source.codeaurora.org/quic/le/file.git;protocol=https;branch=file/master"
SRC_URI += " \
        file://debian-742262.patch \
        file://0001-Add-P-prompt-into-Usage-info.patch \
        "
