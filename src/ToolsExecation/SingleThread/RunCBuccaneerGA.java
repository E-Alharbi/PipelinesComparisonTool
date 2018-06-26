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
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import ResultsParsing.ARPResultsAnalysis;
import ResultsParsing.Factors;
import ResultsParsing.Results;
import Run.RunComparison;
import Run.RunningPram;
import ToolsExecution.Refmac;

public class RunCBuccaneerGA {
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
		new RunCBuccaneerGA().RunBuccaneerTool();
	}
	
	boolean SaveIntermediateResults() throws InterruptedException, IOException {
		if(new File(JobDirectory+"/"+PDBID+"Ref.pdb").exists()) {
     		FileUtils.copyFile(new File(JobDirectory+"/"+PDBID+"Ref.pdb"),  new File("./BuccaneerResults/IntermediatePDBs/"+PDBID+".pdb"));
     		
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
		RunCBuccaneerGA RBM=new RunCBuccaneerGA();
		String CCP4Dir=System.getenv("CCP4");
		//RunningPram.BuccaneerPipeLine=CCP4Dir+"/share/python/CCP4Dispatchers/buccaneer_pipeline.py";
	//	RunningPram.BuccaneerPipeLine=CCP4Dir+"/share/python/CCP4Dispatchers/cbuccaneer.py";
		
		//String BuccaneerPipeLine=args[1];
		//System.out.print("DataPath: "+DataPath);
		//System.out.print("BuccaneerPipeLine: "+BuccaneerPipeLine);
		String PATHLogs = "./BuccaneerResults/BuccaneerLogs";// LogsFilesPath
		new RunComparison().CheckDirAndFile("BuccaneerResults");
		new RunComparison().CheckDirAndFile("./BuccaneerResults/PDBs");
		new RunComparison().CheckDirAndFile(PATHLogs);
		new RunComparison().CheckDirAndFile("./BuccaneerResults/IntermediateLogs");    
		new RunComparison().CheckDirAndFile("./BuccaneerResults/IntermediatePDBs");    

		
		 Vector<String> FilesNames= new Vector <String>();
		
		 File[] processedfiles = new File(PATHLogs).listFiles();
			
		 for (File file : processedfiles) {
			 
			 FilesNames.add(file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),""));
			 System.out.println(file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),""));
		 }
		 
		 
     File[] files = new File(RunningPram.DataPath).listFiles();
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
   Results res= new RunCBuccaneerGA().RunBuccaneer(FileName,CaseName);

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

	Results RunBuccaneer(String FilePathAndName,String FileName){
		System.out.println("FilePathAndName "+FilePathAndName);
		System.out.println("FileName "+FileName);
		System.out.println("BuccaneerPipeLine "+ RunningPram.BuccaneerPipeLine);
		String WorkingDir="BuccaneerResults/WorkingDir"+FileName;
		 JobDirectory=WorkingDir;
		 PDBID=FileName;
		new RunComparison().CheckDirAndFile(WorkingDir);
		 String st = null;
		 Date ProStartTime = new java.util.Date();
		  StartTime= new java.util.Date();
		 Results res= new Results();
		         try {
	String seqin=FilePathAndName+".seq";
	String mtzin=FilePathAndName+".mtz";
	String colinhl="parrot.ABCD.A,parrot.ABCD.B,parrot.ABCD.C,parrot.ABCD.D";
	String colinfo="FP,SIGFP";
	String colinfree="FreeR_flag";
	//String pdbout=FileName+".pdb";
	String cycles="3";
	String BAC="-anisotropy-correction";
	String BuccaneerFast="-fast";	
	String BKV="-verbose 5"; 
	String pdbout=WorkingDir+"/build.pdb";	
	String LIBINRefMac="FP=FP SIGFP=SIGFP FREE=FreeR_flag HLA=parrot.ABCD.A HLB=parrot.ABCD.B HLC=parrot.ABCD.C HLD=parrot.ABCD.D";
	 String[]callAndArgs= {
			 RunningPram.BuccaneerPipeLine,
	"-seqin",seqin,
	"-mtzin",mtzin,
	"-colin-hl",colinhl,
	"-colin-fo",colinfo,
	"-colin-free",colinfree,
	"-pdbout",pdbout,
	"-cycles",cycles,
	"-fragments-per-100-residues","100",
	BAC,
	BuccaneerFast,
	//BKV
	};

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
		            	 LogTXT+=st+"\n";
	                 if(st.contains("Resolution range:")){
	                	
	                  Resolution=st.substring(st.indexOf('-')+1).trim();
		              
	                 }
	                 if(st.contains("R factor")){
		
	               Rfactor=st.split(" ")[st.split(" ").length-1];
	   	              
	                    }
	                
		             }

		            RunningPram.RefmacPath=System.getenv("CCP4")+"/bin/refmac5";
			        Factors F=  new Refmac().RunRefmac(FilePathAndName+".mtz", WorkingDir+"/build.pdb",  RunningPram.RefmacPath, "CBuccaneer", WorkingDir+"/"+FileName,LIBINRefMac);
			        res.LogFile+=F.Log;
			            for (int i =1 ; i< 1 ; ++i ) {
			            	System.out.println("########## Cycle "+i+" ##########");
			            	mtzin=WorkingDir+"/"+FileName+"Ref.mtz";
			            	cycles="2";
			            	String pdbin = WorkingDir+"/"+FileName+"Ref.pdb";
			            	colinhl="HLACOMB,HLBCOMB,HLCCOMB,HLDCOMB";
			            	 String [] callAndArgs1= {
			            			 RunningPram.BuccaneerPipeLine,
			            	"-seqin",seqin,
			            	"-mtzin",mtzin,
			            	"-colin-hl",colinhl,
			            	"-colin-fo",colinfo,
			            	"-colin-free",colinfree,
			            	"-pdbout",pdbout,
			            	"-pdbin",pdbin,
			            	"-cycles",cycles,
			            	BAC,
			            	BuccaneerFast,
			            	//BKV
			            	};

			            	  p = Runtime.getRuntime().exec(callAndArgs1);
			            	  stdInput = new BufferedReader(new 

					                  InputStreamReader(p.getInputStream()));



					              stdError = new BufferedReader(new 

					                  InputStreamReader(p.getErrorStream()));
			            	 while ((st = stdInput.readLine()) != null) {
				            	 System.out.println(st);
				            	 res.LogFile+=st+"\n";
				            	 LogTXT+=st+"\n";
				             }
			            	 while ((st = stdError.readLine()) != null) {

				                 System.out.println(st);

				             }
			            	 System.out.println("");
			   System.out.println("##### Refmac is Running...  #####"); 
			   System.out.println("##### Refmac Path: "+ RunningPram.RefmacPath); 
			    F=  new Refmac().RunRefmac(FilePathAndName+".mtz", WorkingDir+"/build.pdb",  RunningPram.RefmacPath, "CBuccaneer", WorkingDir+"/"+FileName,LIBINRefMac);
			  System.out.println("##### End of Refmac Running  #####"); 
			  res.LogFile+=F.Log;
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
	         		
	         		FileUtils.copyFile(new File(WorkingDir+"/"+FileName+"Ref.pdb"),  new File("./BuccaneerResults/PDBs/"+FileName+".pdb"));
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
