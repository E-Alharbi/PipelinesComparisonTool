package ResultsParsing;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

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
		 /*
	        XYPlot xyPlot = (XYPlot) ScatterPlot.getPlot();
	        xyPlot.setDomainCrosshairVisible(true);
	        xyPlot.setRangeCrosshairVisible(true);
	        XYItemRenderer renderer = xyPlot.getRenderer();
	        renderer.setSeriesShape(0, cross);
	        renderer.setSeriesPaint(0, Color.BLUE);
	      */
	       
	        
		      int width = 1000;   /* Width of the image */
		      int height = 800;  /* Height of the image */ 
		      File XYChart = new File( "box"+".jpeg" ); 
		      ChartUtilities.saveChartAsJPEG( XYChart,1.0f, ScatterPlot, width, height);
   }
   
   
   
}
