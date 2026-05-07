# OpenSSL optimization for low-memory systems
# This bbappend reduces libcrypto.so.3 size from ~5MB to ~2-3MB
# by disabling unused cryptographic algorithms and features
# NOTE: Only applies to machines with MACHINE_FEATURES containing 'qti-vm'
# NOTE: Only applies to target builds, not native/nativesdk to avoid build tool issues

# Base values (always defined, empty by default)
OPENSSL_OPTIMIZATIONS = ""
DEPRECATED_CRYPTO_FLAGS = ""

python () {
    machine_features = (d.getVar('MACHINE_FEATURES') or '').split()

    # Only apply optimizations for machines with the 'qti-vm' MACHINE_FEATURES flag
    if 'qti-vm' not in machine_features:
        bb.debug(1, "openssl bbappend: skipping size optimizations "
                    "(qti-vm not in MACHINE_FEATURES)")
        return

    bb.debug(1, "openssl bbappend: applying size optimizations for qti-vm machine")

    # Prepend FILESEXTRAPATHS
    thisdir = d.getVar('THISDIR') or ''
    pn = d.getVar('PN') or ''
    fps = d.getVar('FILESEXTRAPATHS') or ''
    d.setVar('FILESEXTRAPATHS', '%s/%s:%s' % (thisdir, pn, fps))

    # Only apply optimizations to target builds, not native tools
    d.setVar('OPENSSL_OPTIMIZATIONS:class-target', '1')

    # Disable legacy and rarely-used cryptographic algorithms
    # Keep only modern, commonly-used algorithms (AES, SHA256, RSA, ECDSA, etc.)
    deprecated_flags = (
        "no-aria "
        "no-bf "
        "no-camellia "
        "no-cast "
        "no-des "
        "no-idea "
        "no-md2 "
        "no-md4 "
        "no-mdc2 "
        "no-rc2 "
        "no-rc4 "
        "no-rc5 "
        "no-rmd160 "
        "no-seed "
        "no-siphash "
        "no-sm2 "
        "no-sm3 "
        "no-sm4 "
        "no-whirlpool "
        "no-ssl3 "
        "no-ssl3-method "
        "no-weak-ssl-ciphers"
    )
    d.setVar('DEPRECATED_CRYPTO_FLAGS:class-target', deprecated_flags)

    # Disable additional features not commonly needed in embedded systems (target only)
    extra_conf = (
        " no-engine"
        " no-hw"
        " no-autoerrinit"
        " no-comp"
        " no-cms"
        " no-ct"
        " no-srp"
        " no-srtp"
        " no-gost"
        " no-nextprotoneg"
        " no-psk"
        " no-sctp"
        " -Os"
    )
    d.setVar('EXTRA_OECONF:append:class-target', extra_conf)

    # Additional size optimizations (target only)
    d.setVar('TARGET_CFLAGS:append:class-target', ' -ffunction-sections -fdata-sections')
    d.setVar('TARGET_LDFLAGS:append:class-target', ' -Wl,--gc-sections')
}

# Note: The following algorithms are KEPT (commonly used):
# - AES (symmetric encryption)
# - SHA256, SHA384, SHA512 (hashing)
# - RSA (public key crypto)
# - ECDSA, ECDH (elliptic curve crypto)
# - DH (Diffie-Hellman - required by Python SSL module)
# - TLS 1.2, TLS 1.3 (modern TLS)
# - X509 certificates
# - HMAC
