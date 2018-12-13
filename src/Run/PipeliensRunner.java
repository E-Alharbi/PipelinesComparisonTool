package Run;

import java.io.File;

public class PipeliensRunner {
	public static void main(String[] args) {
		
		File[] a = new File("/users/emra500/scratch").listFiles();
		System.out.print(a.length);
	}
}
