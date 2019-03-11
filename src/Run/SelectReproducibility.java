package Run;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import Analyser.ExcelContents;
import Analyser.ExcelLoader;

public class SelectReproducibility {

	
	// Select Reproducibility cases
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ExcelLoader e = new ExcelLoader();
	
Vector<ExcelContents> Con= 			e.ReadExcel("/Volumes/PhDHardDrive/jcsg1200Results/Fasta/VikingRun3ArpNoFree/All/noncs/PhenixHAL.xlsx.xlsx");
Collections.sort(Con, ExcelContents.DataContainerComparatorTimeTaking);// Sorting based on Time Taking
List<ExcelContents> c=Con.subList(3, 33);
int cout =0 ;
for(ExcelContents EC : c) {
	if(EC.BuiltPDB.equals("T") && EC.Intermediate.equals("F")) {
	//System.out.println(EC.TimeTaking);
		System.out.println("cp /users/emra500/scratch/Dataset/noncsFakeWaveFakeWaveFakeAnom/"+EC.PDB_ID+".mtz ./");
		System.out.println("cp /users/emra500/scratch/Dataset/noncsFakeWaveFakeWaveFakeAnom/"+EC.PDB_ID+".fasta ./");
		System.out.println("cp /users/emra500/scratch/Dataset/noncsFakeWaveFakeWaveFakeAnom/"+EC.PDB_ID+".pdb ./");

		cout++;
	}
}
System.out.println(cout);
	}

}
