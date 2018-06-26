package Utilities;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

import ResultsParsing.DataContainer;
import ResultsParsing.ExcelSheet;
import ResultsParsing.LoadExcel;
import Run.RunningPram;

public class UpdateExcel {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub

		LoadExcel e = new LoadExcel();
		Vector<DataContainer> Crank = new Vector<DataContainer>();
		Crank = e.ReadExcel("/Users/emadalharbi/Desktop/PhDYork/jcsg202Results/ToolsResults12_3_2018/Crank.xlsx");
		Vector<DataContainer> Arp = e.ReadExcel("/Users/emadalharbi/Desktop/PhDYork/jcsg202Results/ToolsResults12_3_2018/ARPwARP.xlsx");
		for(int i=0; i< Crank.size();++i) {
			
			for(int a=0; a< Arp.size();++a) {
				if(Crank.get(i).PDB_ID.equals(Arp.get(a).PDB_ID)) {
					System.out.println(Crank.get(i).OptimalR_factor);
					System.out.println(Arp.get(a).OptimalR_factor);
					Crank.get(i).OptimalR_factor=Arp.get(a).OptimalR_factor;
					Crank.get(i).Resolution=Arp.get(a).Resolution;
				}
			}
		}
		
		new ExcelSheet().FillInExcel(Crank, "Crank");
	}

}
