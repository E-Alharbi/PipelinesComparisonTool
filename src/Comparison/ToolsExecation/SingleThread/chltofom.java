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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import Comparison.Runner.RunComparison;
import Comparison.Runner.RunningParameter;

public class chltofom {

	

	
	public void RunChltofom(String DataPath) {
		String OutputFolder = new File(DataPath).getName()+"Chltofom";
		new RunComparison().CheckDirAndFile("./"+OutputFolder);
		File[] files = new File(DataPath).listFiles();
	     System.out.println(files.length);
			 for (File file : files) {
				 String FileName= file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),"");
	
				 if(FilenameUtils.getExtension(file.getName()).equals("mtz")) {
		 String st = null;
	
		         try {
	String mtzin=DataPath+"/"+FileName+".mtz";
    String mtzoutpath=OutputFolder+"/";
	// String Chltofom=System.getenv("CCP4")+"/share/python/CCP4Dispatchers/chltofom.py";
	 String Chltofom="chltofom";
	// String[]callAndArgs= {"python",
	//		 Chltofom,
	//"-mtzin",mtzin,
	//"-mtzout",mtzoutpath+FileName+".mtz",
	//"-colin-hl","parrot.ABCD.A,parrot.ABCD.B,parrot.ABCD.C,parrot.ABCD.D",
	//"-colout","hltofom",
	//};
	 String[]callAndArgs= {
			 Chltofom,
	"-mtzin",mtzin,
	"-mtzout",mtzoutpath+FileName+".mtz",
	//"-colin-hl","parrot.ABCD.A,parrot.ABCD.B,parrot.ABCD.C,parrot.ABCD.D",
	"-colin-hl",RunningParameter.Phases,
	"-colout",RunningParameter.chtofomcolname,
	};
	
	Process p = Runtime.getRuntime().exec(callAndArgs);

		             

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
		             if(new File(DataPath+"/"+FileName+".pdb").exists())
		             FileUtils.copyFile(new File(DataPath+"/"+FileName+".pdb"),  new File(mtzoutpath+FileName+".pdb"));
		             if(new File(DataPath+"/"+FileName+".seq").exists())
		             FileUtils.copyFile(new File(DataPath+"/"+FileName+".seq"),  new File(mtzoutpath+FileName+".seq"));
		             if(new File(DataPath+"/"+FileName+".fasta").exists())
			         FileUtils.copyFile(new File(DataPath+"/"+FileName+".fasta"),  new File(mtzoutpath+FileName+".fasta"));
		             if(new File(DataPath+"/"+FileName+".fa").exists())
				     FileUtils.copyFile(new File(DataPath+"/"+FileName+".fa"),  new File(mtzoutpath+FileName+".fa"));
			        
		         }
		         catch (IOException e) {

		             System.out.println("exception occured");

		             e.printStackTrace();

		             
		         }
			 }
			 }
	}
}
