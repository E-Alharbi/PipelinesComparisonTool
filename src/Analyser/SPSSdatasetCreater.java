package Analyser;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Vector;

import org.apache.commons.io.FileUtils;

import Run.Preparer;

public class SPSSdatasetCreater {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		  String CSV="DatasetNumber,DM_Types,Pipelines,File,Resolution,Time_Taking_In_Minutes,R-factorLog,R-freeLog, R-factor Δ R-free,Overfitting?, R Factor,R Free,Optimal R-factor,Structure_Size,Number of Atoms in Second PDB,Number Of Atoms In First Not In Second,Number Of Atoms In Second Not In First,Seq(r/(n1n2+n2n1)),n1m2,n2m1,F-map,E-map,BuiltPDB,Warring Time Taking,Warring Log Size,Exception (No Log File),Ramachandran Outliers,Ramachandran Favored,Rotamer Outliers,Clash Score,RMS Bonds,RMS Angles,MolProbity Score,RWorkMolPro,RFreeMolPro,Refinement Program,Intermediate,Completeness,PDB ID \n" ;
		  		;
		String FolderPath="/Users/emadalharbi/Desktop/Arp/ExFaliedCases/ExcludedBuccaneerDevSet"; //where to save the csv file 
		File [] DatasetFolders = new File(FolderPath).listFiles();
		int FolderNumber=0;	
		  for(File Folder : DatasetFolders) {
				
				if(Folder.isDirectory()) {
					++FolderNumber;
					for(File Excel : Folder.listFiles()) {
						System.out.println(Excel.getAbsolutePath());
						 ExcelLoader e = new ExcelLoader();
							Vector<DataContainer> Container = e.ReadExcel(Excel.getAbsolutePath());
							
							for(int i=0; i < Container.size() ; ++i) {
							System.out.println(Container.get(i).toString());
							if(Container.get(i).BuiltPDB.equals("T")) {
								//DecimalFormat df = new DecimalFormat("#.#");
								//df.setRoundingMode(RoundingMode.HALF_UP);
								
								//Container.get(i).Resolution = df.format(BigDecimal.valueOf(Double.valueOf(Container.get(i).Resolution)));
								Container.get(i).Completeness=	new BigDecimal(Container.get(i).Completeness).setScale(0, RoundingMode.HALF_UP).toString();
							CSV+=FolderNumber+","+Folder.getName() + ","+Excel.getName()+","+Container.get(i).toString() +"\n";
							
							}
						}
					}
				}
			}
		
		  CSV=new ResultsInLatex().FormatingPipelinesNames(CSV,false);
		  FileUtils.deleteQuietly(new File(FolderPath+"/SPSSDataset.csv")); // Removing previous csv if any 
             	new Preparer().WriteTxtFile(FolderPath+"/SPSSDataset.csv", CSV);
	}

}
