#!/bin/csh -f
#
#  
#
$1 \
HKLIN   $2 \
HKLOUT   $3 \
XYZIN   $4 \
XYZOUT  $5  << eor
NCYCLES 0
FREE $6
LABIN FP=FP SIGFP=SIGFP FREE=FreeR_flag
END
eor
