package Run;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

import org.apache.commons.io.FilenameUtils;

import NotUsed.ARPResultsAnalysis;

public class test2 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		 Vector<String> FilesNames= new Vector <String>();
			File[] processedfiles = new File("/Volumes/PhDHardDrive/TempResults/Pipelines/Phenix/PhenixLogsHancs").listFiles();
				 for (File file : processedfiles) {
					 FilesNames.add(file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),""));
					 System.out.println(file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),""));
				 }
				 File[] files = new File("/Volumes/PhDHardDrive/jcsg1200/hancsWithMissingCases").listFiles();
				 System.out.println(FilesNames.size());
				 FilesNames=AddFileNameToList(FilesNames);
				 System.out.println(FilesNames.size());
				int count=0;
						 for (File file : files) {
							 FilesNames=AddFileNameToList(FilesNames);
							 String CaseName=file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),"");
					
							 if(!FilesNames.contains(CaseName)){
								 WriteFileNameToList(CaseName);
						System.out.println("File Name "+CaseName);
						count++;
					}
						 }
						 System.out.println(count);
		
	}
	static Vector<String> AddFileNameToList( Vector<String> FilesNames) throws IOException{
		File yourFile = new File("ProcessedFilesNamesPhenix.txt");
		yourFile.createNewFile();
		 String FileNamesTxt=new ARPResultsAnalysis().readFileAsString("ProcessedFilesNamesPhenix.txt");
		 FilesNames.addAll(Arrays.asList(FileNamesTxt.split("\n")));
		 return FilesNames;
		
	}
	static void WriteFileNameToList(String Name) throws IOException{
		File yourFile = new File("ProcessedFilesNamesPhenix.txt");
		yourFile.createNewFile();
		 BufferedWriter output;
		 output = new BufferedWriter(new FileWriter("ProcessedFilesNamesPhenix.txt", true));
		 output.append(Name+"\n");
		 output.close();
	}

}
