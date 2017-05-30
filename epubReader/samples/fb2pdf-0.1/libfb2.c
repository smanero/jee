/*
 * libfb2.c FB2 Viewer plugin for Hanlin V3 e-Reader.
 * Copyright (c) 2009 Tigran Aivazian <tigran@bibles.org.uk>
 * License: GPLv2
 * Version: 0.1
 *
 */

#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

#include <ft2build.h>
#include FT_FREETYPE_H 
#include <expat.h>

#include "libfb2.h"
#include "debug.h"
#include "keyvalue.h"

#define LIBFB2_VERSION     "0.1"

#if DEBUG
#include <sys/time.h>
#include <errno.h>
FILE *logfp;
#define PROFILE_START DPRINTF("%s: ", __FUNCTION__); struct timeval tv1, tv2; gettimeofday(&tv1, NULL);
#define PROFILE_STOP  gettimeofday(&tv2, NULL); DPRINTF("%ldms\n", 1000*(tv2.tv_sec - tv1.tv_sec) + (tv2.tv_usec - tv1.tv_usec)/1000);
#else
#define PROFILE_START  /* nothing */
#define PROFILE_STOP  /* nothing */
#endif

#define EPRINTF(str, args...)   { char errbuf[512]; int len = sprintf(errbuf, str, ##args); v3_callbacks->BeginDialog(); v3_callbacks->SetFontAttr(0); v3_callbacks->SetFontSize(18); v3_callbacks->ClearScreen(0xFF); v3_callbacks->TextOut(20, 60, errbuf, len, TF_ASCII); v3_callbacks->Print(); sleep(5); v3_callbacks->EndDialog(); }

#define SCREEN_WIDTH    600
#define SCREEN_HEIGHT   800

static int page_number, numpages, buffer_valid, fd;

/* GREYSCALE 8-bit bitmap */
static unsigned char imagebuf[SCREEN_WIDTH*SCREEN_HEIGHT+1];

/* GREYSCALE 2-bit bitmap */
static unsigned char screenbuf[(SCREEN_WIDTH+3)*SCREEN_HEIGHT/4];

static struct CallbackFunction *v3_callbacks;

void SetCallbackFunction(struct CallbackFunction *cb)
{
    v3_callbacks = cb;
}

int GetPageNum(void) { DPRINTF("%s\n", __FUNCTION__); return numpages; }
void bGetUserData(void **vUserData, int *iUserDataLength) { }
void vSetUserData(void *vUserData, int iUserDataLength) { }
int iGetDocPageWidth(void) { return SCREEN_WIDTH; }
int iGetDocPageHeight(void) { return SCREEN_HEIGHT; }
unsigned short usGetLeftBarFlag(void) { return 4; }
void vEndInit(int iEndStyle) { }
int OnStatusInfoChange(status_info_t *statusInfo, myrect *rectToUpdate) { return 0; }
int IsStandardStatusBarVisible(void) { return 0; }
int bGetRotate(void) { return 0; }
int GetPageIndex(void) { DPRINTF("%s\n", __FUNCTION__); return page_number; }
void vSetRotate(int rot) { }
void vGetTotalPage(int *iTotalPage) { DPRINTF("%s\n", __FUNCTION__); *iTotalPage = numpages; }
int Origin(void) { return 1; }
int Bigger(void) { return 0; }
int Smaller(void) { return 0; }
int Rotate(void) { return 1; }
int Fit(void) { return 1; }
void GetPageDimension(int *width, int *height) { *width = SCREEN_WIDTH; *height = SCREEN_HEIGHT; return; }
void SetPageDimension(int width, int height) { return; }
double dGetResizePro(void) { return 1.0; }
void vSetResizePro(double dSetPro) { }
int Prev(void) { return GotoPage(page_number - 1); }
int Next(void) { return GotoPage(page_number + 1); }

void vSetCurPage(int p)
{
    DPRINTF("%s(%d)\n", __FUNCTION__, p);
    if (p < 0)
         p = 0;
    else if (p >= numpages)
         p = numpages - 1;
    page_number = p;
}

int GotoPage(int n)
{
    DPRINTF("%s(%d)\n", __FUNCTION__, n);
    if (n < 0)
        n = 0;
    else if (n >= numpages)
        n = numpages - 1;
    return 1;
}

void GetPageData(void **data)
{
    DPRINTF("%s\n", __FUNCTION__);
    if (buffer_valid) {
        *data = screenbuf;
        DPRINTF("\t satisfied from the cache\n");
        return;
    }
    *data = screenbuf;
}

static void XMLCALL data_handler(void *userdata, const XML_Char *s, int len)
{
//    DPRINTF("%.*s", len, s);
}

static void XMLCALL start_element(void *userdata, const char *name, const char **attr)
{
  int i;
  int *depth = userdata;
  printf("%s: ", name);
  for (i = 0; attr[i]; i += 2)
    printf(" %s='%s'\n", attr[i], attr[i + 1]);
  *depth++;
}

static void XMLCALL end_element(void *userdata, const char *name)
{
  int *depth = userdata;
  *depth -= 1;
  printf("END\n");
}


int InitDoc(char *filename)
{
    static char buf[1024];
    ssize_t len;
    int done, depth = 0;

PROFILE_START
    XML_Parser parser = XML_ParserCreate(NULL);
    XML_SetUserData(parser, &depth);
    XML_SetElementHandler(parser, start_element, end_element);
    XML_SetCharacterDataHandler(parser, data_handler);
    DPRINTF("%s(%s)\n", __FUNCTION__, filename);
    fd = open(filename, O_RDONLY);
    if (fd == -1) {
        DPRINTF("%s: open(\"%s\", O_RDONLY) failed: %s\n", __FUNCTION__, filename, strerror(errno));
        EPRINTF("%s: open(\"%s\", O_RDONLY) failed: %s\n", __FUNCTION__, filename, strerror(errno));
    }
    while ((len = read(fd, buf, sizeof(buf))) > 0) {
        done = len < sizeof(buf);
        if (XML_Parse(parser, buf, len, done) == XML_STATUS_ERROR) {
            EPRINTF("%s at line %d\n", XML_ErrorString(XML_GetErrorCode(parser)), XML_GetCurrentLineNumber(parser));
            return 0;
        }
    }
PROFILE_STOP
    return 1;
}

int iInitDocF(char *filename, int pageno, int flag)
{
    DPRINTF("%s(%s,%d,%d)\n", __FUNCTION__, filename, pageno, flag);
    return 0;
}

void vEndDoc(void)
{
    DPRINTF("%s\n", __FUNCTION__);
    (void)close(fd);
}

const char *GetAboutInfoText(void)
{
    static char buf[512];
    sprintf(buf, "FB2 Plugin V%s (C) 2009 Tigran Aivazian\n", LIBFB2_VERSION);
    return buf;
}
