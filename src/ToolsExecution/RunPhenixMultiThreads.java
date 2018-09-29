package ToolsExecution;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Stack;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Analyser.DataContainer;
import Analyser.ExcelSheet;
import Analyser.Results;
import Analyser.ResultsAnalyserMultiThreads;
import NotUsed.ARPResultsAnalysis;
import Run.RunComparison;
import Run.RunningPram;
import Utilities.DataSetChecking;

public class RunPhenixMultiThreads implements Runnable {

	static Stack<String> Files = new Stack<>();
	String PATHLogs = "./PhenixResults/PhinexLogs";// LogsFilesPath
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		
		if(args.length<2){
			System.out.println("Error: No Inputs! ");
			System.out.println("You have to set the path for your data folder and Phenix.autobuild");
			System.exit(-1);
		}
		
		RunningPram.DataPath=args[0];
		RunningPram.PhenixAutobuild=args[1];
		RunningPram.NumberofThreads=args[2];
		new RunPhenixMultiThreads().RunPhenixTool();
	}
public synchronized String GetFile() {
		
		System.out.println(" #### Unprocessed  files:  "+ Files.size());
		return Files.pop();
	}

	public void RunPhenixTool() throws IOException
	{
		
		new RunComparison().CheckDirAndFile("PhenixResults");
		new RunComparison().CheckDirAndFile(PATHLogs);
		new RunComparison().CheckDirAndFile("./PhenixResults/PDBs");
		new RunComparison().CheckDirAndFile("./PhenixResults/WorkingDir");
	
		 Vector<String> FilesNames= new Vector <String>();
		File[] processedfiles = new File(PATHLogs).listFiles();
			 for (File file : processedfiles) {
				 FilesNames.add(file.getName().substring(0,file.getName().indexOf('.')));
				 System.out.println(file.getName().substring(0,file.getName().indexOf('.')));
			 }
		 
			 RunPhenixMultiThreads Phenix = new RunPhenixMultiThreads();
				int NumberpfThreads= Integer.valueOf(RunningPram.NumberofThreads);
					
					
					 File[] files = new File(RunningPram.DataPath).listFiles();
					 for (File file : files) {
						 if(!Phenix.Files.contains(file.getName().substring(0,file.getName().indexOf('.'))))
						 Phenix.Files.push(file.getName().substring(0,file.getName().indexOf('.'))); 
						 
					 }
					 
					
				    
				     int ThreadNumber=0;
				     while (Files.size()!=0) {
				    	 if(Thread.activeCount() < NumberpfThreads ) {
				    		 Thread t1 = new Thread(new RunPhenixMultiThreads(), "Thread "+String.valueOf(ThreadNumber));
				    	     
				    	     t1.start();
				    	     ++ThreadNumber;
				    	    
				    	 }
				     }
		
	
		
	}

	Results Run(String FileName){
		
		System.out.println("####### Thread : "+ Thread.currentThread().getName() + " Processing " + FileName +" #####");
		
		String st = null;
		 Date ProStartTime = new java.util.Date();
		 Results res= new Results();
		 
		         try {
	
String mtzin=RunningPram.DataPath+"/"+FileName+".mtz";
String seqin=RunningPram.DataPath+"/"+FileName+".seq";
//String[]callAndArgss= {"source","/Applications/ccp4-7.0/setup-scripts/ccp4.setup-sh"};
//Process pp = Runtime.getRuntime().exec(callAndArgss);

//"/Applications/phenix-1.12-2829/build/bin/phenix.autobuild"
String labels=" FP SIGFP PHIB FOM parrot.ABCD.A parrot.ABCD.B parrot.ABCD.C parrot.ABCD.D FreeR_flag";

	 String[]callAndArgs= {
			 RunningPram.PhenixAutobuild,
	"data=",mtzin,
	"seq_file=",seqin,
	//"input_labels="," FP SIGFP PHIB FOM HLA HLB HLC HLD"
	//"input_labels="," FP SIGFP PHIB FOM parrot.ABCD.A parrot.ABCD.B parrot.ABCD.C parrot.ABCD.D FreeR_flag",
	"skip_xtriage=","True"
	};
	 
	 Process p = Runtime.getRuntime().exec(callAndArgs);

	BufferedReader stdInput = new BufferedReader(new 

		                  InputStreamReader(p.getInputStream()));



		             BufferedReader stdError = new BufferedReader(new 

		                  InputStreamReader(p.getErrorStream()));



		             // read the output
	String Resolution="";
	String Rfactor="";
	String  Working_directory="";
		             while ((st = stdInput.readLine()) != null) {
		            	 System.out.println(st);
		            	 res.LogFile+=st+"\n";
		            	 
	                 if(st.contains("Resolution range:")){
	                	
	                  Resolution=st;
		              
	                 }
	                 if(st.contains("After refmac, R =")){
		
	               Rfactor=st;
	   	              
	                    }
	                 if(st.contains("Working directory:") && Working_directory.equals("")){
	                	 Working_directory=st.split(":")[st.split(":").length-1].trim();
	                	 System.out.println("-Working_directory "+Working_directory);
	                 }
	                
		             }

		             
		            System.out.println("Resolution "+Resolution);
	                System.out.println("Rfactor "+Rfactor);
	                double difference = new java.util.Date().getTime() - ProStartTime.getTime(); 
	                DecimalFormat df = new DecimalFormat("#.##");
	                df.setRoundingMode(RoundingMode.HALF_DOWN);
	                String TimeTaking= df.format((difference/1000)/60);
	         		System.out.println("Time Taking "+TimeTaking ) ;
	         		res.LogFile+="TimeTaking "+TimeTaking+"\n";
		             // read any errors
	         		res.Roslution=Resolution;
	         		res.Rfactor=Rfactor;
	         		res.TimeTaking=TimeTaking;
	         		res.ProcessStatus="Success";
	         		
	         	FileUtils.copyFile(new File(Working_directory+"/overall_best.pdb"),  new File("./PhenixResults/PDBs/"+FileName+".pdb"));

	         	FileUtils.copyDirectoryToDirectory(new File(Working_directory),new File( System.getProperty("user.dir")+"/PhenixResults/WorkingDir/"+FileName));
	       		FileUtils.deleteDirectory(new File(System.getProperty("user.dir")+"/"+Working_directory));
	       	
	         		
		             while ((st = stdError.readLine()) != null) {

		                 System.out.println(st);

		             }

		             

		            // System.exit(0);

		         }

		         catch (IOException e) {

		             System.out.println("exception occured");

		             e.printStackTrace();

		             //System.exit(-1);
		             res.ProcessStatus="Failed";
		             return res;
		         }
		 return res;
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println(Thread.currentThread().getName() + " START");
		String FileName=GetFile();
		Results res= new RunPhenixMultiThreads().Run(FileName);
			
			try(  PrintWriter out = new PrintWriter( PATHLogs+"/"+FileName+".txt" )  ){
			    out.println( res.LogFile );
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
}
