SRC_DIR = "${WORKSPACE}/qcom-opensource/location/loc_api/loc_api_v02"
S = "${WORKDIR}/qcom-opensource/location/loc_api/loc_api_v02"

SRC_URI_remove = "file://CMakeLists.txt;subdir=vendor/qcom/opensource/location/loc_api/loc_api_v02"
SRC_URI += "file://CMakeLists.txt;subdir=qcom-opensource/location/loc_api/loc_api_v02"
