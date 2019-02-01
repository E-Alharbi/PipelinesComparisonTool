package Run;

import java.util.Vector;

import Analyser.DataContainer;
import Analyser.ExcelLoader;

import java.io.IOException;
import java.util.Collections;
public class test3 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ExcelLoader e = new ExcelLoader();
		Vector<DataContainer> listoffiles = e.ReadExcel("/Volumes/PhDHardDrive/jcsg1200Results/Fasta/Run13UpdatingResoWithArpNoRFree/All/Synthetic/hancs/Buccaneeri1-5.xlsx.xlsx.xlsx");
		
		Vector<String> files = new Vector<String>();
		for(int i=0 ; i < listoffiles.size() ; ++i)
		{
			
			files.add(listoffiles.get(i).PDB_ID);
		}
		Collections.shuffle(files);
		String Mini="";
		String Training="";
		String Testing="";
		for(int i=0 ; i < 224 ; ++i)
		{
			Mini+="cp /users/emra500/scratch/Dataset/noncs/noncs/"+files.get(i)+".mtz ./ \n";
			Mini+="cp /users/emra500/scratch/Dataset/noncs/noncs/"+files.get(i)+".fasta ./ \n";
			Mini+="cp /users/emra500/scratch/Dataset/noncs/noncs/"+files.get(i)+".pdb ./ \n";
		}
		
		for(int i=224 ; i < 672 ; ++i)
		{
			Training+="cp /users/emra500/scratch/Dataset/noncs/noncs/"+files.get(i)+".mtz ./ \n";
			Training+="cp /users/emra500/scratch/Dataset/noncs/noncs/"+files.get(i)+".fasta ./ \n";
			Training+="cp /users/emra500/scratch/Dataset/noncs/noncs/"+files.get(i)+".pdb ./ \n";
		}
		
		for(int i=672 ; i < 1009 ; ++i)
		{
			Testing+="cp /users/emra500/scratch/Dataset/noncs/noncs/"+files.get(i)+".mtz ./ \n";
			Testing+="cp /users/emra500/scratch/Dataset/noncs/noncs/"+files.get(i)+".fasta ./ \n";
			Testing+="cp /users/emra500/scratch/Dataset/noncs/noncs/"+files.get(i)+".pdb ./ \n";
		}
		new Preparer().WriteTxtFile("Mini.txt",Mini);
		new Preparer().WriteTxtFile("Training.txt",Training);
		new Preparer().WriteTxtFile("Testing.txt",Testing);
	}

}
