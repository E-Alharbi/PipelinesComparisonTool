package Analyser;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
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

		String ExcelDir = "/Volumes/PhDHardDrive/jcsg1200Results/Fasta/Reproducibility2/ExFaliedCases/OriginalExperiment";
		
		// String ExcelDir="/Volumes/PhDHardDrive/jcsg1200Results/ExcelSheets17";

		new RunComparison().CheckDirAndFile("CSV");
		new RunComparison().CheckDirAndFile("Latex");

		// new ResultsInCSV().PDBTable(Container);

		//new ResultsInLatex().OverallResults(ExcelDir);
		// new ResultsInLatex().PDBList(ExcelDir);
		// new ResultsInLatex().BestAndWorstCases(ExcelDir);
		// new ResultsInLatex().PrepareExcelForSpss(ExcelDir);
		// new ResultsInLatex().SpssBootstraping("SpssExcel");
		// new ResultsInLatex().ReadingSpssBootstraping("SpssExcelResults");
		//new ResultsInLatex().MatrixOfResults(ExcelDir);
		//new ResultsInLatex().LongMatrixOfResults(ExcelDir);

		// new ResultsInCSV().GroupByPhases(ExcelDir);
		
		//new ResultsInLatex().TimeTakingTable(ExcelDir);
		new ResultsInLatex().CompRTimeAvgTable(ExcelDir);
	}
	void CompRTimeAvgTable(String ExcelDir) throws IOException {
		
		for (File Folder : new File(ExcelDir).listFiles()) {
			if(Folder.isDirectory()) {
				String Table="\\tiny Pipeline & \\tiny Completeness &\\tiny R-work/R-free & \\tiny Time taking \\\\ \\hline \n";
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
						ExcelLoader f = new ExcelLoader();
						Vector<DataContainer> Container = f.ReadExcel(Excel.getAbsolutePath());
						for(int i=0; i < Container.size() ; ++i) {
							Com+=Double.parseDouble(Container.get(i).Completeness);
							Rwork+=Double.parseDouble(Container.get(i).R_factor0Cycle);
							Rfree+=Double.parseDouble(Container.get(i).R_free0Cycle);
							Time+=Double.parseDouble(Container.get(i).TimeTaking);
						}
						DecimalFormat df = new DecimalFormat("#.##");
						df.setRoundingMode(RoundingMode.HALF_UP);
						
						
						Table+="\\tiny "+Excel.getName()+" & \\tiny "+Math.round((Com/Container.size())) +" & \\tiny "+ df.format(BigDecimal.valueOf(Double.valueOf((Rwork/Container.size()))))+"/"+df.format(BigDecimal.valueOf(Double.valueOf((Rfree/Container.size()))))+" & \\tiny "+Math.round((Time/Container.size())) +"\\\\ \\hline \n";
					}
				}
				new Preparer().WriteTxtFile("Latex/ReproducibilityTable" + Folder.getName() + ".tex", FormatingPipelinesNames(Table,true) + "\n"+Comments);

			}
		}
	}
void TimeTakingTable(String ExcelDir) throws IOException {
	
	for (File Folder : new File(ExcelDir).listFiles()) {
		if(Folder.isDirectory()) {
			String Table="\\tiny Pipeline & \\tiny Min &\\tiny Max & \\tiny Avg. \\\\ \\hline \n";
			String Comments="";
			Comments+="% "+ new Date().toString() +" \n ";
			Comments+="% Folder: "+ Folder.getName() +" \n ";
			Comments+="% Full folder path :  "+ Folder.getAbsolutePath() +" \n ";
			Comments+="% Excel used  :  "+ " \n ";
			for(File Excel : Folder.listFiles()) {
				if(Excel.isFile()) {
					Comments+="%"+Excel.getAbsolutePath() +" \n ";
					ExcelLoader f = new ExcelLoader();
					Vector<DataContainer> Container = f.ReadExcel(Excel.getAbsolutePath());
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
			System.out.println(Table);
			new Preparer().WriteTxtFile("Latex/TimeTakingTable" + Folder.getName() + ".tex", FormatingPipelinesNames(Table,true) + "\n"+Comments);
		}
	}
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
		ExcelLoader e = new ExcelLoader();
		Vector<Vector<DataContainer>> Container = new Vector<Vector<DataContainer>>();
		String Comments="% "+new Date().toString() +" \n";
		for (File Folder : Folders) {
			if (Folder.isDirectory()) {
				Comments+="% Folder "+Folder.getName() +" \n";
				Comments+="% Full folder path "+Folder.getAbsolutePath() +" \n";
				Comments+="% Excel used:  \n";
				for (File Excel : Folder.listFiles()) {
					if(Excel.isFile()) { // Because this folder might contains Bucc54 folder
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
		System.out.println(CSV);
		new Preparer().WriteTxtFile("CSV/Overall.csv", CSV);
		Vector<String> CheckedFiles = new Vector<String>();
		String Table = "\\tiny Pipeline &&\\tiny HA-NCS &&&&\\tiny MR-NCS &&&& \\tiny NO-NCS\\\\ \n" + 
				"&&\\tiny Compelete & \\tiny Intermediate& \\tiny Falied&&\\tiny Compelete &\\tiny Intermediate& \\tiny Falied&& \\tiny Compelete & \\tiny Intermediate& \\tiny Falied\\\\ \\hline";
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

				System.out.println("\\tiny " + Results.get(i).FileName + "&&" + hancs + "&&" + mrncs + "&&"
						+ noncs + "\\\\");
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
				Vector<Vector<DataContainer>> AllToolsData = new Vector<Vector<DataContainer>>();
				for(int m =0 ; m < Container.size() ; ++m ) {
					if(e.ToolsNames.get(m).contains("hancs"))
					AllToolsData.add(Container.get(m));
				}
				Vector <DataContainer> AfterExBuccDev= new Exculding54Dataset().Exculding(Container.get(i), true);
				NumberofConsideredCasesHancs=f.CheckPDBexists(AllToolsData,AfterExBuccDev).size();
				System.out.println(e.ToolsNames.get(i) +" NumberofConsideredCases "+NumberofConsideredCasesHancs);
				System.out.println(e.ToolsNames.get(i) +" AfterExBuccDev "+AfterExBuccDev.size());
				
			}
if(e.ToolsNames.get(i).contains("mrncs") && NumberofConsideredCasesMrncs==0) {
	ExcelLoader f = new ExcelLoader();
	Vector<Vector<DataContainer>> AllToolsData = new Vector<Vector<DataContainer>>();
	for(int m =0 ; m < Container.size() ; ++m ) {
		if(e.ToolsNames.get(m).contains("mrncs"))
		AllToolsData.add(Container.get(m));
	}
	Vector <DataContainer> AfterExBuccDev= new Exculding54Dataset().Exculding(Container.get(i), true);
	NumberofConsideredCasesMrncs=f.CheckPDBexists(AllToolsData,AfterExBuccDev).size();
	System.out.println(e.ToolsNames.get(i) +" NumberofConsideredCases "+NumberofConsideredCasesMrncs);
	System.out.println(e.ToolsNames.get(i) +" AfterExBuccDev "+AfterExBuccDev.size());
			}
if(e.ToolsNames.get(i).contains("noncs") && NumberofConsideredCasesNoncs==0) {
	ExcelLoader f = new ExcelLoader();
	Vector<Vector<DataContainer>> AllToolsData = new Vector<Vector<DataContainer>>();
	for(int m =0 ; m < Container.size() ; ++m ) {
		if(e.ToolsNames.get(m).contains("noncs"))
		AllToolsData.add(Container.get(m));
	}
	Vector <DataContainer> AfterExBuccDev= new Exculding54Dataset().Exculding(Container.get(i), true);
	NumberofConsideredCasesNoncs=f.CheckPDBexists(AllToolsData,AfterExBuccDev).size();
	System.out.println(e.ToolsNames.get(i) +" NumberofConsideredCases "+NumberofConsideredCasesNoncs);
	System.out.println(e.ToolsNames.get(i) +" AfterExBuccDev "+AfterExBuccDev.size());
}
		}
		Table += " \\multicolumn{10}{c}{\\tiny{ Considered models: "  + NumberofConsideredCasesHancs + " HA-NCS, " + NumberofConsideredCasesMrncs + " MR-NCS and " + NumberofConsideredCasesNoncs + " NO-NCS.}}"
				+ " \n";
	
		
		Table=FormatingPipelinesNames(Table,true);
		new Preparer().WriteTxtFile("Latex/TheNumberOfCompletedCases.tex", Table.replace(".xlsx", "") +" \n "+Comments);
	}

	void PDBList(String ResultsDir) throws IOException {
		File[] Folders = new File(ResultsDir).listFiles();
		ExcelLoader e = new ExcelLoader();
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
				ExcelLoader e = new ExcelLoader();
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
			ExcelLoader e = new ExcelLoader();
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
		ExcelLoader e = new ExcelLoader();
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
		if(new File("MatricesLogs").exists()) {
			FileUtils.deleteDirectory(new File("MatricesLogs"));
		}
		File[] Folders = new File(ResultsDir).listFiles();
		
		for (File Folder : Folders) {
			if (Folder.isDirectory()) {
				Vector<Vector<DataContainer>> Container = new Vector<Vector<DataContainer>>();
				ExcelLoader e = new ExcelLoader();
				String Comments="";
				Comments = "% "+new Date().toString()+" \n";
				Comments += "% This table generated from \n % Folder :"+Folder.getName()+" \n";
				Comments += "% Full folder path :"+Folder.getAbsolutePath()+" \n";
				Comments += "% Excel used : \n ";
				for (File Excel : Folder.listFiles()) {
					if(Excel.isFile()) {
					System.out.println(Excel.getAbsolutePath());
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
				/*
				for(int i=0 ;i < Container.size() ; ++i) {
					for(int c=0 ;c < Container.get(i).size() ; ++c) {
						if(Container.get(i).get(c).Completeness.equals("None") || Container.get(i).get(c).R_factor0Cycle.equals("None"))
							Container.get(i).get(c).BuiltPDB="F";
					}
				}
				*/
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
					 RowCom0Equivalent  += "\\tiny " + e.ToolsNames.get(i);
					 RowCom5Equivalent += "\\tiny " + e.ToolsNames.get(i);
					
					 RowCom0Models += "\\tiny " + e.ToolsNames.get(i);
					 RowCom5Models+= "\\tiny " + e.ToolsNames.get(i);
					 
					 
					 RowR0 += "\\tiny " + e.ToolsNames.get(i)+" _{R-work}";
					 RowR0Equivalent+= "\\tiny " + e.ToolsNames.get(i)+" _{R-work}";
					 RowROverfitting="\\tiny Overfitting" ;
					 RowR5 += "\\tiny " + e.ToolsNames.get(i)+" _{R-work}";
					 RowR5Overfitting="\\tiny Overfitting" ;
					 
					  RowRFree0 ="\\tiny " + e.ToolsNames.get(i)+" _R_{-free}";
					  
					  RowRFree5 = "\\tiny " + e.ToolsNames.get(i)+" _R_{-free}";
					  
					  RowRFree0Equivalent="\\tiny " + e.ToolsNames.get(i)+" _R_{-free}";
				
					   RowRFree5Equivalent = "\\tiny " + e.ToolsNames.get(i)+" _R_{-free}";
					   RowR5Equivalent += "\\tiny " + e.ToolsNames.get(i)+" _{R-work}";
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
						
						for (int model = 0; model < Container.get(i).size(); ++model) {
							for (int modeComTo = 0; modeComTo < Container.get(m).size(); ++modeComTo) {
								
								
								//System.out.println(Folder.getName());
								if (Container.get(i).get(model).PDB_ID.equals(Container.get(m).get(modeComTo).PDB_ID)) {
									if (Container.get(i).get(model).BuiltPDB.equals("T")
											&& Container.get(m).get(modeComTo).BuiltPDB.equals("T")) {
										CountModel++;
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
							
						//	System.out.println(Container.get(m).get(modeComTo).PDB_ID);	
							//	System.out.println(Container.get(m).get(modeComTo).R_free0Cycle);	
						//	if( new BigDecimal(Container.get(i).get(model).R_free0Cycle).subtract(new BigDecimal(Container.get(i).get(model).R_factor0Cycle) ).compareTo(new BigDecimal("0.05")) <= 0 &&  new BigDecimal(Container.get(m).get(modeComTo).R_free0Cycle).subtract(new BigDecimal(Container.get(m).get(modeComTo).R_factor0Cycle) ).compareTo(new BigDecimal("0.05")) > 0 ) {
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
	System.out.println(e.ToolsNames.get(m));
									
	EquivalentR5++;
	LogOfR5Equivalent+=e.ToolsNames.get(i) +"\t"+Container.get(i).get(model).PDB_ID+"\t"+	Container.get(i).get(model).R_factor0Cycle+"\t"+	e.ToolsNames.get(m)+"\t"+Container.get(m).get(modeComTo).PDB_ID+"\t"	+Container.get(m).get(modeComTo).R_factor0Cycle+"\n";				
								}

if(new BigDecimal(Container.get(i).get(model).R_free0Cycle).compareTo(new BigDecimal(Container.get(m).get(modeComTo).R_free0Cycle)) < 0 &&new BigDecimal(Container.get(i).get(model).R_free0Cycle).add(new BigDecimal("0.05")).compareTo(new BigDecimal(Container.get(m).get(modeComTo).R_free0Cycle)) > 0 &&  new BigDecimal(Container.get(i).get(model).R_free0Cycle).compareTo(new BigDecimal(Container.get(m).get(modeComTo).R_free0Cycle))!=0) {

	System.out.println(e.ToolsNames.get(m));
	EquivalentRFree5++;
	
	LogOfRFree5Equivalent+=e.ToolsNames.get(i) +"\t"+Container.get(i).get(model).PDB_ID+"\t"+	Container.get(i).get(model).R_free0Cycle+"\t"+	e.ToolsNames.get(m)+"\t"+Container.get(m).get(modeComTo).PDB_ID+"\t"	+Container.get(m).get(modeComTo).R_free0Cycle+"\n";				

									
								}
								
									}
								}
							}
						}
						/*
						if (i==m) {
							
							RowCom0 += "& \\tiny -" ;
							//RowCom0 += " \\tiny (=" + decim.format(((EquivalentFor0 * 100) / CountModel)) + "\\%)";
							 RowCom0Equivalent += "& \\tiny -" ;
							
							
							RowCom5 += "& \\tiny -";
							
							
							 RowCom5Equivalent+= " &\\tiny- ";
							
							  RowCom0Models += "& \\tiny -";
							  RowCom5Models+= "& \\tiny- ";
							  
							 
							  RowR0 += "&  \\tiny -"  ;
							  
								  RowR0 += "& \\tiny- " ;
								//RowCom0 += " \\tiny (=" + decim.format(((EquivalentFor0 * 100) / CountModel)) + "\\%)";
							  RowR0Equivalent += "& \\tiny- ";
							  
							  
								
							  RowROverfitting+=   "& \\tiny- ";
							  
								
							  RowR5 += "& \\tiny- " ;
								 
									 
								  
							
							  RowR5Overfitting+=   "& \\tiny- " ;

							 
							  
							
						RowRFree0 += "& \\tiny-" ;
								
								
									 RowRFree5 += "& \\tiny - ";
									
						}
						*/
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
							//DecimalFormat decim = new DecimalFormat("#.##");
							DecimalFormat decim = new DecimalFormat("#");
							//if(((CountModelCom0 * 100) / CountModel) >= 30)
							//RowCom0 += "& \\cellcolor{gray!35} \\tiny " + decim.format(((CountModelCom0 * 100) / CountModel)) + "\\% " ;
							//if(((CountModelCom0 * 100) / CountModel) < 30)
							RowCom0 += " &  " + decim.format(((CountModelCom0 * 100) / CountModel))  ;
							//RowCom0 += " \\tiny (=" + decim.format(((EquivalentFor0 * 100) / CountModel)) + "\\%)";
							 RowCom0Equivalent += " &  " + decim.format(((EquivalentFor0 * 100) / CountModel))  ;
							
							//if(((CountModelCom5 * 100) / CountModel) >=30)
							//	RowCom5 += "& \\cellcolor{gray!35} \\tiny " + decim.format(((CountModelCom5 * 100) / CountModel)) + "\\% ";
							//	if(((CountModelCom5 * 100) / CountModel)<30)
							RowCom5 += " &  " + decim.format(((CountModelCom5 * 100) / CountModel));
							
							//RowCom5 += " \\tiny (=" + decim.format(((EquivalentFor5 * 100) / CountModel)) + "\\%)";
							 RowCom5Equivalent+= " & " + decim.format(((EquivalentFor5 * 100) / CountModel));
							
							  RowCom0Models += " &  "+(int)CountModel;
							  RowCom5Models+= " &  "+(int)CountModel;
							  
							//  if(((CountModelR * 100) / CountModel) >= 30)
							//  RowR0 += "& \\cellcolor{gray!35} \\tiny " + decim.format(((CountModelR * 100) / CountModel)) + "\\% ";
							//  if(((CountModelR * 100) / CountModel) < 30)
							 RowR0 += " &  " + decim.format(((CountModelR * 100) / CountModel))  ;
								//RowCom0 += " \\tiny (=" + decim.format(((EquivalentFor0 * 100) / CountModel)) + "\\%)";
							  RowR0Equivalent += " &  " + decim.format(((EquivalentR * 100) / CountModel)) ;
							  
							  if(OverfittingR==0)
								  RowROverfitting+=   "& "; 
								  if(OverfittingR!=0)
							  RowROverfitting+=   " &  " + decim.format(((OverfittingR * 100) / CountModelR));
							  
								//  if(((CountModelR5 * 100) / CountModel) >= 30)
							//  RowR5 += "& \\cellcolor{gray!35} \\tiny " + decim.format(((CountModelR5 * 100) / CountModel)) + "\\% " ;
								  //if(((CountModelR5 * 100) / CountModel) < 30)
									  RowR5 += " &  " + decim.format(((CountModelR5 * 100) / CountModel))  ;
								  
							  if(OverfittingR5==0)
						       RowR5Overfitting+=   " &  0";
							  if(OverfittingR5!=0)
							  RowR5Overfitting+=   " &  " + decim.format(((OverfittingR5 * 100) / CountModelR5)) ;

							 
							  
							//  if(((CountRFree0 * 100) / CountModel) >= 30)
						// RowRFree0 += "& \\cellcolor{gray!35} \\tiny " + decim.format(((CountRFree0 * 100) / CountModel)) + "\\% " ;
							//	if(((CountRFree0 * 100) / CountModel) < 30)
						RowRFree0 += " &  " + decim.format(((CountRFree0 * 100) / CountModel))  ;
								
								// if(((CountRFree5 * 100) / CountModel) >= 30)
								//	 RowRFree5 += "& \\cellcolor{gray!35} \\tiny " + decim.format(((CountRFree5 * 100) / CountModel)) + "\\% " ;
								//if(((CountRFree5 * 100) / CountModel) < 30)
								RowRFree5 += " &  " + decim.format(((CountRFree5 * 100) / CountModel))  ;	
							  
					RowRFree0Equivalent+=	" & " + decim.format(((EquivalentRFree * 100) / CountModel));		
					
					 RowR5Equivalent += " & " + decim.format(((EquivalentR5 * 100) / CountModel)) ;
					
					 RowRFree5Equivalent+=	" &  " + decim.format(((EquivalentRFree5 * 100) / CountModel)) ;
					
					 
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
					//RowR0 += "\\\\ \\hline \n ";
					
					
					RowR0Equivalent += " \\\\  \n ";
					RowR0Equivalent+=RowRFree0Equivalent +" \\\\ \\hline \n ";
					
					
					//RowR5 += "\\\\ \\hline \n ";
					RowR5 += " \\\\  \n ";
					RowR5+=RowRFree5 +" \\\\ \\hline \n ";
					
					RowR5Equivalent += " \\\\  \n ";
					RowR5Equivalent+=RowRFree5Equivalent +" \\\\ \\hline \n ";
				//System.out.println(RowR5Equivalent);
				}
				//System.out.println(Col);
				//System.out.println(RowCom0);
				Table += Col + " \\\\ \\hline \n ";
				Table += RowCom0;
				Table=ShadedTable(FormatingPipelinesNames(Table,true))+" \n "+Comments;
				new Preparer().WriteTxtFile("Latex/MatrixOfResults" + Folder.getName() + "Com0.tex", Table);
			
				String TableCom5 = Col + "\\\\ \\hline \n ";
				TableCom5 += RowCom5;
				TableCom5=ShadedTable(FormatingPipelinesNames(TableCom5,true))+" \n "+Comments; 
				new Preparer().WriteTxtFile("Latex/MatrixOfResults" + Folder.getName() + "Com5.tex", TableCom5);
				
				String TableCom0Equivalent = Col + "\\\\ \\hline \n ";
				TableCom0Equivalent += RowCom0Equivalent;
				TableCom0Equivalent=	ShadedTable(FormatingPipelinesNames(TableCom0Equivalent,true)) +" \n "+Comments;
				new Preparer().WriteTxtFile("Latex/MatrixOfResults" + Folder.getName() + "Com0Equivalent.tex", TableCom0Equivalent);
				
				String TableCom5Equivalent = Col + "\\\\ \\hline \n ";
				TableCom5Equivalent += RowCom5Equivalent;
				TableCom5Equivalent=	ShadedTable(FormatingPipelinesNames(TableCom5Equivalent,true)) +" \n "+Comments;
				new Preparer().WriteTxtFile("Latex/MatrixOfResults" + Folder.getName() + "Com5Equivalent.tex", TableCom5Equivalent);
			
				String TableCom0Models = Col + "\\\\ \\hline \n ";
				TableCom0Models += RowCom0Models;
				TableCom0Models=ShadedTable(FormatingPipelinesNames(TableCom0Models,true)) +" \n "+Comments;
				new Preparer().WriteTxtFile("Latex/MatrixOfResults" + Folder.getName() + "Com0Models.tex", TableCom0Models);
				
				
				String TableCom5Models = Col + "\\\\ \\hline \n ";
				TableCom5Models += RowCom5Models;
				TableCom5Models=ShadedTable(FormatingPipelinesNames(TableCom5Models,true)) +" \n "+Comments;
				new Preparer().WriteTxtFile("Latex/MatrixOfResults" + Folder.getName() + "Com5Models.tex", TableCom5Models);
				
				
				String TableRModels = Col + "\\\\ \\hline \n ";
				TableRModels += RowR0;
				TableRModels=ShadedTable(FormatingPipelinesNames(TableRModels,true)) +" \n "+Comments;
				new Preparer().WriteTxtFile("Latex/MatrixOfResults" + Folder.getName() + "RModels.tex", TableRModels);
				
				
				String TableRModelsEquivalent = Col + "\\\\ \\hline \n ";
				TableRModelsEquivalent += RowR0Equivalent;
				TableRModelsEquivalent=ShadedTable(FormatingPipelinesNames(TableRModelsEquivalent,true)) +" \n "+Comments;
				new Preparer().WriteTxtFile("Latex/MatrixOfResults" + Folder.getName() + "REquivalentModels.tex", TableRModelsEquivalent);
			
			
				String TableR5Models = Col + "\\\\ \\hline \n ";
				TableR5Models += RowR5;
				TableR5Models=ShadedTable(FormatingPipelinesNames(TableR5Models,true)) +" \n "+Comments;
				new Preparer().WriteTxtFile("Latex/MatrixOfResults" + Folder.getName() + "R5Models.tex", TableR5Models);
			
			
				String TableRModelsEquivalent5 = Col + "\\\\ \\hline \n ";
				TableRModelsEquivalent5 += RowR5Equivalent;
				TableRModelsEquivalent5=ShadedTable(FormatingPipelinesNames(TableRModelsEquivalent5,true)) +" \n "+Comments;
				new Preparer().WriteTxtFile("Latex/MatrixOfResults" + Folder.getName() + "REquivalent5Models.tex", TableRModelsEquivalent5);
				
				
				new RunComparison().CheckDirAndFile("MatricesLogs");
				new Preparer().WriteTxtFile("MatricesLogs/" + Folder.getName() + "LogOfR5Equivalent.txt", FormatingPipelinesNames(LogOfR5Equivalent,false));
				
				new Preparer().WriteTxtFile("MatricesLogs/" + Folder.getName() + "LogOfCom0.txt", FormatingPipelinesNames(LogOfCom0,false));

				new Preparer().WriteTxtFile("MatricesLogs/" + Folder.getName() + "LogOfCom0Equivalent.txt", FormatingPipelinesNames(LogOfCom0Equivalent,false));
				
				new Preparer().WriteTxtFile("MatricesLogs/" + Folder.getName() + "LogOfCom5.txt", FormatingPipelinesNames(LogOfCom5,false));
				new Preparer().WriteTxtFile("MatricesLogs/" + Folder.getName() + "LogOfCom5Equivalent.txt", FormatingPipelinesNames(LogOfCom5Equivalent,false));
				new Preparer().WriteTxtFile("MatricesLogs/" + Folder.getName() + "LogOfR0.txt", FormatingPipelinesNames(LogOfR0,false));
				new Preparer().WriteTxtFile("MatricesLogs/" + Folder.getName() + "LogOfRFree0.txt", FormatingPipelinesNames(LogOfRFree0,false));

					  
				
				new Preparer().WriteTxtFile("MatricesLogs/" + Folder.getName() + "LogOfR0Equivalent.txt", FormatingPipelinesNames(LogOfR0Equivalent,false));
						new Preparer().WriteTxtFile("MatricesLogs/" + Folder.getName() + "LogOfRFree0Equivalent.txt", FormatingPipelinesNames(LogOfRFree0Equivalent,false));

						new Preparer().WriteTxtFile("MatricesLogs/" + Folder.getName() + "LogOfR5.txt", FormatingPipelinesNames(LogOfR5,false));
	
						
						new Preparer().WriteTxtFile("MatricesLogs/" + Folder.getName() + "LogOfRFree5.txt", FormatingPipelinesNames(LogOfRFree5,false));
						
						new Preparer().WriteTxtFile("MatricesLogs/" + Folder.getName() + "LogOfRFree5Equivalent.txt", FormatingPipelinesNames(LogOfRFree5Equivalent,false));

			}

		}

	}
	String AddingAvgToTheTable(String Table) {
		 String a = Table;
		 String [] Lines= a.split("\n");
		 Vector<String> LinesAvg = new Vector<String>(); 
		 int i=0;
		
		 for(String Line : Lines) {
			 System.out.println(Line);
			 double sumofTheLine=0;
			 double NumberOfPipelien=0;
			 if(i!=0 && Line.trim().length()!=0) {
			 for(String Number : Line.split(" ")) {
				 if(Number.matches("[0-9]+")){
					 sumofTheLine+= Integer.valueOf(Number);
					 NumberOfPipelien++;
				 }
			 }
			 LinesAvg.add(String.valueOf(Math.round((sumofTheLine/NumberOfPipelien))));
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
		 
System.out.println("ShadedTable");
String a = Table;
System.out.println(a);
//a=a.replaceAll("\\\\%", "");
//a=a.replaceAll("\\btiny\\b", "");
//a=a.replace("\\b\\\\b", "");
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
		//FormattedTable+="\\cellcolor{black!35} \\tiny "+e+" ";
		FormattedTable+=" \\tiny "+e+" ";
		
	}
	else if(e.matches("[0-9]+") && Integer.valueOf(e)  > Math.round(Sum/NumberCount)) {
	//FormattedTable+="\\cellcolor{gray!35} \\tiny "+e+" ";
	FormattedTable+=" \\tiny "+e+" ";
		
	
}
else {
	if(!e.matches("[0-9]+"))
	FormattedTable+=e+" ";
	else
		FormattedTable+=" \\tiny "+e+" ";
}
}
System.out.println(FormattedTable);
return FormattedTable;

//System.out.println(Sum);
//System.out.println(NumberCount);
//System.out.println(Sum/NumberCount);
	}
	String FormatingPipelinesNames(String Table, boolean RemoveDatasetNames) {
		if(RemoveDatasetNames==true) {
		Table=Table.replaceAll("hancs", "");
		Table=Table.replaceAll("mrncs", "");
		Table=Table.replaceAll("noncs", "");
		}
		
		Table=Table.replaceAll(".xlsx", "");
		Table=Table.replaceAll("\\bARPwARPB25\\b", "ARP(B 25I)");
		Table=Table.replaceAll("\\bARPwARPB5\\b", "ARP(B 5I)");
		Table=Table.replaceAll("\\bARPwARP\\b", "ARP");
		Table=Table.replaceAll("\\bBuccaneeri1-25\\b", "i1(25I)");
		Table=Table.replaceAll("\\bBuccaneeri2-25\\b", "i2(25I)");
		Table=Table.replaceAll("\\bBuccaneeri1-5\\b", "i1(5I)");
		Table=Table.replaceAll("\\bBuccaneeri2-5\\b", "i2(5I)");
		Table=Table.replaceAll("\\bBuccaneeri2W-25\\b", "i2W(25I)");
		Table=Table.replaceAll("\\bBuccaneeri2W-5\\b", "i2W(5I)");
		Table=Table.replaceAll("\\bPhenix\\b", "Phenix/Parrot");
		Table=Table.replaceAll("\\bPhenixUnmodifiedPhases\\b", "Phenix");
		
		/*
		Table=Table.replaceAll(".xlsx", "");
		Table=Table.replaceAll("ARPwARPB25", "ARP/wARP(i1(25I))");
		Table=Table.replaceAll("ARPwARPB5", "ARP/wARP(i1(5I))");
		Table=Table.replaceAll("ARPwARP", "ARP/wARP");
		Table=Table.replaceAll("Buccaneeri1-25", "Bucc-i1(25I)");
		Table=Table.replaceAll("Buccaneeri2-25", "Bucc-i2(25I)");
		Table=Table.replaceAll("Buccaneeri1-5", "Bucc-i1(5I)");
		Table=Table.replaceAll("Buccaneeri2-5", "Bucc-i2(5I)");
		Table=Table.replaceAll("Buccaneeri2W-25", "Bucc-i1W(25I)");
		Table=Table.replaceAll("Buccaneeri2W-5", "Bucc-i2W(5I)");
		Table=Table.replaceAll("PhenixUnmodifiedPhases", "Phenix(UN)");
		*/
		return Table;
	}

	void LongMatrixOfResults(String ResultsDir) throws IOException {
		File[] Folders = new File(ResultsDir).listFiles();
		
		DecimalFormat decim = new DecimalFormat("#.##");
		
		Vector<Vector<Vector<DataContainer>>> AllDatasetContainer = new Vector<Vector<Vector<DataContainer>>>();
		Vector<String> DatasetNames = new Vector<String>();
		Vector<ExcelLoader> ExcelNames = new Vector<ExcelLoader>();
		Vector<String> ExcelNamesAsInTableHeader = new Vector<String>();
		String TableHeader = "\\tiny Pipelines && ";
		String ImprovemedBy = "\\tiny Improvemed By & ";

		for (File Folder : Folders) {

			if (Folder.isDirectory() && !Folder.getName().equals("Headers")) {

				ExcelLoader e = new ExcelLoader();

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