package Comparison.Utilities;

import java.io.File;
import java.io.IOException;

import Comparison.Runner.Preparer;
import Comparison.Runner.RunComparison;

public class RemoveHeader {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
new RemoveHeader().RemoveHeaderFromRefPDB(new File("/Users/emadalharbi/Desktop/reference.pdb"));
	}

	public File  RemoveHeaderFromRefPDB(File PDB) throws IOException {
		new RunComparison().CheckDirAndFile("RefPDBAfterRemvingFirstLine");
		 String [] PDBContents=new FilesManagements().readFileAsString(PDB.getAbsolutePath()).split("\n");
		 String UpdatedPDB="";
		 for(int i=1 ; i < PDBContents.length ;++i) {
			 UpdatedPDB+=PDBContents[i]+"\n";
		 }
		 new Preparer().WriteTxtFile("RefPDBAfterRemvingFirstLine/"+PDB.getName(), UpdatedPDB);
		 System.out.println(UpdatedPDB);
		 
		 return new File("RefPDBAfterRemvingFirstLine/"+PDB.getName());
		 
	}
}
