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
#include <pthread.h>
#include <string.h>
#include <unistd.h>
#include <time.h>
#include <errno.h>
#include <sys/time.h>
#include "common_interfaces.h"

#define __STDC_CONSTANT_MACROS

extern "C" {
#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
#include <libswscale/swscale.h>
#include <libavutil/imgutils.h>
#include <libavutil/log.h>

#include <libavutil/avutil.h>
#include <libavdevice/avdevice.h>
};

extern int ubwc_1080p_show_init(int channels);
extern int ubwc_1080p_show(int channels);
extern int ubwc_1080p_format_convert(int ubwc_1080p_fd, int channel);

struct ffmpeg_decode_info {
	AVCodec *pCodec;
	AVCodecContext *pCodecCtx;
	AVFormatContext *pFormatCtx;
	AVFrame	*pFrame;
	AVPacket *packet;
	struct SwsContext *img_convert_ctx;
	char decode_name[64];
	char file_name[256];
	int channel;
	pthread_t thread;
};

static struct ffmpeg_decode_info decode_info[MAX_CHANNELS];
static int fix_size = 0;
static char input_file_name[256];
static char decode_name[64] = "h264_v4l2m2m";
static pthread_mutex_t ffmpeg_lock;

void usage() {
	printf("-i <file name>\n");
	printf("        : Input file name\n");
	printf("-t <number>\n");
	printf("        : How many thread concurrency\n");
	printf("-c <codec name>\n");
	printf("        : Choice codec by name, default h264_v4l2m2m\n");
	printf("-s \n");
	printf("        : Choice for 1080p align size to 1088\n");
}

static int interrupt_callback(void *p)
{
	return 0;
}

static int ffmpeg_demo_init(struct ffmpeg_decode_info *decode_info)
{
	int i, video_index, ret = 0;
	AVDictionary* options = NULL;

	av_dict_set(&options, "max_delay", "500000", 0);
	av_dict_set(&options, "rtsp_transport", "tcp", 0);

	decode_info->pFormatCtx = avformat_alloc_context();
	decode_info->pFormatCtx->video_codec = avcodec_find_decoder_by_name(decode_info->decode_name);
	decode_info->pFormatCtx->interrupt_callback.callback = interrupt_callback;
	decode_info->pFormatCtx->interrupt_callback.opaque = decode_info;

	ret = avformat_open_input(&decode_info->pFormatCtx, decode_info->file_name, NULL, &options);
	if(ret != 0) {
		printf("Couldn't open input stream.\n");
		goto open_input_err;
	}

	ret = avformat_find_stream_info(decode_info->pFormatCtx, NULL);
	if (ret < 0) {
		av_log(NULL, AV_LOG_FATAL, "Failed with find stream information.\n");
		goto stream_info;
	}

	for (i = 0; i < decode_info->pFormatCtx->nb_streams; i++) { 
		if (decode_info->pFormatCtx->streams[i]->codecpar->codec_type == AVMEDIA_TYPE_VIDEO) {
			video_index = i;
			break;
		}
	}

	if (video_index == -1) {
		av_log(NULL, AV_LOG_FATAL, "Failed to find a video stream. \n");
		ret = -1;
		goto stream_info; 
	}

	decode_info->pCodec = decode_info->pFormatCtx->video_codec;
	if (decode_info->pCodec == NULL){
		av_log(NULL, AV_LOG_FATAL, "Codec not found. \n");
		goto stream_info;
	}

	decode_info->pCodecCtx = avcodec_alloc_context3(decode_info->pCodec);
	if (!decode_info->pCodecCtx) {
		av_log(NULL, AV_LOG_FATAL, "Filed with allocate video codec context %s \n", strerror(errno));
		goto stream_info;
	}

	avcodec_parameters_to_context(decode_info->pCodecCtx, 
			decode_info->pFormatCtx->streams[video_index]->codecpar);

	if (fix_size) {
		decode_info->pCodecCtx->height = 1088;
	}

	decode_info->pCodecCtx->pix_fmt = AV_PIX_FMT_NV12_UBWC;
	ret = avcodec_open2(decode_info->pCodecCtx, decode_info->pCodec, NULL);
	if (ret < 0) {
		av_log(NULL, AV_LOG_FATAL, "Failed with open codec. \n");
		goto open2_err;
	}

	return 0;

open2_err:
	av_free(decode_info->pCodecCtx);
stream_info:
open_input_err:
	avformat_close_input(&decode_info->pFormatCtx);
	return ret;
}

static int ffmpeg_demo_handle(struct ffmpeg_decode_info *decode_info)
{
	int ret, got_picture;
	unsigned long long ion_addr = 0;

	decode_info->pFrame = av_frame_alloc();
	decode_info->packet = (AVPacket *)av_malloc(sizeof(AVPacket));

	decode_info->img_convert_ctx = sws_getContext(decode_info->pCodecCtx->width,
			decode_info->pCodecCtx->height,
			decode_info->pCodecCtx->pix_fmt, decode_info->pCodecCtx->width,
			decode_info->pCodecCtx->height, AV_PIX_FMT_NV12_UBWC,
			SWS_BICUBIC, NULL, NULL, NULL);

	while (1) {
		ret = av_read_frame(decode_info->pFormatCtx, decode_info->packet);
		if (ret == EAGAIN) {
			printf("EAGAIN with channel %d\n", decode_info->channel);
			continue;
		}

		ret = avcodec_send_packet(decode_info->pCodecCtx, decode_info->packet);
		if (ret < 0) {
			av_log(NULL, AV_LOG_ERROR, "Failed with send packet\n");
			break;
		}

		got_picture = avcodec_receive_frame(decode_info->pCodecCtx, decode_info->pFrame);
		if (!got_picture) {
			ion_addr = (unsigned long long)decode_info->pFrame->data[1];
			ion_addr &= 0xffffffff;
			ubwc_1080p_format_convert((int)ion_addr, decode_info->channel);
		}

		av_packet_unref(decode_info->packet);
	}

	return 0;
}

static int ffmpeg_demo_destory(struct ffmpeg_decode_info *decode_info)
{
	sws_freeContext(decode_info->img_convert_ctx);
	av_frame_free(&decode_info->pFrame);
	avcodec_close(decode_info->pCodecCtx);
	av_free(decode_info->pCodecCtx);
	avformat_close_input(&decode_info->pFormatCtx);

	return 0;
}

static void *ffmpeg_thread_loop(void *ptr)
{
	struct ffmpeg_decode_info *decode_info = (struct ffmpeg_decode_info *)ptr;

	av_log(NULL, AV_LOG_FATAL, "Channel %4d decoding\n", decode_info->channel);

	ffmpeg_demo_handle(decode_info);

	return NULL;
}

static int ffmpeg_create_thread(int channels)
{
	int i = 0;
	pthread_attr_t thread_attr;

	pthread_attr_init(&thread_attr);
	pthread_attr_setschedpolicy(&thread_attr, SCHED_RR);
	pthread_mutex_init(&ffmpeg_lock, NULL);
	for (i = 0; i < channels; i++) {
		decode_info[i].channel = i;
		snprintf(decode_info[i].decode_name, 64, "%s", decode_name);
		snprintf(decode_info[i].file_name, 256, "%s", input_file_name);
		pthread_mutex_lock(&ffmpeg_lock);
		ffmpeg_demo_init(&decode_info[i]);
		pthread_mutex_unlock(&ffmpeg_lock);
		pthread_create(&decode_info[i].thread, &thread_attr, ffmpeg_thread_loop, (void *)&decode_info[i]);
	}

	while (1) {
		ubwc_1080p_show(channels);
	}

	for (i = 0; i < channels; i++) {
		pthread_join(decode_info[i].thread, (void **)NULL);
	}

	pthread_mutex_destroy(&ffmpeg_lock);

	return 0;
}

int main(int argc, char* argv[])
{
	int opt = 0;
	int channels = 0;

	av_log_set_level(AV_LOG_FATAL);

	while ((opt = getopt(argc, argv, "t:i:o:c:d:nusmh?l")) != -1) {
		switch (opt) {
		case 'i':
			snprintf(input_file_name, 256, "%s", optarg);
			break;
		case 't':
			channels = atoi(optarg);
			break;
		case 'c':
			snprintf(decode_name, 64, "%s", optarg);
			break;
		case 'l':
			av_log_set_level(AV_LOG_DEBUG);
			break;
		case 's':
			fix_size = 1;
			break;
		default:
			usage();
			return 0;
		}
	}

	av_log(NULL, AV_LOG_FATAL, "Input file: %s\n", input_file_name);
	av_log(NULL, AV_LOG_FATAL, "PID: %d\n", getpid());

	if (channels <= 0) {
		channels = 24;
	}

	avformat_network_init();
	ubwc_1080p_show_init(channels);
	ffmpeg_create_thread(channels);

	return 0;
}
