package Comparison.ToolsExecation.SingleThread;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;

import Comparison.Analyser.PipelineLog;
import Comparison.Runner.Preparer;
import Comparison.Runner.RunComparison;
import Comparison.Runner.RunningParameter;

public class shelxe extends Tool{
	static boolean FinshedBuilding=false;
	static String LogTXT="";
	static Date StartTime;
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		RunningParameter.DataPath="/Users/emadalharbi/Downloads/ShelxeDatasets/1o6a-1.9-parrot-noncs.shelxc";
		RunningParameter.Shelxe="/Users/emadalharbi/Downloads/shelxe";
		new shelxe().RunshelxeTool();
		
		
		
	}
	public void RunshelxeTool() throws IOException
	{
		long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		// Create required dirs 
		String PATHLogs = "./shelxeResults/shelxeLogs";// LogsFilesPath
		new RunComparison().CheckDirAndFile("shelxeResults");
		new RunComparison().CheckDirAndFile("./shelxeResults/PDBs");
		new RunComparison().CheckDirAndFile(PATHLogs);
		new RunComparison().CheckDirAndFile("./shelxeResults/IntermediateLogs");    
		new RunComparison().CheckDirAndFile("./shelxeResults/IntermediatePDBs");
		new RunComparison().CheckDirAndFile("ParametersUsed");
		
		Vector<String> FilesNames= new Vector <String>();
	   
	     File[] files=null ;
	     if(new File(RunningParameter.DataPath).isDirectory()) { // Read dir and it will pick a random dataset that not built before 
	    	 files = new File(RunningParameter.DataPath).listFiles();
	     }
		if(new File(RunningParameter.DataPath).isFile()) {  // only build  this dataset 
			
			files = ArrayUtils.add(files, new File(RunningParameter.DataPath));
		}
		
		// Adding built datasets where they have logs files in logs dir 
		 File[] processedfiles = new File(PATHLogs).listFiles();
	     
		 for (File file : processedfiles) {
			 FilesNames.add(file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),""));
		 }
		 // Adding  built datasets names from the txt file if exist 
		 FilesNames=AddFileNameToList(FilesNames,"./ProcessedFilesNamesShelxe.txt");
		 
		 // now loop on the datasets to pick a dataset
		 for (File file : files) {
				String CaseName=file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),"");
				// if not built before, go ahead  and build it 
				if(!FilesNames.contains(CaseName)){
					
					// write the dataset name to the txt file to avoid duplicate run from other jobs on the cluster   
					WriteFileNameToList(CaseName,"./ProcessedFilesNamesShelxe.txt");
				    String FileName=file.getParentFile()+"/"+CaseName;
				    
				  
				    FilesNames.add(CaseName);
				    
				    PipelineLog res= RunShelxe(FileName,CaseName);

				 	
				    long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
				    long actualMemUsed=afterUsedMem-beforeUsedMem;
				    System.out.println("Used memory is bytes: " + actualMemUsed);
				    res.LogFile+="Used memory is bytes: "+actualMemUsed;
				  
				 	try(  PrintWriter out = new PrintWriter( PATHLogs+"/"+CaseName+".txt" )  ){
				 	    out.println( res.LogFile );
				 	}
				 	FinshedBuilding =true;
				 	FilesNames=AddFileNameToList(FilesNames,"./ProcessedFilesNamesShelxe.txt");
				 	break;// to be sure each model takes the whole time in running
				}
		 }
	}
	PipelineLog RunShelxe(String FilePathAndName,String FileName) throws IOException {
		String st = null;
		Date ProStartTime = new java.util.Date();
		StartTime= new java.util.Date();
		PipelineLog res= new PipelineLog();
		System.out.println("FilePathAndName "+FilePathAndName);
		System.out.println("FileName "+FileName);
		new RunComparison().CheckDirAndFile(FileName);
		String WorkingDir="./"+FileName+"/";
		Timer timer = new Timer();
		 //JobDirectory=System.getProperty("user.dir")+"/wArpResults/WorkingDir/"+FileName;
		// PDBID=FileName;
		 timer(WorkingDir+FileName,FileName,timer);
		
		//Create a working dir 
		
		 FileUtils.copyFile(new File(FilePathAndName+".phi"),  new File(WorkingDir+FileName+".phi"));
		 FileUtils.copyFile(new File(FilePathAndName+".ins"),  new File(WorkingDir+FileName+".ins"));
		 FileUtils.copyFile(new File(FilePathAndName+".hkl"),  new File(WorkingDir+FileName+".hkl"));
		 String[]callAndArgs= {RunningParameter.Shelxe,WorkingDir+FileName+".phi","-a","-n","-t20"};
		
		 if(new File("SolventFraction.txt").exists()) {
			String [] SolventFraction = new String ( Files.readAllBytes( Paths.get("SolventFraction.txt") ) ).split("\n");
          System.out.println("SolventFraction "+SolventFraction.length);
		boolean Found=false;	
          for(int line=0; line<SolventFraction.length;++line) {
        	  // System.out.println(SolventFraction[line].split(",")[0].substring(0, 4));
        	  // System.out.println(FileName.substring(0,4));
        	   if(SolventFraction[line].split(",")[0].substring(0, 4).equals(FileName.substring(0,4))) {
        		   //System.out.println("matched ");
        		   if(SolventFraction[line].split(",").length==3) {
        			   //System.out.println("3 elements ");
        			   List<String> a = new ArrayList<String>();
        				 a.addAll(Arrays.asList(callAndArgs));
        				 a.add("-s"+SolventFraction[line].split(",")[2]);
        				 String[] myArray = new String[a.size()];
        				 a.toArray(myArray);
        				 callAndArgs=myArray;
        				 Found=true;
        		   }
        		   else {
        			   timer.cancel();
        			   
        			   return res; 
        		   }
        	   }
        	   
           }
          if(Found==false) {
        	  timer.cancel();
			   
			   return res; 
          }
		}
		
		
		new Preparer().WriteTxtFile("ParametersUsed/"+FileName+".txt", new Date().toString()+" \n "+ Arrays.toString(callAndArgs));
		 Process p = Runtime.getRuntime().exec(callAndArgs);
		
		// Process p = Runtime.getRuntime().exec(callAndArgs, null, new File("./"+FileName));
			             

		BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        while ((st = stdInput.readLine()) != null) {
       	 System.out.println(st);
       	 res.LogFile+=st+"\n";
       	 LogTXT+=st+"\n";
        }
        while ((st = stdError.readLine()) != null) {

            System.out.println(st);

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
 		 timer.cancel();
 		 
 		 if(res.LogFile.contains("Best trace")) // this means is a normal termination 
		 FileUtils.copyFile(new File(WorkingDir+FileName+".pdb"),   new File("./shelxeResults/PDBs/"+FileName+".pdb"));
		
		return res;
	}
	@Override
	boolean SaveIntermediateResults(String JobDirectory , String PDBID) throws IOException{
		
		if(new File(JobDirectory+".pdb").exists()) {
		 FileUtils.copyFile(new File(JobDirectory+".pdb"),   new File("./shelxeResults/IntermediatePDBs/"+PDBID+".pdb"));
		}
		 double difference = new java.util.Date().getTime() - StartTime.getTime(); 
         DecimalFormat df = new DecimalFormat("#.##");
         df.setRoundingMode(RoundingMode.HALF_DOWN);
         String TimeTaking= df.format((difference/1000)/60);
  		
  		String TimeTakingAndLog=LogTXT+" \n TimeTaking "+TimeTaking+"\n";
		
		 try(  PrintWriter out = new PrintWriter( "./shelxeResults/IntermediateLogs/"+PDBID+".txt" )  ){
			    out.println( TimeTakingAndLog );
			}
		   return true;
		}
}
