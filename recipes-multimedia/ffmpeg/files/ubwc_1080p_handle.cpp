/*
 * Copyright (c) 2020, The Linux Foundation. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above
 *       copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials provided
 *       with the distribution.
 *     * Neither the name of The Linux Foundation nor the names of its
 *       contributors may be used to endorse or promote products derived
 *       from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
 * IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

#include <stdio.h>
#include <ion/ion.h>
#include <sys/ioctl.h>
#include <sys/mman.h>
#include <opencv2/opencv.hpp>
#include "image_convert.h"
#include "common_interfaces.h"

static int rgba_240p_ion_fd[MAX_CHANNELS];
static void *rgba_240p_uspace_addr[MAX_CHANNELS];
static cv::Mat frame_input[MAX_CHANNELS];
static std::string window_name[MAX_CHANNELS];

int ubwc_1080p_show_init(int channels)
{
	int i, ret;

	if (channels < 0) {
		printf("%s: Invalid arguments!\n", __FUNCTION__);
		return -1;
	}

	for (i = 0; i < channels; i++) {
		alloc_ion_buffer(&rgba_240p_ion_fd[i], RGBA8888_240P_SIZE);
		ret = userspace_mmap(rgba_240p_ion_fd[i], RGBA8888_240P_SIZE, &rgba_240p_uspace_addr[i]);
		if (ret != 0) {
			printf("%s: map to 240p userspace failed!\n", __FUNCTION__);
			return -1;
		}
		
		frame_input[i].create(240, 320, CV_8UC4);
		frame_input[i].data = (unsigned char *)rgba_240p_uspace_addr[i];
		window_name[i] = std::to_string(i);
	}

	return 0;
}

int ubwc_1080p_show(int channels)
{
	int i;
	
	if (channels < 0) {
		printf("%s: Invalid argument!\n", __FUNCTION__);
		return -1;
	}
	
	for (i = 0; i < channels; i++) {
		cv::imshow(window_name[i], frame_input[i]);
	}

	cv::waitKey(35);

	return 0;
}

static int ubwc_1080p_to_rgba8888_240p(int ion_fd_ubwc_1080p, int ion_fd_rgba_240p)
{
	QImgConv::Image src, dst;
	int ret;

	src.fd     = ion_fd_ubwc_1080p;
	src.format = PIXEL_FORMAT_YUV_UBWC;
	src.width  = 1920;
	src.height = 1080;

	dst.fd     = ion_fd_rgba_240p;
	dst.format = PIXEL_FORMAT_RGBA_8888;
	dst.width  = 320;
	dst.height = 240;

	ret = QImgConv::formatConvert(&src, &dst);
	if (ret != 0) {
		printf("%s: fomatConvert failed!\n", __FUNCTION__);
		return ret;
	}

	return 0;
}

int ubwc_1080p_format_convert(int ubwc_1080p_fd, int channel)
{
	if (channel < 0 || channel >= MAX_CHANNELS) {
		printf("%s: Invalid channel: %d !\n", __FUNCTION__, channel);
		return -1;
	}

	ubwc_1080p_to_rgba8888_240p(ubwc_1080p_fd, rgba_240p_ion_fd[channel]);

	return 0;
}
