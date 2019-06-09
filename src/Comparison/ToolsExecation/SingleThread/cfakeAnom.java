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
			import java.math.RoundingMode;
			import java.text.DecimalFormat;
			import java.util.Vector;

			import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import Comparison.Runner.RunComparison;
import Comparison.Runner.RunningPram;

			public class cfakeAnom {

				public  void Run(String DataPath) throws IOException {
					// TODO Auto-generated method stub

					String OutputFolder = new File(DataPath).getName()+"FakeWaveFakeAnom/";
					new RunComparison().CheckDirAndFile("./"+OutputFolder);
					 File[] files = new File(DataPath).listFiles();
					 Vector<String> ProccessedFiles=new Vector<String>();
					 int count=0;
					 for (File file : files) {
						 String jscgID= file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),"");
						 if(!ProccessedFiles.contains(jscgID)) {
							new cfakeAnom().Ffake(jscgID,DataPath,OutputFolder);
							ProccessedFiles.add(jscgID);
							++count;
							System.out.println(count);
						 }

					 }
				}

				void Ffake( String PDBid, String DataPath, String OutputPath) throws IOException {
					
					 String[]callAndArgs= {
							 RunningPram.CfakeAnom,
							
							"-mtzin" ,DataPath+"/"+PDBid+".mtz",
							"-colin-fo","FP,SIGFP",
							"-mtzout",OutputPath+"/"+PDBid+".mtz",	
					
					 };
					

					 Process p = Runtime.getRuntime().exec(callAndArgs);
					 
					 BufferedReader stdInput = new BufferedReader(new 

			                 InputStreamReader(p.getInputStream()));



			            BufferedReader stdError = new BufferedReader(new 

			                 InputStreamReader(p.getErrorStream()));



			String st="";
			            while ((st = stdInput.readLine()) != null) {
			            	 System.out.println(st);
			            	
			            }

			            
			         
			    	
			    		
			            while ((st = stdError.readLine()) != null) {

			                System.out.println(st);

			            }
						 FileUtils.copyFile(new File(DataPath+"/"+PDBid+".pdb"),  new File(OutputPath+"/"+PDBid+".pdb"));
						 
						 if(new File(DataPath+"/"+PDBid+".seq").exists())
							 FileUtils.copyFile(new File(DataPath+"/"+PDBid+".seq"),  new File(OutputPath+"/"+PDBid+".seq"));
					          if(new File(DataPath+"/"+PDBid+".fasta").exists())
					        	  FileUtils.copyFile(new File(DataPath+"/"+PDBid+".fasta"),  new File(OutputPath+"/"+PDBid+".fasta"));
					           if(new File(DataPath+"/"+PDBid+".fa").exists())
					        	   FileUtils.copyFile(new File(DataPath+"/"+PDBid+".fa"),  new File(OutputPath+"/"+PDBid+".fa"));
						        
				}
			}
