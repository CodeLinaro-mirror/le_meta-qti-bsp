
SRC_URI_append_8x96autogvmquintcu = "file://0001-irq-Add-IRQF_ONESHOT-to-request_irq-flag-for-Hypervisor.patch"

KERNEL_CC_append = " -Wno-misleading-indentation"

BB_HASH_IGNORE_MISMATCH = "1"

ALLOW_EMPTY_kernel-module-wlan-${KERNEL_VERSION} = "1"
