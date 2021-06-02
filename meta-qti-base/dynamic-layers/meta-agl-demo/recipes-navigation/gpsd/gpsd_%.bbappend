# Explicitly disable python module support to avoid failures
# stemming from seeing the host python.  This can be removed
# when python and python2 are removed from HOSTTOOLS_NONFATAL.
EXTRA_OESCONS += "python='false'"
