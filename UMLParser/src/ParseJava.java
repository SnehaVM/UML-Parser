/**
 * 
 */

/**
 * @author sneha_2 
 *
 */
import java.io.File;
import com.github.javaparser.*;
import com.github.javaparser.ast.CompilationUnit;
//import for compilation unit 
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

public class ParseJava {
	public void parseCode(File sourceFile) {
		ParseClass parseClass;
		//ParseInterface parseInfc;

		try {
			// parser returns Abstract Syntax Tree
			final CompilationUnit c = JavaParser.parse(sourceFile);
			// Return the list of types in compilation unit
			NodeList<TypeDeclaration<?>> typeList = c.getTypes();
			// String className = "";
			for (TypeDeclaration<?> t : typeList) {
				// we need only class/interface types now
				if (t instanceof ClassOrInterfaceDeclaration) {
					if (((ClassOrInterfaceDeclaration) t).isInterface()) {
						// is an interface -- TO DO
					} else {
						// is a class 
						parseClass = new ParseClass();
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
