package Comparison.Analyser;

import static org.junit.Assume.assumeNoException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;

import org.apache.commons.io.FileUtils;

import Comparison.Runner.RunComparison;

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
    
    
    new ResolutionUpdater().Update(ExcelPath);
    
    System.out.println("Splitting orginal and synthetic .... ");
    new OrginalAndSyntheticSplitter().Split(ExcelPath);
    System.out.println("Excluding failed datasets from orginal category  .... ");
	new FailedDatasetsExcluder().Exclude(ExcelPath+"/OrginalBuccEx54");
	System.out.println("Excluding failed datasets from synthetic category  .... ");
	new FailedDatasetsExcluder().Exclude(ExcelPath+"/SyntheticBuccEx54");
	
	System.out.println("Creating the comparsion tables  .... ");
		ComparisonMeasures r = new ComparisonMeasures();
		
		r.PathToLatexFolder=ExcelPath+"/OrginalBuccEx54ExFaliedCaseslatex";
		new RunComparison().CheckDirAndFile(r.PathToLatexFolder);
        r.OverallResults(ExcelPath+"/Orginal",true);
        
	r.MatrixOfResults(ExcelPath+"/OrginalBuccEx54ExFaliedCases");
	r.TimeTakingTable(ExcelPath+"/OrginalBuccEx54ExFaliedCases");
	
		r.PathToLatexFolder=ExcelPath+"/SyntheticBuccEx54ExFaliedCaseslatex";
		new RunComparison().CheckDirAndFile(r.PathToLatexFolder);
		
        r.OverallResults(ExcelPath+"/Synthetic",true);
        r.MatrixOfResults(ExcelPath+"/SyntheticBuccEx54ExFaliedCases");
    	r.TimeTakingTable(ExcelPath+"/SyntheticBuccEx54ExFaliedCases");
    	
    	
    	new RunComparison().CheckDirAndFile(ExcelPath+"/All");
    	for(int i=0; i < DM.size() ; ++i) {
    		new RunComparison().CheckDirAndFile(ExcelPath+"/All/"+DM.get(i).getName());
     		FileUtils.copyDirectory(DM.get(i), new File(ExcelPath+"/All/"+DM.get(i).getName()));

    	}
    	
    	new FailedDatasetsExcluder().Exclude(ExcelPath+"/All");
    	new Excluding54Datasets().Exculde(ExcelPath+"/AllExFaliedCases", true);
    	
    	new BinsCreater().Bining(ExcelPath+"/AllExFaliedCasesExcludedBuccaneerDevSet");
    	
    	new FailedDatasetsExcluder().Exclude(ExcelPath+"/OrginalBuccInc54");
		
		
		r.PathToLatexFolder=ExcelPath+"/OrginalBuccInc54ExFaliedCaseslatex";
		new RunComparison().CheckDirAndFile(r.PathToLatexFolder);
        r.OverallResults(ExcelPath+"/OrginalBuccInc54",false);
        
	    r.MatrixOfResults(ExcelPath+"/OrginalBuccInc54ExFaliedCases");
	    r.TimeTakingTable(ExcelPath+"/OrginalBuccInc54ExFaliedCases");
	
	}

}
