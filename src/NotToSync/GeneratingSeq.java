package NotToSync;
/**
*
* @author Emad Alharbi
* University of York
*/
import java.io.File;
import java.io.IOException;

import Comparison.Runner.RunningParameter;

public class GeneratingSeq {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		 File[] processedfiles = new File("/Users/emadalharbi/Desktop/PhDYork/jcsg202/EditedNames").listFiles();
			
		 for (File file : processedfiles) {
			 if(file.getName().substring(file.getName().indexOf('.')).equals(".pdb")){
				 System.out.println(file.getName());
				 System.out.println(file.getAbsolutePath());
				 System.out.println(file.getParent()+"/"+file.getName().substring(0,file.getName().indexOf('.')));
				 
				 
				 String[]callAndArgs= {"python",
						 "/Applications/ccp4-7.0/share/python/CCP4Dispatchers/cmodeltoseq.py",
				"-pdbin",file.getAbsolutePath(),
				"-seqout",file.getParent()+"/"+file.getName().substring(0,file.getName().indexOf('.'))+".seq"};
				 Process p = Runtime.getRuntime().exec(callAndArgs);
			 }
			 
			
		 }
	}

}
