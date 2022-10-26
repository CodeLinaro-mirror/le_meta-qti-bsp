#Copyright (c) 2022 Qualcomm Innovation Center, Inc. All rights reserved.
#SPDX-License-Identifier: BSD-3-Clause-Clear

#BUILDDIR="/$esdk_work_path"

#set prebuilt conf in esdk
if [[ $OE_SKIP_SDK_CHECK =~ "1" ]]; then
        #set need variables when generate prebuilt confs
	WS=$(readlink -f  ${BUILDDIR}/)
	MACHINE="qrb5165-rb5"

	# Generate prebuilt conf by reading manifest.
	#the .repo/mainfests/default.xml need place upder the path of WS
	source "${BUILDDIR}/layers/poky/meta-qti-internal/scripts/generate_prebuilt_confs.sh"
	# include generated prebuilt conf in auto.conf
	cat >> ${BUILDDIR}/conf/auto.conf <<EOF
#----------------------------------------
# Include prebuilt configuration file
#----------------------------------------
include conf/${MACHINE}_prebuilts.conf
EOF
fi

