/**
 * 
 */

/**
 * @author sneha_2
 *
 */
import java.io.File;
public class Umlparser {
	//private String fileName = "";
	public static void main (String[] args) throws Exception {
	
		String sourceFolder = "";
		String fileName = "";
		String  parserOutput = "";
		if (args.length == 2)
		{
			sourceFolder = args[0];
			fileName = args[1];	
			Umlparser u = new Umlparser();
			u.getJavaSourceFiles(sourceFolder);
			//parserOutput = u.parseJava(sourceFolder);
			//u.generateUML(parserOutput);
			  
		}
		else
		{
			System.out.println("Please provide input in correct format - <source folder><output file name>" );
		}	
		
	}
	public void getJavaSourceFiles(String sourceFolder)
	{
		File srcFolder = new File(sourceFolder);
		if(srcFolder.isDirectory())
		{
			File[] allFiles = srcFolder.listFiles();			
			if (allFiles != null) {
				for (File f : allFiles) {
					if(f.getName().endsWith(".java")){
					parseCode(f);
					}
			    }	
			}
		}
	}
	public void parseCode(File sourceFile)
	{
	ParseJava p = new ParseJava();
	p.parseCode(sourceFile);
	
	}
	
	public void generateUML(String parserOutput)
	{
		//call uml generator
	}
	}

