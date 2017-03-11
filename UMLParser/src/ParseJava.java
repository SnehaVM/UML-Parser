/**
 * 
 */

/**
 * @author sneha_2 
 *
 */
import java.io.File;
import java.util.EnumSet;
import java.util.List;

import com.github.javaparser.*;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
//import for compilation unit 
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
//import for ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.AccessSpecifier;

public class ParseJava {
	
public void parseCode(File sourceFile)
{
 try {
	 //parser returns Abstract Syntax Tree
	final CompilationUnit c = JavaParser.parse(sourceFile);	
	//Return the list of types in compilation unit
	NodeList<TypeDeclaration<?>> typeList = c.getTypes();
	String className = "";
	for(TypeDeclaration<?> t :typeList)
	{
		//we need only class/interface types now
		if(t instanceof ClassOrInterfaceDeclaration)
		{
			if(((ClassOrInterfaceDeclaration) t).isInterface()){
			 //is an interface -- TO DO 
			}
			else
			{
			//is a class 
				className = t.getName().toString();
				List<BodyDeclaration<?>> bodyList = t.getMembers(); 
				for(BodyDeclaration<?> b: bodyList)
				{
					if(b instanceof FieldDeclaration)
					{											
						EnumSet<Modifier> mods = ((FieldDeclaration) b).getModifiers();		
						if(mods.contains(Modifier.PUBLIC))
						{
						//Public attribute
						}
						else if(mods.contains(Modifier.PRIVATE))	
						{
						//private attribute
						
						}
					}
					else if (b instanceof ConstructorDeclaration)
					{
						//to do
					}
					else if(b instanceof MethodDeclaration)
					{
						//to do
					}
					
				}
			}
		}
	}
	
 } catch (Exception e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
}
/*public static AccessSpecifier getAccessSpecifier(EnumSet<Modifier> modifiers) {
    if (modifiers.contains(Modifier.PUBLIC)) {
        return AccessSpecifier.PUBLIC;
    } else if (modifiers.contains(Modifier.PROTECTED)) {
        return AccessSpecifier.PROTECTED;
    } else if (modifiers.contains(Modifier.PRIVATE)) {
        return AccessSpecifier.PRIVATE;
    } else {
        return AccessSpecifier.DEFAULT;
    }
}*/
}
