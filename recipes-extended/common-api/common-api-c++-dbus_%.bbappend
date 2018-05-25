DEPENDS += "gtest"

SRC_URI := "git://github.com/GENIVI/capicxx-dbus-runtime.git"
SRCREV = "1b5f83a9bcfcf5b36e183eace369cdfadb5d295f"

do_configure_prepend () {
    sed -i -e "s/set(DEF_INSTALL_CMAKE_DIR lib\/cmake\/CommonAPI-DBus-\${COMPONENT_VERSION})/set(DEF_INSTALL_CMAKE_DIR \${INSTALL_LIB_DIR}\/cmake\/CommonAPI-DBus-\${COMPONENT_VERSION})/g" ${S}/CMakeLists.txt
    sed -i -e "s/install(FILES \${PROJECT_BINARY_DIR}\/CommonAPI-DBus.pc DESTINATION lib\/pkgconfig)/install(FILES \${PROJECT_BINARY_DIR}\/CommonAPI-DBus.pc DESTINATION \${INSTALL_LIB_DIR}\/pkgconfig)/g" ${S}/CMakeLists.txt
}
#rb1.4SRC_URI_remove = "git://git.projects.genivi.org/ipc/common-api-dbus-runtime.git;protocol=http"
#rb1.4SRC_URI_append = " ${CAF_GIT}/genivi/ipc/common-api-dbus-runtime;protocol=git;branch=genivi/common-api-dbus-runtime/master "

