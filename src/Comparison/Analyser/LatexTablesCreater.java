package Comparison.Analyser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Vector;

import org.apache.commons.io.FileUtils;

import Comparison.Runner.RunComparison;

public class LatexTablesCreater {

	public static void main(String[] args) throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub

    String ExcelPath=args[0]; 
   
   
   Vector<String> ShelxeExcelNamesInVec=new LatexTablesCreater().ReadInVec(args[1]);
	
	 Vector<String> ExcludeFromOrginal=new LatexTablesCreater().ReadInVec(args[3]);
	 Vector<String> ExcludeFromSynthetic=new LatexTablesCreater().ReadInVec(args[4]);
	 Vector<String> RfreeToZero=new LatexTablesCreater().ReadInVec(args[5]);
	 
	
	
    Vector<File> DM= new Vector<File>();
    for(File F : new File(ExcelPath).listFiles())
    {
    	if(F.isDirectory())
    		DM.add(F);
    }
    for (File dm : DM) {
    	for(File excel : dm.listFiles()) {
    		if(excel.getName().contains(".DS_Store")) {
    			System.out.println("You have .DS_Store files in "+excel.getAbsolutePath());
    			System.out.println("Remove them!");
    			System.exit(-1);
    		}
    	}
    }
    
    
    
    System.out.println("Checking if SHELXE was used ... ");
   
    for (File dm : DM) {
    	for(File excel : dm.listFiles()) {
    		if(excel.getName().toLowerCase().contains("shelxe")) {
    			System.out.print("Warning: this excel seems to have SHELXE results: "+excel.getAbsolutePath());	
    			
    			if(ShelxeExcelNamesInVec.contains(excel.getName().substring(0,excel.getName().indexOf(".")))) {
    				new CAInCorrectPosition().UpdateCom(excel);
    				System.out.println(". Completeness is updated ");
    			}
else {
if(new CAInCorrectPosition().IsCalculatedFromSeq(new ExcelLoader().ReadExcel(excel.getAbsolutePath())) ==true) {
	System.out.println("\n .Completeness calculated from the number of sequenced residues. If you want to change it to calculate from number Ca in correct position, use UpdateCom=ExcelFileName1,ExcelFileName2 (use a comma to split excel files names ). If you are sure is not calculated the number of sequenced residues, then ignore this warning  ");	

}
else {
	System.out.println("\n .Completeness calculated from the number of Ca in the correct position. If you are sure is not calculated from Ca in the correct position, then ignore this warning");
}
    		}
    		}
    	}
    }
    
    if(args[2]!=null) {
    	System.out.println(" Completing missing data ...");
    	if(args[2].equals("Auto")) {
    		for (File dm : DM) {
    			int max=0;
    			File MaxExcel=null;
    			for(File excel : dm.listFiles()) {
    				ExcelLoader f = new ExcelLoader();
    				Vector<ExcelContents> Pipeline1Excel = f.ReadExcel(excel.getAbsolutePath());
        			if(Pipeline1Excel.size() > max) {
        				max=Pipeline1Excel.size();
        				MaxExcel=excel;
        			}
        		}	
    			System.out.println("We will use this excel "+MaxExcel.getAbsolutePath()+" to fill in the missing data");
    			for(File excel : dm.listFiles()) {
    			    if(!MaxExcel.getAbsoluteFile().equals(excel.getAbsoluteFile()))
        			new CompleteExcelWithDummy().FillInWithDummy(MaxExcel,excel);
        		}
    		}
    	}
    	else {
    	for (File dm : DM) {
    		for(File excel : dm.listFiles()) {
    			if(!new File(args[2]).getAbsoluteFile().equals(excel.getAbsoluteFile()))
    			new CompleteExcelWithDummy().FillInWithDummy(new File(args[2]),excel);
    		}
    	}
    	}
    }
    System.out.println("Checking correctness of excel files ... ");
    for (File dm : DM) {
    	Vector<Integer> NumberOfDatasets= new Vector<Integer>();
    	for(File excel : dm.listFiles()) {
    		ExcelLoader f = new ExcelLoader();
    		Vector<ExcelContents> Pipeline1Excel = f.ReadExcel(excel.getAbsolutePath());
    		NumberOfDatasets.add(Pipeline1Excel.size());
    	}
    	int max=0;
    	int min=Integer.MAX_VALUE;
    	for(int a : NumberOfDatasets) {
    		if(max < a) max=a;
    		if(a < min) min=a;
    	}
    	if(max!=min) {
    		System.out.println("Excel files are incomplete and can not be analysed. If you want to fill in incomplete excel files, use FillInMissingData= path to one of the complete excel or set to FillInMissingData=Auto to predict which excel should be used. This will fill in the missing data by dummy data and mark them not built. ");
    		System.exit(-1);
    	}
    }
    for (File dm : DM) {
    	for(File excel : dm.listFiles()) {
    		ExcelLoader f = new ExcelLoader();
    		Vector<ExcelContents> Pipeline1Excel = f.ReadExcel(excel.getAbsolutePath());
    		for(int i=0 ; i < Pipeline1Excel.size();++i) {
    			if(Pipeline1Excel.get(i).BuiltPDB.equals("T") ) {
    				if(Pipeline1Excel.get(i).Completeness.equals("None")){
    					System.out.println("Excel "+excel.getAbsolutePath());
    					System.out.println("PDB "+Pipeline1Excel.get(i).PDB_ID);
    					System.out.println("Completeness is none and Built flag is true!");
    					System.exit(-1);
    				}
if(Pipeline1Excel.get(i).R_factor0Cycle.equals("None")){
	System.out.println("Excel "+excel.getAbsolutePath());
	System.out.println("PDB "+Pipeline1Excel.get(i).PDB_ID);
	System.out.println("R-work 0 cycle is none and Built flag is true!");
	System.exit(-1);	
    				}
if(Pipeline1Excel.get(i).R_free0Cycle.equals("None")){
	System.out.println("Excel "+excel.getAbsolutePath());
	System.out.println("PDB "+Pipeline1Excel.get(i).PDB_ID);
	System.out.println("R-free 0 cycle is none and Built flag is true!");
	System.exit(-1);
}
    			}
    			   
    		}
    	}
    }
    
    System.out.println("Set R-free to zero for "+RfreeToZero.toString() );
    for (File dm : DM) {
    	for(File excel : dm.listFiles()) {
    		if(RfreeToZero.contains(excel.getName().substring(0,excel.getName().indexOf(".")))) {
    			new LatexTablesCreater().FillRfreeWithZero(excel.getAbsolutePath());
    		}
    	}
    }
    
    
    System.out.println("Checking if R-free is used ... ");
    for (File dm : DM) {
    	for(File excel : dm.listFiles()) {
    		
    		if(new LatexTablesCreater().CheckingIfRfreeIsUsed(new ExcelLoader().ReadExcel(excel.getAbsolutePath()))==false) {
    			 System.out.println("Warning: R-free in this excel seems to be meaningless. If the tool did not use R-free, then fill R-free column with zero "+ excel.getAbsolutePath()+". Ignore this warning if you sure R-free is correct");
    		}
    		
    	}
    }
    System.out.println("Checking the format of PDB name ... ");
    for (File dm : DM) {
    	for(File excel : dm.listFiles()) {
    		
    		ExcelLoader f = new ExcelLoader();
    		Vector<ExcelContents> Pipeline1Excel = f.ReadExcel(excel.getAbsolutePath());
    		for(int i=0 ; i < Pipeline1Excel.size();++i) {
    			if(Pipeline1Excel.get(i).PDB_ID.length()<7 ) {
    				
					System.out.println("Error: PDB name in the wrong format. PDB name should be PDB ID-Resolution. For example, 1o6a-1.9. This might cause in a wrong removing when you have two PDBs with the same ID and different resolutions. For instance, original PDB and a simulated with lower resolution");
				System.out.println("Excel: "+excel.getAbsolutePath());
				System.out.println("PDB: "+Pipeline1Excel.get(i).PDB_ID);
				System.exit(-1);
    			}
    		}
    		
    	}
    }
    
    
    new ResolutionUpdater().Update(ExcelPath);
    
    System.out.println("Splitting orginal and synthetic .... ");
    new OrginalAndSyntheticSplitter().Split(ExcelPath);
   
    new LatexTablesCreater().RemoveFromFolder(ExcludeFromOrginal,new File(ExcelPath+"/OrginalBuccEx54"));
    new LatexTablesCreater().RemoveFromFolder(ExcludeFromSynthetic,new File(ExcelPath+"/SyntheticBuccEx54"));
    
    new LatexTablesCreater().RemoveFromFolder(ExcludeFromOrginal,new File(ExcelPath+"/Orginal"));
    new LatexTablesCreater().RemoveFromFolder(ExcludeFromSynthetic,new File(ExcelPath+"/Synthetic"));
    
    System.out.println("Excluding failed datasets from orginal category  .... ");
	new FailedDatasetsExcluder().Exclude(ExcelPath+"/OrginalBuccEx54");
	new FailedDatasetsExcluder().Exclude(ExcelPath+"/Orginal");
	System.out.println("Excluding failed datasets from synthetic category  .... ");
	new FailedDatasetsExcluder().Exclude(ExcelPath+"/SyntheticBuccEx54");
	new FailedDatasetsExcluder().Exclude(ExcelPath+"/Synthetic");
	
	
	
	
	System.out.println("Creating the comparsion tables  .... ");
	ComparisonMeasures r = new ComparisonMeasures();
		
	r.PathToLatexFolder=ExcelPath+"/OrginalBuccEx54ExFaliedCaseslatex";
    new RunComparison().CheckDirAndFile(r.PathToLatexFolder);
    r.OverallResults(ExcelPath+"/Orginal",true);
        
	r.MatrixOfResults(ExcelPath+"/OrginalBuccEx54ExFaliedCases");
	//r.TimeTakingTable(ExcelPath+"/OrginalBuccEx54ExFaliedCases");
	r.ImprovementsLevel(ExcelPath+"/OrginalBuccEx54ExFaliedCases");
	r.TableOfMeanAndSD(ExcelPath+"/OrginalBuccEx54ExFaliedCases");
	r.BestOfCombinedPipeline(ExcelPath+"/OrginalBuccEx54ExFaliedCases");
	r.BestOfAll(ExcelPath+"/OrginalBuccEx54ExFaliedCases");
	new Heatmap().HeatmapFromComparisonTable(ExcelPath+"/OrginalBuccEx54ExFaliedCaseslatex");
	
	
	r.PathToLatexFolder=ExcelPath+"/SyntheticBuccEx54ExFaliedCaseslatex";
	
		new RunComparison().CheckDirAndFile(r.PathToLatexFolder);
		
		
        r.OverallResults(ExcelPath+"/Synthetic",true);
        r.MatrixOfResults(ExcelPath+"/SyntheticBuccEx54ExFaliedCases");
    	//r.TimeTakingTable(ExcelPath+"/SyntheticBuccEx54ExFaliedCases");
    	r.ImprovementsLevel(ExcelPath+"/SyntheticBuccEx54ExFaliedCases");
    	r.TableOfMeanAndSD(ExcelPath+"/SyntheticBuccEx54ExFaliedCases");
    	r.BestOfCombinedPipeline(ExcelPath+"/SyntheticBuccEx54ExFaliedCases");
    	r.BestOfAll(ExcelPath+"/SyntheticBuccEx54ExFaliedCases");
    	new Heatmap().HeatmapFromComparisonTable(ExcelPath+"/SyntheticBuccEx54ExFaliedCaseslatex");
    	
    	new RunComparison().CheckDirAndFile(ExcelPath+"/All");
    	for(int i=0; i < DM.size() ; ++i) {
    		new RunComparison().CheckDirAndFile(ExcelPath+"/All/"+DM.get(i).getName());
     		FileUtils.copyDirectory(DM.get(i), new File(ExcelPath+"/All/"+DM.get(i).getName()));

    	}
    	
    	System.out.println("Excluding from both orginal and synthetic to use in plots  "+ExcludeFromOrginal.toString()+" "+ExcludeFromSynthetic.toString());
    	 new LatexTablesCreater().RemoveFromFolder(ExcludeFromOrginal,new File(ExcelPath+"/All"));
    	 new LatexTablesCreater().RemoveFromFolder(ExcludeFromSynthetic,new File(ExcelPath+"/All"));

    	new FailedDatasetsExcluder().Exclude(ExcelPath+"/All");
    	new Excluding54Datasets().Exculde(ExcelPath+"/AllExFaliedCases", true);
    	
    	
    	
    	new BinsCreater().Bining(ExcelPath+"/AllExFaliedCasesExcludedBuccaneerDevSet");
    	
    	new BinsCreater().Bining(ExcelPath+"/OrginalBuccEx54ExFaliedCases");
    	new BinsCreater().Bining(ExcelPath+"/SyntheticBuccEx54ExFaliedCases");
    	
    	 new LatexTablesCreater().RemoveFromFolder(ExcludeFromOrginal,new File(ExcelPath+"/OrginalBuccInc54"));
    	new FailedDatasetsExcluder().Exclude(ExcelPath+"/OrginalBuccInc54");
		
    	
		r.PathToLatexFolder=ExcelPath+"/OrginalBuccInc54ExFaliedCaseslatex";
		new RunComparison().CheckDirAndFile(r.PathToLatexFolder);
        r.OverallResults(ExcelPath+"/OrginalBuccInc54",false);
        
	    r.MatrixOfResults(ExcelPath+"/OrginalBuccInc54ExFaliedCases");
	   // r.TimeTakingTable(ExcelPath+"/OrginalBuccInc54ExFaliedCases");
	    r.ImprovementsLevel(ExcelPath+"/OrginalBuccInc54ExFaliedCases");
	r.TableOfMeanAndSD(ExcelPath+"/OrginalBuccInc54ExFaliedCases");
	    r.BestOfCombinedPipeline(ExcelPath+"/OrginalBuccInc54ExFaliedCases");
	    new Heatmap().HeatmapFromComparisonTable(ExcelPath+"/OrginalBuccInc54ExFaliedCaseslatex");
	    
	    
	    
	    new LatexTablesCreater().RemoveFromFolder(ExcludeFromSynthetic,new File(ExcelPath+"/SyntheticBuccInc54"));
	    new FailedDatasetsExcluder().Exclude(ExcelPath+"/SyntheticBuccInc54");
	    r.PathToLatexFolder=ExcelPath+"/SyntheticBuccInc54ExFaliedCaseslatex";
		new RunComparison().CheckDirAndFile(r.PathToLatexFolder);
        r.OverallResults(ExcelPath+"/SyntheticBuccInc54",false);
        
	    r.MatrixOfResults(ExcelPath+"/SyntheticBuccInc54ExFaliedCases");
	   // r.TimeTakingTable(ExcelPath+"/OrginalBuccInc54ExFaliedCases");
	    r.ImprovementsLevel(ExcelPath+"/SyntheticBuccInc54ExFaliedCases");
	    r.TableOfMeanAndSD(ExcelPath+"/SyntheticBuccInc54ExFaliedCases");
	    r.BestOfCombinedPipeline(ExcelPath+"/SyntheticBuccInc54ExFaliedCases");
	    new Heatmap().HeatmapFromComparisonTable(ExcelPath+"/SyntheticBuccInc54ExFaliedCaseslatex");
	    
	    System.out.println("Use the fallowing csv to plot the results: ");
	    System.out.println("1- csv file in "+ExcelPath+"/AllExFaliedCasesExcludedBuccaneerDevSet"+" for completness vs resolution, R-work vs resolution and R-free vs resolution ");
	    System.out.println("2- csv file in "+ExcelPath+"/OrginalBuccEx54ExFaliedCases"+" for completness vs F-map Correlation ");

	}

	Vector<String> ReadInVec(String arg){
		Vector<String> Vec=new Vector<String>();
		if(arg!=null) {
			if(arg.contains(",")) {
			String [] ArgLine=arg.split(",");
			Vec = new Vector<String>(Arrays.asList(ArgLine));
			}
			else {
				Vec = new Vector<String>();
				Vec.add(arg);
			}
		}
		return Vec;
	}
	void RemoveFromFolder(Vector<String> Vec, File Folder) {
		
		for(File dm :  Folder.listFiles())
		for(File excel : dm.listFiles() ) {
			
			if(Vec.contains(excel.getName().substring(0,excel.getName().indexOf(".")))) {
				excel.delete();
			}
		}
	}
	
	boolean CheckingIfRfreeIsUsed(Vector<ExcelContents> PipelineExcel) {
		double CountWhenRworkEqRfree=0;
		double CountWhenRworkHigherthanRfree=0;
		for(int i=0; i < PipelineExcel.size();++i) {
			if(PipelineExcel.get(i).R_factor0Cycle.equals(PipelineExcel.get(i).R_free0Cycle) && !PipelineExcel.get(i).R_free.equals("0")) {
				CountWhenRworkEqRfree++;
			}
			if(!PipelineExcel.get(i).R_free0Cycle.equals("None") && !PipelineExcel.get(i).R_factor0Cycle.equals("None"))
			if( new BigDecimal(PipelineExcel.get(i).R_free0Cycle).compareTo(new BigDecimal(PipelineExcel.get(i).R_factor0Cycle)) < 0  &&!PipelineExcel.get(i).R_free.equals("0")) {

				
				CountWhenRworkHigherthanRfree++;
				

			}
		}
		
		if((CountWhenRworkEqRfree*100)/PipelineExcel.size() > 50) return false;
		if((CountWhenRworkHigherthanRfree*100)/PipelineExcel.size() > 50) return false;
		
		return true;
		
	}
	void FillRfreeWithZero(String ExcelPath) throws FileNotFoundException, IOException {
		
		Vector<ExcelContents> PipelineExcel=new ExcelLoader().ReadExcel(ExcelPath);
		for(int i=0; i < PipelineExcel.size();++i) {
			PipelineExcel.get(i).R_free="0";
		}
		new ExcelSheet().FillInExcel(PipelineExcel, ExcelPath);
	}
}
