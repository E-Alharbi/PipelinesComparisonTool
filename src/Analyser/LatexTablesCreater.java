package Analyser;

import static org.junit.Assume.assumeNoException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

import org.apache.commons.io.FileUtils;

import Run.RunComparison;

public class LatexTablesCreater {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub

    String ExcelPath=args[0]; 
	
    Vector<File> DM= new Vector<File>();
    for(File F : new File(ExcelPath).listFiles())
    {
    	if(F.isDirectory())
    		DM.add(F);
    }
    
    
    new ResoUpdater().Update(ExcelPath);
    
    new OrginalAndSyntheticSplitter().Split(ExcelPath);
	new FailedCasesExcluder().Exclude(ExcelPath+"/OrginalBuccEx54");
	new FailedCasesExcluder().Exclude(ExcelPath+"/SyntheticBuccEx54");
	
	
		ResultsInLatex r = new ResultsInLatex();
		
		r.PathToLatexFolder=ExcelPath+"/OrginalBuccEx54ExFaliedCaseslatex";
		new RunComparison().CheckDirAndFile(r.PathToLatexFolder);
        r.OverallResults(ExcelPath+"/Orginal");
        
	r.MatrixOfResults(ExcelPath+"/OrginalBuccEx54ExFaliedCases");
	r.TimeTakingTable(ExcelPath+"/OrginalBuccEx54ExFaliedCases");
	
		r.PathToLatexFolder=ExcelPath+"/SyntheticBuccEx54ExFaliedCaseslatex";
		new RunComparison().CheckDirAndFile(r.PathToLatexFolder);
		
        r.OverallResults(ExcelPath+"/Synthetic");
        r.MatrixOfResults(ExcelPath+"/SyntheticBuccEx54ExFaliedCases");
    	r.TimeTakingTable(ExcelPath+"/SyntheticBuccEx54ExFaliedCases");
    	
    	
    	new RunComparison().CheckDirAndFile(ExcelPath+"/All");
    	for(int i=0; i < DM.size() ; ++i) {
    		new RunComparison().CheckDirAndFile(ExcelPath+"/All/"+DM.get(i).getName());
     		FileUtils.copyDirectory(DM.get(i), new File(ExcelPath+"/All/"+DM.get(i).getName()));

    	}
    	
    	new FailedCasesExcluder().Exclude(ExcelPath+"/All");
    	new Exculding54Dataset().Exculde(ExcelPath+"/AllExFaliedCases", true);
    	
    	new BinsCreater().Bining(ExcelPath+"/AllExFaliedCasesExcludedBuccaneerDevSet");
	
	}

}
