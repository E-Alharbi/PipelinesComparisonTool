package Comparison.Analyser;
/**
*
* @author Emad Alharbi
* University of York
*/
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
//import  org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import Comparison.Runner.Preparer;
import Comparison.Runner.RunComparison;
import Comparison.Runner.RunningParameter;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

public class ComparisonMeasures {

	// Creating many types of comparison tables in latex format 

String PathToLatexFolder="./Latex";
boolean AvgInMatrices=false;

public static void main(String[] args) throws FileNotFoundException, IOException {
	//ComparisonMeasures r = new ComparisonMeasures();
	//r.PathToLatexFolder="/Users/emadalharbi/Desktop/Test2/Latex";
	//r.MatrixOfResults("/Users/emadalharbi/Desktop/Test2/OrginalBuccEx54ExFaliedCases copy");
	//r.TableOfMeanAndSD("/Users/emadalharbi/Desktop/Test2/OrginalBuccEx54ExFaliedCases copy");
	//r.OverallResults("/Users/emadalharbi/Desktop/Test2/OrginalBuccEx54ExFaliedCases copy",true);
	//Reproducibility
	//ComparisonMeasures cm = new ComparisonMeasures();
	//cm.PathToLatexFolder="";
	//cm.CompRTimeAvgTable("","");
}
	void CompRTimeAvgTable(String ExcelDir, String PathForOriginalExp) throws IOException {//Reproducibility tables. Run this method twice for original and synthetic   
		
		for (File Folder : new File(ExcelDir).listFiles()) {
			if(Folder.isDirectory()) {
				String Table="\\tiny Pipeline variant & \\tiny Completeness &\\tiny R-work/R-free & \\tiny Execution time \\\\ \\hline \n";
				String TableOriginal="\\tiny Pipeline variant & \\tiny Completeness &\\tiny R-work/R-free & \\tiny Execution time \\\\ \\hline \n";

				String Comments="";
				Comments+="% "+ new Date().toString() +" \n ";
				Comments+="% Folder: "+ Folder.getName() +" \n ";
				Comments+="% Full folder path :  "+ Folder.getAbsolutePath() +" \n ";
				Comments+="% Excel used  :  "+ " \n ";
				
				
				for(File Excel : Folder.listFiles()) {
					if(Excel.isFile()) {
						Comments+="%"+Excel.getAbsolutePath() +" \n ";
						double Com=0;
						double Rwork=0;
						double Rfree=0;
						double Time=0;
						
						double ComOriginal=0;
						double RworkOriginal=0;
						double RfreeOriginal=0;
						double TimeOriginal=0;
						int countBuiltPDB=0;
						ExcelLoader f = new ExcelLoader();
						Vector<ExcelContents> Container = f.ReadExcel(Excel.getAbsolutePath());
						for(int i=0; i < Container.size() ; ++i) {
							if(Container.get(i).BuiltPDB.equals("T")) {
								countBuiltPDB++;
							Com+=Double.parseDouble(Container.get(i).Completeness);
							Rwork+=Double.parseDouble(Container.get(i).R_factor0Cycle);
							Rfree+=Double.parseDouble(Container.get(i).R_free0Cycle);
							Time+=Double.parseDouble(Container.get(i).TimeTaking);
							for (File FolderOriginalExp : new File(PathForOriginalExp).listFiles()) {
								if(FolderOriginalExp.getName().equals(Folder.getName()))
									for(File OriginalExcel : FolderOriginalExp.listFiles()) {
										if(Excel.getName().equals(OriginalExcel.getName())) {
											Vector<ExcelContents> OriginalContainer = f.ReadExcel(OriginalExcel.getAbsolutePath());
											for(int o=0; o < OriginalContainer.size() ; ++o) {
												if(Container.get(i).PDB_ID.equals(OriginalContainer.get(o).PDB_ID)) {
													ComOriginal+=Double.parseDouble(OriginalContainer.get(o).Completeness);
													RworkOriginal+=Double.parseDouble(OriginalContainer.get(o).R_factor0Cycle);
													RfreeOriginal+=Double.parseDouble(OriginalContainer.get(o).R_free0Cycle);
													TimeOriginal+=Double.parseDouble(OriginalContainer.get(o).TimeTaking);
													break;// no need to complete the loop
												}
												
											}
										}
									}
							}
						}
						}
						DecimalFormat df = new DecimalFormat("#.##");
						df.setRoundingMode(RoundingMode.HALF_UP);
						
						
						Table+="\\tiny "+Excel.getName()+" & \\tiny "+Math.round((Com/countBuiltPDB)) +" & \\tiny "+ df.format(BigDecimal.valueOf(Double.valueOf((Rwork/countBuiltPDB))))+"/"+df.format(BigDecimal.valueOf(Double.valueOf((Rfree/countBuiltPDB))))+" & \\tiny "+Math.round((Time/countBuiltPDB)) +"\\\\ \\hline \n";
						TableOriginal+="\\tiny "+Excel.getName()+" & \\tiny "+Math.round((ComOriginal/countBuiltPDB)) +" & \\tiny "+ df.format(BigDecimal.valueOf(Double.valueOf((RworkOriginal/countBuiltPDB))))+"/"+df.format(BigDecimal.valueOf(Double.valueOf((RfreeOriginal/countBuiltPDB))))+" & \\tiny "+Math.round((TimeOriginal/countBuiltPDB)) +"\\\\ \\hline \n";


						
					
					
					}
				}
				new Preparer().WriteTxtFile(PathToLatexFolder+"/ReproducibilityTable" + Folder.getName() + ".tex", FormatingPipelinesNames(Table,true,true) + "\n"+Comments);
				new Preparer().WriteTxtFile(PathToLatexFolder+"/ReproducibilityTableOriginal" + Folder.getName() + ".tex", FormatingPipelinesNames(TableOriginal,true,true) + "\n"+Comments);

			}
		}
	}
void TimeTakingTable(String ExcelDir) throws IOException {
	
	for (File Folder : new File(ExcelDir).listFiles()) {
		if(Folder.isDirectory()) {
			String Table="\\tiny Pipeline variant & \\tiny Min &\\tiny Max & \\tiny Mean \\\\ \\hline \n";
			String Comments="";
			Comments+="% "+ new Date().toString() +" \n ";
			Comments+="% Folder: "+ Folder.getName() +" \n ";
			Comments+="% Full folder path :  "+ Folder.getAbsolutePath() +" \n ";
			Comments+="% Excel used  :  "+ " \n ";
			File [] ThisFolder=Folder.listFiles();
			Arrays.sort(ThisFolder, (f1, f2) -> f1.compareTo(f2));
			for(File Excel : ThisFolder) {
				if(Excel.isFile()) {
					Comments+="%"+Excel.getAbsolutePath() +" \n ";
					ExcelLoader f = new ExcelLoader();
					Vector<ExcelContents> Container = f.ReadExcel(Excel.getAbsolutePath());
					double min=Double.parseDouble(Container.get(0).TimeTaking);
					double max=Double.parseDouble(Container.get(0).TimeTaking);
					double total=0;
					for(int i=0; i < Container.size() ; ++i) {
						if(Double.parseDouble(Container.get(i).TimeTaking) > max)
							max=Double.parseDouble(Container.get(i).TimeTaking);
						if(Double.parseDouble(Container.get(i).TimeTaking) < min)
							min=Double.parseDouble(Container.get(i).TimeTaking);
						total+=Double.parseDouble(Container.get(i).TimeTaking);
					}
					Table+=" \\tiny "+Excel.getName()+" & \\tiny "+Math.round(min)+" & \\tiny "+Math.round(max)+" & \\tiny "+ +Math.round((total/Container.size()))+" \\\\ \\hline \n";
				}
				
			}
			
			
			new Preparer().WriteTxtFile(PathToLatexFolder+"/TimeTakingTable" + Folder.getName() + ".tex", FormatingPipelinesNames(Table,true,true) + "\n"+Comments);
		}
	}
}
	void PDBTable(Vector<Vector<ExcelContents>> Container) throws IOException {
		// Sorting by Resolution
		Vector<String> PDB = new Vector<String>();
		for (int i = 0; i < Container.size(); ++i) {
			Collections.sort(Container.get(i), ExcelContents.DataContainerComparator);
		}

		new RunComparison().CheckDirAndFile("CSV");
		String CSV = "PDB,Resolution,FmapCorrelation,Simulated,EmapCorrelation\n";
		for (int i = 0; i < Container.get(0).size(); ++i) {
			String PDBID = Container.get(0).get(i).PDB_ID.contains("-")
					? Container.get(0).get(i).PDB_ID.substring(0, Container.get(0).get(i).PDB_ID.indexOf('-'))
					: Container.get(0).get(i).PDB_ID;
			String Simulated = "1";
			if (!PDB.contains(PDBID)) {
				Simulated = "2";
				CSV += PDBID + "," + Container.get(0).get(i).Resolution + "," + Container.get(0).get(i).F_mapCorrelation
						+ "," + Simulated + "," + Container.get(0).get(i).E_mapCorrelation + "\n";

			}
			PDB.add(PDBID);
		}
		new Preparer().WriteTxtFile("CSV/PDBTable.csv", CSV);
	}

	void OverallResults(String ResultsDir , boolean Excluded) throws IOException {
		File[] Folders = new File(ResultsDir).listFiles();
		Arrays.sort(Folders, (f1, f2) -> f1.compareTo(f2)); // sorting in this order hancs, mrncs and noncs
		ExcelLoader e = new ExcelLoader();
		Vector<Vector<ExcelContents>> Container = new Vector<Vector<ExcelContents>>();
		String Comments="% "+new Date().toString() +" \n";
		for (File Folder : Folders) {
			if (Folder.isDirectory()) {
				Comments+="% Folder "+Folder.getName() +" \n";
				Comments+="% Full folder path "+Folder.getAbsolutePath() +" \n";
				Comments+="% Excel used:  \n";
				File [] ThisFolder=Folder.listFiles();
				Arrays.sort(ThisFolder, (f1, f2) -> f1.compareTo(f2)); // sorting pipelines 
				for (File Excel : ThisFolder) {
					if(Excel.isFile()) { 
						Comments+="% "+Excel.getAbsolutePath()+"  \n";
					Container.add(e.ReadExcel(Excel.getAbsolutePath()));
					e.ToolsNames.add(Excel.getName() + Folder.getName());}
				}
			}

		}

		String CSV = "Pipeline,Completed,Intermediate,Failed,DM\n";
		Vector<OverallResult> Results = new Vector<OverallResult>();
		for (int i = 0; i < Container.size(); ++i) {
			int Completed = 0;
			int Intermediate = 0;
			int IntermediateTime = 0;
			int Failed = 0;

			for (int y = 0; y < Container.get(i).size(); ++y) {
				if (Container.get(i).get(y).BuiltPDB.equals("T") && Container.get(i).get(y).Intermediate.equals("F"))
					Completed++;
				if (Container.get(i).get(y).BuiltPDB.equals("T") && Container.get(i).get(y).Intermediate.equals("T")) {
					if (Math.round(Double.valueOf(Container.get(i).get(y).TimeTaking) / 60) >= 48)
						IntermediateTime++;
					else
						Intermediate++;
				}
				if (Container.get(i).get(y).BuiltPDB.equals("F") && Container.get(i).get(y).Intermediate.equals("F"))
					Failed++;
			}
			CSV += e.ToolsNames.get(i) + "," + Completed + "," + Intermediate + "," + Failed + ","
					+ e.ToolsNames.get(i).substring(e.ToolsNames.get(i).length() - 5, e.ToolsNames.get(i).length())
					+ "\n";
			Results.add(new OverallResult(e.ToolsNames.get(i).substring(0, e.ToolsNames.get(i).length() - 5),
					e.ToolsNames.get(i).substring(e.ToolsNames.get(i).length() - 5, e.ToolsNames.get(i).length()),
					Completed, Intermediate, Failed, IntermediateTime));
		}
		
		new RunComparison().CheckDirAndFile(PathToLatexFolder+"/CSV");
		new Preparer().WriteTxtFile(PathToLatexFolder+"/CSV/Overall.csv", CSV);
		Vector<String> CheckedFiles = new Vector<String>();
		String Table = "\\tiny Pipeline variant &&\\tiny HA-NCS &&&&\\tiny MR-NCS &&&& \\tiny NO-NCS\\\\ \n" + 
				"&&\\tiny Complete & \\tiny Intermediate& \\tiny Failed&&\\tiny Complete &\\tiny Intermediate& \\tiny Failed&& \\tiny Complete & \\tiny Intermediate& \\tiny Failed\\\\ \\hline";
		for (int i = 0; i < Results.size(); ++i) {
			
			String hancs = "-&-&-";
			String mrncs = "-&-&-";
			String noncs = "-&-&-";

			if (!CheckedFiles.contains(Results.get(i).FileName)) {
				for (int dm = 0; dm < Results.size(); ++dm) {
					if (Results.get(i).FileName.equals(Results.get(dm).FileName)) {
						if (Results.get(dm).DM.equals("hancs")) {
							hancs = "\\tiny " + Results.get(dm).Completed + "&" + "\\tiny "
									+ Results.get(dm).IntermediateTime + "(T) " + Results.get(dm).Intermediate + "(C) &"
									+ "\\tiny " + Results.get(dm).Failed;
						}
						if (Results.get(dm).DM.equals("mrncs")) {
							mrncs = "\\tiny " + Results.get(dm).Completed + "&" + "\\tiny "
									+ Results.get(dm).IntermediateTime + "(T) " + Results.get(dm).Intermediate + "(C) &"
									+ "\\tiny " + Results.get(dm).Failed;
						}
						if (Results.get(dm).DM.equals("noncs")) {
							noncs = "\\tiny " + Results.get(dm).Completed + "&" + "\\tiny "
									+ Results.get(dm).IntermediateTime + "(T) " + Results.get(dm).Intermediate + "(C) &"
									+ "\\tiny " + Results.get(dm).Failed;
						}
					}
				}
				
				

				
				Table += "\\tiny " + Results.get(i).FileName + "&&" + hancs + "&&" + mrncs + "&&" + noncs + "\\\\"
						+ "\\hline \n";
				;
			}
			CheckedFiles.add(Results.get(i).FileName);
		}
		int NumberofConsideredCasesHancs=0;
		int NumberofConsideredCasesMrncs=0;
		int NumberofConsideredCasesNoncs=0;
		for (int i = 0; i < Container.size(); ++i) {
			if(e.ToolsNames.get(i).contains("hancs") && NumberofConsideredCasesHancs==0) {
				ExcelLoader f = new ExcelLoader();
				Vector<Vector<ExcelContents>> AllToolsData = new Vector<Vector<ExcelContents>>();
				for(int m =0 ; m < Container.size() ; ++m ) {
					if(e.ToolsNames.get(m).contains("hancs"))
					AllToolsData.add(Container.get(m));
				}
				Vector <ExcelContents> AfterExBuccDev= new Excluding54Datasets().Exculding(Container.get(i), Excluded);
				NumberofConsideredCasesHancs=f.CheckPDBexists(AllToolsData,AfterExBuccDev).size();
				
				
			}
if(e.ToolsNames.get(i).contains("mrncs") && NumberofConsideredCasesMrncs==0) {
	ExcelLoader f = new ExcelLoader();
	Vector<Vector<ExcelContents>> AllToolsData = new Vector<Vector<ExcelContents>>();
	for(int m =0 ; m < Container.size() ; ++m ) {
		if(e.ToolsNames.get(m).contains("mrncs"))
		AllToolsData.add(Container.get(m));
	}
	Vector <ExcelContents> AfterExBuccDev= new Excluding54Datasets().Exculding(Container.get(i), Excluded);
	NumberofConsideredCasesMrncs=f.CheckPDBexists(AllToolsData,AfterExBuccDev).size();
	
			}
if(e.ToolsNames.get(i).contains("noncs") && NumberofConsideredCasesNoncs==0) {
	ExcelLoader f = new ExcelLoader();
	Vector<Vector<ExcelContents>> AllToolsData = new Vector<Vector<ExcelContents>>();
	for(int m =0 ; m < Container.size() ; ++m ) {
		if(e.ToolsNames.get(m).contains("noncs"))
		AllToolsData.add(Container.get(m));
	}
	Vector <ExcelContents> AfterExBuccDev= new Excluding54Datasets().Exculding(Container.get(i), Excluded);
	NumberofConsideredCasesNoncs=f.CheckPDBexists(AllToolsData,AfterExBuccDev).size();
	
}
		}
		Table += " \\multicolumn{10}{c}{\\tiny{ Models used in the comparison: "  + NumberofConsideredCasesHancs + " HA-NCS, " + NumberofConsideredCasesMrncs + " MR-NCS and " + NumberofConsideredCasesNoncs + " NO-NCS.}}"
				+ " \n";
	
		
		Table=FormatingPipelinesNames(Table,true,true);
		new Preparer().WriteTxtFile(PathToLatexFolder+"/TheNumberOfCompletedCases.tex", Table.replace(".xlsx", "") +" \n "+Comments);
	}

	void PDBList(String ResultsDir) throws IOException {
		File[] Folders = new File(ResultsDir).listFiles();
		ExcelLoader e = new ExcelLoader();
		Vector<Vector<ExcelContents>> Container = new Vector<Vector<ExcelContents>>();
		for (File Folder : Folders) {
			if (Folder.isDirectory()) {
				for (File Excel : Folder.listFiles()) {
					Container.add(e.ReadExcel(Excel.getAbsolutePath()));
					e.ToolsNames.add(Excel.getName() + Folder.getName());
				}
			}

		}
		Vector<String> CheckedFiles = new Vector<String>();
		Vector<ExcelContents> PDBList = new Vector<ExcelContents>();
		for (int i = 0; i < Container.size(); ++i) {

			for (int y = 0; y < Container.get(i).size(); ++y) {
				boolean Found = false;
				for (int f = 0; f < PDBList.size(); ++f) {
					if (PDBList.get(f).PDBIDTXT.equals(Container.get(i).get(y).PDB_ID.substring(0, 4))
							&& PDBList.get(f).Resolution.equals(Container.get(i).get(y).Resolution))
						Found = true;
				}
				if (Found == false && !Container.get(i).get(y).Resolution.equals("None")) {
					ExcelContents DC = new ExcelContents();
					DC.PDBIDTXT = Container.get(i).get(y).PDB_ID.substring(0, 4);
					DC.F_mapCorrelation = Container.get(i).get(y).F_mapCorrelation;
					DC.E_mapCorrelation = Container.get(i).get(y).E_mapCorrelation;
					DC.Resolution = Container.get(i).get(y).Resolution;
					PDBList.add(DC);
					CheckedFiles.add(Container.get(i).get(y).PDB_ID);
					if (DC.Resolution.equals("None")) {
						//System.out.println("This is null");
						//System.out.println(e.ToolsNames.get(i));
						//System.out.println(DC.PDBIDTXT);
						;
					}
				}
			}
		}

		
		GroupedResults(PDBList);// Print Reso Table
		Vector<ExcelContents> CheckedPDB = new Vector<ExcelContents>();

		for (int i = 0; i < PDBList.size(); ++i) {

			boolean NotFoundAtAll = false;
			boolean FoundPDB = false;
			int Index = 0;
			for (int f = 0; f < CheckedPDB.size(); ++f) {
				
				if (CheckedPDB.get(f).PDBIDTXT.equals(PDBList.get(i).PDBIDTXT)
						&& CheckedPDB.get(f).Resolution.equals(PDBList.get(i).Resolution))
					NotFoundAtAll = true;
				if (CheckedPDB.get(f).PDBIDTXT.equals(PDBList.get(i).PDBIDTXT)) {
					FoundPDB = true;
					Index = f;
				}
			}
			if (NotFoundAtAll == false && FoundPDB == false) {
				
				ExcelContents DC = new ExcelContents();
				DC.PDBIDTXT = PDBList.get(i).PDBIDTXT;
				DC.F_mapCorrelation = PDBList.get(i).F_mapCorrelation;
				DC.E_mapCorrelation = PDBList.get(i).E_mapCorrelation;
				DC.Resolution = PDBList.get(i).Resolution;
				CheckedPDB.add(DC);
			}
			if (FoundPDB == true) {
				
				ExcelContents DC = CheckedPDB.get(Index);
				DC.Resolution += " " + PDBList.get(i).Resolution;
				DC.F_mapCorrelation += " " + PDBList.get(i).F_mapCorrelation;
				DC.E_mapCorrelation += " " + PDBList.get(i).E_mapCorrelation;
				CheckedPDB.remove(Index);
				CheckedPDB.add(DC);
			}
		}
		String TableContext = "";
		for (int i = 0; i < CheckedPDB.size(); ++i) {
			
			TableContext += "\\tiny " + CheckedPDB.get(i).PDBIDTXT + "&& \\tiny" + CheckedPDB.get(i).Resolution
					+ "&& \\tiny " + CheckedPDB.get(i).F_mapCorrelation + "& \\tiny "
					+ CheckedPDB.get(i).E_mapCorrelation + "\\\\" + "\n";
		}
		Collections.sort(CheckedPDB, ExcelContents.DataContainerComparator);
		
		new Preparer().WriteTxtFile(PathToLatexFolder+"/PDBList.tex", TableContext);

	}

	public Vector<Integer> GroupedResults(Vector<ExcelContents> PDBList) throws IOException {
		Collections.sort(PDBList, ExcelContents.DataContainerComparator);
		// Count The cases depend on Reso
		Vector<Integer> Reso = new Vector<Integer>();
		Vector<Integer> NumReso = new Vector<Integer>();
		

		for (int m = 0; m < PDBList.size(); ++m) {
			
			Double Value = Double.parseDouble(PDBList.get(m).Resolution);

			if (Reso.contains(Value.intValue())) {

				NumReso.set(Reso.indexOf(Value.intValue()), NumReso.get(Reso.indexOf(Value.intValue())) + 1);

			} else {

				Reso.add(Value.intValue());
				NumReso.add(1);

			}

			// }
		}
		String ResoTable = "";
		for (int m = 0; m < Reso.size(); ++m) {
			
			ResoTable += " &" + " \\scriptsize From " + Reso.get(m) + " to less than " + (Reso.get(m) + 1)
					+ " & \\scriptsize " + NumReso.get(m) + " \\\\ \n";
		}
		if (!new File(PathToLatexFolder+"/ResoTable.tex").exists()) {

			new Preparer().WriteTxtFile(PathToLatexFolder+"/ResoTable.tex", ResoTable);

		} else {

			new File(PathToLatexFolder+"/ResoTable.tex").delete();
			new Preparer().WriteTxtFile(PathToLatexFolder+"/ResoTable.tex", ResoTable);
		}

		
		return Reso;
	}

	void BestAndWorstCases(String ResultsDir) throws IOException {
		File[] Folders = new File(ResultsDir).listFiles();

		for (File Folder : Folders) {
			Vector<Vector<ExcelContents>> Container = new Vector<Vector<ExcelContents>>();
			if (Folder.isDirectory()) {
				ExcelLoader e = new ExcelLoader();
				for (File Excel : Folder.listFiles()) {
					Container.add(e.ReadExcel(Excel.getAbsolutePath()));
					e.ToolsNames.add(Excel.getName() + Folder.getName());
				}

				
				Vector<Vector<ExcelContents>> Container2 = new Vector<Vector<ExcelContents>>();
				

				Container2.addAll(Container);
				String TableOfBestCases = "";
				String TableOfWorstCases = "";

				for (int i = 0; i < Container2.size(); ++i) {
					
					Vector<Integer> Reso = GroupedResults(Container2.get(i));
					for (int r = 0; r < Reso.size(); ++r) {

						ExcelContents BestCase = new ExcelContents();
						ExcelContents WorstCase = new ExcelContents();
						BestCase.Completeness = "0";
						WorstCase.Completeness = "None";// Assuming is the worst
						for (int B = 0; B < Container2.get(i).size(); ++B) {
							Double Value = Double.parseDouble(Container2.get(i).get(B).Resolution);
							if (Reso.get(r).equals(Value.intValue())) {
								if (!Container2.get(i).get(B).Completeness.equals("None")
										&& WorstCase.Completeness.equals("None")) {// assign first value and assume is
																					// the worst
									WorstCase = Container2.get(i).get(B);
									// WorstCase.Completeness=Container2.get(i).get(B).Completeness;
								}
								if (!Container2.get(i).get(B).Completeness.equals("None")
										&& Integer.valueOf(Container2.get(i).get(B).Completeness) > Integer
												.valueOf(BestCase.Completeness)) {
									BestCase = Container2.get(i).get(B);
								}
								if (!Container2.get(i).get(B).Completeness.equals("None")
										&& Integer.valueOf(Container2.get(i).get(B).Completeness) < Integer
												.valueOf(WorstCase.Completeness)) {
									WorstCase = Container2.get(i).get(B);
								}
							}
						}
						
						String BestCaseCompareTo = "-1";
						String WorstCaseComapreTo = "";
						String EMapBest = "";
						String PDB1Best = "";

						String RfactorBest = "";
						String TimeTakingBest = "";

						String EMapWorst = "";
						String PDB1Worst = "";

						String RfactorWorst = "";
						String TimeTakingWorst = "";
						int OtherToolBest = i;
						int OtherToolWorst = i;
						BestCaseCompareTo = BestCase.Completeness;
						WorstCaseComapreTo = WorstCase.Completeness;
						for (int Other = 0; Other < Container2.size(); ++Other) {
							// TableOfBestCases+=e.ToolsNames.get(Other)+"&& ";

							for (int t = 0; t < Container2.get(Other).size(); ++t) {

								if (Container2.get(Other).get(t).PDB_ID.equals(BestCase.PDB_ID)
										&& !Container2.get(Other).get(t).Completeness.equals("None")
										&& Integer.valueOf(BestCaseCompareTo) <= Integer
												.valueOf(Container2.get(Other).get(t).Completeness)) {
									
									BestCaseCompareTo = Container2.get(Other).get(t).Completeness;
									EMapBest = Container2.get(Other).get(t).E_mapCorrelation;
									PDB1Best = Container2.get(Other).get(t).NumberofAtomsinFirstPDB;
									RfactorBest = Container2.get(Other).get(t).R_factor0Cycle;
									TimeTakingBest = Container2.get(Other).get(t).TimeTaking;
									OtherToolBest = Other;
								}
								if (Container2.get(Other).get(t).PDB_ID.equals(WorstCase.PDB_ID)
										&& !Container2.get(Other).get(t).Completeness.equals("None")
										&& Integer.valueOf(WorstCaseComapreTo) >= Integer
												.valueOf(Container2.get(Other).get(t).Completeness)) {
									
									WorstCaseComapreTo = Container2.get(Other).get(t).Completeness;

									EMapWorst = Container2.get(Other).get(t).E_mapCorrelation;
									PDB1Worst = Container2.get(Other).get(t).NumberofAtomsinFirstPDB;
									RfactorWorst = Container2.get(Other).get(t).R_factor0Cycle;
									TimeTakingWorst = Container2.get(Other).get(t).TimeTaking;
									OtherToolWorst = Other;
								}

							}

						}

						// TableOfBestCases+=" \\tiny "+e.ToolsNames.get(i)+"(Best Case)" +"& \\tiny
						// "+Reso.get(r)+" to less than "+(Reso.get(r)+1)+" & \\tiny
						// "+e.ToolsNames.get(OtherToolBest)+"&& \\tiny "+BestCase.PDBIDTXT+" & \\tiny
						// "+EMapBest+" & \\tiny "+PDB1Best+" & \\tiny "+BestCaseCompareTo+" & \\tiny
						// "+RfactorBest+" & \\tiny "+TimeTakingBest+" \\\\ \n"; ;
						// TableOfWorstCases+="\\tiny "+ e.ToolsNames.get(i)+"(Worst Case)" +"& \\tiny
						// "+Reso.get(r)+" to less than "+(Reso.get(r)+1)+" & \\tiny
						// "+e.ToolsNames.get(OtherToolWorst)+"&& \\tiny "+WorstCase.PDBIDTXT+" & \\tiny
						// "+EMapWorst+" & \\tiny "+PDB1Worst+" & \\tiny "+WorstCaseComapreTo+" & \\tiny
						// "+RfactorWorst+" & \\tiny "+TimeTakingWorst+" \\\\ \n"; ;

						TableOfBestCases += " \\tiny " + e.ToolsNames.get(i) + "(Best Case)" + "& \\tiny " + Reso.get(r)
								+ " to less than " + (Reso.get(r) + 1) + " & \\tiny " + e.ToolsNames.get(OtherToolBest)
								+ "&& \\tiny " + BestCase.PDBIDTXT + " & \\tiny " + EMapBest + " & \\tiny " + PDB1Best
								+ " & \\tiny " + BestCase.Completeness + "\\% " + BestCaseCompareTo + "\\% & \\tiny "
								+ BestCase.R_factor0Cycle + " " + RfactorBest + " & \\tiny " + BestCase.TimeTaking + " "
								+ TimeTakingBest + " \\\\ \n";
						;
						TableOfWorstCases += "\\tiny " + e.ToolsNames.get(i) + "(Worst Case)" + "& \\tiny "
								+ Reso.get(r) + " to less than " + (Reso.get(r) + 1) + " & \\tiny "
								+ e.ToolsNames.get(OtherToolWorst) + "&& \\tiny " + WorstCase.PDBIDTXT + " & \\tiny "
								+ EMapWorst + " & \\tiny " + PDB1Worst + " & \\tiny " + WorstCase.Completeness + "\\% "
								+ WorstCaseComapreTo + "\\% & \\tiny " + WorstCase.R_factor0Cycle + " " + RfactorWorst
								+ " & \\tiny " + WorstCase.TimeTaking + " " + TimeTakingWorst + " \\\\ \n";
						;

						TableOfBestCases += TableOfWorstCases;
						TableOfWorstCases = "";
					}
				}
				
				new Preparer().WriteTxtFile(PathToLatexFolder+"/" + Folder.getName() + "BestAndWorstTable.tex", TableOfBestCases);
			}

		}

	}
	
/*
	void PrepareExcelForSpss(String ResultsDir) throws IOException {
		File[] Folders = new File(ResultsDir).listFiles();

		for (File Folder : Folders) {
			Vector<Vector<ExcelContents>> Container = new Vector<Vector<ExcelContents>>();
			ExcelLoader e = new ExcelLoader();
			if (Folder.isDirectory()) {
				for (File Excel : Folder.listFiles()) {
					Container.add(e.ReadExcel(Excel.getAbsolutePath()));
					e.ToolsNames.add(Excel.getName());
				}
				
				Vector<Vector<ExcelContents>> Container2 = new Vector<Vector<ExcelContents>>();
				for (int i = 0; i < Container.size(); ++i) {

					
					Container2.addElement(e.CheckPDBexists(Container, Container.get(i)));
					
				}
				
				Container.removeAllElements();
				Container.addAll(Container2); // only the cases that built by all tools
				
				Vector<Integer> Reso = GroupedResults(Container.get(0)); // because all excels have the same cases so
																			// one is engouh
				for (int r = 0; r < Reso.size(); ++r) {
					Vector<ExcelContents> ToBeInExcel = new Vector<ExcelContents>();
					for (int i = 0; i < Container.size(); ++i) {
						for (int y = 0; y < Container.get(i).size(); ++y) {
							Double Value = Double.parseDouble(Container.get(i).get(y).Resolution);
							

							if (Reso.get(r).equals(Value.intValue())) {
								ToBeInExcel.add(Container.get(i).get(y));
								ToBeInExcel.get(ToBeInExcel.size() - 1).PDB_ID = e.ToolsNames.get(i);// to save the name
																										// of tool
																										// instead of
																										// PDB ID
							}

						}
					}
					new RunComparison().CheckDirAndFile("SpssExcel");
					new RunComparison().CheckDirAndFile("SpssExcel/Reso" + r);
					new ExcelSheet().FillInExcel(ToBeInExcel, ("SpssExcel/Reso" + r + "/" + Folder.getName()));
				}
			}

		}
	}

	void SpssBootstraping(String ResultsDir) throws StatsException {
		File[] Folders = new File(ResultsDir).listFiles();
		new RunComparison().CheckDirAndFile("SpssExcelResults");

		for (File Folder : Folders) {
			if (Folder.isDirectory()) {
				for (File Excel : Folder.listFiles()) {

					String File = "/FILE='" + Excel.getAbsolutePath() + "'";
					new RunComparison().CheckDirAndFile("SpssExcelResults/" + Folder.getName());
					String Des = "/DESTINATION FORMAT =XLSX OUTFILE = 'SpssExcelResults/" + Folder.getName() + "/"
							+ Excel.getName() + "'.";
					StatsUtil.start();
					String[] command = { "GET DATA ", "  /TYPE=XLSX ", File, "  /SHEET=name 'Data' ",
							"  /CELLRANGE=FULL ", "  /READNAMES=ON ", "  /DATATYPEMIN PERCENTAGE=95.0 ",
							"  /HIDDEN IGNORE=YES. ", "AUTORECODE VARIABLES=File ", "  /INTO Tool", "  /PRINT.",
							"alter type Completeness(f3).", "alter type Rfactor0Cycle(f3).",
							"alter type TimeTaking(f6).", "alter type Rfree0Cycle(f3).", // New Line was added
							"OMS SELECT TABLES ", "/IF COMMANDS = ['ONEWAY'] ", "SUBTYPES = ['Descriptives']", Des,
							" BOOTSTRAP", "  /SAMPLING METHOD=SIMPLE",
							"  /VARIABLES TARGET=Completeness Rfactor0Cycle Rfree0Cycle TimeTaking  INPUT=Tool",
							"  /CRITERIA CILEVEL=95 CITYPE=PERCENTILE  NSAMPLES=1000",
							"  /MISSING USERMISSING=EXCLUDE.",
							"   ONEWAY Completeness Rfactor0Cycle Rfree0Cycle TimeTaking  BY Tool",
							"  /STATISTICS DESCRIPTIVES", "  /MISSING ANALYSIS.",

							"OMSEND.", };

					StatsUtil.submit(command);
				}
			}
		}

	}

	void ReadingSpssBootstraping(String ResultsDir) throws IOException {
		File[] Folders = new File(ResultsDir).listFiles();
		Arrays.sort(Folders);
		String TheFullTable = "";
		for (File Folder : Folders) {

			if (Folder.isDirectory()) {
				Vector<SPSSDescriptivesTable> table = new Vector<SPSSDescriptivesTable>();
				String Reso = "";
				for (File Excel : Folder.listFiles()) {

					Vector<SPSSDescriptivesTable> ThisExcel = new SPSS()
							.DescriptivesTableReader(Excel.getAbsolutePath());
					

					for (int i = 0; i < ThisExcel.size(); ++i) {
						ThisExcel.get(i).Dataset = Excel.getName();
					}
					Reso = Folder.getName().replaceAll("^\\D*(\\d+).*", "$1");
					table.addAll(ThisExcel);

				}
				

				Vector<String> Pipelines = new Vector<String>();
				for (int i = 0; i < table.size(); ++i) {

					String RowHancs = "";
					String RowMrncs = "";
					String RowNoncs = "";

					if (!Pipelines.contains(table.get(i).Pipeline)) {
						for (int r = 0; r < table.size(); ++r) {
							if (table.get(r).Pipeline.equals(table.get(i).Pipeline)) {
								if (table.get(r).Dataset.contains("hancs")) {
									RowHancs += " \\tiny " + table.get(r).Mean + " &";
								}
								if (table.get(r).Dataset.contains("mrncs")) {
									RowMrncs += " \\tiny " + table.get(r).Mean + " &";
								}
								if (table.get(r).Dataset.contains("noncs")) {
									RowNoncs += " \\tiny " + table.get(r).Mean + "&";
								}

							}
						}

						Pipelines.add(table.get(i).Pipeline);
						if (RowHancs.trim().equals(""))
							RowHancs = "&-&-&-&-&";
						if (RowMrncs.trim().equals(""))
							RowMrncs = "&-&-&-&-&";
						if (RowNoncs.trim().equals(""))
							RowNoncs = "&-&-&-&-";
						else
							RowNoncs = RowNoncs.substring(0, RowNoncs.length() - 2); // to remove the last &
						
						
						String ResoRow = " \\footnotesize \\parbox[t]{-10mm}{\\multirow{PipelinesNum}{2.3cm}{\\rotatebox[origin=c]{90}{Group R1 \\r{A}}}}";

						ResoRow = ResoRow.replace("R1", String.valueOf((Integer.valueOf(Reso) + 1)));
						
						if (TheFullTable.contains(ResoRow)) {
							TheFullTable += "& " + " \\tiny " + table.get(i).Pipeline + " &" + RowHancs + " " + RowMrncs
									+ " " + RowNoncs + " \\\\ \n";
							;
						} else {
							TheFullTable += ResoRow + " & \\tiny " + table.get(i).Pipeline + "& " + RowHancs + " "
									+ RowMrncs + " " + RowNoncs + " \\\\ \n";
							;
						}
					}

					

				}

				TheFullTable = TheFullTable.replace("PipelinesNum", String.valueOf(Pipelines.size()));
				TheFullTable += "\\hline \n";
			}
		}

		new Preparer().WriteTxtFile(PathToLatexFolder+"/" + "TheComparsionResults.tex", TheFullTable);

	}
*/
	void GroupByPhases(String ResultsDir) throws IOException {
		File[] Folders = new File(ResultsDir).listFiles();
		ExcelLoader e = new ExcelLoader();
		Vector<Vector<ExcelContents>> Container = new Vector<Vector<ExcelContents>>();
		for (File Folder : Folders) {
			if (Folder.isDirectory()) {
				for (File Excel : Folder.listFiles()) {
					Container.add(e.ReadExcel(Excel.getAbsolutePath()));
					e.ToolsNames.add(Excel.getName() + Folder.getName());
				}
			}

		}

		double min = 1.0;
		double max = 0.0;
		for (int i = 0; i < Container.size(); ++i)
			for (int m = 0; m < Container.get(i).size(); ++m) {

				if (NumberUtils.isParsable(Container.get(i).get(m).E_mapCorrelation)
						&& Double.parseDouble(Container.get(i).get(m).E_mapCorrelation) < min)
					min = Double.parseDouble(Container.get(i).get(m).E_mapCorrelation);
				if (NumberUtils.isParsable(Container.get(i).get(m).E_mapCorrelation)
						&& Double.parseDouble(Container.get(i).get(m).E_mapCorrelation) > max)
					max = Double.parseDouble(Container.get(i).get(m).E_mapCorrelation);
			}

		PhasesTable(Container.get(1));
	}

	Vector<Integer> PhasesTable(Vector<ExcelContents> PDBList) throws IOException {
		Collections.sort(PDBList, ExcelContents.DataContainerComparatorEmap);
		// Count The cases depend on Reso
		Vector<Integer> Reso = new Vector<Integer>();
		Vector<Integer> NumReso = new Vector<Integer>();
		

		for (int m = 0; m < PDBList.size(); ++m) {
			
			if (NumberUtils.isParsable(PDBList.get(m).E_mapCorrelation)) {
				Double Value = Double.parseDouble(PDBList.get(m).E_mapCorrelation) * 10;

				if (Reso.contains(Value.intValue())) {

					NumReso.set(Reso.indexOf(Value.intValue()), NumReso.get(Reso.indexOf(Value.intValue())) + 1);

				} else {

					Reso.add(Value.intValue());
					NumReso.add(1);

				}
			} else {
				
				if (Reso.contains(-1)) {

					NumReso.set(Reso.indexOf(-1), NumReso.get(Reso.indexOf(-1)) + 1);

				} else {

					Reso.add(-1);
					NumReso.add(1);

				}
			}
		}
		String ResoTable = "";
		for (int m = 0; m < Reso.size(); ++m) {
			

			ResoTable += " &" + " \\tiny From " + Reso.get(m) + " to less than " + (Reso.get(m) + 1) + " & \\tiny "
					+ NumReso.get(m) + " \\\\ \n";
		}
		if (!new File(PathToLatexFolder+"/ResoTable.tex").exists()) {

			new Preparer().WriteTxtFile(PathToLatexFolder+"/ResoTable.tex", ResoTable);

		} else {

			new File(PathToLatexFolder+"/ResoTable.tex").delete();
			new Preparer().WriteTxtFile(PathToLatexFolder+"/ResoTable.tex", ResoTable);
		}

		
		return Reso;
	}
	
void TableOfMeanAndSD(String ResultsDir) throws IOException {
	File[] Folders = new File(ResultsDir).listFiles();
	for (File Folder : Folders) {
		if (Folder.isDirectory()) {
			String Table="\\tiny Pipeline &  \\multicolumn{2}{c}{\\tiny Completeness} &  \\multicolumn{2}{c}{\\tiny R-free} \\\\  \n";
			 Table+="& \\tiny mean & \\tiny SD & \\tiny mean & \\tiny SD \\\\ \\hline \n";
			for(File excel : Folder.listFiles()) {
				Vector<ExcelContents> Excel = new Vector<ExcelContents>();
				ExcelLoader e = new ExcelLoader();
				Excel=e.ReadExcel(excel.getAbsolutePath());
				Table+="\\tiny "+excel.getName()+"&";
				double Com=0;
				double R=0;
				double Rfree=0;
				double [] ComInArr= new double [Excel.size()]; 
				double [] RfreeInArr= new double [Excel.size()]; 
				for(int i=0 ; i < Excel.size();++i) {
					BigDecimal comDec= new BigDecimal(Excel.get(i).Completeness);
					Com+=comDec.doubleValue();
					ComInArr[i]=comDec.doubleValue();
					BigDecimal RDec= new BigDecimal(Excel.get(i).R_factor0Cycle);
					R+=RDec.doubleValue();
					
					BigDecimal RfreeDec= new BigDecimal(Excel.get(i).R_free0Cycle);
					Rfree+=RfreeDec.doubleValue();
					RfreeInArr[i]=RfreeDec.doubleValue();
				}
				
				Table+="\\tiny "+new BigDecimal(Com/Excel.size()).setScale(0, RoundingMode.HALF_UP)+"& \\tiny"+new BigDecimal(new StandardDeviation().evaluate(ComInArr)).setScale(0, RoundingMode.HALF_UP)+"&";
				Table+="\\tiny "+new BigDecimal(Rfree/Excel.size()).setScale(2, RoundingMode.HALF_UP)+"& \\tiny"+new BigDecimal(new StandardDeviation().evaluate(RfreeInArr)).setScale(2, RoundingMode.HALF_UP)+"\\\\ \n";

			}
			new Preparer().WriteTxtFile(PathToLatexFolder+"/"+Folder.getName()+"Mean.tex",FormatingPipelinesNames(Table,true,true));

		}
	}
}
	
	void MatrixOfResults(String ResultsDir) throws IOException {
		if(new File("MatricesLogs").exists()) {
			FileUtils.deleteDirectory(new File("MatricesLogs"));
		}
		File[] Folders = new File(ResultsDir).listFiles();
		
		for (File Folder : Folders) {
			if (Folder.isDirectory()) {
				Vector<Vector<ExcelContents>> Container = new Vector<Vector<ExcelContents>>();
				ExcelLoader e = new ExcelLoader();
				String Comments="";
				Comments = "% "+new Date().toString()+" \n";
				Comments += "% This table generated from \n % Folder :"+Folder.getName()+" \n";
				Comments += "% Full folder path :"+Folder.getAbsolutePath()+" \n";
				Comments += "% Excel used : \n ";
				
				File [] ThisFolder = Folder.listFiles();
				Arrays.sort(ThisFolder, (f1, f2) -> f1.compareTo(f2)); // sorting pipelines 
				for (File Excel : ThisFolder) {
					if(Excel.isFile()) {
					
					Comments += " %  "+Excel.getAbsolutePath() +" \n";
					Container.add(e.ReadExcel(Excel.getAbsolutePath()));
					e.ToolsNames.add(Excel.getName() + Folder.getName());
					}
				}
				String Table = "";
				String Col = "";
				String RowCom0 = "";
				String RowCom5 = "";
				
				String RowCom0Equivalent = "";
				String RowCom5Equivalent = "";
				
				String RowCom0Models = "";
				String RowCom5Models= "";
				
				
				String RowR0 = "";
				
				
				String RowR0Equivalent = "";
			
				String RowROverfitting = "";
				
				String RowR5 = "";
				String RowR5Overfitting="";
				
				String RowRFree0 = "";
				String RowRFree5 = "";
				
				String RowRFree0Equivalent = "";
				String RowRFree5Equivalent = "";
				String RowR5Equivalent = "";
				
				String LogOfR5Equivalent="";
				
				String LogOfCom0="";
				String LogOfCom0Equivalent="";
				String LogOfCom5="";
				String LogOfCom5Equivalent="";
				String LogOfR0="";
				String LogOfRFree0="";
				
				String LogOfR0Equivalent="";
				String LogOfRFree0Equivalent="";
				
				String LogOfR5="";
				String LogOfRFree5="";
				
				String LogOfRFree5Equivalent="";
				String CSVCompareTo="Pipeline,Percentage,CompareTo,StatisticalMeasure\n";
				for (int i = 0; i < Container.size(); ++i) {
					Col = "\\tiny Pipeline variant ";// avoiding repetition
					
					RowCom0 += "\\tiny " + e.ToolsNames.get(i);
					RowCom5 += "\\tiny " + e.ToolsNames.get(i);
					 RowCom0Equivalent  += "\\tiny " + e.ToolsNames.get(i);
					 RowCom5Equivalent += "\\tiny " + e.ToolsNames.get(i);
					
					 RowCom0Models += "\\tiny " + e.ToolsNames.get(i);
					 RowCom5Models+= "\\tiny " + e.ToolsNames.get(i);
					 
					 
					 RowR0 += "\\tiny " + e.ToolsNames.get(i)+" $_{R-work}$";
					 RowR0Equivalent+= "\\tiny " + e.ToolsNames.get(i)+" $_{R-work}$";
					 RowROverfitting="\\tiny Overfitting" ;
					 RowR5 += "\\tiny " + e.ToolsNames.get(i)+" $_{R-work}$";
					 RowR5Overfitting="\\tiny Overfitting" ;
					 
					  RowRFree0 ="\\tiny " + e.ToolsNames.get(i)+" $_R{_{-free}}$";
					  
					  RowRFree5 = "\\tiny " + e.ToolsNames.get(i)+" $_R{_{-free}}$";
					  
					  RowRFree0Equivalent="\\tiny " + e.ToolsNames.get(i)+" $_R{_{-free}}$";
				
					   RowRFree5Equivalent = "\\tiny " + e.ToolsNames.get(i)+" $_R{_{-free}}$";
					   RowR5Equivalent += "\\tiny " + e.ToolsNames.get(i)+" $_{R-work}$";
					   
					for (int m = 0; m < Container.size(); ++m) {

						if (Col.length() == 0) {
							Col +=  " \\tiny " + e.ToolsNames.get(m);
						} else {
							Col +=  "& \\tiny " + e.ToolsNames.get(m);
						}
						float CountModelCom0 = 0;
						float EquivalentFor0=0;
						float CountModel=0;
						
						float CountModelCom5 = 0;
						float EquivalentFor5=0;
						
						float CountModelR = 0;
						float EquivalentR=0;
						float OverfittingR=0;
						
						float CountModelR5 = 0;
						float OverfittingR5= 0;
						
						float CountRFree0=0;
						float CountRFree5=0;
						
						float EquivalentRFree=0;
						
						float EquivalentR5=0;
						float EquivalentRFree5=0;
						
						boolean IsRFreeFlagUsed=true;// because ARP/wARP usually not used it. if RFREE is zero from the log file that mean not used. 
						for (int model = 0; model < Container.get(i).size(); ++model) {
							for (int modeComTo = 0; modeComTo < Container.get(m).size(); ++modeComTo) {
								
								
								
								if (Container.get(i).get(model).PDB_ID.equals(Container.get(m).get(modeComTo).PDB_ID)) {
									if (Container.get(i).get(model).BuiltPDB.equals("T")
											&& Container.get(m).get(modeComTo).BuiltPDB.equals("T")) {
										CountModel++;
										if(Container.get(i).get(model).R_free.equals("0") || Container.get(m).get(modeComTo).R_free.equals("0") ) {
											IsRFreeFlagUsed=false;
										}
										
										if (  new BigDecimal(Container.get(i).get(model).Completeness).setScale(0, RoundingMode.HALF_UP).subtract( 
												new BigDecimal(Container.get(m).get(modeComTo).Completeness).setScale(0, RoundingMode.HALF_UP)).compareTo(new BigDecimal("1")) >=0)  { 
										

												CountModelCom0++;
												LogOfCom0+=e.ToolsNames.get(i) +"\t"+Container.get(i).get(model).PDB_ID+"\t"+	new BigDecimal(Container.get(i).get(model).Completeness).setScale(0, RoundingMode.HALF_UP)+"\t"+	e.ToolsNames.get(m)+"\t"+Container.get(m).get(modeComTo).PDB_ID+"\t"	+new BigDecimal(Container.get(m).get(modeComTo).Completeness).setScale(0, RoundingMode.HALF_UP)+"\n";				

												
										}
									if (  new BigDecimal(Container.get(i).get(model).Completeness).setScale(0, RoundingMode.HALF_UP).subtract( 
											new BigDecimal(Container.get(m).get(modeComTo).Completeness).setScale(0, RoundingMode.HALF_UP)).compareTo(new BigDecimal("0"))==0) {
										// if(Integer.valueOf(Container.get(i).get(model).Completeness) -
										// Integer.valueOf(Container.get(m).get(modeComTo).Completeness) >=5) {

										EquivalentFor0++;
										LogOfCom0Equivalent+=e.ToolsNames.get(i) +"\t"+Container.get(i).get(model).PDB_ID+"\t"+	new BigDecimal(Container.get(i).get(model).Completeness).setScale(0, RoundingMode.HALF_UP)+"\t"+	e.ToolsNames.get(m)+"\t"+Container.get(m).get(modeComTo).PDB_ID+"\t"	+new BigDecimal(Container.get(m).get(modeComTo).Completeness).setScale(0, RoundingMode.HALF_UP)+"\n";				

										
									}
									
									
									if (  new BigDecimal(Container.get(i).get(model).Completeness).setScale(0, RoundingMode.HALF_UP).subtract( 
											new BigDecimal(Container.get(m).get(modeComTo).Completeness).setScale(0, RoundingMode.HALF_UP)).compareTo(new BigDecimal("5")) >=0)  {
									
								

										CountModelCom5++;
										LogOfCom5+=e.ToolsNames.get(i) +"\t"+Container.get(i).get(model).PDB_ID+"\t"+	new BigDecimal(Container.get(i).get(model).Completeness).setScale(0, RoundingMode.HALF_UP)+"\t"+	e.ToolsNames.get(m)+"\t"+Container.get(m).get(modeComTo).PDB_ID+"\t"	+new BigDecimal(Container.get(m).get(modeComTo).Completeness).setScale(0, RoundingMode.HALF_UP)+"\n";				
	
										
							}
							if (new BigDecimal(Container.get(i).get(model).Completeness).setScale(0, RoundingMode.HALF_UP).subtract( 
									new BigDecimal(Container.get(m).get(modeComTo).Completeness).setScale(0, RoundingMode.HALF_UP)).compareTo(new BigDecimal("5")) <0 && new BigDecimal(Container.get(i).get(model).Completeness).setScale(0, RoundingMode.HALF_UP).subtract( 
											new BigDecimal(Container.get(m).get(modeComTo).Completeness).setScale(0, RoundingMode.HALF_UP)).compareTo(new BigDecimal("0")) >0 ) {
								LogOfCom5Equivalent+=e.ToolsNames.get(i) +"\t"+Container.get(i).get(model).PDB_ID+"\t"+	new BigDecimal(Container.get(i).get(model).Completeness).setScale(0, RoundingMode.HALF_UP)+"\t"+	e.ToolsNames.get(m)+"\t"+Container.get(m).get(modeComTo).PDB_ID+"\t"	+new BigDecimal(Container.get(m).get(modeComTo).Completeness).setScale(0, RoundingMode.HALF_UP)+"\n";				

								
								EquivalentFor5++;
							}
							
						
								if( new BigDecimal(Container.get(i).get(model).R_factor0Cycle).compareTo(new BigDecimal(Container.get(m).get(modeComTo).R_factor0Cycle)) < 0  ) { 

									
								CountModelR ++;
								LogOfR0+=e.ToolsNames.get(i) +"\t"+Container.get(i).get(model).PDB_ID+"\t"+	new BigDecimal(Container.get(i).get(model).R_factor0Cycle)+"\t"+	e.ToolsNames.get(m)+"\t"+Container.get(m).get(modeComTo).PDB_ID+"\t"	+new BigDecimal(Container.get(m).get(modeComTo).R_factor0Cycle)+"\n";				

								
						if( new BigDecimal(Container.get(i).get(model).R_free0Cycle).subtract(new BigDecimal(Container.get(i).get(model).R_factor0Cycle)).compareTo(new BigDecimal("0.05")) >0 ) {
		
								OverfittingR++;
						}
							}
								
								
								
								if( new BigDecimal(Container.get(i).get(model).R_free0Cycle).compareTo(new BigDecimal(Container.get(m).get(modeComTo).R_free0Cycle)) < 0  ) {

									
									CountRFree0++;
									
									LogOfRFree0+=e.ToolsNames.get(i) +"\t"+Container.get(i).get(model).PDB_ID+"\t"+	new BigDecimal(Container.get(i).get(model).R_free0Cycle)+"\t"+	e.ToolsNames.get(m)+"\t"+Container.get(m).get(modeComTo).PDB_ID+"\t"	+new BigDecimal(Container.get(m).get(modeComTo).R_free0Cycle)+"\n";				

								}
								
								
								if(   new BigDecimal(Container.get(i).get(model).R_factor0Cycle).add(new BigDecimal("0.05")).compareTo(new BigDecimal(Container.get(m).get(modeComTo).R_factor0Cycle)) <= 0  ) {

									
									LogOfR5+=e.ToolsNames.get(i) +"\t"+Container.get(i).get(model).PDB_ID+"\t"+	new BigDecimal(Container.get(i).get(model).R_factor0Cycle)+"\t"+	e.ToolsNames.get(m)+"\t"+Container.get(m).get(modeComTo).PDB_ID+"\t"	+new BigDecimal(Container.get(m).get(modeComTo).R_factor0Cycle)+"\n";				

									CountModelR5++;
									if( new BigDecimal(Container.get(i).get(model).R_free0Cycle).subtract(new BigDecimal(Container.get(i).get(model).R_factor0Cycle)).compareTo(new BigDecimal("0.05")) >0 ) {
										
										OverfittingR5++;
								}
								}	
								
								
if( new BigDecimal(Container.get(i).get(model).R_free0Cycle).add(new BigDecimal("0.05")).compareTo(new BigDecimal(Container.get(m).get(modeComTo).R_free0Cycle)) <= 0  ) {

		
	LogOfRFree5+=e.ToolsNames.get(i) +"\t"+Container.get(i).get(model).PDB_ID+"\t"+	new BigDecimal(Container.get(i).get(model).R_free0Cycle)+"\t"+	e.ToolsNames.get(m)+"\t"+Container.get(m).get(modeComTo).PDB_ID+"\t"	+new BigDecimal(Container.get(m).get(modeComTo).R_free0Cycle)+"\n";				

	CountRFree5++;
									
								}		
								
						//	if( new BigDecimal(Container.get(i).get(model).R_free0Cycle).subtract(new BigDecimal(Container.get(i).get(model).R_factor0Cycle) ).compareTo(new BigDecimal("0.05")) <= 0 &&  new BigDecimal(Container.get(m).get(modeComTo).R_free0Cycle).subtract(new BigDecimal(Container.get(m).get(modeComTo).R_factor0Cycle) ).compareTo(new BigDecimal("0.05")) <= 0 ) {
								if( new BigDecimal(Container.get(i).get(model).R_factor0Cycle).compareTo(new BigDecimal(Container.get(m).get(modeComTo).R_factor0Cycle)) == 0  ) {
		
								 EquivalentR++;
									LogOfR0Equivalent+=e.ToolsNames.get(i) +"\t"+Container.get(i).get(model).PDB_ID+"\t"+	new BigDecimal(Container.get(i).get(model).R_factor0Cycle)+"\t"+	e.ToolsNames.get(m)+"\t"+Container.get(m).get(modeComTo).PDB_ID+"\t"	+new BigDecimal(Container.get(m).get(modeComTo).R_factor0Cycle)+"\n";				

							}
								
								
								
								if( new BigDecimal(Container.get(i).get(model).R_free0Cycle).compareTo(new BigDecimal(Container.get(m).get(modeComTo).R_free0Cycle)) == 0  ) {
									
									EquivalentRFree++;
									LogOfRFree0Equivalent+=e.ToolsNames.get(i) +"\t"+Container.get(i).get(model).PDB_ID+"\t"+	new BigDecimal(Container.get(i).get(model).R_free0Cycle)+"\t"+	e.ToolsNames.get(m)+"\t"+Container.get(m).get(modeComTo).PDB_ID+"\t"	+new BigDecimal(Container.get(m).get(modeComTo).R_free0Cycle)+"\n";				

								}
							
								
if(new BigDecimal(Container.get(i).get(model).R_factor0Cycle).compareTo(new BigDecimal(Container.get(m).get(modeComTo).R_factor0Cycle)) < 0 && new BigDecimal(Container.get(i).get(model).R_factor0Cycle).add(new BigDecimal("0.05")).compareTo(new BigDecimal(Container.get(m).get(modeComTo).R_factor0Cycle)) > 0 &&  new BigDecimal(Container.get(i).get(model).R_factor0Cycle).compareTo(new BigDecimal(Container.get(m).get(modeComTo).R_factor0Cycle))!=0) {
	
									
	EquivalentR5++;
	LogOfR5Equivalent+=e.ToolsNames.get(i) +"\t"+Container.get(i).get(model).PDB_ID+"\t"+	Container.get(i).get(model).R_factor0Cycle+"\t"+	e.ToolsNames.get(m)+"\t"+Container.get(m).get(modeComTo).PDB_ID+"\t"	+Container.get(m).get(modeComTo).R_factor0Cycle+"\n";				
								}

if(new BigDecimal(Container.get(i).get(model).R_free0Cycle).compareTo(new BigDecimal(Container.get(m).get(modeComTo).R_free0Cycle)) < 0 &&new BigDecimal(Container.get(i).get(model).R_free0Cycle).add(new BigDecimal("0.05")).compareTo(new BigDecimal(Container.get(m).get(modeComTo).R_free0Cycle)) > 0 &&  new BigDecimal(Container.get(i).get(model).R_free0Cycle).compareTo(new BigDecimal(Container.get(m).get(modeComTo).R_free0Cycle))!=0) {

	
	EquivalentRFree5++;
	
	LogOfRFree5Equivalent+=e.ToolsNames.get(i) +"\t"+Container.get(i).get(model).PDB_ID+"\t"+	Container.get(i).get(model).R_free0Cycle+"\t"+	e.ToolsNames.get(m)+"\t"+Container.get(m).get(modeComTo).PDB_ID+"\t"	+Container.get(m).get(modeComTo).R_free0Cycle+"\n";				

									
								}
								
									}
								}
							}
						}
						
						 if (RowCom0.length() == 0) {
							RowCom0 += CountModelCom0;
							RowCom5 += CountModelCom5;
							RowR0 += CountModelR;
							RowROverfitting+=OverfittingR;
							RowR5+=CountModelR5;
							RowR5Overfitting+=OverfittingR5;
							RowR5Equivalent+=EquivalentR5;
							RowRFree5Equivalent+=EquivalentRFree5;
						}
						 else{
							
							DecimalFormat decim = new DecimalFormat("#");
							
							RowCom0 += " &  " + decim.format(((CountModelCom0 * 100) / CountModel))  ;
							
							 RowCom0Equivalent += " &  " + decim.format(((EquivalentFor0 * 100) / CountModel))  ;
							
							
							RowCom5 += " &  " + decim.format(((CountModelCom5 * 100) / CountModel));
							
							
							 RowCom5Equivalent+= " & " + decim.format(((EquivalentFor5 * 100) / CountModel));
							
							  RowCom0Models += " &  "+(int)CountModel;
							  RowCom5Models+= " &  "+(int)CountModel;
							  
							
							 RowR0 += " &  " + decim.format(((CountModelR * 100) / CountModel))  ;
								
							  RowR0Equivalent += " &  " + decim.format(((EquivalentR * 100) / CountModel)) ;
							  
							  if(OverfittingR==0)
								  RowROverfitting+=   "& "; 
								  if(OverfittingR!=0)
							  RowROverfitting+=   " &  " + decim.format(((OverfittingR * 100) / CountModelR));
							  
								
									  RowR5 += " &  " + decim.format(((CountModelR5 * 100) / CountModel))  ;
								  
							  if(OverfittingR5==0)
						       RowR5Overfitting+=   " &  0";
							  if(OverfittingR5!=0)
							  RowR5Overfitting+=   " &  " + decim.format(((OverfittingR5 * 100) / CountModelR5)) ;

							  CSVCompareTo+=e.ToolsNames.get(i)+","+decim.format(((CountModelCom5 * 100) / CountModel))+","+e.ToolsNames.get(m)+",Com5\n";
							  CSVCompareTo+=e.ToolsNames.get(i)+","+decim.format(((CountModelCom0 * 100) / CountModel))+","+e.ToolsNames.get(m)+",Com0\n";
							  CSVCompareTo+=e.ToolsNames.get(i)+","+decim.format(((CountModelR * 100) / CountModel))+","+e.ToolsNames.get(m)+",R0\n";
							  CSVCompareTo+=e.ToolsNames.get(i)+","+decim.format(((CountModelR5 * 100) / CountModel))+","+e.ToolsNames.get(m)+",R5\n";
							  CSVCompareTo+=e.ToolsNames.get(i)+","+decim.format(((CountRFree5 * 100) / CountModel))+","+e.ToolsNames.get(m)+",Free5\n";
							  CSVCompareTo+=e.ToolsNames.get(i)+","+decim.format(((CountRFree0 * 100) / CountModel))+","+e.ToolsNames.get(m)+",Free0\n";

						if(IsRFreeFlagUsed==true) {	
					RowRFree0 += " &  " + decim.format(((CountRFree0 * 100) / CountModel))  ;
								
								
					RowRFree5 += " &  " + decim.format(((CountRFree5 * 100) / CountModel))  ;	
							  
					RowRFree0Equivalent+=	" & " + decim.format(((EquivalentRFree * 100) / CountModel));		
					
					
					 RowRFree5Equivalent+=	" &  " + decim.format(((EquivalentRFree5 * 100) / CountModel)) ;
					 
						}
						if(IsRFreeFlagUsed==false) {	
							RowRFree0 += " & - " ;
							
							
							RowRFree5 += " & - " ;	
									  
							RowRFree0Equivalent+=	" & -" ;		
							
							 
							
							 RowRFree5Equivalent+=	" & - "  ;
								
						}
					 RowR5Equivalent += " & " + decim.format(((EquivalentR5 * 100) / CountModel)) ;

					 LogOfCom0+="\n Number of models: "+CountModelCom0+" \n";
					 
					 LogOfCom0Equivalent+="\n Number of models: "+EquivalentFor0+" \n";
					 
					 LogOfCom5+="\n Number of models: "+CountModelCom5+" \n";
					 
					 LogOfCom5Equivalent+="\n Number of models: "+EquivalentFor5+" \n";
					 LogOfR0+="\n Number of models: "+CountModelR+" \n";
					 
					 LogOfRFree0+="\n Number of models: "+CountRFree0+" \n";
					 
LogOfR0Equivalent+="\n Number of models: "+EquivalentR+" \n";

					 LogOfRFree0Equivalent+="\n Number of models: "+EquivalentRFree+" \n";
					 LogOfR5+="\n Number of models: "+CountModelR5+" \n";
					 
					 LogOfRFree5+="\n Number of models: "+CountRFree5+" \n";
					 LogOfRFree5Equivalent+="\n Number of models: "+EquivalentRFree5+" \n";
						 }
					}
					
					
					RowCom0 += " \\\\ \\hline \n ";
					RowCom5 += " \\\\ \\hline \n ";
					
					RowCom0Equivalent += " \\\\ \\hline \n ";
					RowCom0Models += " \\\\ \\hline \n ";
					
					RowCom5Equivalent += " \\\\ \\hline \n ";
					RowCom5Models += " \\\\ \\hline \n ";
					
					RowR0 += " \\\\  \n ";
					RowR0+=RowRFree0 +" \\\\ \\hline \n ";
					
					
					
					RowR0Equivalent += " \\\\  \n ";
					RowR0Equivalent+=RowRFree0Equivalent +" \\\\ \\hline \n ";
					
					
					
					RowR5 += " \\\\  \n ";
					RowR5+=RowRFree5 +" \\\\ \\hline \n ";
					
					RowR5Equivalent += " \\\\  \n ";
					RowR5Equivalent+=RowRFree5Equivalent +" \\\\ \\hline \n ";
				
				}
				
				Table += Col + " \\\\ \\hline \n ";
				Table += RowCom0;
				Table=ShadedTable(FormatingPipelinesNames(Table,true,true))+" \n "+Comments;
				new Preparer().WriteTxtFile(PathToLatexFolder+"/MatrixOfResults" + Folder.getName() + "Com0.tex", Table);
			
				String TableCom5 = Col + "\\\\ \\hline \n ";
				TableCom5 += RowCom5;
				TableCom5=ShadedTable(FormatingPipelinesNames(TableCom5,true,true))+" \n "+Comments; 
				new Preparer().WriteTxtFile(PathToLatexFolder+"/MatrixOfResults" + Folder.getName() + "Com5.tex", TableCom5);
				
				String TableCom0Equivalent = Col + "\\\\ \\hline \n ";
				TableCom0Equivalent += RowCom0Equivalent;
				TableCom0Equivalent=	ShadedTable(FormatingPipelinesNames(TableCom0Equivalent,true,true)) +" \n "+Comments;
				new Preparer().WriteTxtFile(PathToLatexFolder+"/MatrixOfResults" + Folder.getName() + "Com0Equivalent.tex", TableCom0Equivalent);
				
				String TableCom5Equivalent = Col + "\\\\ \\hline \n ";
				TableCom5Equivalent += RowCom5Equivalent;
				TableCom5Equivalent=	ShadedTable(FormatingPipelinesNames(TableCom5Equivalent,true,true)) +" \n "+Comments;
				new Preparer().WriteTxtFile(PathToLatexFolder+"/MatrixOfResults" + Folder.getName() + "Com5Equivalent.tex", TableCom5Equivalent);
			
				String TableCom0Models = Col + "\\\\ \\hline \n ";
				TableCom0Models += RowCom0Models;
				TableCom0Models=ShadedTable(FormatingPipelinesNames(TableCom0Models,true,true)) +" \n "+Comments;
				new Preparer().WriteTxtFile(PathToLatexFolder+"/MatrixOfResults" + Folder.getName() + "Com0Models.tex", TableCom0Models);
				
				
				String TableCom5Models = Col + "\\\\ \\hline \n ";
				TableCom5Models += RowCom5Models;
				TableCom5Models=ShadedTable(FormatingPipelinesNames(TableCom5Models,true,true)) +" \n "+Comments;
				new Preparer().WriteTxtFile(PathToLatexFolder+"/MatrixOfResults" + Folder.getName() + "Com5Models.tex", TableCom5Models);
				
				
				String TableRModels = Col + "\\\\ \\hline \n ";
				TableRModels += RowR0;
				TableRModels=ShadedTable(FormatingPipelinesNames(TableRModels,true,true)) +" \n "+Comments;
				new Preparer().WriteTxtFile(PathToLatexFolder+"/MatrixOfResults" + Folder.getName() + "RModels.tex", TableRModels);
				
				
				String TableRModelsEquivalent = Col + "\\\\ \\hline \n ";
				TableRModelsEquivalent += RowR0Equivalent;
				TableRModelsEquivalent=ShadedTable(FormatingPipelinesNames(TableRModelsEquivalent,true,true)) +" \n "+Comments;
				new Preparer().WriteTxtFile(PathToLatexFolder+"/MatrixOfResults" + Folder.getName() + "REquivalentModels.tex", TableRModelsEquivalent);
			
			
				String TableR5Models = Col + "\\\\ \\hline \n ";
				TableR5Models += RowR5;
				TableR5Models=ShadedTable(FormatingPipelinesNames(TableR5Models,true,true)) +" \n "+Comments;
				new Preparer().WriteTxtFile(PathToLatexFolder+"/MatrixOfResults" + Folder.getName() + "R5Models.tex", TableR5Models);
			
			
				String TableRModelsEquivalent5 = Col + "\\\\ \\hline \n ";
				TableRModelsEquivalent5 += RowR5Equivalent;
				TableRModelsEquivalent5=ShadedTable(FormatingPipelinesNames(TableRModelsEquivalent5,true,true)) +" \n "+Comments;
				new Preparer().WriteTxtFile(PathToLatexFolder+"/MatrixOfResults" + Folder.getName() + "REquivalent5Models.tex", TableRModelsEquivalent5);
				new Preparer().WriteTxtFile(PathToLatexFolder+"/"+Folder.getName()+"CSVCompareTo.csv",FormatingPipelinesNames(CSVCompareTo,true,false));
				
				new RunComparison().CheckDirAndFile("MatricesLogs");
				new Preparer().WriteTxtFile("MatricesLogs/" + Folder.getName() + "LogOfR5Equivalent.txt", FormatingPipelinesNames(LogOfR5Equivalent,false,true));
				
				new Preparer().WriteTxtFile("MatricesLogs/" + Folder.getName() + "LogOfCom0.txt", FormatingPipelinesNames(LogOfCom0,false,true));

				new Preparer().WriteTxtFile("MatricesLogs/" + Folder.getName() + "LogOfCom0Equivalent.txt", FormatingPipelinesNames(LogOfCom0Equivalent,false,true));
				
				new Preparer().WriteTxtFile("MatricesLogs/" + Folder.getName() + "LogOfCom5.txt", FormatingPipelinesNames(LogOfCom5,false,true));
				new Preparer().WriteTxtFile("MatricesLogs/" + Folder.getName() + "LogOfCom5Equivalent.txt", FormatingPipelinesNames(LogOfCom5Equivalent,false,true));
				new Preparer().WriteTxtFile("MatricesLogs/" + Folder.getName() + "LogOfR0.txt", FormatingPipelinesNames(LogOfR0,false,true));
				new Preparer().WriteTxtFile("MatricesLogs/" + Folder.getName() + "LogOfRFree0.txt", FormatingPipelinesNames(LogOfRFree0,false,true));

					  
				
				new Preparer().WriteTxtFile("MatricesLogs/" + Folder.getName() + "LogOfR0Equivalent.txt", FormatingPipelinesNames(LogOfR0Equivalent,false,true));
						new Preparer().WriteTxtFile("MatricesLogs/" + Folder.getName() + "LogOfRFree0Equivalent.txt", FormatingPipelinesNames(LogOfRFree0Equivalent,false,true));

						new Preparer().WriteTxtFile("MatricesLogs/" + Folder.getName() + "LogOfR5.txt", FormatingPipelinesNames(LogOfR5,false,true));
	
						
						new Preparer().WriteTxtFile("MatricesLogs/" + Folder.getName() + "LogOfRFree5.txt", FormatingPipelinesNames(LogOfRFree5,false,true));
						
						new Preparer().WriteTxtFile("MatricesLogs/" + Folder.getName() + "LogOfRFree5Equivalent.txt", FormatingPipelinesNames(LogOfRFree5Equivalent,false,true));

			}

		}

	}
	
	
	
	
	String AddingAvgToTheTable(String Table) {
		if(AvgInMatrices==false)
			return Table;
		
		 String a = Table;
		 String [] Lines= a.split("\n");
		 Vector<String> LinesAvg = new Vector<String>(); 
		 int i=0;
		
		 for(String Line : Lines) {
			
			 double sumofTheLine=0;
			 double NumberOfPipelien=0;
			 if(i!=0 && Line.trim().length()!=0) {
			 for(String Number : Line.split(" ")) {
				 if(Number.matches("[0-9]+")){
					 sumofTheLine+= Integer.valueOf(Number);
					 NumberOfPipelien++;
				 }
			 }
			 if(sumofTheLine!=0)
			 LinesAvg.add(String.valueOf(Math.round((sumofTheLine/(NumberOfPipelien-1))))); // -1 to exclude the compared pipeline 
			 if(sumofTheLine==0)
			 LinesAvg.add("-");

			 }
			 ++i;
		 }
		 i=0;
		 String FormattedTable="";
		 int IndexOFAvg=0;
		 for(String Line : Lines) {
			 if(Line.trim().length()!=0) {
				 Line=Line.replaceAll("\\\\\\\\", "");
				 boolean ContainsHline=false;
				 if(Line.contains("\\hline"))
					 ContainsHline=true;
				 Line=Line.replaceAll("\\\\hline", "");
			 if(i==0) {
				 if(ContainsHline==true) {
				 FormattedTable+=Line+"& \\tiny Avg. \\\\ \\hline \n"; 
				 }
				 else {
					 FormattedTable+=Line+"& \\tiny Avg. \\\\  \n"; 
				 }
			 }
			 else {
				 if(ContainsHline==true) {
				 FormattedTable+=Line+"& \\tiny "+LinesAvg.get(IndexOFAvg) +" \\\\ \\hline \n"; }
				 else {
					 FormattedTable+=Line+"& \\tiny "+LinesAvg.get(IndexOFAvg) +" \\\\ \n";  }
				 IndexOFAvg++;
			 }
			 
			 }
			 ++i;
			 
		 }
		 return FormattedTable;
	}
	 String ShadedTable (String Table) {
		// TODO Auto-generated method stub
		
		 Table=AddingAvgToTheTable(Table);
		 

String a = Table;


String [] numbers= a.split(" ");
int Sum=0;
int NumberCount=0;
for(String n : numbers) {
	if(n.matches("[0-9]+")) {
	
	Sum+=Integer.valueOf(n);
	NumberCount++;
	}
}
String [] Elements= a.split(" ");
String FormattedTable="";
for(String e : Elements) {
	if(e.matches("[0-9]+") && Integer.valueOf(e)  == Math.round(Sum/NumberCount)) {
		
		FormattedTable+=" \\tiny "+e+" ";
		
	}
	else if(e.matches("[0-9]+") && Integer.valueOf(e)  > Math.round(Sum/NumberCount)) {
	
	FormattedTable+=" \\tiny "+e+" ";
		
	
}
else {
	if(!e.matches("[0-9]+"))
	FormattedTable+=e+" ";
	else
		FormattedTable+=" \\tiny "+e+" ";
}
}

return FormattedTable;


	}
	public String FormatingPipelinesNames(String Table, boolean RemoveDatasetNames, boolean latexSyntax) {
	
		if(RemoveDatasetNames==true) {
		Table=Table.replaceAll("hancs", "");
		Table=Table.replaceAll("mrncs", "");
		Table=Table.replaceAll("noncs", "");
		}
		
		Table=Table.replaceAll(".xlsx", "");
		
		if(RunningParameter.ReplaceToSingleLetter.equals("F")) {
		Table=Table.replaceAll("\\bArpWArpAfterBuccaneeri1\\b", "ARP(B 25I)");
		Table=Table.replaceAll("\\bArpWArpAfterBuccaneeri1I5\\b", "ARP(B 5I)");
		Table=Table.replaceAll("\\bARPwARP\\b", "ARP");
		Table=Table.replaceAll("\\bPhenix\\b", "PHENIX/Parrot");
		Table=Table.replaceAll("\\bPhenixHAL\\b", "PHENIX");
		Table=Table.replaceAll("\\bShelxeWithTFlagChFomPhi\\b", "SHELXE/Parrot");
		Table=Table.replaceAll("\\bShelxeWithTFlag\\b", "SHELXE");
		Table=Table.replaceAll("\\bBuccaneeri1\\b", "i1(25I)");
		Table=Table.replaceAll("\\bBuccaneeri1I5\\b", "i1(5I)");
	}
		
		
		if(RunningParameter.ReplaceToSingleLetter.equals("T")) {
			Table=Table.replaceAll("\\bArpWArpAfterBuccaneeri1\\b", "A(B 25I)");
			Table=Table.replaceAll("\\bArpWArpAfterBuccaneeri1I5\\b", "A(B 5I)");
			Table=Table.replaceAll("\\bARPwARP\\b", "A");
			
			Table=Table.replaceAll("\\bPhenixHAL\\b", "P");
			
			Table=Table.replaceAll("\\bShelxeWithTFlag\\b", "S");
			Table=Table.replaceAll("\\bBuccaneeri1I5\\b", "B");
			Table=Table.replaceAll("\\bBuccaneeri1\\b", "B(25I)");
			
			if(latexSyntax==true) {
				Table=Table.replaceAll("\\bPhenix\\b", "\\$P^{\\\\ast}\\$ ");
				Table=Table.replaceAll("\\bShelxeWithTFlagChFomPhi\\b", "\\$S^{\\\\ast}\\$ ");
			}
			if(latexSyntax==false) {
				Table=Table.replaceAll("\\bPhenix\\b", "P*");
				Table=Table.replaceAll("\\bShelxeWithTFlagChFomPhi\\b", "S*");
			}
		}	
		
		
		
		
		
		Table=Table.replaceAll("\\bBuccaneeri2\\b", "i2(25I)");
		
		
		
		Table=Table.replaceAll("\\bBuccaneeri2I5\\b", "i2(5I)");
		
		Table=Table.replaceAll("\\bBuccaneeri2W\\b", "i2W(25I)");
		
		Table=Table.replaceAll("\\bBuccaneeri2WI5\\b", "i2W(5I)");
		Table=Table.replaceAll("\\bBuccaneeri1I1GA\\b", "i1(I1GA)");
		Table=Table.replaceAll("\\bBuccaneeri1I2GA\\b", "i1(I2GA)");
		Table=Table.replaceAll("\\bBuccaneeri1I3GA\\b", "i1(I3GA)");
		Table=Table.replaceAll("\\bBuccaneeri1I4GA\\b", "i1(I4GA)");
		Table=Table.replaceAll("\\bBuccaneeri1I5GA\\b", "i1(I5GA)");
		
		if(latexSyntax==true) {
		Table=Table.replaceAll("\\bArpPhenix\\b", "A\\$\\\\rightarrow\\$\\$P^{\\\\ast}\\$ ");
		Table=Table.replaceAll("\\bArpPhenixHLA\\b", "A\\$\\\\rightarrow\\$\\$P\\$ ");
		Table=Table.replaceAll("\\bArpWArpBuccaneeri1I5\\b", "A\\$\\\\rightarrow\\$\\$B\\$ ");
		Table=Table.replaceAll("\\bBuccaneeri1I5Phenix\\b", "B\\$\\\\rightarrow\\$\\$P^{\\\\ast}\\$ ");
		Table=Table.replaceAll("\\bBuccaneeri1I5PhenixHLA\\b", "B\\$\\\\rightarrow\\$\\$P\\$ ");
		Table=Table.replaceAll("\\bPhenixArp\\b", "\\$P^{\\\\ast}\\$\\\\rightarrow\\$A\\$ ");
		Table=Table.replaceAll("\\bPhenixBuccaneeri1I5\\b", "\\$P^{\\\\ast}\\$\\\\rightarrow\\$B\\$ ");
		Table=Table.replaceAll("\\bPhenixHLAArp\\b", "P\\$\\\\rightarrow\\$\\$A\\$ ");
		Table=Table.replaceAll("\\bPhenixHLABuccaneeri1I5\\b", "P\\$\\\\rightarrow\\$\\$B\\$ ");
		Table=Table.replaceAll("\\bShelxeWithTFlagArp\\b", "S\\$\\\\rightarrow\\$\\$A\\$ ");
		Table=Table.replaceAll("\\bShelxeWithTFlagBuccaneeri1I5\\b", "S\\$\\\\rightarrow\\$\\$B\\$ ");
		Table=Table.replaceAll("\\bShelxeWithTFlagChFomPhiArp\\b", "\\$S^{\\\\ast}\\$\\\\rightarrow\\$A\\$ ");
		Table=Table.replaceAll("\\bShelxeWithTFlagChFomPhiBuccaneeri1I5\\b", "\\$S^{\\\\ast}\\$\\\\rightarrow\\$B\\$ ");
		Table=Table.replaceAll("\\bShelxeWithTFlagChFomPhiPhenix\\b", "\\$S^{\\\\ast}\\$\\\\rightarrow\\$P^{\\\\ast}\\$ ");
		Table=Table.replaceAll("\\bShelxeWithTFlagChFomPhiPhenixHLA\\b", "\\$S^{\\\\ast}\\$\\\\rightarrow\\$P\\$ ");
		Table=Table.replaceAll("\\bShelxeWithTFlagPhenix\\b", "S\\\\rightarrow\\$P^{\\\\ast}\\$ ");
		//Table=Table.replaceAll("\\bShelxeWithTFlagChFomPhiPhenixHLA\\b", "S^{\\ast}\\rightarrow$P$");
		Table=Table.replaceAll("\\bShelxeWithTFlagPhenix\\b", "S\\$\\\\rightarrow\\$\\$P^{\\\\ast}\\$ ");
		Table=Table.replaceAll("\\bShelxeWithTFlagPhenixHLA\\b", "S\\$\\\\rightarrow\\$\\$P\\$ ");
	}
		
		if(latexSyntax==false) {
		Table=Table.replaceAll("\\bArpPhenix\\b", "A→P* ");
		Table=Table.replaceAll("\\bArpPhenixHLA\\b", "A→P ");
		Table=Table.replaceAll("\\bArpWArpBuccaneeri1I5\\b", "A→B ");
		Table=Table.replaceAll("\\bBuccaneeri1I5Phenix\\b", "B→P* ");
		Table=Table.replaceAll("\\bBuccaneeri1I5PhenixHLA\\b", "B→P ");
		Table=Table.replaceAll("\\bPhenixArp\\b", "P*→A ");
		Table=Table.replaceAll("\\bPhenixBuccaneeri1I5\\b", "P*→B ");
		Table=Table.replaceAll("\\bPhenixHLAArp\\b", "P→A ");
		Table=Table.replaceAll("\\bPhenixHLABuccaneeri1I5\\b", "P→B ");
		Table=Table.replaceAll("\\bShelxeWithTFlagArp\\b", "S→A ");
		Table=Table.replaceAll("\\bShelxeWithTFlagBuccaneeri1I5\\b", "S→B ");
		Table=Table.replaceAll("\\bShelxeWithTFlagChFomPhiArp\\b", "S*→A ");
		Table=Table.replaceAll("\\bShelxeWithTFlagChFomPhiBuccaneeri1I5\\b", "S*→B ");
		Table=Table.replaceAll("\\bShelxeWithTFlagChFomPhiPhenix\\b", "S*→P* ");
		Table=Table.replaceAll("\\bShelxeWithTFlagChFomPhiPhenixHLA\\b", "S*→P ");
		Table=Table.replaceAll("\\bShelxeWithTFlagPhenix\\b", "S→P* ");
		//Table=Table.replaceAll("\\bShelxeWithTFlagChFomPhiPhenixHLA\\b", "S^{\\ast}\\rightarrow$P$");
		Table=Table.replaceAll("\\bShelxeWithTFlagPhenix\\b", "S→P* ");
		Table=Table.replaceAll("\\bShelxeWithTFlagPhenixHLA\\b", "S→P ");
	}
		
		return Table;
	}

	void LongMatrixOfResults(String ResultsDir) throws IOException {
		File[] Folders = new File(ResultsDir).listFiles();
		
		DecimalFormat decim = new DecimalFormat("#.##");
		
		Vector<Vector<Vector<ExcelContents>>> AllDatasetContainer = new Vector<Vector<Vector<ExcelContents>>>();
		Vector<String> DatasetNames = new Vector<String>();
		Vector<ExcelLoader> ExcelNames = new Vector<ExcelLoader>();
		Vector<String> ExcelNamesAsInTableHeader = new Vector<String>();
		String TableHeader = "\\tiny Pipelines && ";
		String ImprovemedBy = "\\tiny Improvemed By & ";

		for (File Folder : Folders) {

			if (Folder.isDirectory() && !Folder.getName().equals("Headers")) {

				ExcelLoader e = new ExcelLoader();

				Vector<Vector<ExcelContents>> CurrentReading = new Vector<Vector<ExcelContents>>();
				for (File Excel : Folder.listFiles()) {
					
					CurrentReading.add(e.ReadExcel(Excel.getAbsolutePath()));
					e.ToolsNames.add(Excel.getName());

				}
for(int i=0; i < CurrentReading.size() ; ++i) {
	for(int c=0; c < CurrentReading.get(i).size() ; ++c) {
		if(CurrentReading.get(i).get(c).R_factor0Cycle.equals("None"))
			CurrentReading.get(i).get(c).BuiltPDB="F";
	}
}
				AllDatasetContainer.add(CurrentReading);
				ExcelNames.add(e);// Saving excel names
				DatasetNames.add(Folder.getName()); // Saving dataset name in case there is more than one
			}
			if (Folder.isDirectory() && Folder.getName().equals("Headers")) {
				for (File Excel : Folder.listFiles()) {
					ExcelNamesAsInTableHeader.add(Excel.getName());
					TableHeader += " \\multicolumn{2}{c}{ \\tiny " + Excel.getName() + "} &";
					ImprovemedBy += " & \\tiny 0\\% &  \\tiny 5\\% ";
				}
			}
		}

		

		// Writing the table header

		Vector<String> ExcelNamesAsInTableRows = new Vector<String>();
		for (int i = 0; i < ExcelNames.size(); ++i) {

			for (int n = 0; n < ExcelNames.get(i).ToolsNames.size(); ++n) {
				if (!TableHeader.contains(ExcelNames.get(i).ToolsNames.get(n))) {

					/*
					 * if(ExcelNames.get(i).ToolsNames.get(n).equals("ARPwARP.xlsx") || // Change
					 * this to change the table header and change the pipelines that you compare
					 * with. ExcelNames.get(i).ToolsNames.get(n).equals("Buccaneer5C.xlsx") ||
					 * ExcelNames.get(i).ToolsNames.get(n).equals("Buccaneeri2WC5.xlsx") ||
					 * ExcelNames.get(i).ToolsNames.get(n).equals("Buccaneeri2C5.xlsx") ||
					 * ExcelNames.get(i).ToolsNames.get(n).equals("Phenix.xlsx") ) {
					 * TableHeader+=" \\multicolumn{2}{c}{ \\tiny "+
					 * ExcelNames.get(i).ToolsNames.get(n) +"} &";
					 * ImprovemedBy+=" & \\tiny 0\\% &  \\tiny 5\\% ";
					 * ExcelNamesAsInTableHeader.add(ExcelNames.get(i).ToolsNames.get(n));
					 * 
					 * }
					 */
				}
				if (!ExcelNamesAsInTableRows.contains(ExcelNames.get(i).ToolsNames.get(n)))
					ExcelNamesAsInTableRows.add(ExcelNames.get(i).ToolsNames.get(n));
			}
		}
		
		TableHeader += " \\multicolumn{2}{c}{ \\tiny All(Built)} \\\\ \n " + ImprovemedBy
				+ "  & \\tiny 0\\% &  \\tiny 5\\% \\\\ \\hline ";

		
		String Rows = TableHeader + "\n";
		String RowsRFactor = TableHeader + "\n";
		
		for (int i = 0; i < ExcelNamesAsInTableRows.size(); ++i) { // looping on pipelines
			Rows += " \\tiny " + ExcelNamesAsInTableRows.get(i);// Pipeline name
			RowsRFactor += " \\tiny " + ExcelNamesAsInTableRows.get(i);
			for (int d = 0; d < DatasetNames.size(); ++d) { // looping on datasets
				if (ExcelNames.get(d).ToolsNames.contains(ExcelNamesAsInTableRows.get(i))) {
					
					String ZeroPercentRow = " & \\tiny " + DatasetNames.get(d);
					String RowRFactor = " & \\tiny " + DatasetNames.get(d);

					
					String FivePercentRow = " & \\tiny " + DatasetNames.get(d) + " 5\\%";

					for (int e = 0; e < ExcelNamesAsInTableHeader.size(); ++e) {
						int IndexForTheRowContainer = -1;
						int IndexForTheHeaderContainer = -1;
						for (int c = 0; c < AllDatasetContainer.get(d).size(); ++c) {// Finding the index for the
																						// containers
							if (ExcelNames.get(d).ToolsNames.get(c).equals(ExcelNamesAsInTableHeader.get(e))) {
								IndexForTheHeaderContainer = c;
							}
							if (ExcelNames.get(d).ToolsNames.get(c).equals(ExcelNamesAsInTableRows.get(i))) {
								IndexForTheRowContainer = c;
							}

						}

						int CountZeroPrecentge = 0;
						int CountFivePrecentge = 0;
						
						int CountZeroPrecentgeRFactor = 0; // to use in Rafctor matrix
						int CountFivePrecentgeRFactor = 0;// to use in Rafctor matrix

						if (IndexForTheHeaderContainer != -1 && IndexForTheRowContainer != -1
								&& IndexForTheRowContainer != IndexForTheHeaderContainer)// This Excel not found in this
																							// dataset also do not
																							// compare the same excel
							for (int c = 0; c < AllDatasetContainer.get(d).get(IndexForTheRowContainer).size(); ++c) {
								for (int compareTo = 0; compareTo < AllDatasetContainer.get(d)
										.get(IndexForTheHeaderContainer).size(); ++compareTo) {
									if (AllDatasetContainer.get(d).get(IndexForTheRowContainer).get(c).BuiltPDB
											.equals("T")
											&& AllDatasetContainer.get(d).get(IndexForTheHeaderContainer)
													.get(compareTo).BuiltPDB.equals("T"))
										if (AllDatasetContainer.get(d).get(IndexForTheRowContainer).get(c).PDB_ID
												.equals(AllDatasetContainer.get(d).get(IndexForTheHeaderContainer)
														.get(compareTo).PDB_ID)) {

											
											if (Integer.parseInt(AllDatasetContainer.get(d).get(IndexForTheRowContainer)
													.get(c).Completeness) >= Integer.parseInt(
															AllDatasetContainer.get(d).get(IndexForTheHeaderContainer)
																	.get(compareTo).Completeness)) 
												{
													
													CountZeroPrecentge++;
												}
											
											else {
												//System.out.println("First "+AllDatasetContainer.get(d).get(IndexForTheRowContainer)
												//	.get(c).Completeness);
												//System.out.println("Second "+AllDatasetContainer.get(d).get(IndexForTheHeaderContainer)
												//				.get(compareTo).Completeness);
											}
												if ((Integer
														.parseInt(AllDatasetContainer.get(d)
																.get(IndexForTheRowContainer).get(c).Completeness)
														- Integer.parseInt(AllDatasetContainer.get(d)
																.get(IndexForTheHeaderContainer)
																.get(compareTo).Completeness)) >= 5) {
													
														
														CountFivePrecentge++;
													}
													if (Double.parseDouble(
															AllDatasetContainer.get(d).get(IndexForTheRowContainer)
																	.get(c).R_factor0Cycle) <= Double
																			.parseDouble(AllDatasetContainer.get(d)
																					.get(IndexForTheHeaderContainer)
																					.get(compareTo).R_factor0Cycle) )

													{
														
														
														double FirstPDB = 
																 Double.parseDouble(AllDatasetContainer.get(d)
																		.get(IndexForTheRowContainer)
																		.get(c).R_free0Cycle) -Double.parseDouble(AllDatasetContainer.get(d)
																				.get(IndexForTheRowContainer).get(c).R_factor0Cycle);
														
														
														double SecondPDB = 
																 Double.parseDouble(AllDatasetContainer.get(d)
																		.get(IndexForTheHeaderContainer)
																		.get(compareTo).R_free0Cycle) - Double
																				.parseDouble(AllDatasetContainer.get(d)
																						.get(IndexForTheHeaderContainer)
																						.get(compareTo).R_factor0Cycle);
														FirstPDB = Double.parseDouble(decim.format(FirstPDB));
														SecondPDB = Double.parseDouble(decim.format(SecondPDB));

														if (FirstPDB <= 0.05)
															CountZeroPrecentgeRFactor++;
														
													}

													if ((Double
															.parseDouble(AllDatasetContainer.get(d)
																	.get(IndexForTheRowContainer).get(c).R_factor0Cycle)
															- Double.parseDouble(AllDatasetContainer.get(d)
																	.get(IndexForTheHeaderContainer)
																	.get(compareTo).R_factor0Cycle)) <= -0.05

													)

													{
														double FirstPDB = 
																 Double.parseDouble(AllDatasetContainer.get(d)
																		.get(IndexForTheRowContainer)
																		.get(c).R_free0Cycle) -Double.parseDouble(AllDatasetContainer.get(d)
																				.get(IndexForTheRowContainer).get(c).R_factor0Cycle);
														
														
														double SecondPDB = 
																Double.parseDouble(AllDatasetContainer.get(d)
																		.get(IndexForTheHeaderContainer)
																		.get(compareTo).R_free0Cycle) -Double
																.parseDouble(AllDatasetContainer.get(d)
																		.get(IndexForTheHeaderContainer)
																		.get(compareTo).R_factor0Cycle);
														
														FirstPDB = Double.parseDouble(decim.format(FirstPDB));
														SecondPDB = Double.parseDouble(decim.format(SecondPDB));
														if (FirstPDB <= 0.05)
														CountFivePrecentgeRFactor++;
													}

												
											
										}

								}
							}

						if (IndexForTheRowContainer != -1) {
							
							
							ZeroPercentRow += " & \\tiny " + ((CountZeroPrecentge * 100)
									/ AllDatasetContainer.get(d).get(IndexForTheRowContainer).size());
						
							
							ZeroPercentRow += " & \\tiny " + ((CountFivePrecentge * 100)
									/ AllDatasetContainer.get(d).get(IndexForTheRowContainer).size());

							RowRFactor += " & \\tiny " + ((CountZeroPrecentgeRFactor * 100)
									/ AllDatasetContainer.get(d).get(IndexForTheRowContainer).size());
							RowRFactor += " & \\tiny " + ((CountFivePrecentgeRFactor * 100)
									/ AllDatasetContainer.get(d).get(IndexForTheRowContainer).size());

							
						} else {

							ZeroPercentRow += " & \\tiny " + CountZeroPrecentge;
							ZeroPercentRow += " & \\tiny " + CountFivePrecentge;

							RowRFactor += " & \\tiny " + CountZeroPrecentgeRFactor;
							RowRFactor += " & \\tiny " + CountFivePrecentgeRFactor;
							
						}
					}
					int CountZeroPrecentgeAll = 0;
					int CountFivePrecentgeAll = 0;

					int CountZeroPrecentgeAllRFactor = 0;
					int CountFivePrecentgeAllRFactor = 0;
					for (int c = 0; c < AllDatasetContainer.get(d).get(i).size(); ++c) {
						
						

						

						Vector<Boolean> GreaterThanZeroPercent = new Vector<Boolean>();
						Vector<Boolean> GreaterThanZeroFivePercent = new Vector<Boolean>();
						
						Vector<Boolean> GreaterThanZeroPercentRFactor = new Vector<Boolean>();
						Vector<Boolean> GreaterThanZeroFivePercentRFactor = new Vector<Boolean>();
						int CountHowManyPipelineInTheHeaderWeComapreWith = 0;
						for (int all = 0; all < AllDatasetContainer.get(d).size(); ++all) {
							if (ExcelNamesAsInTableHeader.contains(ExcelNames.get(d).ToolsNames.get(all))
									&& !ExcelNames.get(d).ToolsNames.get(all)
											.equals(ExcelNames.get(d).ToolsNames.get(i))) {
								CountHowManyPipelineInTheHeaderWeComapreWith++;
							}
						}
						for (int all = 0; all < AllDatasetContainer.get(d).size(); ++all) {

							
							if (ExcelNamesAsInTableHeader.contains(ExcelNames.get(d).ToolsNames.get(all))
									&& !ExcelNames.get(d).ToolsNames.get(all)
											.equals(ExcelNames.get(d).ToolsNames.get(i)))
								for (int Excel = 0; Excel < AllDatasetContainer.get(d).get(all).size(); ++Excel) {
									if (AllDatasetContainer.get(d).get(i).get(c).BuiltPDB.equals("T")
											&& AllDatasetContainer.get(d).get(all).get(Excel).BuiltPDB.equals("T"))
										if (AllDatasetContainer.get(d).get(i).get(c).PDB_ID
												.equals(AllDatasetContainer.get(d).get(all).get(Excel).PDB_ID)) {

											if (Integer.parseInt(
													AllDatasetContainer.get(d).get(i).get(c).Completeness) >= Integer
															.parseInt(AllDatasetContainer.get(d).get(all)
																	.get(Excel).Completeness)) {

												
												GreaterThanZeroPercent.add(true);
												
											}
											

											if (((Integer
													.parseInt(AllDatasetContainer.get(d).get(i).get(c).Completeness)
													- Integer.parseInt(AllDatasetContainer.get(d).get(all)
															.get(Excel).Completeness)) >= 5)) {

												GreaterThanZeroFivePercent.add(true);
												
											}
											

											if (i != all && Double.parseDouble(
													AllDatasetContainer.get(d).get(i).get(c).R_factor0Cycle) <= Double
															.parseDouble(AllDatasetContainer.get(d).get(all)
																	.get(Excel).R_factor0Cycle))
													 {

												double FirstPDB=Double.parseDouble(AllDatasetContainer.get(d).get(i).get(c).R_free0Cycle) - Double.parseDouble(AllDatasetContainer.get(d).get(i).get(c).R_factor0Cycle); 
												double SecondPDB=Double.parseDouble(AllDatasetContainer.get(d).get(all).get(Excel).R_free0Cycle) - Double.parseDouble(AllDatasetContainer.get(d).get(all).get(Excel).R_factor0Cycle);
												FirstPDB = Double.parseDouble(decim.format(FirstPDB));
												SecondPDB = Double.parseDouble(decim.format(SecondPDB));
												if(FirstPDB <= 0.05 )
													GreaterThanZeroPercentRFactor.add(true);
												

											}

											if (i != all && ((Double.parseDouble(
													AllDatasetContainer.get(d).get(i).get(c).R_factor0Cycle)
													- Double.parseDouble(AllDatasetContainer.get(d).get(all)
															.get(Excel).R_factor0Cycle)) <= -0.05)) {
												
												double FirstPDB=Double.parseDouble(AllDatasetContainer.get(d).get(i).get(c).R_free0Cycle) - Double.parseDouble(AllDatasetContainer.get(d).get(i).get(c).R_factor0Cycle); 
												double SecondPDB=Double.parseDouble(AllDatasetContainer.get(d).get(all).get(Excel).R_free0Cycle) - Double.parseDouble(AllDatasetContainer.get(d).get(all).get(Excel).R_factor0Cycle);
												FirstPDB = Double.parseDouble(decim.format(FirstPDB));
												SecondPDB = Double.parseDouble(decim.format(SecondPDB));
												
													if(FirstPDB <= 0.05 )
													GreaterThanZeroFivePercentRFactor.add(true);

											}

										}
								}
						}

						if (CountHowManyPipelineInTheHeaderWeComapreWith == GreaterThanZeroPercent.size()
								&& GreaterThanZeroPercent.size() != 0) {
							
							CountZeroPrecentgeAll++;
						}
						if (CountHowManyPipelineInTheHeaderWeComapreWith == GreaterThanZeroFivePercent.size()
								&& GreaterThanZeroFivePercent.size() != 0) {
							
							CountFivePrecentgeAll++;
						}

						
						if (CountHowManyPipelineInTheHeaderWeComapreWith == GreaterThanZeroPercentRFactor.size()
								&& GreaterThanZeroPercentRFactor.size() != 0) {
							
							CountZeroPrecentgeAllRFactor++;
						}
						if (CountHowManyPipelineInTheHeaderWeComapreWith == GreaterThanZeroFivePercentRFactor.size()
								&& GreaterThanZeroFivePercentRFactor.size() != 0) {
							
							CountFivePrecentgeAllRFactor++;
						}
						
						
					}

					ZeroPercentRow += " & \\tiny "
							+ ((CountZeroPrecentgeAll * 100) / AllDatasetContainer.get(d).get(i).size());
					ZeroPercentRow += " & \\tiny "
							+ ((CountFivePrecentgeAll * 100) / AllDatasetContainer.get(d).get(i).size());

					RowRFactor += " & \\tiny "
							+ ((CountZeroPrecentgeAllRFactor * 100) / AllDatasetContainer.get(d).get(i).size());
					RowRFactor += " & \\tiny "
							+ ((CountFivePrecentgeAllRFactor * 100) / AllDatasetContainer.get(d).get(i).size());
					

					Rows += ZeroPercentRow + " \\\\ \n";
					RowsRFactor += RowRFactor + " \\\\ \n";
					
				}
			}
			Rows += " \\hline \n";
			RowsRFactor += " \\hline \n";
		}

		
		new Preparer().WriteTxtFile(PathToLatexFolder+"/LongMatrixCompleteness" + ".tex", Rows);
		new Preparer().WriteTxtFile(PathToLatexFolder+"/LongMatrixRfactor" + ".tex", RowsRFactor);
	}
	
	public  void ImprovementsLevel(String DMDir) throws IOException {
		// TODO Auto-generated method stub

		File []  DMPath=new File(DMDir).listFiles();
		for(File DM : DMPath) {
			
		
		HashMap<Integer,Integer> Completenessimprovementslevels=new HashMap<Integer,Integer>(); 
		HashMap<Integer,Integer> Rworkimprovementslevels=new HashMap<Integer,Integer>();
		HashMap<Integer,Integer> Rfreeimprovementslevels=new HashMap<Integer,Integer>();
		Completenessimprovementslevels.put(1, 0);
		Completenessimprovementslevels.put(5, 0);
		
		Rworkimprovementslevels.put(1, 0);
		Rworkimprovementslevels.put(5, 0);
		
		Rfreeimprovementslevels.put(1, 0);
		Rfreeimprovementslevels.put(5, 0);
		for (int i =10 ; i < 60 ; i =i+10 ) {
			//System.out.println(i);
			//improvementslevels.add(i);
			Completenessimprovementslevels.put(i, 0);
			Rworkimprovementslevels.put(i, 0);
			Rfreeimprovementslevels.put(i, 0);
		}
		SortedSet<Integer> keys = new TreeSet<>(Completenessimprovementslevels.keySet());
		//File []  ExcelDir= new File("/Users/emadalharbi/Desktop/PhDYork/SecondYearReportV1/Excel/Metaoptimization/All/SyntheticBuccEx54ExFaliedCases/noncs").listFiles(); 
		File []  ExcelDir= new File(DM.getAbsolutePath()).listFiles(); 

		Vector<String> PipelinesNames = new Vector<String>(); 
		String CompletenessTable=" \\tiny "+"Improvement level &";
		String RTable=" \\tiny "+"Improvement level &";
		String Logs="";
		//Read pipelines names
		//System.out.println(DM.getAbsolutePath());
		for(int i=0 ; i <ExcelDir.length ; ++i) {
			PipelinesNames.add(ExcelDir[i].getName());
			
		}
		// sort them and write to the table 
		Collections.sort(PipelinesNames);
		for (Integer key : keys) { 
			CompletenessTable+=" \\tiny "+key+"&";
		}
		CompletenessTable=CompletenessTable.substring(0, CompletenessTable.lastIndexOf("&"));
		CompletenessTable+=" \\\\ \\hline";
		CompletenessTable+="\n";
		RTable=CompletenessTable;
		for(int i=0 ; i <PipelinesNames.size() ; ++i) {
			File Pipeline1=null;
			for(File e :ExcelDir ) {
				if(PipelinesNames.get(i).equals(e.getName())) {
					Pipeline1=e;
					break;
				}
			}
			for(int p=0 ; p <PipelinesNames.size() ; ++p) {
				if(i!=p){
				File Pipeline2=null;
				for(File e :ExcelDir ) {
					if(PipelinesNames.get(p).equals(e.getName())) {
						Pipeline2=e;
						break;
					}
				}
				//Read the two excels 
				ExcelLoader f = new ExcelLoader();
				Vector<ExcelContents> Pipeline1Excel = f.ReadExcel(Pipeline1.getAbsolutePath());
				Vector<ExcelContents> Pipeline2Excel = f.ReadExcel(Pipeline2.getAbsolutePath());
				float CountModels=0;
				for(int m=0; m <Pipeline1Excel.size();++m ) {
					for(int n=0; n <Pipeline2Excel.size();++n) {
						if(Pipeline1Excel.get(m).PDB_ID.equals(Pipeline2Excel.get(n).PDB_ID)) {
							CountModels++;
							for(Entry<Integer, Integer> tempmap:Completenessimprovementslevels.entrySet()) {
								if (  new BigDecimal(Pipeline1Excel.get(m).Completeness).setScale(0, RoundingMode.HALF_UP).subtract( 
										new BigDecimal(Pipeline2Excel.get(n).Completeness).setScale(0, RoundingMode.HALF_UP)).compareTo(new BigDecimal(tempmap.getKey())) >=0)  {
								
							
									Completenessimprovementslevels.put(tempmap.getKey(), Completenessimprovementslevels.get(tempmap.getKey())+1);
									
												

									
						}
								
								if( new BigDecimal(Pipeline1Excel.get(m).R_free0Cycle).add(new BigDecimal( Double.toString(tempmap.getKey()/100.0))).compareTo(new BigDecimal(Pipeline2Excel.get(n).R_free0Cycle)) <= 0  ) {
									Logs+=PipelinesNames.get(i).substring(0,PipelinesNames.get(i).indexOf('.')) +" VS "+"  "+PipelinesNames.get(p).substring(0,PipelinesNames.get(p).indexOf('.')) +",";
									Logs+=Pipeline1Excel.get(m).PDB_ID+" "+" , "+tempmap.getKey()/100.0+", "+Pipeline1Excel.get(m).R_free0Cycle+", "+Pipeline2Excel.get(n).R_free0Cycle+"\n";
									//System.out.println("Level :"+tempmap.getKey()/100.0);
									//System.out.println("P1 :"+Pipeline1Excel.get(m).R_free0Cycle);
									//System.out.println("P2 :"+Pipeline2Excel.get(n).R_free0Cycle);
									Rfreeimprovementslevels.put(tempmap.getKey(), Rfreeimprovementslevels.get(tempmap.getKey())+1);
									
																	
								}
								if( new BigDecimal(Pipeline1Excel.get(m).R_factor0Cycle).add(new BigDecimal(Double.toString(tempmap.getKey()/100.0))).compareTo(new BigDecimal(Pipeline2Excel.get(n).R_factor0Cycle)) <= 0  ) {

									Rworkimprovementslevels.put(tempmap.getKey(), Rworkimprovementslevels.get(tempmap.getKey())+1);
									
																	
								}
							}
						}
					}
				}
				//System.out.println(PipelinesNames.get(i));
				//System.out.println(PipelinesNames.get(p));
				
				CompletenessTable+=" \\tiny "+PipelinesNames.get(i).substring(0,PipelinesNames.get(i).indexOf('.')) +" VS "+" \\tiny "+PipelinesNames.get(p).substring(0,PipelinesNames.get(p).indexOf('.'))+" ";
				CompletenessTable+="&";
				
				RTable+=" \\tiny "+PipelinesNames.get(i).substring(0,PipelinesNames.get(i).indexOf('.')) +" VS "+" \\tiny "+PipelinesNames.get(p).substring(0,PipelinesNames.get(p).indexOf('.'))+" $_{R-work}$";
				RTable+="&";
				
				
				for (Integer key : keys) { 
					DecimalFormat decim = new DecimalFormat("#");
					   //Table+=improvementslevels.get(key)+"&";
					CompletenessTable+=" \\tiny "+decim.format(((Completenessimprovementslevels.get(key) * 100) / CountModels)) +"&";
					RTable+=" \\tiny "+decim.format(((Rworkimprovementslevels.get(key) * 100) / CountModels)) +"&";
					//RTable+=" \\tiny "+Rworkimprovementslevels.get(key) +"&";

				}
				CompletenessTable=CompletenessTable.substring(0, CompletenessTable.lastIndexOf("&"));
				CompletenessTable+=" \\\\ \\hline";
				CompletenessTable+="\n";
				
				RTable=RTable.substring(0, RTable.lastIndexOf("&"));
				RTable+=" \\\\ ";
				RTable+="\n";
				RTable+=" \\tiny "+PipelinesNames.get(i).substring(0,PipelinesNames.get(i).indexOf('.')) +" VS "+" \\tiny "+PipelinesNames.get(p).substring(0,PipelinesNames.get(p).indexOf('.'))+" $_{R-free}$";
				RTable+="&";
				for (Integer key : keys) { 
					DecimalFormat decim = new DecimalFormat("#");
					  
					RTable+=" \\tiny "+decim.format(((Rfreeimprovementslevels.get(key) * 100) / CountModels)) +"&";
					//RTable+=" \\tiny "+Rfreeimprovementslevels.get(key)  +"&";
					Logs+= (Rfreeimprovementslevels.get(key) * 100) / CountModels+" \n ";
					Logs+= decim.format(((Rfreeimprovementslevels.get(key) * 100) / CountModels))+" \n ";
				}
				RTable=RTable.substring(0, RTable.lastIndexOf("&"));
				RTable+=" \\\\ \\hline";
				RTable+="\n";
				Logs+="models "+CountModels+" \n ";
				//System.out.println();
				for(Entry<Integer, Integer> tempmap:Completenessimprovementslevels.entrySet()) {
					Completenessimprovementslevels.put(tempmap.getKey(), 0);
					Rworkimprovementslevels.put(tempmap.getKey(), 0);
					Rfreeimprovementslevels.put(tempmap.getKey(), 0);
				}
			}
			}
		}
		CompletenessTable = new ComparisonMeasures().FormatingPipelinesNames(CompletenessTable, false,true);
		RTable = new ComparisonMeasures().FormatingPipelinesNames(RTable, false,true);

		//System.out.println(CompletenessTable);
		//System.out.println(RTable);
		Logs = new ComparisonMeasures().FormatingPipelinesNames(Logs, false,true);
		new Preparer().WriteTxtFile(DM.getName()+"Logs.txt",Logs);
		//new Preparer().WriteTxtFile(PathToLatexFolder+"/MatrixOfResults" + Folder.getName() + "Com0Equivalent.tex", TableCom0Equivalent);
		new Preparer().WriteTxtFile(PathToLatexFolder+"/"+DM.getName() + "CompletenessByImprovementsLevel.tex", CompletenessTable);
		new Preparer().WriteTxtFile(PathToLatexFolder+"/"+DM.getName() + "RTableByImprovementsLevel.tex", RTable);

	}
}
}

class OverallResult {
	String FileName;
	String DM;
	int Completed;
	int Intermediate;
	int Failed;
	int IntermediateTime;

	public OverallResult(String filename, String dm, int completed, int intermediate, int failed,
			int intermediateTime) {
		this.FileName = filename;
		this.DM = dm;
		this.Completed = completed;
		this.Intermediate = intermediate;
		this.Failed = failed;
		this.IntermediateTime = intermediateTime;
	}
}