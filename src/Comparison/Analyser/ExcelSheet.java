package Comparison.Analyser;
/**
*
* @author Emad Alharbi
* University of York
*/
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Vector;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Comparison.Runner.RunComparison;
public class ExcelSheet {

	//Writing the excel sheet. 
	// Any new change in the excel structure should be made from here. 
	int Row;
	public XSSFWorkbook CreateWorkBook(){
		Row=0;
		 XSSFWorkbook workbook = new XSSFWorkbook();
		 return workbook;
	}
	public XSSFSheet CreateSheet(String SheetName,XSSFWorkbook workbook ){
		XSSFSheet sheet = workbook.createSheet("Data");
		return sheet;
	}
	public Row CreateRow(XSSFSheet sheet){
		 Row row = sheet.createRow(Row++);
		 return row;
	}
	public Cell CreateCell(Row row, int col){
		 Cell cell = row.createCell(col);
		 return cell;
	}
	public void SetCellValue(Cell cell,String val){
		cell.setCellValue(val);
	}
	public void SetCellColor(Cell cell,String val,XSSFWorkbook workbook){
		 CellStyle style = workbook.createCellStyle();
		
		
	}
	public void WriteExcel(XSSFWorkbook workbook, String FileName) throws FileNotFoundException, IOException{
		
		if(FileName.contains(".xlsx")) {
			try (FileOutputStream outputStream = new FileOutputStream(FileName)) {
	            workbook.write(outputStream);
	        }
		}
		else {
			try (FileOutputStream outputStream = new FileOutputStream(FileName+".xlsx")) {
	            workbook.write(outputStream);
	        }
		}
		
	}
	public void FillInExcel(Vector <ExcelContents> DataContainer, String ToolName) throws FileNotFoundException, IOException{
		ExcelSheet e = new ExcelSheet ();
		 XSSFWorkbook w= e.CreateWorkBook();
		 
		 XSSFSheet sheet=e.CreateSheet(ToolName, w);
		Row r= e.CreateRow(sheet);
		Cell c = e.CreateCell(r, 0);
		e.SetCellValue(c, "File");
		 c = e.CreateCell(r, 1);
		e.SetCellValue(c, "Resolution");
		c = e.CreateCell(r, 2);
		e.SetCellValue(c, "TimeTaking");
		c = e.CreateCell(r, 3);
		e.SetCellValue(c, "R-work");
		c = e.CreateCell(r, 4);
		e.SetCellValue(c, "R-free");
		c = e.CreateCell(r, 5);
		e.SetCellValue(c, " Diff R-work and R-free");
		c = e.CreateCell(r, 6);
		e.SetCellValue(c, "Overfitting?");
      
       
		c = e.CreateCell(r, 7);
       e.SetCellValue(c, "R-work (0 Cycle)");
		c = e.CreateCell(r, 8);
		e.SetCellValue(c, "R-free (0 Cycle)");
		c = e.CreateCell(r, 9);
		e.SetCellValue(c, "Deposited R-work");
		c = e.CreateCell(r, 10);
		e.SetCellValue(c, "Number of Atoms in First PDB");
		
		c = e.CreateCell(r, 11);
		e.SetCellValue(c, "Number of Atoms in Second PDB");
		
		c = e.CreateCell(r, 12);
		e.SetCellValue(c, "Number Of Atoms In First Not In Second");
		
		c = e.CreateCell(r, 13);
		e.SetCellValue(c, "Number Of Atoms In Second Not In First");
		
		c = e.CreateCell(r, 14);
		e.SetCellValue(c, "Seq(r/(n1n2+n2n1))");
		
		c = e.CreateCell(r, 15);
		e.SetCellValue(c, "n1m2");
		
		c = e.CreateCell(r, 16);
		e.SetCellValue(c, "n2m1");
		
		c = e.CreateCell(r, 17);
		e.SetCellValue(c, "F-mapCorrelation");
		
		c = e.CreateCell(r, 18);
		e.SetCellValue(c, "E-mapCorrelation");
		c = e.CreateCell(r, 19);
		e.SetCellValue(c, "BuiltPDB");
		
		c = e.CreateCell(r, 20);
		e.SetCellValue(c, "Warring execution time");
		
		c = e.CreateCell(r, 21);
		e.SetCellValue(c, "Warring log Size");
		c = e.CreateCell(r, 22);
		e.SetCellValue(c, "Exception (no log file)");
		
		c = e.CreateCell(r, 23);	
		e.SetCellValue(c, "Ramachandran outliers"); 
	 c = e.CreateCell(r, 24);
	 e.SetCellValue(c, "Ramachandran favored");; 
		 c = e.CreateCell(r, 25);
		 e.SetCellValue(c,"Rotamer outliers");
		 c = e.CreateCell(r, 26);
		
		 e.SetCellValue(c,"Clash score");
		 c = e.CreateCell(r, 27);
		 ; 
		 e.SetCellValue(c,"RMS bonds");
		 c = e.CreateCell(r, 28);
		  ; 
		 e.SetCellValue(c,"RMS angles");
		 c = e.CreateCell(r, 29);
		  ;
		 e.SetCellValue(c,"MolProbity score");
		 c = e.CreateCell(r, 30);
		  ;
		 e.SetCellValue(c,"Rwork");
		 c = e.CreateCell(r, 31);
		  ;
		 e.SetCellValue(c,"Rfree");
		 c = e.CreateCell(r, 32);
		  ;
		 e.SetCellValue(c,"Refinement program");
		 
		 c = e.CreateCell(r, 33);
		  ;
		 e.SetCellValue(c,"Intermediate");
		 c = e.CreateCell(r, 34);
		  ;
		 e.SetCellValue(c,"Completeness");
		 
		 c = e.CreateCell(r, 35);
		  ;
		 e.SetCellValue(c,"PDB ID");
		
		for (int i=0; i < DataContainer.size() ; ++i){
			 r= e.CreateRow(sheet);
			 c = e.CreateCell(r, 0);
			e.SetCellValue(c, DataContainer.get(i).PDB_ID);
			 c = e.CreateCell(r, 1);
			e.SetCellValue(c, DataContainer.get(i).Resolution);
			c = e.CreateCell(r, 2);
			e.SetCellValue(c, DataContainer.get(i).TimeTaking);
			c = e.CreateCell(r, 3);
			e.SetCellValue(c, DataContainer.get(i).R_factor);
			c = e.CreateCell(r, 4);
			e.SetCellValue(c,  DataContainer.get(i).R_free);
			c = e.CreateCell(r, 5);
			DecimalFormat df = new DecimalFormat("#.##");
            df.setRoundingMode(RoundingMode.HALF_DOWN);
             
			e.SetCellValue(c, String.valueOf( DataContainer.get(i).R_factorΔR_free));
			
			c = e.CreateCell(r, 6);
			e.SetCellValue(c, DataContainer.get(i).Overfitting);
			c = e.CreateCell(r, 7);
			e.SetCellValue(c , DataContainer.get(i).R_factor0Cycle );
			
			c = e.CreateCell(r, 8);
			e.SetCellValue(c , DataContainer.get(i).R_free0Cycle );
			
			c = e.CreateCell(r, 9);
			e.SetCellValue(c, DataContainer.get(i).OptimalR_factor);
			
			c = e.CreateCell(r, 10);
			e.SetCellValue(c, DataContainer.get(i).NumberofAtomsinFirstPDB);
			c = e.CreateCell(r, 11);
			e.SetCellValue(c, DataContainer.get(i).NumberofAtomsinSecondPDB);
			
			c = e.CreateCell(r, 12);
			e.SetCellValue(c, DataContainer.get(i).NumberOfAtomsInFirstNotInSecond);
			
			c = e.CreateCell(r, 13);
			e.SetCellValue(c, DataContainer.get(i).NumberOfAtomsInSecondNotInFirst);
			
			c = e.CreateCell(r, 14);
			e.SetCellValue(c, DataContainer.get(i).Seqrn1n2n2n1);
			
			c = e.CreateCell(r, 15);
			e.SetCellValue(c, DataContainer.get(i).n1m2);
			
			c = e.CreateCell(r, 16);
			e.SetCellValue(c, DataContainer.get(i).n2m1);
			
			c = e.CreateCell(r, 17);
			e.SetCellValue(c, DataContainer.get(i).F_mapCorrelation);
			
			c = e.CreateCell(r, 18);
			e.SetCellValue(c, DataContainer.get(i).E_mapCorrelation);
			
			c = e.CreateCell(r, 19);
			e.SetCellValue(c, DataContainer.get(i).BuiltPDB);
			c = e.CreateCell(r, 20);
			e.SetCellValue(c, DataContainer.get(i).WarringTimeTaking);
			c = e.CreateCell(r, 21);
			e.SetCellValue(c, DataContainer.get(i).WarringLogFile);
			c = e.CreateCell(r, 22);
			e.SetCellValue(c, DataContainer.get(i).ExceptionNoLogFile);
			
			c = e.CreateCell(r, 23);
			e.SetCellValue(c, DataContainer.get(i).molProbityData.RamachandranOutliers);
			 
			 c = e.CreateCell(r, 24);
			 ; 
				e.SetCellValue(c, DataContainer.get(i).molProbityData.RamachandranFavored);
			 c = e.CreateCell(r, 25);
			 ;
			 e.SetCellValue(c, DataContainer.get(i).molProbityData.RotamerOutliers);
			 c = e.CreateCell(r, 26);
			 ; 
			 e.SetCellValue(c, DataContainer.get(i).molProbityData.Clashscore);
			 c = e.CreateCell(r, 27);
			 ; 
			 e.SetCellValue(c, DataContainer.get(i).molProbityData.RMSBonds);
			 c = e.CreateCell(r, 28);
			  ; 
			 e.SetCellValue(c, DataContainer.get(i).molProbityData.RMSAngles);
			 c = e.CreateCell(r, 29);
			  ;
			 e.SetCellValue(c, DataContainer.get(i).molProbityData.MolProbityScore);
			 c = e.CreateCell(r, 30);
			  ;
			 e.SetCellValue(c, DataContainer.get(i).molProbityData.RWork);
			 c = e.CreateCell(r, 31);
			  ;
			 e.SetCellValue(c, DataContainer.get(i).molProbityData.RFree);
			 c = e.CreateCell(r, 32);
			  ;
			 e.SetCellValue(c, DataContainer.get(i).molProbityData.RefinementProgram);
			 
			 c = e.CreateCell(r, 33);
			  ;
			 e.SetCellValue(c, DataContainer.get(i).Intermediate);
			 c = e.CreateCell(r, 34);
			  ;
			 e.SetCellValue(c, DataContainer.get(i).Completeness);
			 
			 c = e.CreateCell(r, 35);
			  ;
			 e.SetCellValue(c, DataContainer.get(i).PDBIDTXT);
		
		}
		
		if (ToolName.equals("Buccaneer")){
			new RunComparison().CheckDirAndFile("BuccaneerResults");
			e.WriteExcel(w, ToolName);	
		}
		else if (ToolName.equals("Phenix")){
			new RunComparison().CheckDirAndFile("PhenixResults");
			e.WriteExcel(w, ToolName);	
		}
		else if (ToolName.equals("ARPwARP")){
			new RunComparison().CheckDirAndFile("wArpResults");
			e.WriteExcel(w, ToolName);	
		}
		else if (ToolName.equals("ARPwARPAfterBuccaneeri1")){
			new RunComparison().CheckDirAndFile("wArpResults");
			e.WriteExcel(w, ToolName);	
		}
		else if (ToolName.equals("Crank")){
			new RunComparison().CheckDirAndFile("Crank");
			e.WriteExcel(w, ToolName);	
		}
		else if (ToolName.equals("Buccaneeri2")){
			new RunComparison().CheckDirAndFile("Buccaneeri2");
			e.WriteExcel(w, ToolName);	
		}
		else if (ToolName.equals("Buccaneeri2W")){
			new RunComparison().CheckDirAndFile("Buccaneeri2W");
			e.WriteExcel(w, ToolName);	
		}
		else {
			e.WriteExcel(w, ToolName);
			
		}
		
	}
	 public static void main(String[] args) throws IOException {
		 
		 ExcelSheet e = new ExcelSheet ();
		 XSSFWorkbook w= e.CreateWorkBook();
		 XSSFSheet sheet=e.CreateSheet("Buccaneer", w);
		Row r= e.CreateRow(sheet);
		Cell c = e.CreateCell(r, 0);
		e.SetCellValue(c, "test");
		
		 r= e.CreateRow(sheet);
		 c = e.CreateCell(r, 0);
		e.SetCellValue(c, "test");
		
		e.WriteExcel(w, "test");
		
	    }

	 public static Vector<String> ReadExcelByColIndex(String FilePath, int ColIndex) throws IOException{
		 Vector<String> ColData= new Vector <String>();
		 
	        FileInputStream inputStream = new FileInputStream(new File(FilePath));
	         
	        Workbook workbook = new XSSFWorkbook(inputStream);
	        Sheet firstSheet = workbook.getSheetAt(0);
	        Iterator<Row> iterator = firstSheet.iterator();
	         
	        while (iterator.hasNext()) {
	            Row nextRow = iterator.next();
	            Iterator<Cell> cellIterator = nextRow.cellIterator();
	             
	            while (cellIterator.hasNext()) {
	                Cell cell = cellIterator.next();
	                 
	                switch (cell.getColumnIndex()) {
	                    case 0:
	                    	ColData.add(cell.getStringCellValue().trim());
	                        
	                        break;
	                   
	                }
	               
	            }
	            
	        }
	         
	        workbook.close();
	        inputStream.close();
	        return ColData;
	 }
}
