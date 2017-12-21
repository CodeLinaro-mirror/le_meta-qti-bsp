SUMMARY = "Point-to-Point Protocol (PPP) support"
DESCRIPTION = "ppp (Paul's PPP Package) is an open source package which implements \
the Point-to-Point Protocol (PPP) on Linux and Solaris systems."

SRC_URI_remove += " \
           file://ppp-fix-building-with-linux-4.8.patch \
"

