#$ -cwd
#$ -V
#$ -l h_vmem=2G
#$ -l h_rt=48:00:00
#$ -m be
#$ -pe smp 10
LD_LIBRARY_PATH=$LD_LIBRARY_PATH:&ccp4&/lib
export LD_LIBRARY_PATH
export MALLOC_ARENA_MAX=4
vmArgs="-Xmx100m -XX:ParallelGCThreads=1"
java -jar RunComparison.jar RunAnalyser data=&data& castat2Path=&cstat& LogsDir=&Logs& PDBsDir=&PDBs& ToolName=&Tool& IPDBsDir=&IPDBs& ILogsDir=&ILogs& MolProbity=&Mol& UsingMolProbity=&UsingMol& PhasesUsedCPhasesMatch=&CPhasesMatchPhases& Threads=20 \ << eor
END
eor
