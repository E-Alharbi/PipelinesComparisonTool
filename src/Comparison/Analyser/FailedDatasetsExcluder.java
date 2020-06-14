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

public class FailedDatasetsExcluder {

	public void Exclude(String Path) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		
      
		
		
		File [] DatasetFolders = new File(Path).listFiles();
		
		String PathToWrite=new File(Path).getParent()+"/"+new File(Path).getName()+"ExFaliedCases";
		new RunComparison().CheckDirAndFile(PathToWrite);
		for(File Folder : DatasetFolders) {
			if(Folder.isDirectory()) {
				
				new RunComparison().CheckDirAndFile(PathToWrite+"/"+Folder.getName());
				Vector<Vector<ExcelContents>> AllToolsData = new Vector<Vector<ExcelContents>>();
				ExcelLoader e = new ExcelLoader();
				for(File Excel : Folder.listFiles()) {
					
					AllToolsData.add(e.ReadExcel(Excel.getAbsolutePath()));
				
				
				
				}
				for(File Excel : Folder.listFiles()) {
					AllToolsData.add(e.ReadExcel(Excel.getAbsolutePath()));
					
					Vector<ExcelContents> ModifiedExcel=e.CheckPDBexists(AllToolsData, e.ReadExcel(Excel.getAbsolutePath()));
					if(ModifiedExcel.size()==0)
					System.out.println("Warning: This file "+Excel.getAbsolutePath()+" is empty and will result in incorrect analysis. This might happen when a pipeline did not build any model.   ");
					new ExcelSheet().FillInExcel(ModifiedExcel, PathToWrite+"/"+Folder.getName()+"/"+Excel.getName());
				}
				
			}
		}
		
	}

	
}
