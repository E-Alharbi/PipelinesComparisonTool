package Comparison.Runner;
/**
*
* @author Emad Alharbi
* University of York
*/
import java.io.File;
import java.io.IOException;

public class PipeliensRerun {
	public  void ReRun(String Path) throws IOException {
		
		
		
		File[] Pipeliens = new File(Path).listFiles();
		String Script="";
		for(File p : Pipeliens) {
			if(p.isDirectory() && !p.getName().contains("Analyser")) {
				File Results=new File("");
				File ToolScript=null;
				for(File r : p.listFiles()) {
					if(r.isDirectory() && r.getName().contains("Results"))
						Results=r;
					if(r.isFile() && !r.getName().contains("Manager")&&r.getName().contains("sh"))
					   ToolScript=r;
				}
				
			if(Results.exists() ) {
				
				RunningPram.PDBsDir=Results.getAbsolutePath()+"/PDBs";
				for(File log : Results.listFiles())
				{
					if(log.getName().contains("Logs") && !log.getName().contains("IntermediateLogs"))
						RunningPram.LogsDir=log.getAbsolutePath();
				}
				
				RunningPram.IntermediateLogs=Results.getAbsolutePath()+"/IntermediateLogs";
				RunningPram.IntermediatePDBs=Results.getAbsolutePath()+"/IntermediatePDBs";
				Script+="cd "+p.getAbsolutePath()+"\n";
				new CleanerForRunner().Clean();
				Script+="sh Cleaner.sh "+"\n";
				RunningPram.PDBsDir=Results.getAbsolutePath()+"PDBs";
				
				Script+="cd "+Path+"\n";
			}
			else {
				
				Script+="cd "+p.getAbsolutePath()+"\n";
				RunningPram.PDBsDir=null;
				
				Script+="cd "+Path+"\n";
			}
			
			}
		}
		
		new Preparer().WriteTxtFile("ReRun.sh",Script);
		
	}
}
