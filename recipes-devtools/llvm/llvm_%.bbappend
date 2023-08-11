FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

EXTRA_OECMAKE += "-DLLVM_TARGETS_TO_BUILD='AArch64' \
                  -DLLVM_TARGET_ARCH='AArch64' \
                  -DLLVM_DEFAULT_TARGET_TRIPLE=aarch64-unknown-linux-gnu \
                  -DLLVM_HOST_TRIPLE=aarch64-unknown-linux-gnu \
                  -DCMAKE_BUILD_TYPE=Release \
                 "
