BOOST_PARALLEL_MAKE = "${@base_less_or_equal('CPU_COUNT', '16', '-j${CPU_COUNT}', '-j16',d)}"
