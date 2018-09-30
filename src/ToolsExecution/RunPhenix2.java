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
import java.util.Collection;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Analyser.ExcelSheet;
import Analyser.PipelineLog;
import NotUsed.ARPResultsAnalysis;
import Run.RunComparison;
import Run.RunningPram;

public class RunPhenix2 {
	static String JobDirectory="";// to use for IntermediateResults
	static String PDBID="";
	static boolean FinshedBuilding=false;
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		
		if(args.length<2){
			System.out.println("Error: No Inputs! ");
			System.out.println("You have to set the path for your data folder and Phenix.autobuild");
			System.exit(-1);
		}
		
		RunningPram.DataPath=args[0];
		RunningPram.PhenixAutobuild=args[1];
	
		new RunPhenix2().RunPhenixTool();
	}
	boolean SaveIntermediateResults() throws InterruptedException, IOException {
		if(!JobDirectory.equals("")) {
		
			if(new File(JobDirectory+"/overall_best.pdb").exists()) {
      		FileUtils.copyFile(new File(JobDirectory+"/overall_best.pdb"),  new File("./PhenixResults/IntermediatePDBs/"+PDBID+".pdb"));
      		
      		 Collection<File> Logs = FileUtils.listFiles(
    				 new File(JobDirectory), 
    				 new RegexFileFilter("^AutoBuild_run_.*"), 
   				  FileFileFilter.FILE
    				);
    		 Vector <File>LogsFiles = new   Vector <File>();
    		 LogsFiles.addAll(Logs);
    		 String LogTxt="";
    		 for(int i=0 ; i < LogsFiles.size() ;++i) {
    			 LogTxt+= new ARPResultsAnalysis().readFileAsString(LogsFiles.get(i).getAbsolutePath())+"\n";  
    		 }
      		//FileUtils.copyFile(new File(LogsFiles.get(0).getAbsolutePath()),  new File("./PhenixResults/IntermediateLogs/"+PDBID+".txt"));
    		 try(  PrintWriter out = new PrintWriter( "./PhenixResults/IntermediateLogs/"+PDBID+".txt" )  ){
 			    out.println( LogTxt );
 			}
			
			}
		}
		
		//Thread.sleep(1000);
		//if(FinshedBuilding ==false)
		//return SaveIntermediateResults();
		
		return true;
	}
	public void RunPhenixTool() throws IOException
	{
		
		Timer t = new Timer();

		t.scheduleAtFixedRate(
		    new TimerTask()
		    {
		        public void run()
		        {
		            
		            try {
		            	if(FinshedBuilding ==false)
					SaveIntermediateResults();
		            	else
		            	t.cancel();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
		    },
		    0,1000); 
		
		/*
		Runnable r = new Runnable() {
	         public void run() {
	        	 try {
					SaveIntermediateResults();
				} catch (InterruptedException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	         }
	     };
	     new Thread(r).start();
	     */
		//Runtime runtime = Runtime.getRuntime();
		long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		String PATHLogs = "./PhenixResults/PhinexLogs";// LogsFilesPath
		new RunComparison().CheckDirAndFile("PhenixResults");
		new RunComparison().CheckDirAndFile(PATHLogs);
		new RunComparison().CheckDirAndFile("./PhenixResults/PDBs");
		new RunComparison().CheckDirAndFile("./PhenixResults/WorkingDir");
		new RunComparison().CheckDirAndFile("./PhenixResults/IntermediateLogs");    
		new RunComparison().CheckDirAndFile("./PhenixResults/IntermediatePDBs");
		 Vector<String> FilesNames= new Vector <String>();
		File[] processedfiles = new File(PATHLogs).listFiles();
			 for (File file : processedfiles) {
				 FilesNames.add(file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),""));
				 System.out.println(file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),""));
			 }
		 
		
		
	
   
    
	File[] files = new File(RunningPram.DataPath).listFiles();
    FilesNames=AddFileNameToList(FilesNames);
		 for (File file : files) {
			 String CaseName=file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),"");
	if(!FilesNames.contains(CaseName)){
		System.out.print("File Name "+CaseName);
		WriteFileNameToList(CaseName);
    String FileName=file.getParentFile()+"/"+CaseName;
	
	FilesNames.add(CaseName);
   System.out.println(CaseName);
   PipelineLog res= new RunPhenix2().Run(FileName,CaseName);
   
   
   long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
   long actualMemUsed=afterUsedMem-beforeUsedMem;
   System.out.println("Used memory is bytes: " + actualMemUsed);
   res.LogFile+="Used memory is bytes: "+actualMemUsed;
   
	
   File f = new File(PATHLogs+"/"+CaseName+".txt");
		 if(f.exists() && !f.isDirectory()) { 
		     System.out.println("Exists");
		     Date CurrentDate = new java.util.Date();
		     try(PrintWriter out = new PrintWriter( PATHLogs+"/"+CaseName+".txt"+CurrentDate.toString() )  ){
		 	    out.println( res.LogFile );
		 	}
		 }
		 else {
			 System.out.println("NotExists");
			 try(  PrintWriter out = new PrintWriter( PATHLogs+"/"+CaseName+".txt" )  ){
				    out.println( res.LogFile );
				}
		 }	
	
   
		// FinshedBuilding =true;
	FilesNames=AddFileNameToList(FilesNames);
	break;// to be sure each model takes the whole seven days 
	}
	
}
		
		 FinshedBuilding =true;
	}

	PipelineLog Run(String FilePathAndName,String FileName){
		  
		  PDBID=FileName;
		 String st = null;
		 Date ProStartTime = new java.util.Date();
		 PipelineLog res= new PipelineLog();
		 
		         try {
	
String mtzin=FilePathAndName+".mtz";
String seqin=FilePathAndName+".seq";
//String[]callAndArgss= {"source","/Applications/ccp4-7.0/setup-scripts/ccp4.setup-sh"};
//Process pp = Runtime.getRuntime().exec(callAndArgss);

//"/Applications/phenix-1.12-2829/build/bin/phenix.autobuild"
//String labels=" FP SIGFP PHIB FOM parrot.ABCD.A parrot.ABCD.B parrot.ABCD.C parrot.ABCD.D FreeR_flag";

	 String[]callAndArgs= {
			 RunningPram.PhenixAutobuild,
	"data=",mtzin,
	"seq_file=",seqin,
	//"input_labels="," FP SIGFP PHIB FOM HLA HLB HLC HLD"
	"input_labels=","FP SIGFP hltofom.Phi_fom.phi hltofom.Phi_fom.fom parrot.ABCD.A parrot.ABCD.B parrot.ABCD.C parrot.ABCD.D FreeR_flag",
	"clean_up=","True"
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
	                	 JobDirectory=Working_directory;
	                	 System.out.println("-Working_directory "+Working_directory);
	                	 WriteWorkingDirToTxtFile(FileName,Working_directory);
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
		            
	         		res.Roslution=Resolution;
	         		res.Rfactor=Rfactor;
	         		res.TimeTaking=TimeTaking;
	         		res.ProcessStatus="Success";
	         		
	         		File f = new File("./PhenixResults/PDBs/"+FileName+".pdb");
	       		 if(f.exists() && !f.isDirectory()) { 
	       		     System.out.println("Exists");
	       		  FileUtils.copyFile(new File(Working_directory+"/overall_best.pdb"),  new File("./PhenixResults/PDBs/"+FileName+".pdb"+new java.util.Date().toString()));

		         	//FileUtils.copyDirectoryToDirectory(new File(Working_directory),new File( System.getProperty("user.dir")+"/PhenixResults/WorkingDir/"+FileName));
		       		//FileUtils.deleteDirectory(new File(System.getProperty("user.dir")+"/"+Working_directory));
		       	
	       		 }
	       		 else {
	       			 System.out.println("NotExists");
	       			FileUtils.copyFile(new File(Working_directory+"/overall_best.pdb"),  new File("./PhenixResults/PDBs/"+FileName+".pdb"));

		         	//FileUtils.copyDirectoryToDirectory(new File(Working_directory),new File( System.getProperty("user.dir")+"/PhenixResults/WorkingDir/"+FileName));
		       		//FileUtils.deleteDirectory(new File(System.getProperty("user.dir")+"/"+Working_directory));
		       	
	       		 }		
	         		
	         	
	         		
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
	
	static Vector<String> AddFileNameToList( Vector<String> FilesNames) throws IOException{
		File yourFile = new File("./ProcessedFilesNamesPhenix.txt");
		yourFile.createNewFile();
		 String FileNamesTxt=new ARPResultsAnalysis().readFileAsString("./ProcessedFilesNamesPhenix.txt");
		 FilesNames.addAll(Arrays.asList(FileNamesTxt.split("\n")));
		 return FilesNames;
		
	}
	static void WriteFileNameToList(String Name) throws IOException{
		File yourFile = new File("./ProcessedFilesNamesPhenix.txt");
		yourFile.createNewFile();
		 BufferedWriter output;
		 output = new BufferedWriter(new FileWriter("./ProcessedFilesNamesPhenix.txt", true));
		 output.append(Name+"\n");
		 output.close();
	}
	static void WriteWorkingDirToTxtFile(String Name, String WorkingDir) throws IOException{
		File yourFile = new File("./WorkingDir.txt");
		yourFile.createNewFile();
		 BufferedWriter output;
		 output = new BufferedWriter(new FileWriter("./WorkingDir.txt", true));
		 output.append(Name+"\n");
		 output.append(WorkingDir+"\n");
		 output.close();
	}
}
