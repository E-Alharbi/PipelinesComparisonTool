package Run;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.io.FilenameUtils;

public class CleanerForRunner {

	public  void Clean() throws IOException {
		// TODO Auto-generated method stub


	File [] PDBS= new File(RunningPram.PDBsDir).listFiles();
	File [] Logs= new File(RunningPram.LogsDir).listFiles();
String Script="";
	for(File Log : Logs) {
		String CaseName=Log.getName().replaceAll("."+FilenameUtils.getExtension(Log.getName()),"");	
		Script+="rm J"+CaseName+".sh \n";
		boolean found=false;
		String PDBName="";
for(File PDB : PDBS) {
	String PDBID=PDB.getName().replaceAll("."+FilenameUtils.getExtension(PDB.getName()),"");	
	PDBName=PDB.getName();
	if(CaseName.equals(PDBID)) {
		found=true;
	}
}
if(found==false) {
	Script+="rm "+RunningPram.LogsDir+"/"+Log.getName()+"\n";
	Script+="rm "+RunningPram.IntermediateLogs+"/"+Log.getName()+"\n";
	Script+="rm "+RunningPram.IntermediatePDBs+"/"+PDBName+"\n";
}
	}
	Script+="rm *.sh.*"+"\n";
	Script+="rm Qsub.sh"+"\n";
	Script+="rm ProcessedFilesNames*"+"\n";
	new Preparer().WriteTxtFile("Cleaner.sh", Script);
	
	}

}
