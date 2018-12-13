#!/bin/bash
#SBATCH --job-name=Emadjob          # Job name
#SBATCH --time=48:00:00                # Time limit hrs:min:sec
#SBATCH --mem=2000                     # Total memory limit
#SBATCH -o logs/log_emad_o     # Standard output and error log
#SBATCH -e logs/log_emad_e     # Standard output and error log
##SBATCH --mail-type=END,FAIL         # Mail events (NONE, BEGIN, END, FAIL, ALL)
##SBATCH --mail-user=emra500@york.ac.uk   # Where to send mail	
##SBATCH --ntasks-per-node=8            # How many tasks on each node
#SBATCH --account=CS-MPMSEDM-2018
LD_LIBRARY_PATH=$LD_LIBRARY_PATH:&ccp4&/lib
export LD_LIBRARY_PATH
export MALLOC_ARENA_MAX=4
vmArgs="-Xmx100m -XX:ParallelGCThreads=1"
java -jar RunComparison.jar RunAnalyser data=&data& castat2Path=&cstat& LogsDir=&Logs& PDBsDir=&PDBs& ToolName=&Tool& IPDBsDir=&IPDBs& ILogsDir=&ILogs& MolProbity=&Mol& UsingMolProbity=&UsingMol& PhasesUsedCPhasesMatch=&CPhasesMatchPhases& Threads=20 \ << eor
END
eor
