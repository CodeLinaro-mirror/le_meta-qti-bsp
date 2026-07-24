#Copyright (c) Qualcomm Technologies, Inc. and/or its subsidiaries.
#SPDX-License-Identifier: BSD-3-Clause-Clear

# ============================================================================
# Global Control
# ============================================================================
# Default sanitizer mode if enabled. Valid: "asan", "hwasan", "" (disabled)
QTI_SANITIZER ?= ""

# Enable all recipes (except blacklist) or only whitelist
# "1" = Enable All, "0" = Whitelist Only
QTI_SANITIZER_ENABLE_ALL ?= "0"

# ============================================================================
# Sanitizer Extra Flags Control
# ============================================================================
QTI_SANITIZER_EXTRA_CFLAGS ?= " -fno-omit-frame-pointer -mbranch-protection=none -fsanitize-recover=all "

# Additional sanitizer linker options (space-separated)
# These flags will be appended to LDFLAGS for all sanitizer modes
QTI_SANITIZER_EXTRA_LDFLAGS ?= ""

# Per-sanitizer extra flags (override the global ones if needed)
# By default, they inherit from the global EXTRA flags
QTI_SANITIZER_EXTRA_CFLAGS_ASAN ?= "-fsanitize=address ${QTI_SANITIZER_EXTRA_CFLAGS}"
QTI_SANITIZER_EXTRA_LDFLAGS_ASAN ?= "-fsanitize=address ${QTI_SANITIZER_EXTRA_LDFLAGS}"

QTI_SANITIZER_EXTRA_CFLAGS_HWASAN ?= "-fsanitize=hwaddress ${QTI_SANITIZER_EXTRA_CFLAGS}"
QTI_SANITIZER_EXTRA_LDFLAGS_HWASAN ?= "-fsanitize=hwaddress ${QTI_SANITIZER_EXTRA_LDFLAGS}"

# ============================================================================
# Sanitizer Flags Configuration
# ============================================================================

# Variable to hold custom flags that should ONLY be applied when sanitizer is enabled.
SANITIZER_CUSTOM_CFLAGS ?= ""

# --- ASAN Flags ---
SANITIZER_CFLAGS_ASAN_CLANG ?= "-shared-libsan ${QTI_SANITIZER_EXTRA_CFLAGS_ASAN}"
SANITIZER_LDFLAGS_ASAN_CLANG ?= "-fuse-ld=lld -fno-sanitize-link-runtime -L${STAGING_LIBDIR}/clang/18/lib/linux -Wl,-rpath-link,${STAGING_LIBDIR}/clang/18/lib/linux -Wl,-rpath,/usr/lib/clang/18/lib/linux -lclang_rt.asan-aarch64 -shared-libsan ${QTI_SANITIZER_EXTRA_LDFLAGS_ASAN}"

SANITIZER_CFLAGS_ASAN_GCC ?= "${QTI_SANITIZER_EXTRA_CFLAGS_ASAN}"
SANITIZER_LDFLAGS_ASAN_GCC ?= "-lasan ${QTI_SANITIZER_EXTRA_LDFLAGS_ASAN}"

# --- HWASAN Flags ---
SANITIZER_CFLAGS_HWASAN_CLANG ?= "${QTI_SANITIZER_EXTRA_CFLAGS_HWASAN}"
SANITIZER_LDFLAGS_HWASAN_CLANG ?= "-fuse-ld=lld -fno-sanitize-link-runtime -L${STAGING_LIBDIR}/clang/18/lib/linux -Wl,-rpath-link,${STAGING_LIBDIR}/clang/18/lib/linux -Wl,-rpath,/usr/lib/clang/18/lib/linux -lclang_rt.hwasan-aarch64 ${QTI_SANITIZER_EXTRA_LDFLAGS_HWASAN}"

SANITIZER_CFLAGS_HWASAN_GCC ?= " ${QTI_SANITIZER_EXTRA_CFLAGS_HWASAN}"
SANITIZER_LDFLAGS_HWASAN_GCC ?= "${QTI_SANITIZER_EXTRA_LDFLAGS_HWASAN}"

# ============================================================================
# Recipe Specific Overrides
# ============================================================================
TARGET_CXXFLAGS:append:pn-sensors-ship-qti = " -fpermissive"
REMOVE_INTEGRATED_AS_FLAG = "-no-integrated-as"
# Dont remove -no-integrated-as for fastcv-noship
REMOVE_INTEGRATED_AS_FLAG:pn-fastcv-noship = ""
TUNE_CCARGS:remove:toolchain-sdllvm = "${REMOVE_INTEGRATED_AS_FLAG}"

# Remove GCC-specific path canonicalization flag when using SDLLVM/Clang
# to prevent "unknown argument" build errors.
CFLAGS:remove:toolchain-sdllvm = "-fcanon-prefix-map"
CXXFLAGS:remove:toolchain-sdllvm = "-fcanon-prefix-map"
LDFLAGS:remove:toolchain-sdllvm = "-fcanon-prefix-map"
TARGET_CFLAGS:remove:toolchain-sdllvm = "-fcanon-prefix-map"
TARGET_CXXFLAGS:remove:toolchain-sdllvm = "-fcanon-prefix-map"

# ============================================================================
# Whitelist Configuration
# ============================================================================
# This is the default mode when QTI_SANITIZER_ENABLE_ALL = "0"
#
# Add your recipes here:
QTI_SANITIZER_WHITELIST ?= ""

# ============================================================================
# Blacklist Configuration
# ============================================================================
# Recipes in this list will NEVER have sanitizer enabled, regardless of
# QTI_SANITIZER_ENABLE_ALL or whitelist settings
#
# Add recipes here that:
#   - Are part of the sanitizer toolchain itself
#   - Have compatibility issues with sanitizers
#   - Need to be excluded for performance or technical reasons
#
QTI_SANITIZER_BLACKLIST ?= "\
    compiler-rt \
    compiler-rt-sanitizers \
    llvm-project-source \
    clang \
    llvm \
    libcxx \
    libcxxabi \
    libunwind \
    compiler-rt-native \
    clang-native \
    llvm-native \
    gcc-sanitizers \
    gcc \
    gcc-runtime \
    binutils \
    busybox \
    glibc \
    musl \
    linux-libc-headers \
    fastcv-noship \
"

# Additional recipes to blacklist (can be extended in local.conf or machine conf)
# Example:
# QTI_SANITIZER_BLACKLIST:append = " problematic-recipe another-recipe"

# ============================================================================
# Logging Functions
# ============================================================================

# Prevent Pseudo from tracking the log directory to avoid inode mismatches
# and crashes during do_package.
PSEUDO_IGNORE_PATHS .= ",${TMPDIR}/log/sanitizer"

def log_sanitizer_status(d, status, reason="", sanitizer_type="", toolchain_used=""):
    """
    Log sanitizer application status to a summary file.
    Uses file locking and build ID checking to ensure logs are cleared only once per build.
    """
    import os
    import time
    import shutil
    import bb.utils

    pn = d.getVar('PN') or 'unknown'
    pv = d.getVar('PV') or 'unknown'
    toolchain = d.getVar('TOOLCHAIN') or 'unknown'
    arch = d.getVar('PACKAGE_ARCH') or 'unknown'
    machine = d.getVar('MACHINE_ARCH') or 'unknown'
    target_arch = d.getVar('TARGET_ARCH') or 'unknown'
    file_layername = d.getVar('FILE_LAYERNAME') or 'unknown'

    # Get log directory from TMPDIR
    tmpdir = d.getVar('TMPDIR')
    if not tmpdir:
        bb.warn("TMPDIR not set, cannot log sanitizer status")
        return

    # Fixed log directory (no longer includes datetime in path)
    base_log_dir = os.path.join(tmpdir, 'log')
    log_dir = os.path.join(base_log_dir, 'sanitizer')

    # Define lockfile path (must be OUTSIDE the directory we might delete)
    # This prevents deleting the lock file while another process is waiting on it
    lockfile_path = os.path.join(log_dir, '.sanitizer.lock')
    
    # Ensure base log dir exists for the lockfile
    try:
        os.makedirs(log_dir, exist_ok=True)
    except OSError:
        pass

    # This block is critical to prevent race conditions where multiple recipes
    # try to clean the directory simultaneously.
    lf = bb.utils.lockfile(lockfile_path)

    try:
        # Get the unique ID for this specific bitbake run
        current_build_id = d.getVar('DATETIME') or time.strftime('%Y%m%d%H%M%S')
        build_id_file = os.path.join(log_dir, '.build_id')
        enabled_log_name = "sanitizer-enabled_%s.log" % current_build_id
        skipped_log_name = "sanitizer-skipped_%s.log" % current_build_id
        summary_csv_name = "sanitizer-summary_%s.csv" % current_build_id
        marker_dir = os.path.join(log_dir, '.markers')
        
        should_clean = True

        # Check if the existing directory belongs to the current build
        if os.path.exists(build_id_file):
            try:
                with open(build_id_file, 'r') as f:
                    stored_id = f.read().strip()
                if stored_id == current_build_id:
                    should_clean = False
            except Exception:
                # If we can't read the ID, assume it's corrupt/old and clean it
                should_clean = True
        
        # If this is the first time this build is accessing the log dir, clean it
        if should_clean:
            if os.path.exists(marker_dir):
                try:
                    shutil.rmtree(marker_dir)
                except Exception as e:
                    bb.warn("Failed to clean sanitizer log dir: %s" % str(e))
            
            # Recreate directory and write current build ID
            os.makedirs(marker_dir, exist_ok=True)
            with open(build_id_file, 'w') as f:
                f.write(current_build_id)

        # ---------------------------------------------------------
        # Deduplication Logic
        # ---------------------------------------------------------
        os.makedirs(marker_dir, exist_ok=True)
        marker_file = os.path.join(marker_dir, pn)
        
        if os.path.exists(marker_file):
            return # Already logged this recipe in this build

        # Mark as logged
        with open(marker_file, 'w') as f:
            f.write("1")

        # ---------------------------------------------------------
        # Write Log Entry
        # ---------------------------------------------------------
        timestamp_str = time.strftime('%Y-%m-%d %H:%M:%S')

        if status == "ENABLED":
            log_file = os.path.join(log_dir, enabled_log_name)
        else:
            log_file = os.path.join(log_dir, skipped_log_name)

        log_entry = {
            'timestamp': timestamp_str,
            'recipe': pn,
            'version': pv,
            'status': status,
            'sanitizer': sanitizer_type.upper() if sanitizer_type else 'N/A',
            'toolchain': toolchain,
            'toolchain_used': toolchain_used.upper() if toolchain_used else 'N/A',
            'target_arch': target_arch,
            'package_arch': arch,
            'machine_arch': machine,
            'layer': file_layername,
            'reason': reason or 'N/A'
        }

        # Write TXT Log
        file_is_new = not os.path.exists(log_file) or os.path.getsize(log_file) == 0
        with open(log_file, 'a') as f:
            if file_is_new:
                header = "# Sanitizer Status Log\n"
                header += "# Format: TIMESTAMP | RECIPE | VERSION | STATUS | SANITIZER | TOOLCHAIN | TOOLCHAIN_USED | TARGET_ARCH | PACKAGE_ARCH | MACHINE_ARCH | LAYER | REASON\n"
                header += "#" + "="*150 + "\n"
                f.write(header)
            
            line = "{timestamp} | {recipe:30s} | {version:15s} | {status:8s} | {sanitizer:7s} | {toolchain:10s} | {toolchain_used:7s} | {target_arch:10s} | {package_arch:15s} | {machine_arch:15s} | {layer:20s} | {reason}\n".format(**log_entry)
            f.write(line)
            f.flush()

        # Write CSV Summary
        summary_file = os.path.join(log_dir, summary_csv_name)
        file_exists = os.path.exists(summary_file) and os.path.getsize(summary_file) > 0
        
        with open(summary_file, 'a') as f:
            if not file_exists:
                f.write("Timestamp,Recipe,Version,Status,Sanitizer,Toolchain,ToolchainUsed,TargetArch,PackageArch,MachineArch,Layer,Reason\n")
            
            csv_entry = {k: str(v).replace('"', '""') for k, v in log_entry.items()}
            csv_line = '"{timestamp}","{recipe}","{version}","{status}","{sanitizer}","{toolchain}","{toolchain_used}","{target_arch}","{package_arch}","{machine_arch}","{layer}","{reason}"\n'.format(**csv_entry)
            f.write(csv_line)
            f.flush()

    except Exception as e:
        bb.warn("Failed to write sanitizer log for %s: %s" % (pn, str(e)))
    finally:
        # RELEASE LOCK
        bb.utils.unlockfile(lf)