package Comparison.Analyser;
/**
*
* @author Emad Alharbi
* University of York
*/
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Vector;

import Comparison.Runner.RunComparison;

public class OrginalAndSyntheticSplitter {

	public  void Split(String PathToExperimentExcel) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
	
		File [] DatasetFolders = new File(PathToExperimentExcel).listFiles();
		for(File Folder : DatasetFolders) {
			if(Folder.isDirectory()) {
				
				String OrginalPath=Folder.getParent()+"/Orginal/";
				new RunComparison().CheckDirAndFile(OrginalPath);
				 OrginalPath=Folder.getParent()+"/Orginal/"+Folder.getName()+"/";;
				new RunComparison().CheckDirAndFile(OrginalPath);
				
				String SyntheticPath=Folder.getParent()+"/Synthetic/";
				new RunComparison().CheckDirAndFile(SyntheticPath);
				 SyntheticPath=Folder.getParent()+"/Synthetic/"+Folder.getName()+"/";;
				new RunComparison().CheckDirAndFile(SyntheticPath);
				
				for(File Excel : Folder.listFiles()) {
					if(Excel.isFile()) {
					 ExcelLoader e = new ExcelLoader();
					 
					Vector<ExcelContents> Container = e.ReadExcel(Excel.getAbsolutePath());
					Vector<ExcelContents> OrginalContainer = new Vector<ExcelContents>();
					Vector<ExcelContents> SyntheticContainer = new Vector<ExcelContents>();
					Vector <String> CheckedCases= new Vector <String>();
					for(int i=0 ; i < Container.size() ; ++i ) {
						Vector<ExcelContents> AllCaseReso= new Vector<ExcelContents> ();
						if(!CheckedCases.contains(Container.get(i).PDBIDTXT)) {
						for(int o=0 ; o < Container.size() ; ++o ) { // loop to find the orginal case by comparing the reso
							if(Container.get(i).PDBIDTXT.equals(Container.get(o).PDBIDTXT)) {
								AllCaseReso.add(Container.get(o));
							}
						}
						CheckedCases.add(Container.get(i).PDBIDTXT);
						Collections.sort(AllCaseReso, ExcelContents.DataContainerComparator);// Sorting based on Reso
						
						OrginalContainer.addElement(AllCaseReso.get(0));
						AllCaseReso.remove(0); // Removing first record which is the Orginal case 
						SyntheticContainer.addAll(AllCaseReso);// the remaining are the  Synthetic cases. 
						}
						
					}
					
					//Writing to new excel files 
					new ExcelSheet().FillInExcel(OrginalContainer, OrginalPath+Excel.getName());
					new ExcelSheet().FillInExcel(SyntheticContainer, SyntheticPath+Excel.getName());
					Container.clear();
					OrginalContainer.clear();
					SyntheticContainer.clear();
				}
				}
				
				File [] BuccExcel=new File(OrginalPath).listFiles();
				//new RunComparison().CheckDirAndFile(OrginalPath+"/BuccEx54");
				String OrginalPathBuccEx54=Folder.getParent()+"/OrginalBuccEx54/";
				new RunComparison().CheckDirAndFile(OrginalPathBuccEx54);
				new RunComparison().CheckDirAndFile(OrginalPathBuccEx54+"/"+Folder.getName());
				for(File BExcel : BuccExcel) {
						ExcelLoader e = new ExcelLoader();
						Vector<ExcelContents> BuccContainer = new Excluding54Datasets().Exculding(e.ReadExcel(BExcel.getAbsolutePath()),true);
						new ExcelSheet().FillInExcel(BuccContainer, OrginalPathBuccEx54+"/"+Folder.getName()+"/"+BExcel.getName());
	
				}
				
				String OrginalPathBuccInc54=Folder.getParent()+"/OrginalBuccInc54/";
				new RunComparison().CheckDirAndFile(OrginalPathBuccInc54);
				new RunComparison().CheckDirAndFile(OrginalPathBuccInc54+"/"+Folder.getName());
				
				for(File BExcel : BuccExcel) {
					
					
						ExcelLoader e = new ExcelLoader();
						Vector<ExcelContents> BuccContainer = new Excluding54Datasets().Exculding(e.ReadExcel(BExcel.getAbsolutePath()),false);
						new ExcelSheet().FillInExcel(BuccContainer, OrginalPathBuccInc54+"/"+Folder.getName()+"/"+BExcel.getName());

					
				}
				
				
				
				File [] SyntheticPathExcel=new File(SyntheticPath).listFiles();
				//new RunComparison().CheckDirAndFile(OrginalPath+"/BuccEx54");
				String SyntheticPathBuccEx54=Folder.getParent()+"/SyntheticBuccEx54/";
				new RunComparison().CheckDirAndFile(SyntheticPathBuccEx54);
				new RunComparison().CheckDirAndFile(SyntheticPathBuccEx54+"/"+Folder.getName());
				for(File BExcel : SyntheticPathExcel) {
						ExcelLoader e = new ExcelLoader();
						Vector<ExcelContents> BuccContainer = new Excluding54Datasets().Exculding(e.ReadExcel(BExcel.getAbsolutePath()),true);
						new ExcelSheet().FillInExcel(BuccContainer, SyntheticPathBuccEx54+"/"+Folder.getName()+"/"+BExcel.getName());
	
				}
				
				String SyntheticPathBuccInc54=Folder.getParent()+"/SyntheticBuccInc54/";
				new RunComparison().CheckDirAndFile(SyntheticPathBuccInc54);
				new RunComparison().CheckDirAndFile(SyntheticPathBuccInc54+"/"+Folder.getName());
				
				for(File BExcel : SyntheticPathExcel) {
					
					
						ExcelLoader e = new ExcelLoader();
						Vector<ExcelContents> BuccContainer = new Excluding54Datasets().Exculding(e.ReadExcel(BExcel.getAbsolutePath()),false);
						new ExcelSheet().FillInExcel(BuccContainer, SyntheticPathBuccInc54+"/"+Folder.getName()+"/"+BExcel.getName());

					
				}
				
				
			}
		}
	}

}
