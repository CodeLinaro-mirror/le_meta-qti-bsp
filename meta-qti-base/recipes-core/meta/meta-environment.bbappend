# Override the default CC/CXX/CPP with including directory for linux-msm-headers added
create_sdk_files:append() {
    echo '# Override the default CC/CXX/CPP with including directory for linux-msm-headers added' >> ${SDK_OUTPUT}/${SDKPATH}/environment-setup-${REAL_MULTIMACH_TARGET_SYS}
    echo 'export CC="${TARGET_PREFIX}gcc ${TARGET_CC_ARCH} --sysroot=$SDKTARGETSYSROOT -I$SDKTARGETSYSROOT/usr/include/linux-msm"' >> ${SDK_OUTPUT}/${SDKPATH}/environment-setup-${REAL_MULTIMACH_TARGET_SYS}
    echo 'export CXX="${TARGET_PREFIX}g++ ${TARGET_CC_ARCH} --sysroot=$SDKTARGETSYSROOT -I$SDKTARGETSYSROOT/usr/include/linux-msm"' >> ${SDK_OUTPUT}/${SDKPATH}/environment-setup-${REAL_MULTIMACH_TARGET_SYS}
    echo 'export CPP="${TARGET_PREFIX}gcc -E ${TARGET_CC_ARCH} --sysroot=$SDKTARGETSYSROOT -I$SDKTARGETSYSROOT/usr/include/linux-msm"' >> ${SDK_OUTPUT}/${SDKPATH}/environment-setup-${REAL_MULTIMACH_TARGET_SYS}
}
