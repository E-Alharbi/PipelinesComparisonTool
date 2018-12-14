package Run;
/**
*
* @author Emad Alharbi
* University of York
*/
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import org.apache.commons.io.FilenameUtils;

import Analyser.ResultsAnalyserMultiThreads;

public class JobCreater {

	public void CreateJobs (String ToolName) throws IOException {
		// TODO Auto-generated method stub
		
		
String DataPath=new ResultsAnalyserMultiThreads().readFileAsString(ToolName);
String Script=DataPath;
String ScriptAnotherCopy=DataPath;
DataPath=DataPath.substring(DataPath.indexOf("data="));
DataPath=DataPath.substring(DataPath.indexOf("data="),DataPath.indexOf(" "));
DataPath=DataPath.replaceAll("data=", "");
System.out.println(DataPath);
File [] files= new File(DataPath).listFiles();
Vector<String> FilesNames= new Vector <String>();
if(RunningPram.PDBsDir!=null){
	File [] PDBs= new File(RunningPram.PDBsDir).listFiles();
	for(File PDB : PDBs)
	  FilesNames.addElement(PDB.getName().replaceAll("."+FilenameUtils.getExtension(PDB.getName()),""));
}
String Qsub="";
for (File file : files) {
	String CaseName=file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),"");
	if(!FilesNames.contains(CaseName)) {
		Script=Script.replaceAll(DataPath, DataPath+"/"+file.getName());
		//System.out.println(Script);
		FilesNames.add(CaseName);
		new Preparer().WriteTxtFile("J"+CaseName+".sh",Script);
		if(RunningPram.ClusterServerGrid=="Slurm")
			Qsub+="sbatch J"+CaseName+".sh \n";
		else
			Qsub+="qsub J"+CaseName+".sh \n";
		Script=ScriptAnotherCopy;
	}
}
new Preparer().WriteTxtFile("Qsub.sh",Qsub);

	}

}
