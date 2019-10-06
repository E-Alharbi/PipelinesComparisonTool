package Comparison.ToolsExecation.SingleThread;
/**
*
* @author Emad Alharbi
* University of York
*/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import Comparison.Runner.RunningParameter;

public class RunnerManager {
	

	public void ScriptRunnerManager(String ScriptPath, int RunTimes) throws IOException {

		
		for (int i = 0; i < RunTimes; ++i) {
			String[] callAndArgs = { ScriptPath,

			};

			

			Process p = Runtime.getRuntime().exec("qsub "+ScriptPath);

			BufferedReader stdInput = new BufferedReader(new

			InputStreamReader(p.getInputStream()));

			BufferedReader stdError = new BufferedReader(new

			InputStreamReader(p.getErrorStream()));

			String st = null;

			String JobID = "";
			while ((st = stdInput.readLine()) != null) {
				st=st.substring(0, st.indexOf('('));
				JobID = st.replaceAll("[^0-9]", "");
				System.out.println("Job id "+ JobID);
			}
			boolean isRun = false;
			while (isRun == false) {
				String[] CheckIfRun = { "qalter -w p " + JobID,
				};
				Process RunP = Runtime.getRuntime().exec("qalter -w p " + JobID);
				BufferedReader RunCheckingInput = new BufferedReader(new InputStreamReader(RunP.getInputStream()));
				while ((st = RunCheckingInput.readLine()) != null) {
					if (st.contains("running")) {
						isRun = true;
					}
					System.out.println("qalter "+ st);
				}
			}
		}
	}

}
