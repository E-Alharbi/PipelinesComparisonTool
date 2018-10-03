package ToolsExecation.SingleThread;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Stack;
import java.util.Vector;

import Analyser.DataContainer;
import Analyser.ExcelSheet;
import Analyser.LoadExcel;
import Analyser.ResultsAnalyserMultiThreads;
import Run.RunningPram;
import Utilities.DataSetChecking;

public class MolProbity implements Runnable {
	static Stack<DataContainer> OldContainer =  new Stack<>();;
	static Vector<DataContainer> NewContainer = new Vector<DataContainer>();
	public synchronized void AddRowToContainer(DataContainer C ) {
		NewContainer.addElement(C);
	}
	public synchronized DataContainer GetFile() {
		
		System.out.println(" #### Unanalysed files:  "+ OldContainer.size());
		return OldContainer.pop();
	}
	public static void main(String[] args) throws IOException {
		/*
		// TODO Auto-generated method stub
		Vector<DataContainer> Container = new LoadExcel().ReadExcel("/Volumes/PhDHardDrive/jcsg1200Results/mancs/MancsExcelSheets/Buccaneeri1.xlsx");
		RunningPram.PhenixMolProbity="/Applications/phenix-1.12-2829/build/bin/phenix.molprobity";
		String PDBs="/Volumes/PhDHardDrive/jcsg1200Results/mancs/Bucci1/PDBs/";
		String MTZ="/Volumes/PhDHardDrive/jcsg1200/mrncs/";
		Vector<DataContainer> NewContainer = new Vector<DataContainer>();
		for(int i=0 ; i < Container.size() ; ++i) {
			System.out.println("i "+i);
		MolProbityData  Results=	new MolProbity().RunMolProbity(PDBs+Container.get(i).PDB_ID+".pdb", MTZ+Container.get(i).PDB_ID+".mtz:FP,SIGFP");
		System.out.println(Results.RamachandranOutliers);
		System.out.println(Results.RamachandranFavored);
		System.out.println(Results.RotamerOutliers);
		System.out.println(Results.Clashscore);
		System.out.println(Results.RMSBonds);
		System.out.println(Results.RMSAngles);
		System.out.println(Results.MolProbityScore);
		System.out.println(Results.RWork);
		System.out.println(Results.RFree);
		System.out.println(Results.RefinementProgram);
		DataContainer d = Container.get(i);
		d.molProbityData=Results;
		NewContainer.add(d);
		}
		RunningPram.ToolName="Buccaneer";
		new ExcelSheet().FillInExcel(NewContainer, RunningPram.ToolName);
		*/
	}
	public static void RunMol() throws FileNotFoundException, IOException {
		
	  	int NumberpfThreads= Integer.valueOf(RunningPram.NumberofThreads);
			Vector <DataContainer> temp = new LoadExcel().ReadExcel(RunningPram.ExcellPath);
			for(int i=0 ; i < temp.size() ; ++i) {
				OldContainer.push(temp.get(i));
			}
	
			 
			
		     //System.out.println(Thread.activeCount()); 
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
		//System.out.println( RunningPram.PhenixMolProbity);
		//System.out.println(RunningPram.PDBs+ D.PDB_ID+".pdb");
		//System.out.println(RunningPram.DataPath+D.PDB_ID+".mtz:FP,SIGFP");
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
			           
			     		//D.molProbityData=Results;
			     		//NewContainer.add(D);
			     		//CreateExcel();
			     	//System.out.println(Results.RamachandranOutliers);
			    		//System.out.println(Results.RamachandranFavored);
			    		//System.out.println(Results.RotamerOutliers);
			    		//System.out.println(Results.Clashscore);
			    		//System.out.println(Results.RMSBonds);
			    		//System.out.println(Results.RMSAngles);
			    		//System.out.println(Results.MolProbityScore);
			    		//System.out.println(Results.RWork);
			    		//System.out.println(Results.RFree);
			    		//System.out.println(Results.RefinementProgram);
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

