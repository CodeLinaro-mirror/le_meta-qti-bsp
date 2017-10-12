do_strip_header_virtclass-multilib-lib32(){
    rm  ${D}/usr/include/bits/wordsize.h
}

do_strip_header(){
}

addtask do_strip_header after do_populate_sysroot before do_package
