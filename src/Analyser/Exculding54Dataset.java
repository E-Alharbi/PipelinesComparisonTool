package Analyser;
/**
*
* @author Emad Alharbi
* University of York
*/
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

import Run.RunComparison;

public class Exculding54Dataset {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		String PathToDM="/Volumes/PhDHardDrive/jcsg1200Results/Fasta/VikingRun1ArpNoFree/SyntheticExFaliedCases";
		boolean Exculded=true;
		String FolderName="";
		if(Exculded==true)
			FolderName="ExcludedBuccaneerDevSet";
		if(Exculded==false)
			FolderName="IncludedBuccaneerDevSet";
		File [] Folders= new File(PathToDM).listFiles();
		new RunComparison().CheckDirAndFile(PathToDM+"/"+FolderName);
		for(File Folder : Folders) {
			if(Folder.isDirectory()) {
				new RunComparison().CheckDirAndFile(PathToDM+"/"+FolderName+"/"+Folder.getName());
				for(File Excel : Folder.listFiles()) {
					if(Excel.isFile()) {
					System.out.println(Excel.getAbsolutePath());
					ExcelLoader e = new ExcelLoader();
					Vector<DataContainer> BuccContainer = e.ReadExcel(Excel.getAbsolutePath());
					BuccContainer=new Exculding54Dataset().Exculding(BuccContainer,Exculded);
					new ExcelSheet().FillInExcel(BuccContainer, PathToDM+"/"+FolderName+"/"+Folder.getName()+"/"+Excel.getName());}
				}
					
			}
		}
		
	
		
	}
	
	public Vector<DataContainer> Exculding(Vector<DataContainer> Container , boolean ExculdingTheCases) throws IOException {
		// TODO Auto-generated method stub

		String  Dataset54=new ResultsAnalyserMultiThreads().readFileAsString("datasets.54");
		Vector<DataContainer> BuccExThe54Dataset= new Vector<DataContainer>();
			for(int o=0 ; o < Container.size() ; ++o ) {
				if(!Dataset54.contains(Container.get(o).PDBIDTXT) && ExculdingTheCases==true) {
					BuccExThe54Dataset.add(Container.get(o));
				}
				if(Dataset54.contains(Container.get(o).PDBIDTXT) && ExculdingTheCases==false) {
					BuccExThe54Dataset.add(Container.get(o));
				}
			}
		
return BuccExThe54Dataset;
	}

}
