package draft.utils;

import java.io.*;

public class readFile {
	public static String read(String fileName) {
	    String sCurrentLine = "nothing";
	    String content = "";
	    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
				while ((sCurrentLine = br.readLine()) != null) {
					content = content + sCurrentLine + "\n" ;
//	                             return sCurrentLine;
				}

			} catch (IOException e) {
				e.printStackTrace();                       
			}
	    return content;//sCurrentLine;
	}
	public static void read (ProcessBuilder processBuilder,
							 PrintWriter pw, String name,
							 String contentFile, String outputName){
		try {

    		Process process = processBuilder.start();

    		StringBuilder output = new StringBuilder();

    		BufferedReader reader = new BufferedReader(
    				new InputStreamReader(process.getInputStream()));

    		String line;
    		while ((line = reader.readLine()) != null) {
    			output.append(line + "\n");
    		}

    		int exitVal = process.waitFor();
    		if (exitVal == 0) {
    			System.out.println("Success!");
    			System.out.println(output);
    			pw.println("<br/>");
    			pw.println("Here is the content of "+ name + ".mc2 File");
    			pw.println("<br/>");
    			pw.println(contentFile.replace("\n", "<br/>"));
    			pw.println("<br/>");
    			pw.println("Here is the content of the output after Warp Script running"+"<br/>");
    			pw.println(output);
    			pw.println("<br/>");
    			writeFile.writeOut(outputName,output);
    			//System.exit(0);
    		} else {
    			//abnormal...
    		}

    	} catch (IOException e) {
    		e.printStackTrace();
    	} catch (InterruptedException e) {
    		e.printStackTrace();
    	}
	}
	public static void read (ProcessBuilder processBuilder, String name, String contentFile, String outputName){
		try {

			Process process = processBuilder.start();

			StringBuilder output = new StringBuilder();

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));

			String line;
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

			int exitVal = process.waitFor();
			if (exitVal == 0) {
				System.out.println("Success!");
				System.out.println(output);
				System.out.println("<br/>");
				System.out.println("Here is the content of "+ name + ".mc2 File");
				//pw.println("<br/>");
				System.out.println(contentFile);
				//pw.println("<br/>");
				System.out.println("Here is the content of the output after Warp Script running");
				System.out.println(output);
				//pw.println("<br/>");
				writeFile.writeOut(outputName,output);
				//System.exit(0);
			} else {
				//abnormal...
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public static void readNormal (ProcessBuilder processBuilder, String name, String contentFile, String outputName) {
		try {

			Process process = processBuilder.start();

			StringBuilder output = new StringBuilder();

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));

			String line;
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

			int exitVal = process.waitFor();
			if (exitVal == 0) {
				System.out.println("Success!");
				System.out.println(output);
				//System.out.println("<br/>");
				System.out.println("Here is the content of " + name + ".mc2 File");
				//pw.println("<br/>");
				System.out.println(contentFile);
				//pw.println("<br/>");
				System.out.println("Here is the content of the output after Warp Script running");
				System.out.println(output);
				//pw.println("<br/>");
				writeFile.writeOut(outputName, output);
				//System.exit(0);
			} else {
				//abnormal...
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

