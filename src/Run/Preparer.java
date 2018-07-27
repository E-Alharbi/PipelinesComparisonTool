package Run;

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

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
public class Preparer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Vector<String> Tools = new Vector<String>();
		Tools.add("Phenix");
		Tools.add("ArpWArp");
		Tools.add("Bucci1");
		Tools.add("Bucci2");
		Tools.add("BucciW");
		Tools.add("Crank");
		Tools.add("ArpWArpAfterBucci1");
		
	}
public void Prepare(Vector<String> Tools) throws IOException {
	
	String ReadMe= ReadResourceAsString("/ReadMe");
	WriteTxtFile("./ReadMe.txt",ReadMe);
	String PHENIX=System.getenv("PHENIX");
	 Collection<File> files = FileUtils.listFiles(
			 new File(PHENIX), 
			  new RegexFileFilter("phenix.molprobity"), 
			  DirectoryFileFilter.DIRECTORY
			);
	 Vector<File> a = new Vector<File>();
		a.addAll(files);
		
		RunningPram.PhenixMolProbity=a.get(0).getAbsolutePath();
	 CreaterFolders(Tools);
	 if(Tools.contains("Phenix"))
		 PhenixPrepare();
	 if(Tools.contains("ArpWArp"))
		 ArpwArpPrepare(false);
	 if(Tools.contains("Buccaneeri1"))
		 Bucci1Prepare();
	 if(Tools.contains("Buccaneeri2"))
	     Bucci2Prepare(false);
	 if(Tools.contains("Buccaneeri2W"))
	   Bucci2Prepare(true);
	 if(Tools.contains("Crank"))
	 Crank();
	 if(Tools.contains("ArpWArpAfterBuccaneeri1"))
	ArpwArpPrepare(true);
}
boolean PhenixPrepare() throws IOException {
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
		
		RunningPram.PhenixAutobuild=a.get(0).getAbsolutePath();
		int NumberOfFile=new File(RunningPram.ChltomDataPath).listFiles().length;
		NumberOfFile=NumberOfFile/3;//because mtz,seq and pdb	
	Vector<String> PhenixScript = new Vector<String>();
	PhenixScript.add("#$ -cwd");
	PhenixScript.add("#$ -V");
	PhenixScript.add("#$ -l h_vmem=2G");
	PhenixScript.add("#$ -l h_rt=48:00:00");
	PhenixScript.add("export MALLOC_ARENA_MAX=4");
	PhenixScript.add("vmArgs=\"-Xmx100m -XX:ParallelGCThreads=1\"");
	PhenixScript.add("java $vmArgs -jar RunComparison.jar RunPhenix data="+new File(RunningPram.ChltomDataPath).getAbsolutePath()+" PhenixAutobuild="+RunningPram.PhenixAutobuild+" \\ << eor");
	PhenixScript.add("END");	
	PhenixScript.add("eor");	
	String PhenixSh="";
	for(int i=0 ; i < PhenixScript.size();++i) {
		PhenixSh+=PhenixScript.get(i)+"\n";
	}
	WriteTxtFile("./Phenix/Phenix.sh",PhenixSh);
	CreateManagerScript(NumberOfFile,"Phenix","Phenix.sh");
	
	//Analyser
	String Analyser= ReadResourceAsString("/Analyser.sh");
	Analyser=Analyser.replace("&ccp4&", System.getenv("CCP4"));
	Analyser=Analyser.replace("&data&", new File(RunningPram.DataPath).getAbsolutePath());
	Analyser=Analyser.replace("&cstat&", RunningPram.castat2Path);
	Analyser=Analyser.replace("&Logs&", "../Phenix/PhenixResults/PhinexLogs");
	Analyser=Analyser.replace("&PDBs&", "../Phenix/PhenixResults/PDBs/");
	Analyser=Analyser.replace("&Tool&", "Phenix");
	Analyser=Analyser.replace("&IPDBs&", "../Phenix/PhenixResults/IntermediatePDBs/");
	Analyser=Analyser.replace("&ILogs&", "../Phenix/PhenixResults/IntermediateLogs/");
	Analyser=Analyser.replace("&Mol&", RunningPram.PhenixMolProbity);
	WriteTxtFile("./PhenixAnalyser/PhenixAnalyser.sh",Analyser);
	return true;
}


boolean ArpwArpPrepare(boolean Bucc) throws IOException {
	String warpbin=System.getenv("warpbin");
	if(warpbin.equals(""))
	{
		System.out.println("Unable to find warpbin. Be sure you have Phenix and you set the Phenix variables ");
	return false;
	}
			
	RunningPram.wArpAutotracing=warpbin+"/auto_tracing.sh";
	int NumberOfFile=new File(RunningPram.ChltomDataPath).listFiles().length;
	NumberOfFile=NumberOfFile/3;//because mtz,seq and pdb	
	Vector<String> Script = new Vector<String>();
	Script.add("#$ -cwd");
	Script.add("#$ -V");
	Script.add("#$ -l h_vmem=2G");
	Script.add("#$ -l h_rt=48:00:00");
	Script.add("export MALLOC_ARENA_MAX=4");
	Script.add("vmArgs=\"-Xmx100m -XX:ParallelGCThreads=1\"");
	if(Bucc==false)
	Script.add("java $vmArgs -jar RunComparison.jar RunwArp data="+new File(RunningPram.ChltomDataPath).getAbsolutePath()+" wArpAutotracing="+RunningPram.wArpAutotracing+" UseBuccModels=F \\ << eor");
	if(Bucc==true)
    Script.add("java $vmArgs -jar RunComparison.jar RunwArp data="+new File(RunningPram.ChltomDataPath).getAbsolutePath()+" wArpAutotracing="+RunningPram.wArpAutotracing+" UseBuccModels=T BuccModels=../Buccaneeri1/BuccaneerResults/PDBs/ \\ << eor");

	Script.add("END");	
	Script.add("eor");	
	String ArpSh="";
	for(int i=0 ; i < Script.size();++i) {
		ArpSh+=Script.get(i)+"\n";
	}
	
	if(Bucc==false) {
	WriteTxtFile("./ArpWArp/Arp.sh",ArpSh);
	CreateManagerScript(NumberOfFile,"ArpWArp","Arp.sh");
	}
	if(Bucc==true) {
	WriteTxtFile("./ArpWArpAfterBuccaneeri1/Arp.sh",ArpSh);
	CreateManagerScript(NumberOfFile,"ArpWArpAfterBuccaneeri1","Arp.sh");
	}
	
	//Analyser
		String Analyser= ReadResourceAsString("/Analyser.sh");
		Analyser=Analyser.replace("&ccp4&", System.getenv("CCP4"));
		Analyser=Analyser.replace("&data&", new File(RunningPram.DataPath).getAbsolutePath());
		Analyser=Analyser.replace("&cstat&", RunningPram.castat2Path);
		
		Analyser=Analyser.replace("&Mol&", RunningPram.PhenixMolProbity);
		if(Bucc==false) {
		Analyser=Analyser.replace("&Tool&", "ARPwARP");
		Analyser=Analyser.replace("&Logs&", "../ArpWArp/wArpResults/ArpLogs");
		Analyser=Analyser.replace("&PDBs&", "../ArpWArp/wArpResults/PDBs");
		Analyser=Analyser.replace("&IPDBs&", "../ArpWArp/wArpResults/IntermediatePDBs/");
		Analyser=Analyser.replace("&ILogs&", "../ArpWArp/wArpResults/IntermediateLogs/");
		WriteTxtFile("./ArpWArpAnalyser/ArpWArpAnalyser.sh",Analyser);
		}
		if(Bucc==true) {
			Analyser=Analyser.replace("&Tool&", "ARPwARPAfterBuccaneeri1");
			Analyser=Analyser.replace("&Logs&", "../ArpWArpAfterBuccaneeri1/wArpResults/ArpLogs");
			Analyser=Analyser.replace("&PDBs&", "../ArpWArpAfterBuccaneeri1/wArpResults/PDBs");	
			Analyser=Analyser.replace("&IPDBs&", "../ArpWArpAfterBuccaneeri1/wArpResults/IntermediatePDBs/");
			Analyser=Analyser.replace("&ILogs&", "../ArpWArpAfterBuccaneeri1/wArpResults/IntermediateLogs/");
			WriteTxtFile("./ArpWArpAfterBuccaneeri1Analyser/ArpWArpAnalyser.sh",Analyser);
		}
		
		
	
	return true;
}


boolean Bucci1Prepare() throws IOException {
	
	int NumberOfFile=new File(RunningPram.DataPath).listFiles().length;
	NumberOfFile=NumberOfFile/3;//because mtz,seq and pdb	
	Vector<String> Script = new Vector<String>();
	Script.add("#$ -cwd");
	Script.add("#$ -V");
	Script.add("#$ -l h_vmem=2G");
	Script.add("#$ -l h_rt=48:00:00");
	Script.add("export MALLOC_ARENA_MAX=4");
	Script.add("vmArgs=\"-Xmx100m -XX:ParallelGCThreads=1\"");
	Script.add("java $vmArgs -jar RunComparison.jar RunCBuccaneer data="+new File(RunningPram.DataPath).getAbsolutePath()+" \\ << eor");
	Script.add("END");	
	Script.add("eor");	
	String Buccaneeri1="";
	for(int i=0 ; i < Script.size();++i) {
		Buccaneeri1+=Script.get(i)+"\n";
	}
	WriteTxtFile("./Buccaneeri1/Buccaneeri1.sh",Buccaneeri1);
	CreateManagerScript(NumberOfFile,"Buccaneeri1","Buccaneeri1.sh");
	WriteRefMacScriptForBucci1();
	
	
	
	//Analyser
	String Analyser= ReadResourceAsString("/Analyser.sh");
	Analyser=Analyser.replace("&ccp4&", System.getenv("CCP4"));
	Analyser=Analyser.replace("&data&", new File(RunningPram.DataPath).getAbsolutePath());
	Analyser=Analyser.replace("&cstat&", RunningPram.castat2Path);
	Analyser=Analyser.replace("&Logs&", "../Buccaneeri1/BuccaneerResults/BuccaneerLogs");
	Analyser=Analyser.replace("&PDBs&", "../Buccaneeri1/BuccaneerResults/PDBs/");
	Analyser=Analyser.replace("&Tool&", "Buccaneer");
	Analyser=Analyser.replace("&IPDBs&", "../Buccaneeri1/BuccaneerResults/IntermediatePDBs/");
	Analyser=Analyser.replace("&ILogs&", "../Buccaneeri1/BuccaneerResults/IntermediateLogs/");
	Analyser=Analyser.replace("&Mol&", RunningPram.PhenixMolProbity);
	WriteTxtFile("./Buccaneeri1Analyser/Buccaneeri1Analyser.sh",Analyser);
	return true;
}
boolean Crank() throws IOException {

	//WriteTxtFile("./Crank/crank.sh",ReadResourceAsString("/crank.sh"));
	WriteTxtFile("./Crank/fakeheavy.pdb",ReadResourceAsString("/fakeheavy.pdb"));
	//FileUtils.copyFile(Map.class.getResource("/crank.sh"),  new File("./Crank/crank.sh"));
	URL inputUrl = getClass().getResource("/crank.sh");
	File dest = new File("./Crank/crank.sh");
	FileUtils.copyURLToFile(inputUrl, dest);
	Vector<String> Script = new Vector<String>();
	Script.add("#$ -cwd");
	Script.add("#$ -V");
	Script.add("#$ -l h_vmem=2G");
	Script.add("#$ -l h_rt=48:00:00");
	Script.add("export MALLOC_ARENA_MAX=4");
	Script.add("vmArgs=\"-Xmx100m -XX:ParallelGCThreads=1\"");
	Script.add("java $vmArgs -jar RunComparison.jar RunCrank data="+new File(RunningPram.DatafakeAnomalous).getAbsolutePath()+" CrankPipeLine="+RunningPram.ccp4i2Core+"/pipelines/crank2/crank2/crank2.py \\ << eor");
	Script.add("END");	
	Script.add("eor");	
	String Crank="";
	for(int i=0 ; i < Script.size();++i) {
		Crank+=Script.get(i)+"\n";
	}
	int NumberOfFile=new File(RunningPram.DatafakeAnomalous).listFiles().length;
	NumberOfFile=NumberOfFile/3;//because mtz,seq and pdb	
	WriteTxtFile("./Crank/CrankJava.sh",Crank);
	CreateManagerScript(NumberOfFile,"Crank","CrankJava.sh");
	
	//Analyser
		String Analyser= ReadResourceAsString("/Analyser.sh");
		Analyser=Analyser.replace("&ccp4&", System.getenv("CCP4"));
		Analyser=Analyser.replace("&data&", new File(RunningPram.DataPath).getAbsolutePath());
		Analyser=Analyser.replace("&cstat&", RunningPram.castat2Path);
		Analyser=Analyser.replace("&Logs&", "../Crank/CrankResults/CrankLogs");
		Analyser=Analyser.replace("&PDBs&", "../Crank/CrankResults/PDBs/");
		Analyser=Analyser.replace("&Tool&", "Crank");
		Analyser=Analyser.replace("&IPDBs&", "../Crank/CrankResults/IntermediatePDBs/");
		Analyser=Analyser.replace("&ILogs&", "../Crank/CrankResults/IntermediateLogs/");
		Analyser=Analyser.replace("&Mol&", RunningPram.PhenixMolProbity);
		WriteTxtFile("./CrankAnalyser/CrankAnalyser.sh",Analyser);
	
	return true;
}
boolean Bucci2Prepare(boolean Water) throws IOException {

	String bucrefi2= ReadResourceAsString("/bucrefi2.py");
	bucrefi2=bucrefi2.replace("ccp4i2Core", RunningPram.ccp4i2Core);
	String PluginUtils= ReadResourceAsString("/PluginUtils.py");
	PluginUtils=PluginUtils.replace("ccp4i2Core", RunningPram.ccp4i2Core);
	if(Water==true)
	{
      bucrefi2=bucrefi2.replace("controlParameters.COOT_REALSPACE_OPERATION ='none'", "controlParameters.COOT_REALSPACE_OPERATION ='coot_add_waters'");
	
	WriteTxtFile("./Buccaneeri2W/bucrefi2.py",bucrefi2);
	WriteTxtFile("./Buccaneeri2W/PluginUtils.py",PluginUtils);
	}
	else {
		WriteTxtFile("./Buccaneeri2/bucrefi2.py",bucrefi2);
		
		WriteTxtFile("./Buccaneeri2/PluginUtils.py",PluginUtils);
	}

	int NumberOfFile=new File(RunningPram.DataPath).listFiles().length;
	NumberOfFile=NumberOfFile/3;//because mtz,seq and pdb	
	Vector<String> Script = new Vector<String>();
	Script.add("#$ -cwd");
	Script.add("#$ -V");
	Script.add("#$ -l h_vmem=2G");
	Script.add("#$ -l h_rt=48:00:00");
	Script.add("export MALLOC_ARENA_MAX=4");
	Script.add("vmArgs=\"-Xmx100m -XX:ParallelGCThreads=1\"");
	Script.add("java $vmArgs -jar RunComparison.jar RunBuccaneeri2 data="+new File(RunningPram.DataPath).getAbsolutePath()+" Buccaneeri2=bucrefi2.py \\ << eor");
	Script.add("END");	
	Script.add("eor");	
	String Buccaneeri2="";
	for(int i=0 ; i < Script.size();++i) {
		Buccaneeri2+=Script.get(i)+"\n";
	}
	if(Water==true) {
	WriteTxtFile("./Buccaneeri2W/Buccaneeri2W.sh",Buccaneeri2);
	
	CreateManagerScript(NumberOfFile,"Buccaneeri2W","Buccaneeri2W.sh");
	}
	else {
		
	WriteTxtFile("./Buccaneeri2/Buccaneeri2.sh",Buccaneeri2);
	CreateManagerScript(NumberOfFile,"Buccaneeri2","Buccaneeri2.sh");
	}
	
	//Analyser
			String Analyser= ReadResourceAsString("/Analyser.sh");
			Analyser=Analyser.replace("&ccp4&", System.getenv("CCP4"));
			Analyser=Analyser.replace("&data&", new File(RunningPram.DataPath).getAbsolutePath());
			Analyser=Analyser.replace("&cstat&", RunningPram.castat2Path);
			Analyser=Analyser.replace("&Mol&", RunningPram.PhenixMolProbity);
			if(Water==true) {
				Analyser=Analyser.replace("&Tool&", "Buccaneeri2");
				Analyser=Analyser.replace("&Logs&", "../Buccaneeri2/BuccaneerResults/BuccaneerLogs");
				Analyser=Analyser.replace("&PDBs&", "../Buccaneeri2/BuccaneerResults/PDBs/");
				Analyser=Analyser.replace("&IPDBs&", "../Buccaneeri2/BuccaneerResults/IntermediatePDBs/");
				Analyser=Analyser.replace("&ILogs&", "../Buccaneeri2/BuccaneerResults/IntermediateLogs/");
				WriteTxtFile("./Buccaneeri2Analyser/Buccaneeri2Analyser.sh",Analyser);
			}
			if(Water==false) {
				Analyser=Analyser.replace("&Tool&", "Buccaneeri2W");
				Analyser=Analyser.replace("&Logs&", "../Buccaneeri2W/BuccaneerResults/BuccaneerLogs");
				Analyser=Analyser.replace("&PDBs&", "../Buccaneeri2W/BuccaneerResults/PDBs/");
				Analyser=Analyser.replace("&IPDBs&", "../Buccaneeri2W/BuccaneerResults/IntermediatePDBs/");
				Analyser=Analyser.replace("&ILogs&", "../Buccaneeri2W/BuccaneerResults/IntermediateLogs/");
				WriteTxtFile("./Buccaneeri2WAnalyser/Buccaneeri2WAnalyser.sh",Analyser);
				
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
		 ManagerScript.add("#$ -cwd");
		 ManagerScript.add("#$ -V");
		 ManagerScript.add("#$ -l h_vmem=2G");
		 ManagerScript.add("#$ -l h_rt=168:00:00");
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
//System.out.println(line);
Txt+=line +" \n";
}
		    reader.close();
	return 	  Txt;  
}
}
