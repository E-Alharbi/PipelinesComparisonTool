package ResultsParsing;
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

import Run.RunningPram;
import ToolsExecution.Castat2Data;
import ToolsExecution.CphasesMatch;
import ToolsExecution.Refmac;
import ToolsExecution.castat2;
import Utilities.DataSetChecking;
import Utilities.FilesManagements;
import table.draw.LogFile;


public class ArpResultsAnalysis2 {

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
			System.exit(-1);
		}
		RunningPram.DataPath=args[0];//Data folder path 
		RunningPram.LogsDirwArp=args[1];// logs files for wArp
		RunningPram.PDBsDirwArp=args[2];// PDBS files that built by wArp
		RunningPram.castat2Path=args[3];// castat2 path
		RunningPram.CphasesMatchScriptPath=args[4];//CphasesMatch Script Path in CCP4 folder
		RunningPram.RefmacPath=args[5];	
		new ArpResultsAnalysis2().AnalysingwArpResults();
	}
	public void AnalysingwArpResults() throws IOException
	{
		
		String DataPath=RunningPram.DataPath;//Data folder path 
		String LogsDir=RunningPram.LogsDirwArp;// logs files for wArp
		String PDBsDir=RunningPram.PDBsDirwArp;// PDBS files that built by wArp
		String castat2Path=RunningPram.castat2Path;// castat2 path
		String CphasesMatchScriptPath=RunningPram.CphasesMatchScriptPath;//CphasesMatch Script Path in CCP4 folder
		String RefmacPath=RunningPram.RefmacPath;
		String LIBIN="FP=FP SIGFP=SIGFP FREE=FreeR_flag HLA=parrot.ABCD.A HLB=parrot.ABCD.B HLC=parrot.ABCD.C HLD=parrot.ABCD.D";
		
		
		
	//	Vector<String> ColData=  ExcelSheet.ReadExcelByColIndex("./DataRunResults.xlsx",0);
		Vector <DataContainer> Container = new Vector <DataContainer>();
		 File[] files = new File(LogsDir).listFiles();
	//	for(int i=0 ; i < ColData.size() ; ++i){
		 int countFiles=0;
			for (File file : files) {
				countFiles++;
				//System.out.println(file.getName());
				String NameOfFile=file.getName().substring(0,file.getName().indexOf(".")).trim();
				DataContainer DC = new DataContainer();
				DC.ExceptionNoLogFile="F";
				String RFactor="";
				String RFree="";
				Double OverfiitingPercentage=0.0;
				//System.out.println(NameOfFile);
				String LogTxt=new ArpResultsAnalysis2().readFileAsString(LogsDir+"/"+NameOfFile+".txt");
			
				String Reso=LogTxt.substring(LogTxt.indexOf("Resolution range:"));
				Reso=Reso.substring(Reso.indexOf("Resolution range:"),Reso.indexOf("\n")).split(" ")[Reso.substring(Reso.indexOf("Resolution range:"),Reso.indexOf("\n")).split(" ").length-1];
				//System.out.println(Reso);
				//new LogFile().Log("wArp/Arp", file.getName(), countFiles+" out of "+files.length, " Parsing Resolution", Reso);
				String TimeTakig="-1";
				Factors F=null;
				
				if(LogTxt.contains("TimeTaking")){
					
								TimeTakig=LogTxt.substring(LogTxt.indexOf("TimeTaking")).split(" ")[1].trim();
								//System.out.println(TimeTakig);
								//new LogFile().Log("wArp/Arp", file.getName(), countFiles+" out of "+files.length, " Parsing TimeTakig", TimeTakig);
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
							if(Line.contains("R =")){
								RFactor=Line;
								
							}
							if(Line.contains("Rfree =")){
								RFree=Line;
								
							}
							Line="";
						}
					}
					RFactor=RFactor.substring(RFactor.indexOf("R ="));
					RFactor=RFactor.substring(3,RFactor.indexOf("(")).trim();
					
					RFree=RFree.substring(RFree.indexOf("Rfree ="));
					
					RFree=RFree.substring(7,RFree.indexOf(")")).trim();
					
					//new LogFile().Log("wArp/Arp", file.getName(), countFiles+" out of "+files.length, "R and R free", RFactor +" "+RFree);

					//System.out.println("RFree "+RFree);
					//System.out.println("RFactor "+RFactor.trim());
					//new LogFile().Log("wArp/Arp", file.getName(), countFiles+" out of "+files.length, "Run Refmac 0 cycle ", "Running ...");

					 F = new Refmac().RunRefmac(DataPath+"/"+NameOfFile+".mtz", PDBsDir+"/"+NameOfFile+".pdb", RefmacPath, "wARP", NameOfFile,LIBIN);

						//new LogFile().Log("wArp/Arp", file.getName(), countFiles+" out of "+files.length, "Run Refmac 0 cycle ", F.RFactor +" "+F.FreeFactor);

					if(Double.parseDouble(F.RFactor)>Double.parseDouble(F.FreeFactor)){
						OverfiitingPercentage=(Double.parseDouble(F.RFactor)- Double.parseDouble(F.FreeFactor));
					}
					else{
						OverfiitingPercentage=(Double.parseDouble(F.FreeFactor)- Double.parseDouble(F.RFactor));
					}
					//System.out.println("OverfiitingPercentage "+OverfiitingPercentage);
					
					//System.out.println("F-map "+new CphasesMatch().cphasesmatch(DataPath+"/"+NameOfFile,CphasesMatchScriptPath));
					DC.R_factor=RFactor;

					DC.R_free=RFree;

					DC.R_factorΔR_free=String.valueOf(OverfiitingPercentage);

					DC.Overfitting=String.valueOf((OverfiitingPercentage>0.05)?"T":"F");

					DC.R_factor0Cycle=F.RFactor;


					DC.R_free0Cycle=F.FreeFactor;
					//new LogFile().Log("wArp/Arp", file.getName(), countFiles+" out of "+files.length, "Run Refmac (well known) ", "Running ...");

					 F = new Refmac().RunRefmac(DataPath+"/"+NameOfFile+".mtz", DataPath+"/"+NameOfFile+".pdb", RefmacPath, "wARP", NameOfFile,LIBIN);
					//	new LogFile().Log("wArp/Arp", file.getName(), countFiles+" out of "+files.length, "Run Refmac (well know) ", F.RFactor +" "+F.FreeFactor);

					DC.OptimalR_factor=F.RFactor;
					//new LogFile().Log("wArp/Arp", file.getName(), countFiles+" out of "+files.length, "Castat  ", "Running ...");
					Castat2Data Cas =  new castat2().Runcastat2(DataPath+"/"+NameOfFile+".pdb", PDBsDir+"/"+NameOfFile+".pdb", castat2Path);
					//new LogFile().Log("wArp/Arp", file.getName(), countFiles+" out of "+files.length, "Castat  ", "Done");

					DC.NumberofAtomsinFirstPDB=Cas.NumberOfAtomsInFirstPDB;

					DC.NumberofAtomsinSecondPDB=Cas.NumberOfAtomsInSecondPDB;

					DC.NumberOfAtomsInFirstNotInSecond=Cas.NumberOfAtomsInFirstNotInSecond;

					DC.NumberOfAtomsInSecondNotInFirst=Cas.NumberOfAtomsInSecondNotInFirst;

					DC.Seqrn1n2n2n1=Cas.Seq;

					DC.n1m2=Cas.n1m2;

					DC.n2m1=Cas.n2m1;
					//new LogFile().Log("wArp/Arp", file.getName(), countFiles+" out of "+files.length, "cphasesmatch  ", "Running... ");

					 String [] FandE=new CphasesMatch().cphasesmatch(DataPath+"/"+NameOfFile,CphasesMatchScriptPath);
					DC.F_mapCorrelation=FandE[0];
					DC.E_mapCorrelation=FandE[1];
					//new LogFile().Log("wArp/Arp", file.getName(), countFiles+" out of "+files.length, "cphasesmatch F and E map  ", FandE[0] +" "+ FandE[1]);

					DC.BuiltPDB="T";
				}
				else{
					DC.BuiltPDB="F";
					//new LogFile().Log("wArp/Arp", file.getName(), countFiles+" out of "+files.length, "Built PDB ?", " No");

				}
				
				//if(ColData.get(i).equals(file.getName().substring(0,file.getName().indexOf(".")).trim())){
				
			
DC.PDB_ID=file.getName().substring(0,file.getName().indexOf('.'));

DC.Resolution=Reso;

DC.TimeTaking=TimeTakig;


Container.add(DC);
				
				//}
			
			 }
		//}
Container=new DataSetChecking().CheckIfAllDataSetHasProcessed(Container , RunningPram.LogsDirwArp,RunningPram.DataPath);

		new ExcelSheet().FillInExcel(Container, "wArp");
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
