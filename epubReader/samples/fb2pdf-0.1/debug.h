#ifndef _DEBUG_H
#define _DEBUG_H

#define DEBUG 0

#if DEBUG
#define DPRINTF(str, args...)   fprintf(stderr, str, ##args)
#else
#define DPRINTF(str, ...)
#endif

#endif
