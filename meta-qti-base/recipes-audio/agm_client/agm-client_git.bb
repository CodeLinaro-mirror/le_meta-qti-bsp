SUMMARY = "AGM Client Library"
DESCRIPTION = "This is the client library of AGM, based on Binder IPC."
HOMEPAGE = "http://www.codeaurora.org"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"
DEPENDS += "agm-server ar-osal ar-util binder gsl-fe-noship libcutils liblog libuhab libutils linux-msm-headers spf"

SRC_URI = "${PATH_TO_REPO}/vendor/qcom/opensource/agm/.git;protocol=${PROTO};destsuffix=vendor/qcom/opensource/agm/ipc/SwBinders/agm_client;subpath=agm_client;usehead=1"
SRCREV = "${AUTOREV}"

S = "${WORKDIR}/vendor/qcom/opensource/agm/ipc/SwBinders/agm_client"

inherit autotools pkgconfig

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
