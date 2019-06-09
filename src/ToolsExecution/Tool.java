package ToolsExecution;
/**
*
* @author Emad Alharbi
* University of York
*/
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

import org.apache.commons.io.FilenameUtils;

import Comparison.Runner.RunningPram;
import NotUsed.ARPResultsAnalysis;

public  class Tool {
	
	
	 synchronized Vector<String> AddFileNameToList( Vector<String> FilesNames) throws IOException{
		File yourFile = new File(this.getClass().getSimpleName()+".txt");
		yourFile.createNewFile();
		 String FileNamesTxt=new ARPResultsAnalysis().readFileAsString(this.getClass().getSimpleName()+".txt");
		 FilesNames.addAll(Arrays.asList(FileNamesTxt.split("\n")));
		 return FilesNames;
		
	}
	 synchronized void WriteFileNameToList(String Name) throws IOException{
		File yourFile = new File(this.getClass().getSimpleName()+".txt");
		yourFile.createNewFile();
		 BufferedWriter output;
		 output = new BufferedWriter(new FileWriter(this.getClass().getSimpleName()+".txt", true));
		 output.append(Name+"\n");
		 output.close();
	}
	 void WriteWorkingDirToTxtFile(String Name, String WorkingDir) throws IOException{
		File yourFile = new File(this.getClass().getSimpleName()+"WorkingDir.txt");
		yourFile.createNewFile();
		 BufferedWriter output;
		 output = new BufferedWriter(new FileWriter(this.getClass().getSimpleName()+"WorkingDir.txt", true));
		 output.append(Name+"\n");
		 output.append(WorkingDir+"\n");
		 output.close();
	}
	public  synchronized  File  PickACase() throws IOException {
			
		 System.out.println("PickACase() "+this.getClass().getSimpleName());
			Vector<String> FilesNames= new Vector <String>();
			File[] files = new File(RunningPram.DataPath).listFiles();
			
		    FilesNames=AddFileNameToList(FilesNames);
		    System.out.println(FilesNames.size());
				 for (File file : files) {
					 String CaseName=file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),"");
			if(!FilesNames.contains(CaseName)){
				WriteFileNameToList(CaseName);
				System.out.println(Thread.currentThread().getName()+" Picked "+file.getName());
				return file;
			}
				 }
			return null;	 
		}
}
