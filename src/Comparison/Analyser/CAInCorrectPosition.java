package Comparison.Analyser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Vector;

public class CAInCorrectPosition {

	public  void UpdateCom(File excel) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub

		
		ExcelLoader f = new ExcelLoader();
		Vector<ExcelContents> Pipeline1Excel = f.ReadExcel(excel.getAbsolutePath());
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.HALF_UP);
		System.out.print(new CAInCorrectPosition().IsCalculatedFromSeq(Pipeline1Excel));
		for(int i=0 ; i <Pipeline1Excel.size();++i ) {
			
		if(Pipeline1Excel.get(i).BuiltPDB.equals("T")) {	
		String Completeness= df.format((BigDecimal.valueOf((Double.parseDouble(Pipeline1Excel.get(i).NumberOfAtomsInSecondNotInFirst) * 100.00)/Double.parseDouble(Pipeline1Excel.get(i).NumberofAtomsinFirstPDB)) ));
		
		
		Pipeline1Excel.get(i).Completeness=Completeness;
		}
		}
		excel.delete();
		new ExcelSheet().FillInExcel(Pipeline1Excel, excel.getAbsolutePath());
		
	}
	// This method might predict wrongly when the number of CA in correct position same as the number of CA in the same sequence. It is advised to check excel files manually.       
	public boolean IsCalculatedFromSeq(Vector<ExcelContents> Pipeline1Excel) {
		
	
		for(int i=0 ; i <Pipeline1Excel.size();++i ) {
			if(Pipeline1Excel.get(i).BuiltPDB.equals("T")) {
			DecimalFormat df = new DecimalFormat("#.##");
			df.setRoundingMode(RoundingMode.HALF_UP);
			String Completeness= df.format((BigDecimal.valueOf((Double.parseDouble(Pipeline1Excel.get(i).NumberOfAtomsInSecondNotInFirst) * 100.00)/Double.parseDouble(Pipeline1Excel.get(i).NumberofAtomsinFirstPDB)) ));
if(Pipeline1Excel.get(i).Completeness.equals(Completeness))
	
	return false;
		}
	}
		return true;
	}

}
