/**
 * 
 */

/**
 * @author sneha_2
 *
 */
import java.io.File;
public class Umlparser {
	public static void main(String[] args) throws Exception {
		String sourceFolder = "";
		String fileName = "";
		// Get inputs from command line args
		if (args.length == 2) {			
			sourceFolder = args[0];
			fileName = args[1];
			ParseClass p = new ParseClass(sourceFolder, fileName);
		        p.getJavaSourceFiles();
			
		} 
		else {
			// incorrect input format
			System.out.println("Please provide input in correct format - <source folder><output file name>");
		}
	}
	
}
