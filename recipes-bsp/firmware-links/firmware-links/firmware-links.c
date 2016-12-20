/*
 * Copyright (c) 2016, The Linux Foundation. All rights reserved.
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

#include <unistd.h>
#include <dirent.h>
#include <sys/types.h>
#include <stdio.h>
#include <getopt.h>
#include <errno.h>
#include <string.h>

#define LOG_TAG  "firmware-links"
#define is_abs_path(str)  ( str[0] && (str[0] == '/'))

/*
 * symlink firmware bin from src to target
 */
int main(int argc, char* argv[])
{
    const char* p_sourcedir;
    const char* p_targetdir;
    struct dirent* p_dirent;
    char srcbuf[1024];
    DIR *p_dir;
    int c, index;

    const struct option long_option[] = {
        {"sourcedir", required_argument, NULL, 's'},
        {"targetdir", required_argument, NULL, 't'},
        {"help", no_argument, NULL, 'h'},
        {NULL, 0, NULL, 0}
    };

    while ((c = getopt_long_only(argc, argv, "s:t:h", long_option, &index)) != -1 )
    {
        switch (c)
        {
        case 's':
            printf("%s: psourcedir is %s\r\n", LOG_TAG, optarg);
            p_sourcedir = optarg;
            break;
        case 't':
            printf("%s: ptargetdir is %s\r\n", LOG_TAG, optarg);
            p_targetdir = optarg;
            break;
        case 'h':
            printf("symlink firmware file\r\n");
            break;
        default:
            break;
        }
    }

    if (!(is_abs_path(p_sourcedir) && is_abs_path(p_targetdir)))
    {
        printf("both sourcedir %s and destdir %s need to be absolute\r\n",p_sourcedir, p_targetdir);
        return -1;
    }

    /* chdir to target directory */
    chdir(p_targetdir);

    p_dir = opendir(p_sourcedir);
    if (!p_dir)
    {
        printf("%s: opendir failed %s \r\n", LOG_TAG, strerror(errno));
        return -1;
    }

    while (p_dirent = readdir(p_dir))
    {
        if (strcmp(p_dirent->d_name, ".") == 0 || strcmp(p_dirent->d_name, "..") == 0)
            continue;

        memset(srcbuf, 0, sizeof(srcbuf));
        snprintf(srcbuf, sizeof(srcbuf),"%s%s%s", p_sourcedir,"/", p_dirent->d_name);
        /* if already exists, continue */
        if (!access(p_dirent->d_name, R_OK))
            continue;

        symlink(srcbuf, p_dirent->d_name);
    }

    closedir(p_dir);

    printf("%s:link finished\r\n", LOG_TAG);
    return 0;
}
