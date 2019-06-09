package Comparison.Runner;
/**
*
* @author Emad Alharbi
* University of York
*/
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

import Comparison.Analyser.LatexTablesCreater;
import Comparison.Analyser.MultiThreadedAnalyser;
import Comparison.ToolsExecation.SingleThread.Arp;
import Comparison.ToolsExecation.SingleThread.Buccaneeri1;
import Comparison.ToolsExecation.SingleThread.Buccaneeri2;
import Comparison.ToolsExecation.SingleThread.CAD;
import Comparison.ToolsExecation.SingleThread.Crank;
import Comparison.ToolsExecation.SingleThread.MolProbity;
import Comparison.ToolsExecation.SingleThread.Phenix;
import Comparison.ToolsExecation.SingleThread.RunnerManager;
import Comparison.ToolsExecation.SingleThread.cfakeAnom;
import Comparison.ToolsExecation.SingleThread.chltofom;
import Comparison.Utilities.PhenixTempCleaner;
import NotToSync.Buccaneeri1Testing;
import NotUsed.ArpResultsAnalysis2;
import NotUsed.BuccaneerResultsAnalysis;
import NotUsed.PhenixResultsAnalysis2;
import NotUsed.RunCBuccaneer;
import ToolsExecution.Buccaneeri2Runner;
import ToolsExecution.CrankRunner;
import ToolsExecution.ArpRunner;
import ToolsExecution.RunArpTesting;
import ToolsExecution.RunBuccaneerMulti;
import ToolsExecution.CBuccaneerRunner;
import ToolsExecution.RunCBuccaneerTestingProupse;
import ToolsExecution.RunPhenix2;
import ToolsExecution.RunPhenixHLA;
import ToolsExecution.RunPhenixMultiThreads;
import ToolsExecution.PhenixRunner;
import ToolsExecution.ThreadsRunner;

public class RunComparison {

	/*
	 * Main class of the comparison script 
	 */
	
	public static void main(String[] args) throws IOException, InterruptedException, InstantiationException, IllegalAccessException {
		// TODO Auto-generated method stub
		if(System.getenv("PHENIX")==null) {
			System.out.println("PHENIX installation cannot be found! if you instaled PHENIX, run the setup up script first. ");
	
			System.exit(-1);
		}
		
		if(System.getenv("CCP4")==null){
			System.out.println("CCP4 installation cannot be found! if you instaled ccp4, run the setup up script first. (setup script canbe found in ccp4/setup-scripts)");
			System.exit(-1);
		}
		else{
			String CCP4Dir=System.getenv("CCP4");
			RunningPram.BuccaneerPipeLine=CCP4Dir+"/share/python/CCP4Dispatchers/buccaneer_pipeline.py";
			 //RunningPram.CphasesMatchScriptPath=System.getenv("CCP4")+"/share/python/CCP4Dispatchers/cphasematch.py";//CphasesMatch Script Path in CCP4 folder
			 RunningPram.CphasesMatchScriptPath=System.getenv("CCP4")+"/bin/cphasematch";//CphasesMatch Script Path in CCP4 folder

			 RunningPram.RefmacPath=System.getenv("CCP4")+"/bin/refmac5";
			
			 //System.out.println(" BuccaneerPipeLine "+RunningPram.BuccaneerPipeLine);
			 //System.out.println(" CphasesMatchScriptPath "+RunningPram.CphasesMatchScriptPath);
			// System.out.println(" RefmacPath "+RunningPram.RefmacPath);
			 if(args.length==0){
				
				 PrintInstructions();	
				System.exit(-1);
			}
			 Vector <String> Parm = new Vector<String>();
			 for (int i=0 ; i < args.length ; ++i){
				 
				 if(args[i].contains("=")){
					 Parm.addAll(Arrays.asList(args[i].split("=")));
					
				 }
				 
			 }
			 System.out.println("Parameters read correctly: ");
 for (int i=0 ; i < Parm.size()-1 ; i=i+2){
				 
				System.out.println("Parm "+Parm.get(i) + " value "+ Parm.get(i+1));
					 
					
				 
				 
			 }
			 
			 
			  if(args[0].equals("chltofom")){
					
				 
					String DataPath="";
				 Parm.addAll(Arrays.asList(args));
			
				if(checkArg(Parm,"data")==null){
					System.out.println("One or more of the required parameters is missing! ");
					System.out.println("The required parameters are : ");
					System.out.println("data= the path for data folder");
					System.exit(-1);
				}
				if(checkArg(Parm,"data")!=null){
					DataPath=checkArg(Parm,"data");
					
				 }
					new chltofom().RunChltofom(DataPath);
			 }
			 
			 
			 
			 
			 
			 else if(args[0].equals("CAD")){
					
				 
				 String DataPath="";
				 Parm.addAll(Arrays.asList(args));
			
				if(checkArg(Parm,"data")==null){
					
					System.out.println("One or more of the required parameters is missing! ");
					System.out.println("The required parameters are : ");
					System.out.println("data= the path for data folder");
					System.exit(-1);
					
				}
				if(checkArg(Parm,"data")!=null){
					DataPath=checkArg(Parm,"data");
					
				 }
					new CAD().RunCAD(DataPath);
			 }
 else if(args[0].equals("ReRun")){
					
				 
				 String DataPath="";
				 Parm.addAll(Arrays.asList(args));
			
				if(checkArg(Parm,"Path")==null){
					
					System.out.println("One or more of the required parameters is missing! ");
					System.out.println("The required parameters are : ");
					System.out.println("Path= the path for the folder that contains pipelines");
					System.exit(-1);
					
				}
				if(checkArg(Parm,"Path")!=null){
					DataPath=checkArg(Parm,"Path");
					
				 }
					new PipeliensRerun().ReRun(DataPath);
			 }
			 
                else if(args[0].equals("CfakeAnom")){
					
				 
				 String DataPath="";
				 Parm.addAll(Arrays.asList(args));
			
				if(checkArg(Parm,"data")==null){
					System.out.println("One or more of the required parameters is missing! ");
					System.out.println("The required parameters are : ");
					System.out.println("data= the path for data folder");
					System.out.println("Cfake= cfake anom for path ");
					System.exit(-1);
				}
				if(checkArg(Parm,"data")!=null){
					DataPath=checkArg(Parm,"data");
					
				 }
				if(checkArg(Parm,"Cfake")!=null){
					
					 RunningPram.CfakeAnom= checkArg(Parm,"Cfake");
				 }
					new cfakeAnom().Run(DataPath);
			 }
			  
			  
			  
			  
			  
			 else if(args[0].equals("PhenixTempCleaner")){
					
				 
					
				 Parm.addAll(Arrays.asList(args));
			
				if(checkArg(Parm,"LogsDirPhenix")==null || checkArg(Parm,"PhenixWorkingDirList")==null){
					System.out.println("One or more of the required parameters is missing! ");
					System.out.println("The required parameters are : ");
					System.out.println("data= the path for data folder");
					System.exit(-1);
				}
					 if(checkArg(Parm,"PhenixWorkingDirList")!=null){
						 RunningPram.PhenixWorkingDirList=checkArg(Parm,"PhenixWorkingDirList");
						
					 }
					 if(checkArg(Parm,"LogsDirPhenix")!=null){
						 RunningPram.LogsDirPhenix=checkArg(Parm,"LogsDirPhenix");
						
					 }
					new PhenixTempCleaner().CleanTemp(RunningPram.LogsDirPhenix,RunningPram.PhenixWorkingDirList);	
			 }
			 else if(args[0].equals("RunBuccaneer")){
				
				 
				
				 Parm.addAll(Arrays.asList(args));
			
				if(checkArg(Parm,"data")==null){
					System.out.println("One or more of the required parameters is missing! ");
					System.out.println("The required parameters are : ");
					System.out.println("data= the path for data folder");
					System.exit(-1);
				}
					 if(checkArg(Parm,"data")!=null){
						 RunningPram.DataPath=checkArg(Parm,"data");
						
					 }
					new RunBuccaneerMulti().RunBuccaneerTool();	
			 }
			 else if(args[0].equals("RunCrank")){
					
				 
					
				 Parm.addAll(Arrays.asList(args));
			
				if(checkArg(Parm,"data")==null){
					System.out.println("One or more of the required parameters is missing! ");
					System.out.println("The required parameters are : ");
					System.out.println("data= the path for data folder");
					System.out.println("CrankPipeLine= the path for data folder");
					System.exit(-1);
				}
					 if(checkArg(Parm,"data")!=null){
						 RunningPram.DataPath=checkArg(Parm,"data");
						
					 }
					 if(checkArg(Parm,"CrankPipeLine")!=null){
						 RunningPram.CrankPipeLine=checkArg(Parm,"CrankPipeLine");
						
					 }
					 
					 if(checkArg(Parm,"UsingRFree")!=null){
						 RunningPram.UsingRFree=checkArg(Parm,"UsingRFree");
						
					 }
				//	new CrankRunner().RunCrank(new CrankRunner().PickACase());	
					 //new ThreadsRunner().Threads(new CrankRunner());
					 new Crank().RunCrank();
			 }
			 else if(args[0].equals("RunBuccaneeri2")){
					
				 
					
				 Parm.addAll(Arrays.asList(args));
			
				if(checkArg(Parm,"data")==null){
					System.out.println("One or more of the required parameters is missing! ");
					System.out.println("The required parameters are : ");
					System.out.println("data= the path for data folder");
					System.out.println("Buccaneeri2= the path for data folder");
					System.exit(-1);
				}
					 if(checkArg(Parm,"data")!=null){
						 RunningPram.DataPath=checkArg(Parm,"data");
						
					 }
					 if(checkArg(Parm,"Buccaneeri2")!=null){
						 RunningPram.Buccaneeri2PipeLine=checkArg(Parm,"Buccaneeri2");
						
					 }
					 
					 if(checkArg(Parm,"UsingRFree")!=null){
						 RunningPram.UsingRFree=checkArg(Parm,"UsingRFree");
						
					 }
					 
					 if(checkArg(Parm,"Iterations")!=null){
						 RunningPram.BuccaneerIterations=checkArg(Parm,"Iterations");
						
					 }
					//new Buccaneeri2().RunBuccaneerTool(new Buccaneeri2().PickACase());	
					 //new ThreadsRunner().Threads(new Buccaneeri2Runner());
					 new Buccaneeri2().RunBuccaneerTool();
			 }
			 
			 else if(args[0].equals("Preparer")){
					
				 
					 
				 Parm.addAll(Arrays.asList(args));
			
				if(checkArg(Parm,"Pipelines")==null){
					System.out.println("One or more of the required parameters is missing! ");
					System.out.println("The required parameters are : ");
					System.out.println("Pipelines= the pipelines that you want to run separated by a comma (Ex Pipelines=Phenix,ArpWArp,Buccaneeri1,Buccaneeri2,Buccaneeri2W,Crank,ArpWArpAfterBuccaneeri1)");
					System.out.println("FOMDataPath= the data folder after converting to figure-of-merit");
					System.out.println("DataPath= the data folder with parrot phases ");
					System.out.println("ccp4i2Core= path for ccp4i2 core folder   ");
					System.out.println("DatafakeAnomalous= path Data fake Anomalous");
					System.out.println("cstat2= path for cstat2 core folder");
					System.out.println("UsingMolProbity= T or F");
					System.out.println("PhasesUsedCPhasesMatch= Default is (parrot.ABCD.A,parrot.ABCD.B,parrot.ABCD.C,parrot.ABCD.D)");
					System.out.println("UsingRfree= T or F");
					System.exit(-1);
				}
					 if(checkArg(Parm,"Pipelines")!=null){
						 RunningPram.Pipelines=checkArg(Parm,"Pipelines");
						
					 }
					 if(checkArg(Parm,"FOMDataPath")!=null){
						 RunningPram.ChltomDataPath=checkArg(Parm,"FOMDataPath");
						
					 }
					 if(checkArg(Parm,"DataPath")!=null){
						 RunningPram.DataPath=checkArg(Parm,"DataPath");
						
					 }
					 if(checkArg(Parm,"ccp4i2Core")!=null){
						 RunningPram.ccp4i2Core=checkArg(Parm,"ccp4i2Core");
						
					 }
					 if(checkArg(Parm,"DatafakeAnomalous")!=null){
						 RunningPram.DatafakeAnomalous=checkArg(Parm,"DatafakeAnomalous");
						
					 }
					 if(checkArg(Parm,"cstat2")!=null){
						 RunningPram.castat2Path=checkArg(Parm,"cstat2");
						
					 }
					 
					 if(checkArg(Parm,"UsingMolProbity")!=null){
						 RunningPram.UsingMolProbity=checkArg(Parm,"UsingMolProbity");
						
					 }
					 
					 if(checkArg(Parm,"PhasesUsedCPhasesMatch")!=null){
						 RunningPram.PhasesUsedCPhasesMatch=checkArg(Parm,"PhasesUsedCPhasesMatch");
						
					 }
					 if(checkArg(Parm,"UsingRfree")!=null){
						 RunningPram.UsingRFree=checkArg(Parm,"UsingRfree");
						
					 }
					 Vector <String>PipelinesNames=  new Vector<String>(Arrays.asList(RunningPram.Pipelines.split(",")));
					
					new Preparer().Prepare(PipelinesNames);	
			 }
			 
			 else if(args[0].equals("JobsCreater")){
					
				 
					
				 Parm.addAll(Arrays.asList(args));
			
				if(checkArg(Parm,"ToolName")==null){
					System.out.println("One or more of the required parameters is missing! ");
					System.out.println("The required parameters are : ");
					System.out.println("ToolName= the path for tool script");
					System.out.println("PDBDir= the path for tool PDBs. If not provided, will create scripts for the all files");
					System.exit(-1);
				}
				if(checkArg(Parm,"PDBDir")!=null)	 
					RunningPram.PDBsDir=checkArg(Parm,"PDBDir");
				if(checkArg(Parm,"PDBDir")==null)
					RunningPram.PDBsDir=null;
				
					new JobCreater().CreateJobs(checkArg(Parm,"ToolName"));
			 }
			  
			 else if(args[0].equals("Cleaner")){
					
				 
					
				 Parm.addAll(Arrays.asList(args));
			
				if(checkArg(Parm,"LogsDir")==null || checkArg(Parm,"PDBDir")==null || checkArg(Parm,"LogsDirInter")==null || checkArg(Parm,"PDBDirInter")==null){
					System.out.println("One or more of the required parameters is missing! ");
					System.out.println("The required parameters are : ");
					System.out.println("LogsDir= the path for the log files");
					System.out.println("PDBDir= the path for the PDB files");
					System.out.println("LogsDirInter= the path for the log files");
					System.out.println("PDBDirInter= the path for the PDB files");
					System.exit(-1);
				}
				
				if(checkArg(Parm,"PDBDir")!=null)	 
					RunningPram.PDBsDir=checkArg(Parm,"PDBDir");
				if(checkArg(Parm,"PDBDir")==null)
					RunningPram.PDBsDir=null;
				
				if(checkArg(Parm,"LogsDir")!=null)	 
					RunningPram.LogsDir=checkArg(Parm,"LogsDir");
				if(checkArg(Parm,"LogsDir")==null)
					RunningPram.LogsDir=null;
				
				if(checkArg(Parm,"LogsDirInter")!=null)	 
					RunningPram.IntermediateLogs=checkArg(Parm,"LogsDirInter");
				if(checkArg(Parm,"LogsDirInter")==null)
					RunningPram.IntermediateLogs=null;
				
				if(checkArg(Parm,"PDBDirInter")!=null)	 
					RunningPram.IntermediatePDBs=checkArg(Parm,"PDBDirInter");
				if(checkArg(Parm,"PDBDirInter")==null)
					RunningPram.IntermediatePDBs=null;
				
					new CleanerForRunner().Clean();
			 }
			 
			  
			  
			  
			  
			  else if(args[0].equals("LatexTablesCreater")){
					
					 
					
					 Parm.addAll(Arrays.asList(args));
				
					 if(checkArg(Parm,"ExcelDir")==null){
							System.out.println("One or more of the required parameters is missing! ");
							System.out.println("The required parameters are : ");
							System.out.println("ExcelDir= the path for root folder that contain three folders: hancs, mrncs and nonce. Each contains excel files belong to the same DM type. At less you need a one folder (such as noncs) in the root folder ");
							System.exit(-1);
						}
					 String [] p =  {checkArg(Parm,"ExcelDir")};
						new LatexTablesCreater().main( p);
				 }
				  
			 
			  
			  
			 else if(args[0].equals("ScriptManager")){
					
				 
					
				 Parm.addAll(Arrays.asList(args));
			
				if(checkArg(Parm,"shScriptPath")==null){
					System.out.println("One or more of the required parameters is missing! ");
					System.out.println("The required parameters are : ");
					System.out.println("shScriptPath= the path for data folder");
					System.out.println("NumberofTimes= the path for data folder");
					System.exit(-1);
				}
					 if(checkArg(Parm,"shScriptPath")!=null){
						 RunningPram.shScriptPath=checkArg(Parm,"shScriptPath");
						
					 }
					 if(checkArg(Parm,"NumberofTimes")!=null){
						 RunningPram.NoofTime=checkArg(Parm,"NumberofTimes");
						
					 }
					new RunnerManager().ScriptRunnerManager(RunningPram.shScriptPath, Integer.parseInt(RunningPram.NoofTime.trim()));
			 }
			  
			  
			  
			 else if(args[0].equals("RunCBuccaneer")){
					
				 
					
				 Parm.addAll(Arrays.asList(args));
			
				if(checkArg(Parm,"data")==null || checkArg(Parm,"Iterations")==null){
					System.out.println("One or more of the required parameters is missing! ");
					System.out.println("The required parameters are : ");
					System.out.println("data= the path for data folder");
					System.out.println("Iterations= number of iterations");
					System.exit(-1);
				}
					 if(checkArg(Parm,"data")!=null){
						 RunningPram.DataPath=checkArg(Parm,"data");
						
					 }
					 if(checkArg(Parm,"Iterations")!=null){
						 RunningPram.BuccaneerIterations=checkArg(Parm,"Iterations");
						
					 }
					 
					 if(checkArg(Parm,"UsingRFree")!=null){
						 RunningPram.UsingRFree=checkArg(Parm,"UsingRFree");
						
					 }
					 new Buccaneeri1().RunBuccaneerTool();
			 }
			 else if(args[0].equals("Buccaneeri1TestMode")){
					
				 
					
				 Parm.addAll(Arrays.asList(args));
			
				if(checkArg(Parm,"data")==null || checkArg(Parm,"Iterations")==null){
					System.out.println("One or more of the required parameters is missing! ");
					System.out.println("The required parameters are : ");
					System.out.println("data= the path for data folder");
					System.out.println("Iterations= number of iterations");
					System.exit(-1);
				}
					 if(checkArg(Parm,"data")!=null){
						 RunningPram.DataPath=checkArg(Parm,"data");
						
					 }
					 if(checkArg(Parm,"Iterations")!=null){
						 RunningPram.BuccaneerIterations=checkArg(Parm,"Iterations");
						
					 }
					 
					 if(checkArg(Parm,"UsingRFree")!=null){
						 RunningPram.UsingRFree=checkArg(Parm,"UsingRFree");
						
					 }
					//new RunCBuccaneer().RunBuccaneerTool(	new RunCBuccaneer().PickACase());
					// new ThreadsRunner().Threads(new RunCBuccaneer());
					 
					// new RunCBuccaneer().RunBuccaneerTool();
					 new Buccaneeri1Testing().RunBuccaneerTool();
			 }
			 else if(args[0].equals("RunCBuccaneerTestingProupse")){
					
				 
					
				 Parm.addAll(Arrays.asList(args));
			
				if(checkArg(Parm,"data")==null){
					System.out.println("One or more of the required parameters is missing! ");
					System.out.println("The required parameters are : ");
					System.out.println("data= the path for data folder");
					System.exit(-1);
				}
					 if(checkArg(Parm,"data")!=null){
						 RunningPram.DataPath=checkArg(Parm,"data");
						
					 }
					new RunCBuccaneerTestingProupse().RunBuccaneerTool();
			 }
			 else if(args[0].equals("RunwArp")){
				
				
				
				
				 Parm.addAll(Arrays.asList(args));
			
				if(checkArg(Parm,"data") ==null ||checkArg(Parm,"wArpAutotracing")==null){
					System.out.println("One or more of the required parameters is missing! ");
					System.out.println("The required parameters are : ");
					System.out.println("data= the path for data folder");
					System.out.println("wArpAutotracing= the path for autotracing.sh");
					
					System.exit(-1);
				}
					 if(checkArg(Parm,"data")!=null){
						 RunningPram.DataPath=checkArg(Parm,"data");
						
					 }
					 if(checkArg(Parm,"wArpAutotracing")!=null){
						 RunningPram.wArpAutotracing=checkArg(Parm,"wArpAutotracing");
						
					 }
					 if(checkArg(Parm,"UseBuccModels")!=null){
						 RunningPram.UseBuccModels=checkArg(Parm,"UseBuccModels");
						
					 }
					 if(checkArg(Parm,"BuccModels")!=null){
						 RunningPram.BuccModels=checkArg(Parm,"BuccModels");
						
					 }
					 
					 if(checkArg(Parm,"UsingRFree")!=null){
						 RunningPram.UsingRFree=checkArg(Parm,"UsingRFree");
						
					 }

					
					 new Arp().RunwArpTool();
			 }
			 else if(args[0].equals("RunwArpTesting")){
					
					
					
					
				 Parm.addAll(Arrays.asList(args));
			
				if(checkArg(Parm,"data") ==null ||checkArg(Parm,"wArpAutotracing")==null){
					System.out.println("One or more of the required parameters is missing! ");
					System.out.println("The required parameters are : ");
					System.out.println("data= the path for data folder");
					System.out.println("wArpAutotracing= the path for autotracing.sh");
					
					System.exit(-1);
				}
					 if(checkArg(Parm,"data")!=null){
						 RunningPram.DataPath=checkArg(Parm,"data");
						
					 }
					 if(checkArg(Parm,"wArpAutotracing")!=null){
						 RunningPram.wArpAutotracing=checkArg(Parm,"wArpAutotracing");
						
					 }
					 if(checkArg(Parm,"UseBuccModels")!=null){
						 RunningPram.UseBuccModels=checkArg(Parm,"UseBuccModels");
						
					 }
					 if(checkArg(Parm,"BuccModels")!=null){
						 RunningPram.BuccModels=checkArg(Parm,"BuccModels");
						
					 }

					 new RunArpTesting().RunwArpTool();
 
			 }
			 else if (args[0].equals("RunPhenix") ){
				
				
				
				 
				 Parm.addAll(Arrays.asList(args));
			
				if(checkArg(Parm,"data")==null || checkArg(Parm,"PhenixAutobuild")==null){
					System.out.println("One or more of the required parameters is missing! ");
					System.out.println("The required parameters are : ");
					System.out.println("data= the path for data folder");
	
					System.out.println("PhenixAutobuild= the path for Phenix.autobuild");
				
					System.exit(-1);
				}
					if(checkArg(Parm,"data")!= null){
						 RunningPram.DataPath=checkArg(Parm,"data");
						 
					 }
				
					 if(checkArg(Parm,"PhenixAutobuild")!= null){
						 RunningPram.PhenixAutobuild=checkArg(Parm,"PhenixAutobuild");
						
					 }
					 if(checkArg(Parm,"UsingRFree")!= null){
						 RunningPram.UsingRFree=checkArg(Parm,"UsingRFree");
						
					 }
					 if(checkArg(Parm,"Phases")!= null){
						 RunningPram.PhenixPhases=checkArg(Parm,"Phases");
						 
					 }
					 
					 
					 if(checkArg(Parm,"DensityModified")!= null){
						 RunningPram.DensityModifiedPhenix=checkArg(Parm,"DensityModified");
						 
					 }
					 
					 RunningPram.PhenixPhases= RunningPram.PhenixPhases.replaceAll(",", " ");
					 System.out.println("data = "+RunningPram.DataPath);
					 System.out.println("PhenixAutobuild = "+RunningPram.PhenixAutobuild);
					
					new Phenix().RunPhenixTool();
			 }
			 
 else if (args[0].equals("RunPhenixMultiThreads") ){
				
				
				
				 
				 Parm.addAll(Arrays.asList(args));
			
				if(checkArg(Parm,"data")==null || checkArg(Parm,"PhenixAutobuild")==null){
					System.out.println("One or more of the required parameters is missing! ");
					System.out.println("The required parameters are : ");
					System.out.println("data= the path for data folder");
					System.out.println("PhenixAutobuild= the path for Phenix.autobuild");
					System.out.println("Threads= Number of Threads");
					System.exit(-1);
				}
					if(checkArg(Parm,"data")!= null){
						 RunningPram.DataPath=checkArg(Parm,"data");
						 
					 }
				
					 if(checkArg(Parm,"PhenixAutobuild")!= null){
						 RunningPram.PhenixAutobuild=checkArg(Parm,"PhenixAutobuild");
						
					 }
					 if(checkArg(Parm,"Threads")!=null){
						 RunningPram.NumberofThreads=checkArg(Parm,"Threads");
						
					 }
					 System.out.println("data = "+RunningPram.DataPath);
					 System.out.println("PhenixAutobuild = "+RunningPram.PhenixAutobuild);
					 new RunPhenixMultiThreads().RunPhenixTool(); 
			 }
			 
 
			 
			 
			 else if(args[0].equals("RunBuccaneerAnalyser")){
				
				 
				
				 
				 Parm.addAll(Arrays.asList(args));
			
				if(checkArg(Parm,"data")==null || checkArg(Parm,"castat2Path")==null || checkArg(Parm,"LogsDir")==null ||checkArg(Parm,"PDBsDir")==null ){
					System.out.println("One or more of the required parameters is missing! ");
					System.out.println("The required parameters are : ");
					System.out.println("data= the path for data folder");
					System.out.println("castat2Path= the path for castat2");
					System.out.println("LogsDir= the path for buccaneer logs ");
					System.out.println("PDBsDir= the path for buccaneer PDBs");
					System.exit(-1);
				}
					 if(checkArg(Parm,"data")!=null){
						 RunningPram.DataPath=checkArg(Parm,"data");
						
					 }
					
					 if(checkArg(Parm,"castat2Path")!=null){
						 RunningPram.castat2Path=checkArg(Parm,"castat2Path");
						
					 }
					 if(checkArg(Parm,"LogsDir")!=null){
						 RunningPram.LogsDirBuccaneer=checkArg(Parm,"LogsDir");
						
					 }
					 if(checkArg(Parm,"PDBsDir")!=null){
						 RunningPram.PDBsDirBuccaneer=checkArg(Parm,"PDBsDir");
						
					 }
					
						new BuccaneerResultsAnalysis().AnalysingBuccaneerResults();

			 }
			 
 else if(args[0].equals("RunAnalyser")){
				
				 
				
				 
				 Parm.addAll(Arrays.asList(args));
			
				if(checkArg(Parm,"data")==null || checkArg(Parm,"castat2Path")==null || checkArg(Parm,"LogsDir")==null ||checkArg(Parm,"PDBsDir")==null ||  checkArg(Parm,"Threads")==null){
					System.out.println("One or more of the required parameters is missing! ");
					System.out.println("The required parameters are : ");
					System.out.println("data= the path for data folder");
					System.out.println("castat2Path= the path for castat2");
					System.out.println("LogsDir= the path for the tool logs ");
					System.out.println("PDBsDir= the path for the tool PDBs");
					System.out.println("Threads= Number of threads");
					System.out.println("ToolName= Buccaneer,  ARP/wARP or Phenix");
					System.out.println("ILogsDir= Intermediate Logs folder ");
					System.out.println("IPDBsDir= Intermediate PDBs folder");
					System.out.println("UsingMolProbity= T or F");
					System.out.println("PhasesUsedCPhasesMatch= Default is (parrot.ABCD.A,parrot.ABCD.B,parrot.ABCD.C,parrot.ABCD.D)");
					
					System.exit(-1);
				}
					 if(checkArg(Parm,"data")!=null){
						 RunningPram.DataPath=checkArg(Parm,"data");
						
					 }
					
					 if(checkArg(Parm,"castat2Path")!=null){
						 RunningPram.castat2Path=checkArg(Parm,"castat2Path");
						
					 }
					 if(checkArg(Parm,"LogsDir")!=null){
						 RunningPram.LogsDir=checkArg(Parm,"LogsDir");
						
					 }
					 if(checkArg(Parm,"PDBsDir")!=null){
						 RunningPram.PDBsDir=checkArg(Parm,"PDBsDir");
						
					 }
					 if(checkArg(Parm,"Threads")!=null){
						 RunningPram.NumberofThreads=checkArg(Parm,"Threads");
						
					 }
					 if(checkArg(Parm,"ToolName")!=null){
						 RunningPram.ToolName=checkArg(Parm,"ToolName");
						
					 }
					 if(checkArg(Parm,"IPDBsDir")!=null){
						 RunningPram.IntermediatePDBs=checkArg(Parm,"IPDBsDir");
						
					 }
					 
					 if(checkArg(Parm,"ILogsDir")!=null){
						 RunningPram.IntermediateLogs=checkArg(Parm,"ILogsDir");
						
					 }
					 if(checkArg(Parm,"MolProbity")!=null){
						 RunningPram.PhenixMolProbity=checkArg(Parm,"MolProbity");
						
					 }
					 if(checkArg(Parm,"UsingMolProbity")!=null){
						 RunningPram.UsingMolProbity=checkArg(Parm,"UsingMolProbity");
						
					 }
					 
					 if(checkArg(Parm,"PhasesUsedCPhasesMatch")!=null){
						 RunningPram.PhasesUsedCPhasesMatch=checkArg(Parm,"PhasesUsedCPhasesMatch");
						
					 }
						new MultiThreadedAnalyser().Analyses();

			 }
			 
 else if(args[0].equals("MolProbity")){
		
	 
		
	 System.out.println("MolProbity is choosen ");
	 Parm.addAll(Arrays.asList(args));

	if(checkArg(Parm,"data")==null   ||checkArg(Parm,"PDBs")==null || checkArg(Parm,"Excel")==null ||checkArg(Parm,"Threads")==null || checkArg(Parm,"MolProbity")==null){
		System.out.println("One or more of the required parameters is missing! ");
		System.out.println("The required parameters are : ");
		System.out.println("data= the path for data folder");
		System.out.println("Excel= the path for Excell");
		System.out.println("MolProbity= the path for  MolProbity ");
		System.out.println("PDBs= the path for  PDBs folder ");
		System.out.println("Threads= Number of threads");
		System.out.println("ToolName= Buccaneer,  ARP/wARP or Phenix ... etc");
		System.exit(-1);
	}
		 if(checkArg(Parm,"data")!=null){
			 RunningPram.DataPath=checkArg(Parm,"data");
			
		 }
		
		 if(checkArg(Parm,"MolProbity")!=null){
			 RunningPram.PhenixMolProbity=checkArg(Parm,"MolProbity");
			
		 }
		 if(checkArg(Parm,"PDBs")!=null){
			 RunningPram.PDBs=checkArg(Parm,"PDBs");
			
		 }
		 if(checkArg(Parm,"Excel")!=null){
			 RunningPram.ExcellPath=checkArg(Parm,"Excel");
			
		 }
		 if(checkArg(Parm,"Threads")!=null){
			 RunningPram.NumberofThreads=checkArg(Parm,"Threads");
			
		 }
		 if(checkArg(Parm,"ToolName")!=null){
			 RunningPram.ToolName=checkArg(Parm,"ToolName");
			
		 }
		
			new MolProbity().RunMol();

 }
			 else if(args[0].equals("RunwArpAnalyser")){
				 
				 
				
				
				 Parm.addAll(Arrays.asList(args));
			
				if(checkArg(Parm,"data")==null || checkArg(Parm,"castat2Path")==null || checkArg(Parm,"LogsDir")==null ||checkArg(Parm,"PDBsDir")==null ){
					System.out.println("One or more of the required parameters is missing! ");
					System.out.println("The required parameters are : ");
					System.out.println("data= the path for data folder");
					System.out.println("castat2Path= the path for castat2");
					System.out.println("LogsDir= the path for Arp/wArp logs ");
					System.out.println("PDBsDir= the path for Arp/wArp PDBs");
					System.exit(-1);
				}
					 if(checkArg(Parm,"data")!=null){
						 RunningPram.DataPath=checkArg(Parm,"data");
						
					 }
					
					 if(checkArg(Parm,"castat2Path")!=null){
						 RunningPram.castat2Path=checkArg(Parm,"castat2Path");
						
					 }
					 if(checkArg(Parm,"LogsDir")!=null){
						 RunningPram.LogsDirwArp=checkArg(Parm,"LogsDir");
						
					 }
					 if(checkArg(Parm,"PDBsDir")!=null){
						 RunningPram.PDBsDirwArp=checkArg(Parm,"PDBsDir");
						
					 }
					
					 new ArpResultsAnalysis2().AnalysingwArpResults();

			 }
			 
			 else if(args[0].equals("RunPhenixAnalyser")){
				
				 
				
				 
				 Parm.addAll(Arrays.asList(args));
			
				if(checkArg(Parm,"data")==null || checkArg(Parm,"castat2Path")==null || checkArg(Parm,"LogsDir")==null ||checkArg(Parm,"PDBsDir")==null ){
					System.out.println("One or more of the required parameters is missing! ");
					System.out.println("The required parameters are : ");
					System.out.println("data= the path for data folder");
					System.out.println("castat2Path= the path for castat2");
					System.out.println("LogsDir= the path for Phenix logs ");
					System.out.println("PDBsDir= the path for Phenix PDBs");
					System.exit(-1);
				}
					 if(checkArg(Parm,"data")!=null){
						 RunningPram.DataPath=checkArg(Parm,"data");
						
					 }
					
					 if(checkArg(Parm,"castat2Path")!=null){
						 RunningPram.castat2Path=checkArg(Parm,"castat2Path");
						
					 }
					 if(checkArg(Parm,"LogsDir")!=null){
						 RunningPram.LogsDirPhenix=checkArg(Parm,"LogsDir");
						
					 }
					 if(checkArg(Parm,"PDBsDir")!=null){
						 RunningPram.PDBsDirPhenix=checkArg(Parm,"PDBsDir");
						
					 }
					
					 new PhenixResultsAnalysis2().AnalysingPhenixResults();

			 }
			 else{
				 System.out.println("Unknown keyword");
				 PrintInstructions();			
				 
			 }
		}
	}
	static void PrintInstructions(){
		
		System.out.println("1- All to run the whole comparsion");
		System.out.println("The required parameters are : ");
		System.out.println("\t data= the path for data folder");
		System.out.println("\t wArpAutotracing= the path for autotracing.sh");
		System.out.println("\t PhenixAutobuild= the path for Phenix.autobuild");
		System.out.println("\t castat2Path= the path for castat2");
		
		 System.out.println("2- RunBuccaneer to run Buccaneer");
		 System.out.println("The required parameters are : ");
		 System.out.println("\t data= the path for data folder");
		
		 
		 System.out.println("3- RunwArp to run Arp/wArp");
		 
				System.out.println("The required parameters are : ");
				System.out.println("\t data= the path for data folder");
				System.out.println("\t wArpAutotracing= the path for autotracing.sh");
				
	
				 System.out.println("4- RunPhenix to run Phenix");
				
				System.out.println("The required parameters are : ");
				System.out.println("\t data= the path for data folder");
				System.out.println("\t PhenixAutobuild= the path for Phenix.autobuild");
			
				 System.out.println("5- RunBuccaneerAnalyser to run buccaneer results analyser");
		
						
						System.out.println("The required parameters are : ");
						System.out.println("\t data= the path for data folder");
						System.out.println("\t castat2Path= the path for castat2");
						System.out.println("\t LogsDir= the path for buccaneer logs ");
						System.out.println("\t PDBsDir= the path for buccaneer PDBs");
						
					
			
		 
		 System.out.println("6- RunwArpAnalyser to run Arp/wArp results analyser");

		 System.out.println("The required parameters are : ");
		 System.out.println("\t data= the path for data folder");
		 System.out.println("\t castat2Path= the path for castat2");
		 System.out.println("\t LogsDir= the path for Arp/wArp logs ");
		 System.out.println("\t PDBsDir= the path for Arp/wArp PDBs");
				
				System.out.println("7- RunPhenixAnalyser to run Phenix results analyser");
				System.out.println("The required parameters are : ");
				System.out.println("\t data= the path for data folder");
				System.out.println("\t castat2Path= the path for castat2");
				System.out.println("\t LogsDir= the path for Phenix logs ");
				System.out.println("\t PDBsDir= the path for Phenix PDBs");
						
				System.out.println("8- chltofom to convert to/from Hendrickson-Lattman coefficients for ARP/wARP. The mtz should have phases from parrot");
				System.out.println("\t data= the path for data folder");
				
				System.out.println("9- CAD to add fakewave in mtz");
				System.out.println("\t data= the path for data folder");
				
				System.out.println("10- CfakeAnom ");
				System.out.println("\t data= the path for data folder");
				System.out.println("\t Cfake= path to c++ code");
				
				
				System.out.println("11- ReRun to re-run the pipeliens. Only intermediate and failed cases will re-run  ");
				System.out.println("Path= the path for the folder that contains pipelines");
				
				System.out.println("12- RunBuccaneeri2 to run Run Buccaneeri2  ");
				System.out.println("Path= the path for the folder that contains pipelines");
				System.out.println("data= the path for data folder");
				System.out.println("Buccaneeri2= the path for data folder");
	
				
				System.out.println("13- Preparer command is very useful to preparer all the necessary scripts. Should be used before the first run.");
				System.out.println("Pipelines= the pipelines that you want to run separated by a comma (Ex Pipelines=Phenix,ArpWArp,Buccaneeri1,Buccaneeri2,Buccaneeri2W,Crank,ArpWArpAfterBuccaneeri1)");
				System.out.println("FOMDataPath= the data folder after converting to figure-of-merit");
				System.out.println("DataPath= the data folder with parrot phases ");
				System.out.println("ccp4i2Core= path for ccp4i2 core folder   ");
				System.out.println("DatafakeAnomalous= path Data fake Anomalous");
				System.out.println("cstat2= path for cstat2 core folder");
				System.out.println("UsingMolProbity= T or F");
				System.out.println("PhasesUsedCPhasesMatch= Default is (parrot.ABCD.A,parrot.ABCD.B,parrot.ABCD.C,parrot.ABCD.D)");
				System.out.println("UsingRfree= T or F");

				
				System.out.println("14- JobsCreater command  creates single job for each case. ");
				System.out.println("ToolName= the path for tool script");
				System.out.println("PDBDir= the path for tool PDBs. If not provided, will create job scripts for the all files");
	
				System.out.println("15- Cleaner command to clean intermediate and unnecessary scripts. Recommended before a re-run.");
				System.out.println("LogsDir= the path for the log files");
				System.out.println("PDBDir= the path for the PDB files");
				System.out.println("LogsDirInter= the path for the log files");
				System.out.println("PDBDirInter= the path for the PDB files");
	
				System.out.println("16- MolProbity command.");
				System.out.println("The required parameters are : ");
				System.out.println("data= the path for data folder");
				System.out.println("Excel= the path for Excell");
				System.out.println("MolProbity= the path for  MolProbity ");
				System.out.println("PDBs= the path for  PDBs folder ");
				System.out.println("Threads= Number of threads");
				System.out.println("ToolName= Buccaneer,  ARP/wARP or Phenix ... etc");
	
				System.out.println("Some useful keywords: ");
				System.out.println("Threads= number of threads to use in the analyser ");
				System.out.println("Iterations= number of iterations  for  Buccaneer");
				System.out.println("UsingRFree= T or F. Default is T");
	}
	static public boolean CheckDirAndFile(String Path){
		
		   try {
			File directory = new File(Path);
			    if (! directory.exists()){
			        directory.mkdir();
			    }
			    return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("Error: Unable to create "+Path);
			System.exit(-1);
			return false;
			
		}
		   
	}
	static String checkArg(Vector<String> Args, String Keyword ){
		for (int i=0 ; i< Args.size() ; ++i){
			if(Args.get(i).equals(Keyword))
				return Args.get(i+1);
		}
		return null;
		
	}

}
