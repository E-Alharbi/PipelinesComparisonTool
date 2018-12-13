package Run;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

import Analyser.DataContainer;
import Analyser.ExcelSheet;
import Analyser.ExcelLoader;

public class LargeCasesRemover {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub

		File [] DatasetFolders  = new File("/Volumes/PhDHardDrive/jcsg1200Results/Fasta/Run6Copy").listFiles();
		
		for(File Dataset : DatasetFolders) {
			if(Dataset.isDirectory()) {
				new RunComparison().CheckDirAndFile(Dataset+"NoLargeStr");
				for(File Excel : Dataset.listFiles()) {
					 ExcelLoader e = new ExcelLoader();
						Vector<DataContainer> Container = e.ReadExcel(Excel.getAbsolutePath());
						Vector<DataContainer> ContainerWithNoLargeStr =new Vector<DataContainer>();
				for(int i=0 ; i < Container.size() ; ++i) {
					if(!Container.get(i).PDBIDTXT.contains("2pnk")) {
						ContainerWithNoLargeStr.add(Container.get(i));
					}
				}
				new ExcelSheet().FillInExcel(ContainerWithNoLargeStr, Dataset+"NoLargeStr/"+Excel.getName());
				}
			}
		}
	}

}
