# freetype 2.11.1 fails to compile with "-pie -fPIE", use "-fPIC" instead
# Error:
#     libtool: compile: unable to infer tagged configuration
#     libtool: compile: specify a tag with `--tag'
SECURITY_CFLAGS = "${SECURITY_PIC_CFLAGS}"
