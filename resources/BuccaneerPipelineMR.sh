#$ -cwd
#$ -V
#$ -l h_vmem=2G
#$ -l h_rt=48:00:00
cd {@5}
 {@1} -mtzin {@2} -seqin  {@3} -colin-fo {@10} -colin-hl {@11} {@6} -buccaneer-anisotropy-correction -buccaneer-fast -buccaneer-keyword verbose 5 -cycles {@4} {@7} -pdbin-mr {@8} -refmac-mlhl false {@9} -buccaneer-keyword mr-model-seed   
