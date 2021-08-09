#$ -cwd
#$ -V
#$ -l h_vmem=2G
#$ -l h_rt=48:00:00
cd @5
 @1 -mtzin @2 -seqin  @3 -colin-fo @8 -colin-hl @9 @6 -buccaneer-anisotropy-correction -buccaneer-fast -buccaneer-keyword verbose 5 -cycles @4 @7   
