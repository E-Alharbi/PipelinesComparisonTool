package NotToSync;

import java.io.File;
import java.util.Arrays;

import Comparison.Analyser.Bucaneeri2LogGenerator;

public class test4 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		File[] Folders = new File("/Volumes/PhDHardDrive/jcsg1200Results/Fasta/VikingRun3ArpNoFreeNoCrankRemovedAvgTestFixedLatexBugs/All/noncs").listFiles();
		Arrays.sort(Folders, (f1, f2) -> f1.compareTo(f2));
	for(File f : Folders) {
		System.out.println(f.getName());
	}
	}

}
