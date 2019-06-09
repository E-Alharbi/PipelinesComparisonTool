package NotToSync;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Vector;

import Comparison.Analyser.ExcelContents;
import Comparison.Analyser.ExcelLoader;

public class test3 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		File [] folder = new File("/Volumes/PhDHardDrive/jcsg1200Results/Fasta/VikingRun3ArpNoFreeNoCrank copy/OrginalBuccEx54ExFaliedCases").listFiles();
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.HALF_UP);
		for(File f : folder) {
		if(f.isDirectory()) {
			System.out.println("Pipelien\t|\tCompleteness\t|\tR-work\t|\tRfree");
			for(File excel : f.listFiles()) {
				ExcelLoader e = new ExcelLoader();
				Vector<ExcelContents> Container = new Vector<ExcelContents>();
				Container=e.ReadExcel(excel.getAbsolutePath());
				String RFactor = df.format(BigDecimal.valueOf(Double.valueOf(new ExcelContents().TotalOfRwork(e.ReadExcel(excel.getAbsolutePath() ) ) /e.ReadExcel(excel.getAbsolutePath()).size())));
				String RFree = df.format(BigDecimal.valueOf(Double.valueOf(new ExcelContents().TotalOfRfree(e.ReadExcel(excel.getAbsolutePath() ) ) /e.ReadExcel(excel.getAbsolutePath()).size())));
				System.out.print(excel.getName().substring(0,excel.getName().indexOf(".")));
				System.out.print("\t|\t"+Math.round(new ExcelContents().TotalOfCompleteness(e.ReadExcel(excel.getAbsolutePath() ) ) /e.ReadExcel(excel.getAbsolutePath()).size())   );
				System.out.print("\t|\t"+ RFactor  );
				System.out.print("\t|\t"+RFree  );
				System.out.println("");
			}
		}
	}
	}

}
