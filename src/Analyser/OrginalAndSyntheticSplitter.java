package Analyser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Vector;

import org.apache.commons.io.FileUtils;

import Run.RunComparison;
import Run.RunningPram;

public class OrginalAndSyntheticSplitter {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
	
		File [] DatasetFolders = new File("/Users/emadalharbi/Desktop/Arp").listFiles();
		for(File Folder : DatasetFolders) {
			if(Folder.isDirectory()) {
				//String OrginalPath=Folder.getAbsolutePath()+"/Orginal/";
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
					 System.out.println(Excel.getAbsolutePath());
					Vector<DataContainer> Container = e.ReadExcel(Excel.getAbsolutePath());
					Vector<DataContainer> OrginalContainer = new Vector<DataContainer>();
					Vector<DataContainer> SyntheticContainer = new Vector<DataContainer>();
					Vector <String> CheckedCases= new Vector <String>();
					for(int i=0 ; i < Container.size() ; ++i ) {
						Vector<DataContainer> AllCaseReso= new Vector<DataContainer> ();
						if(!CheckedCases.contains(Container.get(i).PDBIDTXT)) {
						for(int o=0 ; o < Container.size() ; ++o ) { // loop to find the orginal case by comparing the reso
							if(Container.get(i).PDBIDTXT.equals(Container.get(o).PDBIDTXT)) {
								AllCaseReso.add(Container.get(o));
							}
						}
						CheckedCases.add(Container.get(i).PDBIDTXT);
						Collections.sort(AllCaseReso, DataContainer.DataContainerComparator);// Sorting based on Reso
						
						OrginalContainer.addElement(AllCaseReso.get(0));
						AllCaseReso.remove(0); // Removing first record which is the Orginal case 
						SyntheticContainer.addAll(AllCaseReso);// the remaining are the  Synthetic cases. 
						}
						
					}
					System.out.println(OrginalContainer.size());
					System.out.println(SyntheticContainer.size());
					//Writing to new excel files 
					new ExcelSheet().FillInExcel(OrginalContainer, OrginalPath+Excel.getName());
					new ExcelSheet().FillInExcel(SyntheticContainer, SyntheticPath+Excel.getName());
					Container.clear();
					OrginalContainer.clear();
					SyntheticContainer.clear();
				}
				}
				File [] BuccExcel=new File(OrginalPath).listFiles();
				new RunComparison().CheckDirAndFile(OrginalPath+"/BuccEx54");
				for(File BExcel : BuccExcel) {
					
					//if(BExcel.isFile() && BExcel.getName().contains("Buccaneer")) {
						ExcelLoader e = new ExcelLoader();
						Vector<DataContainer> BuccContainer = new Exculding54Dataset().Exculding(e.ReadExcel(BExcel.getAbsolutePath()),true);
						new ExcelSheet().FillInExcel(BuccContainer, OrginalPath+"/BuccEx54/"+BExcel.getName()+"Ex54");

					//}
					//else {
					//	 FileUtils.copyFile(BExcel,  new File(OrginalPath+"/BuccEx54/"+BExcel.getName()));
					//}
				}
				
				
				new RunComparison().CheckDirAndFile(OrginalPath+"/BuccInc54");
				for(File BExcel : BuccExcel) {
					
					//if(BExcel.isFile() && BExcel.getName().contains("Buccaneer")) {
						ExcelLoader e = new ExcelLoader();
						Vector<DataContainer> BuccContainer = new Exculding54Dataset().Exculding(e.ReadExcel(BExcel.getAbsolutePath()),false);
						new ExcelSheet().FillInExcel(BuccContainer, OrginalPath+"/BuccInc54/"+BExcel.getName()+"Ex54");

					//}
					//else {
					//	 FileUtils.copyFile(BExcel,  new File(OrginalPath+"/BuccEx54/"+BExcel.getName()));
					//}
				}
			}
		}
	}

}
