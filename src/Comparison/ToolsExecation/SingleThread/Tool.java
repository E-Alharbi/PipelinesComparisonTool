package Comparison.ToolsExecation.SingleThread;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import Comparison.Utilities.FilesManagements;


public class Tool {

	
	
void timer(String JobDirectory , String PDBID,Timer t ) {
		
		
		t.scheduleAtFixedRate(
		    new TimerTask()
		    {
		        public void run()
		        {
		            
		            try {
						SaveIntermediateResults(JobDirectory ,  PDBID );
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
		    },
		    0,1000); 
	}
   boolean SaveIntermediateResults(String JobDirectory , String PDBID) throws IOException{
		
	   return true;
	}
	Vector<String> AddFileNameToList( Vector<String> FilesNames,  String Filename) throws IOException{
		File yourFile = new File(Filename);
		yourFile.createNewFile();
		 String FileNamesTxt=new FilesManagements().readFileAsString(Filename);
		 FilesNames.addAll(Arrays.asList(FileNamesTxt.split("\n")));
		 return FilesNames;
		
	}
	public  void WriteFileNameToList(String Line, String Filename) throws IOException{
		File yourFile = new File(Filename);
		//"./ProcessedFilesNamesBuccaneer.txt"
		yourFile.createNewFile();
		 BufferedWriter output;
		 output = new BufferedWriter(new FileWriter(Filename, true));
		 output.append(Line+"\n");
		 output.close();
	}
}
