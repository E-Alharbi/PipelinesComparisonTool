package Utilities;
/**
*
* @author Emad Alharbi
* University of York
*/
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import org.apache.commons.io.FilenameUtils;

import Analyser.ExcelContents;
import Analyser.ExcelSheet;
import Analyser.FactorsFlags;
import Analyser.ResultsAnalyserMultiThreads;
import Run.RunningPram;
import ToolsExecation.SingleThread.CphasesMatch;
import ToolsExecation.SingleThread.Refmac;

public class DataSetChecking {

	public Vector<ExcelContents> CheckIfAllDataSetHasProcessed (Vector <ExcelContents> Container, String LogsDir , String DataSetPath) throws IOException {
		 File[] files = new File(LogsDir).listFiles();
		File [] DataSet = new File(DataSetPath).listFiles();
		Vector <String> NamesChecked= new Vector <String>();
		for (File Data : DataSet) {
			boolean Found=false;
			//String ExFileName=Data.getName().substring(0,Data.getName().indexOf(".")).trim();	
			String ExFileName=Data.getName().replaceAll("."+FilenameUtils.getExtension(Data.getName()),"");
	if(NamesChecked.contains(ExFileName)==false) {
		for (File LogFile : files) {
		//	String LogFileName=LogFile.getName().substring(0,LogFile.getName().indexOf(".")).trim();	
			String LogFileName=LogFile.getName().replaceAll("."+FilenameUtils.getExtension(LogFile.getName()),"");

			if(LogFileName.equals(ExFileName)) {
				Found=true;
				break;
			}
				}
		if(Found==false) {
			File PDB=null;
			ExcelContents DC = new ExcelContents();
			DC.PDB_ID=ExFileName;
			DC.PDBIDTXT=ExFileName.substring(0,4);
			FactorsFlags F = new Refmac().RunRefmac(DataSetPath+"/"+ExFileName+".mtz", DataSetPath+"/"+ExFileName+".pdb", RunningPram.RefmacPath, RunningPram.ToolName, ExFileName,"");
            DC.Resolution=F.Reso;
			if(new File(RunningPram.IntermediatePDBs+"/"+ExFileName+".pdb").exists()) {
				PDB=new File(RunningPram.IntermediatePDBs+"/"+ExFileName+".pdb");
				DC.Intermediate="T";
			}
			if(!new File(RunningPram.IntermediateLogs+"/"+ExFileName+".txt").exists()) {
				
	            DC.BuiltPDB="F";
			}
			else {
	
		new ResultsAnalyserMultiThreads().Parse(DC,new File(RunningPram.IntermediateLogs+"/"+ExFileName+".txt"),PDB);
		
			}
			
			/*
			DC.ExceptionNoLogFile="T";
			DC.BuiltPDB="F";
			 String [] FandE=new CphasesMatch().cphasesmatch(DataSetPath+"/"+ExFileName,RunningPram.CphasesMatchScriptPath);
			DC.F_mapCorrelation=FandE[0];
			DC.E_mapCorrelation=FandE[1];
			Factors F = new Refmac().RunRefmac(DataSetPath+"/"+ExFileName+".mtz", DataSetPath+"/"+ExFileName+".pdb", RunningPram.RefmacPath, RunningPram.ToolName, ExFileName,"");
            DC.Resolution=F.Reso;
			*/
			Container.add(DC);
			
			NamesChecked.add(ExFileName);
		}
		}
		}
		return Container;
	}
}
