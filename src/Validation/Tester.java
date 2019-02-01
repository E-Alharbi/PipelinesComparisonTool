package Validation;
/**
*
* @author Emad Alharbi
* University of York
*/
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Analyser.DataContainer;
import Analyser.ExcelLoader;
import Analyser.ResultsAnalyserMultiThreads;

import java.math.BigDecimal;
public class Tester {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		  File[] Dataset = new File("/Volumes/PhDHardDrive/jcsg1200Results/Fasta/VikingRun3ArpNoFree").listFiles();
		
		 // File[] Dataset2 = new File("/Volumes/PhDHardDrive/jcsg1200Results/Fasta/Run8/All").listFiles();
		 /*
 for (File folder : Dataset) {
	 ExcelLoader e = new ExcelLoader();
			 if(folder.isDirectory()) {
				 for(File Excel : folder.listFiles()) {
					 for (File folder2 : Dataset2) {
						 if(folder.getName().equals(folder2.getName())) {
							 for(File Excel2 : folder2.listFiles()) {
								 if(Excel.getName().equals(Excel2.getName())) {
									 System.out.println("Dataset 1 "+folder.getName()+ "Excel "+Excel.getName());
									 System.out.println("Dataset 2 "+folder2.getName()+ "Excel2 "+Excel2.getName());
									 System.out.println( new Tester().ComparingTwoPipeline(e.ReadExcel(Excel.getAbsolutePath()),e.ReadExcel(Excel2.getAbsolutePath()))? "Pass" : "Fail"); 
								 }
							 }
						 }
					 } 
				 }
				
			 }
 }
 */
		 for (File folder : Dataset) {
			 
			 if(folder.isDirectory()) {
				 System.out.println("Dataset "+folder.getName());
				 for(File Excel : folder.listFiles()) {
					 
					 System.out.println("Excel "+Excel.getName());
					 ExcelLoader e = new ExcelLoader();
					 
					// System.out.println("------- Number of Records Test -------");
					// new Tester().NumberOfRecords(e.ReadExcel(Excel.getAbsolutePath()));
					
					// System.out.println("------- Duplicate Records Test -------");
					// System.out.println(new Tester().DuplicateRecord(e.ReadExcel(Excel.getAbsolutePath()))? "Pass" : "Fail");
					 
					// System.out.println("------- RFactor Checking Test -------");
					 
					 //System.out.println(new Tester().RFactorAnd0CycleRFactor(e.ReadExcel(Excel.getAbsolutePath()))? "Pass" : "Fail");
					 
					System.out.println("------- RFactor None Checking Test -------");
					System.out.println(new Tester().CheckingRNoneCol(e.ReadExcel(Excel.getAbsolutePath()))? "Pass" :  "Fail");
					
					System.out.println("------- Seq None Checking Test -------");
					
					System.out.println( new Tester().CheckingSeqNoneCol(e.ReadExcel(Excel.getAbsolutePath()))? "Pass" : "Fail");
					//System.out.println( new Tester().DuplicateRecord(e.ReadExcel(Excel.getAbsolutePath())));
					
                    // System.out.println("------- Seq In Depoisted Model Not Less Than Seq In Built Model Test -------");
					
					//System.out.println( new Tester().SeqInDepoistedModelNotLessThanSeqInBuiltModel(e.ReadExcel(Excel.getAbsolutePath()))? "Pass" : "Fail");
					
				 
				 }
			 }
		 }
		 /*
		  Dataset = new File("/Volumes/PhDHardDrive/jcsg1200Results/Results23:10:2018Fasta/hancs").listFiles();
		System.out.println("Particular Pipeliens Tests ");
		 for (File folder : Dataset) {
			 if(folder.isDirectory()&&folder.getName().contains("Arp") && !folder.getName().contains("PDB")) {
				System.out.println(folder.getAbsolutePath());
				 System.out.println(new Tester().ArpLogs(folder.getAbsolutePath()));
			 }
			 if(!folder.getName().contains("Arp") &&folder.isDirectory()&&folder.getName().contains("Buccaneer") && !folder.getName().contains("PDB")) {
					System.out.println(folder.getAbsolutePath());
					 System.out.println(new Tester().Bucc5CLogs(folder.getAbsolutePath()));
				 }
			 
		 }
		 */
	}

	
	boolean RFactorAnd0CycleRFactor(Vector<DataContainer> Pipeline) {
		boolean Matched=true;
		int NotMatched=0;
		int NumberOfModelsCount=0;
		for(DataContainer DC : Pipeline) {
			if(!DC.R_factor0Cycle.equals("None") && !DC.R_factor.equals("None")) {
				NumberOfModelsCount++;
			if( new BigDecimal(DC.R_factor).subtract(new BigDecimal(DC.R_factor0Cycle) ).compareTo(new BigDecimal("0.02")) > 0 ||  new BigDecimal(DC.R_free).subtract(new BigDecimal(DC.R_free0Cycle) ).compareTo(new BigDecimal("0.02")) > 0 ) {
				Matched=false;
				
				NotMatched++;
				// BigDecimal Rfree = new BigDecimal(DC.R_free);
			   //   BigDecimal RFree0cycle = new BigDecimal(DC.R_free0Cycle) ;
			     // System.out.println(Rfree.subtract(RFree0cycle));
			     // System.out.println(new BigDecimal(DC.R_free).subtract(new BigDecimal(DC.R_free0Cycle) ).compareTo(new BigDecimal("0.02")));
				
				//System.out.println("PDB ID "+ DC.PDB_ID +" RFactor " +Double.valueOf(DC.R_factor) +" RFree " + Double.valueOf(DC.R_free)  +" RFactor0Cycle " + Double.valueOf(DC.R_factor0Cycle) +" RFree0Cycle " + Double.valueOf(DC.R_free0Cycle));
				
			}
			}
		}
		if(NotMatched!=0)
		System.out.println(((NotMatched*100)/NumberOfModelsCount ) +"% Not matched of the models");
		return Matched;
	}
	
	boolean ArpLogs(String LogsPath) throws IOException {
		
		File[] Logs= new File(LogsPath).listFiles();
		boolean Passed=true;
		int incompletelogs=0;
		for(File log :Logs) {
			String LogContent=new ResultsAnalyserMultiThreads().readFileAsString(log.getAbsolutePath());
			if(!LogContent.contains("Cycle 49")) {
				Passed=false;
				++incompletelogs;
				System.out.println(log.getName());
			}
		}
		System.out.println("incomplete logs "+incompletelogs);
		return Passed;
		
	}
	
	boolean CheckingRNoneCol(Vector<DataContainer> Pipeline) {
		boolean NoNone=true;
		
		for(DataContainer DC : Pipeline) {
			if(DC.BuiltPDB.equals("T")&&DC.R_factor0Cycle.equals("None"))
				NoNone=false;
		}
		return NoNone;
	}
	boolean CheckingSeqNoneCol(Vector<DataContainer> Pipeline) {
		boolean NoNone=true;
		
		for(DataContainer DC : Pipeline) {
			if(DC.BuiltPDB.equals("T")&& DC.n1m2.equals("None"))
				NoNone=false;
		}
		return NoNone;
	}
	boolean DuplicateRecord(Vector<DataContainer> Pipeline) {
		boolean NoDuplicated=true;
		
		for(DataContainer DC : Pipeline) {
			int Record=0;
			for(DataContainer DC2 : Pipeline) {
			if(DC.PDB_ID.equals(DC2.PDB_ID))
				Record++;
				
			}
			if(Record>1)
				NoDuplicated=false;
		}
		return NoDuplicated;
	}
	void NumberOfRecords(Vector<DataContainer> Pipeline) {
		System.out.println(Pipeline.size());
	}
	
 boolean SeqInDepoistedModelNotLessThanSeqInBuiltModel(Vector<DataContainer> Pipeline) {
		boolean NoNone=true;
		int count=0;
		for(DataContainer DC : Pipeline) {
			if(DC.BuiltPDB.equals("T")&& Integer.parseInt(DC.n1m2) != Integer.parseInt(DC.n2m1)) {
				NoNone=false;
			count++;}
		}
		System.out.println(count);
		return NoNone;
	}
 
 boolean ComparingTwoPipeline(Vector<DataContainer> Pipeline1,Vector<DataContainer> Pipeline2) {
		boolean Matched=true;
		int CountMismatchedRecords=0;
		for(DataContainer DC : Pipeline1) {
			for(DataContainer DC2 : Pipeline2) {
				if(DC.PDB_ID.equals(DC2.PDB_ID)) {
					if(!DC.Completeness.equals(DC2.Completeness) || !DC.R_factor0Cycle.equals(DC2.R_factor0Cycle)) {
						Matched=false;
						System.out.println("Record 1 " +DC.PDB_ID);
						System.out.println("Record 2 " +DC2.PDB_ID);
						CountMismatchedRecords++;
					}
				}
			}
		}
		System.out.println(" Mismatched Records "+CountMismatchedRecords);
		return Matched;
	}
 
 boolean Bucc5CLogs(String LogsPath) throws IOException {
		
		File[] Logs= new File(LogsPath).listFiles();
		boolean Passed=true;
		int incompletelogs=0;
		for(File log :Logs) {
			String LogContent=new ResultsAnalyserMultiThreads().readFileAsString(log.getAbsolutePath());
			int i = 0;
			Pattern p = Pattern.compile("cbuccaneer");
			Matcher m = p.matcher( LogContent );
			while (m.find()) {
			    i++;
			}
			if(i/2==5 || i/2==25)
				Passed=false;
			else {
				incompletelogs++;
				System.out.println("log "+log.getName());
			}
		}
		
			
		System.out.println("incomplete logs "+incompletelogs);
		return Passed;
		
	}
 
 
 
}
