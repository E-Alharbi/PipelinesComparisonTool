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
import java.util.Stack;
import java.util.Vector;

import Comparison.Analyser.ExcelContents;
import Comparison.Analyser.ExcelLoader;
import Comparison.Analyser.ExcelSheet;
import Comparison.Analyser.MultiThreadedAnalyser;
import Comparison.Runner.RunningPram;
import Comparison.Utilities.DataSetChecking;

public class MolProbity implements Runnable {
	static Stack<ExcelContents> OldContainer =  new Stack<>();;
	static Vector<ExcelContents> NewContainer = new Vector<ExcelContents>();
	public synchronized void AddRowToContainer(ExcelContents C ) {
		NewContainer.addElement(C);
	}
	public synchronized ExcelContents GetFile() {
		
		System.out.println(" #### Unanalysed files:  "+ OldContainer.size());
		return OldContainer.pop();
	}
	
	public static void RunMol() throws FileNotFoundException, IOException {
		
	  	int NumberpfThreads= Integer.valueOf(RunningPram.NumberofThreads);
			Vector <ExcelContents> temp = new ExcelLoader().ReadExcel(RunningPram.ExcellPath);
			for(int i=0 ; i < temp.size() ; ++i) {
				OldContainer.push(temp.get(i));
			}
	
			 
			
		     
		     int ThreadNumber=0;
		     while (OldContainer.size()!=0) {
		    	 if(Thread.activeCount() < NumberpfThreads ) {
		    		 Thread t1 = new Thread(new MolProbity(), "Thread "+String.valueOf(ThreadNumber));
		    	     
		    	     t1.start();
		    	     ++ThreadNumber;
		    	    
		    	 }
		     }
		     while (OldContainer.size()==0) {
		     	 if(Thread.activeCount() == 1 ) {
		     		 System.out.println("Creating The excel file");
		     		CreateExcel();
		     		break;
		     	 }
		     }
		}
		static public synchronized void  CreateExcel() throws FileNotFoundException, IOException {
			
			new ExcelSheet().FillInExcel(NewContainer, RunningPram.ToolName);
			
		}
		
	public MolProbityData molProbity(File PDB, File mtz) throws IOException {
		
		MolProbityData  Results= new MolProbityData();
		 String st = null;
		String[]callAndArgs= {
				 RunningPram.PhenixMolProbity,
				 PDB.getAbsolutePath(),mtz.getAbsolutePath()+":FP,SIGFP", 
				 
		 };
		 
		 Process p = Runtime.getRuntime().exec(callAndArgs);

			             

		BufferedReader stdInput = new BufferedReader(new 

			                  InputStreamReader(p.getInputStream()));



			             BufferedReader stdError = new BufferedReader(new 

			                  InputStreamReader(p.getErrorStream()));



			            
	boolean IsSummaryAppeared=false;
			             while ((st = stdInput.readLine()) != null) {
			            	 
			            	 if(IsSummaryAppeared==true) {
			            		 //System.out.println(st);
				            	 if(st.contains("Ramachandran outliers")) {
				            		 Results.RamachandranOutliers=st.split("=")[1];
				            	 }
				            	 if(st.contains("favored")) {
				            		 Results.RamachandranFavored=st.split("=")[1];
				            	 }
				            	 if(st.contains("Rotamer outliers")) {
				            		 Results.RotamerOutliers=st.split("=")[1];
				            	 }
				            	 if(st.contains("Clashscore")) {
				            		 Results.Clashscore=st.split("=")[1];
				            	 }
				            	 if(st.contains("RMS(bonds)")) {
				            		 Results.RMSBonds=st.split("=")[1];
				            	 }
				            	 if(st.contains("RMS(angles)")) {
				            		 Results.RMSAngles=st.split("=")[1];
				            	 }
				            	 if(st.contains("MolProbity score")) {
				            		 Results.MolProbityScore=st.split("=")[1];
				            	 }
				            	 if(st.contains("R-work")) {
				            		 Results.RWork=st.split("=")[1];
				            	 }
				            	 if(st.contains("R-free")) {
				            		 Results.RFree=st.split("=")[1];
				            	 }
				            	 if(st.contains("Refinement program")) {
				            		 Results.RefinementProgram=st.split("=")[1];
				            	 }
			            	 }
			            	 if(st.contains("Summary"))
			            		 IsSummaryAppeared=true;
			             }
			             while ((st = stdError.readLine()) != null) {

			                 System.out.println(st);

			             }
			           
			     		
			             return Results;
			             }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	//System.out.println(Thread.currentThread().getName() + " START");
/*
	try {
		if(!OldContainer.isEmpty())
			molProbity(GetFile());
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	*/
	}
	

}

