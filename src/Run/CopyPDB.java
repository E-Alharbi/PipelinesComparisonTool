package Run;
/**
*
* @author Emad Alharbi
* University of York
*/
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import org.apache.commons.io.FileUtils;

import Analyser.ExcelContents;
import Analyser.ExcelLoader;

public class CopyPDB {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		 ExcelLoader e = new ExcelLoader();
		Vector<ExcelContents> Container2 = e.ReadExcel("/Volumes/PhDHardDrive/jcsg1200Results/Fasta/Run5/ARPwARP.xlsx");
	String PDBPath="/Volumes/PhDHardDrive/simulated_jcsg/";
	String Checked="";
	for(int i=0 ; i < Container2.size() ; ++i ) {
		if(!Checked.contains(Container2.get(i).PDBIDTXT)) {
			FileUtils.copyFile(new File(PDBPath+Container2.get(i).PDBIDTXT+"/"+Container2.get(i).PDBIDTXT+"-deposited.pdb"), new File("PDB/"+Container2.get(i).PDBIDTXT+".pdb"));
			Checked+=Container2.get(i).PDBIDTXT+" ";
		}
	}
	
	}

}
