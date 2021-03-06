package Comparison.Validation;
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

import Comparison.Analyser.ExcelContents;
import Comparison.Analyser.ExcelLoader;
import Comparison.Analyser.MultiThreadedAnalyser;

import java.math.BigDecimal;
public class Tester {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		  File[] Dataset = new File("").listFiles();// set a path for DM folders here 
		
		 
		 
		 
		 for (File folder : Dataset) {
			 
			 if(folder.isDirectory()) {
				 System.out.println("Dataset "+folder.getName());
				 for(File Excel : folder.listFiles()) {
					 
					 System.out.println("Excel "+Excel.getName());
					 ExcelLoader e = new ExcelLoader();
					 
					
					//This an example on how to run the tests
					System.out.println("------- RFactor None Checking Test -------");
					System.out.println(new Tester().CheckingRNoneCol(e.ReadExcel(Excel.getAbsolutePath()))? "Pass" :  "Fail");
					
					System.out.println("------- Seq None Checking Test -------");
					
					System.out.println( new Tester().CheckingSeqNoneCol(e.ReadExcel(Excel.getAbsolutePath()))? "Pass" : "Fail");
					
					
				 
				 }
			 }
		 }
		 
		
	}

	
	boolean RFactorAnd0CycleRFactor(Vector<ExcelContents> Pipeline) {
		boolean Matched=true;
		int NotMatched=0;
		int NumberOfModelsCount=0;
		for(ExcelContents DC : Pipeline) {
			if(!DC.R_factor0Cycle.equals("None") && !DC.R_factor.equals("None")) {
				NumberOfModelsCount++;
			if( new BigDecimal(DC.R_factor).subtract(new BigDecimal(DC.R_factor0Cycle) ).compareTo(new BigDecimal("0.02")) > 0 ||  new BigDecimal(DC.R_free).subtract(new BigDecimal(DC.R_free0Cycle) ).compareTo(new BigDecimal("0.02")) > 0 ) {
				Matched=false;
				
				NotMatched++;
				
				
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
			String LogContent=new MultiThreadedAnalyser().readFileAsString(log.getAbsolutePath());
			if(!LogContent.contains("Cycle 49")) {
				Passed=false;
				++incompletelogs;
				System.out.println(log.getName());
			}
		}
		System.out.println("incomplete logs "+incompletelogs);
		return Passed;
		
	}
	
	boolean CheckingRNoneCol(Vector<ExcelContents> Pipeline) {
		boolean NoNone=true;
		
		for(ExcelContents DC : Pipeline) {
			if(DC.BuiltPDB.equals("T")&&DC.R_factor0Cycle.equals("None"))
				NoNone=false;
		}
		return NoNone;
	}
	boolean CheckingSeqNoneCol(Vector<ExcelContents> Pipeline) {
		boolean NoNone=true;
		
		for(ExcelContents DC : Pipeline) {
			if(DC.BuiltPDB.equals("T")&& DC.n1m2.equals("None"))
				NoNone=false;
		}
		return NoNone;
	}
	boolean DuplicateRecord(Vector<ExcelContents> Pipeline) {
		boolean NoDuplicated=true;
		
		for(ExcelContents DC : Pipeline) {
			int Record=0;
			for(ExcelContents DC2 : Pipeline) {
			if(DC.PDB_ID.equals(DC2.PDB_ID))
				Record++;
				
			}
			if(Record>1)
				NoDuplicated=false;
		}
		return NoDuplicated;
	}
	void NumberOfRecords(Vector<ExcelContents> Pipeline) {
		System.out.println(Pipeline.size());
	}
	
 boolean SeqInDepoistedModelNotLessThanSeqInBuiltModel(Vector<ExcelContents> Pipeline) {
		boolean NoNone=true;
		int count=0;
		for(ExcelContents DC : Pipeline) {
			if(DC.BuiltPDB.equals("T")&& Integer.parseInt(DC.n1m2) != Integer.parseInt(DC.n2m1)) {
				NoNone=false;
			count++;}
		}
		System.out.println(count);
		return NoNone;
	}
 
 boolean ComparingTwoPipeline(Vector<ExcelContents> Pipeline1,Vector<ExcelContents> Pipeline2) {
		boolean Matched=true;
		int CountMismatchedRecords=0;
		for(ExcelContents DC : Pipeline1) {
			for(ExcelContents DC2 : Pipeline2) {
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
			String LogContent=new MultiThreadedAnalyser().readFileAsString(log.getAbsolutePath());
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
