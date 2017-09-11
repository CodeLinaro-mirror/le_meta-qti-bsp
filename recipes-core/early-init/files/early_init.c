/*
 * Copyright (c) 2017, The Linux Foundation. All rights reserved.
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
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/mount.h>
#include <fcntl.h>
#include <errno.h>

#define DEFAULT_CONF   "/etc/early_init.conf"
#define END_TAG        "<end>"
#define LINE_MAX       2048
#define WHITESPACE     " \t\n\r"
#define KPI_VALUE_PATH "/debug/bootkpi/kpi_values"
#define GPIO_EXPORT    "/sys/class/gpio/export"

static struct {
	char* appname;
	char* cmd;
	char* applog;
	char* pidfile;
	int   env_used;
	char* env[32];
	int   argv_used;
	char* argv[32];
	char* gpio;
	int   usleep;
	char* wait;
} app_launcher;

static void inline safe_free(char** p)
{
	if (*p)
		free(*p);
	*p = NULL;
	return;
}

static void inline safe_close(int fd)
{
	if (fd > 0)
		close(fd);
	return;
}

static void inline write_marker(const char* name)
{
	int fd = -1;

	fd = open(KPI_VALUE_PATH, O_WRONLY);
	if (fd > 0) {
		write(fd, name, strlen(name));
	} else {
		printf("open bootkpi for name %s failed %s\r\n", name, strerror(errno));
	}
	safe_close(fd);

	return;
}


/*
 * Only support abs path
 */
static inline void mkdirs(char* p, mode_t mode)
{
	char str[1024] = {0};
	struct stat st = {0};
	int i = 0, len = 0;

	len = strlen(p);
	if (len > 1024)
		printf("input string is too long\r\n");

	strncpy(str, p, len);

	if (str[0] != '/')
		return;

	if (str[len - 1] == '/') {
		len--;
		str[len] = '\0';
	}

	for (i = 1; i < len; i++) {
		if (str[i] == '/') {
			str[i] = '\0';
			if (stat(str, &st) == -1) {
				mkdir(str, 0755);
			}
			str[i] = '/';
		}
	}

	if (stat(str, &st) == -1) {
		mkdir(str, mode);
	}

	return;
}

static inline void prepare_dir(char* p)
{
	struct stat st = {0};
	int ret = 0;

	switch (*p) {
		case 'd':
			if (0 == strncmp(p + 1, "ebugfs", strlen("ebugfs"))) {
				/*
				 * Mount debugfs
				 */
				if (stat("/debug", &st) == -1) {
					mkdir("/debug", 0755);
				}

				ret = mount("debugfs", "/debug", "debugfs", 0, NULL);
				if (ret < 0) {
					perror("mount debugfs failed");
				}
			}
			break;
		case 'x':
			if (0 == strncmp(p + 1, "dg_runtime_dir", strlen("dg_runtime_dir"))) {
				/*
				 * Prepare dir for /early/user/0
				 */
				if (stat("/early", &st) == -1) {
					mkdir("/early", 0700);
				}

				ret = mount("tmpfs", "/early", "tmpfs", 0, NULL);
				if (ret < 0) {
					perror("mount tmpfs failed");
				}

				mkdirs("/early/user/0", 0700);
			}
			break;
		case 's':
			if (0 == strncmp(p + 1, "hm", strlen("hm"))) {
				mkdirs("/dev/shm", 0777);
				ret = mount("tmpfs", "/dev/shm", "tmpfs", 0, NULL);
				if (ret < 0) {
					perror("mount tmpfs failed");
				}
			} else if (0 == strncmp(p + 1, "ysfs", strlen("ysfs"))) {
				/*
				 * Mount sysfs
				 */
				if (stat("/sys", &st) == -1) {
					mkdir("/sys", 0755);
				}

				ret = mount("sysfs", "/sys", "sysfs", 0, NULL);
				if (ret < 0) {
					perror("mount sysfs failed");
				}
			} else {
				printf("warning unknown input string %s for prepare_dir", p);
			}
			break;
		default:
			printf("warning unknown input string %s for prepare_dir", p);
	}

out:
	return;
}

/*
 * Remove trailing spaces
 */
static inline char *strstrip(char *s) {
	char* end = s + strlen(s) - 1;

	while (end > s) {
		if (*end == ' ' || *end == '\t' || *end == '\n' || *end == '\r') {
			end--;
		} else {
			break;
		}

	}

	*(end+1) = 0;

	return s;
}

/*
 * Suceess, return 0, else return -1
 */
static int inline find_rvalue(char** p) {
	char *t;
	int ret = -1;
	int len;

	t = strchr(*p, '=');
	if (t)
		*p = t;
	else
		goto out;

	(*p)++;
	len = strlen(*p);

	while (**p == ' ' || **p == '\t')
	{
		(*p)++;
		len--;
		if (len == 0)
			break;
		printf("please remove redundant space.\r\n");
	}
	if (len > 0)
		ret = 0;

out:
	return ret;
}

static void inline app_launcher_start_over(void)
{
	int i = 0;

	safe_free(&app_launcher.appname);
	safe_free(&app_launcher.cmd);
	safe_free(&app_launcher.applog);
	safe_free(&app_launcher.gpio);
	safe_free(&app_launcher.pidfile);
	safe_free(&app_launcher.wait);
	app_launcher.usleep = -1;

	for (i = 0; i < app_launcher.argv_used; i++)
		safe_free(&app_launcher.argv[i]);

	for (i = 0; i < app_launcher.env_used; i++)
		safe_free(&app_launcher.env[i]);

	app_launcher.argv_used = 0;
	app_launcher.env_used = 0;

	return;
}

/*
 * Remove redundant whitespace
 */
static inline int parse_line(char* p)
{
	int i = 0;
	char* t;
	pid_t pid;
	int fd;
	char pid_file[10] = {0};

	/*
	 * Skip whitespace and comment line
	 */
	for (i = 0; i < strlen(p); i++) {

		if (p[i] == ' ' || p[i] == '\t')
			continue;

		if (p[i] == '#')
			goto out;
		else
			break;

		p += i;
	}

	switch (*p) {

		case '[':
			t = strchr(p, ']');
			if (t) {
				app_launcher_start_over();
				*t = '\0';
				p++;
				app_launcher.appname= strdup(p);
				printf("appname is %s \r\n", app_launcher.appname);
			}
			break;
		case 'c':/* cmd */
			if (0 == strncmp(p + 1, "md", strlen("md")) && 0 == find_rvalue(&p)) {
				app_launcher.cmd = strdup(p);
				app_launcher.argv[app_launcher.argv_used] = strdup(p);
				printf("argv[%d] is %s ", app_launcher.argv_used, p);
				app_launcher.argv_used++;
			}
			break;
		case 'e':/* env */
			if (0 == strncmp(p + 1, "nv", strlen("nv")) && 0 == find_rvalue(&p) && app_launcher.env_used < 31) {
				app_launcher.env[app_launcher.env_used] = strdup(p);
				printf("env[%d] is %s ", app_launcher.env_used, p);
				app_launcher.env_used++;
			}
			break;
		case 'a':/* argv */
			if (0 == strncmp(p + 1, "rgv", strlen("rgv")) && 0 == find_rvalue(&p) && app_launcher.argv_used < 31) {
				app_launcher.argv[app_launcher.argv_used] = strdup(p);
				printf("argv[%d] is %s ", app_launcher.argv_used, p);
				app_launcher.argv_used++;
			}
			break;
		case 'l':/* applog */
			if (0 == strncmp(p + 1, "og", strlen("og")) && 0 == find_rvalue(&p)) {
				app_launcher.applog = strdup(p);
				printf("applog is %s", app_launcher.applog);
			}
			break;
		case 'g':/* gpio */
			if (0 == strncmp(p + 1, "pio", strlen("pio")) && 0 == find_rvalue(&p)) {
				app_launcher.gpio = strdup(p);
				printf("gpio is %s", app_launcher.gpio);
			}
			break;
		case 'w':/* wait */
			if (0 == strncmp(p + 1, "ait", strlen("ait")) && 0 == find_rvalue(&p)) {
				app_launcher.wait = strdup(p);
				printf("wait is %s", app_launcher.wait);
			}
			break;
		case 'p':/* pidfile */
			if (0 == strncmp(p + 1, "idfile", strlen("idfile")) && 0 == find_rvalue(&p)) {
				app_launcher.pidfile = strdup(p);
				printf("pidfile is %s", app_launcher.pidfile);
			}
			break;
		case 'm':/* msleep */
			if (0 == strncmp(p + 1, "sleep", strlen("sleep")) && 0 == find_rvalue(&p)) {
				app_launcher.usleep = atoi(p) * 1000;
				printf("usleep is %d", app_launcher.usleep);
			}
			break;
		case '<':/* end */
			/*
			 * When comes to the end, start up the app
			 */
			if (strncmp(p, END_TAG, strlen(END_TAG)))
				goto out;

			pid = fork();
			if (pid < 0) {
				perror("fork child process failed \r\n");
				goto out;
			}

			if (0 == pid) {
				/*
				 * Handle log redirect
				 */
				if (app_launcher.applog) {
					fd = open(app_launcher.applog, O_RDWR | O_CREAT);
					if (fd > 0) {
						dup2(fd, fileno(stdout));
						dup2(fd, fileno(stderr));
						safe_close(fd);
						safe_close(fd);
					}
				}

				if (app_launcher.gpio) {
					fd = open(GPIO_EXPORT, O_WRONLY);
					if (fd < 0)
						perror("open gpio export node failed \r\n");
					else {
						if (-1 == write(fd, app_launcher.gpio,strlen(app_launcher.gpio)))
							printf("config gpio to %s failed: %s", app_launcher.gpio, strerror(errno));
					}
					safe_close(fd);
				}

				if (app_launcher.usleep > 0)
					usleep(app_launcher.usleep);

				if (app_launcher.pidfile) {
					fd = open(app_launcher.pidfile, O_WRONLY | O_CREAT);
					if (fd < 0)
						perror("open pid file failed \r\n");
					else {
						snprintf(pid_file, sizeof(pid_file) , "%d" ,getpid());
						if (-1 == write(fd, pid_file, sizeof(pid_file)))
							printf("write pidfile i%s failed: %s", app_launcher.pidfile, strerror(errno));
					}
					safe_close(fd);
				}

				/*
				 * Wait for early_driver
				 */
				if (app_launcher.wait) {
					printf("app %s waiting for %s ...\r\n", app_launcher.appname, app_launcher.wait);
					for (i = 0; i < 50; i++) {
						if (-1 != access(app_launcher.wait, F_OK))
							break;
						usleep(15000);
					}
				}
				/*
				 * Before weston startup, trigger firmware loading
				 */
				/* if (0 == strncmp(app_launcher.appname, "weston", strlen("weston"))) { */
				/* 	write_marker("early init open card0"); */
				/* 	fd = open("/dev/dri/card0", O_CLOEXEC); */
				/* 	if (fd > 0) { */
				/* 		write_marker("early init open card0 finished"); */
				/* 	} else { */
				/* 		perror("open card0 failed"); */
				/* 	} */
				/* 	safe_close(fd); */
				/* } */

				app_launcher.argv[app_launcher.argv_used] = NULL;
				app_launcher.env[app_launcher.env_used] = NULL;

				if (app_launcher.cmd) {
					execvpe(app_launcher.cmd, app_launcher.argv, app_launcher.env);
				}
				exit(0);
			}

			printf("fire up %s \r\n", app_launcher.appname);
			break;
		default:
			printf("unknown config line %s\r\n", p);
	}

out:
	return 0;
}
/*
 * Check if line is empty or not
 */
static inline bool is_empty_line(const char* p)
{
	return (strspn(p, WHITESPACE) == strlen(p));
}

int main(int argc, char* argv[])
{
	FILE* f;
	char line[LINE_MAX];
	int fd;

	prepare_dir("debugfs");
	prepare_dir("xdg_runtime_dir");
	prepare_dir("shm");
	prepare_dir("sysfs");

	fd = open("/early/early_init.log", O_RDWR | O_CREAT);
	if (fd < 0)
		perror("open log file failed");

	dup2(fd, fileno(stdout));
	dup2(fd, fileno(stderr));
	safe_close(fd);
	safe_close(fd);

	f = fopen(DEFAULT_CONF, "re");
	if (f < 0) {
		perror("open early_init.conf failed.\r\n");
		return -1;
	}

	write_marker("early-init-start-up");

	while (1) {

		if (!fgets(line, sizeof(line), f)) {
			if (feof(f))
				goto out;
			else {
				perror("read conf file meet error");
				goto out;
			}
		}
		if (is_empty_line(line))
			continue;

		strstrip(line);
		parse_line(line);
		memset(line, 0, sizeof(line));
		/* write_marker("early-init-line...."); */
	}
out:
	fclose(f);
	write_marker("early-init-exit");
	return 0;
}
