package ToolsExecution;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Vector;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Analyser.ExcelSheet;
import Analyser.Results;

public class RunArpFc {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

	
		
		
		
		
		 Vector<String> FilesNames= new Vector <String>();
		File[] Unprocessfiles = new File("/Users/emadalharbi/Desktop/PhDYork/ARPproccessedData").listFiles();
		
		 for (File file : Unprocessfiles) {
			 FilesNames.add(file.getName().substring(0,file.getName().indexOf('.')));
			 System.out.println(file.getName().substring(0,file.getName().indexOf('.')));
		 }
		
		System.out.println(FilesNames.size());
	
    
     File[] files = new File("/Users/emadalharbi/Desktop/PhDYork/jcsg-datasets-54/data").listFiles();
		
		 for (File file : files) {
	if(!FilesNames.contains(file.getName().substring(0,file.getName().indexOf('.')))){
    String FileName=file.getParentFile()+"/"+file.getName().substring(0,file.getName().indexOf('.'));
	
	FilesNames.add(file.getName().substring(0,file.getName().indexOf('.')));
   System.out.println(file.getName().substring(0,file.getName().indexOf('.')));
   Results res= new RunArpFc().RunArpTool(FileName,file.getName().substring(0,file.getName().indexOf('.')));
	
	
	
   
	try(  PrintWriter out = new PrintWriter( "./ArpLogs/"+file.getName().substring(0,file.getName().indexOf('.'))+".txt" )  ){
	    out.println( res.LogFile );
	}
	}
	
}
		
		
	}

	Results RunArpTool(String FilePathAndName,String FileName){
		 String st = null;
		 Date ProStartTime = new java.util.Date();
		 Results res= new Results();
		 
		         try {

	
	String mtzin=FilePathAndName+".mtz";
String seqin=FilePathAndName+".seq";
//String[]callAndArgss= {"source","/Applications/ccp4-7.0/setup-scripts/ccp4.setup-sh"};
//Process pp = Runtime.getRuntime().exec(callAndArgss);
	 String[]callAndArgs= {
		 
	"/Applications/arp_warp_7.6/share/auto_tracing.sh",
	"datafile",mtzin,
	"workdir","/Users/emadalharbi/Desktop/PhDYork/ARPlogs",
	"phibest","parrot.F_phi.phi",
	"fom","FOM",
	"seqin",seqin};

	 Process p = Runtime.getRuntime().exec(callAndArgs);

		             

	BufferedReader stdInput = new BufferedReader(new 

		                  InputStreamReader(p.getInputStream()));



		             BufferedReader stdError = new BufferedReader(new 

		                  InputStreamReader(p.getErrorStream()));



		             // read the output
	String Resolution="";
	String Rfactor="";
		             while ((st = stdInput.readLine()) != null) {
		            	 System.out.println(st);
		            	 res.LogFile+=st+"\n";
	                 if(st.contains("Resolution range:")){
	                	
	                  Resolution=st;
		              
	                 }
	                 if(st.contains("After refmac, R =")){
		
	               Rfactor=st;
	   	              
	                    }
	                
		             }

		            System.out.println("Resolution "+Resolution);
	                System.out.println("Rfactor "+Rfactor);
	                double difference = new java.util.Date().getTime() - ProStartTime.getTime(); 
	                DecimalFormat df = new DecimalFormat("#.##");
	                df.setRoundingMode(RoundingMode.HALF_DOWN);
	                String TimeTaking= df.format((difference/1000)/60);
	         		System.out.println("Time Taking "+TimeTaking ) ;
	         		res.LogFile+="TimeTaking "+TimeTaking+"\n";
		             // read any errors

	         		res.Roslution=Resolution;
	         		res.Rfactor=Rfactor;
	         		res.TimeTaking=TimeTaking;
	         		res.ProcessStatus="Success";
		             while ((st = stdError.readLine()) != null) {

		                 System.out.println(st);

		             }

		             

		            // System.exit(0);

		         }

		         catch (IOException e) {

		             System.out.println("exception occured");

		             e.printStackTrace();

		             //System.exit(-1);
		             res.ProcessStatus="Failed";
		             return res;
		         }
		 return res;
	}
	
	
}
