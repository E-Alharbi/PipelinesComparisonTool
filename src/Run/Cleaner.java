package Run;

import java.io.IOException;
import java.util.Vector;

import ResultsParsing.DataContainer;
import ResultsParsing.LoadExcel;

public class Cleaner {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		LoadExcel e = new LoadExcel();
		Vector<DataContainer> Container = e.ReadExcel("/Volumes/PhDHardDrive/jcsg1200Results/ExcelSheets14/hancs/Phenix.xlsx");
		int count=0;
		String Script="";
		String PDB="";
		
		for(int i=0; i < Container.size();++i ) {
			
			//if(Container.get(i).BuiltPDB.equals("F") || (Container.get(i).Intermediate.equals("T") && Math.round(Double.valueOf(Container.get(i).TimeTaking)/60) < 48 )) {
				if(Container.get(i).BuiltPDB.equals("F") || Container.get(i).Intermediate.equals("T")) {

				count++;
				//Script+="rm -r project"+Container.get(i).PDB_ID+" \n";
				//Script+="rm -r "+Container.get(i).PDB_ID+" \n";
				Script+="rm PhenixResults/PDBs/"+Container.get(i).PDB_ID+".pdb \n";
				Script+="rm PhenixResults/PhinexLogs/"+Container.get(i).PDB_ID+".txt \n";
				Script+="rm PhenixResults/IntermediatePDBs/"+Container.get(i).PDB_ID+".pdb \n";
				Script+="rm PhenixResults/IntermediateLogs/"+Container.get(i).PDB_ID+".txt \n";

			}
			else {
				PDB+=Container.get(i).PDB_ID+" \n";
			}
			
				/*
			if(Container.get(i).BuiltPDB.equals("F") || Container.get(i).Intermediate.equals("T")) {
					count++;
					Script+="rm -r project"+Container.get(i).PDB_ID+" \n";
					Script+="rm -r "+Container.get(i).PDB_ID+" \n";
					Script+="rm BuccaneerResults/PDBs/"+Container.get(i).PDB_ID+".pdb \n";
					Script+="rm BuccaneerResults/BuccaneerLogs/"+Container.get(i).PDB_ID+".txt \n";
					Script+="rm BuccaneerResults/IntermediatePDBs/"+Container.get(i).PDB_ID+".pdb \n";
					Script+="rm BuccaneerResults/IntermediateLogs/"+Container.get(i).PDB_ID+".txt \n";

				}
				else {
					PDB+=Container.get(i).PDB_ID+" \n";
				}
				*/
			/*
				if(Container.get(i).BuiltPDB.equals("F") || Container.get(i).Intermediate.equals("T")) {
					count++;
					Script+="rm -r "+Container.get(i).PDB_ID+"xml \n";
					
					Script+="rm CrankResults/PDBs/"+Container.get(i).PDB_ID+".pdb \n";
					Script+="rm CrankResults/CrankLogs/"+Container.get(i).PDB_ID+".txt \n";
					Script+="rm CrankResults/IntermediatePDBs/"+Container.get(i).PDB_ID+".pdb \n";
					Script+="rm CrankResults/IntermediateLogs/"+Container.get(i).PDB_ID+".txt \n";
					Script+="rm -r CrankResults/WorkingDir/"+Container.get(i).PDB_ID+" \n";

				}
				else {
					PDB+=Container.get(i).PDB_ID+" \n";
				}
				*/
			}
		new Preparer().WriteTxtFile("RemoverScript.sh",Script);
		new Preparer().WriteTxtFile("ProcessedFilesNamesPhenix.txt",PDB);
	}

}
