#!/bin/sh
#
#fdeinvoke        init.d script to start the hwfde_service
#
#

set -e
/usr/bin/hwfde_service
if [ $? -ne 0 ]; then
    exit
fi

datamountret=`mount | grep /data | wc -l`

if [ $datamountret == "1" ]; then
    /sbin/restorecon -R /data
fi
