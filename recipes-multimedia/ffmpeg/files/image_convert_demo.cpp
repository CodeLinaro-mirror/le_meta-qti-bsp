Copyright (c) 2020, The Linux Foundation. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above
      copyright notice, this list of conditions and the following
      disclaimer in the documentation and/or other materials provided
      with the distribution.
    * Neither the name of The Linux Foundation nor the names of its
      contributors may be used to endorse or promote products derived
      from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESS OR IMPLIED
WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT
ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

#include <stdio.h>
#include <ion/ion.h>
#include <sys/ioctl.h>
#include <sys/stat.h>
#include <sys/mman.h>
#include <opencv2/opencv.hpp>
#include "image_convert.h"
#include "common_interfaces.h"

int yuv2rgb_1080p(int yuv_ion_fd, int yuv_format, int rgb_ion_fd, int rgb_format)
{
	QImgConv::Image yuv_1080p, rgb_1080p;
	int ret;

	yuv_1080p.fd     = yuv_ion_fd;
	yuv_1080p.format = yuv_format;
	yuv_1080p.width  = 1920;
	yuv_1080p.height = 1080;

	rgb_1080p.fd    = rgb_ion_fd;
	rgb_1080p.format = rgb_format;
	rgb_1080p.width  = 1920;
	rgb_1080p.height = 1080;

	ret = QImgConv::formatConvert(&yuv_1080p, &rgb_1080p);
	if (ret != 0) {
		printf("%s: fomatConvert failed!\n", __FUNCTION__);
		return ret;
	}

	return 0;
}

int yuv2rgb_720p(int yuv_ion_fd, int yuv_format, int rgb_ion_fd, int rgb_format)
{
	QImgConv::Image yuv_720p, rgb_720p;
	int ret;

	yuv_720p.fd     = yuv_ion_fd;
	yuv_720p.format = yuv_format;
	yuv_720p.width  = 1280;
	yuv_720p.height = 720;

	rgb_720p.fd    = rgb_ion_fd;
	rgb_720p.format = rgb_format;
	rgb_720p.width  = 1280;
	rgb_720p.height = 720;

	ret = QImgConv::formatConvert(&yuv_720p, &rgb_720p);
	if (ret != 0) {
		printf("%s: fomatConvert failed!\n", __FUNCTION__);
		return ret;
	}

	return 0;
}

int yuv2rgb_480p(int yuv_ion_fd, int yuv_format, int rgb_ion_fd, int rgb_format)
{
	QImgConv::Image yuv_480p, rgb_480p;
	int ret;

	yuv_480p.fd     = yuv_ion_fd;
	yuv_480p.format = yuv_format;
	yuv_480p.width  = 640;
	yuv_480p.height = 480;

	rgb_480p.fd    = rgb_ion_fd;
	rgb_480p.format = rgb_format;
	rgb_480p.width  = 640;
	rgb_480p.height = 480;

	ret = QImgConv::formatConvert(&yuv_480p, &rgb_480p);
	if (ret != 0) {
		printf("%s: fomatConvert failed!\n", __FUNCTION__);
		return ret;
	}

	return 0;
}

int yuv2rgb_240p(int yuv_ion_fd, int yuv_format, int rgb_ion_fd, int rgb_format)
{
	QImgConv::Image yuv_240p, rgb_240p;
	int ret;

	yuv_240p.fd     = yuv_ion_fd;
	yuv_240p.format = yuv_format;
	yuv_240p.width  = 320;
	yuv_240p.height = 240;

	rgb_240p.fd    = rgb_ion_fd;
	rgb_240p.format = rgb_format;
	rgb_240p.width  = 320;
	rgb_240p.height = 240;

	ret = QImgConv::formatConvert(&yuv_240p, &rgb_240p);
	if (ret != 0) {
		printf("%s: fomatConvert failed!\n", __FUNCTION__);
		return ret;
	}

	return 0;
}

//fixd size to 240p
int yuv_1080p_resize(int ion_fd_src, int yuv_format, int ion_fd_dst)
{
	QImgConv::Image src, dst;
	int ret;

	src.fd     = ion_fd_src;
	src.format = yuv_format;
	src.width  = 1920;
	src.height = 1080;

	dst.fd    = ion_fd_dst;
	dst.format = yuv_format;
	dst.width  = 320;
	dst.height = 240;

	ret = QImgConv::formatConvert(&src, &dst);
	if (ret != 0) {
		printf("%s: fomatConvert failed!\n", __FUNCTION__);
		return ret;
	}

	return 0;
}

int yuv_720p_resize(int ion_fd_src, int yuv_format, int ion_fd_dst)
{
	QImgConv::Image src, dst;
	int ret;

	src.fd     = ion_fd_src;
	src.format = yuv_format;
	src.width  = 1280;
	src.height = 720;

	dst.fd    = ion_fd_dst;
	dst.format = yuv_format;
	dst.width  = 320;
	dst.height = 240;

	ret = QImgConv::formatConvert(&src, &dst);
	if (ret != 0) {
		printf("%s: fomatConvert failed!\n", __FUNCTION__);
		return ret;
	}

	return 0;
}

int yuv_480p_resize(int ion_fd_src, int yuv_format, int ion_fd_dst)
{
	QImgConv::Image src, dst;
	int ret;

	src.fd     = ion_fd_src;
	src.format = yuv_format;
	src.width  = 640;
	src.height = 480;

	dst.fd    = ion_fd_dst;
	dst.format = yuv_format;
	dst.width  = 320;
	dst.height = 240;

	ret = QImgConv::formatConvert(&src, &dst);
	if (ret != 0) {
		printf("%s: fomatConvert failed!\n", __FUNCTION__);
		return ret;
	}

	return 0;
}

int rgb_1080p_resize(int ion_fd_src, int rgb_format, int ion_fd_dst)
{
	QImgConv::Image rgb_src, rgb_dst;
	int ret;

	rgb_src.fd     = ion_fd_src;
	rgb_src.format = rgb_format;
	rgb_src.width  = 1920;
	rgb_src.height = 1080;

	rgb_dst.fd    = ion_fd_dst;
	rgb_dst.format = rgb_format;
	rgb_dst.width  = 320;
	rgb_dst.height = 240;

	ret = QImgConv::formatConvert(&rgb_src, &rgb_dst);
	if (ret != 0) {
		printf("%s: fomatConvert failed!\n", __FUNCTION__);
		return ret;
	}

	return 0;
}

int rgb_720p_resize(int ion_fd_src, int rgb_format, int ion_fd_dst)
{
	QImgConv::Image rgb_src, rgb_dst;
	int ret;

	rgb_src.fd     = ion_fd_src;
	rgb_src.format = rgb_format;
	rgb_src.width  = 1280;
	rgb_src.height = 720;

	rgb_dst.fd    = ion_fd_dst;
	rgb_dst.format = rgb_format;
	rgb_dst.width  = 320;
	rgb_dst.height = 240;

	ret = QImgConv::formatConvert(&rgb_src, &rgb_dst);
	if (ret != 0) {
		printf("%s: fomatConvert failed!\n", __FUNCTION__);
		return ret;
	}

	return 0;
}

int rgb_480p_resize(int ion_fd_src, int rgb_format, int ion_fd_dst)
{
	QImgConv::Image rgb_src, rgb_dst;
	int ret;

	rgb_src.fd     = ion_fd_src;
	rgb_src.format = rgb_format;
	rgb_src.width  = 640;
	rgb_src.height = 480;

	rgb_dst.fd    = ion_fd_dst;
	rgb_dst.format = rgb_format;
	rgb_dst.width  = 320;
	rgb_dst.height = 240;

	ret = QImgConv::formatConvert(&rgb_src, &rgb_dst);
	if (ret != 0) {
		printf("%s: fomatConvert failed!\n", __FUNCTION__);
		return ret;
	}

	return 0;
}

int nv12_1080p_to_rgba8888_720p(int ion_fd_nv12_1080p, int ion_fd_rgba_720p)
{
	QImgConv::Image src, dst;
	int ret;

	src.fd     = ion_fd_nv12_1080p;
	src.format = PIXEL_FORMAT_YUV_NV12;
	src.width  = 1920;
	src.height = 1080;

	dst.fd     = ion_fd_rgba_720p;
	dst.format = PIXEL_FORMAT_RGBA_8888;
	dst.width  = 1280;
	dst.height = 720;

	ret = QImgConv::formatConvert(&src, &dst);
	if (ret != 0) {
		printf("%s: fomatConvert failed!\n", __FUNCTION__);
		return ret;
	}

	return 0;
}

int nv12_1080p_to_rgba8888_480p(int ion_fd_nv12_1080p, int ion_fd_rgba_480p)
{
	QImgConv::Image src, dst;
	int ret;

	src.fd     = ion_fd_nv12_1080p;
	src.format = PIXEL_FORMAT_YUV_NV12;
	src.width  = 1920;
	src.height = 1080;

	dst.fd     = ion_fd_rgba_480p;
	dst.format = PIXEL_FORMAT_RGBA_8888;
	dst.width  = 640;
	dst.height = 480;

	ret = QImgConv::formatConvert(&src, &dst);
	if (ret != 0) {
		printf("%s: fomatConvert failed!\n", __FUNCTION__);
		return ret;
	}

	return 0;
}

int nv12_1080p_to_rgba8888_240p(int ion_fd_nv12_1080p, int ion_fd_rgba_240p)
{
	QImgConv::Image src, dst;
	int ret;

	src.fd     = ion_fd_nv12_1080p;
	src.format = PIXEL_FORMAT_YUV_NV12;
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

int nv12_1080p_to_rgb888_720p(int ion_fd_nv12_1080p, int ion_fd_rgb888_720p)
{
	QImgConv::Image src, dst;
	int ret;

	src.fd     = ion_fd_nv12_1080p;
	src.format = PIXEL_FORMAT_YUV_NV12;
	src.width  = 1920;
	src.height = 1080;

	dst.fd     = ion_fd_rgb888_720p;
	dst.format = PIXEL_FORMAT_RGB_888;
	dst.width  = 1280;
	dst.height = 720;

	ret = QImgConv::formatConvert(&src, &dst);
	if (ret != 0) {
		printf("%s: fomatConvert failed!\n", __FUNCTION__);
		return ret;
	}

	return 0;
}

int nv12_1080p_to_rgb888_480p(int ion_fd_nv12_1080p, int ion_fd_rgb888_480p)
{
	QImgConv::Image src, dst;
	int ret;

	src.fd     = ion_fd_nv12_1080p;
	src.format = PIXEL_FORMAT_YUV_NV12;
	src.width  = 1920;
	src.height = 1080;

	dst.fd     = ion_fd_rgb888_480p;
	dst.format = PIXEL_FORMAT_RGB_888;
	dst.width  = 640;
	dst.height = 480;

	ret = QImgConv::formatConvert(&src, &dst);
	if (ret != 0) {
		printf("%s: fomatConvert failed!\n", __FUNCTION__);
		return ret;
	}

	return 0;
}

int nv12_1080p_to_rgb888_240p(int ion_fd_nv12_1080p, int ion_fd_rgb888_240p)
{
	QImgConv::Image src, dst;
	int ret;

	src.fd     = ion_fd_nv12_1080p;
	src.format = PIXEL_FORMAT_YUV_NV12;
	src.width  = 1920;
	src.height = 1080;

	dst.fd     = ion_fd_rgb888_240p;
	dst.format = PIXEL_FORMAT_RGB_888;
	dst.width  = 320;
	dst.height = 240;

	ret = QImgConv::formatConvert(&src, &dst);
	if (ret != 0) {
		printf("%s: fomatConvert failed!\n", __FUNCTION__);
		return ret;
	}

	return 0;
}

int ubwc_1080p_to_rgba8888_720p(int ion_fd_ubwc_1080p, int ion_fd_rgba_720p)
{
	QImgConv::Image src, dst;
	int ret;

	src.fd     = ion_fd_ubwc_1080p;
	src.format = PIXEL_FORMAT_YUV_UBWC;
	src.width  = 1920;
	src.height = 1080;

	dst.fd     = ion_fd_rgba_720p;
	dst.format = PIXEL_FORMAT_RGBA_8888;
	dst.width  = 1280;
	dst.height = 720;

	ret = QImgConv::formatConvert(&src, &dst);
	if (ret != 0) {
		printf("%s: fomatConvert failed!\n", __FUNCTION__);
		return ret;
	}

	return 0;
}

int ubwc_1080p_to_rgba8888_480p(int ion_fd_ubwc_1080p, int ion_fd_rgba_480p)
{
	QImgConv::Image src, dst;
	int ret;

	src.fd     = ion_fd_ubwc_1080p;
	src.format = PIXEL_FORMAT_YUV_UBWC;
	src.width  = 1920;
	src.height = 1080;

	dst.fd     = ion_fd_rgba_480p;
	dst.format = PIXEL_FORMAT_RGBA_8888;
	dst.width  = 640;
	dst.height = 480;

	ret = QImgConv::formatConvert(&src, &dst);
	if (ret != 0) {
		printf("%s: fomatConvert failed!\n", __FUNCTION__);
		return ret;
	}

	return 0;
}

int ubwc_1080p_to_rgba8888_240p(int ion_fd_ubwc_1080p, int ion_fd_rgba_240p)
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

int ubwc_1080p_to_rgb888_720p(int ion_fd_ubwc_1080p, int ion_fd_rgb888_720p)
{
	QImgConv::Image src, dst;
	int ret;

	src.fd     = ion_fd_ubwc_1080p;
	src.format = PIXEL_FORMAT_YUV_UBWC;
	src.width  = 1920;
	src.height = 1080;

	dst.fd     = ion_fd_rgb888_720p;
	dst.format = PIXEL_FORMAT_RGB_888;
	dst.width  = 1280;
	dst.height = 720;

	ret = QImgConv::formatConvert(&src, &dst);
	if (ret != 0) {
		printf("%s: fomatConvert failed!\n", __FUNCTION__);
		return ret;
	}

	return 0;
}

int ubwc_1080p_to_rgb888_480p(int ion_fd_ubwc_1080p, int ion_fd_rgb888_480p)
{
	QImgConv::Image src, dst;
	int ret;

	src.fd     = ion_fd_ubwc_1080p;
	src.format = PIXEL_FORMAT_YUV_UBWC;
	src.width  = 1920;
	src.height = 1080;

	dst.fd     = ion_fd_rgb888_480p;
	dst.format = PIXEL_FORMAT_RGB_888;
	dst.width  = 640;
	dst.height = 480;

	ret = QImgConv::formatConvert(&src, &dst);
	if (ret != 0) {
		printf("%s: fomatConvert failed!\n", __FUNCTION__);
		return ret;
	}

	return 0;
}

int ubwc_1080p_to_rgb888_240p(int ion_fd_ubwc_1080p, int ion_fd_rgb888_240p)
{
	QImgConv::Image src, dst;
	int ret;

	src.fd     = ion_fd_ubwc_1080p;
	src.format = PIXEL_FORMAT_YUV_UBWC;
	src.width  = 1920;
	src.height = 1080;

	dst.fd     = ion_fd_rgb888_240p;
	dst.format = PIXEL_FORMAT_RGB_888;
	dst.width  = 320;
	dst.height = 240;

	ret = QImgConv::formatConvert(&src, &dst);
	if (ret != 0) {
		printf("%s: fomatConvert failed!\n", __FUNCTION__);
		return ret;
	}

	return 0;
}

int image_show_rgba8888(int ion_fd, int ion_size, int width, int height)
{
	int ret;
	void *userspace_addr;

	if (ion_fd < 0 || width <= 0 || height <= 0) {
		printf("%s: Invalid argument!\n", __FUNCTION__);
		return -1;
	}

	ret = userspace_mmap(ion_fd, ion_size, &userspace_addr);
	if (ret != 0) {
		printf("%s: map to userspace failed!\n", __FUNCTION__);
		return -1;
	}

	cv::Mat rgba_mat(height, width, CV_8UC4, userspace_addr, width * 4);

	cv::imshow("RGB8888", rgba_mat);
#if 0
	cv::waitKey();
	userspace_munmap(userspace_addr, ion_size);
#endif
	return 0;
}

int image_show_rgb888(int ion_fd, int ion_size, int width, int height)
{
	int ret;
	void *userspace_addr;

	if (ion_fd < 0 || width <= 0 || height <= 0) {
		printf("%s: Invalid argument!\n", __FUNCTION__);
		return -1;
	}

	ret = userspace_mmap(ion_fd, ion_size, &userspace_addr);
	if (ret != 0) {
		printf("%s: map to userspace failed!\n", __FUNCTION__);
		return -1;
	}

	cv::Mat rgba_mat(height, width, CV_8UC3, userspace_addr, width * 3);
	
	cv::imshow("RGB888", rgba_mat);

#if 0
	cv::waitKey();
	userspace_munmap(userspace_addr, ion_size);
#endif

	return 0;
}

int image_show_rgb565(int ion_fd, int ion_size, int width, int height)
{
	int ret;
	void *userspace_addr;

	if (ion_fd < 0 || width <= 0 || height <= 0) {
		printf("%s: Invalid argument!\n", __FUNCTION__);
		return -1;
	}

	ret = userspace_mmap(ion_fd, ion_size, &userspace_addr);
	if (ret != 0) {
		printf("%s: map to userspace failed!\n", __FUNCTION__);
		return -1;
	}

	cv::Mat rgba_mat(height, width, CV_8UC2, userspace_addr, width * 2);

	cv::imshow("RGB565", rgba_mat);
#if 0
	cv::waitKey();

	userspace_munmap(userspace_addr, ion_size);
#endif

	return 0;
}

//not support 480x320
int load_image_from_file(const char *file_name, void *userspace_addr)
{
	FILE *fp;
	struct stat statbuf;
	int ret, file_size;

	if (file_name == NULL || userspace_addr) {
		printf("%s: Invalid arguments!\n", __FUNCTION__);
		return -1;
	}

	fp = fopen(file_name, "r");
	if (!fp) {
		printf("%s: open failed!\n", file_name);
		return -1;
	}

	stat(file_name, &statbuf);

	file_size = statbuf.st_size;
	if (file_size <= 0) {
		fclose(fp);
		return -1;
	}

	ret = fread(userspace_addr, 1, file_size, fp);
	if (ret != file_size) {
		printf("%s, read error!\n", __FUNCTION__);
		fclose(fp);
		return -1;
	}
	
	fclose(fp);

	return 0;
}

int load_image_to_ion(const char *file_name, int ion_fd, int ion_size)
{
	FILE *fp;
	struct stat statbuf;
	int ret, file_size;
	void *userspace_addr;

	if (file_name == NULL || ion_fd <= 0 || ion_size <= 0) {
		printf("%s: Invalid arguments!\n", __FUNCTION__);
		return -1;
	}

	fp = fopen(file_name, "r");
	if (!fp) {
		printf("%s: open failed!\n", file_name);
		return -1;
	}

	stat(file_name, &statbuf);

	file_size = statbuf.st_size;
	if (file_size <= 0) {
		fclose(fp);
		return -1;
	}

	ret = userspace_mmap(ion_fd, ion_size, &userspace_addr);
	if (ret != 0) {
		printf("%s: map to userspace failed!\n", __FUNCTION__);
		fclose(fp);
		return -1;
	}

	ret = fread(userspace_addr, 1, file_size, fp);
	if (ret != file_size) {
		printf("%s, read error!\n", __FUNCTION__);
		userspace_munmap(userspace_addr, ion_size);
		fclose(fp);
		return -1;
	}

	userspace_munmap(userspace_addr, ion_size);
	fclose(fp);

	return 0;
}

int load_buffer_to_ion(const char *buffer, int buffer_size, int ion_fd, int ion_size)
{
	int ret;
	void *userspace_addr;

	if (buffer == NULL || ion_fd <= 0 || ion_size <= 0 || buffer_size <= 0) {
		printf("%s: Invalid arguments!\n", __FUNCTION__);
		return -1;
	}

	ret = userspace_mmap(ion_fd, ion_size, &userspace_addr);
	if (ret != 0) {
		printf("%s: map to userspace failed!\n", __FUNCTION__);
		return -1;
	}

	memcpy(userspace_addr, buffer, buffer_size);

	userspace_munmap(userspace_addr, ion_size);

	return 0;
}

//ubwc 1080p -resize-> ubwc 240p -convert-> rgba 240p
int ubwc_1080p_gpu_handle_opt1(int ubwc_1080p_fd, int ubwc_240p_fd, int rgba_240p_fd)
{
	yuv_1080p_resize(ubwc_1080p_fd, PIXEL_FORMAT_YUV_UBWC, ubwc_240p_fd);

	yuv2rgb_240p(ubwc_240p_fd, PIXEL_FORMAT_YUV_UBWC, rgba_240p_fd, PIXEL_FORMAT_RGBA_8888);

	return 0;
}

//ubwc 1080p -convert-> rgba 1080p -resize-> rgba 240p
int ubwc_1080p_gpu_handle_opt2(int ubwc_1080p_fd, int rgba_1080p_fd, int rgba_240p_fd)
{
	yuv2rgb_1080p(ubwc_1080p_fd, PIXEL_FORMAT_YUV_UBWC, rgba_1080p_fd, PIXEL_FORMAT_RGBA_8888);

	rgb_1080p_resize(rgba_1080p_fd, PIXEL_FORMAT_RGBA_8888, rgba_240p_fd);

	return 0;
}

int ubwc_720p_gpu_handle_opt1(int ubwc_720p_fd, int ubwc_240p_fd, int rgba_240p_fd)
{
	yuv_720p_resize(ubwc_720p_fd, PIXEL_FORMAT_YUV_UBWC, ubwc_240p_fd);

	yuv2rgb_240p(ubwc_240p_fd, PIXEL_FORMAT_YUV_UBWC, rgba_240p_fd, PIXEL_FORMAT_RGBA_8888);

	return 0;
}

int ubwc_720p_gpu_handle_opt2(int ubwc_720p_fd, int rgba_720p_fd, int rgba_240p_fd)
{
	yuv2rgb_720p(ubwc_720p_fd, PIXEL_FORMAT_YUV_UBWC, rgba_720p_fd, PIXEL_FORMAT_RGBA_8888);

	rgb_720p_resize(rgba_720p_fd, PIXEL_FORMAT_RGBA_8888, rgba_240p_fd);

	return 0;
}

int ubwc_480p_gpu_handle_opt1(int ubwc_480p_fd, int ubwc_240p_fd, int rgba_240p_fd)
{
	yuv_480p_resize(ubwc_480p_fd, PIXEL_FORMAT_YUV_UBWC, ubwc_240p_fd);

	yuv2rgb_240p(ubwc_240p_fd, PIXEL_FORMAT_YUV_UBWC, rgba_240p_fd, PIXEL_FORMAT_RGBA_8888);

	return 0;
}

int ubwc_480p_gpu_handle_opt2(int ubwc_480p_fd, int rgba_480p_fd, int rgba_240p_fd)
{
	yuv2rgb_480p(ubwc_480p_fd, PIXEL_FORMAT_YUV_UBWC, rgba_480p_fd, PIXEL_FORMAT_RGBA_8888);

	rgb_480p_resize(rgba_480p_fd, PIXEL_FORMAT_RGBA_8888, rgba_240p_fd);

	return 0;
}

//nv12 1080p -resize-> nv12 240p -convert-> rgba 240p
int nv12_1080p_gpu_handle_opt1(int nv12_1080p_fd, int nv12_240p_fd, int rgba_240p_fd)
{
	yuv_1080p_resize(nv12_1080p_fd, PIXEL_FORMAT_YUV_NV12, nv12_240p_fd);

	yuv2rgb_240p(nv12_240p_fd, PIXEL_FORMAT_YUV_NV12, rgba_240p_fd, PIXEL_FORMAT_RGBA_8888);

	return 0;
}

//ubwc 1080p -convert-> rgba 1080p -resize-> rgba 240p
int nv12_1080p_gpu_handle_opt2(int nv12_1080p_fd, int rgba_1080p_fd, int rgba_240p_fd)
{
	yuv2rgb_1080p(nv12_1080p_fd, PIXEL_FORMAT_YUV_NV12, rgba_1080p_fd, PIXEL_FORMAT_RGBA_8888);

	rgb_1080p_resize(rgba_1080p_fd, PIXEL_FORMAT_RGBA_8888, rgba_240p_fd);

	return 0;
}

int nv12_720p_gpu_handle_opt1(int nv12_720p_fd, int nv12_240p_fd, int rgba_240p_fd)
{
	yuv_720p_resize(nv12_720p_fd, PIXEL_FORMAT_YUV_NV12, nv12_240p_fd);

	yuv2rgb_240p(nv12_240p_fd, PIXEL_FORMAT_YUV_NV12, rgba_240p_fd, PIXEL_FORMAT_RGBA_8888);

	return 0;
}

int nv12_720p_gpu_handle_opt2(int nv12_720p_fd, int rgba_720p_fd, int rgba_240p_fd)
{
	yuv2rgb_720p(nv12_720p_fd, PIXEL_FORMAT_YUV_NV12, rgba_720p_fd, PIXEL_FORMAT_RGBA_8888);

	rgb_720p_resize(rgba_720p_fd, PIXEL_FORMAT_RGBA_8888, rgba_240p_fd);

	return 0;
}

int nv12_480p_gpu_handle_opt1(int nv12_480p_fd, int nv12_240p_fd, int rgba_240p_fd)
{
	yuv_480p_resize(nv12_480p_fd, PIXEL_FORMAT_YUV_NV12, nv12_240p_fd);

	yuv2rgb_240p(nv12_240p_fd, PIXEL_FORMAT_YUV_NV12, rgba_240p_fd, PIXEL_FORMAT_RGBA_8888);

	return 0;
}

int nv12_480p_gpu_handle_opt2(int nv12_480p_fd, int rgba_480p_fd, int rgba_240p_fd)
{
	yuv2rgb_480p(nv12_480p_fd, PIXEL_FORMAT_YUV_NV12, rgba_480p_fd, PIXEL_FORMAT_RGBA_8888);

	rgb_480p_resize(rgba_480p_fd, PIXEL_FORMAT_RGBA_8888, rgba_240p_fd);

	return 0;
}

int rgba_1080p_flip(int src_ion_fd, int dst_ion_fd)
{
	QImgConv::Image src, dst;
	int ret;

	src.fd = src_ion_fd;
	src.format = PIXEL_FORMAT_RGBA_8888;
	src.width = 1920;
	src.height = 1080;

	dst.fd = dst_ion_fd;
	dst.format = PIXEL_FORMAT_RGBA_8888;
	dst.width = 1920;
	dst.height = 1080;

	ret = QImgConv::flip(&src, &dst, QImgConv::FLIP_H_BIT);
	if (ret != 0) {
		printf("%s: flip failed!\n", __FUNCTION__);
		return -1;
	}

	return 0;
}

int rgba_1080p_clip_240p(int ion_1080p_fd, int ion_240p_fd)
{
	QImgConv::Image src, dst;
	QImgConv::Rec rec;
	int ret;

	src.fd = ion_1080p_fd;
	src.format = PIXEL_FORMAT_RGBA_8888;
	src.width = 1920;
	src.height = 1080;

	dst.fd = ion_240p_fd;
	dst.format = PIXEL_FORMAT_RGBA_8888;
	dst.width = 320;
	dst.height = 240;

	rec.x = 0;
	rec.y = 0;
	rec.width = 320;
	rec.height = 240;

	ret = QImgConv::clip(&src, &rec, &dst, 1);
	if (ret != 0) {
		printf("%s: clip failed!\n", __FUNCTION__);
		return -1;
	}

	return 0;
}

int nv12_1080p_test(const char *file_name)
{
	int ion_fd_nv12_1080p, ion_fd_nv12_720p, ion_fd_nv12_480p, ion_fd_nv12_240p;
	int	ion_fd_rgba_1080p, ion_fd_rgba_720p, ion_fd_rgba_480p, ion_fd_rgba_240p;
	int ret;

	alloc_ion_buffer(&ion_fd_nv12_1080p, YUV_NV12_1080P_SIZE);
	alloc_ion_buffer(&ion_fd_nv12_720p, YUV_NV12_720P_SIZE);
	alloc_ion_buffer(&ion_fd_nv12_480p, YUV_NV12_480P_SIZE);
	alloc_ion_buffer(&ion_fd_nv12_240p, YUV_NV12_240P_SIZE);

	alloc_ion_buffer(&ion_fd_rgba_1080p, RGBA8888_1080P_SIZE);
	alloc_ion_buffer(&ion_fd_rgba_720p, RGBA8888_720P_SIZE);
	alloc_ion_buffer(&ion_fd_rgba_480p, RGBA8888_480P_SIZE);
	alloc_ion_buffer(&ion_fd_rgba_240p, RGBA8888_240P_SIZE);

	ret = load_image_to_ion(file_name, ion_fd_nv12_1080p, YUV_NV12_1080P_SIZE);
	if (ret != 0) {
		return -1;
	}

	nv12_1080p_to_rgba8888_240p(ion_fd_nv12_1080p, ion_fd_rgba_240p);

//	image_show_rgba8888(ion_fd_rgba_1080p, RGBA8888_1080P_SIZE, 1920, 1080);
//	image_show_rgba8888(ion_fd_rgba_720p, RGBA8888_720P_SIZE, 1280, 720);
//	image_show_rgba8888(ion_fd_rgba_480p, RGBA8888_480P_SIZE, 640, 480);
	image_show_rgba8888(ion_fd_rgba_240p, RGBA8888_240P_SIZE, 320, 240);

	return 0;
}

int ubwc_1080p_test(const char *file_name)
{
	int ion_fd_ubwc_1080p, ion_fd_ubwc_720p, ion_fd_ubwc_480p, ion_fd_ubwc_240p;
	int ion_fd_rgba_1080p, ion_fd_rgba_720p, ion_fd_rgba_480p, ion_fd_rgba_240p;
	int ion_fd_rgba_1080p_flip;
	int ret;

	alloc_ion_buffer(&ion_fd_ubwc_1080p, YUV_UBWC_1080P_SIZE);
	alloc_ion_buffer(&ion_fd_ubwc_720p, YUV_UBWC_720P_SIZE);
	alloc_ion_buffer(&ion_fd_ubwc_480p, YUV_UBWC_480P_SIZE);
	alloc_ion_buffer(&ion_fd_ubwc_240p, YUV_UBWC_240P_SIZE);

	alloc_ion_buffer(&ion_fd_rgba_1080p, RGBA8888_1080P_SIZE);
	alloc_ion_buffer(&ion_fd_rgba_1080p_flip, RGBA8888_1080P_SIZE);
	alloc_ion_buffer(&ion_fd_rgba_720p, RGBA8888_720P_SIZE);
	alloc_ion_buffer(&ion_fd_rgba_480p, RGBA8888_480P_SIZE);
	alloc_ion_buffer(&ion_fd_rgba_240p, RGBA8888_240P_SIZE);

	ret = load_image_to_ion(file_name, ion_fd_ubwc_1080p, YUV_UBWC_1080P_SIZE);
	if (ret != 0) {
		return -1;
	}

//	ubwc_1080p_gpu_handle_opt1(ion_fd_ubwc_1080p, ion_fd_ubwc_240p, ion_fd_rgba_240p);
//	ubwc_1080p_gpu_handle_opt2(ion_fd_ubwc_1080p, ion_fd_rgba_1080p, ion_fd_rgba_240p);
//	ubwc_1080p_to_rgba8888_240p(ion_fd_ubwc_1080p, ion_fd_rgba_240p);
	yuv2rgb_1080p(ion_fd_ubwc_1080p, PIXEL_FORMAT_YUV_UBWC, ion_fd_rgba_1080p, PIXEL_FORMAT_RGBA_8888);
//	rgba_1080p_flip(ion_fd_rgba_1080p, ion_fd_rgba_1080p_flip);
	image_show_rgba8888(ion_fd_rgba_1080p, RGBA8888_1080P_SIZE, 1920, 1080);
//	image_show_rgba8888(ion_fd_rgba_1080p_flip, RGBA8888_1080P_SIZE, 1920, 1080);
//	rgba_1080p_clip_240p(ion_fd_rgba_1080p, ion_fd_rgba_240p);
//	image_show_rgba8888(ion_fd_rgba_240p, RGBA8888_240P_SIZE, 320, 240);

//	image_show_rgba8888(ion_fd_rgba_1080p, RGBA8888_1080P_SIZE, 1920, 1080);
//	image_show_rgba8888(ion_fd_rgba_720p, RGBA8888_720P_SIZE, 1280, 720);
//	image_show_rgba8888(ion_fd_rgba_480p, RGBA8888_480P_SIZE, 640, 480);
//	image_show_rgba8888(ion_fd_rgba_240p, RGBA8888_240P_SIZE, 320, 240);

	return 0;
}

int main(int argc, char *argv[])
{
	char name[128];

	if (argc < 2) {
		printf("Invalid argument!\n", __FUNCTION__);
		return -1;
	}

	snprintf(name, 128, "%s", argv[1]);

	if (argc >= 3) {
		nv12_1080p_test(argv[1]);
	} else {
		ubwc_1080p_test(argv[1]);
	}

	cv::waitKey();

	return 0;
}
