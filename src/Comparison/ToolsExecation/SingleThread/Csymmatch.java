package Comparison.ToolsExecation.SingleThread;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class Csymmatch {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String  PDBPath = "/Users/emadalharbi/Downloads/PipeliensMR/Buccaneeri1I5TestCsy/BuccaneerResults/PDBs";
	   String  Data = "/Users/emadalharbi/Downloads/examples/datasetsForAnalysis/";
	   
	   if(!new File (new File(PDBPath).getName()).exists())
	   FileUtils.copyDirectoryToDirectory(new File(PDBPath), new File ("./"));
	   else {
		   System.out.println("PDBs folder is exists. Remove it then re-run");
		   System.exit(-1);
	   }
		File [] PDB = new File (new File(PDBPath).getName()).listFiles();

	   for(File P : PDB ) {
		   String PDBCode=P.getName().replaceAll("."+FilenameUtils.getExtension(P.getName()),"");
		   new Csymmatch().RunCsymmatch(P,new File(Data+PDBCode+".pdb"));
	   }
	}
	
public void RunCsymmatch(File PDB, File RefPDB) throws IOException {
	
	// Warring: the output PDB will overwrite the input PDB. Make a copy first before use csymmatch
	// Use csymmatch from bin folder
	 String[]callAndArgs= {
			 System.getenv("CCP4")+"/bin/csymmatch",
	"-pdbin-ref",RefPDB.getAbsolutePath(),
	"-pdbin",PDB.getAbsolutePath(),
	"-pdbout",PDB.getAbsolutePath(),
	"-origin-hand-work",
	};
System.out.println(Arrays.toString(callAndArgs));
	Process p = Runtime.getRuntime().exec(callAndArgs);

		             

	BufferedReader stdInput = new BufferedReader(new 

		                  InputStreamReader(p.getInputStream()));



		             BufferedReader stdError = new BufferedReader(new 

		                  InputStreamReader(p.getErrorStream()));


String st="";
	
		             while ((st = stdInput.readLine()) != null) {
		            	 System.out.println(st);
		             }
		             while ((st = stdError.readLine()) != null) {

			                 System.out.println(st);

			             }
}
}
