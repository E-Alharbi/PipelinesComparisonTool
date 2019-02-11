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
import Run.RunningPram;

public class FailedCasesExcluder {

	public void Exclude(String Path) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		
       // String Path="/Volumes/PhDHardDrive/jcsg1200Results/Fasta/VikingRun1ArpNoFree/Synthetic";
		
		
		File [] DatasetFolders = new File(Path).listFiles();
		//new RunComparison().CheckDirAndFile(Path+"/ExFaliedCases");
		String PathToWrite=new File(Path).getParent()+"/"+new File(Path).getName()+"ExFaliedCases";
		new RunComparison().CheckDirAndFile(PathToWrite);
		for(File Folder : DatasetFolders) {
			if(Folder.isDirectory()) {
				System.out.println("Folder "+Folder.getName() );
				new RunComparison().CheckDirAndFile(PathToWrite+"/"+Folder.getName());
				Vector<Vector<ExcelContents>> AllToolsData = new Vector<Vector<ExcelContents>>();
				ExcelLoader e = new ExcelLoader();
				for(File Excel : Folder.listFiles()) {
					System.out.println(Excel.getAbsolutePath());
					AllToolsData.add(e.ReadExcel(Excel.getAbsolutePath()));
				
				
				
				}
				for(File Excel : Folder.listFiles()) {
					AllToolsData.add(e.ReadExcel(Excel.getAbsolutePath()));
					System.out.println("Excel "+Excel.getName());
					Vector<ExcelContents> ModifiedExcel=e.CheckPDBexists(AllToolsData, e.ReadExcel(Excel.getAbsolutePath()));
					System.out.println("ModifiedExcel "+ModifiedExcel.size());
					new ExcelSheet().FillInExcel(ModifiedExcel, PathToWrite+"/"+Folder.getName()+"/"+Excel.getName());
				}
				
			}
		}
		
	}

	
}
