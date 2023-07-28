SUMMARY = "Packag provides mesa from from OE"

PACKAGE_ARCH = "${TUNE_PKGARCH}"

inherit packagegroup

PROVIDES = "${PACKAGES}"

PACKAGES =  "\
              packagegroup-mesa \
            "

RDEPENDS:packagegroup-mesa = " \
            mesa \
            graphics-loader \
        "
