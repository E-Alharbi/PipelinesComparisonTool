package ToolsExecation.SingleThread;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Vector;

import Analyser.ExcelSheet;

public class CphasesMatch {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
/*
		 Vector<String> ColData=  ExcelSheet.ReadExcelByColIndex("/Users/emadalharbi/Desktop/PhDYork/BuccaneerResults/DataRunResults.xlsx",0); // to make sure the list of file names in  Excel in same order in all Excel files
		 Vector<String> ColDataAdded= new  Vector<String>();
		 
		 Vector<String> FilesNames= new Vector <String>();
	     File[] files = new File("/Users/emadalharbi/Desktop/PhDYork/jcsg-datasets-54/data").listFiles();
			
	     for(int i=0 ; i < ColData.size() ; ++i){
				for (File file : files) {
					// System.out.println(file.getName().substring(file.getName().indexOf(".")+1).trim());
					// System.out.println(ColData.get(i));
					if(ColData.get(i).equals(file.getName().substring(0,file.getName().indexOf(".")).trim()) && !ColDataAdded.contains(ColData.get(i))){
						 //System.out.println(ColData.get(i));
						String FileName=file.getParentFile()+"/"+file.getName().substring(0,file.getName().indexOf('.'));
						 new CphasesMatch().cphasesmatch(FileName,"/Applications/ccp4-7.0/share/python/CCP4Dispatchers/cphasematch.py");
						 ColDataAdded.add(ColData.get(i));
					}
				}
	     }
	     
	  
		for (File file : files) {
		if(!FilesNames.contains(file.getName().substring(0,file.getName().indexOf('.')))){
	    String FileName=file.getParentFile()+"/"+file.getName().substring(0,file.getName().indexOf('.'));
		
		FilesNames.add(file.getName().substring(0,file.getName().indexOf('.')));
	   //System.out.print(file.getName().substring(0,file.getName().indexOf('.')));
	    new CphasesMatch().cphasesmatch(FileName,"");
		
		
		
	  
		//try(  PrintWriter out = new PrintWriter( "./Logs/"+file.getName().substring(0,file.getName().indexOf('.'))+".txt" )  ){
		  //  out.println( res.LogFile );
		//}
		}
		
	}*/
		
	}
	public String [] cphasesmatch(String FilePathAndName, String CphasesMatchScriptPath){
		System.out.println("##cphasesmatch##");
		 String st = null;
		 String F_mapCorrelation="";
		 String E_mapCorrelation="";
		         try {
	String mtzin=FilePathAndName+".mtz";
	String colinhl="parrot.ABCD.A,parrot.ABCD.B,parrot.ABCD.C,parrot.ABCD.D";
	String colinfo="FP,SIGFP";
	String colinhl2="sfcalc.ABCD.A,sfcalc.ABCD.B,sfcalc.ABCD.C,sfcalc.ABCD.D";
	 
	 String[]callAndArgs= {
			 CphasesMatchScriptPath,
	"-mtzin",mtzin,
	"-colin-hl-1",colinhl,
	"-colin-fo",colinfo,
	"-colin-hl-2",colinhl2,
	};

	Process p = Runtime.getRuntime().exec(callAndArgs);

		             

	BufferedReader stdInput = new BufferedReader(new 

		                  InputStreamReader(p.getInputStream()));



		             BufferedReader stdError = new BufferedReader(new 

		                  InputStreamReader(p.getErrorStream()));



		             // read the output
	boolean IsItReadTheLineBefore=false;
	 
		             while ((st = stdInput.readLine()) != null) {
		            	
		            	
		            	 if(IsItReadTheLineBefore==true){
		            		 IsItReadTheLineBefore=false;
		               // 	System.out.println(st.substring(st.indexOf(":")+1).trim());
		                	 int countElement=0;
		                	String CuurentValue="";
		                	 for(int i=0 ; i < st.length(); ++i){
		                		
		                		 if(st.charAt(i)!=' '){
		                			 CuurentValue+= st.charAt(i);
		                			// F_mapCorrelation+= st.charAt(i);
		                			 //E_mapCorrelation+= st.charAt(i);
		                		 }
		                		 else{
		                			if(!CuurentValue.trim().equals(""))
		                			 countElement++;
		                			 if(countElement==7 && F_mapCorrelation.equals("")) {
		                				 F_mapCorrelation= CuurentValue;
		                				// System.out.println("F_mapCorrelation "+F_mapCorrelation);
		                			 }
		                			 if(countElement==8 && E_mapCorrelation.equals("")) {
		                				 E_mapCorrelation= CuurentValue;
		                				// System.out.println("E_mapCorrelation "+E_mapCorrelation);
		                			 }
		                				// break;
		                			 CuurentValue="";
		                		 }
		                	 }
		            	 }
	                 if(st.contains("Qfom1")){
	                	 
	                	// System.out.print(" ");
	                	
	                	 IsItReadTheLineBefore=true;
	                 }
	               
	                
		             }

		           
		             while ((st = stdError.readLine()) != null) {

		                 System.out.println("CphasesMatch Error "+st);

		             }

		            // System.out.println(F_mapCorrelation);

		            // System.exit(0);

		         }

		         catch (IOException e) {

		             System.out.println("exception occured");

		             e.printStackTrace();

		             //System.exit(-1);
		            
		            // return "exception occured ";
		         }
		         String [] FandE= new String [] {F_mapCorrelation,E_mapCorrelation};
		 return FandE;
	}
}
