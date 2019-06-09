package NotToSync;
/**
*
* @author Emad Alharbi
* University of York
*/
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class FormatingFilesNames {
	private static final long MEGABYTE = 1024L * 1024L;

    public static long bytesToMegabytes(long bytes) {
        return bytes / MEGABYTE;
    }
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		File[] jcsgs = new File("/Volumes/PhDHardDrive/jcsg1200/hancsFakewaveFake").listFiles();
		int count=0;
		 for (File jcsg : jcsgs) {
			 String CaseName=jcsg.getName().replaceAll("."+FilenameUtils.getExtension(jcsg.getName()),"");
			 if(count<1500) {
			 FileUtils.copyFile(new File("/Volumes/PhDHardDrive/jcsg1200/hancsFakewaveFake/"+CaseName+".pdb"),  new File("/Volumes/PhDHardDrive/jcsg1200/hancsFakewaveFake1/"+CaseName+".pdb"));
			 FileUtils.copyFile(new File("/Volumes/PhDHardDrive/jcsg1200/hancsFakewaveFake/"+CaseName+".mtz"),  new File("/Volumes/PhDHardDrive/jcsg1200/hancsFakewaveFake1/"+CaseName+".mtz"));

			 FileUtils.copyFile(new File("/Volumes/PhDHardDrive/jcsg1200/hancsFakewaveFake/"+CaseName+".seq"),  new File("/Volumes/PhDHardDrive/jcsg1200/hancsFakewaveFake1/"+CaseName+".seq"));

			 }
			 else {
				 FileUtils.copyFile(new File("/Volumes/PhDHardDrive/jcsg1200/hancsFakewaveFake/"+CaseName+".pdb"),  new File("/Volumes/PhDHardDrive/jcsg1200/hancsFakewaveFake2/"+CaseName+".pdb"));
				 FileUtils.copyFile(new File("/Volumes/PhDHardDrive/jcsg1200/hancsFakewaveFake/"+CaseName+".mtz"),  new File("/Volumes/PhDHardDrive/jcsg1200/hancsFakewaveFake2/"+CaseName+".mtz"));

				 FileUtils.copyFile(new File("/Volumes/PhDHardDrive/jcsg1200/hancsFakewaveFake/"+CaseName+".seq"),  new File("/Volumes/PhDHardDrive/jcsg1200/hancsFakewaveFake2/"+CaseName+".seq"));
 
			 }
			 ++count;
		 
		 }
		/*
		 File[] jcsgs = new File("/Volumes/PhDHardDrive/simulated_jcsg").listFiles();
		 int count=0;
		
		 for (File jcsg : jcsgs) {
			 if(jcsg.isDirectory()) {
				 boolean Copiedhancs=false;
				 boolean Copiedmrncs=false;
				 boolean Copiednoncs=false;
				 File[] CaseDir =   jcsg.listFiles();
				
				 for (File Case : CaseDir) {
					 System.out.println(Case.getName());
					 File SeqPath=new File("");
					 File WellKnownPDBPath=new File("");
					 for (File SeqAndPDB : CaseDir) {
						 if(SeqAndPDB.getName().contains("sequence")) {
							 SeqPath=SeqAndPDB;
						 }
						 if(SeqAndPDB.getName().contains("deposited.pdb")) {
							 WellKnownPDBPath=SeqAndPDB;
						 }
					 }
					 System.out.println(SeqPath);
					 System.out.println(WellKnownPDBPath);
					 
				if(Case.getName().contains("hancs")) {
					if(count<500) {
		       FileUtils.copyFile(Case,  new File("/Volumes/PhDHardDrive/jcsg1200/hancs/"+Case.getName()));
			       Copiedhancs=true;
				System.out.println("Copied hancs");	
				 FileUtils.copyFile(SeqPath,new File("/Volumes/PhDHardDrive/jcsg1200/hancs/"+SeqPath.getName().replaceAll(SeqPath.getName(), Case.getName().replaceAll(".mtz", ".seq"))));
				 FileUtils.copyFile(WellKnownPDBPath,new File("/Volumes/PhDHardDrive/jcsg1200/hancs/"+WellKnownPDBPath.getName().replaceAll(WellKnownPDBPath.getName(), Case.getName().replaceAll(".mtz",".pdb"))));
					} else {
					 FileUtils.copyFile(Case,  new File("/Volumes/PhDHardDrive/jcsg1200/hancs1/"+Case.getName()));
				       Copiedhancs=true;
					System.out.println("Copied hancs");	
					 FileUtils.copyFile(SeqPath,new File("/Volumes/PhDHardDrive/jcsg1200/hancs1/"+SeqPath.getName().replaceAll(SeqPath.getName(), Case.getName().replaceAll(".mtz", ".seq"))));
					 FileUtils.copyFile(WellKnownPDBPath,new File("/Volumes/PhDHardDrive/jcsg1200/hancs1/"+WellKnownPDBPath.getName().replaceAll(WellKnownPDBPath.getName(), Case.getName().replaceAll(".mtz",".pdb"))));
					 
				}
					++count;
				}
				
				*/
				/*
if(Case.getName().contains("mrncs")) {
   FileUtils.copyFile(Case,  new File("/Volumes/PhDHardDrive/jcsg1200/mrncs/"+Case.getName()));
    Copiedmrncs=true;
    System.out.println("Copied mrncs");	
    FileUtils.copyFile(SeqPath,new File("/Volumes/PhDHardDrive/jcsg1200/mrncs/"+SeqPath.getName().replaceAll(SeqPath.getName(), Case.getName().replaceAll(".mtz", ".seq"))));
	 FileUtils.copyFile(WellKnownPDBPath,new File("/Volumes/PhDHardDrive/jcsg1200/mrncs/"+WellKnownPDBPath.getName().replaceAll(WellKnownPDBPath.getName(), Case.getName().replaceAll(".mtz",".pdb"))));

					 }
if(Case.getName().contains("noncs")) {
	FileUtils.copyFile(Case,  new File("/Volumes/PhDHardDrive/jcsg1200/noncs/"+Case.getName()));
	Copiednoncs=true;
	 System.out.println("Copied noncs");	
	 FileUtils.copyFile(SeqPath,new File("/Volumes/PhDHardDrive/jcsg1200/noncs/"+SeqPath.getName().replaceAll(SeqPath.getName(), Case.getName().replaceAll(".mtz", ".seq"))));
	 FileUtils.copyFile(WellKnownPDBPath,new File("/Volumes/PhDHardDrive/jcsg1200/noncs/"+WellKnownPDBPath.getName().replaceAll(WellKnownPDBPath.getName(), Case.getName().replaceAll(".mtz",".pdb"))));

}
*/
		/*
				 }
				 
				if(!Copiedhancs || !Copiedmrncs || !Copiednoncs) {
					System.out.println(jcsg.getName());
					System.out.println("Copiedhancs "+Copiedhancs);
					System.out.println("Copiedmrncs "+Copiedmrncs);
					System.out.println("Copiednoncs "+Copiednoncs);
					
				} 
				 
				
			 }
		 }
		 */
		/*
		 File[] PDBs = new File("/Users/emadalharbi/Desktop/PhDYork/jcsg202Results/Run 7:3:2018/Phenix178/PDBs").listFiles();
		// File[] Logs = new File("/Users/emadalharbi/Desktop/PhDYork/jcsg202Results/Run 7:3:2018/Phenix46/PhinexLogs").listFiles();
		int count=0;
		 
		
			
		// for (File Log : Logs) {
		//	 boolean Found=false;
			// String jscgID= Log.getName().substring(0, Log.getName().indexOf('.'));
			 for (File PDB : PDBs) {
				// if(PDB.getName().substring(0, PDB.getName().indexOf('.')).equals(Log.getName().substring(0, Log.getName().indexOf('.')))) {
					// Found=true;
				 System.out.println(PDB.getName().substring(0, PDB.getName().indexOf('.')));
				 //++count;
				 //}
			 }
			//if(Found==true) {
				// FileUtils.copyFile(new File("/Users/emadalharbi/Desktop/PhDYork/jcsg202Results/Run 7:3:2018/Phenix46/PDBs/"+jscgID+".pdb"),  new File("/Users/emadalharbi/Desktop/PhDYork/jcsg202Results/Run 7:3:2018/Phenix46/PDBs1/"+jscgID+".pdb"));

				// System.out.println(Log.getName().substring(0, Log.getName().indexOf('.')));
				// ++count;
			//}
		// }
		// System.out.println(count);
		 /*
		 File[] Seqs = new File("/Users/emadalharbi/Desktop/PhDYork/jcsg202/jcsg202chltomModifiedSeq").listFiles();

		 for (File Seq : Seqs) {
			 if(Seq.getName().substring(Seq.getName().indexOf('.'), Seq.getName().length()).equals(".seq")) {
				System.out.println(Seq.getName());	
				
				try (BufferedReader br = new BufferedReader(new FileReader(Seq.getAbsolutePath()))) {
				    String line;
				    int countLessThan=0;
				    String SeqContents="";
				    while ((line = br.readLine()) != null) {
				    	if(line.contains(">"))
					    	   ++countLessThan;
					       if(countLessThan==2) {
					    	   PrintWriter out = new PrintWriter( Seq.getAbsolutePath());
						   out.println(SeqContents);
						   out.close();
								
					    	   break;
					       }   
				       System.out.println(line);
				       SeqContents+=line;
				       SeqContents+=System.lineSeparator();
				    }
				}
			 }
		 }
		 */
		/*
		 File[] WorkingDir = new File("/Users/emadalharbi/Desktop/PhDYork/jcsg202Results/PhenixLogsAndPDBs126/PhenixLogs").listFiles();
		// File[] Logs = new File("/Users/emadalharbi/Desktop/PhDYork/Pipelines/Phenix/PhinexLogs").listFiles();
			//System.out.println(PDBs.length);
		 File[] jscgDataset = new File("/Users/emadalharbi/Desktop/PhDYork/jcsg202/jcsg202chltom").listFiles();
		 for (File jscg : jscgDataset) {
			 boolean found=false;
			 String jscgID= jscg.getName().substring(0, jscg.getName().indexOf('.'));
			 for (File folder : WorkingDir) {
				 String PDBId= folder.getName().substring(0, folder.getName().indexOf('.'));
				 System.out.println(PDBId);
				 if(PDBId.equals(jscgID)) {
				
					 found=true;
				 }
			 }
			 if(found==false) {
				 FileUtils.copyFile(new File("/Users/emadalharbi/Desktop/PhDYork/jcsg202/jcsg202chltom/"+jscgID+".pdb"),  new File("/Users/emadalharbi/Desktop/PhDYork/jcsg202Results/PhenixLogsAndPDBs126/jscg/"+jscgID+".pdb"));
				 FileUtils.copyFile(new File("/Users/emadalharbi/Desktop/PhDYork/jcsg202/jcsg202chltom/"+jscgID+".mtz"),  new File("/Users/emadalharbi/Desktop/PhDYork/jcsg202Results/PhenixLogsAndPDBs126/jscg/"+jscgID+".mtz"));
				 FileUtils.copyFile(new File("/Users/emadalharbi/Desktop/PhDYork/jcsg202/jcsg202chltom/"+jscgID+".seq"),  new File("/Users/emadalharbi/Desktop/PhDYork/jcsg202Results/PhenixLogsAndPDBs126/jscg/"+jscgID+".seq")); 
			 }
		 }
		 */
		 
		 ///System.out.println(Logs.length);
	/*
		 int count=0;
		 for (File folder : WorkingDir) {
			 
			 if(folder.length() > 40960) {
				 //System.out.println(folder.getName());
				 String PDBId= folder.getName().substring(0, folder.getName().indexOf('.'));
				 System.out.println(PDBId);
				 FileUtils.copyFile(folder,  new File("/Users/emadalharbi/Desktop/PhDYork/jcsg202Results/PhenixPartRestults/PhenixLogs1/"+folder.getName()));
				 FileUtils.copyFile(new File("/Users/emadalharbi/Desktop/PhDYork/jcsg202Results/PhenixPartRestults/PDBs/"+PDBId+".pdb"),  new File("/Users/emadalharbi/Desktop/PhDYork/jcsg202Results/PhenixPartRestults/PDBs1/"+PDBId+".pdb"));

				 count++;
			 }
			
		 }
		 */
		 //System.out.println(count);
		 /*
		  * 
		  */
		 
		
		
		
	}
	
}
