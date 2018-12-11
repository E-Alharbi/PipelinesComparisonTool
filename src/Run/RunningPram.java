package Run;

public class RunningPram {

	//All the parameters that used by this scripts from the command line 
	
	public static String DataPath;//Data folder path 
	public static String LogsDirBuccaneer;// logs files for Buccaneer
	public static String PDBsDirBuccaneer;// PDBS files that built by Buccaneer
	public static String castat2Path;// castat2 path
	public static String CphasesMatchScriptPath;//CphasesMatch Script Path in CCP4 folder
	public static String RefmacPath;
	public static String LogsDirwArp;// logs files for wArp
	public static String PDBsDirwArp;// PDBS files that built by wArp
	public static String LogsDirPhenix;// logs files for Phenix
	public static String PDBsDirPhenix;// PDBS files that built by Phenix
	public static String PhenixAutobuild;// PhenixAutobuild path
	public static String BuccaneerPipeLine;
	public static String wArpAutotracing;
	public static String ParrtoPhases;
	
	public static String LogsDir;// logs files for Analyser
	public static String PDBsDir;// PDBS files for Analyser
	public static String ToolName;// Tool name  for  Analyser
	public static String NumberofThreads;// Tool name  for  Analyser
	
	public static String UseBuccModels;// This option allows ARP starts from Bucc model as initial model
	public static String BuccModels;// path for BuccModels
	public static String Buccaneeri2PipeLine;// Buccaneeri2 PipeLine
	
	public static String shScriptPath;// the path for script in a cluster server  
	public static String NoofTime;// Number of times to run the script. Using in the Script Manager 
	
	public static String CrankPipeLine;// Number of times to run the script 
	
	public static String ZeroCycle;// Number of times to run the script 
	
	public static String PhenixWorkingDirList; // the txt file which has the working dir lists 
	public static String PhenixMolProbity;// Path for Mol Probity
	public static String PDBs;
	public static String ExcellPath;
	
	public static String Pipelines;
	
	public static String ChltomDataPath;//Data folder path 
	public static String ccp4i2Core;
	public static String DatafakeAnomalous;
	
	public static String IntermediatePDBs;
	public static String IntermediateLogs;
	
	public static String CfakeAnom;
	public static String UsingMolProbity="T"; // Using Mol Probity in the Analyser. Default is False
	//public static String PhasesUsedCPhasesMatch="parrot.ABCD.A,parrot.ABCD.B,parrot.ABCD.C,parrot.ABCD.D";// Default is parrot phases.
	public static String PhasesUsedCPhasesMatch="HLA,HLB,HLC,HLD";// Default is parrot phases.
	
	public static String ClusterServerGrid="Slurm";
	public static String ExperimentType="Both";
	public static String UsingRFree="T";
	
	public static String BuccaneerIterations="25";
	public static String PhenixPhases="hltofom.Phi_fom.phi,hltofom.Phi_fom.fom,parrot.ABCD.A,parrot.ABCD.B,parrot.ABCD.C,parrot.ABCD.D";
}
