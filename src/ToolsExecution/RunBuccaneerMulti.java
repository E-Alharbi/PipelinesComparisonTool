package ToolsExecution;
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
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Comparison.Analyser.PipelineLog;
import Comparison.Runner.RunComparison;
import Comparison.Runner.RunningParameter;
import NotUsed.ARPResultsAnalysis;

public class RunBuccaneerMulti {
/*
 * Remove the double .. before release the code.
 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		if(args.length<1){
			System.out.println("Error: No Inputs! ");
			System.out.println("You have to set the path for your data folder");
			System.exit(-1);
		}
		
		RunningParameter.DataPath=args[0];
		new RunBuccaneerMulti().RunBuccaneerTool();
	}
	public void RunBuccaneerTool() throws IOException
	{
		RunBuccaneerMulti RBM=new RunBuccaneerMulti();
		String CCP4Dir=System.getenv("CCP4");
		RunningParameter.BuccaneerPipeLine=CCP4Dir+"/share/python/CCP4Dispatchers/buccaneer_pipeline.py";
		//RunningPram.BuccaneerPipeLine=CCP4Dir+"/share/python/CCP4Dispatchers/cbuccaneer.py";
		
		//String BuccaneerPipeLine=args[1];
		//System.out.print("DataPath: "+DataPath);
		//System.out.print("BuccaneerPipeLine: "+BuccaneerPipeLine);
		String PATHLogs = "./BuccaneerResults/BuccaneerLogs";// LogsFilesPath
		new RunComparison().CheckDirAndFile("BuccaneerResults");
		new RunComparison().CheckDirAndFile("./BuccaneerResults/PDBs");
		new RunComparison().CheckDirAndFile(PATHLogs);
		    

		
		 Vector<String> FilesNames= new Vector <String>();
		
		 File[] processedfiles = new File(PATHLogs).listFiles();
			
		 for (File file : processedfiles) {
			 FilesNames.add(file.getName().substring(0,file.getName().indexOf('.')));
			 System.out.println(file.getName().substring(0,file.getName().indexOf('.')));
		 }
		 
		 
     File[] files = new File(RunningParameter.DataPath).listFiles();
     FilesNames=RBM.AddFileNameToList(FilesNames);
		 for (File file : files) {
	if(!FilesNames.contains(file.getName().substring(0,file.getName().indexOf('.')))){
		System.out.print("File Name "+file.getName().substring(0,file.getName().indexOf('.')));
		RBM.WriteFileNameToList(file.getName().substring(0,file.getName().indexOf('.')),"./ProcessedFilesNamesBuccaneer.txt");
    String FileName=file.getParentFile()+"/"+file.getName().substring(0,file.getName().indexOf('.'));
	
	FilesNames.add(file.getName().substring(0,file.getName().indexOf('.')));
   System.out.println(file.getName().substring(0,file.getName().indexOf('.')));
   PipelineLog res= new RunBuccaneerMulti().RunBuccaneer(FileName,file.getName().substring(0,file.getName().indexOf('.')));

	try(  PrintWriter out = new PrintWriter( PATHLogs+"/"+file.getName().substring(0,file.getName().indexOf('.'))+".txt" )  ){
	    out.println( res.LogFile );
	}
	FilesNames=AddFileNameToList(FilesNames);
	}
	
}
		
		
	}

	PipelineLog RunBuccaneer(String FilePathAndName,String FileName){
		 String st = null;
		 Date ProStartTime = new java.util.Date();
		 PipelineLog res= new PipelineLog();
		         try {
	String seqin=FilePathAndName+".seq";
	String mtzin=FilePathAndName+".mtz";
	String colinhl="parrot.ABCD.A,parrot.ABCD.B,parrot.ABCD.C,parrot.ABCD.D";
	String colinfo="FP,SIGFP";
	String colinfree="FreeR_flag";
	String pdbout=FileName+".pdb";
	String cycles="25";
	String BAC="-buccaneer-anisotropy-correction";
	String BuccaneerFast="-buccaneer-fast";	
	String BKV="verbose 5"; 
	 String[]callAndArgs= {"python",
			 RunningParameter.BuccaneerPipeLine,
	"-seqin",seqin,
	"-mtzin",mtzin,
	"-colin-hl",colinhl,
	"-colin-fo",colinfo,
	"-colin-free",colinfree,
	"-pdbout",pdbout,
	"-cycles",cycles,
	BAC,
	BuccaneerFast,
	"-buccaneer-keyword",BKV};

	Process p = Runtime.getRuntime().exec(callAndArgs);

		             

	BufferedReader stdInput = new BufferedReader(new 

		                  InputStreamReader(p.getInputStream()));



		             BufferedReader stdError = new BufferedReader(new 

		                  InputStreamReader(p.getErrorStream()));



		             // read the output
	                String Resolution="";
	                String Rfactor="";
		             while ((st = stdInput.readLine()) != null) {
		            	 System.out.println(st);
		            	 res.LogFile+=st+"\n";
	                 if(st.contains("Resolution range:")){
	                	
	                  Resolution=st.substring(st.indexOf('-')+1).trim();
		              
	                 }
	                 if(st.contains("R factor")){
		
	               Rfactor=st.split(" ")[st.split(" ").length-1];
	   	              
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
	         		
	         		FileUtils.copyFileToDirectory(new File(pdbout),  new File("./BuccaneerResults/PDBs"));
	         		new File(pdbout).delete();
	         		new File("./buccaneer").delete();
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
