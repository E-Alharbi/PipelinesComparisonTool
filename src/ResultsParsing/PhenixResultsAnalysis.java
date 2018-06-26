package ResultsParsing;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Vector;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class PhenixResultsAnalysis {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ExcelSheet e = new ExcelSheet ();
		 XSSFWorkbook w= e.CreateWorkBook();
		 XSSFSheet sheet=e.CreateSheet("Phenix", w);
		Row r= e.CreateRow(sheet);
		Cell c = e.CreateCell(r, 0);
		e.SetCellValue(c, "Data");
		 c = e.CreateCell(r, 1);
		e.SetCellValue(c, "Resolution");
		c = e.CreateCell(r, 2);
		e.SetCellValue(c, "TimeTaking");
		c = e.CreateCell(r, 3);
		e.SetCellValue(c, "R-factor");
		c = e.CreateCell(r, 4);
		e.SetCellValue(c, "Process State");
		 Vector<String> ColData=  ExcelSheet.ReadExcelByColIndex("/Users/emadalharbi/Desktop/PhDYork/BuccaneerResults/DataRunResults.xlsx",0); // to make sure the list of file names in  Excel in same order in all Excel files
		 Vector<String> ColDataAdded= new  Vector<String>();
		 File[] files = new File("/Volumes/TOSHIBA EXT/PhinexResults/PhinexResults/PhinexLogs").listFiles();
		for(int i=0 ; i < ColData.size() ; ++i){
			for (File file : files) {
				// System.out.println(file.getName().substring(file.getName().indexOf(".")+1).trim());
				// System.out.println(ColData.get(i));
				if(ColData.get(i).equals(file.getName().substring(0,file.getName().indexOf(".")).trim())){
					ColDataAdded.add(ColData.get(i));
				String NameOfFile=file.getName().substring(0,file.getName().indexOf(".")).trim();
				System.out.println(NameOfFile);
				String LogTxt=new PhenixResultsAnalysis().readFileAsString("/Volumes/TOSHIBA EXT/PhinexResults/PhinexResults/PhinexLogs/"+NameOfFile+".txt");
			
				String Reso=LogTxt.substring(LogTxt.indexOf("Resolution from datafile:"));
				Reso=Reso.substring(Reso.indexOf("Resolution from datafile:"),Reso.indexOf("\n")).split(" ")[Reso.substring(Reso.indexOf("Resolution from datafile:"),Reso.indexOf("\n")).split(" ").length-1];
				
				String RFactor="";
				
				String Line="";
				for(int s=0 ; s<LogTxt.length();++s){
					Line+=LogTxt.charAt(s);
					if(LogTxt.charAt(s)=='\n'){
						if(Line.contains("Best solution on cycle:")){
							RFactor=Line.split("=")[Line.split("=").length-1].trim();
							RFactor=RFactor.split("/")[0];
							Line="";
						}
					}
				}
				
				//RFactor=RFactor.substring(RFactor.indexOf("R ="));
				//RFactor=RFactor.substring(3,RFactor.indexOf("(")).trim();
				
				  DecimalFormat df = new DecimalFormat("#.##");
		             df.setRoundingMode(RoundingMode.HALF_DOWN);
		            Reso= df.format(Double.parseDouble(Reso));
		            df = new DecimalFormat("#.###");
		            df.setRoundingMode(RoundingMode.HALF_DOWN);
		            RFactor=df.format(Double.parseDouble(RFactor));
		             System.out.println(Reso);
				System.out.println(RFactor);
				String TimeTakig=LogTxt.substring(LogTxt.indexOf("TimeTaking")).split(" ")[1].trim();
				System.out.println(TimeTakig);
				 r= e.CreateRow(sheet);
				 c = e.CreateCell(r, 0);
				e.SetCellValue(c, file.getName().substring(0,file.getName().indexOf('.')));
				 c = e.CreateCell(r, 1);
				e.SetCellValue(c, Reso);
				c = e.CreateCell(r, 2);
				e.SetCellValue(c, TimeTakig);
				c = e.CreateCell(r, 3);
				e.SetCellValue(c, RFactor);
				c = e.CreateCell(r, 4);
				e.SetCellValue(c, "Success");
				}
				
			 }
		}
		 e.WriteExcel(w, "PhenixDataRunResults");	 
		 
		 for(int i=0 ; i < ColData.size() ; ++i){
			 if(!ColDataAdded.contains(ColData.get(i)))
				 System.out.println(ColData.get(i));
		 }
	}

	public String readFileAsString(String filePath) throws IOException {
        try {
			StringBuffer fileData = new StringBuffer();
			BufferedReader reader = new BufferedReader(
			        new FileReader(filePath));
			char[] buf = new char[1024];
			int numRead=0;
			while((numRead=reader.read(buf)) != -1){
			    String readData = String.valueOf(buf, 0, numRead);
			    fileData.append(readData);
			}
			reader.close();
			return fileData.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();return "";
		}
    }
}
