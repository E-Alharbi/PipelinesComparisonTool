package NotToSync;
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
import Comparison.Analyser.ExcelLoader;
import Comparison.Analyser.ExcelSheet;
import Comparison.Analyser.MultiThreadedAnalyser;
import Comparison.Runner.RunningPram;

public class SplitArpWArpPDBForNoDumAtoms {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		File [] PDBs= new File("/Volumes/PhDHardDrive/TempResults/Pipelines/ArpNoncsPDB").listFiles();
		 ExcelLoader e = new ExcelLoader();
		Vector<ExcelContents> Container = e.ReadExcel("/Volumes/PhDHardDrive/jcsg1200Results/Fasta/Run6/noncs/ARPwARP.xlsx");
		Vector<ExcelContents> ContainerNoDummy = new Vector<ExcelContents>();
		int count=0;
		for(File pdb : PDBs) {
		 String Txt = new MultiThreadedAnalyser().readFileAsString(pdb.getAbsolutePath());
		 if(!Txt.contains("DUM")) {
		 String PDB=pdb.getName().replaceAll("."+FilenameUtils.getExtension(pdb.getName()),"");
          for(int i=0 ; i < Container.size() ; ++i) {
        	  if(PDB.equals(Container.get(i).PDB_ID)){
        		  ContainerNoDummy.add(Container.get(i));
        	  }
          }
		 System.out.println(pdb.getName());
		  count++;
		 }
	}
		System.out.println(ContainerNoDummy.size());
		new ExcelSheet().FillInExcel(ContainerNoDummy, "ArpwArpNoDummay");
		System.out.println(count);
	}

}
