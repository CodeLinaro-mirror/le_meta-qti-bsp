#Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
#SPDX-License-Identifier: BSD-3-Clause-Clear

include conf/distro/include/qti-sanitizer.inc

# ============================================================================
# Flag Generation Logic
# ============================================================================

def get_sanitizer_flags(d, var_type):
    """
    Returns the appropriate flags (CFLAGS or LDFLAGS) based on the internally
    set mode from the anonymous python function.
    """
    # Retrieve the mode decided by the anonymous function
    mode = d.getVar('QTI_SANITIZER_INTERNAL_MODE')
    if not mode:
        return ""

    import bb.utils
    toolchain = d.getVar('TOOLCHAIN') or 'gcc'
    use_gcc = should_use_gcc_sanitizer(d) or (toolchain == 'gcc')
    suffix = "GCC" if use_gcc else "CLANG"

    # Construct variable name, e.g., SANITIZER_CFLAGS_ASAN_CLANG
    var_name = "SANITIZER_%s_%s_%s" % (var_type, mode, suffix)
    flags = d.getVar(var_name) or ""

    if var_type == 'CFLAGS':
        # 1. Apply generic custom flags
        custom_flags = d.getVar('SANITIZER_CUSTOM_CFLAGS')
        if custom_flags:
            flags += " " + custom_flags

        # 2. Check for Ignorelist file
        pn = d.getVar('PN')
        search_path = "conf/sanitizer-ignorelist/" + pn
        ignore_file = bb.utils.which(d.getVar('BBPATH'), search_path)
        if ignore_file:
            # Mark this file as a dependency so changes trigger rebuild
            bb.parse.mark_dependency(d, ignore_file)
            flags += " -fsanitize-ignorelist=%s" % ignore_file

    return " " + flags if flags else ""

# Track variable dependencies to ensure re-compilation on change
get_sanitizer_flags[vardeps] += "\
    SANITIZER_CFLAGS_ASAN_CLANG SANITIZER_LDFLAGS_ASAN_CLANG \
    SANITIZER_CFLAGS_ASAN_GCC SANITIZER_LDFLAGS_ASAN_GCC \
    SANITIZER_CFLAGS_HWASAN_CLANG SANITIZER_LDFLAGS_HWASAN_CLANG \
    SANITIZER_CFLAGS_HWASAN_GCC SANITIZER_LDFLAGS_HWASAN_GCC \
    SANITIZER_CUSTOM_CFLAGS \
"

# Inject flags into standard variables
TARGET_CFLAGS:append = "${@get_sanitizer_flags(d, 'CFLAGS')}"
TARGET_CXXFLAGS:append = "${@get_sanitizer_flags(d, 'CFLAGS')}"
LDFLAGS:append = "${@get_sanitizer_flags(d, 'LDFLAGS')}"

# ============================================================================
# Helper Functions
# ============================================================================

def should_use_gcc_sanitizer(d):
    """
    Determine if GCC sanitizer implementation should be used instead of Clang.
    """
    toolchain = d.getVar('TOOLCHAIN') or ''
    layername = d.getVar('FILE_LAYERNAME') or ''
    arch = d.getVar('PACKAGE_ARCH')
    machine = d.getVar('MACHINE_ARCH')

    return toolchain == 'gcc' and 'qti' in layername and arch == machine

def check_sanitizer_filters(d):
    """
    Main filtering logic to decide if a recipe should be sanitized.
    Returns True if sanitizer should be enabled, False otherwise.
    """
    pn = d.getVar('PN')

    # Step 1: Always skip native and nativesdk recipes
    if pn.endswith('-native') or pn.endswith('-nativesdk'):
        return False

    # Step 2: Check global blacklist
    blacklist = (d.getVar('QTI_SANITIZER_BLACKLIST') or '').split()
    if pn in blacklist:
        return False

    # Step 3: Filter by name patterns
    skip_patterns = ('packagegroup', 'kernel', 'headers', 'dlkm', 'linux-msm', 
                     'devicetree', 'image', '-image-', 'adreno')
    if any(pattern in pn for pattern in skip_patterns):
        return False

    # Step 4: Layer-based filtering
    file_layername = d.getVar('FILE_LAYERNAME') or 'unknown'
    # Only allow QTI layers by default
    if not file_layername.startswith('qti-') and file_layername != 'qcom-data':
        return False

    # Skip specific QTI layers with known issues
    skip_layers = ('qti-gst', 'qt-bt', 'qti-bt-prop', 'qti-core', 'qti-core-prop', 'qti-wlan', 'qti-wlan-prop')
    if file_layername in skip_layers:
        return False

    # Step 5: Whitelist vs Enable-All mode
    enable_all = d.getVar('QTI_SANITIZER_ENABLE_ALL') or '0'
    if enable_all == "1":
        return True
    
    whitelist = (d.getVar('QTI_SANITIZER_WHITELIST') or '').split()
    return pn in whitelist

# ============================================================================
# Main Anonymous Python Function
# ============================================================================

python __anonymous() {
    # Early exit if QTI_SANITIZER is not set
    global_sanitizer_raw = d.getVar('QTI_SANITIZER') or ''
    if not global_sanitizer_raw:
        return

    # Handle conflict: both asan and hwasan configured
    san_list = global_sanitizer_raw.split()
    if 'asan' in san_list and 'hwasan' in san_list:
        bb.warn("%s: Both 'asan' and 'hwasan' found in QTI_SANITIZER. ASAN will take priority." % d.getVar('PN'))
        selected_mode = 'asan'
    elif 'asan' in san_list:
        selected_mode = 'asan'
    elif 'hwasan' in san_list:
        selected_mode = 'hwasan'
    else:
        bb.debug(2, "No supported sanitizer found in QTI_SANITIZER: %s" % global_sanitizer_raw)
        return

    pn = d.getVar('PN')
    toolchain = d.getVar('TOOLCHAIN') or 'unknown'

    # Step 1: Run filter logic
    if not check_sanitizer_filters(d):
        log_sanitizer_status(d, "SKIPPED", "Filtered out by whitelist/blacklist logic", selected_mode, "")
        return
 
    # Step 2: Validate toolchain support
    if toolchain not in ['sdllvm', 'gcc']:
        log_sanitizer_status(d, "SKIPPED", "Toolchain '%s' not supported" % toolchain, selected_mode, "")
        return

    # Step 3: Set internal mode for flag generation
    d.setVar('QTI_SANITIZER_INTERNAL_MODE', selected_mode.upper())

    # Step 4: Inject runtime dependencies
    if toolchain == 'sdllvm':
        if pn not in ['compiler-rt', 'compiler-rt-sanitizers', 'llvm-project-source']:
            d.appendVar('DEPENDS', ' compiler-rt-sanitizers')
    elif toolchain == 'gcc':
        if pn not in ['gcc-sanitizers', 'gcc', 'gcc-runtime']:
            d.appendVar('DEPENDS', ' gcc-sanitizers')   

    toolchain_used = 'gcc' if should_use_gcc_sanitizer(d) else toolchain
    log_sanitizer_status(d, "ENABLED", "Auto-enabled via QTI_SANITIZER", selected_mode, toolchain_used)
}