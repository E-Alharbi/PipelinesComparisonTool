package Comparison.Analyser;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;

import Comparison.Runner.Preparer;
import Comparison.Runner.RunComparison;
import Comparison.Utilities.FilesUtilities;

public class Heatmap {
// Creating heat map from comparison tables 
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		new Heatmap().HeatmapFromComparisonTable("/Users/emadalharbi/Downloads/Pairwise%20running%20of%20automated%20crystallographic%20model%20building%20pipelines-Revision1 3/Tables/Synthetic");
		
	}

	void HeatmapFromComparisonTable(String TablesFolder) throws IOException {
		
		
		 new RunComparison().CheckDirAndFile(new File(TablesFolder).getParent()+"/"+new File(TablesFolder).getName()+"HeatMap");
		String PathToWrtieIn=new File(TablesFolder).getParent()+"/"+new File(TablesFolder).getName()+"HeatMap";
		 
		 for(File LatexTable : new File(TablesFolder).listFiles()) {
			if(FilenameUtils.getExtension(LatexTable.getName()).equals("tex")) {
			
			
		
String Table=new FilesUtilities().readFileAsString(LatexTable.getAbsolutePath());

		
		// Removing non digits 
		//String [] Numbers = Table.split("%")[0].replaceAll("\\D+", " ").split(" ");
		Vector<String> Numbers = ExtractNumbers(Table.split("%")[0]);
		if(Numbers!=null) { // null, meaning this not a pairwise comparison table. So we do not want to color it
			
		
			
		 int max=0;
		 int min=Integer.MAX_VALUE;
		 for(int i=0 ; i < Numbers.size() ; ++i) {
			
			 if(Numbers.get(i).trim().length()!=0&& Integer.valueOf(Numbers.get(i).trim()) > max ) {
				 max=Integer.valueOf(Numbers.get(i).trim());
			 }
			 if(Numbers.get(i).trim().length()!=0&& Integer.valueOf(Numbers.get(i).trim()) < min) {
				 min=Integer.valueOf(Numbers.get(i).trim());
			 }
		 }
		 double Range=max-min;
		
		 if(Range==0)// meaning all the values in this table are same
			 Range=max;
		 //System.out.println(Table.split("%")[0]);
		String TableWithoutComments=Table.split("%")[0];
		String Comments="";
		if(Table.contains("%")) // contains comments
		 Comments=Table.substring(Table.indexOf("%"),Table.length());
		 for(int i=min ; i <= max ; ++i) {
			
	     double ColorTransparency=new BigDecimal(i).divide(new BigDecimal(Range),2, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).doubleValue();
//System.out.println(i);
	     TableWithoutComments=TableWithoutComments.replaceAll(" "+String.valueOf(i)+" ", " \\\\cellcolor{red!"+ColorTransparency+"} "+i+" ");
	    
		 }
		 Vector<String> NumbersAfterHeat = ExtractNumbers(TableWithoutComments);
			
			
			
			//System.out.println(TableWithoutComments);
			for(int n=0 ; n < NumbersAfterHeat.size();++n) {
				if(NumbersAfterHeat.get(n).trim().length()!=0 && Numbers.get(n).trim().length()!=0 )
				if(!NumbersAfterHeat.get(n).equals(Numbers.get(n))) {
					
					System.out.println("The table after coloring not matched the table breofe. Please check!");
					System.exit(-1);
					break;
				}
			}
		 
		 
		 
		 String HeatMapLegend="\\begin{tikzpicture}";
			double Pos=0;
			for(int i=0 ; i <= 100 ; ++i) {
				HeatMapLegend+="\\node[draw ,fill=red!"+i+",draw=none,minimum height=0.4cm,minimum width=0.02cm, inner sep=0pt,text width=0.5mm] at ("+Pos+", -4){}; \n";
				Pos=Pos+0.044;
			}
			HeatMapLegend+="\\node[draw ,fill=none,draw=none,minimum height=0.4cm,minimum width=0.02cm] at ("+(Pos-0.4)+", -4){\\small "+max+"};";
			HeatMapLegend+="\\node[draw ,fill=none,draw=none,minimum height=0.4cm,minimum width=0.02cm] at (0.0, -4){\\small "+min+"}; ";
			HeatMapLegend+="\\end{tikzpicture}";
			TableWithoutComments+=" \\\\[0.01in]  \\multicolumn{"+Table.trim().split("\n")[0].split("&").length+"}{l}{ \\tiny{ "+HeatMapLegend+"}}";
		
			
			new Preparer().WriteTxtFile(PathToWrtieIn+"/"+LatexTable.getName(), TableWithoutComments);

		
		// Here we used different regex to validate the table after coloring. Are both regex expressions extract the same numbers?  
		

			}
		
			}
			
			
		}
	}
	
	Vector<String> ExtractNumbers(String Table) {
		// Checking if the number of row and columns is same.  
		
		if(Table.trim().split("\n")[0].split("&").length!=Table.trim().split("\n").length && ((Table.trim().split("\n").length-1)/2) != Table.trim().split("\n")[0].split("&").length-1)
		return null;
		
		Vector<String> Numbers = new Vector<String>();
		Pattern pattern = Pattern.compile(" \\d+ ");
		Matcher matcher = pattern.matcher(Table);
		
		while(matcher.find()) {
			   
			Numbers.add(Table.substring(matcher.start(),matcher.end()));
		}
		
		return Numbers;
	}
}
