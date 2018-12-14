package NotUsed;
/**
*
* @author Emad Alharbi
* University of York
*/
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class PhenixPDBsShearcher {

	//Not Used.
	
	public static void main(String[] args) throws IOException {
		
		 
		
		 File[] files = new File("/Volumes/PhDHardDrive/PhinexResults/PhinexResults/PhinexScript").listFiles();
			for (File file : files) {
				System.out.println(file.getAbsolutePath());
				System.out.println(file.getName());
				String LogTxt=new PhenixResultsAnalysis().readFileAsString(file.getAbsolutePath()+"/"+file.getName()+"1.log");
                String Modelname="";
				try {
					Modelname = LogTxt.substring(LogTxt.indexOf("data="), LogTxt.indexOf(".mtz")).replace("data=", "");
					 System.out.println(new File(Modelname).getName());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
               
                
                try {
					FileUtils.copyFile(new File(file.getAbsolutePath()+"/overall_best.pdb"),  new File("/Volumes/PhDHardDrive/PhenixPDBs/"+new File(Modelname).getName()+".pdb"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					
				}
			}
		
		/*
		// TODO Auto-generated method stub
		 File[] files = new File("/Volumes/TOSHIBA EXT/PhinexResults/PhinexResults/PhinexLogs").listFiles();
			//for(int i=0 ; i < ColData.size() ; ++i){
				for (File file : files) {
					System.out.println(file.getName());
					String LogTxt=new ArpResultsAnalysis2().readFileAsString(file.getAbsolutePath());
					String ModelPath=LogTxt.substring(LogTxt.indexOf("pdb_file:"));
					ModelPath=ModelPath.substring(ModelPath.indexOf(":")+2,ModelPath.indexOf(".pdb"));
					System.out.println(ModelPath);
					if(ModelPath.contains("/Volumes/E/")){
						ModelPath=ModelPath.replace("/Volumes/E/", "/Volumes/TOSHIBA EXT/PhinexResults/");
					}
					if(ModelPath.contains("/Users/emadalharbi/Downloads/RunBacuneerScript/")){
						ModelPath=ModelPath.replace("/Users/emadalharbi/Downloads/RunBacuneerScript/", "/Volumes/TOSHIBA EXT/PhinexResults/");
					}
					File pdb= new File(ModelPath+".pdb");
					
					try {
						FileUtils.copyFile(pdb,  new File("/Volumes/TOSHIBA EXT/PhinexResults/PhinexResults/PhenixPDBs/"+file.getName().substring(0,file.getName().indexOf('.'))+".pdb"));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						//ModelPath=ModelPath.replace("/Volumes/TOSHIBA EXT/PhinexResults/", "/Volumes/TOSHIBA EXT/PhinexResults/PhinexResults/");
						//FileUtils.copyFile(pdb,  new File("/Volumes/TOSHIBA EXT/PhinexResults/PhinexResults/PhenixPDBs/"+file.getName().substring(0,file.getName().indexOf('.'))+".pdb"));
					}
					
					}
			*/	
	}

}
