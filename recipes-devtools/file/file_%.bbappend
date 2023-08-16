#Fetch file package from the codelinaro
SRC_URI = "${CLO_LE_GIT}/file.git;protocol=https;branch=caf_migration/file/master"
SRC_URI += " \
        file://debian-742262.patch \
        file://0001-Add-P-prompt-into-Usage-info.patch \
        "
