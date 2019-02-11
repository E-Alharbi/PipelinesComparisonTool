package NotUsed;
/**
*
* @author Emad Alharbi
* University of York
*/
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Vector;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Analyser.ExcelContents;
import Analyser.ExcelSheet;
import Analyser.FactorsFlags;
import Run.RunningPram;
import ToolsExecation.SingleThread.Castat2Data;
import ToolsExecation.SingleThread.CphasesMatch;
import ToolsExecation.SingleThread.Refmac;
import ToolsExecation.SingleThread.castat2;
import ToolsExecution.RunBuccaneerMulti;
import Utilities.DataSetChecking;
import Utilities.FilesManagements;
import table.draw.LogFile;


public class BuccaneerResultsAnalysis extends Thread {

	// Not Used!!!
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		if(System.getenv("CCP4")==null){
			System.out.println("CCP4 installation cannot be found! if you instaled ccp4, run the setup script first. (setup script canbe found in ccp4/setup-scripts)");
			System.exit(-1);
		}
		if(args.length < 5){
			System.out.println("Error: one or more inputs is missing");
			System.out.println("Data folder path ");
			System.out.println("logs files path for Buccaneer");
			System.out.println("PDBS folder path that built by Buccaneer");
			System.out.println("castat2 path");
			System.out.println("CphasesMatch Script Path in CCP4 folder");
			System.out.println("Refmac Path Script Path in CCP4 folder");
			System.exit(-1);
		}
		RunningPram.DataPath=args[0];//Data folder path 
		RunningPram.LogsDirBuccaneer=args[1];// logs files for Buccaneer
		RunningPram.PDBsDirBuccaneer=args[2];// PDBS files that built by Buccaneer
		RunningPram.castat2Path=args[3];// castat2 path
		RunningPram.CphasesMatchScriptPath=args[4];//CphasesMatch Script Path in CCP4 folder
		RunningPram.RefmacPath=args[5];	
		
		new BuccaneerResultsAnalysis().AnalysingBuccaneerResults();
		
	}
	public void AnalysingBuccaneerResults() throws IOException
	{
		String DataPath=RunningPram.DataPath;//Data folder path 
		String LogsDir=RunningPram.LogsDirBuccaneer;// logs files for Buccaneer
		String PDBsDir=RunningPram.PDBsDirBuccaneer;// PDBS files that built by Buccaneer
		String castat2Path=RunningPram.castat2Path;// castat2 path
		String CphasesMatchScriptPath=RunningPram.CphasesMatchScriptPath;//CphasesMatch Script Path in CCP4 folder
		String RefmacPath=RunningPram.RefmacPath;
		String LIBIN="FP=FP SIGFP=SIGFP FREE=FreeR_flag HLA=parrot.ABCD.A HLB=parrot.ABCD.B HLC=parrot.ABCD.C HLD=parrot.ABCD.D";
		
	
		
		// Vector<String> ColData=  ExcelSheet.ReadExcelByColIndex("./DataRunResults.xlsx",0);
		
		Vector <ExcelContents> Container = new Vector <ExcelContents>();
		 File[] files = new File(LogsDir).listFiles();
		//for(int i=0 ; i < ColData.size() ; ++i){
		 int countFiles=0;
			for (File file : files) {
				countFiles++;
				//if(ColData.get(i).equals(file.getName().substring(0,file.getName().indexOf(".")).trim())){
				String NameOfFile=file.getName().substring(0,file.getName().indexOf(".")).trim();
				System.out.println(NameOfFile);
				String LogTxt=new BuccaneerResultsAnalysis().readFileAsString(LogsDir+"/"+NameOfFile+".txt");
				ExcelContents DC = new ExcelContents();
				//String Reso=LogTxt.substring(LogTxt.indexOf("Resolution range:"));
				//Reso=Reso.substring(Reso.indexOf("Resolution range:"),Reso.indexOf("\n")).split(" ")[Reso.substring(Reso.indexOf("Resolution range:"),Reso.indexOf("\n")).split(" ").length-1];
				String Reso="";
				//System.out.println("Reso "+Reso);
				String RFactor="Not Found";
				String RFree="Not Found";
				String TimeTakig="-1";
				DC.PDB_ID=file.getName().substring(0,file.getName().indexOf('.'));
				DC.ExceptionNoLogFile="F";
				DC.Resolution=Reso;
				DC.TimeTaking=TimeTakig;
				if(LogTxt.contains("TimeTaking")){
					
					TimeTakig=LogTxt.substring(LogTxt.indexOf("TimeTaking")).split(" ")[1].trim();
					//System.out.println(TimeTakig);
				//	new LogFile().Log("Buccaneer", file.getName(), countFiles+" out of "+files.length, " Parsing TimeTakig", TimeTakig);
		if(Double.valueOf(TimeTakig) < 5) {
			DC.WarringTimeTaking="T";
		}
		else {
			DC.WarringTimeTaking="F";
		}
	}
	if(file.length() < 5000) {
		DC.WarringLogFile="T";
		
	}
	else {
		DC.WarringLogFile="F";
	}
				if(new FilesManagements().CheckingIfFileExists(new File(PDBsDir+"/"+NameOfFile+".pdb"))){
					String Line="";
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
					
					if(!RFactor.equals("Not Found") && !RFree.equals("Not Found")){
						
					
					
					System.out.println("RFactor "+RFactor.trim());
					System.out.println("RFree "+RFree);
					
					Double OverfiitingPercentage=0.0;
					if(Double.parseDouble(RFactor)>Double.parseDouble(RFree)){
						OverfiitingPercentage=(Double.parseDouble(RFactor)- Double.parseDouble(RFree));
					}
					else{
						OverfiitingPercentage=(Double.parseDouble(RFree)- Double.parseDouble(RFactor));
					}
					
					System.out.println("OverfiitingPercentage "+OverfiitingPercentage);
					
					
					
					
					DC.R_factor=RFactor;
					
					DC.R_free=RFree;
					
					DC.R_factorΔR_free=String.valueOf(OverfiitingPercentage);
					
					DC.Overfitting=String.valueOf((OverfiitingPercentage>0.05)?"T":"F");
				//	new LogFile().Log("Buccaneer", file.getName(), countFiles+" out of "+files.length, "Run Refmac 0 cycle ", "Running ...");

					FactorsFlags F = new Refmac().RunRefmac(DataPath+"/"+NameOfFile+".mtz", PDBsDir+"/"+NameOfFile+".pdb", RefmacPath, "Buccaneer", NameOfFile,LIBIN);
					
					DC.R_factor0Cycle=F.RFactor;
					
					
					DC.R_free0Cycle=F.FreeFactor;
				//	new LogFile().Log("Buccaneer", file.getName(), countFiles+" out of "+files.length, "Run Refmac 0 cycle ", F.RFactor +" "+F.FreeFactor);

					//new LogFile().Log("Buccaneer", file.getName(), countFiles+" out of "+files.length, "Run Refmac (well known) ", "Running ...");

					 F = new Refmac().RunRefmac(DataPath+"/"+NameOfFile+".mtz", DataPath+"/"+NameOfFile+".pdb", RefmacPath, "Buccaneer", NameOfFile,LIBIN);
					
					DC.OptimalR_factor=F.RFactor;
					//new LogFile().Log("Buccaneer", file.getName(), countFiles+" out of "+files.length, "Run Refmac (well know) ", F.RFactor +" "+F.FreeFactor);

					Castat2Data Cas =  new castat2().Runcastat2(DataPath+"/"+NameOfFile+".pdb", PDBsDir+"/"+NameOfFile+".pdb", castat2Path);
					
					DC.NumberofAtomsinFirstPDB=Cas.NumberOfAtomsInFirstPDB;
				
					DC.NumberofAtomsinSecondPDB=Cas.NumberOfAtomsInSecondPDB;
					
					DC.NumberOfAtomsInFirstNotInSecond=Cas.NumberOfAtomsInFirstNotInSecond;
			
					DC.NumberOfAtomsInSecondNotInFirst=Cas.NumberOfAtomsInSecondNotInFirst;
				
					DC.Seqrn1n2n2n1=Cas.Seq;
				
					DC.n1m2=Cas.n1m2;
					
					DC.n2m1=Cas.n2m1;
					//new LogFile().Log("Buccaneer", file.getName(), countFiles+" out of "+files.length, "cphasesmatch  ", "Running... ");

					 String [] FandE=new CphasesMatch().cphasesmatch(DataPath+"/"+NameOfFile,CphasesMatchScriptPath);
						DC.F_mapCorrelation=FandE[0];
						DC.E_mapCorrelation=FandE[1];
						DC.BuiltPDB="T";
						
					//	new LogFile().Log("Buccaneer", file.getName(), countFiles+" out of "+files.length, "cphasesmatch F and E map  ", FandE[0] +" "+ FandE[1]);

				}
				else {
					DC.BuiltPDB="F";
				}
				
				
				//System.out.println("F-map "+new CphasesMatch().cphasesmatch(DataPath+"/"+NameOfFile,CphasesMatchScriptPath));
				//System.out.println("Optimal R-factor "+new PDBParser().PDBRFactor(DataPath+"/"+NameOfFile+".pdb"));
			

				
			
					Container.add(DC);
				}
				else{
					System.out.println("Something wrong in this log file!!");
new RunBuccaneerMulti().WriteFileNameToList(file.getName().substring(0,file.getName().indexOf('.')),"./BuccaneerResultsAnalyserReport.txt");
				}
				//}
				
			 }
		//}
Container=new DataSetChecking().CheckIfAllDataSetHasProcessed(Container , RunningPram.LogsDirBuccaneer,RunningPram.DataPath);
new ExcelSheet().FillInExcel(Container, "Buccaneer");
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
