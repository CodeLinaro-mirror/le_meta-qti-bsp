# SPDX-License-Identifier: GPL-2.0-only

# This shell script carries a command that can be used to tar and untar directories
# for copying directory trees from a source to destination

tar --exclude='.git' --xattrs --xattrs-include='*' -cf - -C $1 -p . | tar -xf - -C $2
