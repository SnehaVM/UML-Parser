/**
 * 
 */

/**
 * @author sneha_2 
 *
 */
import java.io.File;
import java.io.*;
import java.util.ArrayList;
import com.github.javaparser.*;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

public class ParseJava {
	private ArrayList<CompilationUnit> compUnits = new ArrayList<CompilationUnit>();

	// Method to traverse the source directory
	public void getJavaSourceFiles(String sourceFolder) throws FileNotFoundException {
		File srcFolder = new File(sourceFolder);
		if (srcFolder.isDirectory()) {
			File[] allFiles = srcFolder.listFiles();
			if (allFiles != null) {
				for (File f : allFiles) {
					// pass only .java files to the parser
					if (f.getName().endsWith(".java")) {
						CompilationUnit c = JavaParser.parse(f);
						compUnits.add(c);
					}
				}
				parseCompUnits();
			}
		}
	}

	public void parseCompUnits() {
		for (CompilationUnit cu : compUnits) {
			parseCode(cu);
		}
	}

	public void parseCode(CompilationUnit c) {
		ParseClass parseClass;
		ParseInterface parseInfc;
		try {
			// Return the list of types in compilation unit
			NodeList<TypeDeclaration<?>> typeList = c.getTypes();			
			for (TypeDeclaration<?> t : typeList) {
				// we need only class/interface types now
				if (t instanceof ClassOrInterfaceDeclaration) {
					if (((ClassOrInterfaceDeclaration) t).isInterface()) {
						// is an interface -- TO DO
						ParseInterface parseInterface = new ParseInterface();
						parseInterface.modelInterface(t);

					} else {
						// is a class
						parseClass = new ParseClass(c);
						parseClass.parseClassElements(t);
					}
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
