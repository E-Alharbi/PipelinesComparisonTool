package Run;
import java.io.File;
/**
*
* @author Emad Alharbi
* University of York
*/
import java.io.IOException;

import Analyser.ResultsAnalyserMultiThreads;

public class CreateCleanerScripts {

	/*This code is needed when rerun failed cases to clean the temp folders to avoid using some files from previous run 
     *  To speed up the run of the cleaner, this code spilt the commands into mini scripts. Each mini script is submitted as 
     *  a job in a cluster server.       
     */
			
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		String Path="/Volumes/PhDHardDrive/VikingSync/CleanerNoR.sh";
		String Commands=new ResultsAnalyserMultiThreads().readFileAsString(Path);
		
		new RunComparison().CheckDirAndFile(new File(Path).getParent()+"/"+"CleanerScript");
	
	String [] ScriptCommands=Commands.split("\n");
	System.out.println(ScriptCommands.length);
	String Qsub="";
	for(int i=0 ; i< ScriptCommands.length ; ++i) {
		//String Script="#$ -cwd\n" + 
			//	"#$ -V\n" + 
			//	"#$ -l h_vmem=1G\n" + 
			//	"#$ -l h_rt=24:00:00\n" + 
			//	"#$ -M emra500@york.ac.uk\n" + 
			//	"#$ -m be";
		String Script="#!/bin/bash\n" + 
				"#SBATCH --time=48:00:00                # Time limit hrs:min:sec\n" + 
				"#SBATCH --mem=10000                     # Total memory limit\n" + 
				"#SBATCH --mail-type=END,FAIL         # Mail events (NONE, BEGIN, END, FAIL, ALL)\n" + 
				"#SBATCH --mail-user=emra500@york.ac.uk   # Where to send mail\n" + 
				"#SBATCH --ntasks-per-node=1            # How many tasks on each node\n" + 
				"#SBATCH --account=CS-MPMSEDM-2018";
		
		new Preparer().WriteTxtFile(new File(Path).getParent()+"/"+"CleanerScript"+"/Cleaner"+String.valueOf(i)+".sh", Script+"\n"+ScriptCommands[i]);
		Qsub+="sbatch Cleaner"+String.valueOf(i)+".sh \n";
	System.out.println("qsub Cleaner"+String.valueOf(i)+".sh");
	}new Preparer().WriteTxtFile(new File(Path).getParent()+"/"+"CleanerScript"+"/Submit.sh", Qsub);
	
	}

}
