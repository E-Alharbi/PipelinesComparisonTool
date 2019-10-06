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

import Comparison.Analyser.ExcelSheet;
import Comparison.Analyser.PipelineLog;
import Comparison.Runner.RunComparison;
import Comparison.Runner.RunningParameter;
import NotUsed.ARPResultsAnalysis;

public class RunPhenixHLA {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		
		if(args.length<2){
			System.out.println("Error: No Inputs! ");
			System.out.println("You have to set the path for your data folder and Phenix.autobuild");
			System.exit(-1);
		}
		
		RunningParameter.DataPath=args[0];
		RunningParameter.PhenixAutobuild=args[1];
		
		new RunPhenixHLA().RunPhenixTool();
	}
	public void RunPhenixTool() throws IOException
	{
		String PATHLogs = "./PhenixResultsHLA/PhinexLogs";// LogsFilesPath
		new RunComparison().CheckDirAndFile("PhenixResultsHLA");
		new RunComparison().CheckDirAndFile(PATHLogs);
		new RunComparison().CheckDirAndFile("./PhenixResultsHLA/PDBs");
		new RunComparison().CheckDirAndFile("./PhenixResultsHLA/WorkingDir");
	
		 Vector<String> FilesNames= new Vector <String>();
		File[] processedfiles = new File(PATHLogs).listFiles();
			 for (File file : processedfiles) {
				 FilesNames.add(file.getName().substring(0,file.getName().indexOf('.')));
				 System.out.println(file.getName().substring(0,file.getName().indexOf('.')));
			 }
		 
		
		
	
   
    
	File[] files = new File(RunningParameter.DataPath).listFiles();
     FilesNames=AddFileNameToList(FilesNames);
		 for (File file : files) {
	if(!FilesNames.contains(file.getName().substring(0,file.getName().indexOf('.')))){
		System.out.print("File Name "+file.getName().substring(0,file.getName().indexOf('.')));
		WriteFileNameToList(file.getName().substring(0,file.getName().indexOf('.')));
    String FileName=file.getParentFile()+"/"+file.getName().substring(0,file.getName().indexOf('.'));
	
	FilesNames.add(file.getName().substring(0,file.getName().indexOf('.')));
   System.out.println(file.getName().substring(0,file.getName().indexOf('.')));
   PipelineLog res= new RunPhenixHLA().Run(FileName,file.getName().substring(0,file.getName().indexOf('.')));
	
	
	
   
	try(  PrintWriter out = new PrintWriter( PATHLogs+"/"+file.getName().substring(0,file.getName().indexOf('.'))+".txt" )  ){
	    out.println( res.LogFile );
	}
	FilesNames=AddFileNameToList(FilesNames);
	}
	
}
		
		
	}

	PipelineLog Run(String FilePathAndName,String FileName){
		 String st = null;
		 Date ProStartTime = new java.util.Date();
		 PipelineLog res= new PipelineLog();
		 
		         try {
	
String mtzin=FilePathAndName+".mtz";
String seqin=FilePathAndName+".seq";
//String[]callAndArgss= {"source","/Applications/ccp4-7.0/setup-scripts/ccp4.setup-sh"};
//Process pp = Runtime.getRuntime().exec(callAndArgss);

//"/Applications/phenix-1.12-2829/build/bin/phenix.autobuild"
String labels=" FP SIGFP PHIB FOM parrot.ABCD.A parrot.ABCD.B parrot.ABCD.C parrot.ABCD.D FreeR_flag";

	 String[]callAndArgs= {
			 RunningParameter.PhenixAutobuild,
	"data=",mtzin,
	"seq_file=",seqin,
	"input_labels="," FP SIGFP PHIB FOM HLA HLB HLC HLD"
	};
	 
	 Process p = Runtime.getRuntime().exec(callAndArgs);

		             

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
		            	 
	                 if(st.contains("Resolution range:")){
	                	
	                  Resolution=st;
		              
	                 }
	                 if(st.contains("After refmac, R =")){
		
	               Rfactor=st;
	   	              
	                    }
	                 if(st.contains("Working directory:") && Working_directory.equals("")){
	                	 Working_directory=st.split(":")[st.split(":").length-1].trim();
	                	 System.out.println("-Working_directory "+Working_directory);
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
		             // read any errors
	         		res.Roslution=Resolution;
	         		res.Rfactor=Rfactor;
	         		res.TimeTaking=TimeTaking;
	         		res.ProcessStatus="Success";
	         		
	         	FileUtils.copyFile(new File(Working_directory+"/overall_best.pdb"),  new File("./PhenixResultsHLA/PDBs/"+FileName+".pdb"));

	         	FileUtils.copyDirectoryToDirectory(new File(Working_directory),new File( System.getProperty("user.dir")+"/PhenixResultsHLA/WorkingDir/"+FileName));
	       		FileUtils.deleteDirectory(new File(System.getProperty("user.dir")+"/"+Working_directory));
	       	
	         		
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
	
	static Vector<String> AddFileNameToList( Vector<String> FilesNames) throws IOException{
		File yourFile = new File("./ProcessedFilesNamesPhenixHLA.txt");
		yourFile.createNewFile();
		 String FileNamesTxt=new ARPResultsAnalysis().readFileAsString("./ProcessedFilesNamesPhenixHLA.txt");
		 FilesNames.addAll(Arrays.asList(FileNamesTxt.split("\n")));
		 return FilesNames;
		
	}
	static void WriteFileNameToList(String Name) throws IOException{
		File yourFile = new File("./ProcessedFilesNamesPhenixHLA.txt");
		yourFile.createNewFile();
		 BufferedWriter output;
		 output = new BufferedWriter(new FileWriter("./ProcessedFilesNamesPhenixHLA.txt", true));
		 output.append(Name+"\n");
		 output.close();
	}
}
