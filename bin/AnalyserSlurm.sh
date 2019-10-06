#!/bin/bash
#SBATCH --time=48:00:00                # Time limit hrs:min:sec
#SBATCH --mem=10000                     # Total memory limit
&ccp4moduleload&
&phenixmoduleload&
LD_LIBRARY_PATH=$LD_LIBRARY_PATH:&ccp4&/lib
export LD_LIBRARY_PATH
export MALLOC_ARENA_MAX=4
vmArgs="-Xmx100m -XX:ParallelGCThreads=1"
java -jar RunComparison.jar RunAnalyser data=&data& castat2Path=&cstat& LogsDir=&Logs& PDBsDir=&PDBs& ToolName=&Tool& IPDBsDir=&IPDBs& ILogsDir=&ILogs& MolProbity=&Mol& UsingMolProbity=&UsingMol& PhasesUsedCPhasesMatch=&CPhasesMatchPhases& Threads=20 \ << eor
END
eor
