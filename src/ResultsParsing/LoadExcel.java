package ResultsParsing;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;
import java.util.List;


import Run.RunComparison;
import table.draw.Block;
import table.draw.Board;
import table.draw.LogFile;
import table.draw.Table;

import java.util.ArrayList;
import java.util.Arrays;

public class LoadExcel {
	String PlotsPath;
	public Vector<String> ToolsNames = new Vector<String>();
	String Tables;
	Vector<DataContainer> Phenixx;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		LoadExcel e = new LoadExcel();
		//e.Phenixx = new Vector<DataContainer>();
		//e.Phenixx = e.ReadExcel("/Users/emadalharbi/Desktop/PhDYork/jcsg202Results/ToolsResults12_3_2018/Phenix.xlsx");

		Vector<Vector<DataContainer>> Container = new Vector<Vector<DataContainer>>();
	
		/*
		 * Container.add(e.CheckPDBexists(e.Phenixx,e.ReadExcel(
		 * "/Users/emadalharbi/Desktop/PhDYork/jcsg202Results/Buccaneer.xlsx")));
		 * //System.out.println(Container.size());
		 * 
		 * Container.add(e.CheckPDBexists(e.Phenixx,e.ReadExcel(
		 * "/Users/emadalharbi/Desktop/PhDYork/jcsg202Results/ARPwARPFom.xlsx")));
		 * //System.out.println(Container.size());
		 * Container.add(e.CheckPDBexists(e.Phenixx,e.ReadExcel(
		 * "/Users/emadalharbi/Desktop/PhDYork/jcsg202Results/Phenix.xlsx")));
		 * //System.out.println(Container.size()); //Container.add(e.ReadExcel(
		 * "/Users/emadalharbi/Desktop/PhDYork/jcsg202Results/FullResultsSheetsOld/BuccaneerMac.xlsx"
		 * ));
		 */
/*
		Container.add(e.ReadExcel("/Users/emadalharbi/Desktop/PhDYork/jcsg202Results/Buccaneer.xlsx"));
		// System.out.println(Container.size());

		Container.add(e.ReadExcel("/Users/emadalharbi/Desktop/PhDYork/jcsg202Results/ARPwARP.xlsx"));
		// System.out.println(Container.size());
		// Container.add(e.ReadExcel("/Users/emadalharbi/Desktop/PhDYork/jcsg202Results/Phenix.xlsx"));
		// System.out.println(Container.size());
		// Container.add(e.ReadExcel("/Users/emadalharbi/Desktop/PhDYork/jcsg202Results/FullResultsSheetsOld/BuccaneerMac.xlsx"));
		/*
		Container.add(e.ReadExcel("/Users/emadalharbi/Desktop/PhDYork/jcsg202Results/ARPwARPAfterBucc.xlsx"));
		Container.add(e.ReadExcel("/Users/emadalharbi/Desktop/PhDYork/jcsg202Results/ARPwARPModifiedSeq.xlsx"));
		Container.add(e.ReadExcel("/Users/emadalharbi/Desktop/PhDYork/jcsg202Results/ARPwARPAfterSeqAndBucc.xlsx"));
*/
		e.PlotsPath = "Plots";
		File[] Excels = new File("/Volumes/PhDHardDrive/jcsg1200Results/All").listFiles();
		 for (File excel : Excels) {
			System.out.println(excel.getAbsolutePath());
			 Container.add(e.ReadExcel(excel.getAbsolutePath()));
			
			 e.ToolsNames.add(excel.getName());
			// Container.add(e.CheckPDBexists(e.Phenixx,e.ReadExcel(
				//	 excel.getAbsolutePath())));
		 }
		 Vector<Vector<DataContainer>> Container2 = new Vector<Vector<DataContainer>>();
		 for(int i=0 ; i < Container.size() ; ++i) {
			
			 
			 Container2.addElement(e.CheckPDBexists(Container,Container.get(i)));
			 System.out.println(Container2.get(Container2.size()-1).size());
		 }
		
		 Container.clear();
		 Container.addAll(Container2);
		new RunComparison().CheckDirAndFile("Plots");
		
		for(int i=0 ; i < Container.size() ; ++i) {
		Collections.sort(Container.get(i),DataContainer.DataContainerComparator);
		
		}
		/*
		e.ToolsNames.add("BuccaneerYarcc");
		e.ToolsNames.add("ARPwARP");
		// e.ToolsNames.add("Phenix");
		e.ToolsNames.add("ARPwARPAfterBucc");
		e.ToolsNames.add("ARPwARPModifiedSeq");
		e.ToolsNames.add("ARPwARPAfterSeqAndBucc");
		// e.ToolsNames.add("BuccaneerMac");
		// e.RfacorsFori1(Container);
*/
		
		e.HowManyModelsAreBuilt(Container);
		//e.HowManyModelsAreBuiltByReso(Container);
		// e.VisualTheDataSet(Container);
		
		e.ResolutionVsRFactor(Container);
		//e.PhasesVsRFactor(Container);
		e.RFactorComparsion(Container);
		//e.PhasesVsTimeTaking(Container);
		e.TimeTakingComaprsion(Container);
		e.NumberofAtomsVsTimeTaking(Container);
		e.NumberofAtomsPDB1VsNumberofAtomsPDB2(Container);
		e.NumberofAtomsPDB1AndInNumberofAtomsPDB2(Container);
		e.NumberofAtomsPDB1AndInNumberofAtomsPDB2InsameSequnce(Container);
		e.RFactorVsOpmtimalRComparsion(Container);
		e.ResoVsNumberofAtomsPDB2(Container);
		e.BoxPlotsNumberofAtoms(Container);
		e.BoxPlotsNumberofAtomsInWellKnown(Container);
		e.BoxPlotsNumberofAtomsInWellKnownSameSeq(Container);
		e.BoxPlotsRfactor(Container);
		//e.PhasesVsNumberofAtomsInSeq(Container);
	    //e.WorstestCasesInSeq(Container);
		e.LineChartForToolsPerformance(Container);
		
		e.LineChartMolScore(Container);
		e.BoxPlotsMolProbity(Container);
		e.BoxPlotsMolProbityClashScore(Container);
		e.PrintPDBID(Container);
		
	}

	void PrintPDBID(Vector<Vector<DataContainer>> Container) {
		for(int i=0 ; i < Container.get(0).size() ; ++i) {
			System.out.println(Container.get(0).get(i).PDB_ID);
			System.out.print("  " +Container.get(0).get(i).Resolution);
			System.out.println();
		}
	}
	void HowManyModelsAreBuilt(Vector<Vector<DataContainer>> Container) throws IOException {
		Plot P = new Plot();
		Vector<Double> X = new Vector<Double>();
		Vector<Double> Y = new Vector<Double>();

		ArrayList<ArrayList<String>> ListOfList = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < Container.size(); ++i) {
			int CountModel = 0;
			ArrayList<String> list = new ArrayList<String>();
			for (int m = 0; m < Container.get(i).size(); ++m) {
				X.add(Double.parseDouble(Container.get(i).get(m).Resolution));
				if (Container.get(i).get(m).BuiltPDB.equals("T")) {
					CountModel++;
					Y.add(2.0);
				} else {
					Y.add(1.0);
				}

			}
			P.AddSeries(ToolsNames.get(i), X, Y);
			P.CreateScatterPlot(PlotsPath + "/How Many Models Are Built", "Resolution", "Built?");
			// System.out.println("Tool "+ ToolsNames.get(i));
			// System.out.println("Succussed Models " + CountModel);
			// System.out.println("Failed Models " + (Container.get(i).size()-CountModel));
			// System.out.println((CountModel * 100) / Container.get(i).size()+" %");
			 System.out.println(CountModel);
			list.add(ToolsNames.get(i));
			list.add(String.valueOf(Container.get(i).size()));
			list.add(String.valueOf(CountModel) + " ("
					+ String.valueOf((CountModel * 100) / Container.get(i).size() + "%") + ")");
			list.add(String.valueOf(Container.get(i).size() - CountModel + " ("
					+ String.valueOf(((Container.get(i).size() - CountModel) * 100) / Container.get(i).size() + "%")
					+ ")"));
			ListOfList.add(list);
		}

		Tables = new LogFile().NumberofModelsBuiltTable(ListOfList);

	}

	void HowManyModelsAreBuiltByReso(Vector<Vector<DataContainer>> Container) throws FileNotFoundException {

		ArrayList<String> Headers = new ArrayList<String>();
		ArrayList<ArrayList<String>> ListOfList = new ArrayList<ArrayList<String>>();
		Headers.add("Pipeline");
		for (int i = 0; i < Container.size(); ++i) {
			Vector<Integer> Reso = new Vector<Integer>();
			Vector<Integer> NumReso = new Vector<Integer>();
			Vector<Integer> NumOfSuccussed = new Vector<Integer>();
			System.out.println(ToolsNames.get(i));
			for (int m = 0; m < Container.get(i).size(); ++m) {

				Double Value = Double.parseDouble(Container.get(i).get(m).Resolution);

				if (Reso.contains(Value.intValue())) {

					NumReso.set(Reso.indexOf(Value.intValue()), NumReso.get(Reso.indexOf(Value.intValue())) + 1);
					if (Container.get(i).get(m).BuiltPDB.equals("T"))
						NumOfSuccussed.set(Reso.indexOf(Value.intValue()),
								NumOfSuccussed.get(Reso.indexOf(Value.intValue())) + 1);

				} else {

					Reso.add(Value.intValue());
					NumReso.add(1);
					if (Container.get(i).get(m).BuiltPDB.equals("T"))
						NumOfSuccussed.add(1);
					else
						NumOfSuccussed.add(0);
				}

			}

			ArrayList<String> list = new ArrayList<String>();
			list.add(ToolsNames.get(i));
			for (int m = 0; m < Reso.size(); ++m) {
				// System.out.println("Reso "+ Reso.get(m));
				// System.out.println("Reso Num "+ NumReso.get(m));
				// System.out.println("Succussed Num "+ NumOfSuccussed.get(m));
				// System.out.println((NumOfSuccussed.get(m) * 100) / NumReso.get(m)+" %");
				// String HeaderTitle="~"+String.valueOf(Reso.get(m))+"Å";
				String HeaderTitle = "From " + String.valueOf(Reso.get(m)) + "Å < " + String.valueOf(Reso.get(m) + 1)
						+ "Å";
				if (!Headers.contains(HeaderTitle)) {

					Headers.add(HeaderTitle);
				}

				list.add("Built:" + String.valueOf(NumReso.get(m)) + " Failed:"
						+ (NumReso.get(m) - NumOfSuccussed.get(m)));
			}
			ListOfList.add(list);

		}
		Tables += new LogFile().NumberofModelsBuiltByResoTable(Headers, ListOfList);
		try (PrintWriter out = new PrintWriter("Tables.txt")) {
			out.println(Tables);
		}
	}

	void BoxPlotsNumberofAtoms(Vector<Vector<DataContainer>> Container) throws IOException {

		// int Series=Container.size();
		DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();
		// Vector<List>lists = new ArrayList();

		for (int i = 0; i < Container.size(); ++i) {
			Vector<Integer> Reso = new Vector<Integer>();
			Vector<List> lists = new Vector<List>();
			System.out.println(ToolsNames.get(i));
			for (int m = 0; m < Container.get(i).size(); ++m) {

				Double Value = Double.parseDouble(Container.get(i).get(m).Resolution);

				if (Reso.contains(Value.intValue())) {
					if (!Container.get(i).get(m).NumberofAtomsinSecondPDB.equals("None"))
						lists.get(Reso.indexOf(Value.intValue()))
								.add(new Double(Container.get(i).get(m).NumberofAtomsinSecondPDB));
					else
						lists.get(Reso.indexOf(Value.intValue())).add(new Double(0.0));
				} else {

					Reso.add(Value.intValue());
					List list = new ArrayList();
					lists.add(list);
					if (!Container.get(i).get(m).NumberofAtomsinSecondPDB.equals("None"))
						lists.get(Reso.size() - 1).add(new Double(Container.get(i).get(m).NumberofAtomsinSecondPDB));
					else
						lists.get(Reso.indexOf(Value.intValue())).add(new Double(0.0));
				}

			}
			for (int l = 0; l < Reso.size(); ++l) {
				String ResoLabel = "From " + String.valueOf(Reso.get(l)) + "Å < " + String.valueOf(Reso.get(l) + 1)
						+ "Å";
				dataset.add(lists.get(l), ToolsNames.get(i), ResoLabel);
				System.out.println("Reso " + Reso.get(l));
				// for(int n= 0 ; n <lists.get(l).size() ; ++n ) {
				// System.out.println(lists.get(l).get(n));
				// }
			}

		}
		Plot P = new Plot();
		P.CreateBoxPlot(PlotsPath + "/BoxPlotNumberofAtomsinSecondPDB", " Resolution ",
				"Number of Atoms in Second PDB ", dataset);
	}

	void BoxPlotsNumberofAtomsInWellKnownSameSeq(Vector<Vector<DataContainer>> Container) throws IOException {

		// int Series=Container.size();
		DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();
		// Vector<List>lists = new ArrayList();

		for (int i = 0; i < Container.size(); ++i) {
			Vector<Integer> Reso = new Vector<Integer>();
			Vector<List> lists = new Vector<List>();
			
			for (int m = 0; m < Container.get(i).size(); ++m) {

				Double Value = Double.parseDouble(Container.get(i).get(m).Resolution);

				if (Reso.contains(Value.intValue())) {
					if (!Container.get(i).get(m).n2m1.equals("None")) {
						lists.get(Reso.indexOf(Value.intValue())).add(new Double(Container.get(i).get(m).n2m1));
						System.out.println(ToolsNames.get(i));
					}
						else
						lists.get(Reso.indexOf(Value.intValue())).add(new Double(0.0));
				} else {

					Reso.add(Value.intValue());
					List list = new ArrayList();
					lists.add(list);
					if (!Container.get(i).get(m).n2m1.equals("None"))
						lists.get(Reso.indexOf(Value.intValue())).add(new Double(Container.get(i).get(m).n2m1));
						//lists.get(Reso.size() - 1).add(new Double(Container.get(i).get(m).n2m1));
					else
						lists.get(Reso.indexOf(Value.intValue())).add(new Double(0.0));
				}

			}
			for (int l = 0; l < Reso.size(); ++l) {
				String ResoLabel = "From " + String.valueOf(Reso.get(l)) + "Å < " + String.valueOf(Reso.get(l) + 1)
						+ "Å";
				dataset.add(lists.get(l), ToolsNames.get(i), ResoLabel);
				System.out.println("ResoLabel "+ResoLabel);
				System.out.println("Num "+lists.get(l).size());
				// System.out.println("Reso "+Reso.get(l));
				// for(int n= 0 ; n <lists.get(l).size() ; ++n ) {
				// System.out.println(lists.get(l).get(n));
				// }
			}

		}
		Plot P = new Plot();
		P.CreateBoxPlot(PlotsPath + "/BoxPlotNumberofAtomsinSecondPDBAndInWellKnownModelSameSequence", " Resolution ",
				"Number of Atoms in Second PDB and in well known model (Same Sequence) ", dataset);
	}

	void BoxPlotsNumberofAtomsInWellKnown(Vector<Vector<DataContainer>> Container) throws IOException {

		// int Series=Container.size();
		DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();
		// Vector<List>lists = new ArrayList();

		for (int i = 0; i < Container.size(); ++i) {
			Vector<Integer> Reso = new Vector<Integer>();
			Vector<List> lists = new Vector<List>();
			System.out.println(ToolsNames.get(i));
			for (int m = 0; m < Container.get(i).size(); ++m) {

				Double Value = Double.parseDouble(Container.get(i).get(m).Resolution);

				if (Reso.contains(Value.intValue())) {
					if (!Container.get(i).get(m).NumberOfAtomsInSecondNotInFirst.equals("None"))
						lists.get(Reso.indexOf(Value.intValue()))
								.add(new Double(Container.get(i).get(m).NumberOfAtomsInSecondNotInFirst));
					else
						lists.get(Reso.indexOf(Value.intValue())).add(new Double(0.0));
				} else {

					Reso.add(Value.intValue());
					List list = new ArrayList();
					lists.add(list);
					if (!Container.get(i).get(m).NumberOfAtomsInSecondNotInFirst.equals("None"))
						lists.get(Reso.size() - 1)
								.add(new Double(Container.get(i).get(m).NumberOfAtomsInSecondNotInFirst));
					else
						lists.get(Reso.indexOf(Value.intValue())).add(new Double(0.0));
				}

			}
			for (int l = 0; l < Reso.size(); ++l) {
				String ResoLabel = "From " + String.valueOf(Reso.get(l)) + "Å < " + String.valueOf(Reso.get(l) + 1)
						+ "Å";
				dataset.add(lists.get(l), ToolsNames.get(i), ResoLabel);
				// System.out.println("Reso "+Reso.get(l));
				// for(int n= 0 ; n <lists.get(l).size() ; ++n ) {
				// System.out.println(lists.get(l).get(n));
				// }
			}

		}
		Plot P = new Plot();
		P.CreateBoxPlot(PlotsPath + "/BoxPlotNumberofAtomsinSecondPDBAndInWellKnownModel", " Resolution ",
				"Number of Atoms in Second PDB and in well known model ", dataset);
	}

	
	void BoxPlotsMolProbity(Vector<Vector<DataContainer>> Container) throws IOException {

		// int Series=Container.size();
		DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();
		// Vector<List>lists = new ArrayList();

		for (int i = 0; i < Container.size(); ++i) {
			Vector<Integer> Reso = new Vector<Integer>();
			Vector<List> lists = new Vector<List>();
			System.out.println(ToolsNames.get(i));
			for (int m = 0; m < Container.get(i).size(); ++m) {

				Double Value = Double.parseDouble(Container.get(i).get(m).Resolution);

				if (Reso.contains(Value.intValue())) {
		 if (!Container.get(i).get(m).molProbityData.MolProbityScore.equals("None"))
				lists.get(Reso.indexOf(Value.intValue()))
								.add(new Double(Container.get(i).get(m).molProbityData.MolProbityScore));
					else
						lists.get(Reso.indexOf(Value.intValue())).add(new Double(0.0));
				} else {

					Reso.add(Value.intValue());
					List list = new ArrayList();
					lists.add(list);
					if (!Container.get(i).get(m).molProbityData.MolProbityScore.equals("None"))
						lists.get(Reso.size() - 1)
								.add(new Double(Container.get(i).get(m).molProbityData.MolProbityScore));
					else
						lists.get(Reso.indexOf(Value.intValue())).add(new Double(0.0));
				}

			}
			for (int l = 0; l < Reso.size(); ++l) {
				String ResoLabel = "From " + String.valueOf(Reso.get(l)) + "Å < " + String.valueOf(Reso.get(l) + 1)
						+ "Å";
				dataset.add(lists.get(l), ToolsNames.get(i), ResoLabel);
				// System.out.println("Reso "+Reso.get(l));
				// for(int n= 0 ; n <lists.get(l).size() ; ++n ) {
				// System.out.println(lists.get(l).get(n));
				// }
			}

		}
		Plot P = new Plot();
		P.CreateBoxPlot(PlotsPath + "/BoxPlot Molprobity Score", " Resolution ",
				"MolProbity Score ", dataset);
	}
	
	
	
	void BoxPlotsMolProbityClashScore(Vector<Vector<DataContainer>> Container) throws IOException {

		// int Series=Container.size();
		DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();
		// Vector<List>lists = new ArrayList();

		for (int i = 0; i < Container.size(); ++i) {
			Vector<Integer> Reso = new Vector<Integer>();
			Vector<List> lists = new Vector<List>();
			System.out.println(ToolsNames.get(i));
			for (int m = 0; m < Container.get(i).size(); ++m) {

				Double Value = Double.parseDouble(Container.get(i).get(m).Resolution);

				if (Reso.contains(Value.intValue())) {
		 if (!Container.get(i).get(m).molProbityData.Clashscore.equals("None"))
				lists.get(Reso.indexOf(Value.intValue()))
								.add(new Double(Container.get(i).get(m).molProbityData.Clashscore));
					else
						lists.get(Reso.indexOf(Value.intValue())).add(new Double(0.0));
				} else {

					Reso.add(Value.intValue());
					List list = new ArrayList();
					lists.add(list);
					if (!Container.get(i).get(m).molProbityData.Clashscore.equals("None"))
						lists.get(Reso.size() - 1)
								.add(new Double(Container.get(i).get(m).molProbityData.Clashscore));
					else
						lists.get(Reso.indexOf(Value.intValue())).add(new Double(0.0));
				}

			}
			for (int l = 0; l < Reso.size(); ++l) {
				String ResoLabel = "From " + String.valueOf(Reso.get(l)) + "Å < " + String.valueOf(Reso.get(l) + 1)
						+ "Å";
				dataset.add(lists.get(l), ToolsNames.get(i), ResoLabel);
				// System.out.println("Reso "+Reso.get(l));
				// for(int n= 0 ; n <lists.get(l).size() ; ++n ) {
				// System.out.println(lists.get(l).get(n));
				// }
			}

		}
		Plot P = new Plot();
		P.CreateBoxPlot(PlotsPath + "/BoxPlot Molprobity Clash Score", " Resolution ",
				"MolProbity Clash Score ", dataset);
	}
	
	
	void BoxPlotsRfactor(Vector<Vector<DataContainer>> Container) throws IOException {

		// int Series=Container.size();
		DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();
		// Vector<List>lists = new ArrayList();

		for (int i = 0; i < Container.size(); ++i) {
			Vector<Integer> Reso = new Vector<Integer>();
			Vector<List> lists = new Vector<List>();
			System.out.println(ToolsNames.get(i));
			for (int m = 0; m < Container.get(i).size(); ++m) {

				Double Value = Double.parseDouble(Container.get(i).get(m).Resolution);

				if (Reso.contains(Value.intValue())) {
					if (!Container.get(i).get(m).R_factor0Cycle.equals("None"))
						lists.get(Reso.indexOf(Value.intValue()))
								.add(new Double(Container.get(i).get(m).R_factor0Cycle));
					else
						lists.get(Reso.indexOf(Value.intValue())).add(new Double(0.0));
				} else {

					Reso.add(Value.intValue());
					List list = new ArrayList();
					lists.add(list);
					if (!Container.get(i).get(m).R_factor0Cycle.equals("None"))
						lists.get(Reso.size() - 1).add(new Double(Container.get(i).get(m).R_factor0Cycle));
					else
						lists.get(Reso.indexOf(Value.intValue())).add(new Double(0.0));
				}

			}
			for (int l = 0; l < Reso.size(); ++l) {
				String ResoLabel = "From " + String.valueOf(Reso.get(l)) + "Å < " + String.valueOf(Reso.get(l) + 1)
						+ "Å";
				dataset.add(lists.get(l), ToolsNames.get(i), ResoLabel);
				// System.out.println("Reso "+Reso.get(l));
				// for(int n= 0 ; n <lists.get(l).size() ; ++n ) {
				// System.out.println(lists.get(l).get(n));
				// }
			}

		}
		Plot P = new Plot();
		P.CreateBoxPlot(PlotsPath + "/BoxPlotR_factor0Cycle", " Resolution ", "R-Factor ", dataset);
	}

	void VisualTheDataSet(Vector<Vector<DataContainer>> Container) throws IOException {
		Plot P = new Plot();
		Vector<Double> X = new Vector<Double>();
		Vector<Double> Y = new Vector<Double>();
		for (int i = 0; i < Container.size(); ++i) {

			for (int m = 0; m < Container.get(i).size(); ++m) {
				X.add(Double.parseDouble(Container.get(i).get(m).Resolution));
				Y.add(Double.parseDouble(Container.get(i).get(m).F_mapCorrelation));
			}
			P.AddSeries("Files", X, Y);
		}
		P.CreateScatterPlot(PlotsPath + "/Visualization The Dataset", "Resolution", "F_mapCorrelation");

	}

	void ResolutionVsRFactor(Vector<Vector<DataContainer>> Container) throws IOException {
		Plot P = new Plot();
		Vector<Double> X = new Vector<Double>();
		Vector<Double> Y = new Vector<Double>();
		for (int i = 0; i < Container.size(); ++i) {
			// System.out.println(ToolsNames.get(i));
			for (int m = 0; m < Container.get(i).size(); ++m) {
				// System.out.println(Container.get(i).get(m).PDB_ID);
				if (Container.get(i).get(m).BuiltPDB.equals("T")) {
					X.add(Double.parseDouble(Container.get(i).get(m).Resolution));
					Y.add(Double.parseDouble(Container.get(i).get(m).R_factor));
				}
			}
			P.AddSeries(ToolsNames.get(i), X, Y);
		}
		P.CreateScatterPlot(PlotsPath + "/Resolution vs R-Factor", "Resolution", "R-Factor");

	}

	void PhasesVsRFactor(Vector<Vector<DataContainer>> Container) throws IOException {
		Plot P = new Plot();
		Vector<Double> X = new Vector<Double>();
		Vector<Double> Y = new Vector<Double>();
		for (int i = 0; i < Container.size(); ++i) {

			for (int m = 0; m < Container.get(i).size(); ++m) {
				if (Container.get(i).get(m).BuiltPDB.equals("T")) {
					X.add(Double.parseDouble(Container.get(i).get(m).F_mapCorrelation));
					Y.add(Double.parseDouble(Container.get(i).get(m).R_factor));
				}
			}
			P.AddSeries(ToolsNames.get(i), X, Y);
		}
		P.CreateScatterPlot(PlotsPath + "/F_mapCorrelation vs R-Factor", "F_mapCorrelation", "R-Factor");

	}
	void PhasesVsNumberofAtomsInSeq(Vector<Vector<DataContainer>> Container) throws IOException {
		Plot P = new Plot();
		Vector<Double> X = new Vector<Double>();
		Vector<Double> Y = new Vector<Double>();
		for (int i = 0; i < Container.size(); ++i) {

			for (int m = 0; m < Container.get(i).size(); ++m) {
				if (Container.get(i).get(m).BuiltPDB.equals("T") ) {
					X.add(Double.parseDouble(Container.get(i).get(m).F_mapCorrelation));
					Y.add(Double.parseDouble(Container.get(i).get(m).n1m2));
				}
			}
			P.AddSeries(ToolsNames.get(i), X, Y);
		}
		P.CreateScatterPlot(PlotsPath + "/F_mapCorrelation vs Number of Atoms in Seq", "F_mapCorrelation", "Number of Atoms in Seq");

	}
	void RFactorComparsion(Vector<Vector<DataContainer>> Container) throws IOException {
		Plot P = new Plot();
		Vector<Double> X = new Vector<Double>();
		Vector<Double> Y = new Vector<Double>();
		for (int i = 0; i < Container.size(); ++i) {

			for (int m = 0; m < Container.get(i).size(); ++m) {
				if (Container.get(i).get(m).BuiltPDB.equals("T")) {
					X.add(Double.parseDouble(MinValue(Container, Container.get(i).get(m).PDB_ID, false)));
					Y.add(Double.parseDouble(Container.get(i).get(m).R_factor0Cycle));
				}
			}
			P.AddSeries(ToolsNames.get(i), X, Y);
		}
		P.CreateScatterPlot(PlotsPath + "/R_factor comparsion ", "Minimum R_factor", "R-Factor");

	}

	void RFactorVsOpmtimalRComparsion(Vector<Vector<DataContainer>> Container) throws IOException {
		Plot P = new Plot();
		Vector<Double> X = new Vector<Double>();
		Vector<Double> Y = new Vector<Double>();
		for (int i = 0; i < Container.size(); ++i) {

			for (int m = 0; m < Container.get(i).size(); ++m) {
				if (Container.get(i).get(m).BuiltPDB.equals("T")) {
					X.add(Double.parseDouble(Container.get(i).get(m).OptimalR_factor));
					Y.add(Double.parseDouble(Container.get(i).get(m).R_factor0Cycle));
				}
			}
			P.AddSeries(ToolsNames.get(i), X, Y);
		}
		for (int m = 0; m < Container.get(0).size(); ++m) {
			if (Container.get(0).get(m).BuiltPDB.equals("T")) {
				X.add(Double.parseDouble(Container.get(0).get(m).OptimalR_factor));
				Y.add(Double.parseDouble(Container.get(0).get(m).OptimalR_factor));
			}

		}
		P.AddSeries("Well known R-factor", X, Y);
		P.CreateScatterPlot(PlotsPath + "/RFactor Vs R-factor in the well known model ",
				"R-factor in the well known model", "R-Factor in the model built");

	}

	void PhasesVsTimeTaking(Vector<Vector<DataContainer>> Container) throws IOException {
		Plot P = new Plot();
		Vector<Double> X = new Vector<Double>();
		Vector<Double> Y = new Vector<Double>();
		for (int i = 0; i < Container.size(); ++i) {

			for (int m = 0; m < Container.get(i).size(); ++m) {
				if (Container.get(i).get(m).BuiltPDB.equals("T")) {
					X.add(Double.parseDouble(Container.get(i).get(m).F_mapCorrelation));
					Y.add(Double.parseDouble(Container.get(i).get(m).TimeTaking));
				}
			}
			P.AddSeries(ToolsNames.get(i), X, Y);
		}
		P.CreateScatterPlot(PlotsPath + "/F_mapCorrelation vs TimeTaking", "F_mapCorrelation", "TimeTaking");

	}

	void TimeTakingComaprsion(Vector<Vector<DataContainer>> Container) throws IOException {
		Plot P = new Plot();
		Vector<Double> X = new Vector<Double>();
		Vector<Double> Y = new Vector<Double>();
		for (int i = 0; i < Container.size(); ++i) {

			for (int m = 0; m < Container.get(i).size(); ++m) {
				if (Container.get(i).get(m).BuiltPDB.equals("T")) {
					X.add(Double.parseDouble(MinValue(Container, Container.get(i).get(m).PDB_ID, true)));
					Y.add(Double.parseDouble(Container.get(i).get(m).TimeTaking));
				}
			}
			P.AddSeries(ToolsNames.get(i), X, Y);
		}
		P.CreateScatterPlot(PlotsPath + "/TimeTaking comparsion ", "Min time taking", "TimeTaking");

	}

	void RfacorsFori1(Vector<Vector<DataContainer>> Container) throws IOException {
		Plot P = new Plot();
		Vector<Double> X = new Vector<Double>();
		Vector<Double> Y = new Vector<Double>();
		for (int i = 0; i < Container.size(); ++i) {

			for (int m = 0; m < Container.get(i).size(); ++m) {
				if (!Container.get(i).get(m).R_factor.equals("None")) {
					X.add(Double.parseDouble(MinValue(Container, Container.get(i).get(m).PDB_ID, false)));
					Y.add(Double.parseDouble(Container.get(i).get(m).R_factor0Cycle));
				}
			}
			P.AddSeries(ToolsNames.get(i), X, Y);
		}
		P.CreateScatterPlot(PlotsPath + "/R_factorYarcc vs R-FactorMac", "R_factorYarcc", "R-FactorMac");

	}

	void NumberofAtomsVsTimeTaking(Vector<Vector<DataContainer>> Container) throws IOException {
		Plot P = new Plot();
		Vector<Double> X = new Vector<Double>();
		Vector<Double> Y = new Vector<Double>();
		for (int i = 0; i < Container.size(); ++i) {

			for (int m = 0; m < Container.get(i).size(); ++m) {
				
				if (Container.get(i).get(m).BuiltPDB.equals("T")) {
					System.out.println(	ToolsNames.get(i));
					System.out.println(Container.get(i).get(m).PDB_ID);
					X.add(Double.parseDouble(Container.get(i).get(m).NumberofAtomsinSecondPDB));
					Y.add(Double.parseDouble(Container.get(i).get(m).TimeTaking));
				}
			}
			P.AddSeries(ToolsNames.get(i), X, Y);
		}
		P.CreateScatterPlot(PlotsPath + "/NumberofAtomsinSecondPDB vs TimeTaking", "NumberofAtomsinSecondPDB",
				"TimeTaking");

	}

	void NumberofAtomsPDB1VsNumberofAtomsPDB2(Vector<Vector<DataContainer>> Container) throws IOException {
		Vector<Integer> Reso = ResoRange(Container);
		for (int r = 0; r < Reso.size(); ++r) {
			Plot P = new Plot();
			Vector<Double> X = new Vector<Double>();
			Vector<Double> Y = new Vector<Double>();
			for (int i = 0; i < Container.size(); ++i) {

				for (int m = 0; m < Container.get(i).size(); ++m) {
					Double CaseReso = Double.parseDouble(Container.get(i).get(m).Resolution.trim());
					if (Container.get(i).get(m).BuiltPDB.equals("T") && Reso.get(r).equals(CaseReso.intValue())) {
						X.add(Double.parseDouble(Container.get(i).get(m).NumberofAtomsinFirstPDB));
						Y.add(Double.parseDouble(Container.get(i).get(m).NumberofAtomsinSecondPDB));
					}
				}
				P.AddSeries(ToolsNames.get(i), X, Y);
			}
			P.CreateScatterPlot(PlotsPath + "/NumberofAtomsPDB1 Vs NumberofAtomsPDB2(Resolution from " + Reso.get(r)
					+ " To <" + (Reso.get(r) + 1) + ")", "NumberofAtoms Well Known", "NumberofAtomsPDB2");
		}

	}

	void NumberofAtomsPDB1AndInNumberofAtomsPDB2(Vector<Vector<DataContainer>> Container) throws IOException {

		Vector<Integer> Reso = ResoRange(Container);
		for (int r = 0; r < Reso.size(); ++r) {
			Plot P = new Plot();
			Vector<Double> X = new Vector<Double>();
			Vector<Double> Y = new Vector<Double>();

			for (int i = 0; i < Container.size(); ++i) {

				for (int m = 0; m < Container.get(i).size(); ++m) {
					Double CaseReso = Double.parseDouble(Container.get(i).get(m).Resolution.trim());
					if (Container.get(i).get(m).BuiltPDB.equals("T") && Reso.get(r).equals(CaseReso.intValue())) {

						X.add(Double.parseDouble(MaxValue(Container, Container.get(i).get(m).PDB_ID, false)));
						Y.add(Double.parseDouble(Container.get(i).get(m).NumberOfAtomsInSecondNotInFirst));
					}
				}
				P.AddSeries(ToolsNames.get(i), X, Y);

			}
			P.CreateScatterPlot(
					PlotsPath + "/NumberofAtomsPDB1 and in NumberofAtomsPDB2 (Resolution from " + Reso.get(r) + " To <"
							+ (Reso.get(r) + 1) + ")",
					"Maximum number of atoms in the well known model", "NumberofAtomsPDB2");
		}

	}

	void NumberofAtomsPDB1AndInNumberofAtomsPDB2InsameSequnce(Vector<Vector<DataContainer>> Container)
			throws IOException {

		Vector<Integer> Reso = ResoRange(Container);
		for (int r = 0; r < Reso.size(); ++r) {
			Plot P = new Plot();
			Vector<Double> X = new Vector<Double>();
			Vector<Double> Y = new Vector<Double>();
			for (int i = 0; i < Container.size(); ++i) {

				for (int m = 0; m < Container.get(i).size(); ++m) {
					Double CaseReso = Double.parseDouble(Container.get(i).get(m).Resolution.trim());
					if (Container.get(i).get(m).BuiltPDB.equals("T") && Reso.get(r).equals(CaseReso.intValue())) {
						System.out.print(ToolsNames.get(i));
						System.out.print(" Reso= "+CaseReso.intValue());
System.out.print(" PDB_ID "+Container.get(i).get(m).PDB_ID);
System.out.println(" n2m1 "+Container.get(i).get(m).n2m1);
						X.add(Double.parseDouble(MaxValue(Container, Container.get(i).get(m).PDB_ID, true)));
						Y.add(Double.parseDouble(Container.get(i).get(m).n2m1));
					}
				}
				P.AddSeries(ToolsNames.get(i), X, Y);

			}
			P.CreateScatterPlot(
					PlotsPath + "/NumberofAtomsPDB1 and in NumberofAtomsPDB2 in same sequence (Resolution from "
							+ Reso.get(r) + " To <" + (Reso.get(r) + 1) + ")",
					"Maximum number of atoms in same sequnce in well known", "Number of Atoms in the model");
		}
	}

	void ResoVsNumberofAtomsPDB2(Vector<Vector<DataContainer>> Container) throws IOException {
		Plot P = new Plot();
		Vector<Double> X = new Vector<Double>();
		Vector<Double> Y = new Vector<Double>();
		for (int i = 0; i < Container.size(); ++i) {

			for (int m = 0; m < Container.get(i).size(); ++m) {
				if (Container.get(i).get(m).BuiltPDB.equals("T")) {

					X.add(Double.parseDouble(Container.get(i).get(m).Resolution));
					Y.add(Double.parseDouble(Container.get(i).get(m).NumberofAtomsinSecondPDB));
				}
			}
			P.AddSeries(ToolsNames.get(i), X, Y);

		}
		P.CreateScatterPlot(PlotsPath + "/Resolution Vs  Number of Atoms PDB2", "Resolution",
				"Number of Atoms in the model");

	}

	void LineChartForToolsPerformance(Vector<Vector<DataContainer>> Container) throws IOException {
		
		Plot P = new Plot();
	
	   
		 DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		 for (int i = 0; i < Container.size(); ++i) {
			 String series1 = ToolsNames.get(i);
			 Vector<DataContainer> BestModelInEachReso= new Vector<DataContainer>();
			 for (int m = 0; m < Container.get(i).size(); ++m) {
				 if(Container.get(i).get(m).PDB_ID.contains("2a3n")) {
				DataContainer.AddElemnet(BestModelInEachReso, Container.get(i).get(m));
				 }
			 }
			 for (int m = 0; m < BestModelInEachReso.size(); ++m) {
					dataset.addValue(Integer.valueOf(BestModelInEachReso.get(m).n2m1), series1, BestModelInEachReso.get(m).Resolution);

			 }
		 }
		
		/*
		for (int i = 0; i < 1; ++i) {
			Vector<DataContainer> BestModelInEachReso= new Vector<DataContainer>();
			String series1 = ToolsNames.get(i);
			for (int m = 0; m < Container.get(i).size(); ++m) {
				
					if(DataContainer.CheckIfResoIsInContainer(Container.get(i).get(m).Resolution, BestModelInEachReso)) {
						DataContainer ModelInfo=DataContainer.ReturnByReo(Container.get(i).get(m).Resolution, BestModelInEachReso);
					if(Integer.valueOf(ModelInfo.n2m1) < Integer.valueOf(Container.get(i).get(m).n2m1)) {
					
					BestModelInEachReso.remove(BestModelInEachReso.indexOf(ModelInfo));
					//BestModelInEachReso.add(Container.get(i).get(m));
					DataContainer.AddElemnet(BestModelInEachReso, Container.get(i).get(m));}
					}
					else {
						//BestModelInEachReso.add(Container.get(i).get(m));
						DataContainer.AddElemnet(BestModelInEachReso, Container.get(i).get(m));
					}
				
				
			}
			System.out.println("$$$$$$$");
			
	
			for (int m = 0; m < BestModelInEachReso.size(); ++m) {
				
				System.out.println(BestModelInEachReso.get(m).PDB_ID);
				System.out.println(BestModelInEachReso.get(m).Resolution);
				System.out.println(BestModelInEachReso.get(m).n2m1);
				dataset.addValue(Integer.valueOf(BestModelInEachReso.get(m).n2m1), series1, BestModelInEachReso.get(m).Resolution);
				
			}
		}
		*/
		//P.CreateLinePlot(PlotsPath + "/Line", "Resolution",
		//		"Number of Atoms in the model same seq",dataset);
		
		/*
		Plot P = new Plot();
		Vector<Double> X = new Vector<Double>();
		Vector<Double> Y = new Vector<Double>();
		for (int i = 0; i < Container.size(); ++i) {

			for (int m = 0; m < Container.get(i).size(); ++m) {
				if (Container.get(i).get(m).BuiltPDB.equals("T")) {

					X.add(Double.parseDouble(Container.get(i).get(m).Resolution));
					Y.add(Double.parseDouble(Container.get(i).get(m).NumberofAtomsinSecondPDB));
				}
			}
			P.AddSeries(ToolsNames.get(i), X, Y);

		}
		P.CreateScatterPlot(PlotsPath + "/Resolution Vs  Number of Atoms PDB2", "Resolution",
				"Number of Atoms in the model");
*/
	}
	
	
	
	
void LineChartMolScore(Vector<Vector<DataContainer>> Container) throws IOException {
		
		Plot P = new Plot();
	
		
		// DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		 XYSeriesCollection dataset = new XYSeriesCollection();
		 for (int i = 0; i < Container.size(); ++i) {
			 //String series1 = ToolsNames.get(i);
			 XYSeries series = new XYSeries(ToolsNames.get(i));
			 XYSeries seriesOp = new XYSeries("opmtimal");
			 for (int m = 0; m < Container.get(i).size(); ++m) {
				
				//DataContainer.AddElemnet(BestModelInEachReso, Container.get(i).get(m));
				 if(!Container.get(i).get(m).molProbityData.MolProbityScore.equals("None")) {
					// System.out.println(Container.get(i).get(m).Resolution);
					 series.add(Double.valueOf(Container.get(i).get(m).Resolution), Double.valueOf(Container.get(i).get(m).molProbityData.MolProbityScore));	
					 seriesOp.add(Double.valueOf(Container.get(i).get(m).Resolution), Double.valueOf(Container.get(i).get(m).Resolution));	

					 //dataset.addValue(Double.valueOf(Container.get(i).get(m).molProbityData.MolProbityScore), series1, Double.valueOf(Container.get(i).get(m).Resolution));
				 } 
			 }
			 dataset.addSeries(series);
			 if(i==0)// we need only one 
			 dataset.addSeries(seriesOp);
		 }
		
		
		P.CreateLinePlot(PlotsPath + "/MolScore", "Resolution",
				"MolProbity Score",dataset);
	
	}
	
	

	
	
	String MaxValue(Vector<Vector<DataContainer>> Container, String PDBID, boolean Seq) {
		int Max = 0;
		String PDBid = "";
		for (int i = 0; i < Container.size(); ++i) {

			for (int m = 0; m < Container.get(i).size(); ++m) {
				if (Container.get(i).get(m).PDB_ID.equals(PDBID) && Container.get(i).get(m).BuiltPDB.equals("T")) {
					if (Seq == false) {
						if (Max < Integer.parseInt(Container.get(i).get(m).NumberOfAtomsInFirstNotInSecond)) {
							Max = Integer.parseInt(Container.get(i).get(m).NumberOfAtomsInFirstNotInSecond);
							PDBid = Container.get(i).get(m).PDB_ID;
						}
					} else {
						if (Max < Integer.parseInt(Container.get(i).get(m).n2m1)) {
							Max = Integer.parseInt(Container.get(i).get(m).n2m1);
						}
					}
				}
			}

		}

		return String.valueOf(Max);

	}

	String MinValue(Vector<Vector<DataContainer>> Container, String PDBID, boolean Time) {
		Double Min = 1.0;
		Double MinTime = 1000000.0;
		for (int i = 0; i < Container.size(); ++i) {

			for (int m = 0; m < Container.get(i).size(); ++m) {
				if (Container.get(i).get(m).PDB_ID.equals(PDBID) && Container.get(i).get(m).BuiltPDB.equals("T")) {
					if (Time == false) {
						if (Min > Double.parseDouble(Container.get(i).get(m).R_factor0Cycle)) {
							// System.out.println(Double.parseDouble(Container.get(i).get(m).R_factor0Cycle));
							Min = Double.parseDouble(Container.get(i).get(m).R_factor0Cycle);
						}
					} else {
						if (MinTime > Double.parseDouble(Container.get(i).get(m).TimeTaking)) {
							MinTime = Double.parseDouble(Container.get(i).get(m).TimeTaking);
						}
					}

				}
			}

		}
		if (Time == false) {
			return String.valueOf(Min);
		} else {
			return String.valueOf(MinTime);
		}

	}

	String AvgValue(Vector<Vector<DataContainer>> Container, String PDBID) {
		Double Avg = 0.0;
		int count = 0;
		for (int i = 0; i < Container.size(); ++i) {

			for (int m = 0; m < Container.get(i).size(); ++m) {
				if (Container.get(i).get(m).PDB_ID.equals(PDBID) && Container.get(i).get(m).BuiltPDB.equals("T")) {
					Avg += Double.parseDouble(Container.get(i).get(m).n2m1);
					count++;

				}
			}

		}

		return String.valueOf((Avg / count));

	}

	public Vector<DataContainer> ReadExcel(String Excel) {
System.out.println(Excel);
		Vector<DataContainer> Container = new Vector<DataContainer>();
		try {

			FileInputStream excelFile = new FileInputStream(new File(Excel));
			Workbook workbook = new XSSFWorkbook(excelFile);
			Sheet datatypeSheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = datatypeSheet.iterator();
			boolean isheader = true;
			while (iterator.hasNext()) {

				if (isheader == true) {// ignore the header
					isheader = false;
					iterator.next();
				}
				Row currentRow = iterator.next();

				// Cell currentCell = cellIterator.next();
				DataContainer C = new DataContainer();
				// System.out.print(currentCell.getStringCellValue() + "--");
				C.PDB_ID = currentRow.getCell(0).getStringCellValue();
				C.Resolution = currentRow.getCell(1).getStringCellValue();
				C.TimeTaking = currentRow.getCell(2).getStringCellValue();
				C.R_factor = currentRow.getCell(3).getStringCellValue();
				C.R_free = currentRow.getCell(4).getStringCellValue();
				C.R_factorΔR_free = currentRow.getCell(5).getStringCellValue();

				C.Overfitting = currentRow.getCell(6).getStringCellValue();
				C.R_factor0Cycle = currentRow.getCell(7).getStringCellValue();
				;

				C.R_free0Cycle = currentRow.getCell(8).getStringCellValue();

				C.OptimalR_factor = currentRow.getCell(9).getStringCellValue();

				C.NumberofAtomsinFirstPDB = currentRow.getCell(10).getStringCellValue();
				C.NumberofAtomsinSecondPDB = currentRow.getCell(11).getStringCellValue();

				C.NumberOfAtomsInFirstNotInSecond = currentRow.getCell(12).getStringCellValue();

				C.NumberOfAtomsInSecondNotInFirst = currentRow.getCell(13).getStringCellValue();

				C.Seqrn1n2n2n1 = currentRow.getCell(14).getStringCellValue();

				C.n1m2 = currentRow.getCell(15).getStringCellValue();

				C.n2m1 = currentRow.getCell(16).getStringCellValue();

				C.F_mapCorrelation = currentRow.getCell(17).getStringCellValue();

				C.E_mapCorrelation = currentRow.getCell(18).getStringCellValue();

				C.BuiltPDB = currentRow.getCell(19).getStringCellValue();
				C.WarringTimeTaking = currentRow.getCell(20).getStringCellValue();
				C.WarringLogFile = currentRow.getCell(21).getStringCellValue();
				C.ExceptionNoLogFile = currentRow.getCell(22).getStringCellValue();
				//System.out.println(C.PDB_ID);
				//System.out.println(currentRow.getCell(23).getStringCellValue().trim());
				if( currentRow.getLastCellNum() > 24) {
				 C.molProbityData.RamachandranOutliers= currentRow.getCell(23).getStringCellValue().trim(); 
				 C.molProbityData.RamachandranFavored= currentRow.getCell(24).getStringCellValue().trim(); 
				 C.molProbityData.RotamerOutliers= currentRow.getCell(25).getStringCellValue().trim();
				 C.molProbityData.Clashscore= currentRow.getCell(26).getStringCellValue().trim(); 
				 C.molProbityData.RMSBonds= currentRow.getCell(27).getStringCellValue().trim(); 
				 C.molProbityData.RMSAngles= currentRow.getCell(28).getStringCellValue().trim() ; 
				 C.molProbityData.MolProbityScore= currentRow.getCell(29).getStringCellValue().trim() ;
				 C.molProbityData.RWork= currentRow.getCell(30).getStringCellValue().trim() ;
				 C.molProbityData.RFree= currentRow.getCell(31).getStringCellValue().trim() ;
				 C.molProbityData.RefinementProgram= currentRow.getCell(32).getStringCellValue().trim() ;
				}
				C.Intermediate=currentRow.getCell(33).getStringCellValue().trim() ;
				C.Completeness=currentRow.getCell(34).getStringCellValue().trim() ;
				C.PDBIDTXT=currentRow.getCell(35).getStringCellValue().trim() ;
				Container.add(C);

				// System.out.println();

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return Container;
	}
/*
	Vector<DataContainer> CheckPDBexists(Vector<DataContainer> Phenixx, Vector<DataContainer> OtherTool) {
		System.out.println(Phenixx.size());
		Vector<DataContainer> Container = new Vector<DataContainer>();
		for (int i = 0; i < Phenixx.size(); ++i) {
			for (int m = 0; m < OtherTool.size(); ++m) {
				if (Phenixx.get(i).PDB_ID.equals(OtherTool.get(m).PDB_ID)) {
					Container.add(OtherTool.get(m));

				}
			}

		}
		return Container;
	}*/
	Vector<DataContainer> CheckPDBexists(Vector<Vector<DataContainer>> AllToolsData, Vector<DataContainer> ThisToolData) {
		//System.out.println(Phenixx.size());
		Vector<DataContainer> Container = new Vector<DataContainer>();
		for (int t = 0; t < ThisToolData.size(); ++t) {
			boolean PDFFoundInAll=true;
		for (int i = 0; i < AllToolsData.size(); ++i) {
			boolean PDFFoundInThis=false;
			for (int m = 0; m < AllToolsData.get(i).size(); ++m) {
				String ThisToolDataPDB= ThisToolData.get(t).PDB_ID;
				ThisToolDataPDB=ThisToolDataPDB.replace("-parrot-hancs", "");
				
				ThisToolDataPDB=ThisToolDataPDB.replace("-parrot-mrncs", "");
				ThisToolDataPDB=ThisToolDataPDB.replace("-parrot-noncs", "");
				String AllToolsDataPDB= AllToolsData.get(i).get(m).PDB_ID;
				AllToolsDataPDB=AllToolsDataPDB.replace("-parrot-hancs", "");
				AllToolsDataPDB=AllToolsDataPDB.replace("-parrot-mrncs", "");
				AllToolsDataPDB=AllToolsDataPDB.replace("-parrot-noncs", "");
				
				/*
				if (ThisToolData.get(t).PDB_ID.equals(AllToolsData.get(i).get(m).PDB_ID) &&AllToolsData.get(i).get(m).BuiltPDB.equals("T")&& ThisToolData.get(t).BuiltPDB.equals("T")) {
					PDFFoundInThis=true;

				}
				*/
				if (ThisToolDataPDB.equals(AllToolsDataPDB) &&AllToolsData.get(i).get(m).BuiltPDB.equals("T")&& AllToolsData.get(i).get(m).Intermediate.equals("F")) {
					PDFFoundInThis=true;

				}
				
				
			}
			if(PDFFoundInThis==false) {
				PDFFoundInAll=false;
              break;
			}
			else
				PDFFoundInAll=true;
		}
		if(PDFFoundInAll==true) 
			Container.add(ThisToolData.get(t));
		}
		return Container;
	}
	Vector<Integer> ResoRange(Vector<Vector<DataContainer>> Containers) {

		Vector<Integer> Reso = new Vector<Integer>();
		for (int i = 0; i < Containers.size(); ++i) {
			for (int m = 0; m < Containers.get(i).size(); ++m) {
				Double Value = Double.parseDouble(Containers.get(i).get(m).Resolution);

				if (!Reso.contains(Value.intValue())) {
					Reso.add(Value.intValue());
				}
			}
		}
		return Reso;
	}

	void WorstestCasesInSeq(Vector<Vector<DataContainer>> Containers) {
		for (int i = 0; i < Containers.size(); ++i) {
			for (int m = 0; m < Containers.get(i).size(); ++m) {
				for (int n = 0; n < Containers.size(); ++n) {
					for (int b = 0; b < Containers.get(n).size(); ++b) {
						Double Reso = Double.parseDouble(Containers.get(i).get(m).Resolution);
						if (!Containers.get(i).get(m).n2m1.equals("None")
								&& !Containers.get(n).get(b).n2m1.equals("None")
								&& Integer.parseInt(Containers.get(i).get(m).n2m1) < Integer
										.parseInt(Containers.get(n).get(b).n2m1)
								&& Containers.get(i).get(m).PDB_ID.equals(Containers.get(n).get(b).PDB_ID)
								&& Reso.intValue() < 2) {
							System.out.println("Tool one " + ToolsNames.get(i));
							System.out.println("Tool two " + ToolsNames.get(n));
							System.out.println("PDB id " + Containers.get(n).get(b).PDB_ID);
							System.out.println("Seq " + Containers.get(i).get(m).n2m1);
							System.out.println("Seq " + Containers.get(n).get(b).n2m1);
						}
					}
				}

			}
		}
	}
}
