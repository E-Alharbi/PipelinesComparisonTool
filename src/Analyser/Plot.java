package Analyser;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jfree.chart.*;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.labels.BoxAndWhiskerToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.util.Log;
import org.jfree.util.LogContext;
import org.jfree.util.ShapeUtilities;



import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.Regression;
public class Plot {

	Vector <XYSeries> Seriess = new Vector <XYSeries>();
	
	void AddSeries (String Title, Vector<Double> X , Vector<Double> Y){
		
		 final XYSeries S = new XYSeries(Title);
		 for(int i=0 ; i < X.size() ; ++i) {
			 S.add( X.get(i) ,  Y.get(i) );
		 }
		 Seriess.addElement(S);
	}
	
	void CreateScatterPlot(String PlotTitle, String XTitle , String YTitle) throws IOException {
		final XYSeriesCollection dataset = new XYSeriesCollection( );
		for(int i=0 ; i < Seriess.size() ; ++i)
			dataset.addSeries(Seriess.get(i));
		JFreeChart ScatterPlot = ChartFactory.createScatterPlot(
				PlotTitle, 
				XTitle,
				YTitle, 
		         dataset,
		         PlotOrientation.VERTICAL, 
		         true, true, false);
		 Shape cross = ShapeUtilities.createDiamond(4);
		 
	        XYPlot xyPlot = (XYPlot) ScatterPlot.getPlot();
	        xyPlot.setDomainCrosshairVisible(true);
	        xyPlot.setRangeCrosshairVisible(true);
	        XYItemRenderer renderer = xyPlot.getRenderer();
	        renderer.setSeriesShape(0, cross);
	        renderer.setSeriesPaint(0, Color.BLUE);
	      
	       
	        
		      int width = 1000;   /* Width of the image */
		      int height = 800;  /* Height of the image */ 
		      File XYChart = new File( PlotTitle+".jpeg" ); 
		      ChartUtilities.saveChartAsJPEG( XYChart,1.0f, ScatterPlot, width, height);
		     
	}
	void CreateLinePlot(String PlotTitle, String XTitle , String YTitle , XYSeriesCollection dataset) throws IOException {
		
		
		
		JFreeChart LineChart = ChartFactory.createXYLineChart(
				 PlotTitle, // Chart title
				 XTitle, // X-Axis Label
				 YTitle, // Y-Axis Label
			        dataset,
			        PlotOrientation.VERTICAL,
	                true,
	                true,
	                false
			        );
		LineChart.getXYPlot().setRenderer(new XYSplineRenderer());
		
		 int width = 1000;   /* Width of the image */
	      int height = 800;  /* Height of the image */ 
	      File XYChart = new File( PlotTitle+".jpeg" ); 
	      
	      ChartUtilities.saveChartAsJPEG( XYChart,1.0f, LineChart, width, height);
	}
	void CreateBoxPlot(String Title, String XLabel,String ValueLabel, DefaultBoxAndWhiskerCategoryDataset dataset) throws IOException {
	//	final DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();
		
		JFreeChart ScatterPlot = ChartFactory.createBoxAndWhiskerChart(Title, XLabel, ValueLabel, dataset, true);
		/* 
		Shape cross = ShapeUtilities.createDiamond(4);
		 
	        XYPlot xyPlot = (XYPlot) ScatterPlot.getPlot();
	        xyPlot.setDomainCrosshairVisible(true);
	        xyPlot.setRangeCrosshairVisible(true);
	        XYItemRenderer renderer = xyPlot.getRenderer();
	        renderer.setSeriesShape(0, cross);
	        renderer.setSeriesPaint(0, Color.BLUE);
	      */
	       
	        
		      int width = 1000;   /* Width of the image */
		      int height = 800;  /* Height of the image */ 
		      File XYChart = new File( Title+".jpeg" ); 
		      ChartUtilities.saveChartAsJPEG( XYChart,1.0f, ScatterPlot, width, height);
		      
		      
		      
		      CategoryAxis xAxis = new CategoryAxis(XLabel);
		        NumberAxis yAxis = new NumberAxis("Value");
		        BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
		        
		        CategoryPlot  plot = new CategoryPlot(dataset, xAxis, yAxis, renderer);
		        JFreeChart chart = new JFreeChart("BoxAndWhiskerDemo", plot);
		     //   File XYChart = new File( Title+".jpeg" ); 
			   //   ChartUtilities.saveChartAsJPEG( XYChart,1.0f, chart, width, height);
	}
	
   public static void main( String[ ] args )throws Exception {
	   /*
	   XYSeriesCollection dataset = new XYSeriesCollection();
	   File[] Excels = new File("/Volumes/PhDHardDrive/jcsg1200Results/ExcelSheets17/noncs").listFiles();
		 LoadExcel e = new LoadExcel();
		
		 Vector<Vector<DataContainer>> Con2 = new  Vector<Vector<DataContainer>>();
				 for (File Excel : Excels) {
					 Vector<DataContainer> CurrentPipe = e.ReadExcel(Excel.getAbsolutePath());
					 Vector<DataContainer> CurrentPipeRemovedNone = new   Vector<DataContainer> ();
					 for(int i=0 ; i <CurrentPipe.size() ; ++i ) {
						 if(!CurrentPipe.get(i).NumberofAtomsinFirstPDB.equals("None"))
							 CurrentPipeRemovedNone.add(CurrentPipe.get(i));
					 }
					 Con2.add(CurrentPipeRemovedNone);
					
					 e.ToolsNames.add(Excel.getName());
				 }
				 Vector<Vector<DataContainer>> Container2 = new Vector<Vector<DataContainer>>();
				 for(int i=0 ; i < Con2.size() ; ++i) {
					
					 System.out.println(" "+i + " out of "+Con2.size());
					 Container2.addElement(e.CheckPDBexists(Con2,Con2.get(i))); 
					 System.out.println(Container2.get(Container2.size()-1).size());
				 } 
				
				 for(int c=0 ; c < Container2.size() ; ++c) {
					 
					 Vector<DataContainer> Con = new  Vector<DataContainer>();
					 Con.removeAllElements();
					 Con.addAll(Container2.get(c));
					 Collections.sort(Con,DataContainer.DataContainerComparatorByNumOfCa);
					
					 // Con.add(e.ReadExcel(Excel.getAbsolutePath()));
					
				 
		 
		
		 XYSeries series = new XYSeries(e.ToolsNames.get(c));
		 int FirstDigit=-1;
		 Double TotalTimeTaking=0.0;
		 int NumberOfModels=0;
		 int TotalModelsSize=0;
		 
		 int Model500=0;
		 int Model1000=0;
		 int Model2000=0;
		 int Model3000=0;
		 
		 Double Model500Total=0.0;
		 Double Model1000Total=0.0;
		 Double Model2000Total=0.0;
		 Double Model3000Total=0.0;
		//System.out.println("\\addplot coordinates {");
		for(int i=0; i < Con.size(); ++i) {
			if(FirstDigit!=-1) {
			if(String.valueOf(Con.get(i).NumberofAtomsinFirstPDB).length()==3 && FirstDigit!=String.valueOf(Con.get(i).NumberofAtomsinFirstPDB).charAt(0)) {
				//System.out.println("========="); 
				//System.out.println("("+Double.valueOf(TotalModelsSize/NumberOfModels)+","+Double.valueOf(TotalTimeTaking/NumberOfModels)+")"); 
				series.add(  Double.valueOf(TotalModelsSize/NumberOfModels), Double.valueOf(TotalTimeTaking/NumberOfModels));		
//System.out.println(Double.valueOf(Con.get(i).NumberofAtomsinFirstPDB) +" "+ Double.valueOf(TotalTimeTaking/NumberOfModels));
TotalTimeTaking=0.0;
 NumberOfModels=0;
 TotalModelsSize=0;
			}
			if(String.valueOf(Con.get(i).NumberofAtomsinFirstPDB).length()==4 ) {
				
				String TheFirstTwoDigit=String.valueOf(Con.get(i).NumberofAtomsinFirstPDB.charAt(0));
				TheFirstTwoDigit+=Con.get(i).NumberofAtomsinFirstPDB.charAt(1);
				if(FirstDigit!=Integer.valueOf(TheFirstTwoDigit)) {
					//System.out.println("========="); 
					//System.out.println("("+Double.valueOf(TotalModelsSize/NumberOfModels)+","+Double.valueOf(TotalTimeTaking/NumberOfModels)+")"); 

					series.add(  Double.valueOf(TotalModelsSize/NumberOfModels), Double.valueOf(TotalTimeTaking/NumberOfModels));		
					//System.out.println(Double.valueOf(Con.get(i).NumberofAtomsinFirstPDB) +" "+ Double.valueOf(TotalTimeTaking/NumberOfModels));
					TotalTimeTaking=0.0;
					 NumberOfModels=0;
					 TotalModelsSize=0;
				}
			}
			}
			if(String.valueOf(Con.get(i).NumberofAtomsinFirstPDB).length()==3) {
				FirstDigit = String.valueOf(Con.get(i).NumberofAtomsinFirstPDB).charAt(0);
			}
			else {
				String TheFirstTwoDigit=String.valueOf(Con.get(i).NumberofAtomsinFirstPDB.charAt(0));
				TheFirstTwoDigit+=Con.get(i).NumberofAtomsinFirstPDB.charAt(1);
				FirstDigit=Integer.valueOf(TheFirstTwoDigit);
			}
			//System.out.print(Con.get(i).NumberofAtomsinFirstPDB); 
			//System.out.print(" "+Con.get(i).TimeTaking);
			//System.out.print(" "+Con.get(i).E_mapCorrelation);
			//System.out.print(" "+Con.get(i).PDB_ID);
			//System.out.println(" "+Con.get(i).Completeness);
			NumberOfModels++;
			TotalModelsSize+=Integer.valueOf(Con.get(i).NumberofAtomsinFirstPDB);
			TotalTimeTaking+=Double.valueOf(Con.get(i).TimeTaking);
			
			
			
			
			if(Integer.valueOf(Con.get(i).NumberofAtomsinFirstPDB) <= 500) {
				Model500++;
				Model500Total+=Double.valueOf(Con.get(i).TimeTaking);
			}
			else if(Integer.valueOf(Con.get(i).NumberofAtomsinFirstPDB) > 500 &&  Integer.valueOf(Con.get(i).NumberofAtomsinFirstPDB) <= 1000) {
				Model1000++;
				Model1000Total+=Double.valueOf(Con.get(i).TimeTaking);
			}
			//else if(Integer.valueOf(Con.get(i).NumberofAtomsinFirstPDB) > 1000 &&  Integer.valueOf(Con.get(i).NumberofAtomsinFirstPDB) <= 2000) {
			//	Model2000++;
			//	Model2000Total+=Double.valueOf(Con.get(i).TimeTaking);
			//}
			else {
				Model3000++;
				Model3000Total+=Double.valueOf(Con.get(i).TimeTaking);
			}
		}
	//	System.out.println("};");
//	System.out.println("\\addlegendentry{"+e.ToolsNames.get(c)+"}");
		System.out.println("########");
		System.out.print(e.ToolsNames.get(c));
		System.out.print("< 500 "+ Model500 + " "+ (Model500Total / Model500));
		System.out.print(" 500 - 1000 "+ Model1000 + " "+ (Model1000Total / Model1000));
		System.out.print(" 1000 - 2000 "+ Model2000 + " "+ (Model2000Total / Model2000));
		System.out.println(" > 3000 "+ Model3000 + " "+ (Model3000Total / Model3000));
		System.out.println("########");
		 dataset.addSeries(series);
				 } 
		 new Plot().CreateLinePlot("Size", "Size",
					"Time Taking",dataset);
		 */
	   /*
	   String line = "nnnnThis order was placed for QT3000! OK? \nThis";
	      String pattern = "R free*";

	      // Create a Pattern object
	      XYSeriesCollection dataset = new XYSeriesCollection();
		 String FileNamesTxt=new ARPResultsAnalysis().readFileAsString("/Volumes/PhDHardDrive/jcsg1200Results/Results20:7:2018/hancsBuccaneeri125C/Buccaneeri1/BuccaneerResults/BuccaneerLogs/1vl0-4.0-parrot-hancs.txt");

		

		 Matcher m = Pattern.compile(".*R free.*").matcher(FileNamesTxt);
		 Matcher R = Pattern.compile("\\sR factor.*").matcher(FileNamesTxt);
		 System.out.println(m.groupCount());
		 XYSeries series = new XYSeries("R free");
		 XYSeries seriesOp = new XYSeries("R factor");
		 double Cycle=1.0;
		 
	        while (m.find()) {
	            System.out.println("line = " + m.group());
	            String[] splited = m.group().split("\\s+");
	           
	            
	            series.add(Cycle , Double.parseDouble(splited[4]));	
	            Cycle++;
	        }
	        Cycle=1.0;
	        String [] Lines= FileNamesTxt.split("\n");
	        for(int i =0 ; i < Lines.length ; ++i) {
	        	if(Lines[i].contains("           R factor")) {
	        		 System.out.println("line R = " +Lines[i]);
	        		 String[] splited = Lines[i].split("\\s+");
	        		 seriesOp.add(Cycle , Double.parseDouble(splited[4]));	
	        		 System.out.println(splited[4]);
	 	            Cycle++;
	        	}
	        }
	        */
	        /*
	        while (R.find()) {
	            System.out.println("line R = " + R.group());
	            String[] splited = R.group().split("\\s+");
	            System.out.println(splited[4]);
	            
	            seriesOp.add(Cycle , Double.parseDouble(splited[4]));	
	            Cycle++;
	        }
		 */
		
	   
	//DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			
			
				 //String series1 = ToolsNames.get(i);
				
				
					
					//DataContainer.AddElemnet(BestModelInEachReso, Container.get(i).get(m));
				
						// System.out.println(Container.get(i).get(m).Resolution);
						
						

						 //dataset.addValue(Double.valueOf(Container.get(i).get(m).molProbityData.MolProbityScore), series1, Double.valueOf(Container.get(i).get(m).Resolution));
					 
				 /*
				 dataset.addSeries(series);
				
				 dataset.addSeries(seriesOp);
			 
			
			
			new Plot().CreateLinePlot("MolScore", "Resolution",
					"MolProbity Score",dataset);
	   */
	   /*
	   final int seriesCount = 3;
       final int categoryCount = 3;
       final int entityCount = 22;
       
       final DefaultBoxAndWhiskerCategoryDataset dataset 
           = new DefaultBoxAndWhiskerCategoryDataset();
       for (int i = 0; i < seriesCount; i++) {
           for (int j = 0; j < categoryCount; j++) {
               final List list = new ArrayList();
               // add some values...
               int sum=0;
               for (int k = 0; k < entityCount; k++) {
                   final double value1 = k;
                   list.add(new Double(value1));
                   //final double value2 = 11.25 + Math.random(); // concentrate values in the middle
                   //list.add(new Double(value2));
                   sum+=k;
               }
               System.out.println(sum);
              // LOGGER.debug("Adding series " + i);
             //  LOGGER.debug(list.toString());
               dataset.add(list, "Series " + i, " Type " + j);
           }
           
       }
      

      
      
      
       JFreeChart ScatterPlot = ChartFactory.createBoxAndWhiskerChart("", "tt", "", dataset, true);
		 Shape cross = ShapeUtilities.createDiamond(4);
		
	       
	        
		      int width = 1000;  
		      int height = 800;  
		      File XYChart = new File( "box"+".jpeg" ); 
		      ChartUtilities.saveChartAsJPEG( XYChart,1.0f, ScatterPlot, width, height);
		      
		      */
   }
   
   
   
}
