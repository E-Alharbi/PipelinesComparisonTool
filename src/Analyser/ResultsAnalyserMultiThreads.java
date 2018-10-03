package Analyser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Thread.State;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Run.RunningPram;
import ToolsExecation.SingleThread.Castat2Data;
import ToolsExecation.SingleThread.CphasesMatch;
import ToolsExecation.SingleThread.MolProbity;
import ToolsExecation.SingleThread.Refmac;
import ToolsExecation.SingleThread.castat2;
import ToolsExecution.RunBuccaneerMulti;
import Utilities.DataSetChecking;
import Utilities.FilesManagements;
import table.draw.LogFile;
import java.util.Stack;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class ResultsAnalyserMultiThreads implements Runnable {

	// This the main class for the analyser that analysis the the tools outputs and creates an excel file for each tool. 
	// Using multi  threads 
	// ideal number of threads is 20 threads with minimum memory 10G. If you want to reduce memory also reduce the number of threads 
	
	
	
	static Vector <DataContainer> Container = new Vector <DataContainer>();
	
	 static Stack<File> Files = new Stack<>();
	public synchronized void AddRowToContainer(DataContainer C ) {
		Container.addElement(C);
	}
	public synchronized File GetFile() {
		
		System.out.println(" #### Unanalysed files:  "+ Files.size());
		return Files.pop();
	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		 
	        
		if(System.getenv("CCP4")==null){
			System.out.println("CCP4 installation cannot be found! if you instaled ccp4, run the setup script first. (setup script canbe found in ccp4/setup-scripts)");
			System.exit(-1);
		}
		if(args.length < 5){
			System.out.println("Error: one or more inputs is missing");
			System.out.println("Data folder path ");
			System.out.println("logs files path");
			System.out.println("PDBS folder path that built by the tool");
			System.out.println("castat2 path");
			System.out.println("CphasesMatch Script Path in CCP4 folder");
			System.out.println("Refmac Path Script Path in CCP4 folder");
			System.out.println("Number of threads");
			System.out.println("The tool name");
			System.exit(-1);
		}
		RunningPram.DataPath=args[0];//Data folder path 
		RunningPram.LogsDir=args[1];// logs files for Buccaneer
		RunningPram.PDBsDir=args[2];// PDBS files that built by Buccaneer
		RunningPram.castat2Path=args[3];// castat2 path
		RunningPram.CphasesMatchScriptPath=args[4];//CphasesMatch Script Path in CCP4 folder
		RunningPram.RefmacPath=args[5];	
   	int NumberpfThreads=Integer.valueOf(args[6]);
   	RunningPram.ToolName=args[7];	
	  
   	Analyses();
	}
	public static void Analyses() throws FileNotFoundException, IOException {
		
    ResultsAnalyserMultiThreads Analyser = new ResultsAnalyserMultiThreads();
  	int NumberpfThreads= Integer.valueOf(RunningPram.NumberofThreads);
		
		String LogsDir=RunningPram.LogsDir;// logs files 
		 File[] files = new File(LogsDir).listFiles();
		 for (File file : files) {
			 Analyser.Files.push(file); 
		 }
		 
		 System.out.println("The paramters will be used by the Analyser: ");
		 System.out.println("Pipeline= "+ RunningPram.ToolName);
		 System.out.println("Data Path= "+RunningPram.DataPath);
		 System.out.println("Logs Folder Path= "+RunningPram.LogsDir);
		 System.out.println("PDB Folder Path= "+RunningPram.PDBsDir);
		 System.out.println("Refmac= "+RunningPram.RefmacPath);
		 System.out.println("Cphases Match Path= "+ RunningPram.CphasesMatchScriptPath);
		 System.out.println("Phases used in CPhasesMatch= "+  RunningPram.PhasesUsedCPhasesMatch);
		 System.out.println("Using MolProbity?= "+  RunningPram.UsingMolProbity);
		 if(RunningPram.UsingMolProbity.equals("T"))
		 System.out.println("MolProbity Path= "+  RunningPram.PhenixMolProbity);
		 
	     //System.out.println(Thread.activeCount()); 
	     int ThreadNumber=0;
	     while (Files.size()!=0) {
	    	 if(Thread.activeCount() < NumberpfThreads ) {
	    		 Thread t1 = new Thread(new ResultsAnalyserMultiThreads(), "Thread "+String.valueOf(ThreadNumber));
	    	     
	    	     t1.start();
	    	     ++ThreadNumber;
	    	    
	    	 }
	     }
	     while (Files.size()==0) {
	     	 if(Thread.activeCount() == 1 ) {
	     		 System.out.println("Creating The excel file");
	     		Container=new DataSetChecking().CheckIfAllDataSetHasProcessed(Container , RunningPram.LogsDir,RunningPram.DataPath);
	     		CreateExcel();
	     		break;
	     	 }
	     }
	}
	static public synchronized void  CreateExcel() throws FileNotFoundException, IOException {
		
		new ExcelSheet().FillInExcel(Container, RunningPram.ToolName);
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	//System.out.println(Thread.currentThread().getName() + " START");

	try {
		if(!Files.isEmpty())
			AnalysingResults(GetFile());
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	}
	
	public void AnalysingResults(File file) throws IOException
	{
		
		//System.out.println(Thread.currentThread().getName()+" Proccessing "+file.getAbsolutePath());
			
		
	
		String LogsDir=RunningPram.LogsDir;// 
		String PDBsDir=RunningPram.PDBsDir;//

		
		
		String NameOfFile=file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),"");
		
				DataContainer DC = new DataContainer();
				DC.PDB_ID=NameOfFile;
				DC.PDBIDTXT=NameOfFile.substring(0, 4);
				File PDB=null  ;
				
				
				if(new File(PDBsDir+"/"+NameOfFile+".pdb").exists()) {
					PDB=new File(PDBsDir+"/"+NameOfFile+".pdb");
				}
				else {
					if(new File(RunningPram.IntermediatePDBs+"/"+NameOfFile+".pdb").exists()) {
						PDB=new File(RunningPram.IntermediatePDBs+"/"+NameOfFile+".pdb");
						DC.Intermediate="T";
					}
				}
				if(PDB==null) {// In case no pdb is built and we need the reso in the excel
		
String [] FandE=new CphasesMatch().cphasesmatch(RunningPram.DataPath+"/"+DC.PDB_ID,RunningPram.CphasesMatchScriptPath);
DC.F_mapCorrelation=FandE[0];
DC.E_mapCorrelation=FandE[1];
}
				
				
				
				Parse(DC,new File(LogsDir+"/"+NameOfFile+".txt"),PDB);
				
				// removing  refmac output to save storage 
				if(new File(DC.PDB_ID+"Ref.mtz").exists()) {
				 FileUtils.deleteQuietly(new File(DC.PDB_ID+"Ref.mtz")); 
				 
				}
				if(new File(DC.PDB_ID+"Ref.pdb").exists()) {
					FileUtils.deleteQuietly(new File(DC.PDB_ID+"Ref.pdb"));
				}

	Container.add(DC);
	CreateExcel();
	}
 public void Parse (DataContainer DC, File Log , File PDB) throws IOException {
	
	 List<String> headersList = Arrays.asList("Tool Name", "File Name", "File Num", "Current Step", "Current Step Output");
	
		String LogTxt=new ResultsAnalyserMultiThreads().readFileAsString(Log.getAbsolutePath());

		String RFactor="Not Found";
		String RFree="Not Found";
		String TimeTakig="-1";
		
		DC.ExceptionNoLogFile="F";
		
		DC.TimeTaking=TimeTakig;
		if(LogTxt.contains("TimeTaking")){
			
			TimeTakig=LogTxt.substring(LogTxt.lastIndexOf("TimeTaking")).split(" ")[1].replaceAll("[^0-9\\.]+", "").trim();
			
			DC.TimeTaking=TimeTakig;
if(Double.valueOf(TimeTakig) < 5) {
	DC.WarringTimeTaking="T";
}
else {
	DC.WarringTimeTaking="F";
}
}
if(Log.length() < 5000) {
DC.WarringLogFile="T";

}
else {
DC.WarringLogFile="F";
}

		if(PDB!=null){
			String Line="";
			new LogFile().Log(RunningPram.ToolName, Log.getName(), Thread.currentThread().getName()+" out of "+Files.size(), "Parsing both R and Rfree from log file ", "Running ...",headersList);

			if(RunningPram.ToolName.equals("Buccaneer") || RunningPram.ToolName.equals("Buccaneeri2") || RunningPram.ToolName.equals("Buccaneeri2W")) {
			
				for(int s=0 ; s<LogTxt.length();++s){
				Line+=LogTxt.charAt(s);
				
				if(LogTxt.charAt(s)=='\n'){
					
					if(Line.contains("R factor")){
						
						RFactor=Line.split(" ")[Line.split(" ").length-1];
						
						Line="";
					}
					if(Line.contains("R free")){
						RFree=Line.split(" ")[Line.split(" ").length-1];
						Line="";
					}
				}
			}
			} 
			if(RunningPram.ToolName.equals("ARPwARP") || RunningPram.ToolName.equals("ARPwARPAfterBuccaneeri1")) {
				
				for(int s=0 ; s<LogTxt.length();++s){
					Line+=LogTxt.charAt(s);
					if(LogTxt.charAt(s)=='\n'){
						if(Line.contains("R =")){
							RFactor=Line;
							
						}
						if(Line.contains("Rfree =")){
							RFree=Line;
							
						}
						Line="";
					}
				}
				if(!RFactor.equals("Not Found") && !RFree.equals("Not Found")) {
				RFactor=RFactor.substring(RFactor.indexOf("R ="));
				RFactor=RFactor.substring(3,RFactor.indexOf("(")).trim();
				
				RFree=RFree.substring(RFree.indexOf("Rfree ="));
				
				RFree=RFree.substring(7,RFree.indexOf(")")).trim();
				}
			}
			if(RunningPram.ToolName.equals("Phenix")) {
				for(int s=0 ; s<LogTxt.length();++s){
					Line+=LogTxt.charAt(s);
					if(LogTxt.charAt(s)=='\n'){
						if(Line.contains("Best solution on cycle:")){
							RFactor=Line.split("=")[Line.split("=").length-1].trim();
							String [] RFandRFree=RFactor.split("/");
							RFactor=RFandRFree[0];
							RFree=RFandRFree[1];
							Line="";
						}
						
					}
				}
			}
			if(RunningPram.ToolName.equals("Crank")) {
				for(int s=0 ; s<LogTxt.length();++s){
					Line+=LogTxt.charAt(s);
					if(LogTxt.charAt(s)=='\n'){
						if(Line.contains("R factor after refinement is")){	
							RFactor=Line.replaceAll("[^0-9\\.]+", "");
							
						}
						if(Line.contains("R-free factor after refinement is")){	
							RFree=Line.replaceAll("[^0-9\\.]+", "");
							
						}
						Line="";
					}
				}
			}
			
			DecimalFormat df = new DecimalFormat("#.##");
			if(!RFactor.equals("Not Found") && !RFree.equals("Not Found")){
				
			new LogFile().Log(RunningPram.ToolName, Log.getName(), Thread.currentThread().getName()+" out of "+Files.size(), "Parsing both R and Rfree from log file ", "RFactor "+RFactor + " RFree "+RFree,headersList);

				
			
			
			df.setRoundingMode(RoundingMode.CEILING);
			Double OverfiitingPercentage=0.0;
			RFactor = df.format(Double.valueOf(RFactor));
			RFree = df.format(Double.valueOf(RFree));
			
			df.setRoundingMode(RoundingMode.HALF_DOWN);
			if(Double.parseDouble(RFactor)>Double.parseDouble(RFree)){
			
				OverfiitingPercentage=(Double.parseDouble(RFactor)- Double.parseDouble(RFree));
				
			}
			else{
				OverfiitingPercentage=(Double.parseDouble(RFree)- Double.parseDouble(RFactor));
			}
			
			//System.out.println("OverfiitingPercentage "+OverfiitingPercentage);
			
			DC.R_factorΔR_free=String.valueOf(df.format(Double.valueOf(OverfiitingPercentage)));
			
			df.setRoundingMode(RoundingMode.CEILING);
			DC.R_factor=df.format(Double.valueOf(RFactor));
			
			DC.R_free=df.format(Double.valueOf(RFree));
			//df = new DecimalFormat("#.##");
         //df.setRoundingMode(RoundingMode.CEILING);
             
			
			
			DC.Overfitting=String.valueOf((OverfiitingPercentage>0.05)?"T":"F");
			}
			new LogFile().Log(RunningPram.ToolName, Log.getName(), Thread.currentThread().getName()+" out of "+Files.size(), "Run Refmac 0 cycle ", "Running ...",headersList);

			
			
			Factors F = new Refmac().RunRefmac(RunningPram.DataPath+"/"+DC.PDB_ID+".mtz", PDB.getAbsolutePath(), RunningPram.RefmacPath, RunningPram.ToolName, DC.PDB_ID,"");
			
			if(!F.RFactor.equals("None")) {
			DC.R_factor0Cycle=df.format(Double.valueOf(F.RFactor));
			DC.Resolution=F.Reso;
			}
			else
				DC.R_factor0Cycle="None";
			
			if(!F.FreeFactor.equals("None"))
			DC.R_free0Cycle=df.format(Double.valueOf(F.FreeFactor));
			else
			DC.R_free0Cycle=	"None";
			
			
			
			new LogFile().Log(RunningPram.ToolName, Log.getName(), Thread.currentThread().getName()+" out of "+Files.size(), "Run Refmac 0 cycle ", F.RFactor +" "+F.FreeFactor,headersList);

			new LogFile().Log(RunningPram.ToolName, Log.getName(), Thread.currentThread().getName()+" out of "+Files.size(), "Run Refmac (well known) ", "Running ...",headersList);

			F = new Refmac().RunRefmac(RunningPram.DataPath+"/"+DC.PDB_ID+".mtz",RunningPram.DataPath+"/"+DC.PDB_ID+".pdb", RunningPram.RefmacPath, RunningPram.ToolName, DC.PDB_ID,"");
			 
			DC.OptimalR_factor=df.format(Double.valueOf(F.RFactor));
			
			new LogFile().Log(RunningPram.ToolName, Log.getName(), Thread.currentThread().getName()+" out of "+Files.size(), "Run Refmac (well know) ", F.RFactor +" "+F.FreeFactor,headersList);
			//DC.OptimalR_factor="0";
			Castat2Data Cas =  new castat2().Runcastat2(RunningPram.DataPath+"/"+DC.PDB_ID+".pdb", PDB.getAbsolutePath(), RunningPram.castat2Path);
			new LogFile().Log(RunningPram.ToolName, Log.getName(), Thread.currentThread().getName()+" out of "+Files.size(), "castat2 ", "Running ...",headersList);
			new LogFile().Log(RunningPram.ToolName, Log.getName(), Thread.currentThread().getName()+" out of "+Files.size(), "castat2 ", "Done" ,headersList);

			DC.NumberofAtomsinFirstPDB=Cas.NumberOfAtomsInFirstPDB;
		
			DC.NumberofAtomsinSecondPDB=Cas.NumberOfAtomsInSecondPDB;
			
			DC.NumberOfAtomsInFirstNotInSecond=Cas.NumberOfAtomsInFirstNotInSecond;
	
			DC.NumberOfAtomsInSecondNotInFirst=Cas.NumberOfAtomsInSecondNotInFirst;
		
			DC.Seqrn1n2n2n1=Cas.Seq;
		
			DC.n1m2=Cas.n1m2;
			
			DC.n2m1=Cas.n2m1;
			if(!Cas.n1m2.equals("None"))
			DC.Completeness= df.format(((Integer.parseInt(Cas.n1m2) * 100)/Integer.parseInt(Cas.NumberOfAtomsInFirstPDB)));
			else
		    DC.Completeness=	"None";
			new LogFile().Log(RunningPram.ToolName, Log.getName(), Thread.currentThread().getName()+" out of "+Files.size(), "cphasesmatch  ", "Running... ",headersList);

			 String [] FandE=new CphasesMatch().cphasesmatch(RunningPram.DataPath+"/"+DC.PDB_ID,RunningPram.CphasesMatchScriptPath);
			 DC.F_mapCorrelation=FandE[0];
			 DC.E_mapCorrelation=FandE[1];
			 
			 DC.BuiltPDB="T";
			
			new LogFile().Log(RunningPram.ToolName, Log.getName(), Thread.currentThread().getName()+" out of "+Files.size(), "cphasesmatch F and E map  ", FandE[0] +" "+ FandE[1],headersList);
			new LogFile().Log(RunningPram.ToolName, Log.getName(), Thread.currentThread().getName()+" out of "+Files.size(), "Phases Used in CPhasesMatch  ", RunningPram.PhasesUsedCPhasesMatch,headersList );

			if(RunningPram.UsingMolProbity.equals("T")) {
			DC.molProbityData=new MolProbity().molProbity(PDB,new File(RunningPram.DataPath+"/"+DC.PDB_ID+".mtz"));
			new LogFile().Log(RunningPram.ToolName, Log.getName(), Thread.currentThread().getName()+" out of "+Files.size(), "MolProbity  ", "Running... ",headersList);
			new LogFile().Log(RunningPram.ToolName, Log.getName(), Thread.currentThread().getName()+" out of "+Files.size(), "MolProbity ", "Done ",headersList );

			}
		//else {
		//	DC.BuiltPDB="F";
		//}
		

		}
		else{
			System.out.println("No PDB File");
//new RunBuccaneerMulti().WriteFileNameToList(file.getName().substring(0,file.getName().indexOf('.')),"./BuccaneerResultsAnalyserReport.txt");
			DC.BuiltPDB="F";
			
		}
		
		if(DC.Resolution.equals("None")) { // it is rare to happen, but if might occurred when there is built PDB and refmac throw and error 
		Factors F = new Refmac().RunRefmac(RunningPram.DataPath+"/"+DC.PDB_ID+".mtz", RunningPram.DataPath+"/"+DC.PDB_ID+".pdb", RunningPram.RefmacPath, RunningPram.ToolName, DC.PDB_ID,"");
		DC.Resolution=F.Reso;	
		}
		
 }
	public String readFileAsString(String filePath) throws IOException {
        StringBuffer fileData = new StringBuffer();
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }
	
}
