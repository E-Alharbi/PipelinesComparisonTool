package ToolsExecation.SingleThread;
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

import Analyser.Factors;
import Run.RunComparison;
import Utilities.FilesManagements;

public class Refmac {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		
		/*
		File[] files = new File("/Users/emadalharbi/Desktop/PhDYork/jcsg-datasets-54/data").listFiles();
		 Vector<String> FilesNames= new Vector <String>();
			 for (File file : files) {
				 if(!FilesNames.contains(file.getName().substring(0,file.getName().indexOf('.')))){
				 System.out.println(file.getAbsoluteFile());
				 new Refmac().RunRefmac("/Users/emadalharbi/Desktop/PhDYork/jcsg-datasets-54/data",file.getParent(),"/Applications/ccp4-7.0/bin/refmac5","OptimalRfactor",file.getName());
				 FilesNames.add(file.getName().substring(0,file.getName().indexOf('.')));
				 }
				 }
		*/
	}

	public Factors RunRefmac (String mtzPath , String pdbPath, String RefmacPath,String ToolName, String FileName, String LIBIN) throws IOException{
		
		 String st = null;
		
		// System.out.println("mtzPath "+mtzPath+"/"+FileName);
		// System.out.println("pdbPath "+pdbPath);
		// System.out.println("RefmacPath "+RefmacPath);
		// System.out.println("ToolName "+ToolName);
		// System.out.println("FileName "+FileName);
		 String[] callAndArgs= {
					"sh","./refmacscript.sh",
					RefmacPath,
					mtzPath,
					FileName+"Ref.mtz",
					pdbPath,
					FileName+"Ref.pdb",					
					};

		 Process  p = Runtime.getRuntime().exec(callAndArgs);

						             

					BufferedReader stdInput = new BufferedReader(new 

						                  InputStreamReader(p.getInputStream()));



						             BufferedReader stdError = new BufferedReader(new 

						                  InputStreamReader(p.getErrorStream()));



					String LogTxt="";
					Factors F= new Factors();
					boolean IsReadRfactor=false;
						             while ((st = stdInput.readLine()) != null) {
						            	//System.out.println(st);
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
						    						//System.out.println("Res1 "+Reso);
						    						//System.out.println(R);
						    					}
						    					//System.out.println("Res "+Reso);
						    					
						    					F.Reso=Reso;
						    				} catch (Exception e) {
						    					// TODO Auto-generated catch block
						    					System.out.println("Unable to parse the resolution for "+FileName);
						    					
						    					
						    					F.Reso="None";
						    					//e.printStackTrace();
						    					
						    				}
						             	}
						             }
				//System.out.print("R-factor "+F.RFactor);
				//System.out.print("Free-factor "+F.FreeFactor);
				//System.out.print("ToolName "+ToolName);
				//new RunComparison().CheckDirAndFile("./RefmacLogs"+ToolName);
				//try(  PrintWriter out = new PrintWriter( "./RefmacLogs"+ToolName+"/"+FileName+".txt" )  ){
			//	    out.println( LogTxt );
			//	}
				//new FilesManagements().RemoveFile(new File(FileName+"Ref.mtz"));
				//new FilesManagements().RemoveFile(new File(FileName+"Ref.pdb"));
				F.Log=LogTxt;
				return F;
	}
}
