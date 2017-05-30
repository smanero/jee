#!/bin/bash

#
# fb2pdf.sh - wrapper script for fb2pdf program
#
# Copyright (C) 2009 Tigran Aivazian <tigran@bibles.org.uk>
# License: GPLv2
#

myname=$(basename $0)
export TMPDIR=${TMPDIR:-/tmp}

function cleanup()
{
    rm -rf $tempfb2 $tempfb2utf8 $texdir > /dev/null 2>&1
    exit
}

if [ $# -ne 2 ] ; then
    echo "Usage: $myname input.fb2 output.pdf"
    exit 1
fi

fb2file=$1
pdffile=$2

if [ ! -r $fb2file ] ; then
    echo "$myname: ERROR: Cannot read the file \"$fb2file\""
    exit 1
fi

trap cleanup SIGINT
tempfb2=$(mktemp $TMPDIR/fb2pdf-tmpfb2.XXXXXX)
tempfb2utf8=$(mktemp $TMPDIR/fb2pdf-tmpfb2utf8.XXXXXX)
texdir=$(mktemp -d $TMPDIR/fb2pdf-texdir.XXXXXX)

if type xmllint > /dev/null 2>&1
then
    xmllint $fb2file | sed -e "s/&amp;/\&/g" > $tempfb2
else
    sed -e "s///g" -e "s/&amp;/\&/g" $fb2file > $tempfb2
fi
encoding=$(head -1 $tempfb2  | sed -ne "s/^<?xml .*encoding=\"\(.*\)\".*/\1/p")
if [ "$encoding" != "UTF-8" ] && [ "$encoding" != "utf-8" ] ; then
    iconv -f "$encoding" -t UTF-8 $tempfb2 | sed -e "s/^\(<?xml .*encoding=\"\)\(.*\)\(\".*\)/\1UTF-8\3/"  > $tempfb2utf8
else
    rm -f $tempfb2utf8
    tempfb2utf8=$tempfb2
fi

fb2pdf $tempfb2utf8 $texdir
cp $texdir/book.pdf $pdffile
cleanup
