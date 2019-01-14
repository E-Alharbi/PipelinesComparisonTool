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

	public static void main(String[] args) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
        String Path="/Volumes/PhDHardDrive/jcsg1200Results/Fasta/VikingRun1ArpNoFree/Synthetic";
		
		
		File [] DatasetFolders = new File(Path).listFiles();
		new RunComparison().CheckDirAndFile(Path+"/ExFaliedCases");
		for(File Folder : DatasetFolders) {
			if(Folder.isDirectory()) {
				System.out.println("Folder "+Folder.getName() );
				new RunComparison().CheckDirAndFile(Path+"/ExFaliedCases/"+Folder.getName());
				Vector<Vector<DataContainer>> AllToolsData = new Vector<Vector<DataContainer>>();
				ExcelLoader e = new ExcelLoader();
				for(File Excel : Folder.listFiles()) {
					System.out.println(Excel.getAbsolutePath());
					AllToolsData.add(e.ReadExcel(Excel.getAbsolutePath()));
				
				
				
				}
				for(File Excel : Folder.listFiles()) {
					AllToolsData.add(e.ReadExcel(Excel.getAbsolutePath()));
					System.out.println("Excel "+Excel.getName());
					Vector<DataContainer> ModifiedExcel=e.CheckPDBexists(AllToolsData, e.ReadExcel(Excel.getAbsolutePath()));
					System.out.println("ModifiedExcel "+ModifiedExcel.size());
					new ExcelSheet().FillInExcel(ModifiedExcel, Path+"/ExFaliedCases/"+Folder.getName()+"/"+Excel.getName());
				}
				
			}
		}
		
	}

	
}
