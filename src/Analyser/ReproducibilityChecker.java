package Analyser;

import java.io.File;
import java.util.Vector;

public class ReproducibilityChecker {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File [] ReproducibilityFolders = new File("/Volumes/PhDHardDrive/jcsg1200Results/Fasta/Reproducibility/").listFiles();
		File [] DatasetFolders = new File("/Volumes/PhDHardDrive/jcsg1200Results/Fasta/Run6/All").listFiles();
		
		
		for(File Folder : ReproducibilityFolders) {
			for(File dataset : DatasetFolders) {
				if(Folder.getName().equals(dataset.getName())){
					
					for(File Excel : Folder.listFiles()) {
						for(File ExcelFromDataset : dataset.listFiles()) {
							if(Excel.getName().equals(ExcelFromDataset.getName())){
								 ExcelLoader e = new ExcelLoader();
								
							Vector<DataContainer> ReproducibilityContainer = e.ReadExcel(Excel.getAbsolutePath());
							Vector<DataContainer> DatasetContainer = e.ReadExcel(ExcelFromDataset.getAbsolutePath());
							
							for(int i=0 ; i < ReproducibilityContainer.size() ; ++i) {
								for(int d=0 ; d < DatasetContainer.size() ; ++d) {
									if(ReproducibilityContainer.get(i).PDB_ID.equals(DatasetContainer.get(d).PDB_ID)) {
										if(!ReproducibilityContainer.get(i).Completeness.equals("None") && !DatasetContainer.get(d).Completeness.equals("None") ) 
										if(!ReproducibilityContainer.get(i).Completeness.equals(DatasetContainer.get(d).Completeness) && ReproducibilityContainer.get(i).Intermediate.equals(DatasetContainer.get(d).Intermediate) && ReproducibilityContainer.get(i).BuiltPDB.equals(DatasetContainer.get(d).BuiltPDB)) {
											 System.out.println("##### Completeness ##### ");	
									    System.out.println("Excel 1 "+ Excel.getName());	
										System.out.println("Excel 2 "+ ExcelFromDataset.getName());	
										System.out.println("Reproducibility PDB_ID "+ ReproducibilityContainer.get(i).PDB_ID);	
										System.out.println("DatasetContainer PDB_ID "+ DatasetContainer.get(d).PDB_ID);
										System.out.println("Reproducibility Completeness "+ ReproducibilityContainer.get(i).Completeness);	
										System.out.println("DatasetContainer Completeness "+ DatasetContainer.get(d).Completeness);
										 System.out.println("##### Completeness ##### ");	
										}
										if(!ReproducibilityContainer.get(i).R_factor0Cycle.equals("None") && !DatasetContainer.get(d).R_factor0Cycle.equals("None") && ReproducibilityContainer.get(i).Intermediate.equals(DatasetContainer.get(d).Intermediate) && ReproducibilityContainer.get(i).BuiltPDB.equals(DatasetContainer.get(d).BuiltPDB) ) 
										if(!ReproducibilityContainer.get(i).R_factor0Cycle.equals(DatasetContainer.get(d).R_factor0Cycle)) {
											System.out.println("##### Rfactor ##### ");	
											System.out.println("Excel 1 "+ Excel.getName());	
											System.out.println("Excel 2 "+ ExcelFromDataset.getName());	
											System.out.println("Reproducibility PDB_ID "+ ReproducibilityContainer.get(i).PDB_ID);	
											System.out.println("DatasetContainer PDB_ID "+ DatasetContainer.get(d).PDB_ID);
											System.out.println("Reproducibility R_factor0Cycle "+ ReproducibilityContainer.get(i).R_factor0Cycle);	
											System.out.println("DatasetContainer R_factor0Cycle "+ DatasetContainer.get(d).R_factor0Cycle);
											System.out.println("##### Rfactor ##### ");	
											}
									}
								}
							}
							
							}
						}
					}
				}
			}
		}
	}

}
