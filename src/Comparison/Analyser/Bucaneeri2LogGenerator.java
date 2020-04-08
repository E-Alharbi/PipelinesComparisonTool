package Comparison.Analyser;
/**
*
* @author Emad Alharbi
* University of York
*/
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

import Comparison.Utilities.FilesUtilities;


public class Bucaneeri2LogGenerator {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
String JobDir="";// a path for Bucaneeri2 job 

try(  PrintWriter out = new PrintWriter("" )  ){ //write log file name between "" for ex  2pnk-3.6-parrot-hancs.txt
    out.println(new Bucaneeri2LogGenerator().Log(JobDir) );
}
	}
	String Log(String JobDirectory) throws IOException {
		 Collection<File> Logs = FileUtils.listFiles(
				 new File(JobDirectory), 
				 new RegexFileFilter("log.txt"), 
				 DirectoryFileFilter.DIRECTORY
				);
		 List list = new ArrayList(Logs);
		 Collections.sort(list);
		
		 Vector <File>LogsFiles = new   Vector <File>();
		 LogsFiles.addAll(list);
	
		Collections.sort(LogsFiles, Bucaneeri2LogGenerator.SortingFiles);
		 String LogTxt="";
		 for(int i=0; i<LogsFiles.size();++i ) {
			
			 LogTxt+=new FilesUtilities().readFileAsString(LogsFiles.get(i).getAbsolutePath())+"\n"; 
			
		 }
		 
		 return LogTxt;
	}
	public static Comparator<File> SortingFiles = new Comparator<File>() {

		 @Override
		 public int compare(File o1, File o2) {

		        if (o1.lastModified() < o2.lastModified()) {
		            return -1;
		        } else if (o1.lastModified() > o2.lastModified()) {
		            return +1;
		        } else {
		            return 0;
		        }
		    }
		

	};
}
class FileNameComparator implements Comparator<File> {

    @Override
    public int compare(File file1, File file2) {

        return String.CASE_INSENSITIVE_ORDER.compare(file1.getName(),
                file2.getName());
    }
}


