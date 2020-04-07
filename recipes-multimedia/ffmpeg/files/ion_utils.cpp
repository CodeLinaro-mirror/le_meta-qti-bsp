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

#include <ion/ion.h>
#include <sys/ioctl.h>
#include <sys/mman.h>
#include <stdio.h>

#ifdef __cplusplus
extern "C" {
#endif

static int ion_fd_get(void)
{
	static int fd = -1;
	
	if (fd == -1) {
		fd = ion_open();
	}

	return fd;
}

int alloc_ion_buffer(int *ion_fd, int size)
{
	int ret;

	if (ion_fd == NULL) {
		printf("%s: Invalid argument!\n", __FUNCTION__);
		return -1;
	}

	ret = ion_alloc_fd(ion_fd_get(), size, 4096, 1 << 25, 1, ion_fd);
	if (ret < 0) {
		printf("%s: error happen ret = %d\n", __FUNCTION__, ret);
		return ret;
	}

	return 0;
}

int userspace_mmap(int ion_fd, int size, void **userspace_addr)
{
	if (userspace_addr == NULL || size <= 0 || ion_fd < 0) {
		printf("%s: Invalid argument!\n", __FUNCTION__);
		return -1;
	}

	*userspace_addr = mmap(NULL, size, PROT_READ | PROT_WRITE, MAP_SHARED, ion_fd, 0);
	if (*userspace_addr == MAP_FAILED) {
		printf("%s: Map failed!\n", __FUNCTION__);
		return -1;
	}
	
	return 0;
}

int userspace_munmap(void *userspace_addr, int size)
{
	if (userspace_addr == NULL || size <= 0) {
		printf("%s: Invalid argument!\n", __FUNCTION__);
		return -1;
	}

	munmap(userspace_addr, size);

	return 0;
}

#ifdef __cplusplus
}
#endif
