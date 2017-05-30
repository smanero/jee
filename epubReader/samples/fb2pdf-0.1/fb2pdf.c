/*
 * fb2pdf - Convert fb2 to PDF.
 * Copyright (C) 2009 Tigran Aivazian <tigran@bibles.org.uk>
 * License: GPLv2
 * Assumption: input.fb2 is UTF-8 encoded (taken care of fb2pdf.sh wrapper).
 */

#include <stdio.h>
#include <string.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <errno.h>
#include <expat.h>

#include "debug.h"

#define FB2PDFVERSION    "0.1"

char *myname; // program name for err msgs

enum {UNKNOWN, HISTORY, TITLE, MAINBODY, FOOTNOTESBODY, FOOTNOTETITLE} state;
char *outdir;
char footnotefile[1024];
FILE *texfp;
int print_data, print_footnote;

static void XMLCALL data_handler(void *userdata, const XML_Char *s, int len)
{
    if (print_data && (state != FOOTNOTETITLE)) fprintf(texfp, "%.*s", len, s);

    if (print_footnote) {
        FILE *fp;
        if ((fp = fopen(footnotefile, "a"))) {
            fprintf(fp, "%.*s", len, s);
            DPRINTF("%s: printing footnote \"%.*s\" -> file \"%s\"\n", __FUNCTION__, len, s, footnotefile);
            (void)fclose(fp);
        }
    }
}

static void inline body_start(const char **attr)
{
    int i;

    DPRINTF("%s()\n", __FUNCTION__);
    state = MAINBODY;
    for (i = 0; attr[i]; i += 2) {
        if (!strcmp(attr[i], "name") && !strcmp(attr[i+1], "notes")) {
            DPRINTF("%s: entering FOOTNOTES BODY\n", __FUNCTION__);
            state = FOOTNOTESBODY;
            break;
        }
    }
}

static void inline body_end(void)
{
    DPRINTF("%s()\n", __FUNCTION__);
    state = UNKNOWN;
    print_data = 0;
}

static void inline section_start(const char **attr)
{
    int i;

    DPRINTF("%s()\n", __FUNCTION__);
    print_data = 1;
    if (state == FOOTNOTESBODY) {
        for (i = 0; attr[i]; i += 2) {
            if (!strcmp(attr[i], "id")) {
               sprintf(footnotefile, "%s/%s.tex", outdir, attr[i+1]);
               DPRINTF("%s: found a footnote \"%s\"\n", __FUNCTION__, attr[i+1]);
               print_data = 0;
               break;
            }
        }
    }
}

static void inline section_end(void)
{
    DPRINTF("%s()\n", __FUNCTION__);
    print_data = 0;
}

static void inline anchor_start(const char **attr)
{
    int i;

    DPRINTF("%s()\n", __FUNCTION__);
    for (i = 0; attr[i]; i += 2) {
        if (!strcmp(attr[i], "type") && !strcmp(attr[i+1], "note")) {
            for (i = 0; attr[i]; i += 2)
                if (!strcmp(attr[i], "l:href"))
                    fprintf(texfp, "\\leavevmode\\unskip\\footnote{\\input %s/%s}", outdir, attr[i+1]+1); // +1 to skip '#'
            print_data = 0;
            break;
        }
    }
}

static void inline anchor_end(void)
{
    DPRINTF("%s()\n", __FUNCTION__);
    print_data = 1;
}

static void inline strong_start(void)
{
    DPRINTF("%s()\n", __FUNCTION__);
    if (print_data) fprintf(texfp, "\\textbf{");
}

static void inline strong_end(void)
{
    DPRINTF("%s()\n", __FUNCTION__);
    if (print_data) fprintf(texfp, "}");
}

static void inline title_start(void)
{
    DPRINTF("%s()\n", __FUNCTION__);
    if (state == FOOTNOTESBODY)
        state = FOOTNOTETITLE;
    else if (state != FOOTNOTESBODY) {
        state = TITLE;
        fprintf(texfp, "\n\n\\chapter{%%");
        print_data = 1;
    }
}

static void inline title_end(void)
{
    DPRINTF("%s()\n", __FUNCTION__);
    print_data = 0;
    if (state == FOOTNOTETITLE)
        state = FOOTNOTESBODY;
    else if (state != FOOTNOTESBODY) {
        state = UNKNOWN;
        fprintf(texfp, "}\n");
    }
}

static void inline par_start(void)
{
    DPRINTF("%s: state=%d\n", __FUNCTION__, state);
    if (state != HISTORY)
        print_data = 1;
    if (state == TITLE) {
        fprintf(texfp, "\n");
    } else if (state == FOOTNOTESBODY) {
        print_footnote = 1;
        print_data = 0;
    } else if (state != FOOTNOTETITLE)
        fprintf(texfp, "\n\\par ");
}

static void inline par_end(void)
{
    DPRINTF("%s()\n", __FUNCTION__);
    print_footnote = print_data = 0;
}

static void inline annotation_start(void)
{
    fprintf(texfp, "\n{\\itshape\\small ");
    print_data = 1;
}

static void inline annotation_end(void)
{
    fprintf(texfp, "}\n");
    print_data = 0;
}

static void inline history_start(void)
{
    state = HISTORY;
}

static void XMLCALL start_element(void *userdata, const char *name, const char **attr)
{
    if (!strcmp(name, "body")) body_start(attr);
    else if (!strcmp(name, "section")) section_start(attr);
    else if (!strcmp(name, "a")) anchor_start(attr);
    else if (!strcmp(name, "strong")) strong_start();
    else if (!strcmp(name, "title")) title_start();
    else if (!strcmp(name, "p")) par_start();
    else if (!strcmp(name, "annotation")) annotation_start();
    else if (!strcmp(name, "history")) history_start();
}

static void XMLCALL end_element(void *userdata, const char *name)
{
    if (!strcmp(name, "section")) section_end();
    else if (!strcmp(name, "strong")) strong_end();
    else if (!strcmp(name, "title")) title_end();
    else if (!strcmp(name, "p")) par_end();
    else if (!strcmp(name, "body")) body_end();
    else if (!strcmp(name, "a")) anchor_end();
    else if (!strcmp(name, "annotation")) annotation_end();
}

static void inline write_tex_preamble(void)
{
    fprintf(texfp,
"\\documentclass[12pt]{book}\n"
"\\tolerance=13000\n"
"\\usepackage[dvips,twoside,hmargin={0.1in},vmargin={0.01in,0.03in},papersize={9cm,12cm}]{geometry}\n"
"\\setlength{\\parskip}{0mm}\n"
"\\usepackage{fontspec,polyglossia}\n"
"\\setdefaultlanguage{russian}\n"
"\\setmainfont[Mapping=tex-text,Script=Cyrillic,Language=Russian]{Octava}\n"
"\\usepackage[hyperfootnotes=false]{hyperref}\n"
"\\renewcommand{\\chapter}[1]{\\newpage\\begin{center}\\scshape #1\\end{center}}\n"
"\\pagestyle{empty}\n"
"\\begin{document}\n");
}

static void inline write_tex_end(void)
{
    fprintf(texfp, "\n\\end{document}\n");
}

int main(int argc, char *argv[])
{
    static char buf[65535];
    ssize_t len;
    int done, fb2fd;
    char *fb2name, texname[512];
    XML_Parser parser;

    myname = strdup(argv[0]);
    if (argc != 3) {
        fprintf(stderr, "Usage: %s input.fb2 outdir\n", myname);
        exit(1);
    }
    fb2name = strdup(argv[1]);
    outdir = strdup(argv[2]);
    sprintf(texname, "%s/book.tex", outdir);

    parser = XML_ParserCreate(NULL);
    XML_SetElementHandler(parser, start_element, end_element);
    XML_SetCharacterDataHandler(parser, data_handler);
    fb2fd = open(fb2name, O_RDONLY);
    if (fb2fd == -1) {
        fprintf(stderr, "%s: open(\"%s\", O_RDONLY) failed: %s\n", myname, fb2name, strerror(errno));
        exit(1);
    }
    texfp = fopen(texname, "w");
    if (!texfp) {
        fprintf(stderr, "%s: fopen(\"%s\", \"w\") failed: %s\n", myname, texname, strerror(errno));
        exit(1);
    }
    write_tex_preamble();
    while ((len = read(fb2fd, buf, sizeof(buf))) > 0) {
        done = len < sizeof(buf);
        if (XML_Parse(parser, buf, len, done) == XML_STATUS_ERROR) {
            fprintf(stderr, "%s at line %d\n", XML_ErrorString(XML_GetErrorCode(parser)), XML_GetCurrentLineNumber(parser));
            exit(1);
        }
    }
    (void)close(fb2fd);
    write_tex_end();
    (void)fclose(texfp);
    if (chdir(outdir) == -1) {
        fprintf(stderr, "%s: chdir(\"%s\") failed: %s\n", myname, outdir, strerror(errno));
        exit(1);
    }
    system("xelatex book.tex");
	return 0;
}
