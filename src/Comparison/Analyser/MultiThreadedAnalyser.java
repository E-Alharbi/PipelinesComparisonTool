package Comparison.Analyser;
/**
*
* @author Emad Alharbi
* University of York
*/
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Thread.State;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Comparison.Runner.RunComparison;
import Comparison.Runner.RunningParameter;
import Comparison.ToolsExecation.SingleThread.Castat2Data;
import Comparison.ToolsExecation.SingleThread.CphasesMatch;
import Comparison.ToolsExecation.SingleThread.MolProbity;
import Comparison.ToolsExecation.SingleThread.Refmac;
import Comparison.ToolsExecation.SingleThread.castat2;
import Comparison.Utilities.DataSetChecking;
import Comparison.Utilities.FilesManagements;
import Comparison.Utilities.RemovingWaterChainID;
import ToolsExecution.RunBuccaneerMulti;
import table.draw.LogFile;
import java.util.Stack;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class MultiThreadedAnalyser implements Runnable {

	// This the main class for the analyser that analysis the the tools outputs and creates an excel file for each tool. 
	// Using multi  threads 
	// ideal number of threads is 20 threads with minimum memory 10G. If you want to reduce memory also reduce the number of threads 
	
	
	
	static Vector <ExcelContents> Container = new Vector <ExcelContents>();
	
	 static Stack<File> Files = new Stack<>();
	public synchronized void AddRowToContainer(ExcelContents C ) {
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
		RunningParameter.DataPath=args[0];//Data folder path 
		RunningParameter.LogsDir=args[1];// logs files for Buccaneer
		RunningParameter.PDBsDir=args[2];// PDBS files that built by Buccaneer
		RunningParameter.castat2Path=args[3];// castat2 path
		RunningParameter.CphasesMatchScriptPath=args[4];//CphasesMatch Script Path in CCP4 folder
		RunningParameter.RefmacPath=args[5];	
   	int NumberpfThreads=Integer.valueOf(args[6]);
   	RunningParameter.ToolName=args[7];	
	  
   	Analyses();
	}
	public static void Analyses() throws FileNotFoundException, IOException {
		
    MultiThreadedAnalyser Analyser = new MultiThreadedAnalyser();
  	int NumberpfThreads= Integer.valueOf(RunningParameter.NumberofThreads);
		
		String LogsDir=RunningParameter.LogsDir;// logs files 
		 File[] files = new File(LogsDir).listFiles();
		 for (File file : files) {
			 Analyser.Files.push(file); 
		 }
		 	
		 if(new File(RunningParameter.ToolName+".xlsx").exists()) {
			 System.out.println("Found an excel file ");
			 System.out.println("Excluding the proccseed files based on the excel");
			 ExcelLoader e = new ExcelLoader();
			 Vector<ExcelContents> ProccseedFilesContainer = e.ReadExcel(RunningParameter.ToolName+".xlsx");
			 Vector<ExcelContents> FilesNotNeedToAnalysedContainer=new Vector<ExcelContents>();
			for(int i=0 ;i < ProccseedFilesContainer.size() ; ++i) {
				if(ProccseedFilesContainer.get(i).Intermediate.equals("F") && ProccseedFilesContainer.get(i).BuiltPDB.equals("T")) {
				for(int f=0 ; f <  Analyser.Files.size() ; ++f) {
			  
					String Log= Analyser.Files.get(f).getName().replaceAll("."+FilenameUtils.getExtension(Analyser.Files.get(f).getName()),"");
	               if(ProccseedFilesContainer.get(i).PDB_ID.equals(Log)) {
		            Analyser.Files.remove(Analyser.Files.get(f));
		            FilesNotNeedToAnalysedContainer.add(ProccseedFilesContainer.get(i));
	                  
	               }
				}
			}
			}
			if(FilesNotNeedToAnalysedContainer.size() > 0) {
			Container.addAll(FilesNotNeedToAnalysedContainer);
			}
		 }
		 
		 
		 
		 System.out.println("The paramters will be used by the Analyser: ");
		 System.out.println("Pipeline= "+ RunningParameter.ToolName);
		 System.out.println("Data Path= "+RunningParameter.DataPath);
		 System.out.println("Logs Folder Path= "+RunningParameter.LogsDir);
		 System.out.println("PDB Folder Path= "+RunningParameter.PDBsDir);
		 System.out.println("Refmac= "+RunningParameter.RefmacPath);
		 System.out.println("Cphases Match Path= "+ RunningParameter.CphasesMatchScriptPath);
		 System.out.println("Phases used in CPhasesMatch= "+  RunningParameter.PhasesUsedCPhasesMatch);
		 System.out.println("Using MolProbity?= "+  RunningParameter.UsingMolProbity);
		 if(RunningParameter.UsingMolProbity.equals("T"))
		 System.out.println("MolProbity Path= "+  RunningParameter.PhenixMolProbity);
		 
	     //System.out.println(Thread.activeCount()); 
	     int ThreadNumber=0;
	     while (Files.size()!=0) {
	    	 if(Thread.activeCount() < NumberpfThreads ) {
	    		 Thread t1 = new Thread(new MultiThreadedAnalyser(), "Thread "+String.valueOf(ThreadNumber));
	    	     
	    	     t1.start();
	    	     ++ThreadNumber;
	    	    
	    	 }
	     }
	     while (Files.size()==0) {
	     	 if(Thread.activeCount() == 1 ) {
	     		 System.out.println("Creating The excel file");
	     		Container=new DataSetChecking().CheckIfAllDataSetHasProcessed(Container , RunningParameter.LogsDir,RunningParameter.DataPath);
	     		CreateExcel();
	     		break;
	     	 }
	     }
	}
	static public synchronized void  CreateExcel() throws FileNotFoundException, IOException {
		
		new ExcelSheet().FillInExcel(Container, RunningParameter.ToolName);
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
			
		
	
		String LogsDir=RunningParameter.LogsDir;// 
		String PDBsDir=RunningParameter.PDBsDir;//

		
		
		String NameOfFile=file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),"");
		
				ExcelContents DC = new ExcelContents();
				DC.PDB_ID=NameOfFile;
				DC.PDBIDTXT=NameOfFile.substring(0, 4);
				File PDB=null  ;
				
				
				if(new File(PDBsDir+"/"+NameOfFile+".pdb").exists()) {
					PDB=new File(PDBsDir+"/"+NameOfFile+".pdb");
				}
				else {
					if(new File(RunningParameter.IntermediatePDBs+"/"+NameOfFile+".pdb").exists()) {
						PDB=new File(RunningParameter.IntermediatePDBs+"/"+NameOfFile+".pdb");
						DC.Intermediate="T";
					}
				}
				if(PDB==null) {// In case no pdb is built and we need the reso in the excel
		
String [] FandE=new CphasesMatch().cphasesmatch(RunningParameter.DataPath+"/"+DC.PDB_ID,RunningParameter.CphasesMatchScriptPath);
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
 public void Parse (ExcelContents DC, File Log , File PDB) throws IOException {
	
	 List<String> headersList = Arrays.asList("Tool Name", "File Name", "File Num", "Current Step", "Current Step Output");
	
		String LogTxt=new MultiThreadedAnalyser().readFileAsString(Log.getAbsolutePath());

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
			new LogFile().Log(RunningParameter.ToolName, Log.getName(), Thread.currentThread().getName()+" out of "+Files.size(), "Parsing both R and Rfree from log file ", "Running ...",headersList);

			if(RunningParameter.ToolName.equals("Buccaneeri1") || RunningParameter.ToolName.equals("Buccaneeri2") || RunningParameter.ToolName.equals("Buccaneeri2W") || RunningParameter.ToolName.equals("Buccaneeri1I5") ||  RunningParameter.ToolName.equals("Buccaneeri2I5") || RunningParameter.ToolName.equals("Buccaneeri2WI5")) {
			
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
			if(RunningParameter.ToolName.equals("ARPwARP") || RunningParameter.ToolName.equals("ArpWArpAfterBuccaneeri1") || RunningParameter.ToolName.equals("ArpWArpAfterBuccaneeri1I5")) {
				
				for(int s=0 ; s<LogTxt.length();++s){
					Line+=LogTxt.charAt(s);
					if(LogTxt.charAt(s)=='\n'){
						if(Line.contains(" R =")){
							RFactor=Line;
							
						}
						if(Line.contains("Rfree =")){
							RFree=Line;
							
						}
						Line="";
					}
				}
				if(!RFactor.equals("Not Found") && !RFree.equals("Not Found")) {
					
					System.out.println("RFactor: "+RFactor);
				RFactor=RFactor.substring(RFactor.indexOf(" R = "));
				
				//RFactor=RFactor.substring(3,RFactor.indexOf("(")).trim();
				System.out.println("RFactor: "+RFactor);
				RFactor=RFactor.split(" ")[3];
				System.out.println("RFactor: "+RFactor);
				
				RFree=RFree.substring(RFree.indexOf("Rfree = "));
				System.out.println("Rfree :"+RFree);
				RFree=RFree.split(" ")[2];
				RFree=RFree.replaceAll("\\)", "");
				System.out.println("Rfree :"+RFree);
				
				if(RFactor.trim().length()==0)// in very few cases when R work is empty ARP/wARP
					RFactor="Not Found";
				//RFree=RFree.substring(7,RFree.indexOf(")")).trim();
				}
			}
			if(RunningParameter.ToolName.equals("Phenix")) {
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
			if(RunningParameter.ToolName.equals("Crank")) {
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
				
			new LogFile().Log(RunningParameter.ToolName, Log.getName(), Thread.currentThread().getName()+" out of "+Files.size(), "Parsing both R and Rfree from log file ", "RFactor "+RFactor + " RFree "+RFree,headersList);

				
			
			
			df.setRoundingMode(RoundingMode.HALF_UP);
			Double OverfiitingPercentage=0.0;
			RFactor = df.format(BigDecimal.valueOf(Double.valueOf(RFactor)));
			RFree = df.format(BigDecimal.valueOf(Double.valueOf(RFree)));
			
			df.setRoundingMode(RoundingMode.HALF_UP);
			if(Double.parseDouble(RFactor)>Double.parseDouble(RFree)){
			
				OverfiitingPercentage=(Double.parseDouble(RFactor)- Double.parseDouble(RFree));
				
			}
			else{
				OverfiitingPercentage=(Double.parseDouble(RFree)- Double.parseDouble(RFactor));
			}
			
			//System.out.println("OverfiitingPercentage "+OverfiitingPercentage);
			
			DC.R_factorΔR_free=String.valueOf(df.format(BigDecimal.valueOf(Double.valueOf(OverfiitingPercentage))));
			
			df.setRoundingMode(RoundingMode.HALF_UP);
			DC.R_factor=df.format(BigDecimal.valueOf(Double.valueOf(RFactor)));
			
			DC.R_free=df.format(BigDecimal.valueOf(Double.valueOf(RFree)));
			
		  //df = new DecimalFormat("#.##");
         //df.setRoundingMode(RoundingMode.CEILING);
             
			
			
			DC.Overfitting=String.valueOf((OverfiitingPercentage>0.05)?"T":"F");
			}
			new LogFile().Log(RunningParameter.ToolName, Log.getName(), Thread.currentThread().getName()+" out of "+Files.size(), "Refmac 0 cycle ", "Running ...",headersList);

			
			
			REFMACFactors F = new Refmac().RunRefmac(RunningParameter.DataPath+"/"+DC.PDB_ID+".mtz", PDB.getAbsolutePath(), RunningParameter.RefmacPath, RunningParameter.ToolName, DC.PDB_ID,"");
			
			if(!F.RFactor.equals("None")) {
			DC.R_factor0Cycle=df.format(BigDecimal.valueOf(Double.valueOf(F.RFactor)));
			DC.Resolution=F.Reso;
			}
			else
				DC.R_factor0Cycle="None";
			
			if(!F.FreeFactor.equals("None"))
			DC.R_free0Cycle=df.format(BigDecimal.valueOf(Double.valueOf(F.FreeFactor)));
			else
			DC.R_free0Cycle=	"None";
			
			
			
			new LogFile().Log(RunningParameter.ToolName, Log.getName(), Thread.currentThread().getName()+" out of "+Files.size(), "Refmac 0 cycle ", F.RFactor +" "+F.FreeFactor,headersList);

			new LogFile().Log(RunningParameter.ToolName, Log.getName(), Thread.currentThread().getName()+" out of "+Files.size(), "Refmac (deposited) ", "Running ...",headersList);

			//F = new Refmac().RunRefmac(RunningPram.DataPath+"/"+DC.PDB_ID+".mtz",RunningPram.DataPath+"/"+DC.PDB_ID+".pdb", RunningPram.RefmacPath, RunningPram.ToolName, DC.PDB_ID,"");
			ParsingRFromDepoistedPDB(new File(RunningParameter.DataPath+"/"+DC.PDB_ID+".pdb"),DC);
			DC.OptimalR_factor=df.format(BigDecimal.valueOf(Double.valueOf(DC.OptimalR_factor)));
			
			new LogFile().Log(RunningParameter.ToolName, Log.getName(), Thread.currentThread().getName()+" out of "+Files.size(), "Refmac (deposited) ", DC.OptimalR_factor,headersList);
			//DC.OptimalR_factor="0";
			Castat2Data Cas =  new castat2().Runcastat2(RunningParameter.DataPath+"/"+DC.PDB_ID+".pdb", PDB.getAbsolutePath(), RunningParameter.castat2Path);
			new LogFile().Log(RunningParameter.ToolName, Log.getName(), Thread.currentThread().getName()+" out of "+Files.size(), "castat2 ", "Running ...",headersList);
			new LogFile().Log(RunningParameter.ToolName, Log.getName(), Thread.currentThread().getName()+" out of "+Files.size(), "castat2 ", "Done" ,headersList);

			
			if((RunningParameter.ToolName.equals("Buccaneeri2W") || RunningParameter.ToolName.equals("Buccaneeri2WI5"))  && Cas.n1m2.equals("None")) { // this occurs in very few cases when the water chain ID is Z and the PDB has many chains 
				new RunComparison().CheckDirAndFile("PDBsWithEmptyWaterChainID");
				FileUtils.copyToDirectory(PDB, new File("PDBsWithEmptyWaterChainID"));
				new RemovingWaterChainID().RemoveWaterChainID("PDBsWithEmptyWaterChainID"+"/"+PDB.getName());
				Cas =  new castat2().Runcastat2(RunningParameter.DataPath+"/"+DC.PDB_ID+".pdb", "PDBsWithEmptyWaterChainID"+"/"+PDB.getName(), RunningParameter.castat2Path);
				new LogFile().Log(RunningParameter.ToolName, Log.getName(), Thread.currentThread().getName()+" out of "+Files.size(), "castat2 Buccaneeri2W", "Running ...",headersList);
				new LogFile().Log(RunningParameter.ToolName, Log.getName(), Thread.currentThread().getName()+" out of "+Files.size(), "castat2 Buccaneeri2W", "Done" ,headersList);

			}
			
			DC.NumberofAtomsinFirstPDB=Cas.NumberOfAtomsInFirstPDB;
		
			DC.NumberofAtomsinSecondPDB=Cas.NumberOfAtomsInSecondPDB;
			
			DC.NumberOfAtomsInFirstNotInSecond=Cas.NumberOfAtomsInFirstNotInSecond;
	
			DC.NumberOfAtomsInSecondNotInFirst=Cas.NumberOfAtomsInSecondNotInFirst;
		
			DC.Seqrn1n2n2n1=Cas.Seq;
		
			DC.n1m2=Cas.n1m2;
			
			DC.n2m1=Cas.n2m1;
			
			if(!Cas.n1m2.equals("None"))
			DC.Completeness= df.format((BigDecimal.valueOf((Double.parseDouble(Cas.n1m2) * 100.00)/Double.parseDouble(Cas.NumberOfAtomsInFirstPDB)) ));
			else
		    DC.Completeness=	"None";
			new LogFile().Log(RunningParameter.ToolName, Log.getName(), Thread.currentThread().getName()+" out of "+Files.size(), "cphasesmatch  ", "Running... ",headersList);

			 String [] FandE=new CphasesMatch().cphasesmatch(RunningParameter.DataPath+"/"+DC.PDB_ID,RunningParameter.CphasesMatchScriptPath);
			 DC.F_mapCorrelation=FandE[0];
			 DC.E_mapCorrelation=FandE[1];
			 
			 DC.BuiltPDB="T";
			
			new LogFile().Log(RunningParameter.ToolName, Log.getName(), Thread.currentThread().getName()+" out of "+Files.size(), "cphasesmatch F and E map  ", FandE[0] +" "+ FandE[1],headersList);
			new LogFile().Log(RunningParameter.ToolName, Log.getName(), Thread.currentThread().getName()+" out of "+Files.size(), "Phases Used in CPhasesMatch  ", RunningParameter.PhasesUsedCPhasesMatch,headersList );

			if(RunningParameter.UsingMolProbity.equals("T")) {
			DC.molProbityData=new MolProbity().molProbity(PDB,new File(RunningParameter.DataPath+"/"+DC.PDB_ID+".mtz"));
			new LogFile().Log(RunningParameter.ToolName, Log.getName(), Thread.currentThread().getName()+" out of "+Files.size(), "MolProbity  ", "Running... ",headersList);
			new LogFile().Log(RunningParameter.ToolName, Log.getName(), Thread.currentThread().getName()+" out of "+Files.size(), "MolProbity ", "Done ",headersList );

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
		REFMACFactors F = new Refmac().RunRefmac(RunningParameter.DataPath+"/"+DC.PDB_ID+".mtz", RunningParameter.DataPath+"/"+DC.PDB_ID+".pdb", RunningParameter.RefmacPath, RunningParameter.ToolName, DC.PDB_ID,"");
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
	
	void ParsingRFromDepoistedPDB(File pdb , ExcelContents dc) throws IOException {
		System.out.println(pdb.getAbsolutePath());
		String PDB=new MultiThreadedAnalyser().readFileAsString(pdb.getAbsolutePath());
		
		 
	     
	      String regex = "REMARK\\s+3\\s+R VALUE\\s+\\(WORKING SET\\).+\n";
	      
	      Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
	      Matcher matcher = pattern.matcher(PDB);
	      
	      while (matcher.find())
	      {
	          //System.out.print("1Start index: " + matcher.start());
	          //System.out.print(" 1End index: " + matcher.end() + " ");
	        //  System.out.println(matcher.group().split(":")[1].trim());
	        dc.OptimalR_factor=matcher.group().split(":")[1].trim();
	      }
	      
	      
            regex = "REMARK\\s+3\\s+FREE R VALUE\\s+(?!Test)\\s+.+\n";
	      
	       pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
	       matcher = pattern.matcher(PDB);
	      
	      while (matcher.find())
	      {
	         // System.out.print("Start index: " + matcher.start());
	         // System.out.print("End index: " + matcher.end() + " ");
	         // System.out.println((matcher.group().split(":")[1].trim()));
	        
	      }	
	      //System.out.println(pdb.getAbsolutePath());
			
	}
	
}
