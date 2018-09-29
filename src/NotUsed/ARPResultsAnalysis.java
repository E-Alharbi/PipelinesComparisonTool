package NotUsed;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Analyser.ExcelSheet;


public class ARPResultsAnalysis {

	// Not Used!!!
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ExcelSheet e = new ExcelSheet ();
		 XSSFWorkbook w= e.CreateWorkBook();
		 XSSFSheet sheet=e.CreateSheet("ARP/wARP", w);
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
		 Vector<String> ColData=  ExcelSheet.ReadExcelByColIndex("/Users/emadalharbi/Desktop/PhDYork/BuccaneerResults/DataRunResults.xlsx",0);
		
		 File[] files = new File("/Users/emadalharbi/Desktop/PhDYork/ARPlogs/ArpLogs").listFiles();
		for(int i=0 ; i < ColData.size() ; ++i){
			for (File file : files) {
				// System.out.println(file.getName().substring(file.getName().indexOf(".")+1).trim());
				// System.out.println(ColData.get(i));
				if(ColData.get(i).equals(file.getName().substring(0,file.getName().indexOf(".")).trim())){
				String NameOfFile=file.getName().substring(0,file.getName().indexOf(".")).trim();
				System.out.println(NameOfFile);
				String LogTxt=new ARPResultsAnalysis().readFileAsString("/Users/emadalharbi/Desktop/PhDYork/ARPlogs/ArpLogs/"+NameOfFile+".txt");
			
				String Reso=LogTxt.substring(LogTxt.indexOf("Resolution range:"));
				Reso=Reso.substring(Reso.indexOf("Resolution range:"),Reso.indexOf("\n")).split(" ")[Reso.substring(Reso.indexOf("Resolution range:"),Reso.indexOf("\n")).split(" ").length-1];
				System.out.println(Reso);
				String RFactor="";
				
				String Line="";
				for(int s=0 ; s<LogTxt.length();++s){
					Line+=LogTxt.charAt(s);
					if(LogTxt.charAt(s)=='\n'){
						if(Line.contains("R =")){
							RFactor=Line;
							Line="";
						}
					}
				}
				
				RFactor=RFactor.substring(RFactor.indexOf("R ="));
				RFactor=RFactor.substring(3,RFactor.indexOf("(")).trim();
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
		 e.WriteExcel(w, "ArpDataRunResults");	 
	}

	public String readFileAsString(String filePath) throws IOException {
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
    }
}
