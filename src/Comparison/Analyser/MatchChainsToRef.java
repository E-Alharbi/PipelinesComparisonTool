package Comparison.Analyser;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import Comparison.ToolsExecation.SingleThread.Csymmatch;

public class MatchChainsToRef {

	
	public  void MatchUsingCsymmatch(String DataPath, String PDBPath) throws IOException {
		// TODO Auto-generated method stub
		//String  PDBPath = "/Users/emadalharbi/Downloads/PipeliensMR/Buccaneeri1I5TestCsy/BuccaneerResults/PDBs";
	   //String  Data = "/Users/emadalharbi/Downloads/examples/datasetsForAnalysis/";
	   
	   if(!new File (new File(PDBPath).getName()).exists())
	   FileUtils.copyDirectoryToDirectory(new File(PDBPath), new File ("./"));
	   else {
		   System.out.println("PDBs folder is exists. Remove it then re-run");
		   System.exit(-1);
	   }
		File [] PDB = new File (new File(PDBPath).getName()).listFiles();

	   for(File P : PDB ) {
		   String PDBCode=P.getName().replaceAll("."+FilenameUtils.getExtension(P.getName()),"");
		   new Csymmatch().RunCsymmatch(P,new File(DataPath+PDBCode+".pdb"));
	   }
	}
}
