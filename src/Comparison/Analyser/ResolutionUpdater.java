package Comparison.Analyser;
/**
*
* @author Emad Alharbi
* University of York
*/
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Vector;

import org.apache.commons.io.FileUtils;

import Comparison.Runner.RunningParameter;

public class ResolutionUpdater {

public static void main(String[] args) throws Exception{
		new ResolutionUpdater().Update("/Users/emadalharbi/Downloads/TestBinning");
	}
	/*
	 * This class  updates resolution based on the MTZ not refmac   
	 */
	public  void Update(String Path) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub

		System.out.println("*** Using resolution from Refmac "+RunningParameter.UpdateResolutionRefmac+" ***");
		
		File [] ExcelPath=new File(Path).listFiles();
		
		for(File Folder : ExcelPath) {
			if(Folder.isDirectory()) {
				for(File Excel:Folder.listFiles()) {
					
					ExcelLoader e = new ExcelLoader();
					Vector<ExcelContents> Container = e.ReadExcel(Excel.getAbsolutePath());
					
					for(int i=0 ; i < Container.size() ; ++i) {
						
						if((RunningParameter.UpdateResolutionRefmac.equals("T") && new BigDecimal(RunningParameter.LimitUpdateResolutionFromRefmac).compareTo(new BigDecimal(Container.get(i).Resolution.trim())) == 1 ) || (RunningParameter.UpdateResolutionRefmac.equals("T") && RunningParameter.LimitUpdateResolutionFromRefmac.equals("-1"))) {
							Container.get(i).Resolution=String.valueOf(new BigDecimal(Container.get(i).Resolution.trim()).setScale(1, RoundingMode.HALF_UP).doubleValue());

						}
						else {
							Container.get(i).Resolution=Container.get(i).PDB_ID.substring(5, 8);

						}
						
					}
					//Check if rounded is done or if already rounded 
					Vector<ExcelContents> OriginalContainer = e.ReadExcel(Excel.getAbsolutePath());
					//Vector<Boolean> Rounded=new Vector<Boolean>();
					int Rounded=0;
					for(int i=0 ; i < Container.size() ; ++i) {
						if(Container.get(i).Resolution.trim().equals(OriginalContainer.get(i).Resolution.trim()))
							Rounded++;
					}
					if(Rounded==OriginalContainer.size())
						System.out.println(Excel.getAbsolutePath()+" it seems the resolutions already rounded!");
					FileUtils.deleteQuietly(Excel);
					new ExcelSheet().FillInExcel(Container, Excel.getAbsolutePath());
				}
			}
		}
	}

}
