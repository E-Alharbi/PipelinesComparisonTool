package Run;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
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

import ResultsParsing.ARPResultsAnalysis;
import ResultsParsing.DataContainer;
import ResultsParsing.ExcelSheet;
import ResultsParsing.LoadExcel;
import ResultsParsing.ResultsAnalyserMultiThreads;
import ResultsParsing.ResultsInLatex;

import com.ibm.statistics.plugin.*;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
public class test {
	
	public static void main(String[] args) throws IOException, InterruptedException, StatsException {
	
		
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
		
		/*
		String SeqDir="/Volumes/PhDHardDrive/jcsg1200/Seq";
		String Jscg202="/Volumes/PhDHardDrive/jcsg1200/mrncs";
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
		 //FileUtils.copyFile(new File(Jscg202+"/"+PDBWithNoEx+".mtz"),  new File("/Volumes/PhDHardDrive/jcsg1200/mrncsWithFa/"+PDBWithNoEx+".mtz"));
		 //FileUtils.copyFile(new File(Jscg202+"/"+PDBWithNoEx+".pdb"),  new File("/Volumes/PhDHardDrive/jcsg1200/mrncsWithFa/"+PDBWithNoEx+".pdb"));
		 //FileUtils.copyFile(new File(SeqDir+"/"+seq.getName()),  new File("/Volumes/PhDHardDrive/jcsg1200/mrncsWithFa/"+PDBWithNoEx+".fasta"));
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
