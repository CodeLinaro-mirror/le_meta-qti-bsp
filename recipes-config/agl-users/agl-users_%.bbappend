
USERADD_PACKAGES = "${PN}"

USERADD_PARAM_${PN} = "\
  -g users -d /home/agl-driver -m -K PASS_MAX_DAYS=-1 agl-driver ; \
  -g users -d /home/agl-passenger -m -K PASS_MAX_DAYS=-1 agl-passenger ; \
  -g display --system display ; \
  -g display --system cvbs ; \
  -g display --system ethcam ; \
  ais_server \
"
GROUPADD_PARAM_${PN} = "display"

# As AGL4.0 has cleanup these operation

pkg_postinst_${PN}() {
    :
}

pkg_postinst_${PN}_smack() {
    :
}
