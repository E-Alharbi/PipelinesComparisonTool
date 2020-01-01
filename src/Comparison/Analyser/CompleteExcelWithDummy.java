package Comparison.Analyser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

public class CompleteExcelWithDummy {

	public void FillInWithDummy(File Complete, File Missing) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub

	ExcelLoader e = new ExcelLoader();
	Vector<ExcelContents> MissingContainer = e.ReadExcel(Missing.getAbsolutePath());
	Vector<ExcelContents> CompleteContainer = e.ReadExcel(Complete.getAbsolutePath());
	
	for(int i=0 ; i <CompleteContainer.size()  ; ++i) {
		boolean Found=false;
    for(int c=0; c<MissingContainer.size() ;++c) {
    	
	if(MissingContainer.get(c).PDB_ID.equals(CompleteContainer.get(i).PDB_ID)) {
		Found=true;
		break;
	}
}
    if(Found==false) {
    	ExcelContents ee = new ExcelContents ();
    	ee.PDB_ID=CompleteContainer.get(i).PDB_ID;
    	ee.Resolution=CompleteContainer.get(i).Resolution;
    	ee.PDBIDTXT=CompleteContainer.get(i).PDBIDTXT;
    	ee.BuiltPDB="F";
    	ee.R_free="0";
    	ExcelContents Temp=CompleteContainer.get(i);
    	
    	Temp=ee;
   // Bucc.get(i).BuiltPDB="F";
    //BuccContainer.add(Bucc.get(i));
    	MissingContainer.add(Temp);
    //System.out.println(Missing.getName());
    }
	}
	Missing.delete();
	new ExcelSheet().FillInExcel(MissingContainer, Missing.getAbsolutePath());
	


	

	}

}
