SRC_URI := "git://github.com/GENIVI/capicxx-core-runtime.git"

SRCREV="2fd0625d21f1fa8e6a3adfc89ce9f381a4d33990"

do_configure_prepend () {
   sed -i -e "s/set(DEF_INSTALL_CMAKE_DIR lib\/cmake\/CommonAPI-\${COMPONENT_VERSION})/set(DEF_INSTALL_CMAKE_DIR \${INSTALL_LIB_DIR}\/cmake\/CommonAPI-\${COMPONENT_VERSION})/g" ${S}/CMakeLists.txt
   sed -i -e "s/install(FILES \${PROJECT_BINARY_DIR}\/CommonAPI.pc DESTINATION lib\/pkgconfig)/install(FILES \${PROJECT_BINARY_DIR}\/CommonAPI.pc DESTINATION \${INSTALL_LIB_DIR}\/pkgconfig)/g" ${S}/CMakeLists.txt
}
