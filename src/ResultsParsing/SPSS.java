package ResultsParsing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Vector;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SPSS {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		new SPSS().DescriptivesTableReader("/Users/emadalharbi/Desktop/test/hancsReso3.XLSX");
	}

	public Vector<SPSSDescriptivesTable> DescriptivesTableReader(String Excel) throws IOException {
		
		Vector<SPSSDescriptivesTable> table = new Vector<SPSSDescriptivesTable>();
		FileInputStream excelFile = new FileInputStream(new File(Excel));
		Workbook workbook = new XSSFWorkbook(excelFile);
		Sheet datatypeSheet = workbook.getSheetAt(0);
		Iterator<Row> iterator = datatypeSheet.iterator();
		boolean isheader = true;
		String ComparativeFactor="";
		String Pipeline="";
		SPSSDescriptivesTable row = new SPSSDescriptivesTable();
		while (iterator.hasNext()) {

			if (isheader == true) {// ignore the header
				isheader = false;
				iterator.next();
			}
	Row currentRow = iterator.next();
   if(!currentRow.getCell(0).getStringCellValue().trim().equals(""))
	 ComparativeFactor=currentRow.getCell(0).getStringCellValue();

			if(!ComparativeFactor.equals("")) {
		    boolean NewComparativeFactor=false;
		    while(NewComparativeFactor==false) {
		   
		    	boolean MovedNext=false;
		    	//System.out.println("ppp "+currentRow.getCell(1).getStringCellValue());
		    	if(!currentRow.getCell(1).getStringCellValue().trim().equals("") || !Pipeline.equals(""))	{
		    		if(Pipeline.equals(""))
		    		Pipeline=currentRow.getCell(1).getStringCellValue();
		    		boolean NewPipline=false;
		    		
		    		while(NewPipline==false) {
		    		  	//System.out.println(currentRow.getCell(1).getStringCellValue());
		    		  	//System.out.println("Pipeline "+Pipeline);
		    		  	//System.out.println(ComparativeFactor);
		    			row.ComparativeFactor=ComparativeFactor;
		    			row.Pipeline=Pipeline;
		    			
		    			if(currentRow.getCell(2).getStringCellValue().trim().equals("N")) {
		    				//System.out.println(currentRow.getCell(4).getNumericCellValue());
		    				row.NumberOfCases=String.valueOf(Math.round(currentRow.getCell(4).getNumericCellValue()));
		    			}
		    			if(currentRow.getCell(2).getStringCellValue().equals("Mean")) {
		    				//System.out.println(currentRow.getCell(4).getNumericCellValue());
		    			row.Mean=String.valueOf(round(currentRow.getCell(4).getNumericCellValue(),2));
		    			}
		    			if(currentRow.getCell(2).getStringCellValue().equals("Std. Deviation")) {
		    				//System.out.println(currentRow.getCell(4).getNumericCellValue());
		    			row.StdDev=String.valueOf(Math.round(currentRow.getCell(4).getNumericCellValue()));
		    			}
		    			if(currentRow.getCell(2).getStringCellValue().equals("Std. Error")) {
		    				//System.out.println(currentRow.getCell(4).getNumericCellValue());
		    			row.StdErr=String.valueOf(Math.round(currentRow.getCell(4).getNumericCellValue()));
		    			}
		    			if(currentRow.getCell(2).getStringCellValue().equals("Minimum")) {
		    				//System.out.println(currentRow.getCell(4).getNumericCellValue());
		    			row.Min=String.valueOf(Math.round(currentRow.getCell(4).getNumericCellValue()));
		    			}
		    			if(currentRow.getCell(2).getStringCellValue().equals("Maximum")) {
		    				//System.out.println(currentRow.getCell(4).getNumericCellValue());
		    			
		    			row.Max=String.valueOf(Math.round(currentRow.getCell(4).getNumericCellValue()));
		    			}
		    			
		    				
		    			if(!currentRow.getCell(1).getStringCellValue().trim().equals("") && !currentRow.getCell(1).getStringCellValue().equals(Pipeline) ) {
		    				
		    				Pipeline=currentRow.getCell(1).getStringCellValue();
		    				
		    				table.add(row);
		    				row = new SPSSDescriptivesTable();
		    			
		    				//break;
		    			}
		    			else {
		    				if(iterator.hasNext()) {
		    				currentRow = iterator.next();
		    				MovedNext=true;
		    				if(!currentRow.getCell(0).getStringCellValue().trim().equals("") && !currentRow.getCell(0).getStringCellValue().trim().equals(ComparativeFactor)) {
					    	 	
					    	 	
					    		ComparativeFactor=currentRow.getCell(0).getStringCellValue();
					    		Pipeline=currentRow.getCell(1).getStringCellValue();
				    			//NewComparativeFactor=true;
					    		//NewPipline=true;
					    		break;
				    		}
		    				}
		    				else
		    					break;
		    			}
		    		}
		    		
		    		
		    	}
		    	if(!currentRow.getCell(0).getStringCellValue().trim().equals("")) {
		    	 //	System.out.println("NewComparativeFactor ");
		    		ComparativeFactor=currentRow.getCell(0).getStringCellValue();
	    			//NewComparativeFactor=true;
	    		}
		  // System.out.println(currentRow.getRowNum());
		    	if(iterator.hasNext() && MovedNext==false)
    				currentRow = iterator.next();
    				else
    					break;
		    //	System.out.println(currentRow.getRowNum());
		    }
				
			
			
			}
				
		}

return table;
	}
	public static float round(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }
}
