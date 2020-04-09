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

#ifndef COMMON_INTERFACES_H
#define COMMON_INTERFACES_H

#define PIXEL_FORMAT_RGBA_8888            1
#define PIXEL_FORMAT_RGB_888              3
#define PIXEL_FORMAT_RGB_565              4
#define PIXEL_FORMAT_YUV_NV12          0x7FA30C04
#define PIXEL_FORMAT_YUV_UBWC          0x7FA30C06

//width require 64 bytes align
#define RGBA8888_1080P_SIZE (1920 * 1080 * 4)
#define RGBA8888_720P_SIZE  (1280 * 720 * 4)
#define RGBA8888_480P_SIZE  (640 * 480 * 4)
#define RGBA8888_240P_SIZE  (320 * 240 * 4)

#define RGB888_1080P_SIZE (1920 * 1080 * 3)
#define RGB888_720P_SIZE  (1280 * 720 * 3)
#define RGB888_480P_SIZE  (640 * 480 * 3)
#define RGB888_240P_SIZE  (320 * 240 * 3)

#define RGB565_1080P_SIZE (1920 * 1080 * 2)
#define RGB565_720P_SIZE  (1280 * 720 * 2)
#define RGB565_480P_SIZE  (640 * 480 * 2)
#define RGB565_240P_SIZE  (320 * 240 * 2)

// the following size of YUV/UBWC are larger than really needed 
#define YUV_NV12_1080P_SIZE (4 * 1024 * 1024) //4147200
#define YUV_UBWC_1080P_SIZE (4 * 1024 * 1024) //3153920

#define YUV_NV12_720P_SIZE (2 * 1024 * 1024) //1843200
#define YUV_UBWC_720P_SIZE (2 * 1024 * 1024) //1445888

#define YUV_NV12_480P_SIZE (1 * 1024 * 1024) //614400
#define YUV_UBWC_480P_SIZE (1 * 1024 * 1024) //479232

#define YUV_NV12_240P_SIZE (256 * 1024)
#define YUV_UBWC_240P_SIZE (256 * 1024)


#define MAX_CHANNELS       (24)

#ifdef __cplusplus
extern "C" {
#endif

extern int alloc_ion_buffer(int *ion_fd, int size);
extern int userspace_mmap(int ion_fd, int size, void **userspace_addr);
extern int userspace_munmap(void *userspace_addr, int size);

#ifdef __cplusplus
}
#endif
#endif
