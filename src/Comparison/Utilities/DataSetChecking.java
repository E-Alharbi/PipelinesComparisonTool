package Comparison.Utilities;
/**
*
* @author Emad Alharbi
* University of York
*/
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import org.apache.commons.io.FilenameUtils;

import Comparison.Analyser.ExcelContents;
import Comparison.Analyser.ExcelSheet;
import Comparison.Analyser.REFMACFactors;
import Comparison.Analyser.MultiThreadedAnalyser;
import Comparison.Runner.RunningPram;
import Comparison.ToolsExecation.SingleThread.CphasesMatch;
import Comparison.ToolsExecation.SingleThread.Refmac;

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
			REFMACFactors F = new Refmac().RunRefmac(DataSetPath+"/"+ExFileName+".mtz", DataSetPath+"/"+ExFileName+".pdb", RunningPram.RefmacPath, RunningPram.ToolName, ExFileName,"");
            DC.Resolution=F.Reso;
			if(new File(RunningPram.IntermediatePDBs+"/"+ExFileName+".pdb").exists()) {
				PDB=new File(RunningPram.IntermediatePDBs+"/"+ExFileName+".pdb");
				DC.Intermediate="T";
			}
			if(!new File(RunningPram.IntermediateLogs+"/"+ExFileName+".txt").exists()) {
				
	            DC.BuiltPDB="F";
			}
			else {
	
		new MultiThreadedAnalyser().Parse(DC,new File(RunningPram.IntermediateLogs+"/"+ExFileName+".txt"),PDB);
		
			}
			
			
			Container.add(DC);
			
			NamesChecked.add(ExFileName);
		}
		}
		}
		return Container;
	}
}
