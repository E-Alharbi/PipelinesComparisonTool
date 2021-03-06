package Comparison.Utilities;
/**
*
* @author Emad Alharbi
* University of York
*/
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import Comparison.Analyser.MultiThreadedAnalyser;
import Comparison.Runner.Preparer;
import Comparison.Runner.RunComparison;

public class RemovingWaterChainID {

	public static void main(String[] args) throws IOException {
		new RemovingWaterChainID().RemoveWaterChainID("/Users/emadalharbi/Downloads/2pnk-4.0-parrot-mrncs.pdb");
	}

	public void RemoveWaterChainID(String Path) throws IOException {
		
		
		String PDB=new MultiThreadedAnalyser().readFileAsString(Path);
	    int NumberOfRecords=   PDB.split("\n").length;
		if(PDB.contains("HETATM")) {
		String SubPDB= PDB.substring(PDB.indexOf("HETATM"), PDB.indexOf("END"));
		PDB=PDB.replace(SubPDB, "");
		PDB=PDB.replace("END", "");
		PDB=PDB.replaceAll("(?m)^\\s+$", "");// removing empty lines that added after replace SubPDB and END
		
		String [] Records= SubPDB.split("\n");
		SubPDB="";
		for(int r=0 ; r < Records.length; ++r  ) {
			Records[r]=Records[r].replace(Records[r].charAt(21), ' ');
			SubPDB+=Records[r]+"\n";
			PDB+=Records[r]+"\n";
		}
		PDB+="END";
		if(NumberOfRecords==PDB.split("\n").length) {
			System.out.println("Matched number of records");
			 FileUtils.deleteQuietly(new File(Path).getAbsoluteFile());
         if(!new File(Path).exists())// to be sure the file is deleted and avoid duplicate the context in the PDB
		new Preparer().WriteTxtFile(new File(Path).getAbsolutePath(), PDB);
		}
	}
	}
	/*
	void FindingPDBWithWaterDuplicateChainID (String PDBFolder) throws IOException {
		int NumberOfModels=0;
		for (File pdb : new File(PDBFolder).listFiles()) {
			String PDB=new ResultsAnalyserMultiThreads().readFileAsString(pdb.getAbsolutePath());
			if(PDB.contains("HETATM")) {
				String SubPDB= PDB.substring(PDB.indexOf("HETATM"), PDB.indexOf("END"));
				
				PDB=PDB.replace(SubPDB, "");
				PDB=PDB.replaceAll("(?m)^\\s+$", "");// removing empty lines that added after replace SubPDB 
			
				if(SubPDB.contains(" Z ") && PDB.contains(" Z ") &&   PDB.split("\n")[PDB.split("\n").length-2].contains("UNK") && 
						(PDB.split(" Z ")[0].split("\n")[PDB.split(" Z ")[0].split("\n").length-2].contains("UNK") ||  PDB.contains(" Z   1"))) {
					//if(SubPDB.contains(" Z   1") && PDB.contains(" Z   1")) {
					System.out.println(pdb.getName());
					NumberOfModels++;	
				}
			}
		}
		System.out.println(NumberOfModels);
	}
	*/
}
