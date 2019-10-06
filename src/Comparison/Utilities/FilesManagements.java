package Comparison.Utilities;
import java.io.BufferedReader;
/**
*
* @author Emad Alharbi
* University of York
*/
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import org.apache.commons.io.FileUtils;

import Comparison.Runner.RunningParameter;


public class FilesManagements {

	

	public static boolean RemoveFile(File file) throws IOException{
	
			if (file.delete())
			return true;
			else
			return false;
	}
	public static boolean RemoveDir(File file) throws IOException{
		
		try {
			FileUtils.deleteDirectory(file.getAbsoluteFile());
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
}
	public static boolean CheckingIfFileExists(File file) throws IOException{
		
		if (file.exists())
		return true;
		else
		return false;
}
	public String GetModelPath(String PDBId){
		
		 File[] initialmodels = new File(RunningParameter.InitialModels).listFiles();
		 for (File PDB : initialmodels) {
			 if(PDBId.trim().equals(PDB.getName())) {
				 return PDB.getAbsolutePath();
			 }
		 }
		 initialmodels = new File(RunningParameter.InitialModels.replaceAll("PDBs", "IntermediatePDBs")).listFiles(); 
    for (File PDB : initialmodels) {
		 if(PDBId.trim().equals(PDB.getName())) {
			 return PDB.getAbsolutePath();
		 }
	 } 
		 return "";// Not Found
}
	public String readFileAsString(String filePath) throws IOException {
        StringBuffer fileData = new StringBuffer();
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }
}
