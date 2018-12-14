package Utilities;
/**
*
* @author Emad Alharbi
* University of York
*/
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import org.apache.commons.io.FileUtils;

import ToolsExecution.RunCBuccaneerTestingProupse;

public class FilesManagements {

	public static void main(String[] args) throws IOException {

		//System.out.println(new File("/Users/emadalharbi/eclipse-workspace/RunBacuneerScript/RefmacLogsOptimalRfactor/1o6a-dataset.txt").length());
		/*// TODO Auto-generated method stub
try {
	System.out.println(new FilesManagements().CheckingIfFileExists(new File("/Users/emadalharbi/Downloads/RunBacuneerScript/BuccaneerResults/")));
} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}*/
		RunCBuccaneerTestingProupse RBM=new RunCBuccaneerTestingProupse();
		 Vector<String> FilesNames= new Vector <String>();
		 FilesNames=RBM.AddFileNameToList(FilesNames);
		 int countall=0;
		 for(int i = 0 ; i < FilesNames.size() ; ++i) {
			 int count = 0;
			 for(int m = 0 ; m < FilesNames.size() ; ++m)
			 {
				 if(FilesNames.get(i).equals(FilesNames.get(m)))
					 count++;
			 }
			 if(count==2) {
				 System.out.println(FilesNames.get(i));
				 countall++;
			 }
		 }
		 //System.out.println(countall);
	}

	public static boolean RemoveFile(File file) throws IOException{
	
			if (file.delete())
			return true;
			else
			return false;
	}
	public static boolean RemoveDir(File file) throws IOException{
		
		try {
			FileUtils.deleteDirectory(file.getAbsoluteFile());
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
}
	public static boolean CheckingIfFileExists(File file) throws IOException{
		
		if (file.exists())
		return true;
		else
		return false;
}
}
