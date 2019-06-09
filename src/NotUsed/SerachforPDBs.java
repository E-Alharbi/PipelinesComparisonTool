package NotUsed;
/**
*
* @author Emad Alharbi
* University of York
*/
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.apache.commons.io.FileUtils;

import Comparison.Analyser.ExcelSheet;

public class SerachforPDBs {

	//Not Used
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		File root = new File("/Users/emadalharbi/Desktop/PhDYork/ARPResults");
		 Vector<String> ColData=  ExcelSheet.ReadExcelByColIndex("/Users/emadalharbi/Desktop/PhDYork/BuccaneerResults/DataRunResults.xlsx",0);
		
				for(int i=0 ; i < ColData.size() ; ++i){
					 try {
						 
						 String fileName = ColData.get(i)+"_warpNtrace.pdb";
				            boolean recursive = true;

				            Collection files = FileUtils.listFiles(root, null, recursive);

				            for (Iterator iterator = files.iterator(); iterator.hasNext();) {
				                File file = (File) iterator.next();
				                if (file.getName().equals(fileName)){
				                    System.out.println(file.getAbsolutePath());
				                    
				                    FileUtils.copyFileToDirectory(file,  new File("/Users/emadalharbi/Desktop/PhDYork/ARPResults/PDBs"));

				                   
				                }
				            }
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
				}
	}
		
       
       
	

}
