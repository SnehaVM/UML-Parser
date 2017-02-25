/**
 * 
 */

/**
 * @author sneha_2 
 *
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import com.github.javaparser.*;
import com.github.javaparser.ast.CompilationUnit;
//import for compilation unit 
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
//import for ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
public class ParseJava {
	
public void parseCode(File sourceFile)
{
 try {
	CompilationUnit c = JavaParser.parse(sourceFile);
	//List<NodeList<?>> allNodes = c.getNodeLists();	
	NodeList<TypeDeclaration<?>> typeList = c.getTypes();
	for(TypeDeclaration<?> t :typeList)
	{
		if(t instanceof ClassOrInterfaceDeclaration)
		{
			if(((ClassOrInterfaceDeclaration) t).isInterface()){
			 //is an interface -- TO DO
			}
			else
			{
			//is a class  --
				List<BodyDeclaration<?>> bodyList = t.getMembers();
				for(BodyDeclaration<?> b: bodyList)
				{
					//--To do
				}
			}
		}
	}
	
 } catch (FileNotFoundException e) {
	// TODO Auto-generated catch block
	//e.printStackTrace();
}
}
}
