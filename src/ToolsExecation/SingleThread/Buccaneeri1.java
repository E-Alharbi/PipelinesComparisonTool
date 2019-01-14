package ToolsExecation.SingleThread;
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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Analyser.FactorsFlags;
import Analyser.PipelineLog;
import NotUsed.ARPResultsAnalysis;
import Run.Preparer;
import Run.RunComparison;
import Run.RunningPram;

public class Buccaneeri1 {
/*
 * Remove the double .. before release the code.
 */
	static String JobDirectory="";// to use for IntermediateResults
	static String PDBID="";
	static boolean FinshedBuilding=false;
	static String LogTXT="";
	static Date StartTime;
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		if(args.length<1){
			System.out.println("Error: No Inputs! ");
			System.out.println("You have to set the path for your data folder");
			System.exit(-1);
		}
		
		RunningPram.DataPath=args[0];
		new Buccaneeri1().RunBuccaneerTool();
	}
	
	boolean SaveIntermediateResults() throws InterruptedException, IOException {
		if(new File(JobDirectory+"/"+"refine.pdb").exists()) {
     		FileUtils.copyFile(new File(JobDirectory+"/"+"refine.pdb"),  new File("./BuccaneerResults/IntermediatePDBs/"+PDBID+".pdb"));
     		
     		 double difference = new java.util.Date().getTime() - StartTime.getTime(); 
             DecimalFormat df = new DecimalFormat("#.##");
             df.setRoundingMode(RoundingMode.HALF_DOWN);
             String TimeTaking= df.format((difference/1000)/60);
      		
      		String TakingTimeWithLog=LogTXT+"\n TimeTaking "+TimeTaking+"\n";
     		try(  PrintWriter out = new PrintWriter( "./BuccaneerResults/IntermediateLogs"+"/"+PDBID+".txt" )  ){
     		    out.println( TakingTimeWithLog );
     		}
		}
		return true;
	}
	public void RunBuccaneerTool() throws IOException
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
		
		
		
		
		long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		Buccaneeri1 RBM=new Buccaneeri1();
		String CCP4Dir=System.getenv("CCP4");
		//RunningPram.BuccaneerPipeLine=CCP4Dir+"/share/python/CCP4Dispatchers/buccaneer_pipeline.py";
		RunningPram.BuccaneerPipeLine=CCP4Dir+"/share/python/CCP4Dispatchers/cbuccaneer.py";
		
		//String BuccaneerPipeLine=args[1];
		//System.out.print("DataPath: "+DataPath);
		//System.out.print("BuccaneerPipeLine: "+BuccaneerPipeLine);
		String PATHLogs = "./BuccaneerResults/BuccaneerLogs";// LogsFilesPath
		new RunComparison().CheckDirAndFile("BuccaneerResults");
		new RunComparison().CheckDirAndFile("./BuccaneerResults/PDBs");
		new RunComparison().CheckDirAndFile(PATHLogs);
		new RunComparison().CheckDirAndFile("./BuccaneerResults/IntermediateLogs");    
		new RunComparison().CheckDirAndFile("./BuccaneerResults/IntermediatePDBs");    
		new RunComparison().CheckDirAndFile("ParametersUsed");
		
		 Vector<String> FilesNames= new Vector <String>();
		
		 File[] processedfiles = new File(PATHLogs).listFiles();
			
		 for (File file : processedfiles) {
			 
			 FilesNames.add(file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),""));
			 System.out.println(file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),""));
		 }
		 
		 
		 File[] files=null ;
	     if(new File(RunningPram.DataPath).isDirectory()) {
	    	 files = new File(RunningPram.DataPath).listFiles();
	     }
		if(new File(RunningPram.DataPath).isFile()) {
			
			files = ArrayUtils.add(files, new File(RunningPram.DataPath));
		}
     FilesNames=RBM.AddFileNameToList(FilesNames);
		 for (File file : files) {
	String CaseName=file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),"");
	if(!FilesNames.contains(CaseName)){
		System.out.print("File Name "+CaseName);
		RBM.WriteFileNameToList(CaseName,"./ProcessedFilesNamesBuccaneer.txt");
    //String FileName=file.getParentFile()+"/"+file.getName().substring(0,file.getName().indexOf('.'));
    String FileName=file.getParentFile()+"/"+CaseName;
    
	FilesNames.add(CaseName);
   System.out.println(CaseName);
   PipelineLog res= new Buccaneeri1().RunBuccaneer(FileName,CaseName);

   //Results res= new RunCBuccaneer().RunBuccaneer(FileName,file.getName().substring(0,file.getName().indexOf('.')));
   long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
   long actualMemUsed=afterUsedMem-beforeUsedMem;
   System.out.println("Used memory is bytes: " + actualMemUsed);
   res.LogFile+="Used memory is bytes: "+actualMemUsed;
	try(  PrintWriter out = new PrintWriter( PATHLogs+"/"+CaseName+".txt" )  ){
	    out.println( res.LogFile );
	}
	FilesNames=AddFileNameToList(FilesNames);
	FinshedBuilding=true;
	break;
	}
	
}
		
		
	}

	PipelineLog RunBuccaneer(String FilePathAndName,String FileName) throws IOException{
		System.out.println("FilePathAndName "+FilePathAndName);
		System.out.println("FileName "+FileName);
		
		new RunComparison().CheckDirAndFile(FileName);
		
		
		String CCP4=System.getenv("CCP4");
		String BuccScript=new Preparer().ReadResourceAsString("/BuccaneerPipeline.sh");
		
		String BuccPipeline=CCP4+"/share/python/CCP4Dispatchers/buccaneer_pipeline.py";
		String mtzin=FilePathAndName+".mtz";
		String seqin="";
		if(new File(FilePathAndName+".fasta").exists())	        	 
		seqin=FilePathAndName+".fasta";
		if(new File(FilePathAndName+".fa").exists())	        	 
		seqin=FilePathAndName+".fa";
		if(new File(FilePathAndName+".seq").exists())	        	 
		seqin=FilePathAndName+".seq";
		String cycles=RunningPram.BuccaneerIterations;
		
		BuccScript=BuccScript.replace("@1", BuccPipeline);
		BuccScript=BuccScript.replace("@2", mtzin);
		BuccScript=BuccScript.replace("@3", seqin);
		BuccScript=BuccScript.replace("@4", cycles);
		BuccScript=BuccScript.replace("@5", FileName);
		 if(RunningPram.UsingRFree.equals("T")) {
			 BuccScript=BuccScript.replace("@6", "-colin-free FreeR_flag"); 
		 }
		 if(RunningPram.UsingRFree.equals("F")) {
			 BuccScript=BuccScript.replace("@6", " "); 
		 }
		new Preparer().WriteTxtFile(FileName+"/"+FileName+".sh", BuccScript);
		
		
		 JobDirectory=FileName+"/buccaneer";
		 PDBID=FileName;
		
		 String st = null;
		 Date ProStartTime = new java.util.Date();
		  StartTime= new java.util.Date();
		 PipelineLog res= new PipelineLog();
	try {
	
	 String[]callAndArgs= {"sh",
			 FileName+"/"+FileName+".sh",
	};
	 new Preparer().WriteTxtFile("ParametersUsed/"+FileName+".txt", new Date().toString()+" \n "+ Arrays.toString(callAndArgs));

	Process p = Runtime.getRuntime().exec(callAndArgs);

		             

	BufferedReader stdInput = new BufferedReader(new 

		                  InputStreamReader(p.getInputStream()));



		             BufferedReader stdError = new BufferedReader(new 

		                  InputStreamReader(p.getErrorStream()));



		            
		             while ((st = stdInput.readLine()) != null) {
		            	 System.out.println(st);
		            	 res.LogFile+=st+"\n";
		            	 LogTXT+=st+"\n";
	                 
	                
		             }

		             
			          
		          
	                double difference = new java.util.Date().getTime() - ProStartTime.getTime(); 
	                DecimalFormat df = new DecimalFormat("#.##");
	                df.setRoundingMode(RoundingMode.HALF_DOWN);
	                String TimeTaking= df.format((difference/1000)/60);
	         		System.out.println("Time Taking "+TimeTaking ) ;
	         		res.LogFile+="TimeTaking "+TimeTaking+"\n";
		             

	         		
	         		res.TimeTaking=TimeTaking;
	         		res.ProcessStatus="Success";
	         		
	         		FileUtils.copyFile(new File(FileName+"/buccaneer.pdb"),  new File("./BuccaneerResults/PDBs/"+FileName+".pdb"));
	         		//new File(pdbout).delete();
	         	//	new File("./buccaneer").delete();
	         		
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
	

	 Vector<String> AddFileNameToList( Vector<String> FilesNames) throws IOException{
		File yourFile = new File("./ProcessedFilesNamesBuccaneer.txt");
		yourFile.createNewFile();
		 String FileNamesTxt=new ARPResultsAnalysis().readFileAsString("./ProcessedFilesNamesBuccaneer.txt");
		 FilesNames.addAll(Arrays.asList(FileNamesTxt.split("\n")));
		 return FilesNames;
		
	}
	public  void WriteFileNameToList(String Line, String TxtName) throws IOException{
		File yourFile = new File(TxtName);
		//"./ProcessedFilesNamesBuccaneer.txt"
		yourFile.createNewFile();
		 BufferedWriter output;
		 output = new BufferedWriter(new FileWriter(TxtName, true));
		 output.append(Line+"\n");
		 output.close();
	}
	
}
