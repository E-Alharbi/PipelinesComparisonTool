package Comparison.ToolsExecation.SingleThread;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.io.FileUtils;

import Comparison.Runner.Preparer;
import Comparison.Runner.RunComparison;

public class Coord_format {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
new Coord_format().RunCoord_format("/Users/emadalharbi/Desktop/1vl0-3.6-parrot-noncs.pdb",true,false);
	}
	public File RunCoord_format(String PDB, boolean FIXBLANK, boolean RemoveTER) throws IOException {
		new RunComparison().CheckDirAndFile("ModifiedPDB");
		 FileUtils.deleteQuietly(new File(new File(PDB).getName()+".sh"));
		String Coordformat=System.getenv("CCP4")+"/share/python/CCP4Dispatchers/coord_format.py";
		 if(RemoveTER==true)
			 Coordformat=System.getenv("CCP4")+"/share/python/CCP4Dispatchers/pdbcur.py";
		 String st = null;
		 String Coord_formatScript= new Preparer().ReadResourceAsString("/Coordformat.sh");
		 Coord_formatScript=Coord_formatScript.replace("$1", Coordformat);
		 
		 Coord_formatScript=Coord_formatScript.replace("$3", "./ModifiedPDB/"+new File(PDB).getName());
		 if(RemoveTER==false) {
		 Coord_formatScript=Coord_formatScript.replace("$2", new File(PDB).getAbsolutePath());
		 if(FIXBLANK==true)
		 Coord_formatScript=Coord_formatScript.replace("$4", "FIXBLANK");
		 else
		 Coord_formatScript=Coord_formatScript.replace("$4", "OUTPUT CIF");	 
		 }
		 if(RemoveTER==true) {
			 Coord_formatScript=Coord_formatScript.replace("$2", "./ModifiedPDB/"+new File(PDB).getName());
			 Coord_formatScript=Coord_formatScript.replace("$4", "delter"); 
			 
		 }
		
		 new Preparer().WriteTxtFile(new File(PDB).getName()+".sh",Coord_formatScript);
		
		 
		
        Process p = new ProcessBuilder("/bin/bash", new File(new File(PDB).getName()+".sh").getAbsolutePath()).start();

			             

		BufferedReader stdInput = new BufferedReader(new 

			                  InputStreamReader(p.getInputStream()));



			             BufferedReader stdError = new BufferedReader(new 

			                  InputStreamReader(p.getErrorStream()));



		
			             while ((st = stdInput.readLine()) != null) {
			            	 System.out.println(st);
			             }
			             while ((st = stdError.readLine()) != null) {

				                 System.out.println(st);

				             }
			             
			             if(RemoveTER==false) {
			            	 return RunCoord_format( new File(new File(PDB).getName()).getAbsolutePath(),false,true);
			             }
			            return new File(new File(PDB).getName());
	}
}
