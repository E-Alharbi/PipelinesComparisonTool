package Run;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.lang3.ArrayUtils;

import Analyser.DataContainer;
import Analyser.ExcelLoader;
import Analyser.ResultsAnalyserMultiThreads;
import NotUsed.ARPResultsAnalysis;

public class test {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		 Collection<File> Logs = FileUtils.listFiles(
				 new File("/Volumes/PhDHardDrive/VikingSync/1vku-4.0-parrot-noncs/"), 
				 new RegexFileFilter(".*_warpNtrace.pdb$"), 
				  FileFileFilter.FILE
				);
		 Vector <File>LogsFiles = new   Vector <File>();
		 LogsFiles.addAll(Logs);
		 System.out.println(LogsFiles.size());
		 for(int i=0 ; i < LogsFiles.size() ; ++i)
			 System.out.println(LogsFiles.get(i).getAbsolutePath());
		/*
		RunningPram.DataPath="/Volumes/PhDHardDrive/jcsg1200/mrncs/1o6a-1.9-parrot-mrncs.mtz";
		File[] files=null ;
	     if(new File(RunningPram.DataPath).isDirectory()) {
	    	 files = new File(RunningPram.DataPath).listFiles();
	     }
		if(new File(RunningPram.DataPath).isFile()) {
			
			files = ArrayUtils.add(files, new File(RunningPram.DataPath));
		}
		System.out.println(files.length);
		*/
		

		/*
		String [] Names=new ARPResultsAnalysis().readFileAsString("/Users/emadalharbi/Desktop/noncs.txt").split("\n");
		System.out.println(Names.length);
				int count=0;
				String Script="";
		for (String n : Names) {
			if(n.startsWith("-")) {
				count++;
				n=n.substring(1);
				Script+="rm wArpResults/PDBs/"+n+".pdb \n";
				Script+="rm wArpResults/ArpLogs/"+n+".txt \n";
				Script+="rm wArpResults/IntermediatePDBs/"+n+".pdb \n";
				Script+="rm wArpResults/IntermediateLogs/"+n+".txt \n";
			}
		}
		System.out.println(count);
		new Preparer().WriteTxtFile("Arpnoncs.sh",Script);
		*/
		/*
		new RunComparison().CheckDirAndFile("ARPNoncs");
		ExcelLoader e = new ExcelLoader();
		Vector<DataContainer> Container = e.ReadExcel("/Volumes/PhDHardDrive/jcsg1200Results/Fasta/Run13UpdatingReso/All/OrginalExFaliedCasesExcludedBuccaneerDevSet/noncs/ARPwARP.xlsx.xlsx.xlsx.xlsx.xlsx");
	
		for(int i=0 ; i <Container.size() ; ++i ) {
			 FileUtils.copyFile( new File("/Users/emadalharbi/Downloads/noncs/"+Container.get(i).PDB_ID+".mtz"),  new File("ARPNoncs/"+Container.get(i).PDB_ID+".mtz"));
			 FileUtils.copyFile( new File("/Users/emadalharbi/Downloads/noncs/"+Container.get(i).PDB_ID+".pdb"),  new File("ARPNoncs/"+Container.get(i).PDB_ID+".pdb"));
			 FileUtils.copyFile( new File("/Users/emadalharbi/Downloads/noncs/"+Container.get(i).PDB_ID+".fasta"),  new File("ARPNoncs/"+Container.get(i).PDB_ID+".fasta"));

		}
*/
	}

}
