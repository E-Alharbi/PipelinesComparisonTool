package Comparison.ToolsExecation.SingleThread;
/**
*
* @author Emad Alharbi
* University of York
*/
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Comparison.Analyser.Bucaneeri2LogGenerator;
import Comparison.Analyser.PipelineLog;
import Comparison.Runner.Preparer;
import Comparison.Runner.RunComparison;
import Comparison.Runner.RunningParameter;
import Comparison.Utilities.FilesUtilities;


public class Buccaneeri2 {

	static boolean FinshedBuilding=false;
	static Date StartTime;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		// TODO Auto-generated method stub

		
		
		if(args.length<1){
			System.out.println("Error: No Inputs! ");
			System.out.println("You have to set the path for your data folder");
			System.exit(-1);
		}
		
		RunningParameter.DataPath=args[0];
		new Buccaneeri2().RunBuccaneerTool();
	}
	String Log(String JobDirectory) throws IOException {
		 Collection<File> Logs = FileUtils.listFiles(
				 new File(JobDirectory), 
				  new RegexFileFilter("log.txt"), 
				  DirectoryFileFilter.DIRECTORY
				);
		 List list = new ArrayList(Logs);
		 Collections.sort(list);
		 
		 Vector <File>LogsFiles = new   Vector <File>();
		 
		 LogsFiles.addAll(list);
		 Collections.sort(LogsFiles, Bucaneeri2LogGenerator.SortingFiles);
		 String LogTxt="";
		 for(int i=0; i<LogsFiles.size();++i ) {
			 
			 LogTxt+=new FilesUtilities().readFileAsString(LogsFiles.get(i).getAbsolutePath())+"\n"; 
			
		 }
		 
		 return LogTxt;
	}
	boolean SaveIntermediateResults(String JobDirectory , String PDBID) throws InterruptedException, IOException {
		if(!JobDirectory.equals("")) {
		 File[] files = new File(JobDirectory).listFiles();
		 int JobNum=0;
		 File Job=new File("");
		 double difference = new java.util.Date().getTime() - StartTime.getTime(); 
         DecimalFormat df = new DecimalFormat("#.##");
         df.setRoundingMode(RoundingMode.HALF_DOWN);
         String TimeTaking= df.format((difference/1000)/60);
  		
  		
		 try(  PrintWriter out = new PrintWriter( "./BuccaneerResults/IntermediateLogs"+"/"+PDBID+".txt" )  ){
			    out.println( Log(JobDirectory) +"TimeTaking "+TimeTaking+"\n");
			}
		 
		 for (File file : files) {
			
			 if(file.getName().contains("job") && JobNum < Integer.parseInt(file.getName().replaceAll("[^0-9]", "")) ) {
				 JobNum = Integer.parseInt(file.getName().replaceAll("[^0-9]", ""));
				 Job=file;
				 
				 
				 if(new File(Job.getAbsolutePath()+"/XYZOUT.pdb").exists()) {
					 
		      		
						FileUtils.copyFile(new File(Job.getAbsolutePath()+"/XYZOUT.pdb"),  new File("./BuccaneerResults/IntermediatePDBs/"+PDBID+".pdb"));
					

				 }
			 }
		 }
		
		}
		
		
		return true;
	}
	
void timer(String JobDirectory , String PDBID,Timer t ) {
		
		
		t.scheduleAtFixedRate(
		    new TimerTask()
		    {
		        public void run()
		        {
		            
		            try {
		            
		            		SaveIntermediateResults(JobDirectory ,  PDBID );
		          
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
	}
	
	public void RunBuccaneerTool() throws IOException, InterruptedException
	{
		
		
		
		
		Buccaneeri2 RBM=new Buccaneeri2();
		String CCP4Dir=System.getenv("CCP4");
		String PATHLogs = "./BuccaneerResults/BuccaneerLogs";// LogsFilesPath
		new RunComparison().CheckDirAndFile("BuccaneerResults");
		new RunComparison().CheckDirAndFile("./BuccaneerResults/PDBs");
		new RunComparison().CheckDirAndFile(PATHLogs);
		new RunComparison().CheckDirAndFile("./BuccaneerResults/IntermediateLogs");    
		new RunComparison().CheckDirAndFile("./BuccaneerResults/IntermediatePDBs");   
		new RunComparison().CheckDirAndFile("ParametersUsed");
		 Vector<String> FilesNames= new Vector <String>();
		
		 File[] processedfiles = new File(PATHLogs).listFiles();
			
		 for (File file : processedfiles) { // Checking the models that already built by check the logs. if log  then is built 
			 FilesNames.add(file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),""));
		 }
		 
		 
		 File[] files=null ;
	     if(new File(RunningParameter.DataPath).isDirectory()) {
	    	 files = new File(RunningParameter.DataPath).listFiles();
	     }
		if(new File(RunningParameter.DataPath).isFile()) {
			
			files = ArrayUtils.add(files, new File(RunningParameter.DataPath));
		}
		
     FilesNames=RBM.AddFileNameToList(FilesNames);
     System.out.println("Data Path "+ RunningParameter.DataPath);
		 for (File file : files) {
	String CaseName=file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),"");
	if(!FilesNames.contains(CaseName)){
		
	RBM.WriteFileNameToList(CaseName,"./ProcessedFilesNamesBuccaneer.txt"); // Write the PDB ID that picked by a thread. 
    String FileName=file.getParentFile()+"/"+CaseName;
	
	FilesNames.add(CaseName);
  
   PipelineLog res= new Buccaneeri2().RunBuccaneer(FileName,CaseName);
  
   try(  PrintWriter out = new PrintWriter( PATHLogs+"/"+CaseName+".txt" )  ){
	    out.println(res.LogFile + "\n" +"TimeTaking "+ res.TimeTaking );
	}
	FilesNames=AddFileNameToList(FilesNames);
	FinshedBuilding=true;
	break;
	
	}
	
}
		
		
	}

	PipelineLog RunBuccaneer(String FilePathAndName,String FileName) throws InterruptedException{
		
		System.out.println(Thread.currentThread().getName()+" Proccessing "+FileName);
		Timer timer = new Timer();
		
		String st = null;
		 Date ProStartTime = new java.util.Date();
		 StartTime = new java.util.Date();
		 PipelineLog res= new PipelineLog();
		         try {
	
	String seqin="";
	if(new File(FilePathAndName+".fasta").exists())	        	 
	seqin=FilePathAndName+".fasta";
	if(new File(FilePathAndName+".fa").exists())	        	 
	seqin=FilePathAndName+".fa";
	if(new File(FilePathAndName+".seq").exists())	        	 
	seqin=FilePathAndName+".seq";
	
	String mtzin=FilePathAndName+".mtz";
	// adding water option is set in bucrefi2.py not here!!!
	 String[]callAndArgs= {
			 "ccp4-python",RunningParameter.Buccaneeri2PipeLine,
			 "--mtzin",mtzin,
			 "--seqin",seqin,
			 "--colinfo","FP,SIGFP",
			 "--colinhl","parrot.ABCD.A,parrot.ABCD.B,parrot.ABCD.C,parrot.ABCD.D",
			 "--iterations",RunningParameter.BuccaneerIterations,
			 "--mtz-name",FileName,
			 
	};
	 if(RunningParameter.UsingRFree.equals("T")) {
		 String[]callAndArgsWithRfree= {
				 "ccp4-python",RunningParameter.Buccaneeri2PipeLine,
				 "--mtzin",mtzin,
				 "--seqin",seqin,
				 "--colinfo","FP,SIGFP",
				 "--colinhl","parrot.ABCD.A,parrot.ABCD.B,parrot.ABCD.C,parrot.ABCD.D",
				 "--colinfree","FreeR_flag",
				 "--iterations",RunningParameter.BuccaneerIterations,
				 "--mtz-name",FileName,
				 
		};
		 callAndArgs=callAndArgsWithRfree;//update parameters  
	 }
	 new Preparer().WriteTxtFile("ParametersUsed/"+FileName+".txt", new Date().toString()+" \n "+ Arrays.toString(callAndArgs));

	Process p = Runtime.getRuntime().exec(callAndArgs);	             

	BufferedReader stdInput = new BufferedReader(new 

		                  InputStreamReader(p.getInputStream()));



		             BufferedReader stdError = new BufferedReader(new 

		                  InputStreamReader(p.getErrorStream()));



		             // read the output
	                String Resolution="";
	                String Rfactor="";
	                String JobDir="";
		             while ((st = stdInput.readLine()) != null) {
		            	 System.out.println(st);
		            	 res.LogFile+=st+"\n";
		            	 
		            	 if(st.contains("JobDir#")) {
		            		 JobDir=st.substring(st.indexOf('#')+1,st.length());
		            		// JobDirectory=JobDir;
		            		 timer(JobDir,FileName,timer);
		            	 }
		             }

	                double difference = new java.util.Date().getTime() - ProStartTime.getTime(); 
	                DecimalFormat df = new DecimalFormat("#.##");
	                df.setRoundingMode(RoundingMode.HALF_DOWN);
	                String TimeTaking= df.format((difference/1000)/60);
	         		
	         		res.LogFile+="TimeTaking "+TimeTaking+"\n";
	         		res.LogFile=Log(JobDir);
	         		res.Roslution=Resolution;
	         		res.Rfactor=Rfactor;
	         		res.TimeTaking=TimeTaking;
	         		res.ProcessStatus="Success";
	         		
	         		FileUtils.copyFile(new File(JobDir+"/XYZOUT.pdb"),  new File("./BuccaneerResults/PDBs/"+FileName+".pdb"));
	         		
		             while ((st = stdError.readLine()) != null) {

		                 System.out.println(st);

		             }

		             

		           

		         }

		         catch (IOException e) {

		             System.out.println("exception occured");
		             timer.cancel();
		             e.printStackTrace();

		             
		             res.ProcessStatus="Failed";
		             return res;
		         }
		         timer.cancel();
		 return res;
	}
	

	synchronized Vector<String> AddFileNameToList( Vector<String> FilesNames) throws IOException{
		File yourFile = new File("./ProcessedFilesNamesBuccaneer.txt");
		yourFile.createNewFile();
		 String FileNamesTxt=new FilesUtilities().readFileAsString("./ProcessedFilesNamesBuccaneer.txt");
		 FilesNames.addAll(Arrays.asList(FileNamesTxt.split("\n")));
		 return FilesNames;
		
	}
	public  synchronized void WriteFileNameToList(String Line, String TxtName) throws IOException{
		File yourFile = new File(TxtName);
		
		yourFile.createNewFile();
		 BufferedWriter output;
		 output = new BufferedWriter(new FileWriter(TxtName, true));
		 output.append(Line+"\n");
		 output.close();
	}
	
}
