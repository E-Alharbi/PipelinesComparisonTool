package Comparison.Runner;
/**
*
* @author Emad Alharbi
* University of York
*/
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.io.FilenameUtils;

public class CleanerForRunner {

	public  void Clean() throws IOException {
		// TODO Auto-generated method stub


	File [] PDBS= new File(RunningParameter.PDBsDir).listFiles();
	File [] Logs= new File(RunningParameter.LogsDir).listFiles();
String Script=CheckWhichPDBIsBuilt(Logs,PDBS)+"\n";

File [] InterLogs= new File(RunningParameter.IntermediateLogs).listFiles();
 Script+=CheckWhichPDBIsBuilt(InterLogs,PDBS)+"\n";
	Script+="rm *.sh.*"+"\n";
	Script+="rm Qsub.sh"+"\n";
	Script+="rm ProcessedFilesNames*"+"\n";
	new Preparer().WriteTxtFile("Cleaner.sh", Script);
	
	}
	
	String CheckWhichPDBIsBuilt(File [] Logs,File [] PDBS) {
		String Script="";
		for(File Log : Logs) {
			String CaseName=Log.getName().replaceAll("."+FilenameUtils.getExtension(Log.getName()),"");	
			Script+="rm J"+CaseName+".sh \n";
			boolean found=false;
			
			
	for(File PDB : PDBS) {
		String PDBID=PDB.getName().replaceAll("."+FilenameUtils.getExtension(PDB.getName()),"");	
		
		
		if(CaseName.equals(PDBID)) {
			found=true;
		}
	}
	if(found==false) {
		
		Script+="rm "+RunningParameter.LogsDir+"/"+Log.getName()+"\n";
		Script+="rm "+RunningParameter.IntermediateLogs+"/"+Log.getName()+"\n";
		Script+="rm "+RunningParameter.IntermediatePDBs+"/"+CaseName+".pdb\n";
	}
		}
		return Script;
	}

}
