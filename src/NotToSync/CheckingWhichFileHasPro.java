package NotToSync;
/**
*
* @author Emad Alharbi
* University of York
*/
import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Vector;

import org.apache.commons.io.FilenameUtils;

import NotUsed.ARPResultsAnalysis;
import ToolsExecution.RunBuccaneerMulti;

public class CheckingWhichFileHasPro {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		File[] files = new File("/Volumes/PhDHardDrive/jcsg1200Results/mancs/Phenix/FirstRun/PhinexLogs").listFiles();
		   
		 for (File file : files) {
			 String CaseName=file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),"");
			 System.out.println(CaseName);
		 }
		
		
		/*
		 Vector<String> FilesNames= new Vector <String>();
		 File[] processedfiles = new File("/Users/emadalharbi/Desktop/PhDYork/Scripts/ComparisonScript/PhinexLogs").listFiles();
		 File[] processedfilesPDBs = new File("/Users/emadalharbi/Desktop/PhDYork/jcsg202Run/PhenixResults/PDBs").listFiles();
		// FilesNames=AddFileNameToList(FilesNames);
		 for (File file : processedfiles) {
			 System.out.println(file.getName().substring(0,file.getName().indexOf('.')));	 
		 }*/
		 /*
		 int count=0;
		 for (File file : processedfiles) {
			 System.out.println(file.getName().substring(0,file.getName().indexOf('.')));	
			 
			if(file.length()<35840) {
				 System.out.println(file.getName().substring(0,file.getName().indexOf('.')));	
				String NameOfFile=file.getName().substring(0,file.getName().indexOf('.'));
				 new FilesManagements().RemoveFile(file);

				 if(new FilesManagements().CheckingIfFileExists(new File("/Users/emadalharbi/Desktop/PhDYork/jcsg202Run/PhenixResults/PDBs"+"/"+NameOfFile+".pdb"))){
					 count++;
					 new FilesManagements().RemoveFile(new File("/Users/emadalharbi/Desktop/PhDYork/jcsg202Run/PhenixResults/PDBs"+"/"+NameOfFile+".pdb"));
				 }
				
			}*/
			 
				/*
			 boolean isFound=false; 
			 for (File file2 : processedfilesPDBs) {
				
				 if(file.getName().substring(0,file.getName().indexOf('.')).equals(file2.getName().substring(0,file2.getName().indexOf('.')))) {
					 isFound=true; 
					 //System.out.print(file.getName().substring(0,file.getName().indexOf('.'))+" " );
					 //System.out.println(file2.getName().substring(0,file2.getName().indexOf('.'))+" " );
				 }
			 }
			 if(isFound==false) {
				 System.out.println(file.getName().substring(0,file.getName().indexOf('.')));	
			 }
			 */
		// }
		 //System.out.println(count);	
		 //System.out.println(processedfilesPDBs.length);
		 //System.out.println(processedfiles.length);
		 /*
		 for (File file : processedfiles) {
			// System.out.println(file.getName().substring(0,file.getName().indexOf('.')));
			 boolean Found=false;
			 for (File filePDB : processedfilesPDBs) {
			 if(file.getName().substring(0,file.getName().indexOf('.')).equals(filePDB.getName().substring(0,filePDB.getName().indexOf('.')))){
					 //System.out.println(FilesNames.get(i));
				 Found=true;
					 break;
				 } 
				
			 }
			 if(Found==false)
			 FilesNames.add(file.getName());
		 }
			 for(int i=0 ; i <FilesNames.size() ; ++i){
				 System.out.println(FilesNames.get(i)); 
			 }
			 System.out.println(FilesNames.size()); 
			 /*
			 for(int i=0 ; i <FilesNames.size() ; ++i){
			 if(file.getName().substring(0,file.getName().indexOf('.')).equals(FilesNames.get(i))){
				 //System.out.println(FilesNames.get(i));
				 FilesNames.remove(i);
				 break;
			 }
			 }
			 
		 }*/
		// System.out.println(FilesNames.size());
		 /*
		 for(int i=0 ; i <FilesNames.size() ; ++i){
			 System.out.println(FilesNames.get(i)); 
		 }
		 */
		 
	}
	 static Vector<String> AddFileNameToList( Vector<String> FilesNames) throws IOException{
			File yourFile = new File("/Users/emadalharbi/Desktop/PhDYork/TestingData/ProcessedFilesNamesArp.txt");
			yourFile.createNewFile();
			 String FileNamesTxt=new ARPResultsAnalysis().readFileAsString("/Users/emadalharbi/Desktop/PhDYork/TestingData/ProcessedFilesNamesArp.txt");
			 FilesNames.addAll(Arrays.asList(FileNamesTxt.split("\n")));
			 return FilesNames;
			
		}
}
