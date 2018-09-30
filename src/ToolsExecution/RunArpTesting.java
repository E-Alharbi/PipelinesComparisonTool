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
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Analyser.ExcelSheet;
import Analyser.PipelineLog;
import NotUsed.ARPResultsAnalysis;
import Run.RunComparison;
import Run.RunningPram;

public class RunArpTesting {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		if(args.length<2){
			System.out.println("Error: No Inputs! ");
			System.out.println("You have to set the path for your data folder and Arp/wArp autotracing.sh");
			System.exit(-1);
		}
		
		RunningPram.DataPath=args[0];
		RunningPram.wArpAutotracing=args[1];
		new RunArpTesting().RunwArpTool();
	}
	public void RunwArpTool() throws IOException
	{
		
		String PATHLogs = "./wArpResults/ArpLogs";// LogsFilesPath
		new RunComparison().CheckDirAndFile("wArpResults");
		new RunComparison().CheckDirAndFile("./wArpResults/PDBs");
		new RunComparison().CheckDirAndFile(PATHLogs);
		new RunComparison().CheckDirAndFile("./wArpResults/WorkingDir");
		
     Vector<String> FilesNames= new Vector <String>();
     File[] files = new File(RunningPram.DataPath).listFiles();
	
	
	
	
     File[] processedfiles = new File(PATHLogs).listFiles();
		
	 for (File file : processedfiles) {
		 FilesNames.add(file.getName().substring(0,file.getName().indexOf('.')));
		 System.out.println(file.getName().substring(0,file.getName().indexOf('.')));
	 }
	 RunArpTesting RunArp=new RunArpTesting();
	 FilesNames=RunArp.AddFileNameToList(FilesNames);
		 for (File file : files) {
	if(!FilesNames.contains(file.getName().substring(0,file.getName().indexOf('.')))){
		RunArp.WriteFileNameToList(file.getName().substring(0,file.getName().indexOf('.')),"./ProcessedFilesNamesArp.txt");
    String FileName=file.getParentFile()+"/"+file.getName().substring(0,file.getName().indexOf('.'));
	
	FilesNames.add(file.getName().substring(0,file.getName().indexOf('.')));
   System.out.println(file.getName().substring(0,file.getName().indexOf('.')));
   PipelineLog res= new RunArpTesting().RunArpTool(FileName,file.getName().substring(0,file.getName().indexOf('.')));
	
	
	
 
	try(  PrintWriter out = new PrintWriter( PATHLogs+"/"+file.getName().substring(0,file.getName().indexOf('.'))+".txt" )  ){
	    out.println( res.LogFile );
	}
	 FilesNames=RunArp.AddFileNameToList(FilesNames);
	 break;// to be sure each model takes the whole time in running  
	}
	
}
		
		
	}

	PipelineLog RunArpTool(String FilePathAndName,String FileName){
		 String st = null;
		 Date ProStartTime = new java.util.Date();
		 PipelineLog res= new PipelineLog();
		 
		         try {

	
String mtzin=FilePathAndName+".mtz";
String seqin=FilePathAndName+".seq";

String[]ArpParm;
if(RunningPram.UseBuccModels.trim().equals("T")) {
	String[]callAndArgs= {
			// /Applications/arp_warp_7.6/share/auto_tracing.sh 
		RunningPram.wArpAutotracing,
		"datafile",mtzin,
		"workdir",System.getProperty("user.dir")+"/wArpResults/WorkingDir",
		//"phibest","parrot.F_phi.phi",
		"phibest","hltofom.Phi_fom.phi",
		//"fom","FOM",
		"fom","hltofom.Phi_fom.fom",
		"freelabin","FreeR_flag",
		"fp","FP",
		"sigfp","SIGFP",
		"jobId",FileName,
		"seqin",seqin,
		"modelin",GetModelPath(FileName+".pdb")
		};
	 ArpParm=callAndArgs;
	if(GetModelPath(FileName+".pdb").equals("")) {
		res.LogFile+="Buccaneer model not found!!";
		return res;
	}
}
//String[]callAndArgss= {"source","/Applications/ccp4-7.0/setup-scripts/ccp4.setup-sh"};
//Process pp = Runtime.getRuntime().exec(callAndArgss);
else {
	 String[]callAndArgs= {
		// /Applications/arp_warp_7.6/share/auto_tracing.sh 
	RunningPram.wArpAutotracing,
	"datafile",mtzin,
	"workdir",System.getProperty("user.dir")+"/wArpResults/WorkingDir",
	//"phibest","parrot.F_phi.phi",
	"phibest","hltofom.Phi_fom.phi",
	//"fom","FOM",
	"fom","hltofom.Phi_fom.fom",
	"freelabin","FreeR_flag",
	"fp","FP",
	"sigfp","SIGFP",
	"jobId",FileName,
	"seqin",seqin
	};
	 ArpParm=callAndArgs;
}

	 Process p = Runtime.getRuntime().exec(ArpParm);

		             

	BufferedReader stdInput = new BufferedReader(new 

		                  InputStreamReader(p.getInputStream()));



		             BufferedReader stdError = new BufferedReader(new 

		                  InputStreamReader(p.getErrorStream()));



		             // read the output
	String Resolution="";
	String Rfactor="";
	String JobID="";
		             while ((st = stdInput.readLine()) != null) {
		            	 System.out.println(st);
		            	 res.LogFile+=st+"\n";
	                 if(st.contains("Resolution range:")){
	                	
	                  Resolution=st;
		              
	                 }
	                 if(st.contains("After refmac, R =")){
		
	               Rfactor=st;
	   	              
	                    }
	                 if(st.contains("Job ID is set to")){
	                	 Pattern pattern = Pattern.compile("\\d+");
	             		Matcher m = pattern.matcher(st);
	             		m.find();
	             		 
	                	 JobID=st.substring(m.start());
	                	 
	                 }
	                
		             }
		             System.out.println("JobID "+JobID);
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
	         		
	         		 FileUtils.copyFile(new File(System.getProperty("user.dir")+"/wArpResults/WorkingDir/"+JobID+"/"+FileName+"_warpNtrace.pdb"),  new File("./wArpResults/PDBs/"+FileName+".pdb"));
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
			File yourFile = new File("./ProcessedFilesNamesArp.txt");
			yourFile.createNewFile();
			 String FileNamesTxt=new ARPResultsAnalysis().readFileAsString("./ProcessedFilesNamesArp.txt");
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
		String GetModelPath(String PDBId){
			
				 File[] BuccModels = new File(RunningPram.BuccModels).listFiles();
				 for (File PDB : BuccModels) {
					 if(PDBId.trim().equals(PDB.getName())) {
						 return PDB.getAbsolutePath();
					 }
				 }
				 return "";// Not Found
		}
}
