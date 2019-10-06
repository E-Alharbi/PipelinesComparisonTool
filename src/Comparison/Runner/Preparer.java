package Comparison.Runner;
/**
*
* @author Emad Alharbi
* University of York
*/
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
public class Preparer {

	/*
	 * Due to the large number of runs which used different tools and parameters. This Preparer creates all necessary
	 *  scripts that need to submit as jobs in the cluster server with minimum effort.
	 *  Calling this class form RunComparsion    
	 */
String ScriptHeader="";	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	
		
	}
public void Prepare(Vector<String> Tools) throws IOException {
	if(RunningParameter.ClusterServerGrid=="Slurm") {
		ScriptHeader="#!/bin/bash \n";
		
		ScriptHeader+="#SBATCH --time=8:00:00               \n";
		ScriptHeader+="#SBATCH --mem=4000                      \n";
		
		if(RunningParameter.SlurmEmail.length()!=0) {
		ScriptHeader+="#SBATCH --mail-type=ALL         \n";
		ScriptHeader+="#SBATCH --mail-user="+RunningParameter.SlurmEmail+"    \n";	
		}
		if(RunningParameter.SlurmAccount.length()!=0)
		ScriptHeader+="#SBATCH --account="+RunningParameter.SlurmAccount+" \n";
		
		ScriptHeader+=RunningParameter.CCP4ModuleLoadingCommand+" \n";
		ScriptHeader+=RunningParameter.PhenixLoadingCommand+" \n";
		
	}
	else {
	ScriptHeader="";
	ScriptHeader+="#$ -cwd";
	ScriptHeader+="#$ -V";
	ScriptHeader+="#$ -l h_vmem=2G";
	ScriptHeader+="#$ -l h_rt=48:00:00";
	}
	
	//String ReadMe= ReadResourceAsString("/ReadMe");
	//WriteTxtFile("./ReadMe.txt",ReadMe);
	String PHENIX=System.getenv("PHENIX");
	 Collection<File> files = FileUtils.listFiles(
			 new File(PHENIX), 
			  new RegexFileFilter("phenix.molprobity"), 
			  DirectoryFileFilter.DIRECTORY
			);
	 Vector<File> a = new Vector<File>();
		a.addAll(files);
		
		RunningParameter.PhenixMolProbity=a.get(0).getAbsolutePath();
		
		if(Tools.contains("Buccaneeri1"))
		   Tools.add("Buccaneeri1I5");
		if(Tools.contains("Buccaneeri2"))
		   Tools.add("Buccaneeri2I5");
		if(Tools.contains("Buccaneeri2W"))
		   Tools.add("Buccaneeri2WI5");
		if(Tools.contains("Phenix"))
		   Tools.add("PhenixHLA");
		if(Tools.contains("Buccaneeri1I5"))
		   Tools.add("ArpWArpAfterBuccaneeri1I5");
		if(Tools.contains("Shelxe"))
		   Tools.add("Shelxe");
		if(Tools.contains("ShelxeAfterParrot"))
		   Tools.add("ShelxeAfterParrot");
	
		
	 CreaterFolders(Tools);
	 if(Tools.contains("Phenix")) {
		
	   PhenixPrepare(RunningParameter.PhenixPhases,"Phenix",true);
	 }
	 if(Tools.contains("PhenixHLA"))
	   PhenixPrepare("PHIB,FOM,HLA,HLB,HLC,HLD","PhenixHLA",false);
	 if(Tools.contains("ArpWArp"))
	ArpwArpPrepare(false, null,null);
	 if(Tools.contains("Buccaneeri1"))
     Bucci1Prepare("25","Buccaneeri1");
	 if(Tools.contains("Buccaneeri1I5"))
	 Bucci1Prepare("5","Buccaneeri1I5");
	 if(Tools.contains("Buccaneeri2"))
	 Bucci2Prepare(false,"25","Buccaneeri2");
	 if(Tools.contains("Buccaneeri2I5"))
	  Bucci2Prepare(false,"5","Buccaneeri2I5");
	 if(Tools.contains("Buccaneeri2W"))
	 Bucci2Prepare(true,"25","Buccaneeri2W");
	 if(Tools.contains("Buccaneeri2WI5"))
	 Bucci2Prepare(true,"5","Buccaneeri2WI5");
	 if(Tools.contains("Crank"))
	 Crank();
	 if(Tools.contains("ArpWArpAfterBuccaneeri1"))
	 ArpwArpPrepare(true,"ArpWArpAfterBuccaneeri1","Buccaneeri1");
	 if(Tools.contains("ArpWArpAfterBuccaneeri1I5"))
	 ArpwArpPrepare(true,"ArpWArpAfterBuccaneeri1I5","Buccaneeri1I5");
	 if(Tools.contains("Shelxe"))
	 ShelxePrepare("Shelxe");
	 if(Tools.contains("ShelxeAfterParrot"))
	   ShelxePrepare("ShelxeAfterParrot");
}
boolean PhenixPrepare(String Phases, String WorkFolder, boolean DensityModifiedPhenix) throws IOException {
	String PHENIX=System.getenv("PHENIX");
	if(PHENIX.equals(""))
	{
		System.out.println("Unable to find Phenix. Be sure you have Phenix and you set the Phenix variables ");
	return false;
	}
	 Collection<File> files = FileUtils.listFiles(
			 new File(PHENIX), 
			  new RegexFileFilter("phenix.autobuild"), 
			  DirectoryFileFilter.DIRECTORY
			);
		 
		if(files.size()==0) {
			System.out.println("Unable to find phenix.autobuild. Be sure you have Phenix and you set the Phenix variables ");
return false;
		}
		if(files.size()>1) {
			System.out.println("There is more than two phenix.autobuild which is unusual");
return false;
		}
		Vector<File> a = new Vector<File>();
		a.addAll(files);
		
		RunningParameter.PhenixAutobuild=a.get(0).getAbsolutePath();
		int NumberOfFile=new File(RunningParameter.ChltomDataPath).listFiles().length;
		NumberOfFile=NumberOfFile/3;//because mtz,seq and pdb	
	Vector<String> PhenixScript = new Vector<String>();
	PhenixScript.addAll(Arrays.asList(ScriptHeader.split("\n")));
	
	PhenixScript.add("export MALLOC_ARENA_MAX=4");
	PhenixScript.add("vmArgs=\"-Xmx100m -XX:ParallelGCThreads=1\"");
	if(DensityModifiedPhenix==true)
	PhenixScript.add("java $vmArgs -jar RunComparison.jar RunPhenix data="+new File(RunningParameter.ChltomDataPath).getAbsolutePath()+" PhenixAutobuild="+RunningParameter.PhenixAutobuild+" UsingRFree="+RunningParameter.UsingRFree+" Phases="+Phases+" DensityModified=T \\ << eor");

	else	
	PhenixScript.add("java $vmArgs -jar RunComparison.jar RunPhenix data="+new File(RunningParameter.ChltomDataPath).getAbsolutePath()+" PhenixAutobuild="+RunningParameter.PhenixAutobuild+" UsingRFree="+RunningParameter.UsingRFree+" Phases="+Phases+" \\ << eor");
	PhenixScript.add("END");	
	PhenixScript.add("eor");	
	String PhenixSh="";
	for(int i=0 ; i < PhenixScript.size();++i) {
		PhenixSh+=PhenixScript.get(i)+"\n";
	}
	WriteTxtFile("./"+WorkFolder+"/Phenix.sh",PhenixSh);
	CreateManagerScript(NumberOfFile,WorkFolder,"Phenix.sh");
	new JobCreater().CreateJobs("Phenix.sh", "./"+WorkFolder);
	//Analyser
	String Analyser= ReadResourceAsString("/Analyser.sh");
	if(RunningParameter.ClusterServerGrid=="Slurm")
	Analyser= ReadResourceAsString("/AnalyserSlurm.sh");
	Analyser=Analyser.replace("&ccp4moduleload&", RunningParameter.CCP4ModuleLoadingCommand);
	Analyser=Analyser.replace("&phenixmoduleload&", RunningParameter.PhenixLoadingCommand);
	Analyser=Analyser.replace("&ccp4&", System.getenv("CCP4"));
	Analyser=Analyser.replace("&data&", new File(RunningParameter.DataPath).getAbsolutePath());
	Analyser=Analyser.replace("&cstat&", RunningParameter.castat2Path);
	Analyser=Analyser.replace("&Logs&", "../"+WorkFolder+"/PhenixResults/PhinexLogs");
	Analyser=Analyser.replace("&PDBs&", "../"+WorkFolder+"/PhenixResults/PDBs/");
	Analyser=Analyser.replace("&Tool&", "Phenix");
	Analyser=Analyser.replace("&IPDBs&", "../"+WorkFolder+"/PhenixResults/IntermediatePDBs/");
	Analyser=Analyser.replace("&ILogs&", "../"+WorkFolder+"/PhenixResults/IntermediateLogs/");
	Analyser=Analyser.replace("&Mol&", RunningParameter.PhenixMolProbity);
	Analyser=Analyser.replace("&UsingMol&", RunningParameter.UsingMolProbity);
	Analyser=Analyser.replace("&CPhasesMatchPhases&", RunningParameter.PhasesUsedCPhasesMatch);
	WriteTxtFile("./"+WorkFolder+"Analyser/PhenixAnalyser.sh",Analyser);
	return true;
}


boolean ArpwArpPrepare(boolean Bucc, String WorkFolder, String BuccFolder) throws IOException {
	String warpbin=System.getenv("warpbin");
	if(warpbin.equals(""))
	{
		System.out.println("Unable to find warpbin. Be sure you have Arp/wARP and you set the Arp/wARP variables ");
	return false;
	}
			
	RunningParameter.wArpAutotracing=warpbin+"/auto_tracing.sh";
	int NumberOfFile=new File(RunningParameter.ChltomDataPath).listFiles().length;
	NumberOfFile=NumberOfFile/3;//because mtz,seq and pdb	
	Vector<String> Script = new Vector<String>();
	Script.addAll(Arrays.asList(ScriptHeader.split("\n")));
	Script.add("export MALLOC_ARENA_MAX=4");
	Script.add("vmArgs=\"-Xmx100m -XX:ParallelGCThreads=1\"");
	if(Bucc==false)
	Script.add("java $vmArgs -jar RunComparison.jar RunwArp data="+new File(RunningParameter.ChltomDataPath).getAbsolutePath()+" wArpAutotracing="+RunningParameter.wArpAutotracing+" UsingRFree="+RunningParameter.UsingRFree+" UseBuccModels=F \\ << eor");
	if(Bucc==true)
    Script.add("java $vmArgs -jar RunComparison.jar RunwArp data="+new File(RunningParameter.ChltomDataPath).getAbsolutePath()+" wArpAutotracing="+RunningParameter.wArpAutotracing+" UsingRFree="+RunningParameter.UsingRFree+" UseBuccModels=T BuccModels=../"+BuccFolder+"/BuccaneerResults/PDBs/ \\ << eor");

	Script.add("END");	
	Script.add("eor");	
	String ArpSh="";
	for(int i=0 ; i < Script.size();++i) {
		ArpSh+=Script.get(i)+"\n";
	}
	
	if(Bucc==false) {
	WriteTxtFile("./ArpWArp/Arp.sh",ArpSh);
	CreateManagerScript(NumberOfFile,"ArpWArp","Arp.sh");
	new JobCreater().CreateJobs("Arp.sh", "./"+"ArpWArp");
	}
	if(Bucc==true) {
	WriteTxtFile("./"+WorkFolder+"/Arp.sh",ArpSh);
	CreateManagerScript(NumberOfFile,WorkFolder,"Arp.sh");
	new JobCreater().CreateJobs("Arp.sh", "./"+WorkFolder);
	}
	
	//Analyser
		String Analyser= ReadResourceAsString("/Analyser.sh");
		if(RunningParameter.ClusterServerGrid=="Slurm")
			Analyser= ReadResourceAsString("/AnalyserSlurm.sh");
		Analyser=Analyser.replace("&ccp4moduleload&", RunningParameter.CCP4ModuleLoadingCommand);
		Analyser=Analyser.replace("&phenixmoduleload&", RunningParameter.PhenixLoadingCommand);
		Analyser=Analyser.replace("&ccp4&", System.getenv("CCP4"));
		Analyser=Analyser.replace("&data&", new File(RunningParameter.DataPath).getAbsolutePath());
		Analyser=Analyser.replace("&cstat&", RunningParameter.castat2Path);
		
		Analyser=Analyser.replace("&Mol&", RunningParameter.PhenixMolProbity);
		Analyser=Analyser.replace("&UsingMol&", RunningParameter.UsingMolProbity);
		Analyser=Analyser.replace("&CPhasesMatchPhases&", RunningParameter.PhasesUsedCPhasesMatch);
		if(Bucc==false) {
		Analyser=Analyser.replace("&Tool&", "ARPwARP");
		Analyser=Analyser.replace("&Logs&", "../ArpWArp/wArpResults/ArpLogs");
		Analyser=Analyser.replace("&PDBs&", "../ArpWArp/wArpResults/PDBs");
		Analyser=Analyser.replace("&IPDBs&", "../ArpWArp/wArpResults/IntermediatePDBs/");
		Analyser=Analyser.replace("&ILogs&", "../ArpWArp/wArpResults/IntermediateLogs/");
		WriteTxtFile("./ArpWArpAnalyser/ArpWArpAnalyser.sh",Analyser);
		}
		if(Bucc==true) {
			Analyser=Analyser.replace("&Tool&", WorkFolder);
			Analyser=Analyser.replace("&Logs&", "../"+WorkFolder+"/wArpResults/ArpLogs");
			Analyser=Analyser.replace("&PDBs&", "../"+WorkFolder+"/wArpResults/PDBs");	
			Analyser=Analyser.replace("&IPDBs&", "../"+WorkFolder+"/wArpResults/IntermediatePDBs/");
			Analyser=Analyser.replace("&ILogs&", "../"+WorkFolder+"/wArpResults/IntermediateLogs/");
			WriteTxtFile("./"+WorkFolder+"Analyser/ArpWArpAnalyser.sh",Analyser);
		}
		
		
	
	return true;
}


boolean Bucci1Prepare(String Iterations,String WorkFolder) throws IOException {
	
	int NumberOfFile=new File(RunningParameter.DataPath).listFiles().length;
	NumberOfFile=NumberOfFile/3;//because mtz,seq and pdb	
	Vector<String> Script = new Vector<String>();
	Script.addAll(Arrays.asList(ScriptHeader.split("\n")));
	Script.add("export MALLOC_ARENA_MAX=4");
	Script.add("vmArgs=\"-Xmx100m -XX:ParallelGCThreads=1\"");
	Script.add("java $vmArgs -jar RunComparison.jar RunCBuccaneer data="+new File(RunningParameter.DataPath).getAbsolutePath()+" Iterations="+Iterations+" UsingRFree="+RunningParameter.UsingRFree+" \\ << eor");
	Script.add("END");	
	Script.add("eor");	
	String Buccaneeri1="";
	for(int i=0 ; i < Script.size();++i) {
		Buccaneeri1+=Script.get(i)+"\n";
	}
	WriteTxtFile("./"+WorkFolder+"/Buccaneeri1.sh",Buccaneeri1);
	CreateManagerScript(NumberOfFile,WorkFolder,"Buccaneeri1.sh");
	WriteRefMacScriptForBucci1();
	new JobCreater().CreateJobs("Buccaneeri1.sh", "./"+WorkFolder);
	
	
	//Analyser
	String Analyser= ReadResourceAsString("/Analyser.sh");
	if(RunningParameter.ClusterServerGrid=="Slurm")
		Analyser= ReadResourceAsString("/AnalyserSlurm.sh");
	Analyser=Analyser.replace("&ccp4moduleload&", RunningParameter.CCP4ModuleLoadingCommand);
	Analyser=Analyser.replace("&phenixmoduleload&", RunningParameter.PhenixLoadingCommand);
	Analyser=Analyser.replace("&ccp4&", System.getenv("CCP4"));
	Analyser=Analyser.replace("&data&", new File(RunningParameter.DataPath).getAbsolutePath());
	Analyser=Analyser.replace("&cstat&", RunningParameter.castat2Path);
	Analyser=Analyser.replace("&Logs&", "../"+WorkFolder+"/BuccaneerResults/BuccaneerLogs");
	Analyser=Analyser.replace("&PDBs&", "../"+WorkFolder+"/BuccaneerResults/PDBs/");
	Analyser=Analyser.replace("&Tool&", WorkFolder);
	Analyser=Analyser.replace("&IPDBs&", "../"+WorkFolder+"/BuccaneerResults/IntermediatePDBs/");
	Analyser=Analyser.replace("&ILogs&", "../"+WorkFolder+"/BuccaneerResults/IntermediateLogs/");
	Analyser=Analyser.replace("&Mol&", RunningParameter.PhenixMolProbity);
	Analyser=Analyser.replace("&UsingMol&", RunningParameter.UsingMolProbity);
	Analyser=Analyser.replace("&CPhasesMatchPhases&", RunningParameter.PhasesUsedCPhasesMatch);
	WriteTxtFile("./"+WorkFolder+"Analyser/Buccaneeri1Analyser.sh",Analyser);
	return true;
}
boolean ShelxePrepare(String WorkFolder) throws IOException {
	
	RunningParameter.Shelxe=System.getenv("CBIN")+"/shelxe";
	
	Vector<String> Script = new Vector<String>();
	Script.addAll(Arrays.asList(ScriptHeader.split("\n")));
	Script.add("export MALLOC_ARENA_MAX=4");
	Script.add("vmArgs=\"-Xmx100m -XX:ParallelGCThreads=1\"");
	Script.add("java $vmArgs -jar RunComparison.jar RunShelxe data="+new File(RunningParameter.ShelxeData).getAbsolutePath()+" Shelxe="+RunningParameter.Shelxe+" \\ << eor");
	Script.add("END");	
	Script.add("eor");	
	String Shelxe="";
	for(int i=0 ; i < Script.size();++i) {
		Shelxe+=Script.get(i)+"\n";
	}
	WriteTxtFile("./"+WorkFolder+"/Shelxe.sh",Shelxe);
	//CreateManagerScript(NumberOfFile,WorkFolder,"Buccaneeri1.sh");
	
	new JobCreater().CreateJobs("Shelxe.sh", "./"+WorkFolder);
	
	
	//Analyser
	String Analyser= ReadResourceAsString("/Analyser.sh");
	if(RunningParameter.ClusterServerGrid=="Slurm")
	Analyser= ReadResourceAsString("/AnalyserSlurm.sh");
	Analyser=Analyser.replace("&ccp4moduleload&", RunningParameter.CCP4ModuleLoadingCommand);
	Analyser=Analyser.replace("&phenixmoduleload&", RunningParameter.PhenixLoadingCommand);
	Analyser=Analyser.replace("&ccp4&", System.getenv("CCP4"));
	Analyser=Analyser.replace("&data&", new File(RunningParameter.DataPath).getAbsolutePath());
	Analyser=Analyser.replace("&cstat&", RunningParameter.castat2Path);
	Analyser=Analyser.replace("&Logs&", "../"+WorkFolder+"/shelxeResults/shelxeLogs");
	Analyser=Analyser.replace("&PDBs&", "../"+WorkFolder+"/shelxeResults/PDBs/");
	Analyser=Analyser.replace("&Tool&", WorkFolder);
	Analyser=Analyser.replace("&IPDBs&", "../"+WorkFolder+"/shelxeResults/IntermediatePDBs/");
	Analyser=Analyser.replace("&ILogs&", "../"+WorkFolder+"/shelxeResults/IntermediateLogs/");
	Analyser=Analyser.replace("&Mol&", RunningParameter.PhenixMolProbity);
	Analyser=Analyser.replace("&UsingMol&", RunningParameter.UsingMolProbity);
	Analyser=Analyser.replace("&CPhasesMatchPhases&", RunningParameter.PhasesUsedCPhasesMatch);
	WriteTxtFile("./"+WorkFolder+"Analyser/ShelxeAnalyser.sh",Analyser);
	return true;
}

boolean Crank() throws IOException {

	//WriteTxtFile("./Crank/crank.sh",ReadResourceAsString("/crank.sh"));
	WriteTxtFile("./Crank/fakeheavy.pdb",ReadResourceAsString("/fakeheavy.pdb"));
	//FileUtils.copyFile(Map.class.getResource("/crank.sh"),  new File("./Crank/crank.sh"));
	URL inputUrl = getClass().getResource("/crank.sh");
	File dest = new File("./Crank/crank.sh");
	FileUtils.copyURLToFile(inputUrl, dest);
	 inputUrl = getClass().getResource("/crankNoRfree.sh");
	 dest = new File("./Crank/crankNoRfree.sh");
	FileUtils.copyURLToFile(inputUrl, dest);
	Vector<String> Script = new Vector<String>();
	Script.addAll(Arrays.asList(ScriptHeader.split("\n")));
	Script.add("export MALLOC_ARENA_MAX=4");
	Script.add("vmArgs=\"-Xmx100m -XX:ParallelGCThreads=1\"");
	Script.add("java $vmArgs -jar RunComparison.jar RunCrank data="+new File(RunningParameter.DatafakeAnomalous).getAbsolutePath()+" UsingRFree="+RunningParameter.UsingRFree+" CrankPipeLine="+RunningParameter.ccp4i2Core+"/pipelines/crank2/crank2/crank2.py \\ << eor");
	Script.add("END");	
	Script.add("eor");	
	String Crank="";
	for(int i=0 ; i < Script.size();++i) {
		Crank+=Script.get(i)+"\n";
	}
	int NumberOfFile=new File(RunningParameter.DatafakeAnomalous).listFiles().length;
	NumberOfFile=NumberOfFile/3;//because mtz,seq and pdb	
	WriteTxtFile("./Crank/CrankJava.sh",Crank);
	CreateManagerScript(NumberOfFile,"Crank","CrankJava.sh");
	new JobCreater().CreateJobs("CrankJava.sh", "./"+"Crank");
	//Analyser
		String Analyser= ReadResourceAsString("/Analyser.sh");
		if(RunningParameter.ClusterServerGrid=="Slurm")
			Analyser= ReadResourceAsString("/AnalyserSlurm.sh");
		Analyser=Analyser.replace("&ccp4moduleload&", RunningParameter.CCP4ModuleLoadingCommand);
		Analyser=Analyser.replace("&phenixmoduleload&", RunningParameter.PhenixLoadingCommand);
		Analyser=Analyser.replace("&ccp4&", System.getenv("CCP4"));
		Analyser=Analyser.replace("&data&", new File(RunningParameter.DataPath).getAbsolutePath());
		Analyser=Analyser.replace("&cstat&", RunningParameter.castat2Path);
		Analyser=Analyser.replace("&Logs&", "../Crank/CrankResults/CrankLogs");
		Analyser=Analyser.replace("&PDBs&", "../Crank/CrankResults/PDBs/");
		Analyser=Analyser.replace("&Tool&", "Crank");
		Analyser=Analyser.replace("&IPDBs&", "../Crank/CrankResults/IntermediatePDBs/");
		Analyser=Analyser.replace("&ILogs&", "../Crank/CrankResults/IntermediateLogs/");
		Analyser=Analyser.replace("&Mol&", RunningParameter.PhenixMolProbity);
		Analyser=Analyser.replace("&UsingMol&", RunningParameter.UsingMolProbity);
		Analyser=Analyser.replace("&CPhasesMatchPhases&", RunningParameter.PhasesUsedCPhasesMatch);
		WriteTxtFile("./CrankAnalyser/CrankAnalyser.sh",Analyser);
	
	return true;
}
boolean Bucci2Prepare(boolean Water , String Iterations,String WorkFolder ) throws IOException {

	String bucrefi2= ReadResourceAsString("/bucrefi2.py");
	bucrefi2=bucrefi2.replace("ccp4i2Core", RunningParameter.ccp4i2Core);
	String PluginUtils= ReadResourceAsString("/PluginUtils.py");
	PluginUtils=PluginUtils.replace("ccp4i2Core", RunningParameter.ccp4i2Core);
	
	if(Water==true)
	{
	  	
      bucrefi2=bucrefi2.replace("controlParameters.COOT_REALSPACE_OPERATION ='none'", "controlParameters.COOT_REALSPACE_OPERATION ='coot_add_waters'");
      bucrefi2=bucrefi2.replace("$1", "W");// to solve Viking cluster problem  because sql cannot be created in /scratch 

      
	WriteTxtFile("./"+WorkFolder+"/bucrefi2.py",bucrefi2);
	WriteTxtFile("./"+WorkFolder+"/PluginUtils.py",PluginUtils);
	}
	else {
		 bucrefi2=bucrefi2.replace("$1", "");
		WriteTxtFile("./"+WorkFolder+"/bucrefi2.py",bucrefi2);
		
		WriteTxtFile("./"+WorkFolder+"/PluginUtils.py",PluginUtils);
	}

	int NumberOfFile=new File(RunningParameter.DataPath).listFiles().length;
	NumberOfFile=NumberOfFile/3;//because mtz,seq and pdb	
	Vector<String> Script = new Vector<String>();
	Script.addAll(Arrays.asList(ScriptHeader.split("\n")));
	Script.add("export MALLOC_ARENA_MAX=4");
	Script.add("vmArgs=\"-Xmx100m -XX:ParallelGCThreads=1\"");
	Script.add("java $vmArgs -jar RunComparison.jar RunBuccaneeri2 data="+new File(RunningParameter.DataPath).getAbsolutePath()+" Buccaneeri2=bucrefi2.py"+" Iterations="+Iterations+" UsingRFree="+RunningParameter.UsingRFree+" \\ << eor");
	Script.add("END");	
	Script.add("eor");	
	String Buccaneeri2="";
	for(int i=0 ; i < Script.size();++i) {
		Buccaneeri2+=Script.get(i)+"\n";
	}
	if(Water==true) {
	WriteTxtFile("./"+WorkFolder+"/Buccaneeri2W.sh",Buccaneeri2);
	
	CreateManagerScript(NumberOfFile,WorkFolder,"Buccaneeri2W.sh");
	new JobCreater().CreateJobs("Buccaneeri2W.sh", "./"+WorkFolder);
	}
	else {
		
	WriteTxtFile("./"+WorkFolder+"/Buccaneeri2.sh",Buccaneeri2);
	CreateManagerScript(NumberOfFile,WorkFolder,"Buccaneeri2.sh");
	new JobCreater().CreateJobs("Buccaneeri2.sh", "./"+WorkFolder);
	}
	
	//Analyser
			String Analyser= ReadResourceAsString("/Analyser.sh");
			if(RunningParameter.ClusterServerGrid=="Slurm")
			Analyser= ReadResourceAsString("/AnalyserSlurm.sh");
			Analyser=Analyser.replace("&ccp4moduleload&", RunningParameter.CCP4ModuleLoadingCommand);
			Analyser=Analyser.replace("&phenixmoduleload&", RunningParameter.PhenixLoadingCommand);
			Analyser=Analyser.replace("&ccp4&", System.getenv("CCP4"));
			Analyser=Analyser.replace("&data&", new File(RunningParameter.DataPath).getAbsolutePath());
			Analyser=Analyser.replace("&cstat&", RunningParameter.castat2Path);
			Analyser=Analyser.replace("&Mol&", RunningParameter.PhenixMolProbity);
			Analyser=Analyser.replace("&UsingMol&", RunningParameter.UsingMolProbity);
			Analyser=Analyser.replace("&CPhasesMatchPhases&", RunningParameter.PhasesUsedCPhasesMatch);
			if(Water==false) {
				Analyser=Analyser.replace("&Tool&", WorkFolder);
				Analyser=Analyser.replace("&Logs&", "../"+WorkFolder+"/BuccaneerResults/BuccaneerLogs");
				Analyser=Analyser.replace("&PDBs&", "../"+WorkFolder+"/BuccaneerResults/PDBs/");
				Analyser=Analyser.replace("&IPDBs&", "../"+WorkFolder+"/BuccaneerResults/IntermediatePDBs/");
				Analyser=Analyser.replace("&ILogs&", "../"+WorkFolder+"/BuccaneerResults/IntermediateLogs/");
				WriteTxtFile("./"+WorkFolder+"Analyser/Buccaneeri2Analyser.sh",Analyser);
			}
			if(Water==true) {
				Analyser=Analyser.replace("&Tool&", WorkFolder);
				Analyser=Analyser.replace("&Logs&", "../"+WorkFolder+"/BuccaneerResults/BuccaneerLogs");
				Analyser=Analyser.replace("&PDBs&", "../"+WorkFolder+"/BuccaneerResults/PDBs/");
				Analyser=Analyser.replace("&IPDBs&", "../"+WorkFolder+"/BuccaneerResults/IntermediatePDBs/");
				Analyser=Analyser.replace("&ILogs&", "../"+WorkFolder+"/BuccaneerResults/IntermediateLogs/");
				WriteTxtFile("./"+WorkFolder+"Analyser/Buccaneeri2WAnalyser.sh",Analyser);
				
			}
			
			
			
	
	return true;
}
void WriteRefMacScriptForBucci1() throws IOException {
	Vector<String> Script = new Vector<String>();
	Script.add("#!/bin/csh -f");
	Script.add("#");
	Script.add("#");
	Script.add("#");
	Script.add("$1 \\");
	Script.add("HKLIN   $2 \\");
	Script.add("HKLOUT   $3 \\");
	Script.add("XYZIN   $4 \\");
	Script.add("XYZOUT  $5  << eor");
	Script.add("NCYCLES 10");
	Script.add("LABIN FP=FP SIGFP=SIGFP FREE=FreeR_flag HLA=parrot.ABCD.A HLB=parrot.ABCD.B HLC=parrot.ABCD.C HLD=parrot.ABCD.D");
	Script.add("weight AUTO");
	Script.add("make check NONE");
	Script.add("make hydrogen NO hout NO peptide NO cispeptide YES ssbridge YES symmetry YES sugar YES connectivity NO link NO");
	Script.add("refi type REST PHASE resi MLKF meth CGMAT bref ISOT");
	Script.add("scal type SIMP LSSC ANISO EXPE");
	Script.add("solvent YES VDWProb 1.4 IONProb 0.8 RSHRink 0.8");
	Script.add("PHOUT");
	Script.add("PNAME buccaneer");
	Script.add("DNAME buccaneer");
	Script.add("DNAME buccaneer");
	Script.add("END");	
	Script.add("eor");	
	String Refmac="";
	for(int i=0 ; i < Script.size();++i) {
		Refmac+=Script.get(i)+"\n";
	}
	WriteTxtFile("./Buccaneeri1/refmacscript.sh",Refmac);
}

	public boolean CreaterFolders(Vector<String> Tools) throws IOException{
		
		for(int i=0 ; i < Tools.size() ; ++i) {
			
			if(RunComparison.CheckDirAndFile(Tools.get(i))==false || RunComparison.CheckDirAndFile(Tools.get(i)+"Analyser")==false) {
				System.out.println("Unable to create a folder for "+ Tools.get(i));
				return false;
			}
   			FileUtils.copyFile(new File("RunComparison.jar"),  new File(Tools.get(i)+"/RunComparison.jar"));
   			FileUtils.copyFile(new File("RunComparison.jar"),  new File(Tools.get(i)+"Analyser/RunComparison.jar"));
   			URL inputUrl = getClass().getResource("/refmacscript.sh");
   			File dest = new File("./"+Tools.get(i)+"Analyser/refmacscript.sh");
   			FileUtils.copyURLToFile(inputUrl, dest);
		}
		return true;
			
	}
	
	 public void WriteTxtFile(String FileName, String Txt) throws IOException{
		File yourFile = new File(FileName);
		yourFile.createNewFile();
		 BufferedWriter output;
		 output = new BufferedWriter(new FileWriter(FileName, true));
		 output.append(Txt);
		
		 output.close();
	}	
	 void CreateManagerScript(int NumberofFiles , String ToolName, String ToolScript) throws IOException {
		 Vector<String> ManagerScript = new Vector<String>(); 
		 ManagerScript.addAll(Arrays.asList(ScriptHeader.split("\n")));
		 
		 ManagerScript.add("export MALLOC_ARENA_MAX=4");
		 ManagerScript.add("vmArgs=\"-Xmx100m -XX:ParallelGCThreads=1\"");
		 ManagerScript.add("java $vmArgs -jar RunComparison.jar ScriptManager shScriptPath="+ToolScript+" NumberofTimes="+NumberofFiles+" \\ << eor");
		 ManagerScript.add("END");	
		 ManagerScript.add("eor");	
			String ManagerSh="";
			for(int i=0 ; i < ManagerScript.size();++i) {
				ManagerSh+=ManagerScript.get(i)+"\n";
			}
			WriteTxtFile("./"+ToolName+"/"+"Manager"+ToolName+".sh",ManagerSh);
	 
	 }

public String ReadResourceAsString (String FileName) throws IOException {
	String Txt="";
InputStream res =
Map.class.getResourceAsStream(FileName);

BufferedReader reader = new BufferedReader(new InputStreamReader(res));
String line = null;

while ((line = reader.readLine()) != null) {

Txt+=line +" \n";
}
		    reader.close();
	return 	  Txt;  
}
}
