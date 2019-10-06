package NotToSync;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Vector;
import java.util.regex.Pattern;

import Comparison.Analyser.Bucaneeri2LogGenerator;
import Comparison.Analyser.ExcelContents;
import Comparison.Analyser.ExcelLoader;
import Comparison.Analyser.MultiThreadedAnalyser;

public class test4 {

	public static void main(String[] args) throws IOException {
		
	
		
		
		/*
		File [] Noncs= new File("/Volumes/PhDHardDrive/jcsg1200/noncsWithMissingCasesFasta/").listFiles();
		String  Dataset54=new MultiThreadedAnalyser().readFileAsString("/Volumes/PhDHardDrive/EditorsRevision/Experiment/datasets.54");
		Vector<String> names = new Vector<String>();
		for(File f : Noncs) {
			if(!names.contains(f.getName().substring(0,4))) {
				names.add(f.getName().substring(0,4));
				if(Dataset54.contains(f.getName().substring(0,4)))
				System.out.print(f.getName().substring(0,4)+"*, ");
				else
					System.out.print(f.getName().substring(0,4)+", ");
			}
		}
		*/
		
		ExcelLoader f = new ExcelLoader();
		Vector<ExcelContents> Excel1 = f.ReadExcel("/Users/emadalharbi/Downloads/ShelxeWithTFlag.xlsx");
		Vector<ExcelContents> Excel2 = f.ReadExcel("/Volumes/PhDHardDrive/jcsg1200Results/Fasta/VikingRunShelxeForThePaperRevisionReproducibility/All/noncs/ShelxeWithTFlag.xlsx");
	for(ExcelContents PDB1 : Excel1) {
		for(ExcelContents PDB2 : Excel2) {
			DecimalFormat df = new DecimalFormat("#.##");
			df.setRoundingMode(RoundingMode.HALF_UP);
			String Completeness= df.format((BigDecimal.valueOf((Double.parseDouble(PDB1.NumberOfAtomsInSecondNotInFirst) * 100.00)/Double.parseDouble(PDB1.NumberofAtomsinFirstPDB)) ));
			PDB1.Completeness=Completeness;
			if(PDB1.PDB_ID.equals(PDB2.PDB_ID)) {
				if(!PDB1.Completeness.equals(PDB2.Completeness)) {
					System.out.println("PDB "+PDB1.PDB_ID);
					System.out.println("Com "+PDB1.Completeness);
					System.out.println("Com2 "+PDB2.Completeness);
				}
				if(!PDB1.R_factor0Cycle.equals(PDB2.R_factor0Cycle)) {
					System.out.println("PDB "+PDB1.PDB_ID);
					System.out.println("R "+PDB1.R_factor0Cycle);
					System.out.println("R2 "+PDB2.R_factor0Cycle);
				}
				if(!PDB1.R_free0Cycle.equals(PDB2.R_free0Cycle)) {
					System.out.println("PDB "+PDB1.PDB_ID);
					System.out.println("Rfree "+PDB1.R_free0Cycle);
					System.out.println("Rfree2 "+PDB2.R_free0Cycle);
				}
			}
		}
	}
	
	}
}
