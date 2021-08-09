package Comparison.Runner;
/**
*
* @author Emad Alharbi
* University of York
*/
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import org.apache.commons.io.FilenameUtils;

import Comparison.Analyser.MultiThreadedAnalyser;

public class JobCreater {
String PathToWrite="./"; // default 
public void CreateJobs (String ToolName , String Path) throws IOException{
	PathToWrite=Path+"/";
	CreateJobs(PathToWrite+ToolName,-1);
}
	public void CreateJobs (String ToolName, int JobsNumLimit) throws IOException {
		// TODO Auto-generated method stub
		
		
String DataPath=new MultiThreadedAnalyser().readFileAsString(ToolName);
String Script=DataPath;
String ScriptAnotherCopy=DataPath;
DataPath=DataPath.substring(DataPath.indexOf("data="));
DataPath=DataPath.substring(DataPath.indexOf("data="),DataPath.indexOf(" "));
DataPath=DataPath.replaceAll("data=", "");

File [] files= new File(DataPath).listFiles();

Vector<String> FilesNames= new Vector <String>();
if(RunningParameter.PDBsDir!=null){
	File [] PDBs= new File(RunningParameter.PDBsDir).listFiles();
	for(File PDB : PDBs)
	  FilesNames.addElement(PDB.getName().replaceAll("."+FilenameUtils.getExtension(PDB.getName()),""));
}
String Qsub="#!/bin/bash \n" ; 
		Qsub+="#SBATCH --time=48:00:00                 \n" ; 
Qsub+="#SBATCH --mem=1000                      \n" ; 
		if(RunningParameter.SlurmEmail.length()!=0) {
			Qsub+="#SBATCH --mail-type=ALL          \n" ; 
		
Qsub+="#SBATCH --mail-user="+RunningParameter.SlurmEmail+"   \n" ;  
		}
		if(RunningParameter.SlurmAccount.length()!=0)
		Qsub+="#SBATCH --account="+RunningParameter.SlurmAccount+" \n";
		
int CountJobs=0;		
for (File file : files) {
	String CaseName=file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),"");
	
	if(!FilesNames.contains(CaseName)) {
		CountJobs++;
		Script=Script.replaceAll(DataPath, DataPath+"/"+file.getName());
		
		FilesNames.add(CaseName);
		
		new Preparer().WriteTxtFile(PathToWrite+"J"+CaseName+".sh",Script);
		
		if(RunningParameter.ClusterServerGrid=="Slurm")
			Qsub+="sbatch J"+CaseName+".sh \n";
		else
			Qsub+="qsub J"+CaseName+".sh \n";
		Script=ScriptAnotherCopy;
		
		if(CountJobs==JobsNumLimit)
			break;
	}
}
new Preparer().WriteTxtFile(PathToWrite+"Qsub.sh",Qsub);

	}

}
