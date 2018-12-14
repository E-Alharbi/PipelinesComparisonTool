package ToolsExecution;
/**
*
* @author Emad Alharbi
* University of York
*/
import java.io.File;
import java.io.IOException;

public class ThreadsRunner {

	public void Threads(Tool tool) throws InstantiationException, IllegalAccessException {
		Tool PD=null;
		 if(tool instanceof PhenixRunner) {
	    	  System.out.println("PhenixRunner");
				PD =new PhenixRunner();
			}
		 if(tool instanceof Buccaneeri2Runner) {
	    	  System.out.println("Buccaneeri2Runner");
				PD =new Buccaneeri2Runner();
			}
		 if(tool instanceof CBuccaneerRunner) {
	    	  System.out.println("RunCBuccaneer");
				PD =new CBuccaneerRunner();
			}
		 if(tool instanceof ArpRunner) {
	    	  System.out.println("ArpRunner");
				PD =new ArpRunner();
			}
		 if(tool instanceof CrankRunner) {
	    	  System.out.println("CrankRunner");
				PD =new CrankRunner();
			}
		 
     int NumberOfThreads=Runtime.getRuntime().availableProcessors()*10;
		for(int i=0 ; i < NumberOfThreads ; ++i ) {
		ThreadInitialization T1 = new ThreadInitialization( "Thread - "+i, PD );
		//ThreadInitialization T2 = new ThreadInitialization( "Thread - 2 ", PD );

	      T1.start();
	      //T2.start();

	      // wait for threads to end
	         try {
	         //T1.join();
	         //T2.join();
	      } catch ( Exception e) {
	         System.out.println("Interrupted");
	      }
		}
	}
}
class ThreadInitialization extends Thread {
	   private Thread t;
	   private String threadName;
	   Tool  PD;

	   ThreadInitialization( String name,  Tool  pd) {
	      threadName = name;
	     
	      PD = pd;
	   }
	   
	   public void run() {
		   File file=null;
	      synchronized(PD) {
	    	  try {
				file= PD.PickACase();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      }
	    	try {
	    		if(PD instanceof PhenixRunner) {
	  	    	  
	  	    	new PhenixRunner().RunPhenixTool(file);
	  			}
	  		 if(PD instanceof Buccaneeri2Runner) {
	  	    	  System.out.println("Buccaneeri2");
	  	    	  Thread.currentThread();
	  	    	  
				Thread.sleep(10000); // to avoid conflicts between threads when access to the sql 
	  				new Buccaneeri2Runner().RunBuccaneerTool(file);;
	  			}
	  		if(PD instanceof CBuccaneerRunner) {
	  	    	  System.out.println("Buccaneeri1");
	  	    	 
	  				new CBuccaneerRunner().RunBuccaneerTool(file);;
	  			}
	  		if(PD instanceof ArpRunner) {
	  	    	  System.out.println("RunArp");
	  	    	 
	  				new ArpRunner().RunwArpTool(file);
	  			}
	  		if(PD instanceof CrankRunner) {
	  	    	  System.out.println("CrankRunner");
	  	    	 
	  				new CrankRunner().RunCrank(file);
	  			}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      
	      System.out.println("Thread " +  threadName + " exiting.");
	      
	   }

	   public void start () {
	      System.out.println("Starting " +  threadName );
	      if (t == null) {
	         t = new Thread (this, threadName);
	         t.start ();
	      }
	   }
	}