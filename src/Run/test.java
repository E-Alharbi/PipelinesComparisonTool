package Run;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collection;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

import com.ibm.statistics.plugin.*;

import Analyser.DataContainer;
import Analyser.ExcelSheet;
import Analyser.LoadExcel;
import Analyser.ResultsAnalyserMultiThreads;
import Analyser.ResultsInLatex;
import NotUsed.ARPResultsAnalysis;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
public class test {
	
	
	//Not Used
	void Prog1() throws IOException, InterruptedException {
		Path path = Paths.get("ProcessedFilesNamesCrank.txt");
        FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ,
            StandardOpenOption.WRITE);
        System.out.println("File channel opened for read write. Acquiring lock...");

        FileLock lock = fileChannel.lock();  // gets an exclusive lock
        System.out.println("Lock is shared: " + lock.isShared());

        ByteBuffer buffer = ByteBuffer.allocate(20);
        int noOfBytesRead = fileChannel.read(buffer);
        System.out.println("Buffer contents: ");	

        while (noOfBytesRead != -1) {

            buffer.flip();
            System.out.print("    ");

            while (buffer.hasRemaining()) {
	
                System.out.print((char) buffer.get());                
            }

            System.out.println(" ");

            buffer.clear();
            Thread.sleep(1000);
            noOfBytesRead = fileChannel.read(buffer);
        }

        fileChannel.close();
        System.out.print("Closing the channel and releasing lock.");
	}
	
	void Prog2() throws IOException, InterruptedException  {
		System.out.println("Prog2 ");
		Path path = Paths.get("ProcessedFilesNamesCrank.txt");
        FileChannel fileChannel=null;
		try {
			fileChannel = FileChannel.open(path, StandardOpenOption.READ,
			    StandardOpenOption.WRITE);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
        System.out.println("File channel opened for read write. Acquiring lock...");

        FileLock lock=null;
		try {
			lock = fileChannel.lock();
			 System.out.println("Lock is shared:2 " + lock.isShared());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("hereee ");
			Prog2();
			e.printStackTrace();
		}  // gets an exclusive lock
       

        ByteBuffer buffer = ByteBuffer.allocate(20);
        int noOfBytesRead=0;
		try {
			
			noOfBytesRead = fileChannel.read(buffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
        System.out.println("Buffer contents: ");	

        while (noOfBytesRead != -1) {

            buffer.flip();
            System.out.print("    ");

            while (buffer.hasRemaining()) {
	
                System.out.print((char) buffer.get());                
            }

            System.out.print("Prog2 ");

            buffer.clear();
            Thread.sleep(1000);
            noOfBytesRead = fileChannel.read(buffer);
        }

        fileChannel.close();
        System.out.print("Closing the channel and releasing lock.");
	}
	public static void main(String[] args) throws IOException, InterruptedException, StatsException {
	
		
		/*
		new Thread() { 
	        public void run() {
	        	try {
					new test().Prog1();
				} catch (IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	    }.start();
	    new Thread() { 
	        public void run() {
	        	try {
					new test().Prog2();
				} catch (IOException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	    }.start();
	    //etc

	  
		
		File[] Logs = new File("/Volumes/PhDHardDrive/BuccaneerResults/BuccaneerLogs").listFiles();
		File[] PDBs = new File("/Volumes/PhDHardDrive/BuccaneerResults2/PDBs").listFiles();
		
		 for (File Log : Logs) {
			 boolean Found=false;
			 for (File PDB : PDBs) {
				// System.out.println("Log "+Log.getName().replaceAll("."+FilenameUtils.getExtension(Log.getName()),""));
				// System.out.println("PDB "+PDB.getName().replaceAll("."+FilenameUtils.getExtension(PDB.getName()),""));
			 if(Log.getName().replaceAll("."+FilenameUtils.getExtension(Log.getName()),"").equals(PDB.getName().replaceAll("."+FilenameUtils.getExtension(PDB.getName()),"")) ) {
				 Found=true;
				 break;
				
			 }
			 }
			 if(Found==false) {
				 System.out.println(Log.getName());
			 }
		 }
		*/
		int count=0;
		File[] Logs = new File("/Volumes/PhDHardDrive/TempResults/Pipelines/PhenixNoncsLogsHLA").listFiles();
		String PDBs = "/Volumes/PhDHardDrive/TempResults/Pipelines/noncsPDBS/PhenixnoncsPDBSHLA/";
		for (File Log : Logs) {
			 String Txt = new ResultsAnalyserMultiThreads().readFileAsString(Log.getAbsolutePath());
			// if(Txt.contains("Disk quota exceeded")) {
				 //if(Txt.contains("NOTE: no model built")) {
			// if(Txt.trim().length()==0) {
			// if(Txt.contains("needs more memory than is available")) {
				 if(!Txt.contains("Done cleaning up")) {
				 System.out.println(Log.getName());
				 String PDB=PDBs+ Log.getName().replaceAll("."+FilenameUtils.getExtension(Log.getName()),"")+".pdb";
				 FileUtils.deleteQuietly(new File(PDB));
				 count++;
			 }
		}
		System.out.println(count);
		/*
		//File[] Logs = new File("/Users/emadalharbi/Desktop/test/CrankResultsIntermLogs/IntermediateLogs").listFiles();
		File[] Logs = new File("/Users/emadalharbi/Desktop/test/CrankResults/CrankLogs").listFiles();
		File[] PDBS = new File("/Users/emadalharbi/Desktop/test/CrankResultsPDB/PDBs").listFiles();
		//String [] Names = new String[Size];
		Vector<File> FaliedCases = new Vector<File>();
		int Counter=0;
		for (File Log : Logs) {
			boolean found=false;
		 for (File PDB : PDBS) {
			 
		 
		 
			 if(Log.getName().replaceAll("."+FilenameUtils.getExtension(Log.getName()),"").equals(PDB.getName().replaceAll("."+FilenameUtils.getExtension(PDB.getName()),"")))
			 {
				 found=true;
				 break;
			 }
			 
		 }
		 if(found==false) {
			 FaliedCases.add(Log);
		 }
		 }
		 Vector<String> Names = new Vector<String>();
		 for(int i=0 ; i < FaliedCases.size() ; ++i) {
				 String Txt = new ResultsAnalyserMultiThreads().readFileAsString(FaliedCases.get(i).getAbsolutePath());
			// if(!Txt.contains("Warning: PEAKMAX")&&
		     //!Txt.contains("Warning: Phases")) {
		//	if(Txt.contains("program.ProgramRunError: Buccaneer")) {
				// System.out.println(Log.getName());
				 
				 Names.add(FaliedCases.get(i).getName());
				 ++Counter;
			// }
			
			// if(Txt.contains("Warning: PEAKMAX")) {
			//	 
			// }
		 }
		
		 String [] SortedNAmes = Names.toArray(new String[Names.size()]);
		System.out.println(Counter);
		Arrays.sort(SortedNAmes);
		for (int  i=0 ; i < SortedNAmes.length ; ++i) {
			//System.out.print(SortedNAmes[i].replaceAll("-parrot-noncs.txt", "") +" \u212B ");
			System.out.println(SortedNAmes[i]);
		}
		*/
		/*
		LoadExcel e = new LoadExcel();
		Vector<DataContainer> Arp =e.ReadExcel("/Volumes/PhDHardDrive/jcsg1200Results/ExcelSheets11/mrncs/ARPwARP.xlsx");
		Vector<DataContainer> Bucc =e.ReadExcel("/Volumes/PhDHardDrive/jcsg1200Results/ExcelSheets11/mrncs/Buccaneer5C.xlsx");
	int HowManyModel=0;
	int Models=0;
		for(int i=0 ; i < Arp.size() ; ++i) {
			for(int m=0 ; m < Bucc.size() ; ++m) {
		if(Arp.get(i).PDB_ID.equals(Bucc.get(m).PDB_ID) && Arp.get(i).BuiltPDB.equals("T") &&Bucc.get(m).BuiltPDB.equals("T") ) {
			Models++;
			if(Integer.valueOf(Arp.get(i).Completeness) < Integer.valueOf(Bucc.get(m).Completeness) ) {
				HowManyModel++;	
			}
		}
			}
		}
		System.out.println(Models);
		System.out.println(HowManyModel);
		*/
		/*
		LoadExcel e = new LoadExcel();
		Vector<DataContainer> Arp =e.ReadExcel("/Volumes/PhDHardDrive/jcsg1200Results/ExcelSheets12/mrncs/Buccaneer5C.xlsx");
		Vector<DataContainer> ArpSeq =e.ReadExcel("/Volumes/PhDHardDrive/jcsg1200Results/ExcelSheets12/mrncs/BuccaneerNewSeqR1.xlsx");
int count = 0 ;
Vector<DataContainer> NewCon= new Vector<DataContainer>();
		for(int i=0 ; i < Arp.size() ; ++i) {
			for(int m=0 ; m < ArpSeq.size() ; ++m) {
				if(ArpSeq.get(m).BuiltPDB.equals("T"))
				if(Arp.get(i).PDB_ID.equals(ArpSeq.get(m).PDB_ID) && Integer.valueOf(Arp.get(i).Completeness) < Integer.valueOf(ArpSeq.get(m).Completeness)) {
					if((Integer.valueOf(ArpSeq.get(m).Completeness) - Integer.valueOf(Arp.get(i).Completeness)) > 5) {
					//System.out.print("PDB "+Arp.get(i).PDB_ID  );
					///System.out.print(" (ResSeq="+Arp.get(i).n1m2  );
					//System.out.print(" "+Arp.get(i).Completeness + "%)"  );
					//System.out.print(" Arp seq PDB "+ArpSeq.get(m).PDB_ID  );
					//System.out.print(" (ResSeq="+ArpSeq.get(m).n1m2  );
					//System.out.println(" "+ArpSeq.get(m).Completeness +"% )"   );
					count++;
					NewCon.add(Arp.get(i));
					NewCon.add(ArpSeq.get(m));
					System.out.println(Arp.get(i).PDB_ID+","+Arp.get(i).n1m2+","+Arp.get(i).Completeness +"%,"+ArpSeq.get(m).n1m2+","+ArpSeq.get(m).Completeness+"%");
					}
					
				}
			}
		}
		System.out.println(count);
		*/
		
		/*
		String SeqDir="/Volumes/PhDHardDrive/jcsg1200/Seq";
		String Jscg202="/Volumes/PhDHardDrive/jcsg1200/noncsWithMissingCases";
		File[] Seq = new File(SeqDir).listFiles();
		File[] Jscg = new File(Jscg202).listFiles();
		Vector<String> Names= new Vector<String>();
		 for (File PDB : Jscg) {
			 String PDBid=PDB.getName().substring(0, 4);
			 String PDBWithNoEx=PDB.getName().replaceAll("."+FilenameUtils.getExtension(PDB.getName()),"");
			 boolean Found=false;
			 if(!Names.contains(PDBWithNoEx)) {
			 for (File seq : Seq) {
				 
				 if(PDBid.contains(seq.getName().substring(0, seq.getName().length()-10).toLowerCase())) {
					// System.out.println(seq.getName().substring(0, seq.getName().length()-10).toLowerCase());
					 //System.out.println(seq.getName());
		 FileUtils.copyFile(new File(Jscg202+"/"+PDBWithNoEx+".mtz"),  new File("/Volumes/PhDHardDrive/jcsg1200/noncsWithMissingCasesFasta/"+PDBWithNoEx+".mtz"));
		 FileUtils.copyFile(new File(Jscg202+"/"+PDBWithNoEx+".pdb"),  new File("/Volumes/PhDHardDrive/jcsg1200/noncsWithMissingCasesFasta/"+PDBWithNoEx+".pdb"));
		 FileUtils.copyFile(new File(SeqDir+"/"+seq.getName()),  new File("/Volumes/PhDHardDrive/jcsg1200/noncsWithMissingCasesFasta/"+PDBWithNoEx+".fasta"));
					 Found=true;
				 }
				 Names.add(PDBWithNoEx) ;
			 }
			 if(Found==false) {
				 System.out.println(PDB.getName());
			 }
		 }
		 }
		 */

				/*
			LoadExcel e = new LoadExcel();
			
			 for (File PDB : Jscg) {
				 boolean Found=false;
				 for (File seq : Seq) {
					if(PDB.getName().substring(0, 4).equals(seq.getName().substring(0, 4).toLowerCase()))
					{
						Found=true;
						break;
				     }
				 }
				 if(Found==false) {
					 System.out.println(PDB.getName());
				 }
			 }
		
		 String ExcelDir="/Volumes/PhDHardDrive/jcsg1200Results/ExcelSheets4";
		File[] Folders = new File(ExcelDir).listFiles();
		LoadExcel e = new LoadExcel();
		
		 for (File Folder : Folders) {
			 if (Folder.isDirectory()) {
				 Vector<Vector<DataContainer>> Container = new Vector<Vector<DataContainer>>();
				 for (File Excel : Folder.listFiles()) {
					 Container.add(e.ReadExcel(Excel.getAbsolutePath()));
					 e.ToolsNames.add(Excel.getName()+Folder.getName());
				 }
				 for(int i=0; i < Container.size() ; ++i) {
					 
						
						 for(int y=0 ; y  <  Container.size() ; y++) {
							 boolean Match=true;
							 int MatchIndex=0;
							 for(int u=0 ;u < Container.get(i).size() ; ++u) {
							 for(int h=0 ; h < Container.get(y).size() ; ++h) {
								 if(Container.get(y).get(h).PDB_ID.equals(Container.get(i).get(u).PDB_ID)) {
								 if(!Container.get(y).get(h).n1m2.equals(Container.get(i).get(u).n1m2))
							       {
									 Match=false;
							      }
								 else {
									 MatchIndex=y;
								 }
								 }
							 }
							 
						 }
							 if(Match==true && MatchIndex!=i) {
								 System.out.println("Match ");
								 System.out.println(e.ToolsNames.get(i));
								 System.out.println(e.ToolsNames.get(MatchIndex));
							 }
						 
					 }
				 }
			 }
			
		
		 }
		*/
		/*
		LoadExcel e = new LoadExcel();
		Vector<DataContainer> Container = e.ReadExcel("/Volumes/PhDHardDrive/jcsg1200Results/ExcelSheets2/mrncs/Buccaneer.xlsx");
		Vector<DataContainer> TestingDataSet = new Vector<DataContainer>();
		String PDBs="";
		 for(int i=0 ; i < Container.size() ; ++i) {
			 if(!PDBs.contains(Container.get(i).PDBIDTXT))
			 System.out.print(Container.get(i).PDBIDTXT+",");
			 PDBs+=Container.get(i).PDBIDTXT;
		 }
		 */
		/*
		 Vector<Integer>Reso =  new ResultsInLatex().GroupedResults(Container);
		 
		 for(int r=0; r < Reso.size() ; ++r) {
			 Vector<DataContainer> ThisResoSet = new   Vector<DataContainer>();
			 int count=0;
			 for(int i=0 ; i < Container.size() ; ++i) {
				 Double Value = Double.parseDouble(Container.get(i).Resolution);
					if (Reso.get(r).equals(Value.intValue()))  {
						count++;
					}
			 }
			System.out.println(" Reso "+Reso.get(r) + " count "+count );
			 
		 }*/
		 
		 /*
		 for(int r=0; r < Reso.size() ; ++r) {
			 Vector<DataContainer> ThisResoSet = new   Vector<DataContainer>();
			 for(int i=0 ; i < Container.size() ; ++i) {
				 Double Value = Double.parseDouble(Container.get(i).Resolution);
					if (Reso.get(r).equals(Value.intValue()))  {
						ThisResoSet.add(Container.get(i));
					}
			 }
			 int TwoThird= (ThisResoSet.size()/3)*2;
			 TestingDataSet.addAll(ThisResoSet.subList(0, TwoThird));
			 
		 }
		 */
		 
		 //System.out.println("TestingDataSet "+TestingDataSet.size());
		 //new ExcelSheet().FillInExcel(TestingDataSet, ("TestingDataSet"));
		// System.out.println(" Created TestingDataSet ");
		//StatsUtil.start(); 
		/*
		String[] command={"GET FILE='//Applications/IBM/SPSS/Statistics/25/Samples/English/Employee data.sav'.",
				"OMS SELECT TABLES ",
				"/IF COMMANDS=['Bootstrap'] SUBTYPES=['Bootstrap Specifications'] ", "/DESTINATION FORMAT=OXML XMLWORKSPACE='desc_out' ", "/TAG='desc_out'.",
				 "Bootstrap /SAMPLING METHOD=SIMPLE", "/VARIABLES TARGET=salary INPUT=educ  ","/CRITERIA CILEVEL=95 CITYPE=PERCENTILE  NSAMPLES=10000  " , " /MISSING USERMISSING=EXCLUDE.",
				"OMSEND TAG='desc_out'.",
				 "ONEWAY salary BY educ" ," /STATISTICS DESCRIPTIVES"," /MISSING ANALYSIS."};
			*/
		/*
		String[] command={"GET DATA " , 
				"  /TYPE=XLSX " , 
				"  /FILE='/Volumes/PhDHardDrive/jcsg1200Results/spss/hancs/Reso3.xlsx' " , 
				"  /SHEET=name 'Data' " , 
				"  /CELLRANGE=FULL " , 
				"  /READNAMES=ON ", 
				"  /DATATYPEMIN PERCENTAGE=95.0 " , 
				"  /HIDDEN IGNORE=YES. " , 
				"AUTORECODE VARIABLES=File " , 
				"  /INTO Tool" , 
				"  /PRINT." , 
				"alter type Completeness(f3)." , 
				"alter type Rfactor0Cycle(f3)." , 
				"alter type TimeTaking(f6)." , 
				"OMS SELECT TABLES ",
				"/IF COMMANDS = ['ONEWAY'] ",
				"SUBTYPES = ['Descriptives']",
				"/DESTINATION FORMAT =XLSX OUTFILE = '/Users/emadalharbi/Desktop/test/hancsReso3.XLSX'.",
				" BOOTSTRAP" ,
				"  /SAMPLING METHOD=SIMPLE" , 
				"  /VARIABLES TARGET=Completeness Rfactor0Cycle TimeTaking INPUT=Tool" , 
				"  /CRITERIA CILEVEL=95 CITYPE=PERCENTILE  NSAMPLES=1000" , 
				"  /MISSING USERMISSING=EXCLUDE." , 
				"   ONEWAY Completeness Rfactor0Cycle TimeTaking BY Tool" , 
				"  /STATISTICS DESCRIPTIVES" , 
				"  /MISSING ANALYSIS." ,
				
				"OMSEND.",};
		
		 StatsUtil.submit(command);
		
		
		
	//System.out.println("desc_out "+StatsUtil.getXMLUTF16("desc_out"));
	
	 StatsUtil.stop();
	*/
				//String handle = "desc_table";
				//String context = "/outputTree";
				//String xpath = "//pivotTable[@subType='Descriptive Statistics']" +
				//"/dimension[@axis='row']" + "/category[@varName='salary']" + "/dimension[@axis='column']" + "/category[@text='Mean']" + "/cell/@text";
				//String[] result = StatsUtil.evaluateXPath(handle, context, xpath); StatsUtil.deleteXPathHandle(handle);
				
				
				
				
				
				//System.out.println(result[0]);
				
				//System.out.println(xml);
				//Writer out = new OutputStreamWriter(new FileOutputStream("descriptives_table.xml")); out.write(xml);
				//out.close();
		
		/*
		String[] command={"GET FILE='//Applications/IBM/SPSS/Statistics/25/Samples/English/Employee data.sav'.",
				"OMS SELECT TABLES ",
				"/IF COMMANDS=['Oneway'] SUBTYPES=['ANOVA'] ", "/DESTINATION FORMAT=OXML XMLWORKSPACE='desc_table' ", "/TAG='desc_out'.",
				"ONEWAY salbegin BY id ", " /MISSING ANALYSIS.",
				"OMSEND TAG='desc_out'."};
				StatsUtil.submit(command);
				String result = StatsUtil.getXMLUTF16("desc_table");
				System.out.println(result);
				*/
				/*
				String handle = "desc_table";
				String context = "/outputTree";
				String xpath = "//pivotTable[@subType='ANOVA']"
				
				+"/dimension[@axis='row']" + "/category[@varName='salary']" + "/dimension[@axis='column']" + "/category[@text='Mean']" + "/cell/@text";
				String[] result = StatsUtil.evaluateXPath(handle, context, xpath); StatsUtil.deleteXPathHandle(handle);
		System.out.println(result[0]);
		*/
		
				
				/*
		StatsUtil.start(); 
		String[] command={"DATA LIST LIST (',') /numVar (f) stringVar (f).",
				"BEGIN DATA",
				"1,1",
				"9,2",
				"END DATA.",
				"ONEWAY stringVar BY numVar.",
				
		};
		StatsUtil.submit(command);
		String result = StatsUtil.getXMLUTF16("ANOVA");
		//StatsUtil.submit("ONEWAY stringVar BY numVar");
		DataUtil datautil = new DataUtil();
		String[] varnames = datautil.getVariableNames(); 
		
		datautil.release();
		for(String name: varnames){
			System.out.println(name);
		}
		StatsUtil.stop();
		*/
		
	//new test().gg();
				/*
		LoadExcel e = new LoadExcel();
		Vector<DataContainer> Container = e.ReadExcel("/Volumes/PhDHardDrive/jcsg1200Results/ExcelSheets2/noncs/Crank.xlsx");
		int count=0;
		String Script="";
		String PDB="";
		
		for(int i=0; i < Container.size();++i ) {
				if(Container.get(i).BuiltPDB.equals("F")) {
					count++;
					Script+="rm -r "+Container.get(i).PDB_ID+" \n";
					Script+="rm -r project"+Container.get(i).PDB_ID+" \n";
					Script+="rm BuccaneerResults/BuccaneerLogs/"+Container.get(i).PDB_ID+".txt \n";
					Script+="rm BuccaneerResults/PDBs/"+Container.get(i).PDB_ID+".pdb \n";

				}
				else {
					PDB+=Container.get(i).PDB_ID+" \n";
				}
			}
			
		    for(int i=0; i < Container.size();++i ) {
			if(Container.get(i).BuiltPDB.equals("F")) {
				count++;
				Script+="rm CrankResults/PDBs/"+Container.get(i).PDB_ID+".pdb \n";
				Script+="rm CrankResults/CrankLogs/"+Container.get(i).PDB_ID+".txt \n";
				Script+="rm "+Container.get(i).PDB_ID+"xml \n";
				Script+="rm -r "+Container.get(i).PDB_ID+" \n";
			}
			else {
			 PDB+=Container.get(i).PDB_ID+" \n";
			}
		}
			 System.out.println(count);
			new Preparer().WriteTxtFile("RemoverScript.sh",Script);
			new Preparer().WriteTxtFile("ProcessedFilesNamesCrank.txt",PDB);
			*/
			// FilesNames.add(file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),""));
		
			// System.out.println(file.getName().replaceAll("."+FilenameUtils.getExtension(file.getName()),""));
		
		 }
		//System.out.println(processedfiles.length);
	
	String gg () {
		
		System.out.println(this.getClass().getSimpleName());
		return "";
	}
	Vector<String> AddFileNameToList( Vector<String> FilesNames) throws IOException{
		 String FileNamesTxt=new ARPResultsAnalysis().readFileAsString("/Users/emadalharbi/Desktop/PhDYork/PhinexResults/ProcessedFilesNames.txt");
		 FilesNames.addAll(Arrays.asList(FileNamesTxt.split("\n")));
		 return FilesNames;
		
	}
	void mm (DataContainer DC) {
		DC.BuiltPDB="111";
	}
}
