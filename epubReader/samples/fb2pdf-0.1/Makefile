CC      = gcc
CFLAGS  = -Wall -D_GNU_SOURCE
LDFLAGS = -lexpat

all:  fb2pdf

fb2pdf.o: fb2pdf.c debug.h
		$(CC) $< $(CFLAGS) -c -o $@

fb2pdf:	fb2pdf.o 
		$(CC) $^ $(LDFLAGS) -o $@ 

clean:
		rm -f *.o fb2pdf
