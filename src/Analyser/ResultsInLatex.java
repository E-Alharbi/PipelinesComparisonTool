package Analyser;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
//import  org.apache.commons.lang3.StringUtils;

import org.apache.commons.lang3.math.NumberUtils;

import Run.Preparer;
import Run.RunComparison;
import Run.RunningPram;
import com.ibm.statistics.plugin.*;

public class ResultsInLatex {

	// Creating many types of tables in latex format for results representation

	public static void main(String[] args) throws IOException, StatsException {
		// TODO Auto-generated method stub
		
		/*
		 * File[] Excels = new File("/Users/emadalharbi/Desktop/test").listFiles();
		 * LoadExcel e = new LoadExcel(); Vector<Vector<DataContainer>> Container = new
		 * Vector<Vector<DataContainer>>(); for (File excel : Excels) {
		 * System.out.println(excel.getAbsolutePath());
		 * Container.add(e.ReadExcel(excel.getAbsolutePath()));
		 * 
		 * e.ToolsNames.add(excel.getName()); //
		 * Container.add(e.CheckPDBexists(e.Phenixx,e.ReadExcel( //
		 * excel.getAbsolutePath()))); }
		 */

		// GroupedByPhases=true;
		// String ExcelDir="/Volumes/PhDHardDrive/jcsg1200Results/ExcelSheets17";
		// String ExcelDir="/Volumes/PhDHardDrive/jcsg1200Results/GAResults/Ex5";

		String ExcelDir = "/Volumes/PhDHardDrive/jcsg1200Results/Fasta/Run6Bucc/BuccEx54";
		// String ExcelDir="/Volumes/PhDHardDrive/jcsg1200Results/ExcelSheets17";

		new RunComparison().CheckDirAndFile("CSV");
		new RunComparison().CheckDirAndFile("Latex");

		// new ResultsInCSV().PDBTable(Container);

		// new ResultsInLatex().OverallResults(ExcelDir);
		// new ResultsInLatex().PDBList(ExcelDir);
		// new ResultsInLatex().BestAndWorstCases(ExcelDir);
		// new ResultsInLatex().PrepareExcelForSpss(ExcelDir);
		// new ResultsInLatex().SpssBootstraping("SpssExcel");
		// new ResultsInLatex().ReadingSpssBootstraping("SpssExcelResults");
		 new ResultsInLatex().MatrixOfResults(ExcelDir);
		//new ResultsInLatex().LongMatrixOfResults(ExcelDir);

		// new ResultsInCSV().GroupByPhases(ExcelDir);
	}

	void PDBTable(Vector<Vector<DataContainer>> Container) throws IOException {
		// Sorting by Resolution
		Vector<String> PDB = new Vector<String>();
		for (int i = 0; i < Container.size(); ++i) {
			Collections.sort(Container.get(i), DataContainer.DataContainerComparator);
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

	void OverallResults(String ResultsDir) throws IOException {
		File[] Folders = new File(ResultsDir).listFiles();
		LoadExcel e = new LoadExcel();
		Vector<Vector<DataContainer>> Container = new Vector<Vector<DataContainer>>();
		for (File Folder : Folders) {
			if (Folder.isDirectory()) {
				for (File Excel : Folder.listFiles()) {
					Container.add(e.ReadExcel(Excel.getAbsolutePath()));
					e.ToolsNames.add(Excel.getName() + Folder.getName());
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
		System.out.println(CSV);
		new Preparer().WriteTxtFile("CSV/Overall.csv", CSV);
		Vector<String> CheckedFiles = new Vector<String>();
		String Table = "";
		for (int i = 0; i < Results.size(); ++i) {
			// System.out.println(Results.get(i).DM);
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
				// System.out.println(Results.get(i).FileName);
				// System.out.println(Results.get(i).DM);

				System.out.println("\\scriptsize " + Results.get(i).FileName + "&&" + hancs + "&&" + mrncs + "&&"
						+ noncs + "\\\\");
				Table += "\\scriptsize " + Results.get(i).FileName + "&&" + hancs + "&&" + mrncs + "&&" + noncs + "\\\\"
						+ "\n";
				;
			}
			CheckedFiles.add(Results.get(i).FileName);
		}
		new Preparer().WriteTxtFile("Latex/TheNumberOfCompletedCases.tex", Table.replace(".xlsx", ""));
	}

	void PDBList(String ResultsDir) throws IOException {
		File[] Folders = new File(ResultsDir).listFiles();
		LoadExcel e = new LoadExcel();
		Vector<Vector<DataContainer>> Container = new Vector<Vector<DataContainer>>();
		for (File Folder : Folders) {
			if (Folder.isDirectory()) {
				for (File Excel : Folder.listFiles()) {
					Container.add(e.ReadExcel(Excel.getAbsolutePath()));
					e.ToolsNames.add(Excel.getName() + Folder.getName());
				}
			}

		}
		Vector<String> CheckedFiles = new Vector<String>();
		Vector<DataContainer> PDBList = new Vector<DataContainer>();
		for (int i = 0; i < Container.size(); ++i) {

			for (int y = 0; y < Container.get(i).size(); ++y) {
				boolean Found = false;
				for (int f = 0; f < PDBList.size(); ++f) {
					if (PDBList.get(f).PDBIDTXT.equals(Container.get(i).get(y).PDB_ID.substring(0, 4))
							&& PDBList.get(f).Resolution.equals(Container.get(i).get(y).Resolution))
						Found = true;
				}
				if (Found == false && !Container.get(i).get(y).Resolution.equals("None")) {
					DataContainer DC = new DataContainer();
					DC.PDBIDTXT = Container.get(i).get(y).PDB_ID.substring(0, 4);
					DC.F_mapCorrelation = Container.get(i).get(y).F_mapCorrelation;
					DC.E_mapCorrelation = Container.get(i).get(y).E_mapCorrelation;
					DC.Resolution = Container.get(i).get(y).Resolution;
					PDBList.add(DC);
					CheckedFiles.add(Container.get(i).get(y).PDB_ID);
					if (DC.Resolution.equals("None")) {
						System.out.println("This is null");
						System.out.println(e.ToolsNames.get(i));
						System.out.println(DC.PDBIDTXT);
						;
					}
				}
			}
		}

		System.out.println(PDBList.size());
		GroupedResults(PDBList);// Print Reso Table
		Vector<DataContainer> CheckedPDB = new Vector<DataContainer>();

		for (int i = 0; i < PDBList.size(); ++i) {

			boolean NotFoundAtAll = false;
			boolean FoundPDB = false;
			int Index = 0;
			for (int f = 0; f < CheckedPDB.size(); ++f) {
				// System.out.println(CheckedPDB.get(f).PDBIDTXT);
				// System.out.println(CheckedPDB.get(f).Resolution);
				if (CheckedPDB.get(f).PDBIDTXT.equals(PDBList.get(i).PDBIDTXT)
						&& CheckedPDB.get(f).Resolution.equals(PDBList.get(i).Resolution))
					NotFoundAtAll = true;
				if (CheckedPDB.get(f).PDBIDTXT.equals(PDBList.get(i).PDBIDTXT)) {
					FoundPDB = true;
					Index = f;
				}
			}
			if (NotFoundAtAll == false && FoundPDB == false) {
				// System.out.println("NotFoundAtAll");
				DataContainer DC = new DataContainer();
				DC.PDBIDTXT = PDBList.get(i).PDBIDTXT;
				DC.F_mapCorrelation = PDBList.get(i).F_mapCorrelation;
				DC.E_mapCorrelation = PDBList.get(i).E_mapCorrelation;
				DC.Resolution = PDBList.get(i).Resolution;
				CheckedPDB.add(DC);
			}
			if (FoundPDB == true) {
				// System.out.println("FoundPDB");
				DataContainer DC = CheckedPDB.get(Index);
				DC.Resolution += " " + PDBList.get(i).Resolution;
				DC.F_mapCorrelation += " " + PDBList.get(i).F_mapCorrelation;
				DC.E_mapCorrelation += " " + PDBList.get(i).E_mapCorrelation;
				CheckedPDB.remove(Index);
				CheckedPDB.add(DC);
			}
		}
		String TableContext = "";
		for (int i = 0; i < CheckedPDB.size(); ++i) {
			System.out.println(CheckedPDB.get(i).PDBIDTXT + " " + CheckedPDB.get(i).Resolution);
			TableContext += "\\tiny " + CheckedPDB.get(i).PDBIDTXT + "&& \\tiny" + CheckedPDB.get(i).Resolution
					+ "&& \\tiny " + CheckedPDB.get(i).F_mapCorrelation + "& \\tiny "
					+ CheckedPDB.get(i).E_mapCorrelation + "\\\\" + "\n";
		}
		Collections.sort(CheckedPDB, DataContainer.DataContainerComparator);
		System.out.println(CheckedPDB.size());
		new Preparer().WriteTxtFile("Latex/PDBList.tex", TableContext);

	}

	public Vector<Integer> GroupedResults(Vector<DataContainer> PDBList) throws IOException {
		Collections.sort(PDBList, DataContainer.DataContainerComparator);
		// Count The cases depend on Reso
		Vector<Integer> Reso = new Vector<Integer>();
		Vector<Integer> NumReso = new Vector<Integer>();
		System.out.println(PDBList.size());

		for (int m = 0; m < PDBList.size(); ++m) {
			// System.out.println(PDBList.get(m).PDBIDTXT);
			// System.out.println(PDBList.get(m).Resolution);
			/*
			 * if(GroupedByPhases==true) {
			 * if(NumberUtils.isParsable(PDBList.get(m).E_mapCorrelation)){ Double Value =
			 * Double.parseDouble(PDBList.get(m).E_mapCorrelation)*10;
			 * 
			 * if (Reso.contains(Value.intValue())) {
			 * 
			 * NumReso.set(Reso.indexOf(Value.intValue()),
			 * NumReso.get(Reso.indexOf(Value.intValue())) + 1);
			 * 
			 * 
			 * } else {
			 * 
			 * Reso.add(Value.intValue()); NumReso.add(1);
			 * 
			 * } } else {
			 * 
			 * 
			 * System.out.println(PDBList.get(m).PDB_ID);
			 * System.out.println(PDBList.get(m).E_mapCorrelation); if (Reso.contains(-1)) {
			 * 
			 * NumReso.set(Reso.indexOf(-1), NumReso.get(Reso.indexOf(-1)) + 1);
			 * 
			 * 
			 * } else {
			 * 
			 * Reso.add(-1); NumReso.add(1);
			 * 
			 * } } }
			 * 
			 * else {
			 */
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
			// System.out.println("Reso "+ Reso.get(m));
			// System.out.println("Reso Num "+ NumReso.get(m));

			ResoTable += " &" + " \\scriptsize From " + Reso.get(m) + " to less than " + (Reso.get(m) + 1)
					+ " & \\scriptsize " + NumReso.get(m) + " \\\\ \n";
		}
		if (!new File("Latex/ResoTable.tex").exists()) {

			new Preparer().WriteTxtFile("Latex/ResoTable.tex", ResoTable);

		} else {

			new File("Latex/ResoTable.tex").delete();
			new Preparer().WriteTxtFile("Latex/ResoTable.tex", ResoTable);
		}

		System.out.println(ResoTable);
		return Reso;
	}

	void BestAndWorstCases(String ResultsDir) throws IOException {
		File[] Folders = new File(ResultsDir).listFiles();

		for (File Folder : Folders) {
			Vector<Vector<DataContainer>> Container = new Vector<Vector<DataContainer>>();
			if (Folder.isDirectory()) {
				LoadExcel e = new LoadExcel();
				for (File Excel : Folder.listFiles()) {
					Container.add(e.ReadExcel(Excel.getAbsolutePath()));
					e.ToolsNames.add(Excel.getName() + Folder.getName());
				}

				System.out.println(Folder.getName());
				Vector<Vector<DataContainer>> Container2 = new Vector<Vector<DataContainer>>();
				/*
				 * for(int i=0 ; i < Container.size() ; ++i) {
				 * 
				 * 
				 * Container2.addElement(e.CheckPDBexists(Container,Container.get(i))); // With
				 * intermediate System.out.println(Container2.get(Container2.size()-1).size());
				 * }
				 */

				Container2.addAll(Container);
				String TableOfBestCases = "";
				String TableOfWorstCases = "";

				for (int i = 0; i < Container2.size(); ++i) {
					System.out.println(e.ToolsNames.get(i));
					Vector<Integer> Reso = GroupedResults(Container2.get(i));
					for (int r = 0; r < Reso.size(); ++r) {

						DataContainer BestCase = new DataContainer();
						DataContainer WorstCase = new DataContainer();
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
						// System.out.println(e.ToolsNames.get(i));
						// System.out.println(Reso.get(r));
						// System.out.println("Best "+ BestCase.PDB_ID +" "+BestCase.n1m2);
						// System.out.println("WorstCase "+ WorstCase.PDB_ID +" "+WorstCase.n1m2);

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
									// System.out.println(e.ToolsNames.get(Other));
									// System.out.println("The Best Case in other "+
									// Container2.get(Other).get(t).PDB_ID +" "+Container2.get(Other).get(t).n1m2);
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
									// System.out.println(e.ToolsNames.get(Other));
									// System.out.println("The Worst Case in the other "+
									// Container2.get(Other).get(t).PDB_ID +" "+Container2.get(Other).get(t).n1m2);
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
				// System.out.println(TableOfBestCases);
				new Preparer().WriteTxtFile("Latex/" + Folder.getName() + "BestAndWorstTable.tex", TableOfBestCases);
			}

		}

	}

	void PrepareExcelForSpss(String ResultsDir) throws IOException {
		File[] Folders = new File(ResultsDir).listFiles();

		for (File Folder : Folders) {
			Vector<Vector<DataContainer>> Container = new Vector<Vector<DataContainer>>();
			LoadExcel e = new LoadExcel();
			if (Folder.isDirectory()) {
				for (File Excel : Folder.listFiles()) {
					Container.add(e.ReadExcel(Excel.getAbsolutePath()));
					e.ToolsNames.add(Excel.getName());
				}
				/*
				 * // Add the other DM to this Container in order to exclude the cases were not
				 * built Vector<Vector<DataContainer>> AllDMResults = new
				 * Vector<Vector<DataContainer>>(); for (File FFolder : Folders) {
				 * 
				 * if (FFolder.isDirectory() && !FFolder.getName().equals(Folder.getName()) ) {
				 * for (File Excel : FFolder.listFiles()) {
				 * AllDMResults.add(e.ReadExcel(Excel.getAbsolutePath()));
				 * e.ToolsNames.add(Excel.getName()); } } }
				 * System.out.println(Folder.getName());
				 * 
				 * int OrginalContainerSize=Container.size(); Container.addAll(AllDMResults);
				 */
				Vector<Vector<DataContainer>> Container2 = new Vector<Vector<DataContainer>>();
				for (int i = 0; i < Container.size(); ++i) {

					System.out.println(" " + i + " out of " + Container.size());
					Container2.addElement(e.CheckPDBexists(Container, Container.get(i)));
					System.out.println(Container2.get(Container2.size() - 1).size());
				}
				/*
				 * System.out.println("Container size "+Container.size());
				 * Container.removeAll(AllDMResults);
				 * System.out.println("Container size R "+Container.size());
				 * System.out.println("Container2 size  "+Container2.size());
				 * Container2.removeAll(AllDMResults);
				 * System.out.println("Container2 size R  "+Container2.size());
				 */
				Container.removeAllElements();
				Container.addAll(Container2); // only the cases that built by all tools
				System.out.println("Container size " + Container.size());
				Vector<Integer> Reso = GroupedResults(Container.get(0)); // because all excels have the same cases so
																			// one is engouh
				for (int r = 0; r < Reso.size(); ++r) {
					Vector<DataContainer> ToBeInExcel = new Vector<DataContainer>();
					for (int i = 0; i < Container.size(); ++i) {
						for (int y = 0; y < Container.get(i).size(); ++y) {
							Double Value = Double.parseDouble(Container.get(i).get(y).Resolution);
							/*
							 * if(GroupedByPhases==true) {
							 * if(NumberUtils.isParsable(Container.get(i).get(y).E_mapCorrelation)) Value =
							 * Double.parseDouble(Container.get(i).get(y).E_mapCorrelation)*10; else Value
							 * =-1.0;// not calculated phases }
							 */

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
					System.out.println(Excel.getAbsolutePath());

					for (int i = 0; i < ThisExcel.size(); ++i) {
						ThisExcel.get(i).Dataset = Excel.getName();
					}
					Reso = Folder.getName().replaceAll("^\\D*(\\d+).*", "$1");
					table.addAll(ThisExcel);

				}
				System.out.println(table.size());

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
						System.out.println("$ \\tiny" + table.get(i).Pipeline + " & \\tiny From "
								+ (Integer.valueOf(Reso) + 1) + "To less than " + (Integer.valueOf(Reso) + 2) + " "
								+ RowHancs + " " + RowMrncs + " " + RowNoncs);
						// String ResoRow=" \\footnotesize
						// \\parbox[t]{-10mm}{\\multirow{PipelinesNum}{2.3cm}{\\rotatebox[origin=c]{90}{From
						// R1 \\r{A} to less than R2 \\r{A}}}}";
						String ResoRow = " \\footnotesize \\parbox[t]{-10mm}{\\multirow{PipelinesNum}{2.3cm}{\\rotatebox[origin=c]{90}{Group R1 \\r{A}}}}";

						ResoRow = ResoRow.replace("R1", String.valueOf((Integer.valueOf(Reso) + 1)));
						// ResoRow=ResoRow.replace("R2", String.valueOf((Integer.valueOf(Reso)+2)));
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

					// System.out.println(table.get(i).Dataset);
					// System.out.println(table.get(i).ComparativeFactor);
					// System.out.println(table.get(i).Pipeline);
					// System.out.println(table.get(i).NumberOfCases);
					// System.out.println(table.get(t).get(i).Mean);
					// System.out.println(table.get(t).get(i).Max);
					// System.out.println(table.get(t).get(i).Min);

				}

				TheFullTable = TheFullTable.replace("PipelinesNum", String.valueOf(Pipelines.size()));
				TheFullTable += "\\hline \n";
			}
		}

		new Preparer().WriteTxtFile("Latex/" + "TheComparsionResults.tex", TheFullTable);

	}

	void GroupByPhases(String ResultsDir) throws IOException {
		File[] Folders = new File(ResultsDir).listFiles();
		LoadExcel e = new LoadExcel();
		Vector<Vector<DataContainer>> Container = new Vector<Vector<DataContainer>>();
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
		System.out.println(min);
		System.out.println(max);
		PhasesTable(Container.get(1));
	}

	Vector<Integer> PhasesTable(Vector<DataContainer> PDBList) throws IOException {
		Collections.sort(PDBList, DataContainer.DataContainerComparatorEmap);
		// Count The cases depend on Reso
		Vector<Integer> Reso = new Vector<Integer>();
		Vector<Integer> NumReso = new Vector<Integer>();
		System.out.println(PDBList.size());

		for (int m = 0; m < PDBList.size(); ++m) {
			// System.out.println(PDBList.get(m).PDBIDTXT);
			// System.out.println(PDBList.get(m).Resolution);
			if (NumberUtils.isParsable(PDBList.get(m).E_mapCorrelation)) {
				Double Value = Double.parseDouble(PDBList.get(m).E_mapCorrelation) * 10;

				if (Reso.contains(Value.intValue())) {

					NumReso.set(Reso.indexOf(Value.intValue()), NumReso.get(Reso.indexOf(Value.intValue())) + 1);

				} else {

					Reso.add(Value.intValue());
					NumReso.add(1);

				}
			} else {
				System.out.println(PDBList.get(m).PDB_ID);
				System.out.println(PDBList.get(m).E_mapCorrelation);
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
			// System.out.println("Reso "+ Reso.get(m));
			// System.out.println("Reso Num "+ NumReso.get(m));

			ResoTable += " &" + " \\tiny From " + Reso.get(m) + " to less than " + (Reso.get(m) + 1) + " & \\tiny "
					+ NumReso.get(m) + " \\\\ \n";
		}
		if (!new File("Latex/ResoTable.tex").exists()) {

			new Preparer().WriteTxtFile("Latex/ResoTable.tex", ResoTable);

		} else {

			new File("Latex/ResoTable.tex").delete();
			new Preparer().WriteTxtFile("Latex/ResoTable.tex", ResoTable);
		}

		System.out.println(ResoTable);
		return Reso;
	}

	void MatrixOfResults(String ResultsDir) throws IOException {
		File[] Folders = new File(ResultsDir).listFiles();
		
		for (File Folder : Folders) {
			if (Folder.isDirectory()) {
				Vector<Vector<DataContainer>> Container = new Vector<Vector<DataContainer>>();
				LoadExcel e = new LoadExcel();

				for (File Excel : Folder.listFiles()) {
					System.out.println(Excel.getAbsolutePath());
					Container.add(e.ReadExcel(Excel.getAbsolutePath()));
					e.ToolsNames.add(Excel.getName() + Folder.getName());
				}
				String Table = "";
				String Col = "";
				String RowCom0 = "";
				String RowCom5 = "";
				/*
				Vector<Vector<DataContainer>> Container2 = new Vector<Vector<DataContainer>>();
				for (int i = 0; i < Container.size(); ++i) {

					System.out.println(" " + i + " out of " + Container.size());
					Container2.addElement(e.CheckPDBexists(Container, Container.get(i)));
					System.out.println(Container2.get(Container2.size() - 1).size());
				}

				Container.removeAllElements();
				Container.addAll(Container2); // only the cases that built by all tools
				*/
				for (int i = 0; i < Container.size(); ++i) {
					Col = "\\tiny Pipeline ";// avoiding repetition
					RowCom0 += "\\tiny " + e.ToolsNames.get(i);
					RowCom5 += "\\tiny " + e.ToolsNames.get(i);
					for (int m = 0; m < Container.size(); ++m) {

						if (Col.length() == 0) {
							Col += "\\tiny " + e.ToolsNames.get(m);
						} else {
							Col += " & \\tiny " + e.ToolsNames.get(m);
						}
						float CountModelCom0 = 0;
						float EquivalentFor0=0;
						float CountModel=0;
						
						float CountModelCom5 = 0;
						float EquivalentFor5=0;
						for (int model = 0; model < Container.get(i).size(); ++model) {
							for (int modeComTo = 0; modeComTo < Container.get(m).size(); ++modeComTo) {
								System.out.println(e.ToolsNames.get(m));
								System.out.println(Folder.getName());
								if (Container.get(i).get(model).PDB_ID.equals(Container.get(m).get(modeComTo).PDB_ID)) {
									if (Container.get(i).get(model).BuiltPDB.equals("T")
											&& Container.get(m).get(modeComTo).BuiltPDB.equals("T")) {
										CountModel++;
											if( Integer.valueOf(Container.get(i).get(model).Completeness) - Integer
													.valueOf(Container.get(m).get(modeComTo).Completeness) >= 1) {
										

												CountModelCom0++;
									}
									if ( Integer.valueOf(Container.get(i).get(model).Completeness) - Integer
													.valueOf(Container.get(m).get(modeComTo).Completeness) == 0) {
										// if(Integer.valueOf(Container.get(i).get(model).Completeness) -
										// Integer.valueOf(Container.get(m).get(modeComTo).Completeness) >=5) {

										EquivalentFor0++;
									}
									
									
									if( Integer.valueOf(Container.get(i).get(model).Completeness) - Integer
											.valueOf(Container.get(m).get(modeComTo).Completeness) >= 5) {
								

										CountModelCom5++;
							}
							if ( Integer.valueOf(Container.get(i).get(model).Completeness) - Integer
											.valueOf(Container.get(m).get(modeComTo).Completeness) < 5 &&Integer.valueOf(Container.get(i).get(model).Completeness) - Integer
											.valueOf(Container.get(m).get(modeComTo).Completeness) >0 ) {
								

								EquivalentFor5++;
							}
									
									
									
									}
								}
							}
						}
						if (RowCom0.length() == 0) {
							RowCom0 += CountModelCom0;
							RowCom5 += CountModelCom5;
						}
						else {
							DecimalFormat decim = new DecimalFormat("#.##");
							
							RowCom0 += "& \\tiny " + decim.format(((CountModelCom0 * 100) / CountModel)) + "\\% ";
							RowCom0 += " \\tiny (=" + decim.format(((EquivalentFor0 * 100) / CountModel)) + "\\%)";
							RowCom0 += " \\tiny " + EquivalentFor0  + "";
							
							RowCom5 += "& \\tiny " + decim.format(((CountModelCom5 * 100) / CountModel)) + "\\% ";
							RowCom5 += " \\tiny (=" + decim.format(((EquivalentFor5 * 100) / CountModel)) + "\\%)";
							RowCom5 += " \\tiny " + EquivalentFor5  + "";
						}
					}
					RowCom0 += "\\\\ \\hline \n ";
					RowCom5 += "\\\\ \\hline \n ";

				}
				System.out.println(Col);
				System.out.println(RowCom0);
				Table += Col + "\\\\ \\hline \n ";
				Table += RowCom0;
				
				new Preparer().WriteTxtFile("Latex/MatrixOfResults" + Folder.getName() + "Com0.tex", Table);
			
				String TableCom5 = Col + "\\\\ \\hline \n ";
				TableCom5 += RowCom5;
				new Preparer().WriteTxtFile("Latex/MatrixOfResults" + Folder.getName() + "Com5.tex", TableCom5);
			}

		}

	}

	void LongMatrixOfResults(String ResultsDir) throws IOException {
		File[] Folders = new File(ResultsDir).listFiles();
		
		DecimalFormat decim = new DecimalFormat("#.##");
		
		Vector<Vector<Vector<DataContainer>>> AllDatasetContainer = new Vector<Vector<Vector<DataContainer>>>();
		Vector<String> DatasetNames = new Vector<String>();
		Vector<LoadExcel> ExcelNames = new Vector<LoadExcel>();
		Vector<String> ExcelNamesAsInTableHeader = new Vector<String>();
		String TableHeader = "\\tiny Pipelines && ";
		String ImprovemedBy = "\\tiny Improvemed By & ";

		for (File Folder : Folders) {

			if (Folder.isDirectory() && !Folder.getName().equals("Headers")) {

				LoadExcel e = new LoadExcel();

				Vector<Vector<DataContainer>> CurrentReading = new Vector<Vector<DataContainer>>();
				for (File Excel : Folder.listFiles()) {
					System.out.println(Excel.getName());
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

		System.out.println(AllDatasetContainer.size());
		System.out.println(DatasetNames.size());
		System.out.println(ExcelNames.size());

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
		// TableHeader=TableHeader.substring(0, TableHeader.length()-2); // For removing
		// the last &
		TableHeader += " \\multicolumn{2}{c}{ \\tiny All(Built)} \\\\ \n " + ImprovemedBy
				+ "  & \\tiny 0\\% &  \\tiny 5\\% \\\\ \\hline ";

		System.out.println(TableHeader);
		String Rows = TableHeader + "\n";
		String RowsRFactor = TableHeader + "\n";
		// for(int i=0 ;i < ExcelNamesAsInTableHeader.size() ; ++i) { // looping on
		// pipelines
		for (int i = 0; i < ExcelNamesAsInTableRows.size(); ++i) { // looping on pipelines
			Rows += " \\tiny " + ExcelNamesAsInTableRows.get(i);// Pipeline name
			RowsRFactor += " \\tiny " + ExcelNamesAsInTableRows.get(i);
			for (int d = 0; d < DatasetNames.size(); ++d) { // looping on datasets
				if (ExcelNames.get(d).ToolsNames.contains(ExcelNamesAsInTableRows.get(i))) {
					// Rows+=" & \\tiny "+DatasetNames.get(d); // dataset name
					String ZeroPercentRow = " & \\tiny " + DatasetNames.get(d);
					String RowRFactor = " & \\tiny " + DatasetNames.get(d);

					// String ZeroPercentRow=" & \\tiny "+DatasetNames.get(d)+" 0\\%";
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
/*
											if (AllDatasetContainer.get(d).get(IndexForTheRowContainer)
													.get(c).Completeness.equals("None"))
												AllDatasetContainer.get(d).get(IndexForTheRowContainer)
														.get(c).Completeness = "0";
											if (AllDatasetContainer.get(d).get(IndexForTheHeaderContainer)
													.get(compareTo).Completeness.equals("None"))
												AllDatasetContainer.get(d).get(IndexForTheHeaderContainer)
														.get(compareTo).Completeness = "0";
*/
											// System.out.println("Dataset "+d);

											
											if (Integer.parseInt(AllDatasetContainer.get(d).get(IndexForTheRowContainer)
													.get(c).Completeness) >= Integer.parseInt(
															AllDatasetContainer.get(d).get(IndexForTheHeaderContainer)
																	.get(compareTo).Completeness)) 
												{
													
													CountZeroPrecentge++;
												}
											
											else {
												System.out.println("First "+AllDatasetContainer.get(d).get(IndexForTheRowContainer)
													.get(c).Completeness);
												System.out.println("Second "+AllDatasetContainer.get(d).get(IndexForTheHeaderContainer)
																	.get(compareTo).Completeness);
											}
												if ((Integer
														.parseInt(AllDatasetContainer.get(d)
																.get(IndexForTheRowContainer).get(c).Completeness)
														- Integer.parseInt(AllDatasetContainer.get(d)
																.get(IndexForTheHeaderContainer)
																.get(compareTo).Completeness)) >= 5) {
													
														
														CountFivePrecentge++;
													}
//System.out.println(Double.parseDouble(AllDatasetContainer.get(d).get(IndexForTheRowContainer).get(c).R_factor0Cycle));
//System.out.println(Double.parseDouble(AllDatasetContainer.get(d).get(IndexForTheHeaderContainer).get(compareTo).R_factor0Cycle));
													if (Double.parseDouble(
															AllDatasetContainer.get(d).get(IndexForTheRowContainer)
																	.get(c).R_factor0Cycle) <= Double
																			.parseDouble(AllDatasetContainer.get(d)
																					.get(IndexForTheHeaderContainer)
																					.get(compareTo).R_factor0Cycle) )

													{
														//System.out.println("Met Rfactor");
														
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
														//System.out.println("FirstPDB "+FirstPDB);
//System.out.println("SecondPDB "+SecondPDB);

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
														//System.out.println("Met RFactor 5%");
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
														//System.out.println(" FirstPDB 5% "+FirstPDB);
														//System.out.println("SecondPDB 5% "+SecondPDB);
                                                        // if((FirstPDB - SecondPDB) <= - 0.05 )
														FirstPDB = Double.parseDouble(decim.format(FirstPDB));
														SecondPDB = Double.parseDouble(decim.format(SecondPDB));
														if (FirstPDB <= 0.05)
														CountFivePrecentgeRFactor++;
													}

												
											
										}

								}
							}

						if (IndexForTheRowContainer != -1) {
							
							System.out.println("CountZeroPrecentge "+CountZeroPrecentge);
							ZeroPercentRow += " & \\tiny " + ((CountZeroPrecentge * 100)
									/ AllDatasetContainer.get(d).get(IndexForTheRowContainer).size());
						
							
							ZeroPercentRow += " & \\tiny " + ((CountFivePrecentge * 100)
									/ AllDatasetContainer.get(d).get(IndexForTheRowContainer).size());

							RowRFactor += " & \\tiny " + ((CountZeroPrecentgeRFactor * 100)
									/ AllDatasetContainer.get(d).get(IndexForTheRowContainer).size());
							RowRFactor += " & \\tiny " + ((CountFivePrecentgeRFactor * 100)
									/ AllDatasetContainer.get(d).get(IndexForTheRowContainer).size());

							// FivePercentRow+=" & \\tiny
							// "+((CountFivePrecentge*100)/AllDatasetContainer.get(d).get(IndexForTheRowContainer).size());
						} else {

							ZeroPercentRow += " & \\tiny " + CountZeroPrecentge;
							ZeroPercentRow += " & \\tiny " + CountFivePrecentge;

							RowRFactor += " & \\tiny " + CountZeroPrecentgeRFactor;
							RowRFactor += " & \\tiny " + CountFivePrecentgeRFactor;
							// FivePercentRow+=" & \\tiny "+CountFivePrecentge;
						}
					}
					int CountZeroPrecentgeAll = 0;
					int CountFivePrecentgeAll = 0;

					int CountZeroPrecentgeAllRFactor = 0;
					int CountFivePrecentgeAllRFactor = 0;
					for (int c = 0; c < AllDatasetContainer.get(d).get(i).size(); ++c) {
						// System.out.println("DataSet "+d);
						// System.out.println("Excel "+i);
						// System.out.println("Record "+c);
						

						

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

							//System.out.println(CountHowManyPipelineInTheHeaderWeComapreWith);
							//System.out.println("d " + d);
							// if(all==i && all+1 >= AllDatasetContainer.get(d).size())
							// break;

							// if(all==i && all+1 < AllDatasetContainer.get(d).size()) // to avoid comapre
							// the pipeline with itself when we have only one Pipeline in the header also
							// avoid compare the pipelines with itself in case we have more pipeliens in the
							// header
							// ++all;

							/*
							 * if(all==0) { IsThisModelHasTheHighestCompletnessOverAllZeroPercent=true;
							 * IsThisModelHasTheHighestCompletnessOverAllFivePercent=true;
							 * 
							 * IsThisModelHasTheHighestCompletnessOverAllZeroPercentRFactor=true;
							 * IsThisModelHasTheHighestCompletnessOverAllFivePercentRFactor=true; }
							 */
							if (ExcelNamesAsInTableHeader.contains(ExcelNames.get(d).ToolsNames.get(all))
									&& !ExcelNames.get(d).ToolsNames.get(all)
											.equals(ExcelNames.get(d).ToolsNames.get(i)))
								for (int Excel = 0; Excel < AllDatasetContainer.get(d).get(all).size(); ++Excel) {
									if (AllDatasetContainer.get(d).get(i).get(c).BuiltPDB.equals("T")
											&& AllDatasetContainer.get(d).get(all).get(Excel).BuiltPDB.equals("T"))
										if (AllDatasetContainer.get(d).get(i).get(c).PDB_ID
												.equals(AllDatasetContainer.get(d).get(all).get(Excel).PDB_ID)) {
/*
											if (AllDatasetContainer.get(d).get(i).get(c).Completeness.equals("None")) {
												AllDatasetContainer.get(d).get(i).get(c).Completeness = "0";
											}
											if (AllDatasetContainer.get(d).get(all).get(Excel).Completeness
													.equals("None")) {
												AllDatasetContainer.get(d).get(all).get(Excel).Completeness = "0";
											}
*/
											if (Integer.parseInt(
													AllDatasetContainer.get(d).get(i).get(c).Completeness) >= Integer
															.parseInt(AllDatasetContainer.get(d).get(all)
																	.get(Excel).Completeness)) {

												/*System.out.println("DataSet " + d);
												System.out.println("Excel1 " + ExcelNames.get(d).ToolsNames.get(i));
												System.out.println("Excel2 " + ExcelNames.get(d).ToolsNames.get(all));
												System.out.println("PDB "
														+ AllDatasetContainer.get(d).get(i).get(c).PDB_ID
														+ " Compare to "
														+ AllDatasetContainer.get(d).get(all).get(Excel).PDB_ID);
												System.out.println("PDB "
														+ AllDatasetContainer.get(d).get(i).get(c).Completeness
														+ " Compare to "
														+ AllDatasetContainer.get(d).get(all).get(Excel).Completeness);
												System.out.println("all " + all);
*/
												GreaterThanZeroPercent.add(true);
												// IsThisModelHasTheHighestCompletnessOverAllZeroPercent=false;
											}
											/*
											 * if(Integer.parseInt(AllDatasetContainer.get(d).get(i).get(c).
											 * Completeness) <
											 * Integer.parseInt(AllDatasetContainer.get(d).get(all).get(Excel).
											 * Completeness)) {
											 * 
											 * System.out.println("DataSet "+d);
											 * System.out.println("Excel1 "+ExcelNames.get(d).ToolsNames.get(i));
											 * System.out.println("Excel2 "+ExcelNames.get(d).ToolsNames.get(all));
											 * System.out.println("PDB "+AllDatasetContainer.get(d).get(i).get(c).PDB_ID
											 * +" Compare to "+AllDatasetContainer.get(d).get(all).get(Excel).PDB_ID);
											 * System.out.println("PDB "+AllDatasetContainer.get(d).get(i).get(c).
											 * Completeness
											 * +" Compare to "+AllDatasetContainer.get(d).get(all).get(Excel).
											 * Completeness); System.out.println("all "+all);
											 * 
											 * 
											 * IsThisModelHasTheHighestCompletnessOverAllZeroPercent=false; }
											 */
											// avoiding compare the tool with itself for ex Bucc completeness 95 >> 95
											// -95 =0 so 0 < 5

											if (((Integer
													.parseInt(AllDatasetContainer.get(d).get(i).get(c).Completeness)
													- Integer.parseInt(AllDatasetContainer.get(d).get(all)
															.get(Excel).Completeness)) >= 5)) {
/*
												System.out.println("DataSet " + d);
												System.out.println("Excel1 " + ExcelNames.get(d).ToolsNames.get(i));
												System.out.println("Excel2 " + ExcelNames.get(d).ToolsNames.get(all));
												System.out.println("PDB "
														+ AllDatasetContainer.get(d).get(i).get(c).PDB_ID
														+ " Compare to "
														+ AllDatasetContainer.get(d).get(all).get(Excel).PDB_ID);
												System.out.println("PDB "
														+ AllDatasetContainer.get(d).get(i).get(c).Completeness
														+ " Compare to "
														+ AllDatasetContainer.get(d).get(all).get(Excel).Completeness);
												System.out.println("all " + all);
*/
												GreaterThanZeroFivePercent.add(true);
												// IsThisModelHasTheHighestCompletnessOverAllFivePercent=false;
											}
											/*
											 * if(
											 * ((Integer.parseInt(AllDatasetContainer.get(d).get(i).get(c).Completeness)
											 * - Integer.parseInt(AllDatasetContainer.get(d).get(all).get(Excel).
											 * Completeness)) < 5)) {
											 * 
											 * IsThisModelHasTheHighestCompletnessOverAllFivePercent=false; }
											 */

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
												//IsThisModelHasTheHighestCompletnessOverAllZeroPercentRFactor = false;

											}

											if (i != all && ((Double.parseDouble(
													AllDatasetContainer.get(d).get(i).get(c).R_factor0Cycle)
													- Double.parseDouble(AllDatasetContainer.get(d).get(all)
															.get(Excel).R_factor0Cycle)) <= -0.05)) {
												
												double FirstPDB=Double.parseDouble(AllDatasetContainer.get(d).get(i).get(c).R_free0Cycle) - Double.parseDouble(AllDatasetContainer.get(d).get(i).get(c).R_factor0Cycle); 
												double SecondPDB=Double.parseDouble(AllDatasetContainer.get(d).get(all).get(Excel).R_free0Cycle) - Double.parseDouble(AllDatasetContainer.get(d).get(all).get(Excel).R_factor0Cycle);
												FirstPDB = Double.parseDouble(decim.format(FirstPDB));
												SecondPDB = Double.parseDouble(decim.format(SecondPDB));
												//if((FirstPDB - SecondPDB) <= - 0.05 )
													if(FirstPDB <= 0.05 )
													GreaterThanZeroFivePercentRFactor.add(true);
												//IsThisModelHasTheHighestCompletnessOverAllFivePercentRFactor = false;

											}

										}
								}
						}

						if (CountHowManyPipelineInTheHeaderWeComapreWith == GreaterThanZeroPercent.size()
								&& GreaterThanZeroPercent.size() != 0) {
							// System.out.println("Zero Percent
							// "+AllDatasetContainer.get(d).get(i).get(c).PDB_ID);
							//System.out.println("GreaterThanZeroPercent.size() " + GreaterThanZeroPercent.size());
							CountZeroPrecentgeAll++;
						}
						if (CountHowManyPipelineInTheHeaderWeComapreWith == GreaterThanZeroFivePercent.size()
								&& GreaterThanZeroFivePercent.size() != 0) {
							// System.out.println("Five Percent
							// "+AllDatasetContainer.get(d).get(i).get(c).PDB_ID);
							CountFivePrecentgeAll++;
						}

						
						if (CountHowManyPipelineInTheHeaderWeComapreWith == GreaterThanZeroPercentRFactor.size()
								&& GreaterThanZeroPercentRFactor.size() != 0) {
							// System.out.println("Zero Percent
							// "+AllDatasetContainer.get(d).get(i).get(c).PDB_ID);
							//System.out.println("GreaterThanZeroPercent.size() " + GreaterThanZeroPercent.size());
							CountZeroPrecentgeAllRFactor++;
						}
						if (CountHowManyPipelineInTheHeaderWeComapreWith == GreaterThanZeroFivePercentRFactor.size()
								&& GreaterThanZeroFivePercentRFactor.size() != 0) {
							// System.out.println("Five Percent
							// "+AllDatasetContainer.get(d).get(i).get(c).PDB_ID);
							CountFivePrecentgeAllRFactor++;
						}
						
						/*
						 * if(IsThisModelHasTheHighestCompletnessOverAllZeroPercent==true ) { //
						 * System.out.println("Zero Percent "+AllDatasetContainer.get(d).get(i).get(c).
						 * PDB_ID); CountZeroPrecentgeAll++; }
						 * if(IsThisModelHasTheHighestCompletnessOverAllFivePercent==true ) {
						 * //System.out.println("Five Percent "+AllDatasetContainer.get(d).get(i).get(c)
						 * .PDB_ID); CountFivePrecentgeAll++; }
						
						if (IsThisModelHasTheHighestCompletnessOverAllZeroPercentRFactor == true) {
							// System.out.println("Zero Percent
							// "+AllDatasetContainer.get(d).get(i).get(c).PDB_ID);
							CountZeroPrecentgeAllRFactor++;
						}
						if (IsThisModelHasTheHighestCompletnessOverAllFivePercentRFactor == true) {
							// System.out.println("Five Percent
							// "+AllDatasetContainer.get(d).get(i).get(c).PDB_ID);
							CountFivePrecentgeAllRFactor++;
						}
						 */
					}

					ZeroPercentRow += " & \\tiny "
							+ ((CountZeroPrecentgeAll * 100) / AllDatasetContainer.get(d).get(i).size());
					ZeroPercentRow += " & \\tiny "
							+ ((CountFivePrecentgeAll * 100) / AllDatasetContainer.get(d).get(i).size());

					RowRFactor += " & \\tiny "
							+ ((CountZeroPrecentgeAllRFactor * 100) / AllDatasetContainer.get(d).get(i).size());
					RowRFactor += " & \\tiny "
							+ ((CountFivePrecentgeAllRFactor * 100) / AllDatasetContainer.get(d).get(i).size());
					// FivePercentRow+=" & \\tiny
					// "+((CountFivePrecentgeAll*100)/AllDatasetContainer.get(d).get(i).size());

					Rows += ZeroPercentRow + " \\\\ \n";
					RowsRFactor += RowRFactor + " \\\\ \n";
					// Rows+=FivePercentRow;
					// Rows+=" \\\\ \n";
				}
			}
			Rows += " \\hline \n";
			RowsRFactor += " \\hline \n";
		}

		System.out.println(Rows);
		new Preparer().WriteTxtFile("Latex/LongMatrixCompleteness" + ".tex", Rows);
		new Preparer().WriteTxtFile("Latex/LongMatrixRfactor" + ".tex", RowsRFactor);
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