package ToolsExecation.SingleThread;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import Analyser.DataContainer;
import Analyser.ExcelLoader;

public class Parrot {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ExcelLoader e = new ExcelLoader();
		Vector<Vector<DataContainer>> Container =  new Vector<Vector<DataContainer>>();
		
		Container.add(e.ReadExcel("/Volumes/PhDHardDrive/jcsg1200Results/ExcelSheets2/hancs/Buccaneer.xlsx"));
		Container.add(e.ReadExcel("/Volumes/PhDHardDrive/jcsg1200Results/ExcelSheets2/mrncs/Buccaneer.xlsx"));
		Container.add(e.ReadExcel("/Volumes/PhDHardDrive/jcsg1200Results/ExcelSheets2/noncs/Buccaneer.xlsx"));
		
		
		
		File[] Folders = new File("/Volumes/PhDHardDrive/simulated_jcsg/").listFiles();
			int count=0;
		 for (File folder : Folders) {
			
			 if(folder.isDirectory()) {
				
				 for (File file : folder.listFiles()) {
					 boolean found=false;
					// if( FilenameUtils.getExtension(file.getName()).equals("pdb") ||  FilenameUtils.getExtension(file.getName()).equals("fa"))
					 //break;
						 for(int c =0 ;c < Container.size() ; ++c)
					for(int i=0;i<Container.get(c).size() ; ++i) {
						String CaseName=file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),"");
String CaseNameinExcel=Container.get(c).get(i).PDB_ID.replaceAll("-parrot-hancs", "");
 CaseNameinExcel=Container.get(c).get(i).PDB_ID.replaceAll("-parrot-mrncs", "");
 CaseNameinExcel=Container.get(c).get(i).PDB_ID.replaceAll("-parrot-noncs", "");
 
 CaseName=CaseName.replaceAll("-parrot-hancs", "");
 CaseName=CaseName.replaceAll("-parrot-mrncs", "");
 CaseName=CaseName.replaceAll("-parrot-noncs", "");
 
						if(CaseName.equals(CaseNameinExcel) && FilenameUtils.getExtension(file.getName()).equals("mtz")) {
	found=true;
	//break;
}
					}
					if(found==false) {
						if(FilenameUtils.getExtension(file.getName()).equals("mtz")) {
						count++;
					System.out.println(file.getName());
					 String mtzout=file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),"");

					 System.out.println("mtz "+file.getAbsolutePath());
					 System.out.println("PDB "+folder.getAbsolutePath()+"/"+folder.getName()+"-deposited.pdb");
					 System.out.println("Seq "+folder.getAbsolutePath()+"/"+folder.getName()+"-sequence.fa");
					 System.out.println("Mtzout "+"Datasetmrncs/"+mtzout+"-parrot-mrncs.mtz");
					 System.out.println("PDB "+folder.getAbsolutePath()+"/"+folder.getName()+"-deposited.pdb");
					 
					 RunParrot(file.getAbsolutePath(),folder.getAbsolutePath()+"/"+folder.getName()+"-deposited.pdb",folder.getAbsolutePath()+"/"+folder.getName()+"-sequence.fa","Datasetmrncs/"+mtzout+"-parrot-mrncs.mtz",folder.getAbsolutePath()+"/"+folder.getName()+"-deposited.pdb");
					 // System.out.println("PDB "+file.getAbsolutePath().replace(CaseName, "")+);
						}
					}
				 }
			 }
			// FilesNames.add(file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),""));
			 //System.out.println(file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),""));
		 }
		 System.out.println(count);
		
	}

	static void RunParrot(String mtzPath,String PDB, String Seq, String StringMTZOut, String DepositedPDB) throws IOException {
		String[]callAndArgs= {
				 "python",
				 "/Applications/ccp4-7.0/share/python/CCP4Dispatchers/cparrot.py"
				 ,"-seqin",Seq,
				 "-mtzin",mtzPath, 
				 "-mtzout",StringMTZOut, 
				 "-colin-fo", "FP,SIGFP",
				 "-colin-hl"," HLA,HLB,HLC,HLD",
				 "-cycles"," 3",
				 "-anisotropy-correction",
				 "-pdbin-wrk-mr",PDB,
				 //"-pdbin-wrk-mr","",
		
		 };
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
		  String CaseName=new File(mtzPath).getName().replaceAll("."+FilenameUtils.getExtension(new File(mtzPath).getName()),"");
    		  FileUtils.copyFile(new File(DepositedPDB),  new File("./Datasetmrncs/"+CaseName+"-parrot-mrncs.pdb"));
    		  FileUtils.copyFile(new File(Seq),  new File("./Datasetmrncs/"+CaseName+"-parrot-mrncs.seq"));

	}
}
