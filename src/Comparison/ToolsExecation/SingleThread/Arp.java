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
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.parser.ParseException;

import Comparison.Analyser.ExcelSheet;
import Comparison.Analyser.PipelineLog;
import Comparison.Runner.Preparer;
import Comparison.Runner.RunComparison;
import Comparison.Runner.RunningParameter;
import Comparison.Utilities.FilesUtilities;
import Comparison.Utilities.JSONReader;


public class Arp {
	
	static boolean FinshedBuilding=false;
	static String LogTXT="";
	static Date StartTime;
	public static void main(String[] args) throws IOException, ParseException {
		// TODO Auto-generated method stub

		if(args.length<2){
			System.out.println("Error: No Inputs! ");
			System.out.println("You have to set the path for your data folder and Arp/wArp autotracing.sh");
			System.exit(-1);
		}
		
		RunningParameter.DataPath=args[0];
		RunningParameter.wArpAutotracing=args[1];
		new Arp().RunwArpTool();
	}
	boolean SaveIntermediateResults(String JobDirectory , String PDBID) throws InterruptedException, IOException {
		
		
		if(!JobDirectory.equals("") && new File(JobDirectory+"/temp_tracing").exists()) {
			 Collection<File> Logs = FileUtils.listFiles(
    				 new File(JobDirectory+"/temp_tracing"), 
    				 new RegexFileFilter("^arp_[0-9]+.pdb"), 
   				  FileFileFilter.FILE
    				);
    		 Vector <File>LogsFiles = new   Vector <File>();
    		 LogsFiles.addAll(Logs);
			if(LogsFiles.size()>0) {
			int PDBNum=-1; // because Arp start from cycle 0
			File TempPDB=null;
			for(int i=0; i < LogsFiles.size() ; ++i) {
				
				if(Integer.parseInt(LogsFiles.get(i).getName().replaceAll("[^0-9]", "")) >PDBNum ) {
					PDBNum = Integer.parseInt(LogsFiles.get(i).getName().replaceAll("[^0-9]", ""));
					TempPDB=LogsFiles.get(i);
				}

			 }
			
      		try {
      			if(new File(TempPDB.getAbsolutePath()).exists())
				FileUtils.copyFile(new File(TempPDB.getAbsolutePath()),  new File("./wArpResults/IntermediatePDBs/"+PDBID+".pdb"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				LogTXT+="Failed to save intermediate PDB";
				
			}
      		
      		 double difference = new java.util.Date().getTime() - StartTime.getTime(); 
             DecimalFormat df = new DecimalFormat("#.##");
             df.setRoundingMode(RoundingMode.HALF_DOWN);
             String TimeTaking= df.format((difference/1000)/60);
      		
      		String TimeTakingAndLog=LogTXT+" \n TimeTaking "+TimeTaking+"\n";
    		
    		 try(  PrintWriter out = new PrintWriter( "./wArpResults/IntermediateLogs/"+PDBID+".txt" )  ){
 			    out.println( TimeTakingAndLog );
 			}
			
			}
		}
		
		//Thread.sleep(1000);
		//if(FinshedBuilding ==false)
		//return SaveIntermediateResults();
		
		return true;
	}
void timer(String JobDirectory , String PDBID,Timer t ) {
		
		
		t.scheduleAtFixedRate(
		    new TimerTask()
		    {
		        public void run()
		        {
		            
		            try {
		            //	if(FinshedBuilding ==false)
		            		SaveIntermediateResults(JobDirectory ,  PDBID );
		           // 	else
		           // 	t.cancel();
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
	public void RunwArpTool() throws IOException, ParseException
	{
		
		
		long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		String PATHLogs = "./wArpResults/ArpLogs";// LogsFilesPath
		new RunComparison().CheckDirAndFile("wArpResults");
		new RunComparison().CheckDirAndFile("./wArpResults/PDBs");
		new RunComparison().CheckDirAndFile(PATHLogs);
		new RunComparison().CheckDirAndFile("./wArpResults/WorkingDir");
		new RunComparison().CheckDirAndFile("./wArpResults/IntermediateLogs");    
		new RunComparison().CheckDirAndFile("./wArpResults/IntermediatePDBs");
		new RunComparison().CheckDirAndFile("ParametersUsed");
		Vector<String> FilesNames= new Vector <String>();
    // File[] files = new File(RunningPram.DataPath).listFiles();
     File[] files=null ;
     if(new File(RunningParameter.DataPath).isDirectory()) { // Read dir
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
	 Arp RunArp=new Arp();
	 FilesNames=RunArp.AddFileNameToList(FilesNames);
		 for (File file : files) {
	String CaseName=file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),"");
	
	
	if(!FilesNames.contains(CaseName)){
		RunArp.WriteFileNameToList(CaseName,"./ProcessedFilesNamesArp.txt");
	    String FileName=file.getParentFile()+"/"+CaseName;

   FilesNames.add(CaseName);
   System.out.println(CaseName);
   PipelineLog res= new Arp().RunArpTool(FileName,CaseName);

	
   long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
   long actualMemUsed=afterUsedMem-beforeUsedMem;
   System.out.println("Used memory is bytes: " + actualMemUsed);
   res.LogFile+="Used memory is bytes: "+actualMemUsed;
 
	try(  PrintWriter out = new PrintWriter( PATHLogs+"/"+CaseName+".txt" )  ){
	    out.println( res.LogFile );
	}
	FinshedBuilding =true;
	 FilesNames=RunArp.AddFileNameToList(FilesNames);
	 break;// to be sure each model takes the whole time in running  
	}
	
}
		
		
	}
	
	PipelineLog RunArpTool(String FilePathAndName,String FileName) throws ParseException{
		Timer timer = new Timer();
		 //JobDirectory=System.getProperty("user.dir")+"/wArpResults/WorkingDir/"+FileName;
		// PDBID=FileName;
		 timer(System.getProperty("user.dir")+"/wArpResults/WorkingDir/"+FileName,FileName,timer);
		 String st = null;
		 Date ProStartTime = new java.util.Date();
		  StartTime= new java.util.Date();
		 PipelineLog res= new PipelineLog();
		 String FP=RunningParameter.Colinfo.split(",")[0];
		 String SIGFP=RunningParameter.Colinfo.split(",")[1];
		         try {

	
String mtzin=FilePathAndName+".mtz";
//String seqin=FilePathAndName+".seq";
//String seqin=FilePathAndName+".fa";
//String seqin=FilePathAndName+".fasta";
String seqin="";
if(new File(FilePathAndName+".fasta").exists())	        	 
seqin=FilePathAndName+".fasta";
if(new File(FilePathAndName+".fa").exists())	        	 
seqin=FilePathAndName+".fa";
if(new File(FilePathAndName+".seq").exists())	        	 
seqin=FilePathAndName+".seq";
String[]ArpParm;
if(RunningParameter.UseInitialModels.trim().equals("T")) {
	
	String[]callAndArgs= {
			// /Applications/arp_warp_7.6/share/auto_tracing.sh 
		RunningParameter.wArpAutotracing,
		"datafile",mtzin,
		"workdir",System.getProperty("user.dir")+"/wArpResults/WorkingDir",
		//"phibest","parrot.F_phi.phi",
		"phibest","hltofom.Phi_fom.phi",
		//"fom","FOM",
		"fom","hltofom.Phi_fom.fom",
		"freelabin", RunningParameter.Rfreeflag,
		"fp",FP,
		"sigfp",SIGFP,
		"jobId",FileName,
		"seqin",seqin,
		"modelin",GetModelPath(FileName+".pdb")
		};
	ArpParm=callAndArgs;
	if(RunningParameter.UsingRFree.equals("F")) {
		String[]callAndArgsNoRfree= {
				// /Applications/arp_warp_7.6/share/auto_tracing.sh 
			RunningParameter.wArpAutotracing,
			"datafile",mtzin,
			"workdir",System.getProperty("user.dir")+"/wArpResults/WorkingDir",
			//"phibest","parrot.F_phi.phi",
			"phibest","hltofom.Phi_fom.phi",
			//"fom","FOM",
			"fom","hltofom.Phi_fom.fom",
			"fp",FP,
			"sigfp",SIGFP,
			"jobId",FileName,
			"seqin",seqin,
			"modelin",GetModelPath(FileName+".pdb")
			};
		 ArpParm=callAndArgsNoRfree;
	}
	
	if(GetModelPath(FileName+".pdb").equals("")) {
		res.LogFile+=" model not found!!";
		timer.cancel();
		return res;
	}
}
//String[]callAndArgss= {"source","/Applications/ccp4-7.0/setup-scripts/ccp4.setup-sh"};
//Process pp = Runtime.getRuntime().exec(callAndArgss);
else {
	 String[]callAndArgs= {
		// /Applications/arp_warp_7.6/share/auto_tracing.sh 
	RunningParameter.wArpAutotracing,
	"datafile",mtzin,
	"workdir",System.getProperty("user.dir")+"/wArpResults/WorkingDir",
	//"phibest","parrot.F_phi.phi",
	"phibest","hltofom.Phi_fom.phi",
	//"fom","FOM",
	"fom","hltofom.Phi_fom.fom",
	"freelabin", RunningParameter.Rfreeflag,
	"fp",FP,
	"sigfp",SIGFP,
	"jobId",FileName,
	"seqin",seqin,
	//"fakedata","0.33;0.75;1"
	};
	 ArpParm=callAndArgs;
	 
	 if(RunningParameter.UsingRFree.equals("F")) {
		 String[]callAndArgsNoRFree= {
					// /Applications/arp_warp_7.6/share/auto_tracing.sh 
				RunningParameter.wArpAutotracing,
				"datafile",mtzin,
				"workdir",System.getProperty("user.dir")+"/wArpResults/WorkingDir",
				//"phibest","parrot.F_phi.phi",
				"phibest","hltofom.Phi_fom.phi",
				//"fom","FOM",
				"fom","hltofom.Phi_fom.fom",
				"fp",FP,
				"sigfp",SIGFP,
				"jobId",FileName,
				"seqin",seqin,
				//"fakedata","0.33;0.75;1"
				};
		 ArpParm=callAndArgsNoRFree;
	 }
}

if(RunningParameter.MR.equals("T") && RunningParameter.UsingRFree.equals("T")) {
	 String[]callAndArgsRFree= {
				// /Applications/arp_warp_7.6/share/auto_tracing.sh 
			RunningParameter.wArpAutotracing,
			"datafile",mtzin,
			"workdir",System.getProperty("user.dir")+"/wArpResults/WorkingDir",
			"fp",FP,
			"sigfp",SIGFP,
			"freelabin",RunningParameter.Rfreeflag,
			"jobId",FileName,
			"seqin",seqin,
			"modelin",FilePathAndName+".pdb",
			
			};
	 ArpParm=callAndArgsRFree;
}
if(RunningParameter.MR.equals("T") && RunningParameter.UsingRFree.equals("F")) {
	 String[]callAndArgsRFree= {
				// /Applications/arp_warp_7.6/share/auto_tracing.sh 
			RunningParameter.wArpAutotracing,
			"datafile",mtzin,
			"workdir",System.getProperty("user.dir")+"/wArpResults/WorkingDir",
			"fp",FP,
			"sigfp",SIGFP,
			"jobId",FileName,
			"seqin",seqin,
			"modelin",FilePathAndName+".pdb",
			
			};
	 ArpParm=callAndArgsRFree;
}
if(RunningParameter.MR.equals("T")) {
	//String semet=new JSONReader().JSONToHashMap(FilePathAndName+".json").get("semet");
	String semet="false";
	if(new File(FilePathAndName+".json").exists())
		 semet=new JSONReader().JSONToHashMap(FilePathAndName+".json").get("semet");
		else {
			if(RunningParameter.semet.equals("T"))
				semet="true";
		}
	
	if(semet.toLowerCase().equals("true"))
	{
		List<String> a = new ArrayList<String>();
		 a.addAll(Arrays.asList(ArpParm));
		 a.add("is_semet");
		 a.add("1");
		 String[] myArray = new String[a.size()];
		 a.toArray(myArray);
		 ArpParm=myArray;
	}
}
new Preparer().WriteTxtFile("ParametersUsed/"+FileName+".txt", new Date().toString()+" \n "+ Arrays.toString(ArpParm));
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
		            	 LogTXT+=st+"\n";
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
	         		
	         	//	 FileUtils.copyFile(new File(System.getProperty("user.dir")+"/wArpResults/WorkingDir/"+JobID+"/"+FileName+"_warpNtrace.pdb"),  new File("./wArpResults/PDBs/"+FileName+".pdb"));
	         		 //FileUtils.deleteDirectory(new File(System.getProperty("user.dir")+"/wArpResults/WorkingDir/"+JobID));// removing job dir to save storage   
	         		
	         		Collection<File> wPDB = FileUtils.listFiles(
	        				 new File(System.getProperty("user.dir")+"/wArpResults/WorkingDir/"+JobID+"/"), 
	        				 new RegexFileFilter(".*_warpNtrace.pdb$"), 
	        				  FileFileFilter.FILE
	        				);
	        		 Vector <File>FinalPDB = new   Vector <File>();
	        		 FinalPDB.addAll(wPDB);
	 	       	 FileUtils.copyFile(FinalPDB.get(0),  new File("./wArpResults/PDBs/"+FileName+".pdb"));

	         		 while ((st = stdError.readLine()) != null) {

		                 System.out.println(st);

		             }

		             

		            // System.exit(0);

		         }

		         catch (IOException e) {
		        	 timer.cancel();
		             System.out.println("exception occured");

		             e.printStackTrace();

		             //System.exit(-1);
		             res.ProcessStatus="Failed";
		             return res;
		         }
		         timer.cancel();
		 return res;
	}
	
	 Vector<String> AddFileNameToList( Vector<String> FilesNames) throws IOException{
			File yourFile = new File("./ProcessedFilesNamesArp.txt");
			yourFile.createNewFile();
			 String FileNamesTxt=new FilesUtilities().readFileAsString("./ProcessedFilesNamesArp.txt");
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
			
				 File[] BuccModels = new File(RunningParameter.InitialModels).listFiles();
				 for (File PDB : BuccModels) {
					 if(PDBId.trim().equals(PDB.getName())) {
						 return PDB.getAbsolutePath();
					 }
				 }
		     BuccModels = new File(RunningParameter.InitialModels.replaceAll("PDBs", "IntermediatePDBs")).listFiles(); 
		     for (File PDB : BuccModels) {
				 if(PDBId.trim().equals(PDB.getName())) {
					 return PDB.getAbsolutePath();
				 }
			 } 
				 return "";// Not Found
		}
}
