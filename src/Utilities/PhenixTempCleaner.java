package Utilities;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import NotUsed.ARPResultsAnalysis;
import Run.RunningPram;

public class PhenixTempCleaner {

	public void CleanTemp(String PhenixLogs, String WorkingDir ) throws IOException {
		// TODO Auto-generated method stub
		File[] files = new File(PhenixLogs).listFiles();
	   
			 for (File file : files) {
				 String CaseName=file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),"");

				 System.out.println(CaseName);
				 String Path = new PhenixTempCleaner().WorkingDirPath(CaseName,WorkingDir);
				 if(Path!=null) {
				 FileUtils.deleteDirectory(new File(Path+"/TEMP0"));	
				 System.out.println(Path+"/TEMP0");
				 System.out.println("Deleted");
				 }
				 else {
					 System.out.println(Path+"/TEMP0");
					 System.out.println("Not Deleted");
				 }
			 }
			 
	}

String WorkingDirPath(String CaseName, String WorkingDirList) throws IOException {
	File yourFile = new File(WorkingDirList);
	yourFile.createNewFile();
	 String FileNamesTxt=new ARPResultsAnalysis().readFileAsString(WorkingDirList);
	  String [] CasesAndPaths= FileNamesTxt.split("\n");
	  for(int i=0 ; i <CasesAndPaths.length ; ++i )
		  if(CasesAndPaths[i].equals(CaseName))
		  {
			  return CasesAndPaths[i+1];
		  }
	  return  null;
}
}
