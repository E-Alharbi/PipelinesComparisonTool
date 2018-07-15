#!/usr/bin/env bash

CODE=$1

CRANK=$2
SEQ=$3
ccp4-python $CRANK \
--hklin  $4 \
--xyzout $5 \
--hklout $6 \
--disable-rvapi \
--dirout $1 \
--xmlout $1xml \
<< EOF
exclude typ=freeR free=FreeR_flag
fsigf plus f=fakeanom.F_sigF_ano.F+ sigf=fakeanom.F_sigF_ano.sigF+
fsigf minus f=fakeanom.F_sigF_ano.F- sigf=fakeanom.F_sigF_ano.sigF-
mapcoef hla=HLA hlb=HLB hlc=HLC hld=HLD
sequence "file=$SEQ"
model substr atomtype=Se "file=fakeheavy.pdb"
comb_phdmmb target::SAD  dmfull dm parrot
ref target::mlhl refmac
EOF

