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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
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
import Comparison.Utilities.FilesManagements;
import Comparison.Utilities.JSONReader;


public class Phenix {
	
	static boolean FinshedBuilding=false;
	static Date StartTime;
	public static void main(String[] args) throws IOException, ParseException {
		// TODO Auto-generated method stub

		
		if(args.length<2){
			System.out.println("Error: No Inputs! ");
			System.out.println("You have to set the path for your data folder and Phenix.autobuild");
			System.exit(-1);
		}
		
		RunningParameter.DataPath=args[0];
		RunningParameter.PhenixAutobuild=args[1];
	
		new Phenix().RunPhenixTool();
	}
	boolean SaveIntermediateResults(String JobDirectory , String PDBID) throws InterruptedException, IOException {
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
    			 LogTxt+= new FilesManagements().readFileAsString(LogsFiles.get(i).getAbsolutePath())+"\n";  
    		 }
    		 double difference = new java.util.Date().getTime() - StartTime.getTime(); 
             DecimalFormat df = new DecimalFormat("#.##");
             df.setRoundingMode(RoundingMode.HALF_DOWN);
             String TimeTaking= df.format((difference/1000)/60);
      		
      		LogTxt+="TimeTaking "+TimeTaking+"\n";
    		 try(  PrintWriter out = new PrintWriter( "./PhenixResults/IntermediateLogs/"+PDBID+".txt" )  ){
 			    out.println( LogTxt );
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
	public void RunPhenixTool() throws IOException, ParseException
	{
		
		
		long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
		String PATHLogs = "./PhenixResults/PhinexLogs";// LogsFilesPath
		new RunComparison().CheckDirAndFile("PhenixResults");
		new RunComparison().CheckDirAndFile(PATHLogs);
		new RunComparison().CheckDirAndFile("./PhenixResults/PDBs");
		new RunComparison().CheckDirAndFile("./PhenixResults/WorkingDir");
		new RunComparison().CheckDirAndFile("./PhenixResults/IntermediateLogs");    
		new RunComparison().CheckDirAndFile("./PhenixResults/IntermediatePDBs");
		new RunComparison().CheckDirAndFile("ParametersUsed");
		 Vector<String> FilesNames= new Vector <String>();
		File[] processedfiles = new File(PATHLogs).listFiles();
			 for (File file : processedfiles) {
				 FilesNames.add(file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),""));
				 System.out.println(file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),""));
			 }
		 
		
		
	
   
    
			 File[] files=null ;
		     if(new File(RunningParameter.DataPath).isDirectory()) {
		    	 files = new File(RunningParameter.DataPath).listFiles();
		     }
			if(new File(RunningParameter.DataPath).isFile()) {
				
				files = ArrayUtils.add(files, new File(RunningParameter.DataPath));
			}
    FilesNames=AddFileNameToList(FilesNames);
		 for (File file : files) {
			 String CaseName=file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),"");
	if(!FilesNames.contains(CaseName)){
		System.out.print("File Name "+CaseName);
		WriteFileNameToList(CaseName);
    String FileName=file.getParentFile()+"/"+CaseName;
	
	FilesNames.add(CaseName);
   System.out.println(CaseName);
   PipelineLog res= new Phenix().Run(FileName,CaseName);
   
   
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
	
   
		
	FilesNames=AddFileNameToList(FilesNames);
	break;
	}
	
}
		
		 FinshedBuilding =true;
	}

	PipelineLog Run(String FilePathAndName,String FileName) throws IOException, ParseException{
		Timer timer = new Timer();
		 new RunComparison().CheckDirAndFile(FileName);
		
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
			 RunningParameter.PhenixAutobuild,
	"data=",mtzin,
	"seq_file=",seqin,
	"input_labels="," FP SIGFP PHIB FOM HLA HLB HLC HLD FreeR_flag", 
	
	


	"clean_up=","True"
	 };
	 if(RunningParameter.UsingRFree.equals("F")) {
		 String[]callAndArgsNoRfree= {
				 RunningParameter.PhenixAutobuild,
		"data=",mtzin,
		"seq_file=",seqin,
		"input_labels="," FP SIGFP PHIB FOM HLA HLB HLC HLD", 
		
		
		
		"clean_up=","True"
		 };
		 callAndArgs= callAndArgsNoRfree;
	 }
	 
	 if(RunningParameter.DensityModifiedPhenix.equals("T")) {
		 List<String> a = new ArrayList<String>();
		 a.addAll(Arrays.asList(callAndArgs));
		 a.add("input_map_file=");
		 a.add(mtzin);
		 
		 a.add("map_file_is_density_modified=");
		 a.add("True");
		 
		 a.add("input_map_labels=");
		 a.add("FP hltofom.Phi_fom.phi hltofom.Phi_fom.fom");
		 
		 
		 String[] myArray = new String[a.size()];
		 a.toArray(myArray);
		 callAndArgs=myArray;
		 System.out.println(Arrays.toString(callAndArgs));
	 }
	 if(RunningParameter.UseInitialModels.equals("T")) {
		 List<String> a = new ArrayList<String>();
		 a.addAll(Arrays.asList(callAndArgs));
		 a.add("model=");
		 if(new FilesManagements().GetModelPath(FileName+".pdb").equals("")) {
				res.LogFile+="model not found!!";
				return res;
		}
		 else {
			 a.add(new FilesManagements().GetModelPath(FileName+".pdb")); 
		 }
		 String[] myArray = new String[a.size()];
		 a.toArray(myArray);
		 callAndArgs=myArray;
	 }
	
	 if(RunningParameter.UsingPhenixRebuild_in_place.equals("T") || RunningParameter.UsingPhenixRebuild_in_place.equals("F")) {
		
		 List<String> a = new ArrayList<String>();
		 a.addAll(Arrays.asList(callAndArgs));
		 a.add("rebuild_in_place=");
		 a.add(RunningParameter.PhenixRebuild_in_place);
		 String[] myArray = new String[a.size()];
		 a.toArray(myArray);
		 callAndArgs=myArray;
	 }
	 
	 if(RunningParameter.MR.equals("T") && RunningParameter.UsingRFree.equals("T")) {
		 String[]callAndArgs1= {
				 RunningParameter.PhenixAutobuild,
		"data=",mtzin,
		"seq_file=",seqin,
		"input_labels=","FP SIGFP None None None None None None FREE", 
		
		"model=",FilePathAndName+".pdb",
		//"use_hl_if_present=","False",


		"clean_up=","True"
		
		
		 };
		 
		 
		 callAndArgs=callAndArgs1;
		 
		 String semet=new JSONReader().JSONToHashMap(FilePathAndName+".json").get("semet");
			if(semet.toLowerCase().equals("true"))
			{
				List<String> a = new ArrayList<String>();
				 a.addAll(Arrays.asList(callAndArgs));
				 a.add("semet=");
				 a.add("True");
				 String[] myArray = new String[a.size()];
				 a.toArray(myArray);
				 callAndArgs=myArray;
			}
			
			if(RunningParameter.UsingPhenixRebuild_in_place.equals("T") || RunningParameter.UsingPhenixRebuild_in_place.equals("F")) {
				
				 List<String> a = new ArrayList<String>();
				 a.addAll(Arrays.asList(callAndArgs));
				 a.add("rebuild_in_place=");
				 a.add(RunningParameter.PhenixRebuild_in_place);
				 String[] myArray = new String[a.size()];
				 a.toArray(myArray);
				 callAndArgs=myArray;
			 }
			
			if(RunningParameter.PhenixCluster.equals("T")) {
				//"check_run_command=","True",
				//"run_command=","sbatch",
				//"nproc=","Auto",
				//"background=","False",
				//"queue_commands=","#SBATCH --time=48:00:00",
				//"queue_commands=","#SBATCH --mem=5000",
				//"queue_commands=","#SBATCH --account=CS-MPMSEDM-2018",
				 List<String> a = new ArrayList<String>();
				 a.addAll(Arrays.asList(callAndArgs));
				 a.add("run_command=");
				 a.add("sbatch");
				 a.add("nproc=");
				 a.add("Auto");
				 a.add("background=");
				 a.add("False");
				 a.add("queue_commands=");
				 a.add("#SBATCH --time=48:00:00");
				 a.add("queue_commands=");
				 a.add("#SBATCH --mem=20000");
				 a.add("queue_commands=");
				 a.add("#SBATCH --account=CS-MPMSEDM-2018");
				 String[] myArray = new String[a.size()];
				 a.toArray(myArray);
				 callAndArgs=myArray;
			}
			
	 }
	 new Preparer().WriteTxtFile("ParametersUsed/"+FileName+".txt", new Date().toString()+" \n "+ Arrays.toString(callAndArgs));
	
	
	 Process p=Runtime.getRuntime().exec(callAndArgs,
		        null, new File(FileName));
		             

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
		            	 
	                // if(st.contains("Resolution range:")){
	                	
	                 // Resolution=st;
		              
	                // }
	                // if(st.contains("After refmac, R =")){
		
	               //Rfactor=st; 
	   	              
	                    //}
	                 if(st.contains("Working directory:") && Working_directory.equals("")){
	                	 Working_directory=st.split(":")[st.split(":").length-1].trim();
	                	 
	                	 System.out.println("-Working_directory "+Working_directory);
	                	 WriteWorkingDirToTxtFile(FileName,Working_directory);
	                	 timer( Working_directory ,  FileName, timer );
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
		            
	         		res.Roslution=Resolution;// Not Used 
	         		res.Rfactor=Rfactor; // Not Used 
	         		res.TimeTaking=TimeTaking;
	         		res.ProcessStatus="Success";
	         		
	         		File f = new File("./PhenixResults/PDBs/"+FileName+".pdb");
	       		 if(f.exists() && !f.isDirectory()) {  // rare to happen 
	       		     System.out.println("Exists");
	       		  FileUtils.copyFile(new File(Working_directory+"/overall_best.pdb"),  new File("./PhenixResults/PDBs/"+FileName+".pdb"+new java.util.Date().toString()));

		         	
		       	
	       		 }
	       		 else {
	       			 System.out.println("NotExists");
	       			if(res.LogFile.contains("Done cleaning up")) // it's the last line in log file when the building is succeed which means Phenix done removing temp files  
	       			FileUtils.copyFile(new File(Working_directory+"/overall_best.pdb"),  new File("./PhenixResults/PDBs/"+FileName+".pdb"));

		         	
		       	
	       		 }		
	         		
	         	
	         		
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
		         
		 if(RunningParameter.MR.equals("F")){// we do not want to use these fixes when we test MR
		 if(RunningParameter.UsingPhenixRebuild_in_place.equals("None")) {// Only use this when PhenixAutoBuild set to default        
		 if(RunningParameter.UseInitialModels.equals("T")&& res.LogFile.contains("the segment")&&res.LogFile.contains("could not be matched")) {
			 System.out.println("Now, try with Rebuild_in_place set false");
			 RunningParameter.UsingPhenixRebuild_in_place="F";
			 RunningParameter.PhenixRebuild_in_place="False"; 
			 FileUtils.deleteDirectory(new File (FileName));
			 return Run(FilePathAndName,FileName);
		 }
		 if(RunningParameter.UseInitialModels.equals("T")&& res.LogFile.contains("the PDB file and sequence file could not be aligned")) {
			 System.out.println("Now, try with Rebuild_in_place set false because the PDB file and sequence file could not be aligned");
			 RunningParameter.UsingPhenixRebuild_in_place="F";
			 RunningParameter.PhenixRebuild_in_place="False"; 
			 FileUtils.deleteDirectory(new File (FileName));
			 return Run(FilePathAndName,FileName);
		 }
		 if(RunningParameter.UseInitialModels.equals("T")&& res.LogFile.contains("This can be caused by 'TER' records separating parts of a chain")) {
			 System.out.println("Now, try with removing TER and re-order chains ");
			 FileUtils.deleteDirectory(new File (FileName));
			 new  Coord_format().RunCoord_format(new FilesManagements().GetModelPath(FileName+".pdb"),true,false);// it will remove TER even it set to false. False here just to avoid infinite recursion
			 RunningParameter.InitialModels="./ModifiedPDB"; // set the path to current folder because Coord_format will write the new PDB in main folder   
			 return Run(FilePathAndName,FileName);
		 }
	}
	}
		 if(RunningParameter.MR.equals("T")) {// some models have mismatch that rebuild can not be used 
			 if(RunningParameter.UsingPhenixRebuild_in_place.equals("None")) {
				 if(res.LogFile.contains("the segment")&&res.LogFile.contains("could not be matched")) {
					 System.out.println("Now, try with Rebuild_in_place set false");
					 RunningParameter.UsingPhenixRebuild_in_place="F";
					 RunningParameter.PhenixRebuild_in_place="False"; 
					 FileUtils.deleteDirectory(new File (FileName));
					 return Run(FilePathAndName,FileName);
				 } 
				 if(res.LogFile.contains("the PDB file and sequence file could not be aligned")) {
					 System.out.println("Now, try with Rebuild_in_place set false because the PDB file and sequence file could not be aligned");
					 RunningParameter.UsingPhenixRebuild_in_place="F";
					 RunningParameter.PhenixRebuild_in_place="False"; 
					 FileUtils.deleteDirectory(new File (FileName));
					 return Run(FilePathAndName,FileName);
				 }
			 }
		 }
		 return res;
	}
	
	static Vector<String> AddFileNameToList( Vector<String> FilesNames) throws IOException{
		File yourFile = new File("./ProcessedFilesNamesPhenix.txt");
		yourFile.createNewFile();
		 String FileNamesTxt=new FilesManagements().readFileAsString("./ProcessedFilesNamesPhenix.txt");
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
