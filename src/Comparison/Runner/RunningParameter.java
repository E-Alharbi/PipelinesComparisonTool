package Comparison.Runner;
/**
*
* @author Emad Alharbi
* University of York
*/
public class RunningParameter {

	//All the parameters that used by this scripts from the command line 
	
	public static String DataPath;//Data folder path 
	public static String LogsDirBuccaneer;// logs files for Buccaneer
	public static String PDBsDirBuccaneer;// PDBS files that built by Buccaneer
	public static String castat2Path;// castat2 path
	public static String CphasesMatchScriptPath;//CphasesMatch Script Path in CCP4 folder
	public static String RefmacPath; // path for refmac in bin folder 
	public static String LogsDirwArp;// logs files for wArp
	public static String PDBsDirwArp;// PDBS files that built by wArp
	public static String LogsDirPhenix;// logs files for Phenix
	public static String PDBsDirPhenix;// PDBS files that built by Phenix
	public static String PhenixAutobuild;// PhenixAutobuild path
	public static String BuccaneerPipeLine;
	public static String wArpAutotracing; // path for ARP/wARP auto_tracing.sh 
	public static String ParrtoPhases; // columns parrot phases 
	
	public static String LogsDir;// logs files for Analyser
	public static String PDBsDir;// PDBS files for Analyser
	public static String ToolName;// Tool name  for  Analyser
	public static String NumberofThreads;// Tool name  for  Analyser
	
	//public static String UseBuccModels;// This option allows ARP starts from Bucc model as initial model
	public static String UseInitialModels="F";// This option allows ARP starts from Bucc model as initial model

	//public static String BuccModels;// path for BuccModels
	public static String InitialModels;// path for BuccModels
	public static String Buccaneeri2PipeLine;// Buccaneeri2 PipeLine
	
	public static String shScriptPath;// the path for script in a cluster server  
	public static String NoofTime;// Number of times to run the script. Using in the Script Manager 
	
	public static String CrankPipeLine;// Number of times to run the script 
	
	public static String ZeroCycle;// Number of times to run the script 
	
	public static String PhenixWorkingDirList; // the txt file which has the working dir lists 
	public static String PhenixMolProbity;// Path for Mol Probity
	public static String PDBs; // path for final PDB folder 
	public static String ExcellPath; // path to folder that contains excel files 
	
	public static String Pipelines; // which pipelines need to prepare to run 
	
	public static String ChltomDataPath;//Data folder path 
	public static String ccp4i2Core; 
	public static String DatafakeAnomalous;
	
	public static String IntermediatePDBs; // path for intermediate PDB folder 
	public static String IntermediateLogs; // path for intermediate logs folder 
	
	public static String CfakeAnom;
	public static String UsingMolProbity="T"; // Using Mol Probity in the Analyser. Default is False
	public static String PhasesUsedCPhasesMatch="HLA,HLB,HLC,HLD";// Default is HL  phases.
	
	public static String ClusterServerGrid="Slurm"; //  grid engine  
	public static String ExperimentType="Both";
	public static String UsingRFree="T"; // T to use R-free in building 
	
	public static String BuccaneerIterations="25"; // number of iterations for Buccaneer
	public static String PhenixPhases="hltofom.Phi_fom.phi,hltofom.Phi_fom.fom,parrot.ABCD.A,parrot.ABCD.B,parrot.ABCD.C,parrot.ABCD.D"; // columns to use for phenix autobuild 
	public static String DensityModifiedPhenix="F"; // if Parrot phases are used set to T
    public static String PhenixRebuild_in_place="False"; // enable Rebuild in place for phenix autobuild
    public static String UsingPhenixRebuild_in_place="None"; // set to false or true will effect when run Phenix 
    public static String Shelxe; // Shelxe binary  file 
    public static String SlurmAccount=""; // grid engine  account 
    public static String SlurmEmail=""; // email to use by grid engine to send notification 
    public static String CCP4ModuleLoadingCommand="module load chem/ccp4/7.0.066"; // load command and name of ccp4 module 
    public static String PhenixLoadingCommand="module load chem/phenix/1.14-3260";// load command and name of  phenix module
    public static String ShelxeData; // Path for data folder to use by Shelxe
    public static String MR="F"; // if this MR cases set to T
    public static String FinalPhasesCPhasesMatch="sfcalc.ABCD.A,sfcalc.ABCD.B,sfcalc.ABCD.C,sfcalc.ABCD.D";// Default is HL  phases.
    public static String ShelxeReflectionsSpace="-l2"; // flag to increase reflections space for Shelxe when the reflections are too many  
    public static String PhenixCluster="F"; // to use cluster feature in Phenix AutobBuild 
    public static String ReplaceToSingleLetter="F"; // replace pipelines names by its first letter in comparsion tables 
    public static String UpdateResolutionRefmac="F"; // to use resolution that read from Refmac instead of reading it from the file name (help when the file columns has different resolutions )
    public static String LimitUpdateResolutionFromRefmac="-1"; // use to limit the resolution from Refmac. Ex  LimitUpdateResolutionFromRefmac=3.1, meaning the resolutions lower than 3.1 read from Refmac and others take from file name
    public static String RemoveDummyAtomsFromInitialModel="F"; // remove ARP/wARP dummy atoms before use as initial model for others pipelines
}
