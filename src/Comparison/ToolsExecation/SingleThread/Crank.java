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
import java.util.Arrays;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Comparison.Analyser.ExcelSheet;
import Comparison.Analyser.PipelineLog;
import Comparison.Runner.Preparer;
import Comparison.Runner.RunComparison;
import Comparison.Runner.RunningParameter;
import Comparison.Utilities.FilesManagements;


public class Crank {

	
	static boolean FinshedBuilding=false;
	static Date StartTime;
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		if(args.length<2){
			System.out.println("Error: No Inputs! ");
			System.out.println("You have to set the path for your data folder and crank pipeLine");
			System.exit(-1);
		}
		
		RunningParameter.DataPath=args[0];
		RunningParameter.CrankPipeLine=args[1];
		new Crank().RunCrank();
	}
	boolean SaveIntermediateResults(String JobDirectory , String PDBID) throws InterruptedException, IOException {
		if(!JobDirectory.equals("")) {
		
			if(new File(JobDirectory+"/0-comb_phdmmb/best.pdb").exists()) {
				 double difference = new java.util.Date().getTime() - StartTime.getTime(); 
	                DecimalFormat df = new DecimalFormat("#.##");
	                df.setRoundingMode(RoundingMode.HALF_DOWN);
	                String TimeTaking= df.format((difference/1000)/60);
	         		
	         		
	         
	         	String Log=new FilesManagements().readFileAsString(JobDirectory+"/0-comb_phdmmb/comb_phdmmb.log");
	         	Log+="TimeTaking "+TimeTaking+"\n";
      		FileUtils.copyFile(new File(JobDirectory+"/0-comb_phdmmb/best.pdb"),  new File("./CrankResults/IntermediatePDBs/"+PDBID+".pdb"));
      		try(  PrintWriter out = new PrintWriter( "./CrankResults/IntermediateLogs/"+PDBID+".txt" )  ){
      		    out.println( Log );
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
	public void RunCrank() throws IOException
	{
		
		
		
		long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		String PATHLogs = "./CrankResults/CrankLogs";// LogsFilesPath
		new RunComparison().CheckDirAndFile("CrankResults");
		new RunComparison().CheckDirAndFile("./CrankResults/PDBs");
		new RunComparison().CheckDirAndFile("./CrankResults/MTZout");
		new RunComparison().CheckDirAndFile(PATHLogs);
		new RunComparison().CheckDirAndFile("./CrankResults/WorkingDir");
		new RunComparison().CheckDirAndFile("./CrankResults/IntermediateLogs");    
		new RunComparison().CheckDirAndFile("./CrankResults/IntermediatePDBs");
		new RunComparison().CheckDirAndFile("ParametersUsed");
     Vector<String> FilesNames= new Vector <String>();
     File[] files=null ;
     if(new File(RunningParameter.DataPath).isDirectory()) {
    	 files = new File(RunningParameter.DataPath).listFiles();
     }
	if(new File(RunningParameter.DataPath).isFile()) {
		
		files = ArrayUtils.add(files, new File(RunningParameter.DataPath));
	}
	
	
	
	
     File[] processedfiles = new File(PATHLogs).listFiles();
		
	 for (File file : processedfiles) {
		
		 FilesNames.add(file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),""));
		 System.out.println(file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),""));
	 }
	 Crank RunArp=new Crank();
	 FilesNames=RunArp.AddFileNameToList(FilesNames);
		 for (File file : files) {
			 String CaseName=file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),"");
	if(!FilesNames.contains(CaseName)){
		RunArp.WriteFileNameToList(CaseName,"./ProcessedFilesNamesCrank.txt");
    String FileName=file.getParentFile()+"/"+CaseName;
	
	FilesNames.add(CaseName);
   System.out.println(CaseName);
   PipelineLog res= new Crank().RunCrankTool(FileName,CaseName);
	
	
   long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
   long actualMemUsed=afterUsedMem-beforeUsedMem;
   System.out.println("Used memory is bytes: " + actualMemUsed);
   res.LogFile+="Used memory is bytes: "+actualMemUsed;
 
	try(  PrintWriter out = new PrintWriter( PATHLogs+"/"+CaseName+".txt" )  ){
	    out.println( res.LogFile );
	}
	 FilesNames=RunArp.AddFileNameToList(FilesNames);
	 FinshedBuilding=true;
	 break;// to be sure each model takes the whole time in running  
	}
	
}
		
		
	}

	PipelineLog RunCrankTool(String FilePathAndName,String FileName) throws IOException{
		
	
		Timer timer = new Timer();
		
		
		 timer(FileName,FileName,timer);
		 
		String st = null;
		 Date ProStartTime = new java.util.Date();
		 StartTime= new java.util.Date();
		 PipelineLog res= new PipelineLog();
		 
		         try {

	
String mtzin=FilePathAndName+".mtz";

String seqin="";
if(new File(FilePathAndName+".fasta").exists())	        	 
seqin=FilePathAndName+".fasta";
if(new File(FilePathAndName+".fa").exists())	        	 
seqin=FilePathAndName+".fa";
if(new File(FilePathAndName+".seq").exists())	        	 
seqin=FilePathAndName+".seq";



	 String[]callAndArgs= {
		"sh","crank.sh",
		
		FileName,
		RunningParameter.CrankPipeLine,
		seqin,
		mtzin,
		"./CrankResults/PDBs/"+FileName+".pdb",
		"./CrankResults/MTZout/"+FileName+".mtz",};
	
	 if(RunningParameter.UsingRFree.equals("F")) {
		 String[]callAndArgsNoRfree= {
					"sh","crankNoRfree.sh",
					
					FileName,
					RunningParameter.CrankPipeLine,
					seqin,
					mtzin,
					"./CrankResults/PDBs/"+FileName+".pdb",
					"./CrankResults/MTZout/"+FileName+".mtz",};
		 callAndArgs=callAndArgsNoRfree;
	 }
	
System.out.println(FileName);
	
new Preparer().WriteTxtFile("ParametersUsed/"+FileName+".txt", new Date().toString()+" \n "+ Arrays.toString(callAndArgs));

Process p = Runtime.getRuntime().exec(callAndArgs);
		             

	BufferedReader stdInput = new BufferedReader(new 

		                  InputStreamReader(p.getInputStream()));



		             BufferedReader stdError = new BufferedReader(new 

		                  InputStreamReader(p.getErrorStream()));




		             while ((st = stdInput.readLine()) != null) {
		            	 System.out.println(st);
		            	 res.LogFile+=st+"\n";
	                 
	                
		             }
		          
	                double difference = new java.util.Date().getTime() - ProStartTime.getTime(); 
	                DecimalFormat df = new DecimalFormat("#.##");
	                df.setRoundingMode(RoundingMode.HALF_DOWN);
	                String TimeTaking= df.format((difference/1000)/60);
	         		System.out.println("Time Taking "+TimeTaking ) ;
	         		res.LogFile+="TimeTaking "+TimeTaking+"\n";
		             // read any errors

	         	
	         		res.TimeTaking=TimeTaking;
	         		res.ProcessStatus="Success";
	         		
		         	FileUtils.copyDirectoryToDirectory(new File(FileName),new File( System.getProperty("user.dir")+"/CrankResults/WorkingDir/"+FileName));
		         	FileUtils.deleteDirectory(new File(FileName));
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
	
	 Vector<String> AddFileNameToList( Vector<String> FilesNames) throws IOException{
			File yourFile = new File("./ProcessedFilesNamesCrank.txt");
			yourFile.createNewFile();
			 String FileNamesTxt=new FilesManagements().readFileAsString("./ProcessedFilesNamesCrank.txt");
			 FilesNames.addAll(Arrays.asList(FileNamesTxt.split("\n")));
			 return FilesNames;
			
		}
		public  void WriteFileNameToList(String Line, String TxtName) throws IOException{
			File yourFile = new File(TxtName);
			
			yourFile.createNewFile();
			 BufferedWriter output;
			 output = new BufferedWriter(new FileWriter(TxtName, true));
			 output.append(Line+"\n");
			 output.close();
		}
		
}
