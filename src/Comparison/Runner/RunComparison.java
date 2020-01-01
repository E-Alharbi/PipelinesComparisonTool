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

import org.apache.commons.io.FileUtils;
import org.json.simple.parser.ParseException;

import Comparison.Analyser.LatexTablesCreater;
import Comparison.Analyser.MatchChainsToRef;
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
import Comparison.ToolsExecation.SingleThread.shelxe;
import Comparison.Utilities.PhenixTempCleaner;
import Comparison.Utilities.RemovingDumAtoms;


public class RunComparison {

	/*
	 * Main class of the comparison tool 
	 */
	
	public static void main(String[] args) throws IOException, InterruptedException, InstantiationException, IllegalAccessException, ParseException {
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
			RunningParameter.BuccaneerPipeLine=CCP4Dir+"/share/python/CCP4Dispatchers/buccaneer_pipeline.py";
			 //RunningPram.CphasesMatchScriptPath=System.getenv("CCP4")+"/share/python/CCP4Dispatchers/cphasematch.py";//CphasesMatch Script Path in CCP4 folder
			 RunningParameter.CphasesMatchScriptPath=System.getenv("CCP4")+"/bin/cphasematch";//CphasesMatch Script Path in CCP4 folder

			 RunningParameter.RefmacPath=System.getenv("CCP4")+"/bin/refmac5";
			
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
					
					 RunningParameter.CfakeAnom= checkArg(Parm,"Cfake");
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
						 RunningParameter.PhenixWorkingDirList=checkArg(Parm,"PhenixWorkingDirList");
						
					 }
					 if(checkArg(Parm,"LogsDirPhenix")!=null){
						 RunningParameter.LogsDirPhenix=checkArg(Parm,"LogsDirPhenix");
						
					 }
					new PhenixTempCleaner().CleanTemp(RunningParameter.LogsDirPhenix,RunningParameter.PhenixWorkingDirList);	
			 }
			  /*
			 else if(args[0].equals("RunBuccaneer")){
				
				 
				
				 Parm.addAll(Arrays.asList(args));
			
				if(checkArg(Parm,"data")==null){
					System.out.println("One or more of the required parameters is missing! ");
					System.out.println("The required parameters are : ");
					System.out.println("data= the path for data folder");
					System.exit(-1);
				}
					 if(checkArg(Parm,"data")!=null){
						 RunningParameter.DataPath=checkArg(Parm,"data");
						
					 }
					new RunBuccaneerMulti().RunBuccaneerTool();	
			 }
			  */
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
						 RunningParameter.DataPath=checkArg(Parm,"data");
						
					 }
					 if(checkArg(Parm,"CrankPipeLine")!=null){
						 RunningParameter.CrankPipeLine=checkArg(Parm,"CrankPipeLine");
						
					 }
					 
					 if(checkArg(Parm,"UsingRFree")!=null){
						 RunningParameter.UsingRFree=checkArg(Parm,"UsingRFree");
						
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
						 RunningParameter.DataPath=checkArg(Parm,"data");
						
					 }
					 if(checkArg(Parm,"Buccaneeri2")!=null){
						 RunningParameter.Buccaneeri2PipeLine=checkArg(Parm,"Buccaneeri2");
						
					 }
					 
					 if(checkArg(Parm,"UsingRFree")!=null){
						 RunningParameter.UsingRFree=checkArg(Parm,"UsingRFree");
						
					 }
					 
					 if(checkArg(Parm,"Iterations")!=null){
						 RunningParameter.BuccaneerIterations=checkArg(Parm,"Iterations");
						
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
					System.out.println("PhasesUsedCPhasesMatch= Default is (HLA,HLB,HLC,HLD)");
					System.out.println("FinalPhasesCPhasesMatch= Default is (sfcalc.ABCD.A,sfcalc.ABCD.B,sfcalc.ABCD.C,sfcalc.ABCD.D)");

					System.out.println("SlurmAccount= you project account in slurm [optional] ");
					System.out.println("UsingRfree= T or F");
					System.out.println("SlurmEmail= you email to receive notifications about jobs status [optional]   ");
					System.out.println("CCP4ModuleLoadingCommand= for example module load chem/ccp4/7.0.066   ");
					System.out.println("PhenixLoadingCommand= for example module load chem/phenix/1.14-3260   ");
					System.exit(-1);
				}
					 if(checkArg(Parm,"Pipelines")!=null){
						 RunningParameter.Pipelines=checkArg(Parm,"Pipelines");
						
					 }
					 if(checkArg(Parm,"FOMDataPath")!=null){
						 RunningParameter.ChltomDataPath=checkArg(Parm,"FOMDataPath");
						
					 }
					 if(checkArg(Parm,"DataPath")!=null){
						 RunningParameter.DataPath=checkArg(Parm,"DataPath");
						
					 }
					 if(checkArg(Parm,"ccp4i2Core")!=null){
						 RunningParameter.ccp4i2Core=checkArg(Parm,"ccp4i2Core");
						
					 }
					 if(checkArg(Parm,"DatafakeAnomalous")!=null){
						 RunningParameter.DatafakeAnomalous=checkArg(Parm,"DatafakeAnomalous");
						
					 }
					 if(checkArg(Parm,"cstat2")!=null){
						 RunningParameter.castat2Path=checkArg(Parm,"cstat2");
						
					 }
					 
					 if(checkArg(Parm,"UsingMolProbity")!=null){
						 RunningParameter.UsingMolProbity=checkArg(Parm,"UsingMolProbity");
						
					 }
					 
					 if(checkArg(Parm,"PhasesUsedCPhasesMatch")!=null){
						 RunningParameter.PhasesUsedCPhasesMatch=checkArg(Parm,"PhasesUsedCPhasesMatch");
						
					 }
					 if(checkArg(Parm,"FinalPhasesCPhasesMatch")!=null){
						 RunningParameter.FinalPhasesCPhasesMatch=checkArg(Parm,"FinalPhasesCPhasesMatch");
						
					 }
					 if(checkArg(Parm,"UsingRfree")!=null){
						 RunningParameter.UsingRFree=checkArg(Parm,"UsingRfree");
						
					 }
					 
					 if(checkArg(Parm,"SlurmAccount")!=null){
						 RunningParameter.SlurmAccount=checkArg(Parm,"SlurmAccount");
						
					 }
					 
					 if(checkArg(Parm,"SlurmEmail")!=null){
						 RunningParameter.SlurmEmail=checkArg(Parm,"SlurmEmail");
						
					 }
					 
					 if(checkArg(Parm,"CCP4ModuleLoadingCommand")!=null){
						 RunningParameter.CCP4ModuleLoadingCommand=checkArg(Parm,"CCP4ModuleLoadingCommand");
						
					 }
					 
					 if(checkArg(Parm,"PhenixLoadingCommand")!=null){
						 RunningParameter.PhenixLoadingCommand=checkArg(Parm,"PhenixLoadingCommand");
						
					 }
					 if(checkArg(Parm,"ShelxeData")!=null){
						 RunningParameter.ShelxeData=checkArg(Parm,"ShelxeData");
						
					 }
					 
					 
					 if(checkArg(Parm,"SlurmAccount")==null) {
						 System.out.println("Warning: you do not set SlurmAccount");
					 }
					 if(checkArg(Parm,"SlurmEmail")==null) {
						 System.out.println("Warning: you do not set SlurmEmail");
					 }
					 if(checkArg(Parm,"CCP4ModuleLoadingCommand")==null) {
						 System.out.println("CCP4ModuleLoadingCommand="+RunningParameter.CCP4ModuleLoadingCommand+" default is used");
					 }
					 if(checkArg(Parm,"PhenixLoadingCommand")==null) {
						 System.out.println("PhenixLoadingCommand="+RunningParameter.PhenixLoadingCommand+" default is used");
					 }
					 Vector <String>PipelinesNames=  new Vector<String>(Arrays.asList(RunningParameter.Pipelines.split(",")));
					
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
					RunningParameter.PDBsDir=checkArg(Parm,"PDBDir");
				if(checkArg(Parm,"PDBDir")==null)
					RunningParameter.PDBsDir=null;
				
				
				
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
					RunningParameter.PDBsDir=checkArg(Parm,"PDBDir");
				if(checkArg(Parm,"PDBDir")==null)
					RunningParameter.PDBsDir=null;
				
				if(checkArg(Parm,"LogsDir")!=null)	 
					RunningParameter.LogsDir=checkArg(Parm,"LogsDir");
				if(checkArg(Parm,"LogsDir")==null)
					RunningParameter.LogsDir=null;
				
				if(checkArg(Parm,"LogsDirInter")!=null)	 
					RunningParameter.IntermediateLogs=checkArg(Parm,"LogsDirInter");
				if(checkArg(Parm,"LogsDirInter")==null)
					RunningParameter.IntermediateLogs=null;
				
				if(checkArg(Parm,"PDBDirInter")!=null)	 
					RunningParameter.IntermediatePDBs=checkArg(Parm,"PDBDirInter");
				if(checkArg(Parm,"PDBDirInter")==null)
					RunningParameter.IntermediatePDBs=null;
				
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
					 if(checkArg(Parm,"MR")!=null){
						 RunningParameter.MR=checkArg(Parm,"MR");
						
					 }
					 String [] p =  {checkArg(Parm,"ExcelDir"),checkArg(Parm,"UpdateCom"),checkArg(Parm,"FillInMissingData"),checkArg(Parm,"ExcludeFromOrginal"),checkArg(Parm,"ExcludeFromSynthetic"),checkArg(Parm,"SetRfreeToZero")};
					 
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
						 RunningParameter.shScriptPath=checkArg(Parm,"shScriptPath");
						
					 }
					 if(checkArg(Parm,"NumberofTimes")!=null){
						 RunningParameter.NoofTime=checkArg(Parm,"NumberofTimes");
						
					 }
					new RunnerManager().ScriptRunnerManager(RunningParameter.shScriptPath, Integer.parseInt(RunningParameter.NoofTime.trim()));
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
						 RunningParameter.DataPath=checkArg(Parm,"data");
						
					 }
					 if(checkArg(Parm,"Iterations")!=null){
						 RunningParameter.BuccaneerIterations=checkArg(Parm,"Iterations");
						
					 }
					 
					 if(checkArg(Parm,"UsingRFree")!=null){
						 RunningParameter.UsingRFree=checkArg(Parm,"UsingRFree");
						
					 }
					 if(checkArg(Parm,"UseInitialModels")!=null) {
						 RunningParameter.UseInitialModels=checkArg(Parm,"UseInitialModels"); 
					 }
					 if(checkArg(Parm,"InitialModels")!=null){
						 RunningParameter.InitialModels=checkArg(Parm,"InitialModels");
						
					 }
					 
					 if(checkArg(Parm,"MR")!=null){
						 RunningParameter.MR=checkArg(Parm,"MR");
						
					 }
					 new Buccaneeri1().RunBuccaneerTool();
			 }
			  
			  
			 else if(args[0].equals("RunShelxe")){
					
				 
					
				 Parm.addAll(Arrays.asList(args));
			
				if(checkArg(Parm,"data")==null || checkArg(Parm,"Shelxe")==null){
					System.out.println("One or more of the required parameters is missing! ");
					System.out.println("The required parameters are : ");
					System.out.println("data= the path for data folder");
					System.out.println("Shelxe= path to shelxe file");
					System.exit(-1);
				}
					 if(checkArg(Parm,"data")!=null){
						 RunningParameter.DataPath=checkArg(Parm,"data");
						
					 }
					 if(checkArg(Parm,"Shelxe")!=null){
						 RunningParameter.Shelxe=checkArg(Parm,"Shelxe");
						
					 }
					 if(checkArg(Parm,"MR")!=null){
						 RunningParameter.MR=checkArg(Parm,"MR");
						
					 }
					 
					 new shelxe().RunshelxeTool();
			 }
			  /*
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
						 RunningParameter.DataPath=checkArg(Parm,"data");
						
					 }
					 if(checkArg(Parm,"Iterations")!=null){
						 RunningParameter.BuccaneerIterations=checkArg(Parm,"Iterations");
						
					 }
					 
					 if(checkArg(Parm,"UsingRFree")!=null){
						 RunningParameter.UsingRFree=checkArg(Parm,"UsingRFree");
						
					 }
					//new RunCBuccaneer().RunBuccaneerTool(	new RunCBuccaneer().PickACase());
					// new ThreadsRunner().Threads(new RunCBuccaneer());
					 
					// new RunCBuccaneer().RunBuccaneerTool();
					 new Buccaneeri1Testing().RunBuccaneerTool();
			 }
			  */
			  /*
			 else if(args[0].equals("RunCBuccaneerTestingProupse")){
					
				 
					
				 Parm.addAll(Arrays.asList(args));
			
				if(checkArg(Parm,"data")==null){
					System.out.println("One or more of the required parameters is missing! ");
					System.out.println("The required parameters are : ");
					System.out.println("data= the path for data folder");
					System.exit(-1);
				}
					 if(checkArg(Parm,"data")!=null){
						 RunningParameter.DataPath=checkArg(Parm,"data");
						
					 }
					new RunCBuccaneerTestingProupse().RunBuccaneerTool();
			 }
			  */
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
						 RunningParameter.DataPath=checkArg(Parm,"data");
						
					 }
					 if(checkArg(Parm,"wArpAutotracing")!=null){
						 RunningParameter.wArpAutotracing=checkArg(Parm,"wArpAutotracing");
						
					 }
					 if(checkArg(Parm,"UseBuccModels")!=null){
						 //RunningPram.UseBuccModels=checkArg(Parm,"UseBuccModels");
						 RunningParameter.UseInitialModels=checkArg(Parm,"UseBuccModels");
					 }
					 if(checkArg(Parm,"UseInitialModels")!=null) {
						 RunningParameter.UseInitialModels=checkArg(Parm,"UseInitialModels"); 
					 }
					 if(checkArg(Parm,"BuccModels")!=null){
						 //RunningPram.BuccModels=checkArg(Parm,"BuccModels");
						 RunningParameter.InitialModels=checkArg(Parm,"BuccModels");
						
					 }
					 if(checkArg(Parm,"InitialModels")!=null){
						 RunningParameter.InitialModels=checkArg(Parm,"InitialModels");
						
					 }
					 if(checkArg(Parm,"UsingRFree")!=null){
						 RunningParameter.UsingRFree=checkArg(Parm,"UsingRFree");
						
					 }
					 if(checkArg(Parm,"MR")!=null){
						 RunningParameter.MR=checkArg(Parm,"MR");
						
					 }
					
					 new Arp().RunwArpTool();
			 }
			  /*
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
						 RunningParameter.DataPath=checkArg(Parm,"data");
						
					 }
					 if(checkArg(Parm,"wArpAutotracing")!=null){
						 RunningParameter.wArpAutotracing=checkArg(Parm,"wArpAutotracing");
						
					 }
					 if(checkArg(Parm,"UseBuccModels")!=null){
						 //RunningPram.UseBuccModels=checkArg(Parm,"UseBuccModels");
						 RunningParameter.UseInitialModels=checkArg(Parm,"UseBuccModels");
						
					 }
					 if(checkArg(Parm,"UseInitialModels")!=null) {
						 RunningParameter.UseInitialModels=checkArg(Parm,"UseInitialModels"); 
					 }
					 if(checkArg(Parm,"BuccModels")!=null){
						 //RunningPram.BuccModels=checkArg(Parm,"BuccModels");
						 RunningParameter.InitialModels=checkArg(Parm,"BuccModels");
						
					 }
					 if(checkArg(Parm,"InitialModels")!=null){
						 RunningParameter.InitialModels=checkArg(Parm,"InitialModels");
						
					 }

					 new RunArpTesting().RunwArpTool();
 
			 }
			  */
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
						 RunningParameter.DataPath=checkArg(Parm,"data");
						 
					 }
				
					 if(checkArg(Parm,"PhenixAutobuild")!= null){
						 RunningParameter.PhenixAutobuild=checkArg(Parm,"PhenixAutobuild");
						
					 }
					 if(checkArg(Parm,"UsingRFree")!= null){
						 RunningParameter.UsingRFree=checkArg(Parm,"UsingRFree");
						
					 }
					 if(checkArg(Parm,"Phases")!= null){
						 RunningParameter.PhenixPhases=checkArg(Parm,"Phases");
						 
					 }
					 
					 
					 if(checkArg(Parm,"DensityModified")!= null){
						 RunningParameter.DensityModifiedPhenix=checkArg(Parm,"DensityModified");
						 
					 }
					 
					 if(checkArg(Parm,"UseInitialModels")!=null) {
						 RunningParameter.UseInitialModels=checkArg(Parm,"UseInitialModels"); 
					 }
					 if(checkArg(Parm,"InitialModels")!=null){
						 RunningParameter.InitialModels=checkArg(Parm,"InitialModels");
						
					 }
					 if(checkArg(Parm,"UsingPhenixRebuild_in_place")!=null) {
						 RunningParameter.UsingPhenixRebuild_in_place=checkArg(Parm,"UsingPhenixRebuild_in_place"); 
					 }
					 if(checkArg(Parm,"PhenixRebuild_in_place")!=null) {
						 RunningParameter.PhenixRebuild_in_place=checkArg(Parm,"PhenixRebuild_in_place"); 
					 }
					 if(checkArg(Parm,"MR")!=null){
						 RunningParameter.MR=checkArg(Parm,"MR");
						
					 }
					 if(checkArg(Parm,"PhenixCluster")!=null){
						 RunningParameter.PhenixCluster=checkArg(Parm,"PhenixCluster");
						
					 }
					 
					 
					 
					 RunningParameter.PhenixPhases= RunningParameter.PhenixPhases.replaceAll(",", " ");
					 System.out.println("data = "+RunningParameter.DataPath);
					 System.out.println("PhenixAutobuild = "+RunningParameter.PhenixAutobuild);
					
					new Phenix().RunPhenixTool();
			 }
			/* 
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
						 RunningParameter.DataPath=checkArg(Parm,"data");
						 
					 }
				
					 if(checkArg(Parm,"PhenixAutobuild")!= null){
						 RunningParameter.PhenixAutobuild=checkArg(Parm,"PhenixAutobuild");
						
					 }
					 if(checkArg(Parm,"Threads")!=null){
						 RunningParameter.NumberofThreads=checkArg(Parm,"Threads");
						
					 }
					 System.out.println("data = "+RunningParameter.DataPath);
					 System.out.println("PhenixAutobuild = "+RunningParameter.PhenixAutobuild);
					 new RunPhenixMultiThreads().RunPhenixTool(); 
			 }
			 
 */
			 
			 /*
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
						 RunningParameter.DataPath=checkArg(Parm,"data");
						
					 }
					
					 if(checkArg(Parm,"castat2Path")!=null){
						 RunningParameter.castat2Path=checkArg(Parm,"castat2Path");
						
					 }
					 if(checkArg(Parm,"LogsDir")!=null){
						 RunningParameter.LogsDirBuccaneer=checkArg(Parm,"LogsDir");
						
					 }
					 if(checkArg(Parm,"PDBsDir")!=null){
						 RunningParameter.PDBsDirBuccaneer=checkArg(Parm,"PDBsDir");
						
					 }
					
						new BuccaneerResultsAnalysis().AnalysingBuccaneerResults();

			 }
			 */
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
					System.out.println("PhasesUsedCPhasesMatch= Default is (HLA,HLB,HLC,HLD)");
					
					System.exit(-1);
				}
					 if(checkArg(Parm,"data")!=null){
						 RunningParameter.DataPath=checkArg(Parm,"data");
						
					 }
					
					 if(checkArg(Parm,"castat2Path")!=null){
						 RunningParameter.castat2Path=checkArg(Parm,"castat2Path");
						
					 }
					 if(checkArg(Parm,"LogsDir")!=null){
						 RunningParameter.LogsDir=checkArg(Parm,"LogsDir");
						
					 }
					 if(checkArg(Parm,"PDBsDir")!=null){
						 RunningParameter.PDBsDir=checkArg(Parm,"PDBsDir");
						
					 }
					 if(checkArg(Parm,"Threads")!=null){
						 RunningParameter.NumberofThreads=checkArg(Parm,"Threads");
						
					 }
					 if(checkArg(Parm,"ToolName")!=null){
						 RunningParameter.ToolName=checkArg(Parm,"ToolName");
						
					 }
					 if(checkArg(Parm,"IPDBsDir")!=null){
						 RunningParameter.IntermediatePDBs=checkArg(Parm,"IPDBsDir");
						
					 }
					 
					 if(checkArg(Parm,"ILogsDir")!=null){
						 RunningParameter.IntermediateLogs=checkArg(Parm,"ILogsDir");
						
					 }
					 if(checkArg(Parm,"MolProbity")!=null){
						 RunningParameter.PhenixMolProbity=checkArg(Parm,"MolProbity");
						
					 }
					 if(checkArg(Parm,"UsingMolProbity")!=null){
						 RunningParameter.UsingMolProbity=checkArg(Parm,"UsingMolProbity");
						
					 }
					 
					 if(checkArg(Parm,"PhasesUsedCPhasesMatch")!=null){
						 RunningParameter.PhasesUsedCPhasesMatch=checkArg(Parm,"PhasesUsedCPhasesMatch");
						
					 }
					 
					 if(checkArg(Parm,"FinalPhasesCPhasesMatch")!=null){
						 RunningParameter.FinalPhasesCPhasesMatch=checkArg(Parm,"FinalPhasesCPhasesMatch");
						
					 }
					 
					 if(checkArg(Parm,"MR")!=null){
						 RunningParameter.MR=checkArg(Parm,"MR");
						
					 }
					 if(RunningParameter.MR.equals("T")) {
						 
						 String RefmacScript=new Preparer().ReadResourceAsString("/refmacscript.sh");
						 RefmacScript=RefmacScript.replaceAll("FreeR_flag", "FREE");
						 if(new File("refmacscript.sh").exists())
							 FileUtils.deleteQuietly(new File("refmacscript.sh"));
						 
						 new Preparer().WriteTxtFile("refmacscript.sh", RefmacScript);
						 
						 //Moved to MultiThreadedAnalyser
						 //Apply Csymmatch on final PDB
						// new MatchChainsToRef().MatchUsingCsymmatch(new File(RunningParameter.DataPath).getAbsolutePath()+"/", new File(RunningParameter.PDBsDir).getAbsolutePath()+"/");
						// RunningParameter.PDBsDir=new File(RunningParameter.PDBsDir).getName();
					 
						//Apply Csymmatch on IntermediatePDBs
						// new MatchChainsToRef().MatchUsingCsymmatch(new File(RunningParameter.DataPath).getAbsolutePath()+"/", new File(RunningParameter.IntermediatePDBs).getAbsolutePath()+"/");
						// RunningParameter.IntermediatePDBs=new File(RunningParameter.IntermediatePDBs).getName();
					 
						
						 
						 //Remove DUM from PDB
						 //for(File PDB : new File(RunningParameter.PDBsDir).listFiles()) {
							// if(new RemovingDumAtoms().CheckingIfContainsDUMAtomsMR(PDB.getAbsolutePath()))
							// new RemovingDumAtoms().RemovingWithOverwrite(PDB.getAbsolutePath());
						 //}
						// for(File PDB : new File(RunningParameter.IntermediatePDBs).listFiles()) {
							// if(new RemovingDumAtoms().CheckingIfContainsDUMAtomsMR(PDB.getAbsolutePath()))
							// new RemovingDumAtoms().RemovingWithOverwrite(PDB.getAbsolutePath());
						// }
						
						 
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
			 RunningParameter.DataPath=checkArg(Parm,"data");
			
		 }
		
		 if(checkArg(Parm,"MolProbity")!=null){
			 RunningParameter.PhenixMolProbity=checkArg(Parm,"MolProbity");
			
		 }
		 if(checkArg(Parm,"PDBs")!=null){
			 RunningParameter.PDBs=checkArg(Parm,"PDBs");
			
		 }
		 if(checkArg(Parm,"Excel")!=null){
			 RunningParameter.ExcellPath=checkArg(Parm,"Excel");
			
		 }
		 if(checkArg(Parm,"Threads")!=null){
			 RunningParameter.NumberofThreads=checkArg(Parm,"Threads");
			
		 }
		 if(checkArg(Parm,"ToolName")!=null){
			 RunningParameter.ToolName=checkArg(Parm,"ToolName");
			
		 }
		
			new MolProbity().RunMol();

 }
			  /*
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
						 RunningParameter.DataPath=checkArg(Parm,"data");
						
					 }
					
					 if(checkArg(Parm,"castat2Path")!=null){
						 RunningParameter.castat2Path=checkArg(Parm,"castat2Path");
						
					 }
					 if(checkArg(Parm,"LogsDir")!=null){
						 RunningParameter.LogsDirwArp=checkArg(Parm,"LogsDir");
						
					 }
					 if(checkArg(Parm,"PDBsDir")!=null){
						 RunningParameter.PDBsDirwArp=checkArg(Parm,"PDBsDir");
						
					 }
					
					 new ArpResultsAnalysis2().AnalysingwArpResults();

			 }
			 */
			  /*
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
						 RunningParameter.DataPath=checkArg(Parm,"data");
						
					 }
					
					 if(checkArg(Parm,"castat2Path")!=null){
						 RunningParameter.castat2Path=checkArg(Parm,"castat2Path");
						
					 }
					 if(checkArg(Parm,"LogsDir")!=null){
						 RunningParameter.LogsDirPhenix=checkArg(Parm,"LogsDir");
						
					 }
					 if(checkArg(Parm,"PDBsDir")!=null){
						 RunningParameter.PDBsDirPhenix=checkArg(Parm,"PDBsDir");
						
					 }
					
					 new PhenixResultsAnalysis2().AnalysingPhenixResults();

			 }
			  */
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
				System.out.println("PhasesUsedCPhasesMatch= Default is (HLA,HLB,HLC,HLD)");
				System.out.println("FinalPhasesCPhasesMatch= Default is (sfcalc.ABCD.A,sfcalc.ABCD.B,sfcalc.ABCD.C,sfcalc.ABCD.D)");
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
