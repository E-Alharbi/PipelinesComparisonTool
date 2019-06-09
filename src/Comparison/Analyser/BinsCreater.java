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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.commons.io.FileUtils;

import Comparison.Runner.Preparer;

public class BinsCreater {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		
	}
	public  void Bining(String Path) throws IOException {
		// TODO Auto-generated method stub
		String PathForDM=Path;
	File [] ExcelPath=new File(PathForDM).listFiles();
		String CSV="DM,Pipeline,PDB,Resolution,Completness,bin,Rwork,Rfree,Fmap,Emap\n";
		for(File Folder : ExcelPath) {
			if(Folder.isDirectory()) {
				
				
				for(File Excel:Folder.listFiles()) {
					
					
					ExcelLoader e = new ExcelLoader();
					Vector<ExcelContents> Container = e.ReadExcel(Excel.getAbsolutePath());
					
					
					for(int i=0 ; i < Container.size() ; ++i) {
						
						
						CSV+=Folder.getName()+",";
						CSV+=Excel.getName()+",";
						CSV+=Container.get(i).PDB_ID+",";  
						CSV+=Container.get(i).Resolution+","; 
						CSV+=Container.get(i).Completeness+","; 
						if(Double.parseDouble(Container.get(i).Resolution) <2) {
							Container.get(i).Resolution="1.0";
						}
						if(Double.parseDouble(Container.get(i).Resolution) >=2 && Double.parseDouble(Container.get(i).Resolution) <3.1) {
							Container.get(i).Resolution="2.0";
						}
						if(Double.parseDouble(Container.get(i).Resolution) ==3.2) {
							Container.get(i).Resolution="3.1";
						}
						if(Double.parseDouble(Container.get(i).Resolution) ==3.4) {
							Container.get(i).Resolution="3.3";
						}
						if(Double.parseDouble(Container.get(i).Resolution) ==3.6) {
							Container.get(i).Resolution="3.5";
						}
						if(Double.parseDouble(Container.get(i).Resolution) ==3.8) {
							Container.get(i).Resolution="3.7";
						}
						if(Double.parseDouble(Container.get(i).Resolution) == 4) {
							Container.get(i).Resolution="3.9";
						}
						CSV+=Container.get(i).Resolution+","; 
						CSV+=Container.get(i).R_factor0Cycle+",";  
						CSV+=Container.get(i).R_free0Cycle+",";  
						CSV+=Container.get(i).F_mapCorrelation+","; 
						CSV+=Container.get(i).E_mapCorrelation+"\n";
						
					}
					
			        
				}
			}
		}  
		FileUtils.deleteQuietly(new File(PathForDM+"/SPSSDatasetBins.csv")); // Removing previous csv if any 
		  CSV=new ComparisonMeasures().FormatingPipelinesNames(CSV,false);
     	new Preparer().WriteTxtFile(PathForDM+"/SPSSDatasetBins.csv", CSV);

	}

}
