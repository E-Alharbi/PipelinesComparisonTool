package Analyser;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

import NotUsed.ARPResultsAnalysis;

public class Bucaneeri2LogGenerator {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
String JobDir="/Volumes/PhDHardDrive/Run7Temp/project2pnk-3.8-parrot-hancs/CCP4_JOBS/job_1";

try(  PrintWriter out = new PrintWriter("/Volumes/PhDHardDrive/Run7Temp/2pnk-3.8-parrot-hancs.txt" )  ){
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
			 System.out.println(LogsFiles.get(i).getAbsolutePath());
			 LogTxt+=new ARPResultsAnalysis().readFileAsString(LogsFiles.get(i).getAbsolutePath())+"\n"; 
			
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


