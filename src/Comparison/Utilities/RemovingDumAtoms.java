package Comparison.Utilities;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import Comparison.Analyser.MultiThreadedAnalyser;
import Comparison.Runner.RunComparison;

public class RemovingDumAtoms {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
System.out.println(new RemovingDumAtoms().CheckingIfContainsDUMAtomsMR("/Users/emadalharbi/Downloads/PipeliensMR/6MMZ-3.3-parrot-noncs2.pdb"));
	//new RemovingDumAtoms().RemovingWithOverwrite("/Users/emadalharbi/Downloads/PipeliensMR/4XTW-2.3-parrot-noncs.pdb");
	}

	public File Removing(String PDBPath) throws IOException {
		//  mainly use for Phenix run After ARP/wARP
		new RunComparison().CheckDirAndFile("ModifiedPDBAfterRemovingDUMAtoms");
		String  [] PDB=new MultiThreadedAnalyser().readFileAsString(PDBPath).split("\n");
		String UpdatedPDB="";
		
		for (String Record : PDB) {
			if((!Record.contains("HETATM") && !Record.contains("DUM"))) {
				UpdatedPDB+=Record+"\n";
			}
			if((!Record.contains("HETATM") && Record.contains("DUM"))) { // in case there is Link record with DUM atom
				UpdatedPDB+=Record+"\n";
			}
		}
		
		try(  PrintWriter out = new PrintWriter( "./ModifiedPDBAfterRemovingDUMAtoms/"+new File(PDBPath).getName() )  ){
 		    out.println( UpdatedPDB );
 		}
		return new File("./ModifiedPDBAfterRemovingDUMAtoms/"+new File(PDBPath).getName());
	}
	public void RemovingWithOverwrite(String PDBPath) throws IOException {
		// Use for MR when input PDB to Csymmatch mainly use for ARP/wARP
		String  [] PDB=new MultiThreadedAnalyser().readFileAsString(PDBPath).split("\n");
		String UpdatedPDB="";
		
		for (String Record : PDB) {
			if((Record.contains("ATOM") && Record.contains(" DUM "))) {
				// Do not add them 
			}
			else {
				UpdatedPDB+=Record+"\n";
			}
			//if((!Record.contains("ATOM") && Record.contains(" DUM "))) { // in case there is Link record with DUM atom
				//UpdatedPDB+=Record+"\n";
			//}
		}
		
		try(  PrintWriter out = new PrintWriter( new File(PDBPath).getAbsoluteFile() )  ){
 		    out.println( UpdatedPDB );
 		}
		
	}
	public boolean CheckingIfContainsDUMAtoms(String PDBPath) throws IOException {
		String  [] PDB=new MultiThreadedAnalyser().readFileAsString(PDBPath).split("\n");
		
		
		for (String Record : PDB) {
			if((Record.contains("HETATM") && Record.contains("DUM"))) 
				return true;
		}
		return false;
	}
	
	public boolean CheckingIfContainsDUMAtomsMR(String PDBPath) throws IOException {
		String  [] PDB=new MultiThreadedAnalyser().readFileAsString(PDBPath).split("\n");
		
		
		for (String Record : PDB) {
			if((Record.contains("ATOM") && Record.contains(" DUM ")))
				return true;
		}
		return false;
	}
}
