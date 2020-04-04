package Comparison.Analyser;
/**
*
* @author Emad Alharbi
* University of York
*/
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import Comparison.Runner.Preparer;
import Comparison.Runner.RunningParameter;

public class BinsCreater {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		//RunningParameter.MR="T";
		RunningParameter.ReplaceToSingleLetter="T";
		new BinsCreater().Bining("OrginalBuccEx54ExFaliedCases");
	}
	public  void Bining(String Path) throws IOException {
		// TODO Auto-generated method stub
		String PathForDM=Path;
	File [] ExcelPath=new File(PathForDM).listFiles();
		String CSV="DM,Pipeline,PDB,Resolution,Completness,ResoInBins,Rwork,Rfree,Fmap,Emap,IncorrectlyBuilt,Time,SizeInBins,bin,SizeBin\n";
		
		for(File Folder : ExcelPath) {
			if(Folder.isDirectory()) {
				
				
				for(File Excel:Folder.listFiles()) {
					
					
					ExcelLoader e = new ExcelLoader();
					Vector<ExcelContents> Container = e.ReadExcel(Excel.getAbsolutePath());
					HashMap<String, Integer> NumberOfDatasetsInReso = new HashMap<String, Integer>();
					HashMap<String, Integer> NumberOfDatasetsInSizeGroup = new HashMap<String, Integer>();	
					
					for(int i=0 ; i < Container.size() ; ++i) {
						
						
						CSV+=Folder.getName()+",";
						CSV+=Excel.getName()+",";
						CSV+=Container.get(i).PDB_ID+",";  
						CSV+=Container.get(i).Resolution+","; 
						CSV+=Container.get(i).Completeness+","; 
						String Val="";
						if(RunningParameter.MR.equals("F")) {
						if(Double.parseDouble(Container.get(i).Resolution) <2) {
							Val="1.0 - 1.9";
							
						}
						  if(Double.parseDouble(Container.get(i).Resolution) >=2 && Double.parseDouble(Container.get(i).Resolution) <=3.1) {
							  Val="2.0 - 3.1";
						}
						 if(Double.parseDouble(Container.get(i).Resolution) ==3.2) {
							 Val="3.2";
						}
						 if(Double.parseDouble(Container.get(i).Resolution) ==3.4) {
							 Val="3.4";
						}
						 if(Double.parseDouble(Container.get(i).Resolution) ==3.6) {
							 Val="3.6";
						}
						 if(Double.parseDouble(Container.get(i).Resolution) ==3.8) {
							 Val="3.8";
						}
						 if(Double.parseDouble(Container.get(i).Resolution) == 4) {
							 Val="4.0";
						}
					}
						
						if(RunningParameter.MR.equals("T")) {
							if(Double.parseDouble(Container.get(i).Resolution) <1.6) {
								Val="1.0 - 1.5";
								
							}
							  if(Double.parseDouble(Container.get(i).Resolution) >=1.6 && Double.parseDouble(Container.get(i).Resolution) <=2) {
								  Val="1.6 - 2.0";
							}
							  
							  if(Double.parseDouble(Container.get(i).Resolution) >=2.1 && Double.parseDouble(Container.get(i).Resolution) <=2.5) {
								  Val="2.1 - 2.5";
							}
							  
							  if(Double.parseDouble(Container.get(i).Resolution) >=2.6 && Double.parseDouble(Container.get(i).Resolution) <=3.0) {
								  Val="2.6 - 3.0";
							}
							  if(Double.parseDouble(Container.get(i).Resolution) >=3.1) {
								  Val="3.1+";
							}
						}
						
						if(Val.trim().length()==0)// if not grouped 
							Val="unclassified";
						
						 Container.get(i).Resolution=Val;
						
						if(NumberOfDatasetsInReso.containsKey(Container.get(i).Resolution+"ReservedForNumOfDatasetsInResoBin")) {
							NumberOfDatasetsInReso.put(Container.get(i).Resolution+"ReservedForNumOfDatasetsInResoBin", NumberOfDatasetsInReso.get(Container.get(i).Resolution+"ReservedForNumOfDatasetsInResoBin")+1);
						}
						else {
							NumberOfDatasetsInReso.put(Container.get(i).Resolution+"ReservedForNumOfDatasetsInResoBin",1);
							
						}
						
						
						
						CSV+=Container.get(i).Resolution+","; 
						CSV+=Container.get(i).R_factor0Cycle+",";  
						CSV+=Container.get(i).R_free0Cycle+",";  
						CSV+=Container.get(i).F_mapCorrelation+","; 
						CSV+=Container.get(i).E_mapCorrelation+",";
						
						//double IncorreclyBuilt= ((Double.parseDouble(Container.get(i).NumberofAtomsinSecondPDB) - Double.parseDouble(Container.get(i).NumberOfAtomsInSecondNotInFirst))) /Double.parseDouble(Container.get(i).NumberofAtomsinFirstPDB); 
						
						CSV+=new BigDecimal(Container.get(i).NumberofAtomsinSecondPDB).subtract(new BigDecimal(Container.get(i).NumberOfAtomsInSecondNotInFirst)).divide(new BigDecimal(Container.get(i).NumberofAtomsinFirstPDB),2,RoundingMode.HALF_UP)+",";
						//CSV+=Math.round(IncorreclyBuilt)+",";
						CSV+=Container.get(i).TimeTaking+",";
						
						 Val="";
						if(Double.parseDouble(Container.get(i).NumberofAtomsinFirstPDB) <=200) {
							
							Val="<= 200";
						}
						 if(Double.parseDouble(Container.get(i).NumberofAtomsinFirstPDB) >=201 && Double.parseDouble(Container.get(i).NumberofAtomsinFirstPDB) <=400) {
							 Val="201 - 400";
						}
						 if(Double.parseDouble(Container.get(i).NumberofAtomsinFirstPDB) >=401 && Double.parseDouble(Container.get(i).NumberofAtomsinFirstPDB) <=600) {
							 Val="401 - 600";
						}
						 if(Double.parseDouble(Container.get(i).NumberofAtomsinFirstPDB) >=601 && Double.parseDouble(Container.get(i).NumberofAtomsinFirstPDB) <=800)  {
							 Val="601 - 800";
						}
						 if(Double.parseDouble(Container.get(i).NumberofAtomsinFirstPDB) >=801 && Double.parseDouble(Container.get(i).NumberofAtomsinFirstPDB) <=1000)  {
							 Val="801 - 1000";
						}
						 if(Double.parseDouble(Container.get(i).NumberofAtomsinFirstPDB) >=1001 && Double.parseDouble(Container.get(i).NumberofAtomsinFirstPDB) <=1500)  {
							 Val="1001 - 1500";
						}
						 if(Double.parseDouble(Container.get(i).NumberofAtomsinFirstPDB) >=1501)  {
							 Val="1501+";
						}
						Container.get(i).NumberofAtomsinFirstPDB=Val;
						 
						CSV+=Container.get(i).NumberofAtomsinFirstPDB+",";
						CSV+=Container.get(i).Resolution+"ReservedForNumOfDatasetsInResoBin"+",";
						CSV+=Container.get(i).NumberofAtomsinFirstPDB+"ReservedForNumOfDatasetsInSizeBin"+"\n";
						
						if(NumberOfDatasetsInSizeGroup.containsKey(Container.get(i).NumberofAtomsinFirstPDB+"ReservedForNumOfDatasetsInSizeBin")) {
							NumberOfDatasetsInSizeGroup.put(Container.get(i).NumberofAtomsinFirstPDB+"ReservedForNumOfDatasetsInSizeBin", NumberOfDatasetsInSizeGroup.get(Container.get(i).NumberofAtomsinFirstPDB+"ReservedForNumOfDatasetsInSizeBin")+1);
						}
						else {
							NumberOfDatasetsInSizeGroup.put(Container.get(i).NumberofAtomsinFirstPDB+"ReservedForNumOfDatasetsInSizeBin",1);
						}
					}
					
			        
				
				for (String i : NumberOfDatasetsInReso.keySet()) {
					  
					 //CSV=CSV.replaceAll(i, i.replaceAll("ReservedForNumOfDatasetsInResoBin", "")+"\t("+ String.valueOf(NumberOfDatasetsInReso.get(i)) +")");
					 CSV=CSV.replaceAll(Pattern.quote(i), i.replaceAll("ReservedForNumOfDatasetsInResoBin", "")+"\t("+ String.valueOf(NumberOfDatasetsInReso.get(i)) +")");
					
				}
				  
				  for (String i : NumberOfDatasetsInSizeGroup.keySet()) {
					  CSV=CSV.replaceAll(Pattern.quote(i), i.replaceAll("ReservedForNumOfDatasetsInSizeBin", "")+"\t("+ String.valueOf(NumberOfDatasetsInSizeGroup.get(i)) +")"); // using Pattern.quote(i) because some of the cells contain plus character which cannot be replaced with using Pattern.quote(i) 
					  
					}
				}
			}
		}  
		FileUtils.deleteQuietly(new File(PathForDM+"/SPSSDatasetBins.csv")); // Removing previous csv if any 
		 
		CSV=new ComparisonMeasures().FormatingPipelinesNames(CSV,false,false);
     	
		  
		  new Preparer().WriteTxtFile(PathForDM+"/SPSSDatasetBins.csv", CSV);

	}

}
