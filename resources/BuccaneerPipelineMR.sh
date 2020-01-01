#$ -cwd
#$ -V
#$ -l h_vmem=2G
#$ -l h_rt=48:00:00
cd @5
python @1 -mtzin @2 -seqin  @3 -colin-fo FP,SIGFP -colin-hl model.HLA,model.HLB,model.HLC,model.HLD @6 -buccaneer-anisotropy-correction -buccaneer-fast -buccaneer-keyword verbose 5 -cycles @4 @7 -pdbin-mr @8 -refmac-mlhl false @9   
