package Comparison.Analyser;
/**
*
* @author Emad Alharbi
* University of York
*/
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

import Comparison.Runner.RunComparison;

public class Excluding54Datasets {
	
	/*
	 * Excluding buccaneer development datasets
	 */
	public  void Exculde(String Path , boolean Exculded) throws FileNotFoundException, IOException {
		String PathToDM=Path;
		
		String FolderName="";
		if(Exculded==true)
			FolderName="ExcludedBuccaneerDevSet";
		if(Exculded==false)
			FolderName="IncludedBuccaneerDevSet";
		File [] Folders= new File(PathToDM).listFiles();
		String PathToWrite=new File(PathToDM).getParent()+"/"+new File(PathToDM).getName()+FolderName;
		new RunComparison().CheckDirAndFile(PathToWrite);
		for(File Folder : Folders) {
			if(Folder.isDirectory()) {
				new RunComparison().CheckDirAndFile(PathToWrite+"/"+Folder.getName());
				for(File Excel : Folder.listFiles()) {
					if(Excel.isFile()) {
					
					ExcelLoader e = new ExcelLoader();
					Vector<ExcelContents> BuccContainer = e.ReadExcel(Excel.getAbsolutePath());
					BuccContainer=new Excluding54Datasets().Exculding(BuccContainer,Exculded);
					new ExcelSheet().FillInExcel(BuccContainer, PathToWrite+"/"+Folder.getName()+"/"+Excel.getName());}
				}
					
			}
		}
		
	
		
	}
	
	public Vector<ExcelContents> Exculding(Vector<ExcelContents> Container , boolean ExculdingTheCases) throws IOException {
		// TODO Auto-generated method stub

		String  Dataset54=new MultiThreadedAnalyser().readFileAsString("datasets.54");
		Vector<ExcelContents> BuccExThe54Dataset= new Vector<ExcelContents>();
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
