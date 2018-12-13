package Analyser;

import java.io.File;
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

import Run.Preparer;

public class BinsCreater {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String PathForDM="/Users/emadalharbi/Desktop/Arp/ExFaliedCases/ExcludedBuccaneerDevSet";
	File [] ExcelPath=new File(PathForDM).listFiles();
		String CSV="DM,Pipeline,PDB,Resolution,Completness,bin\n";
		for(File Folder : ExcelPath) {
			if(Folder.isDirectory()) {
				System.out.println(Folder.getName());
				
				for(File Excel:Folder.listFiles()) {
					System.out.println(Excel.getAbsolutePath());
					
					ExcelLoader e = new ExcelLoader();
					Vector<DataContainer> Container = e.ReadExcel(Excel.getAbsolutePath());
					HashMap<Double,Double> Reso=new HashMap<Double,Double>();  
					HashMap<Double,Integer> ResoCounting=new HashMap<Double,Integer>();  
					for(int i=0 ; i < Container.size() ; ++i) {
						
						//if(Double.parseDouble(Container.get(i).Resolution) <= 3) {
						//	Container.get(i).Resolution="3.0";
						//}
						CSV+=Folder.getName()+",";
						CSV+=Excel.getName()+",";
						CSV+=Container.get(i).PDB_ID+",";  
						CSV+=Container.get(i).Resolution+","; 
						CSV+=Container.get(i).Completeness+","; 
						if(Double.parseDouble(Container.get(i).Resolution) <=2) {
							Container.get(i).Resolution="2.0";
						}
						if(Double.parseDouble(Container.get(i).Resolution) <=3) {
							Container.get(i).Resolution="3.0";
						}
						if(Double.parseDouble(Container.get(i).Resolution) ==3.2) {
							Container.get(i).Resolution="3.1";
						}
						if(Double.parseDouble(Container.get(i).Resolution) ==3.4) {
							Container.get(i).Resolution="3.3";
						}
						if(Double.parseDouble(Container.get(i).Resolution) ==3.6) {
							Container.get(i).Resolution="3.4";
						}
						if(Double.parseDouble(Container.get(i).Resolution) ==3.8) {
							Container.get(i).Resolution="3.7";
						}
						if(Double.parseDouble(Container.get(i).Resolution) == 4) {
							Container.get(i).Resolution="3.9";
						}
						CSV+=Container.get(i).Resolution+"\n"; 
						/*
						if(!Reso.containsKey(Double.parseDouble(Container.get(i).Resolution))){
							//Reso.put(Double.parseDouble(Container.get(i).Resolution), new BigDecimal(Container.get(i).Completeness).setScale(0, RoundingMode.HALF_UP));
							Reso.put(Double.parseDouble(Container.get(i).Resolution), Double.parseDouble(Container.get(i).Resolution));
							ResoCounting.put(Double.parseDouble(Container.get(i).Resolution), 1);

						}
						else {
							Reso.put(Double.parseDouble(Container.get(i).Resolution), Reso.get(Double.parseDouble(Container.get(i).Resolution)).add(new BigDecimal(Container.get(i).Completeness).setScale(0, RoundingMode.HALF_UP)) );
							ResoCounting.put(Double.parseDouble(Container.get(i).Resolution), ResoCounting.get(Double.parseDouble(Container.get(i).Resolution))+1);
						}
						*/
					}
					/*
					 Map<Double,BigDecimal> map = new TreeMap<Double,Double>(Reso); 
			         System.out.println("After Sorting:");
			         Set set2 = map.entrySet();
			         Iterator iterator2 = set2.iterator();
			         while(iterator2.hasNext()) {
			              Map.Entry me2 = (Map.Entry)iterator2.next();
			              System.out.print("Reso "+ me2.getKey() + ": ");
			              System.out.print(Math.round(Double.parseDouble(me2.getValue().toString()) / ResoCounting.get(me2.getKey())));
			              System.out.println(" N "+ ResoCounting.get(me2.getKey()) + ": ");
			              CSV+=Folder.getName()+",";
						   CSV+=Excel.getName()+",";
						   CSV+=me2.getKey()+",";
			              CSV+=Math.round(Double.parseDouble(me2.getValue().toString()) / ResoCounting.get(me2.getKey()));
			              CSV+="\n";
 
			         }
			        */
			        
				}
			}
		}  
		FileUtils.deleteQuietly(new File(PathForDM+"/SPSSDatasetBins.csv")); // Removing previous csv if any 
		  CSV=new ResultsInLatex().FormatingPipelinesNames(CSV,false);
     	new Preparer().WriteTxtFile(PathForDM+"/SPSSDatasetBins.csv", CSV);

	}

}
