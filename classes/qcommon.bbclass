#
# Common bitbake recipe information for QTI meta layers.
# Below are common values, statements and functions.
#
inherit autotools-brokensep pkgconfig

FILESPATH        =+ "${WORKSPACE}:"

PACKAGE_ARCH    ?= "${BASEMACHINE_ARCH}"

python __anonymous() {
    # For parsing manifest xml file
    import xml.etree.ElementTree as ET

    workspace = d.getVar('WORKSPACE', True)
    f = open(workspace + "/.repo/manifests/default.xml")
    manifest = f.read()
    f.close()

    root = ET.fromstring(manifest)

    if (d.getVar('USE_LOCAL_FILE_FETCHER', True) == '1'):
        # Convert SRC_URI as 'file' type and point to local repo paths.
        srcuri = (d.getVar('SRC_URI', True) or "").split()
        if len(srcuri) == 0:
            bb.warn("%s: SRC_URI is not set so can't fetch sources" % d.getVar('FILE', True))
        else:
            newuri = []
            qservers = (d.getVar("CAF_GIT_SERVERS", True) or "").split()
            for u in srcuri:
                qserveruri = False
                for s in qservers:
                    if u.startswith(s):
                        u = "" + u.replace(s + "/", '')
                        qserveruri = True
                # Change CAF server path to local path.
                if qserveruri:
                    # Get project name and ignore tail of SRC_URI.
                    u = u.split(".git;")[0]
                    # Find project path in manifest and set it as SRC_URI.
                    for child in root:
                        if child.tag == "project":
                            path = child.get("path")
                            name = child.get("name")
                            if name == u:
                                newuri.append("file://" + path + "/")
                else:
                    # Retain non CAF server paths as is.
                    newuri.append(u)
            # Parsing SRC_URI completed. Use newuri as SRC_URI.
            d.setVar('SRC_URI', ' '.join(newuri))
}
