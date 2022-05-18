#Enable the gcc compile option with libgomp.

EXTRA_OECONF_INITIAL:remove = " --disable-libgomp "
EXTRA_OECONF_INITIAL:append = " --enable-libgomp "

