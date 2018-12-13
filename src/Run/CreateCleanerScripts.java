package Run;

import java.io.IOException;

import Analyser.ResultsAnalyserMultiThreads;

public class CreateCleanerScripts {

	/*This code is needed when rerun failed cases to clean the temp folders to avoid using some files from previous run 
     *  To speed up the run of the cleaner, this code spilt the commands into mini scripts. Each mini script is submitted as 
     *  a job in a cluster server.       
     */
			
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		
		String Commands=new ResultsAnalyserMultiThreads().readFileAsString("/Users/emadalharbi/Desktop/test/CleanerPhenix.sh");
	
	
	String [] ScriptCommands=Commands.split("\n");
	System.out.println(ScriptCommands.length);
	String Qsub="";
	for(int i=0 ; i< ScriptCommands.length ; ++i) {
		String Script="#$ -cwd\n" + 
				"#$ -V\n" + 
				"#$ -l h_vmem=1G\n" + 
				"#$ -l h_rt=24:00:00\n" + 
				"#$ -M emra500@york.ac.uk\n" + 
				"#$ -m be";
		new RunComparison().CheckDirAndFile("CleanerScripts");
		new Preparer().WriteTxtFile("CleanerScripts/Cleaner"+String.valueOf(i)+".sh", Script+"\n"+ScriptCommands[i]);
		Qsub+="qsub Cleaner"+String.valueOf(i)+".sh \n";
	System.out.println("qsub Cleaner"+String.valueOf(i)+".sh");
	}new Preparer().WriteTxtFile("CleanerScripts/Submit.sh", Qsub);
	
	}

}
