package Comparison.Analyser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

/**
*
* @author Emad Alharbi
* University of York
*/
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelLoader {
	

	// Reading an excel file into vector
	
	public Vector<String> ToolsNames = new Vector<String>();
	
	
	public Vector<ExcelContents> ReadExcel(String Excel) {
	      
			Vector<ExcelContents> Container = new Vector<ExcelContents>();
			try {

				FileInputStream excelFile = new FileInputStream(new File(Excel));
				Workbook workbook = new XSSFWorkbook(excelFile);
				Sheet datatypeSheet = workbook.getSheetAt(0);
				Iterator<Row> iterator = datatypeSheet.iterator();
				boolean isheader = true;
				
				while (iterator.hasNext()) {

					if (isheader == true) {// ignore the header
						isheader = false;
						iterator.next();
						if(iterator.hasNext()==false) return Container;
					}
					Row currentRow = iterator.next();

					
					ExcelContents C = new ExcelContents();
					
					C.PDB_ID = currentRow.getCell(0).getStringCellValue();
					C.Resolution = currentRow.getCell(1).getStringCellValue();
					C.TimeTaking = currentRow.getCell(2).getStringCellValue();
					C.R_factor = currentRow.getCell(3).getStringCellValue();
					C.R_free = currentRow.getCell(4).getStringCellValue();
					C.R_factorΔR_free = currentRow.getCell(5).getStringCellValue();

					C.Overfitting = currentRow.getCell(6).getStringCellValue();
					C.R_factor0Cycle = currentRow.getCell(7).getStringCellValue();
					;

					C.R_free0Cycle = currentRow.getCell(8).getStringCellValue();

					C.OptimalR_factor = currentRow.getCell(9).getStringCellValue();

					C.NumberofAtomsinFirstPDB = currentRow.getCell(10).getStringCellValue();
					C.NumberofAtomsinSecondPDB = currentRow.getCell(11).getStringCellValue();

					C.NumberOfAtomsInFirstNotInSecond = currentRow.getCell(12).getStringCellValue();

					C.NumberOfAtomsInSecondNotInFirst = currentRow.getCell(13).getStringCellValue();

					C.Seqrn1n2n2n1 = currentRow.getCell(14).getStringCellValue();

					C.n1m2 = currentRow.getCell(15).getStringCellValue();

					C.n2m1 = currentRow.getCell(16).getStringCellValue();

					C.F_mapCorrelation = currentRow.getCell(17).getStringCellValue();

					C.E_mapCorrelation = currentRow.getCell(18).getStringCellValue();

					C.BuiltPDB = currentRow.getCell(19).getStringCellValue();
					C.WarringTimeTaking = currentRow.getCell(20).getStringCellValue();
					C.WarringLogFile = currentRow.getCell(21).getStringCellValue();
					C.ExceptionNoLogFile = currentRow.getCell(22).getStringCellValue();
					
					if( currentRow.getLastCellNum() > 24) {
					 C.molProbityData.RamachandranOutliers= currentRow.getCell(23).getStringCellValue().trim(); 
					 C.molProbityData.RamachandranFavored= currentRow.getCell(24).getStringCellValue().trim(); 
					 C.molProbityData.RotamerOutliers= currentRow.getCell(25).getStringCellValue().trim();
					 C.molProbityData.Clashscore= currentRow.getCell(26).getStringCellValue().trim(); 
					 C.molProbityData.RMSBonds= currentRow.getCell(27).getStringCellValue().trim(); 
					 C.molProbityData.RMSAngles= currentRow.getCell(28).getStringCellValue().trim() ; 
					 C.molProbityData.MolProbityScore= currentRow.getCell(29).getStringCellValue().trim() ;
					 C.molProbityData.RWork= currentRow.getCell(30).getStringCellValue().trim() ;
					 C.molProbityData.RFree= currentRow.getCell(31).getStringCellValue().trim() ;
					 C.molProbityData.RefinementProgram= currentRow.getCell(32).getStringCellValue().trim() ;
					}
					C.Intermediate=currentRow.getCell(33).getStringCellValue().trim() ;
					C.Completeness=currentRow.getCell(34).getStringCellValue().trim() ;
					C.PDBIDTXT=currentRow.getCell(35).getStringCellValue().trim() ;
					
					Container.add(C);

					

				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return Container;
		}
	
	public Vector<ExcelContents> CheckPDBexists(Vector<Vector<ExcelContents>> AllToolsData, Vector<ExcelContents> ThisToolData) {
		
		Vector<ExcelContents> Container = new Vector<ExcelContents>();
		for (int t = 0; t < ThisToolData.size(); ++t) {
			boolean PDFFoundInAll=true;
		for (int i = 0; i < AllToolsData.size(); ++i) {
			boolean PDFFoundInThis=false;
			for (int m = 0; m < AllToolsData.get(i).size(); ++m) {
				String ThisToolDataPDB= ThisToolData.get(t).PDB_ID;
				ThisToolDataPDB=ThisToolDataPDB.replace("-parrot-hancs", "");
				
				ThisToolDataPDB=ThisToolDataPDB.replace("-parrot-mrncs", "");
				ThisToolDataPDB=ThisToolDataPDB.replace("-parrot-noncs", "");
				String AllToolsDataPDB= AllToolsData.get(i).get(m).PDB_ID;
				AllToolsDataPDB=AllToolsDataPDB.replace("-parrot-hancs", "");
				AllToolsDataPDB=AllToolsDataPDB.replace("-parrot-mrncs", "");
				AllToolsDataPDB=AllToolsDataPDB.replace("-parrot-noncs", "");
				
				
				
				if (ThisToolDataPDB.equals(AllToolsDataPDB) &&AllToolsData.get(i).get(m).BuiltPDB.equals("T")) {
					PDFFoundInThis=true;
					break;

				}
				
				
			}
			if(PDFFoundInThis==false) {
				PDFFoundInAll=false;
              break;
			}
			
		}
		if(PDFFoundInAll==true) 
			Container.add(ThisToolData.get(t));
		}
		return Container;
	}
}
