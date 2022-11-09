SUMMARY = "Package include components SDK depends in esdk"

inherit packagegroup

PACKAGES =  " \
              ${PN} \
              ${PN}-ros \
              ${PN}-robotics \
            "

#ROS SDK  component
RDEPENDS_${PN}-ros = " \
"


#Robotics  SDK component
RDEPENDS_${PN}-robotics=" "
