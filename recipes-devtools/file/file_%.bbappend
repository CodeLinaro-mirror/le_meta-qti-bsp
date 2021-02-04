#Fetch file package from the CAF
SRC_URI = "git://source.codeaurora.org/quic/le/file.git;protocol=https;rev=315cef2f699da3c31a54bd3c6c6070680fbaf1f5;nobranch=1"
SRC_URI += " \
         file://debian-742262.patch \
         file://CVE-2019-8906.patch \
         file://CVE-2019-8904.patch \
         file://CVE-2019-8905_CVE-2019-8907.patch \
         "
