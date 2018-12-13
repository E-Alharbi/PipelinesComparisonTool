#$ -cwd
#$ -V
#$ -l h_vmem=1G
#$ -l h_rt=48:00:00

ccp4-python @Bucc --mtzin @mtzin --seqin @seqin  --colinfo FP,SIGFP --colinhl parrot.ABCD.A,parrot.ABCD.B,parrot.ABCD.C,parrot.ABCD.D --iterations 5 --mtz-name @FileName
