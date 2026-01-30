FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

# Only apply optimizations to target builds, not native tools
OPENSSL_OPTIMIZATIONS = ""
OPENSSL_OPTIMIZATIONS:class-target = "1"

# Disable legacy and rarely-used cryptographic algorithms
# Keep only modern, commonly-used algorithms (AES, SHA256, RSA, ECDSA, etc.)
# Only applied to target builds
DEPRECATED_CRYPTO_FLAGS = ""
DEPRECATED_CRYPTO_FLAGS:class-target = "\
    no-aria \
    no-bf \
    no-blake2 \
    no-camellia \
    no-cast \
    no-des \
    no-idea \
    no-md2 \
    no-md4 \
    no-mdc2 \
    no-rc2 \
    no-rc4 \
    no-rc5 \
    no-rmd160 \
    no-seed \
    no-siphash \
    no-sm2 \
    no-sm3 \
    no-sm4 \
    no-whirlpool \
"

# Disable weak/deprecated SSL/TLS versions
DEPRECATED_CRYPTO_FLAGS += "\
    no-ssl3 \
    no-ssl3-method \
    no-weak-ssl-ciphers \
"

# Disable additional features not commonly needed in embedded systems
# Only applied to target builds
EXTRA_OECONF:append:class-target = " \
    no-engine \
    no-hw \
    no-autoerrinit \
    no-comp \
    no-cms \
    no-ct \
    no-srp \
    no-srtp \
    no-ts \
    no-gost \
    no-nextprotoneg \
    no-psk \
    no-sctp \
    no-ocsp \
"

# Optimize for size instead of speed (target only)
EXTRA_OECONF:append:class-target = " -Os"

# Additional size optimizations (target only)
TARGET_CFLAGS:append:class-target = " -ffunction-sections -fdata-sections"
TARGET_LDFLAGS:append:class-target = " -Wl,--gc-sections"

# Note: The following algorithms are KEPT (commonly used):
# - AES (symmetric encryption)
# - SHA256, SHA384, SHA512 (hashing)
# - RSA (public key crypto)
# - ECDSA, ECDH (elliptic curve crypto)
# - DH (Diffie-Hellman - required by Python SSL module)
# - TLS 1.2, TLS 1.3 (modern TLS)
# - X509 certificates
# - HMAC
