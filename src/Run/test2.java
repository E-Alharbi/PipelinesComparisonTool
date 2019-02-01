package Run;
/**
*
* @author Emad Alharbi
* University of York
*/
import java.util.Vector;

import Analyser.DataContainer;
import Analyser.ExcelLoader;

public class test2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ExcelLoader e = new ExcelLoader();
		
		Vector<DataContainer> ArpWithRFree = e.ReadExcel("/Volumes/PhDHardDrive/jcsg1200Results/Fasta/VikingRun1/noncs/PhenixHAL.xlsx");
		
		Vector<DataContainer> ArpWithNoRFree = e.ReadExcel("/Volumes/PhDHardDrive/jcsg1200Results/Fasta/VikingRun2/noncs/Phenix.xlsx");
		
		int better=0;
		int better10=0;
		int better20=0;
		int better30=0;
		int better40=0;
		int better50=0;
		int worse =0;
		int worse10 =0;
		int worse20 =0;
		int worse30 =0;
		int worse40 =0;
		int worse50 =0;
		int Equal =0;
		int EqualRworkAndRfree=0;
		int EqualRworkLowerRfree=0;
		int EqualRworkHigherRfree=0;
		double WithRFree=0;
		double WithNoRFree=0;
		double TotalComACO=0;
		double TotalCom=0;
		int numberOfCases=0;
		
		
		int Rwork=0;
		int Rfree=0;
		
		
		for (DataContainer d : ArpWithRFree) {
			
			for (DataContainer dd : ArpWithNoRFree) {
				if(d.PDB_ID.equals(dd.PDB_ID) && !d.Completeness.equals("None")&& !dd.Completeness.equals("None") && dd.Intermediate.equals("F") && dd.BuiltPDB.equals("T")) {
					 WithRFree+=Math.round(Double.valueOf(d.Completeness));
					 WithNoRFree+=Math.round(Double.valueOf(dd.Completeness));
					 numberOfCases++;
					 TotalComACO+=Math.round(Double.parseDouble(dd.Completeness));
					 TotalCom+=Math.round(Double.parseDouble(d.Completeness));
					 if(Math.round(Double.parseDouble(d.Completeness)) < Math.round(Double.parseDouble(dd.Completeness) )) {
						better++;
						/*
						System.out.println(d.PDB_ID);
						System.out.println(dd.Resolution);
						System.out.println(d.Completeness);
						System.out.println(dd.Completeness);
						System.out.println(d.TimeTaking);
						System.out.println(dd.TimeTaking);
						*/
					}
					if(Math.round(Double.parseDouble(d.Completeness)) < Math.round(Double.parseDouble(dd.Completeness) ) -10) {
						better10++;
						/*
						System.out.println(d.PDB_ID);
						System.out.println(dd.Resolution);
						System.out.println(d.Completeness);
						System.out.println(dd.Completeness);
						System.out.println(d.TimeTaking);
						System.out.println(dd.TimeTaking);
						*/
					}
					if(Math.round(Double.parseDouble(d.Completeness)) < Math.round(Double.parseDouble(dd.Completeness) ) -20) {
						better20++;
						/*
						System.out.println(d.PDB_ID);
						System.out.println(dd.Resolution);
						System.out.println(d.Completeness);
						System.out.println(dd.Completeness);
						System.out.println(d.TimeTaking);
						System.out.println(dd.TimeTaking);
						*/
					}
					if(Math.round(Double.parseDouble(d.Completeness)) < Math.round(Double.parseDouble(dd.Completeness) ) -30) {
						better30++;
						/*
						System.out.println(d.PDB_ID);
						System.out.println(dd.Resolution);
						System.out.println(d.Completeness);
						System.out.println(dd.Completeness);
						System.out.println(d.TimeTaking);
						System.out.println(dd.TimeTaking);
						*/
					}
					if(Math.round(Double.parseDouble(d.Completeness)) < Math.round(Double.parseDouble(dd.Completeness) ) -40) {
						better40++;
						/*
						System.out.println(d.PDB_ID);
						System.out.println(dd.Resolution);
						System.out.println(d.Completeness);
						System.out.println(dd.Completeness);
						System.out.println(d.TimeTaking);
						System.out.println(dd.TimeTaking);
						*/
					}
					if(Math.round(Double.parseDouble(d.Completeness)) < Math.round(Double.parseDouble(dd.Completeness) ) -50) {
						better50++;
						/*
						System.out.println(d.PDB_ID);
						System.out.println(dd.Resolution);
						System.out.println(d.Completeness);
						System.out.println(dd.Completeness);
						System.out.println(d.TimeTaking);
						System.out.println(dd.TimeTaking);
						*/
					}
					 if(Math.round(Double.parseDouble(d.Completeness)) > Math.round(Double.parseDouble(dd.Completeness))) {
					
						worse++;
						
						
					}
					 if(Math.round(Double.parseDouble(d.Completeness)) -10 > Math.round(Double.parseDouble(dd.Completeness))) {
							
							worse10++;
							
							
						}
					 if(Math.round(Double.parseDouble(d.Completeness)) -20 > Math.round(Double.parseDouble(dd.Completeness))) {
							
							worse20++;
							System.out.println(d.PDB_ID);
							System.out.println(dd.Resolution);
							System.out.println(d.Completeness);
							System.out.println(dd.Completeness);
							System.out.println(d.TimeTaking);
							System.out.println(dd.TimeTaking);
							
							
						}
					 if(Math.round(Double.parseDouble(d.Completeness)) -30 > Math.round(Double.parseDouble(dd.Completeness))) {
							
							worse30++;
							
							
						}
					 if(Math.round(Double.parseDouble(d.Completeness)) -40 > Math.round(Double.parseDouble(dd.Completeness))) {
							
							worse40++;
							
							
						}
					 if(Math.round(Double.parseDouble(d.Completeness)) -50 > Math.round(Double.parseDouble(dd.Completeness))) {
							
							worse50++;
							
							
						}
					 if(Math.round(Double.parseDouble(d.Completeness)) == Math.round(Double.parseDouble(dd.Completeness)))  {
						 Equal++;
						
					 }
					// System.out.println(Double.valueOf(dd.R_factor0Cycle));
					// System.out.println("RFree "+Double.valueOf(dd.R_free0Cycle));
					 if(Double.parseDouble(dd.R_factor0Cycle) == Double.parseDouble(dd.R_free0Cycle))  {
						 EqualRworkAndRfree++;
						
					 }
					 
					 if(Double.parseDouble(dd.R_factor0Cycle) > Double.parseDouble(dd.R_free0Cycle))  {
						 EqualRworkHigherRfree++;
						
					 }
					 
					 if(Double.parseDouble(dd.R_factor0Cycle) < Double.parseDouble(dd.R_free0Cycle))  {
						 EqualRworkLowerRfree++;
						
					 }
					 
					 if(Double.parseDouble(dd.R_factor0Cycle) == Double.parseDouble(d.R_factor0Cycle))  {
						 Rwork++;
						
					 }
					 if(Double.parseDouble(dd.R_free0Cycle) == Double.parseDouble(d.R_free0Cycle))  {
						 Rfree++;
						
					 }
					 
				}
			}
		}
		System.out.println(    WithRFree / ArpWithRFree.size());
		System.out.println(    WithNoRFree / ArpWithNoRFree.size());
		System.out.println("improved by" +"\t"+ "1%" + "\t"+ "10%" + "\t"+ "20%" + "\t"+ "30%" + "\t"+ "40%" + "\t"+ "50%" + "\t");
		System.out.println("\t\t"+better + "\t"+ better10 + "\t"+ better20 + "\t"+ better30 + "\t"+ better40 + "\t"+ better50 + "\t");
		System.out.println("got worse"  +"\t"+ "1%" + "\t"+ "10%" + "\t"+ "20%" + "\t"+ "30%" + "\t"+ "40%" + "\t"+ "50%" + "\t");
		System.out.println("\t\t"+worse + "\t"+ worse10 + "\t"+ worse20 + "\t"+ worse30 + "\t"+ worse40 + "\t"+ worse50 + "\t");
		//System.out.println(worse);
		System.out.println("Equivalent"+"\t"+Equal);
		
		System.out.println("R-work == R-free"+"\t"+EqualRworkAndRfree);
		System.out.println("R-work > R-free "+"\t"+EqualRworkHigherRfree);
		System.out.println("R-work < R-free "+"\t"+EqualRworkLowerRfree);
		
		
		
		System.out.println("Avg ACO "+ (TotalComACO/numberOfCases));
		System.out.println("Avg  "+ (TotalCom/numberOfCases));
		System.out.println("numberOfCases  "+ numberOfCases);
		System.out.println("TotalComACO  "+ TotalComACO);
		System.out.println("TotalCom  "+ TotalCom);
		
		System.out.println("R-work == R-work"+"\t"+Rwork);
		System.out.println("Rfree == R-free "+"\t"+Rfree);
		
		
	}

}
