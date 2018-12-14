package Analyser;
/**
*
* @author Emad Alharbi
* University of York
*/
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

import org.apache.commons.io.FileUtils;


import org.apache.commons.io.FilenameUtils;
import Run.RunningPram;

public class ResoUpdater {

	/*
	 * This class  updates reso based on the MTZ not refmac   
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub

		File [] ExcelPath=new File("/Users/emadalharbi/Desktop/Arp/").listFiles();
		
		for(File Folder : ExcelPath) {
			if(Folder.isDirectory()) {
				for(File Excel:Folder.listFiles()) {
					System.out.println(Excel.getAbsolutePath());
					ExcelLoader e = new ExcelLoader();
					Vector<DataContainer> Container = e.ReadExcel(Excel.getAbsolutePath());
					for(int i=0 ; i < Container.size() ; ++i) {
						System.out.println(Container.get(i).PDB_ID.substring(5, 8));
						Container.get(i).Resolution=Container.get(i).PDB_ID.substring(5, 8);
					}
					FileUtils.deleteQuietly(Excel);
					new ExcelSheet().FillInExcel(Container, Excel.getAbsolutePath());
				}
			}
		}
	}

}
