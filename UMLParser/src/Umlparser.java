/**
 * 
 */

/**
 * @author sneha_2
 *
 */

public class Umlparser {

	public static void main(String[] args) throws Exception {
		try {
			if (args.length == 2) {
				String sourceFolder = args[0];
				String fileName = args[1];
				ParseClass p = new ParseClass(sourceFolder, fileName);
				p.getJavaSourceFiles();

			} else {
				System.out.println("Please provide input in correct format - <sourcefolder> <output file name>");
			}
		} catch (Exception e) {
			System.out.println("Incorrect Input format");
		}
	}

}
