package ResultsParsing;

import java.util.Comparator;
import java.util.Vector;

import ToolsExecution.MolProbityData;

public class DataContainer implements Comparable<DataContainer> {
	public String PDB_ID;
	public String Resolution="None";
	
	String TimeTaking="-1";
	
	String R_factor="None";
	
	String R_free="None";
	
	String R_factorΔR_free="None";
	
	String Overfitting="None";

			String R_factor0Cycle="None";
			String R_free0Cycle="None";
			public String OptimalR_factor="None";
			String NumberofAtomsinFirstPDB="None";

			String NumberofAtomsinSecondPDB="None";

			String NumberOfAtomsInFirstNotInSecond="None";

			String NumberOfAtomsInSecondNotInFirst="None";

			String Seqrn1n2n2n1="None";

			public  String n1m2="None";

			public String n2m1="None";
			String Completeness="None";
			public String F_mapCorrelation="None";
			public String E_mapCorrelation="None";
			public String BuiltPDB;
			String WarringTimeTaking="None";
			String WarringLogFile="None";
			public String ExceptionNoLogFile="None";
		    public String Intermediate="F";
			public MolProbityData molProbityData= new MolProbityData();
			public String PDBIDTXT;
			
			
			
			static boolean CheckIfResoIsInContainer(String Reso, Vector <DataContainer>Container) {
				for(int i=0;i < Container.size();++i) {
				
					if(round(Double.parseDouble(Container.get(i).Resolution ),2)==round(Double.parseDouble( Reso ),2))
						return true;}
				return false;
			}
			static DataContainer ReturnByReo(String Reso, Vector <DataContainer>Container) {
				for(int i=0;i < Container.size();++i) {
					if(round(Double.parseDouble( Container.get(i).Resolution ),2)==round(Double.parseDouble( Reso ),2))
						return Container.get(i);
					
				}
				return null;
			}
			static Vector <DataContainer> AddElemnet(Vector <DataContainer>Container,DataContainer NewEle) {
				
				if(Container.size()==0){
				Container.insertElementAt(NewEle, 0);
				System.out.println("R0 "+Double.valueOf( Container.get(0).Resolution));
				return Container;
				}
				
				if(Double.valueOf( Container.get(0).Resolution) >Double.valueOf( NewEle.Resolution)) {
					System.out.println("R1 "+Double.valueOf( Container.get(0).Resolution));
					Container.insertElementAt(NewEle, 0);
					System.out.println("000");
					
					System.out.println("R2 "+Double.valueOf( NewEle.Resolution));
				}
				else {
					System.out.println("R3 "+Double.valueOf( Container.get( Container.size()-1).Resolution));
					Container.insertElementAt(NewEle, Container.size()-1);
					
					System.out.println("R4 "+Double.valueOf( NewEle.Resolution));
				}
				
				return Container;
			}
			public static double round(double value, int places) {
			    if (places < 0) throw new IllegalArgumentException();

			    long factor = (long) Math.pow(10, places);
			    value = value * factor;
			    long tmp = Math.round(value);
			    return (double) tmp / factor;
			}
			String GetReso () {
				return Resolution;
			}
			String GetSeqRes () {
				return n1m2;
			}
			String GetEmap () {
				return E_mapCorrelation;
			}
	public static Comparator<DataContainer> DataContainerComparator 
            = new Comparator<DataContainer>() {

public int compare(DataContainer Ele1, DataContainer Ele2) {

String Ele11 = Ele1.GetReso().toUpperCase();
String Ele22 = Ele2.GetReso().toUpperCase();

//ascending order
return Ele11.compareTo(Ele22);

//descending order
//return fruitName2.compareTo(fruitName1);
}

};

public static Comparator<DataContainer> DataContainerComparatorByNumOfSeqRes 
= new Comparator<DataContainer>() {

public int compare(DataContainer Ele1, DataContainer Ele2) {

	
String Ele11 = Ele1.GetSeqRes().toUpperCase();
String Ele22 = Ele2.GetSeqRes().toUpperCase();
if(Ele11.equals("None"))
	Ele11="0";
if(Ele22.equals("None"))
	Ele22="0";
//ascending order
return Ele22.compareTo(Ele11);

//descending order
//return fruitName2.compareTo(fruitName1);
}

};

public static Comparator<DataContainer> DataContainerComparatorEmap 
= new Comparator<DataContainer>() {

public int compare(DataContainer Ele1, DataContainer Ele2) {

	
String Ele11 = Ele1.GetEmap().toUpperCase();
String Ele22 = Ele2.GetEmap().toUpperCase();
if(Ele11.equals("None"))
	Ele11="0";
if(Ele22.equals("None"))
	Ele22="0";
//ascending order
return Ele22.compareTo(Ele11);

//descending order
//return fruitName2.compareTo(fruitName1);
}

};
	@Override
	public int compareTo(DataContainer o) {
		// TODO Auto-generated method stub
		return 0;
	}
}
