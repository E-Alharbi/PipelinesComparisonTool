package ToolsExecation.SingleThread;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ResultsParsing.ARPResultsAnalysis;
import ResultsParsing.ExcelSheet;
import ResultsParsing.Results;
import Run.RunComparison;
import Run.RunningPram;

public class RunArp {
	//static String JobDirectory="";// to use for IntermediateResults
	//static String PDBID="";
	static boolean FinshedBuilding=false;
	static String LogTXT="";
	static Date StartTime;
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		if(args.length<2){
			System.out.println("Error: No Inputs! ");
			System.out.println("You have to set the path for your data folder and Arp/wArp autotracing.sh");
			System.exit(-1);
		}
		
		RunningPram.DataPath=args[0];
		RunningPram.wArpAutotracing=args[1];
		new RunArp().RunwArpTool();
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
				//System.out.println("Failed to save intermediate PDB");
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
	public void RunwArpTool() throws IOException
	{
		
		/*
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
		*/
		
		/*
		Runnable r = new Runnable() {
        public void run() {
       	 try {
				SaveIntermediateResults();
			} catch (InterruptedException | IOException  | StackOverflowError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("hereeee");
				
			}
        }
    };

    new Thread(r).start();
    */
		long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		String PATHLogs = "./wArpResults/ArpLogs";// LogsFilesPath
		new RunComparison().CheckDirAndFile("wArpResults");
		new RunComparison().CheckDirAndFile("./wArpResults/PDBs");
		new RunComparison().CheckDirAndFile(PATHLogs);
		new RunComparison().CheckDirAndFile("./wArpResults/WorkingDir");
		new RunComparison().CheckDirAndFile("./wArpResults/IntermediateLogs");    
		new RunComparison().CheckDirAndFile("./wArpResults/IntermediatePDBs");
     Vector<String> FilesNames= new Vector <String>();
     File[] files = new File(RunningPram.DataPath).listFiles();
	
	
	
	
     File[] processedfiles = new File(PATHLogs).listFiles();
     
	 for (File file : processedfiles) {
		 FilesNames.add(file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),""));
		 System.out.println(file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),""));
	 }
	 RunArp RunArp=new RunArp();
	 FilesNames=RunArp.AddFileNameToList(FilesNames);
		 for (File file : files) {
	String CaseName=file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),"");
	if(!FilesNames.contains(CaseName)){
		RunArp.WriteFileNameToList(CaseName,"./ProcessedFilesNamesArp.txt");
   // String FileName=file.getParentFile()+"/"+file.getName().substring(0,file.getName().indexOf('.'));
	    String FileName=file.getParentFile()+"/"+CaseName;

   FilesNames.add(CaseName);
   System.out.println(CaseName);
   //Results res= new RunArp().RunArpTool(FileName,file.getName().substring(0,file.getName().indexOf('.')));
   Results res= new RunArp().RunArpTool(FileName,CaseName);

	
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
	
	Results RunArpTool(String FilePathAndName,String FileName){
		Timer timer = new Timer();
		 //JobDirectory=System.getProperty("user.dir")+"/wArpResults/WorkingDir/"+FileName;
		// PDBID=FileName;
		 timer(System.getProperty("user.dir")+"/wArpResults/WorkingDir/"+FileName,FileName,timer);
		 String st = null;
		 Date ProStartTime = new java.util.Date();
		  StartTime= new java.util.Date();
		 Results res= new Results();
		 
		         try {

	
String mtzin=FilePathAndName+".mtz";
String seqin=FilePathAndName+".seq";
//String seqin=FilePathAndName+".fa";
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
	"seqin",seqin,
	"fakedata","0.33;0.75;1"
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
	         		
	         		 FileUtils.copyFile(new File(System.getProperty("user.dir")+"/wArpResults/WorkingDir/"+JobID+"/"+FileName+"_warpNtrace.pdb"),  new File("./wArpResults/PDBs/"+FileName+".pdb"));
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
