package Comparison.ToolsExecation.SingleThread;
/**
*
* @author Emad Alharbi
* University of York
*/
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import Comparison.Runner.Preparer;
import Comparison.Runner.RunComparison;

public class CAD {

	public  void RunCAD(String DataPath) throws IOException {
		// TODO Auto-generated method stub

		
		
		URL inputUrl = getClass().getResource("/cad.sh");
		File dest = new File("./cad.sh");
		FileUtils.copyURLToFile(inputUrl, dest);
	
		String OutputFolder = new File(DataPath).getName()+"FakeWave/";
		new RunComparison().CheckDirAndFile("./"+OutputFolder);
	
		 File[] files = new File(DataPath).listFiles();
		
		 Vector<String> ProccessedFiles=new Vector<String>();
		 int count=0;
		 for (File file : files) {
			 String jscgID=file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),"");
			

			 if(!ProccessedFiles.contains(jscgID)) {
				new CAD().CAD(jscgID,DataPath,OutputFolder);
				ProccessedFiles.add(jscgID);
				++count;
				System.out.println(count);
			 }

		 }
	}

	void CAD( String PDBid , String DataPath, String OutputPath) throws IOException {
		
	
		 String[]callAndArgs= {"sh",
				 "cad.sh",
				 System.getenv("CCP4")+"/share/python/CCP4Dispatchers/cad.py"
				 ,DataPath+PDBid+".mtz",
				 OutputPath+PDBid+".mtz" 
		
		 };
		 Process p = Runtime.getRuntime().exec(callAndArgs);
		 
		 BufferedReader stdInput = new BufferedReader(new 

                 InputStreamReader(p.getInputStream()));



            BufferedReader stdError = new BufferedReader(new 

                 InputStreamReader(p.getErrorStream()));



String st="";
            while ((st = stdInput.readLine()) != null) {
            	 //System.out.println(st);
            	
            }

            
         
    	
    		
            while ((st = stdError.readLine()) != null) {

                System.out.println(st);

            }
			 //FileUtils.copyFile(new File("/Volumes/PhDHardDrive/jcsg1200/hancs1/"+PDBid+".pdb"),  new File("/Volumes/PhDHardDrive/jcsg1200/hancs1Fakewave/"+PDBid+".pdb"));
			 //FileUtils.copyFile(new File("/Volumes/PhDHardDrive/jcsg1200/hancs1/"+PDBid+".seq"),  new File("/Volumes/PhDHardDrive/jcsg1200/hancs1Fakewave/"+PDBid+".seq"));

			 FileUtils.copyFile(new File(DataPath+PDBid+".pdb"),  new File(OutputPath+PDBid+".pdb"));
            // FileUtils.copyFile(new File(DataPath+PDBid+".seq"),  new File(OutputPath+PDBid+".seq"));
             
             if(new File(DataPath+PDBid+".seq").exists())
	          FileUtils.copyFile(new File(DataPath+PDBid+".seq"),  new File(OutputPath+PDBid+".seq"));
	          if(new File(DataPath+PDBid+".fasta").exists())
		      FileUtils.copyFile(new File(DataPath+PDBid+".fasta"),  new File(OutputPath+PDBid+".fasta"));
	           if(new File(DataPath+PDBid+".fa").exists())
			   FileUtils.copyFile(new File(DataPath+PDBid+".fa"),  new File(OutputPath+PDBid+".fa"));
		        
	}
}
