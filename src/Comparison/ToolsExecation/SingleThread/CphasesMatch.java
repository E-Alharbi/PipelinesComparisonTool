package Comparison.ToolsExecation.SingleThread;
/**
*
* @author Emad Alharbi
* University of York
*/
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

import Comparison.Analyser.ExcelSheet;
import Comparison.Runner.RunningPram;

public class CphasesMatch {

	
	public String [] cphasesmatch(String FilePathAndName, String CphasesMatchScriptPath){
		
		 String st = null;
		 String F_mapCorrelation="";
		 String E_mapCorrelation="";
		         try {
	String mtzin=FilePathAndName+".mtz";
	String colinhl=RunningPram.PhasesUsedCPhasesMatch;
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
		              
		                	 int countElement=0;
		                	String CuurentValue="";
		                	 for(int i=0 ; i < st.length(); ++i){
		                		
		                		 if(st.charAt(i)!=' '){
		                			 CuurentValue+= st.charAt(i);
		                			
		                		 }
		                		 else{
		                			if(!CuurentValue.trim().equals(""))
		                			 countElement++;
		                			 if(countElement==7 && F_mapCorrelation.equals("")) {
		                				 F_mapCorrelation= CuurentValue;
		                				
		                			 }
		                			 if(countElement==8 && E_mapCorrelation.equals("")) {
		                				 E_mapCorrelation= CuurentValue;
		                				
		                			 }
		                				
		                			 CuurentValue="";
		                		 }
		                	 }
		            	 }
	                 if(st.contains("Qfom1")){
	                	 
	                	
	                	
	                	 IsItReadTheLineBefore=true;
	                 }
	               
	                
		             }

		           
		             while ((st = stdError.readLine()) != null) {

		                 System.out.println("CphasesMatch Error "+st);

		             }

		            

		         }

		         catch (IOException e) {

		             

		             e.printStackTrace();

		             
		         }
		         String [] FandE= new String [] {F_mapCorrelation,E_mapCorrelation};
		 return FandE;
	}
}
