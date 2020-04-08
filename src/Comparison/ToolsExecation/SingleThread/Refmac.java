package Comparison.ToolsExecation.SingleThread;
/**
*
* @author Emad Alharbi
* University of York
*/
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Vector;

import Comparison.Analyser.REFMACFactors;
import Comparison.Runner.RunComparison;
import Comparison.Utilities.FilesUtilities;

public class Refmac {

	/*
	 * R-work and R-free are parsed from Refmac log. Changing in Refmac log format might lead to incorrect parsing. It is recommended to validate R-work and R-free which parsed from this method with the actual log.       
	 */
	

	public REFMACFactors RunRefmac (String mtzPath , String pdbPath, String RefmacPath,String ToolName, String FileName, String LIBIN, String ExculdeSetOfFREE) throws IOException{
		
		 String st = null;
		
		
		 String[] callAndArgs= {
					"sh","./refmacscript.sh",
					RefmacPath,
					mtzPath,
					FileName+"Ref.mtz",
					pdbPath,
					FileName+"Ref.pdb",	
					ExculdeSetOfFREE,
					};

		 Process  p = Runtime.getRuntime().exec(callAndArgs);

						             

					BufferedReader stdInput = new BufferedReader(new 

						                  InputStreamReader(p.getInputStream()));



						             BufferedReader stdError = new BufferedReader(new 

						                  InputStreamReader(p.getErrorStream()));



					String LogTxt="";
					REFMACFactors F= new REFMACFactors();
					
						             while ((st = stdInput.readLine()) != null) {
						            	
						            	LogTxt+=st+"\n";
						            	if(st.contains("Overall R factor"))
						            	{
						            		F.RFactor=st.split("=")[st.split("=").length-1];
						            		
						            	}
						            	if(st.contains("Free R factor"))
						            	{
						            		F.FreeFactor=st.split("=")[st.split("=").length-1];
						            		
						            	}
						             	if(st.contains("Resolution limits")) {
						             		try {
						    					String R = st;
						    					String Reso="";
						    					while(R.length()!=0) {
						    						if(R.charAt(0)==' ')
						    							Reso="";
						    						Reso+=R.charAt(0);
						    						R=R.substring(1, R.length());
						    						
						    					}
						    					
						    					
						    					F.Reso=Reso;
						    				} catch (Exception e) {
						    					// TODO Auto-generated catch block
						    					System.out.println("Unable to parse the resolution for "+FileName);
						    					
						    					
						    					F.Reso="None";
						    					
						    					
						    				}
						             	}
						             }
				
				F.Log=LogTxt;
				return F;
	}
}
