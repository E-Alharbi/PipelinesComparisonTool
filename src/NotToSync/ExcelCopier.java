package NotToSync;

import java.io.File;
import java.io.IOException;

import Comparison.Analyser.MultiThreadedAnalyser;

public class ExcelCopier {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String DataPath=new MultiThreadedAnalyser().readFileAsString("/Volumes/PhDHardDrive/VikingSync/ExcelPaths.sh");
	String [] SpiltByLine=DataPath.split("\n");
		
		String script ="";
		for(int i=0 ; i < SpiltByLine.length ; ++i) {
			script+="mkdir "+ new File(new File(new File(SpiltByLine[i]).getParent()).getParent()).getName() +"Excel \n";
			script+="cp "+ new File(SpiltByLine[i]).getAbsolutePath() +" "+ new File(new File(new File(SpiltByLine[i]).getParent()).getParent()).getName() +"Excel \n" ;
		}
		System.out.println(script);
	}

}
