package Run;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;

import org.apache.commons.io.FilenameUtils;

import Analyser.DataContainer;
import Analyser.LoadExcel;

public class Cleaner {

	/*
	 * 
	 * When need to rerun the dataset on failed cases, we need to remove the temp folders and files and update the the 
	 * txt file which record the names of the files that ran. This class do this job!  
	 * 
	 */
	

	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		LoadExcel e = new LoadExcel();
		//Vector<DataContainer> Container = e.ReadExcel("/Volumes/PhDHardDrive/jcsg1200Results/Fasta/Run4/noncs/Crank.xlsx");
		
		int count=0;
		String Script="";
		String PDB="";
		
		Vector<DataContainer> Container = new Vector<DataContainer>();
		
		File[] PDBs = new File("/Volumes/PhDHardDrive/TempResults/BuccaneerResultsPdbNoncs/PDBs").listFiles();
		
		
			 for (File pdb : PDBs) {
				// System.out.println("Log "+Log.getName().replaceAll("."+FilenameUtils.getExtension(Log.getName()),""));
				// System.out.println("PDB "+PDB.getName().replaceAll("."+FilenameUtils.getExtension(PDB.getName()),""));
				 DataContainer id = new DataContainer();
				 id.PDB_ID=pdb.getName().replaceAll("."+FilenameUtils.getExtension(pdb.getName()),"");
				 id.BuiltPDB="T";
				 id.Intermediate="F";
				 Container.add(id);
				
			 }
			
			Vector<DataContainer> Container2 = e.ReadExcel("/Volumes/PhDHardDrive/jcsg1200Results/Fasta/Run4/noncs/ARPwARP.xlsx");
			for(int i=0; i < Container2.size();++i ) {
				boolean Foound=false;
				for(int c=0; c <Container.size() ; ++ c ) {
					if(Container.get(c).PDB_ID.equals(Container2.get(i).PDB_ID)) {
						Foound=true;
						break;
					}
				}
				if(Foound==false) {
					 DataContainer id = new DataContainer();
					 id.PDB_ID=Container2.get(i).PDB_ID;
					 id.BuiltPDB="F";
					 id.Intermediate="F";
					 Container.add(id);
				}
			}
	
		
		
		for(int i=0; i < Container.size();++i ) {
		
			
			
			//if(Container.get(i).BuiltPDB.equals("F") || (Container.get(i).Intermediate.equals("T") && Math.round(Double.valueOf(Container.get(i).TimeTaking)/60) < 48 )) {
				if(Container.get(i).BuiltPDB.equals("F") || Container.get(i).Intermediate.equals("T")) {

					//System.out.println(Container.get(i).PDB_ID);
				Script+="rm -r "+Container.get(i).PDB_ID+" \n";
				Script+="rm -r project"+Container.get(i).PDB_ID+" \n";
				Script+="rm BuccaneerResults/PDBs/"+Container.get(i).PDB_ID+".pdb \n";
				Script+="rm BuccaneerResults/BuccaneerLogs/"+Container.get(i).PDB_ID+".txt \n";
				Script+="rm BuccaneerResults/IntermediatePDBs/"+Container.get(i).PDB_ID+".pdb \n";
				Script+="rm BuccaneerResults/IntermediateLogs/"+Container.get(i).PDB_ID+".txt \n";
				
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
		new Preparer().WriteTxtFile("ProcessedFilesNamesBuccaneer.txt",PDB);
		
		/*
		 * Vector<DataContainer> Container2 = e.ReadExcel("/Volumes/PhDHardDrive/jcsg1200Results/Fasta/Run4/noncs/ARPwARP.xlsx");
		String CopyScript="";
		CopyScript+="mkdir ./noncs \n";
		CopyScript+="mkdir ./noncs/noncs \n";
		CopyScript+="mkdir ./noncs/noncsChltofom \n";
		CopyScript+="mkdir ./noncs/noncsFakeWave \n";
		CopyScript+="mkdir ./noncs/noncsFakeWaveFakeWaveFakeAnom \n";
		CopyScript+="mkdir ./mrncs \n";
		CopyScript+="mkdir ./mrncs/mrncs \n";
		CopyScript+="mkdir ./mrncs/mrncsChltofom \n";
		CopyScript+="mkdir ./mrncs/mrncsFakeWave \n";
		CopyScript+="mkdir ./mrncs/mrncsFakeWaveFakeWaveFakeAnom \n";
		CopyScript+="mkdir ./hancs \n";
		CopyScript+="mkdir ./hancs/hancs \n";
		CopyScript+="mkdir ./hancs/hancsChltofom \n";
		CopyScript+="mkdir ./hancs/hancsFakeWave \n";
		CopyScript+="mkdir ./hancs/hancsFakeWaveFakeWaveFakeAnom \n";
		Vector <Integer> IndeicsWereAppered= new Vector <Integer>();
			for(int i=0 ; i < 50 ; ++i) {
			
				Random a  = new Random();
				int RandomIndex=a.nextInt(Container2.size()-1);
				while(IndeicsWereAppered.contains(RandomIndex))
					RandomIndex=a.nextInt(Container2.size()-1);
				IndeicsWereAppered.add(RandomIndex);
				CopyScript+="cp ../PipelinesFasta/noncs/noncs/"+Container2.get(RandomIndex).PDB_ID+".mtz "+" ./noncs/noncs \n";
				CopyScript+="cp ../PipelinesFasta/noncs/noncs/"+Container2.get(RandomIndex).PDB_ID+".fasta "+" ./noncs/noncs \n";
				CopyScript+="cp ../PipelinesFasta/noncs/noncs/"+Container2.get(RandomIndex).PDB_ID+".pdb "+" ./noncs/noncs \n";
				
				CopyScript+="cp ../PipelinesFasta/noncs/noncsChltofom/"+Container2.get(RandomIndex).PDB_ID+".mtz "+" ./noncs/noncsChltofom \n";
				CopyScript+="cp ../PipelinesFasta/noncs/noncsChltofom/"+Container2.get(RandomIndex).PDB_ID+".fasta "+" ./noncs/noncsChltofom \n";
				CopyScript+="cp ../PipelinesFasta/noncs/noncsChltofom/"+Container2.get(RandomIndex).PDB_ID+".pdb "+" ./noncs/noncsChltofom \n";
				
				CopyScript+="cp ../PipelinesFasta/noncs/noncsFakeWave/"+Container2.get(RandomIndex).PDB_ID+".mtz "+" ./noncs/noncsFakeWave \n";
				CopyScript+="cp ../PipelinesFasta/noncs/noncsFakeWave/"+Container2.get(RandomIndex).PDB_ID+".fasta "+" ./noncs/noncsFakeWave \n";
				CopyScript+="cp ../PipelinesFasta/noncs/noncsFakeWave/"+Container2.get(RandomIndex).PDB_ID+".pdb "+" ./noncs/noncsFakeWave \n";
				
				CopyScript+="cp ../PipelinesFasta/noncs/noncsFakeWaveFakeWaveFakeAnom/"+Container2.get(RandomIndex).PDB_ID+".mtz "+" ./noncs/noncsFakeWaveFakeWaveFakeAnom \n";
				CopyScript+="cp ../PipelinesFasta/noncs/noncsFakeWaveFakeWaveFakeAnom/"+Container2.get(RandomIndex).PDB_ID+".fasta "+" ./noncs/noncsFakeWaveFakeWaveFakeAnom \n";
				CopyScript+="cp ../PipelinesFasta/noncs/noncsFakeWaveFakeWaveFakeAnom/"+Container2.get(RandomIndex).PDB_ID+".pdb "+" ./noncs/noncsFakeWaveFakeWaveFakeAnom \n";
			
			
			
				CopyScript+="cp ../PipelinesFasta/hancs/hancs/"+Container2.get(RandomIndex).PDB_ID.replaceAll("noncs", "hancs")+".mtz "+" ./hancs/hancs \n";
				CopyScript+="cp ../PipelinesFasta/hancs/hancs/"+Container2.get(RandomIndex).PDB_ID.replaceAll("noncs", "hancs")+".fasta "+" ./hancs/hancs \n";
				CopyScript+="cp ../PipelinesFasta/hancs/hancs/"+Container2.get(RandomIndex).PDB_ID.replaceAll("noncs", "hancs")+".pdb "+" ./hancs/hancs \n";
				
				CopyScript+="cp ../PipelinesFasta/hancs/hancsChltofom/"+Container2.get(RandomIndex).PDB_ID.replaceAll("noncs", "hancs")+".mtz "+" ./hancs/hancsChltofom \n";
				CopyScript+="cp ../PipelinesFasta/hancs/hancsChltofom/"+Container2.get(RandomIndex).PDB_ID.replaceAll("noncs", "hancs")+".fasta "+" ./hancs/hancsChltofom \n";
				CopyScript+="cp ../PipelinesFasta/hancs/hancsChltofom/"+Container2.get(RandomIndex).PDB_ID.replaceAll("noncs", "hancs")+".pdb "+" ./hancs/hancsChltofom \n";
				
				CopyScript+="cp ../PipelinesFasta/hancs/hancsFakeWave/"+Container2.get(RandomIndex).PDB_ID.replaceAll("noncs", "hancs")+".mtz "+" ./hancs/hancsFakeWave \n";
				CopyScript+="cp ../PipelinesFasta/hancs/hancsFakeWave/"+Container2.get(RandomIndex).PDB_ID.replaceAll("noncs", "hancs")+".fasta "+" ./hancs/hancsFakeWave \n";
				CopyScript+="cp ../PipelinesFasta/hancs/hancsFakeWave/"+Container2.get(RandomIndex).PDB_ID.replaceAll("noncs", "hancs")+".pdb "+" ./hancs/hancsFakeWave \n";
				
				CopyScript+="cp ../PipelinesFasta/hancs/hancsFakeWaveFakeWaveFakeAnom/"+Container2.get(RandomIndex).PDB_ID.replaceAll("noncs", "hancs")+".mtz "+" ./hancs/hancsFakeWaveFakeWaveFakeAnom \n";
				CopyScript+="cp ../PipelinesFasta/hancs/hancsFakeWaveFakeWaveFakeAnom/"+Container2.get(RandomIndex).PDB_ID.replaceAll("noncs", "hancs")+".fasta "+" ./hancs/hancsFakeWaveFakeWaveFakeAnom \n";
				CopyScript+="cp ../PipelinesFasta/hancs/hancsFakeWaveFakeWaveFakeAnom/"+Container2.get(RandomIndex).PDB_ID.replaceAll("noncs", "hancs")+".pdb "+" ./hancs/hancsFakeWaveFakeWaveFakeAnom \n";
			
			
			
			
				
				
				CopyScript+="cp ../PipelinesFasta/mrncs/mrncsWithMissingCasesFasta/"+Container2.get(RandomIndex).PDB_ID.replaceAll("noncs", "mrncs")+".mtz "+" ./mrncs/mrncs \n";
				CopyScript+="cp ../PipelinesFasta/mrncs/mrncsWithMissingCasesFasta/"+Container2.get(RandomIndex).PDB_ID.replaceAll("noncs", "mrncs")+".fasta "+" ./mrncs/mrncs \n";
				CopyScript+="cp ../PipelinesFasta/mrncs/mrncsWithMissingCasesFasta/"+Container2.get(RandomIndex).PDB_ID.replaceAll("noncs", "mrncs")+".pdb "+" ./mrncs/mrncs \n";
				
				CopyScript+="cp ../PipelinesFasta/mrncs/mrncsWithMissingCasesFastaChltofom/"+Container2.get(RandomIndex).PDB_ID.replaceAll("noncs", "mrncs")+".mtz "+" ./mrncs/mrncsChltofom \n";
				CopyScript+="cp ../PipelinesFasta/mrncs/mrncsWithMissingCasesFastaChltofom/"+Container2.get(RandomIndex).PDB_ID.replaceAll("noncs", "mrncs")+".fasta "+" ./mrncs/mrncsChltofom \n";
				CopyScript+="cp ../PipelinesFasta/mrncs/mrncsWithMissingCasesFastaChltofom/"+Container2.get(RandomIndex).PDB_ID.replaceAll("noncs", "mrncs")+".pdb "+" ./mrncs/mrncsChltofom \n";
				
				CopyScript+="cp ../PipelinesFasta/mrncs/mrncsWithMissingCasesFastaFakeWave/"+Container2.get(RandomIndex).PDB_ID.replaceAll("noncs", "mrncs")+".mtz "+" ./mrncs/mrncsFakeWave \n";
				CopyScript+="cp ../PipelinesFasta/mrncs/mrncsWithMissingCasesFastaFakeWave/"+Container2.get(RandomIndex).PDB_ID.replaceAll("noncs", "mrncs")+".fasta "+" ./mrncs/mrncsFakeWave \n";
				CopyScript+="cp ../PipelinesFasta/mrncs/mrncsWithMissingCasesFastaFakeWave/"+Container2.get(RandomIndex).PDB_ID.replaceAll("noncs", "mrncs")+".pdb "+" ./mrncs/mrncsFakeWave \n";
				
				CopyScript+="cp ../PipelinesFasta/mrncs/mrncsWithMissingCasesFastaFakeWaveFakeWaveFakeAnom/"+Container2.get(RandomIndex).PDB_ID.replaceAll("noncs", "mrncs")+".mtz "+" ./mrncs/mrncsFakeWaveFakeWaveFakeAnom \n";
				CopyScript+="cp ../PipelinesFasta/mrncs/mrncsWithMissingCasesFastaFakeWaveFakeWaveFakeAnom/"+Container2.get(RandomIndex).PDB_ID.replaceAll("noncs", "mrncs")+".fasta "+" ./mrncs/mrncsFakeWaveFakeWaveFakeAnom \n";
				CopyScript+="cp ../PipelinesFasta/mrncs/mrncsWithMissingCasesFastaFakeWaveFakeWaveFakeAnom/"+Container2.get(RandomIndex).PDB_ID.replaceAll("noncs", "mrncs")+".pdb "+" ./mrncs/mrncsFakeWaveFakeWaveFakeAnom \n";
			
			
			
			
			}
			
			new Preparer().WriteTxtFile("CopyScript.sh",CopyScript);
		 */
		
	}

}
