DESCRIPTION = "ros-eagle package group"
LICENSE = "MIT"

inherit packagegroup

PACKAGES = "${PN}"

RDEPENDS_${PN} = "\
    lib32-packagegroup-ros-comm \
    lib32-actionlib \
    lib32-bond \
    lib32-bondcpp \
    lib32-bondpy \
    lib32-smclib \
    lib32-class-loader \
    lib32-actionlib-msgs \
    lib32-diagnostic-msgs \
    lib32-nav-msgs \
    lib32-geometry-msgs \
    lib32-sensor-msgs \
    lib32-shape-msgs \
    lib32-stereo-msgs \
    lib32-trajectory-msgs \
    lib32-visualization-msgs \
    lib32-dynamic-reconfigure \
    lib32-tf2 \
    lib32-tf2-msgs \
    lib32-tf2-py \
    lib32-tf2-ros \
    lib32-tf \
    lib32-image-transport \
    lib32-nodelet-topic-tools \
    lib32-nodelet \
    lib32-pluginlib \
    lib32-cmake-modules \
    lib32-rosconsole-bridge \
    lib32-ros-scripts \
    lib32-camera-info-manager \
    lib32-python-rosdep \
    lib32-python-json \
    lib32-git \
    lib32-python-empy \
    lib32-rospy-tutorials \
"
