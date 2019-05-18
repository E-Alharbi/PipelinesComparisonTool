package Analyser;
/**
*
* @author Emad Alharbi
* University of York
*/
import java.util.Comparator;
import java.util.Vector;

import ToolsExecation.SingleThread.MolProbityData;

public class ExcelContents implements Comparable<ExcelContents> {

	// This class to read the excel file into vector of type DataContainer

	public String PDB_ID;
	public String Resolution = "None";

	public String TimeTaking = "-1";

	public String R_factor = "None";

	public String R_free = "None";

	public String R_factorΔR_free = "None";

	public String Overfitting = "None";

	public String R_factor0Cycle = "None";
	public String R_free0Cycle = "None";
	public String OptimalR_factor = "None";
	public String NumberofAtomsinFirstPDB = "None";

	public String NumberofAtomsinSecondPDB = "None";

	public String NumberOfAtomsInFirstNotInSecond = "None";

	public String NumberOfAtomsInSecondNotInFirst = "None";

	public String Seqrn1n2n2n1 = "None";

	public String n1m2 = "None";

	public String n2m1 = "None";
	public String Completeness = "None";
	public String F_mapCorrelation = "None";
	public String E_mapCorrelation = "None";
	public String BuiltPDB;
	public String WarringTimeTaking = "None";
	public String WarringLogFile = "None";
	public String ExceptionNoLogFile = "None";
	public String Intermediate = "F";
	public MolProbityData molProbityData = new MolProbityData();
	public String PDBIDTXT;

	static boolean CheckIfResoIsInContainer(String Reso, Vector<ExcelContents> Container) {
		for (int i = 0; i < Container.size(); ++i) {

			if (round(Double.parseDouble(Container.get(i).Resolution), 2) == round(Double.parseDouble(Reso), 2))
				return true;
		}
		return false;
	}

	static ExcelContents ReturnByReo(String Reso, Vector<ExcelContents> Container) {
		for (int i = 0; i < Container.size(); ++i) {
			if (round(Double.parseDouble(Container.get(i).Resolution), 2) == round(Double.parseDouble(Reso), 2))
				return Container.get(i);

		}
		return null;
	}

	static Vector<ExcelContents> AddElemnet(Vector<ExcelContents> Container, ExcelContents NewEle) {

		if (Container.size() == 0) {
			Container.insertElementAt(NewEle, 0);
			System.out.println("R0 " + Double.valueOf(Container.get(0).Resolution));
			return Container;
		}

		if (Double.valueOf(Container.get(0).Resolution) > Double.valueOf(NewEle.Resolution)) {
			System.out.println("R1 " + Double.valueOf(Container.get(0).Resolution));
			Container.insertElementAt(NewEle, 0);
			System.out.println("000");

			System.out.println("R2 " + Double.valueOf(NewEle.Resolution));
		} else {
			System.out.println("R3 " + Double.valueOf(Container.get(Container.size() - 1).Resolution));
			Container.insertElementAt(NewEle, Container.size() - 1);

			System.out.println("R4 " + Double.valueOf(NewEle.Resolution));
		}

		return Container;
	}

	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	String GetReso() {
		return Resolution;
	}

	String GetSeqRes() {
		return n1m2;
	}

	String GetEmap() {
		return E_mapCorrelation;
	}

	String NumOfCa() {
		return NumberofAtomsinFirstPDB;
	}
	
	String TimeTaking() {
		return TimeTaking;
	}

	public static Comparator<ExcelContents> DataContainerComparator = new Comparator<ExcelContents>() {

		public int compare(ExcelContents Ele1, ExcelContents Ele2) {

			String Ele11 = Ele1.GetReso().toUpperCase();
			String Ele22 = Ele2.GetReso().toUpperCase();

			// ascending order
			return Ele11.compareTo(Ele22);

			// descending order
			// return fruitName2.compareTo(fruitName1);
		}

	};

	public static Comparator<ExcelContents> DataContainerComparatorByNumOfSeqRes = new Comparator<ExcelContents>() {

		public int compare(ExcelContents Ele1, ExcelContents Ele2) {

			String Ele11 = Ele1.GetSeqRes().toUpperCase();
			String Ele22 = Ele2.GetSeqRes().toUpperCase();
			if (Ele11.equals("None"))
				Ele11 = "0";
			if (Ele22.equals("None"))
				Ele22 = "0";
			// ascending order
			return Ele22.compareTo(Ele11);

			// descending order
			// return fruitName2.compareTo(fruitName1);
		}

	};

	public static Comparator<ExcelContents> DataContainerComparatorEmap = new Comparator<ExcelContents>() {

		public int compare(ExcelContents Ele1, ExcelContents Ele2) {

			String Ele11 = Ele1.GetEmap().toUpperCase();
			String Ele22 = Ele2.GetEmap().toUpperCase();
			if (Ele11.equals("None"))
				Ele11 = "0";
			if (Ele22.equals("None"))
				Ele22 = "0";
			// ascending order
			return Ele22.compareTo(Ele11);

			// descending order
			// return fruitName2.compareTo(fruitName1);
		}

	};
	
	
	public static Comparator<ExcelContents> DataContainerComparatorTimeTaking = new Comparator<ExcelContents>() {

		public int compare(ExcelContents Ele1, ExcelContents Ele2) {

			double Ele11 = Double.parseDouble(Ele1.TimeTaking());
			double Ele22 = Double.parseDouble(Ele2.TimeTaking());
			//if (Ele11.equals("-1"))
			//	Ele11 = "0";
			//if (Ele22.equals("-1"))
			//	Ele22 = "0";
			// ascending order
			
			
			
			if(Ele11<Ele22)
				return -1;
			if(Ele11>Ele22)
				return 1;
			return 0;
					//	return Ele22;
			//return Ele22.compareTo(Ele11);

			// descending order
			// return fruitName2.compareTo(fruitName1);
		}

	};
	public static Comparator<ExcelContents> DataContainerComparatorByNumOfCa = new Comparator<ExcelContents>() {

		public int compare(ExcelContents Ele1, ExcelContents Ele2) {

			Double Ele11 = Double.parseDouble(Ele1.NumOfCa());
			Double Ele22 = Double.parseDouble(Ele2.NumOfCa());

			// ascending order
			return Ele22.compareTo(Ele11);

			// descending order
			// return fruitName2.compareTo(fruitName1);
		}

	};

	@Override
	public int compareTo(ExcelContents o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public double TotalOfCompleteness(Vector<ExcelContents> Container){
		double Total=0;
		for(int i=0 ; i < Container.size() ; ++i) {
			Total+=Double.parseDouble(Container.get(i).Completeness);
		}
		return Total;
		
	}
	public double TotalOfRwork(Vector<ExcelContents> Container){
		double Total=0;
		for(int i=0 ; i < Container.size() ; ++i) {
			Total+=Double.parseDouble(Container.get(i).R_factor0Cycle);
		}
		return Total;
		
	}
	
	public double TotalOfRfree(Vector<ExcelContents> Container){
		double Total=0;
		for(int i=0 ; i < Container.size() ; ++i) {
			Total+=Double.parseDouble(Container.get(i).R_free0Cycle);
		}
		return Total;
		
	}
	public String toString() { 
	   return this.PDB_ID +","+ 
				this.Resolution +","+ 
				this.TimeTaking +","+ 
				this.R_factor +","+ 
				this.R_free +","+ 
				this.R_factorΔR_free +","+ 

				this.Overfitting +","+ 
				this.R_factor0Cycle +","+ 
				
				this.R_free0Cycle +","+ 
				this.OptimalR_factor +","+

				this.NumberofAtomsinFirstPDB +","+ 
				this.NumberofAtomsinSecondPDB +","+ 

				this.NumberOfAtomsInFirstNotInSecond +","+ 

				this.NumberOfAtomsInSecondNotInFirst +","+ 

				this.Seqrn1n2n2n1 +","+ 

				this.n1m2 +","+ 
				this.n2m1 +","+ 

				this.F_mapCorrelation +","+ 

				this.E_mapCorrelation +","+ 

				this.BuiltPDB +","+ 
				this.WarringTimeTaking +","+ 
				this.WarringLogFile +","+ 
				this.ExceptionNoLogFile +","+ 
				this.molProbityData.RamachandranOutliers+","+ 
				 this.molProbityData.RamachandranFavored+","+ 
				 this.molProbityData.RotamerOutliers+","+ 
				 this.molProbityData.Clashscore+","+ 
				 this.molProbityData.RMSBonds+","+ 
				 this.molProbityData.RMSAngles+","+ 
				 this.molProbityData.MolProbityScore+","+ 
				 this.molProbityData.RWork+","+ 
				 this.molProbityData.RFree+","+ 
				 this.molProbityData.RefinementProgram+","+ 
				
				this.Intermediate+","+
				this.Completeness+","+
				this.PDBIDTXT;
	} 
}
