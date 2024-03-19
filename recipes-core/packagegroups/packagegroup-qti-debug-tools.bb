SUMMARY = "Package suite with debugging tools from OE and QTI"

inherit packagegroup

PACKAGES =  "\
              packagegroup-qti-debug-tools \
            "

VALG ?= 'True'
VALG:qti-distro-tele = 'False'

PERF ?= 'True'
PERF:qti-distro-tele = 'False'

ATRACE ?= 'True'
ATRACE:qti-distro-tele = 'False'

# Add debug support packages to RDEPENDS list for a debug build.
# Remote debugging can be carried out(through adb port forwarding)
# on target gdb takes up considerable storage.
# Avoid gdb on target.
RDEPENDS:packagegroup-qti-debug-tools = " \
            gdbserver \
            strace \
            ${@oe.utils.conditional('VALG', 'True', 'valgrind', '', d)} \
            systemd-analyze \
            procrank \
            ${@oe.utils.conditional('PERF', 'True', 'perf', '', d)} \
            ${@oe.utils.conditional('ATRACE', 'True', 'atrace', '', d)} \
        "
