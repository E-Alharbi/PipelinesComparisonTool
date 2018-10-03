package ToolsExecation.SingleThread;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class castat2 {

	public Castat2Data Runcastat2(String PathPDB1,String PathPDB2,String castat2Path){
		 String[]callAndArgs= {
				 
				 castat2Path,
				 PathPDB1,
				 PathPDB2,
					};
		 //System.out.println("PDB1 "+PathPDB1);
		 //System.out.println("PDB2 "+PathPDB2);
try{
					 Process p = Runtime.getRuntime().exec(callAndArgs);

						             

					BufferedReader stdInput = new BufferedReader(new 

						                  InputStreamReader(p.getInputStream()));



						             BufferedReader stdError = new BufferedReader(new 

						                  InputStreamReader(p.getErrorStream()));

String st;
Castat2Data c = new Castat2Data();
						             while ((st = stdInput.readLine()) != null) {
						            	 //System.out.println(st);
						            	  String []Results= st.split(" ");
						            	  c.NumberOfAtomsInFirstPDB=Results[0];
						            	  c.NumberOfAtomsInSecondPDB=Results[1];
						            	  c.NumberOfAtomsInFirstNotInSecond=Results[2];
						            	  c.NumberOfAtomsInSecondNotInFirst=Results[3];
						            	  c.Seq=Results[4];
						            	  c.n1m2=Results[5];
						            	  c.n2m1=Results[6];
						             }

						           
						             while ((st = stdError.readLine()) != null) {

						                 System.out.println(st);

						             }

						             return c; 

						            // System.exit(0);

						         }

						         catch (IOException e) {

						             System.out.println("exception occured");

						             e.printStackTrace();

						             //System.exit(-1);
						            
						             return null;
						         }
						
	}
	
}
