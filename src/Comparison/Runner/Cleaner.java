package Comparison.Runner;
/**
*
* @author Emad Alharbi
* University of York
*/
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;

import org.apache.commons.io.FilenameUtils;

import Comparison.Analyser.ExcelContents;
import Comparison.Analyser.ExcelLoader;

public class Cleaner {

	/*
	 * 
	 * When need to rerun the dataset on failed cases, we need to remove the temp folders and files and update the the 
	 * txt file which record the names of the files that ran. This class do this job!  
	 * 
	 */
	

	
	

	void CleanerUsingExcelSheets(String ExcelSheetFolderPath) throws IOException {
		ExcelLoader e = new ExcelLoader();
		
		 File[] Dataset = new File(ExcelSheetFolderPath).listFiles();
			
		 for (File folder : Dataset) {
			 
			 if(folder.isDirectory()) {
				 for (File Excel : folder.listFiles()) {
					 String ExcelName= Excel.getName().replaceAll("."+FilenameUtils.getExtension(Excel.getName()),"");
					 Vector<ExcelContents> Container = e.ReadExcel(Excel.getAbsolutePath());
					 new Cleaner().WrtieTheCleanerScript(Container, folder.getName()+ExcelName);
				 }
			 }
			
		 }
	}
	
	void WrtieTheCleanerScript(Vector<ExcelContents> Container, String ExcelName) throws IOException {
		String Script="";
		String PDB="";
		String TxtFileName="";
		
        for(int i=0; i < Container.size();++i ) {
		
			
       
        
			if(Container.get(i).BuiltPDB.equals("F") || ((Container.get(i).Intermediate.equals("T") && Math.round(Double.valueOf(Container.get(i).TimeTaking)/60) < 48 ))) {
				
			if(ExcelName.contains("ARP")) {
				Script+="rm wArpResults/PDBs/"+Container.get(i).PDB_ID+".pdb \n";
				Script+="rm wArpResults/ArpLogs/"+Container.get(i).PDB_ID+".txt \n";
				Script+="rm wArpResults/IntermediatePDBs/"+Container.get(i).PDB_ID+".pdb \n";
				Script+="rm wArpResults/IntermediateLogs/"+Container.get(i).PDB_ID+".txt \n";
				TxtFileName="ProcessedFilesNamesArp.txt";
			}
			else if(ExcelName.contains("Buccaneer")) {
				Script+="rm BuccaneerResults/PDBs/"+Container.get(i).PDB_ID+".pdb \n";
				Script+="rm BuccaneerResults/BuccaneerLogs/"+Container.get(i).PDB_ID+".txt \n";
				Script+="rm BuccaneerResults/IntermediatePDBs/"+Container.get(i).PDB_ID+".pdb \n";
				Script+="rm BuccaneerResults/IntermediateLogs/"+Container.get(i).PDB_ID+".txt \n";
				TxtFileName="ProcessedFilesNamesBuccaneer.txt";
			}
			else if(ExcelName.contains("Crank")) {
				Script+="rm CrankResults/PDBs/"+Container.get(i).PDB_ID+".pdb \n";
				Script+="rm CrankResults/CrankLogs/"+Container.get(i).PDB_ID+".txt \n";
				Script+="rm CrankResults/IntermediatePDBs/"+Container.get(i).PDB_ID+".pdb \n";
				Script+="rm CrankResults/IntermediateLogs/"+Container.get(i).PDB_ID+".txt \n";
				TxtFileName="ProcessedFilesNamesCrank.txt";
			}
			else if(ExcelName.contains("Phenix")) {
				Script+="rm PhenixResults/PDBs/"+Container.get(i).PDB_ID+".pdb \n";
				Script+="rm PhenixResults/PhinexLogs/"+Container.get(i).PDB_ID+".txt \n";
				Script+="rm PhenixResults/IntermediatePDBs/"+Container.get(i).PDB_ID+".pdb \n";
				Script+="rm PhenixResults/IntermediateLogs/"+Container.get(i).PDB_ID+".txt \n";
				TxtFileName="ProcessedFilesNamesPhenix.txt";
			}
			}
			else {
			  	
				PDB+=Container.get(i).PDB_ID+"\n";
				
			}
			
			
			}
       if(!Script.equals("")) {
        new RunComparison().CheckDirAndFile("RemoverScripts");
        new RunComparison().CheckDirAndFile("RemoverScripts/"+ExcelName);
		new Preparer().WriteTxtFile("RemoverScripts/"+ExcelName+"/RemoverScript.sh",Script);
		new Preparer().WriteTxtFile("RemoverScripts/"+ExcelName+"/"+TxtFileName,PDB);
       }
	}
}
