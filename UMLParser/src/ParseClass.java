/**
 * 
 */

/**
 * @author sneha_2 
 *
 */

import java.util.*;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.body.Parameter;

public class ParseClass {
	final CompilationUnit c;
	private ArrayList<String> interfaces = new ArrayList<String>();
	private ArrayList<String> attributes = new ArrayList<String>();
	private ArrayList<String> getterSetters = new ArrayList<String>();
	private ArrayList<String> methods = new ArrayList<String>();
	private ArrayList<CompilationUnit> cuList;

	HashMap<String, Boolean> mapClassOrInterface = new HashMap<String, Boolean>();
	HashMap<String, String> mapClassConn = new HashMap<String, String>();

	public ParseClass(CompilationUnit c, ArrayList<CompilationUnit> cList ) {
		this.c = c;
		this.cuList = cList;
		createMapCI(cuList);
	}

	public void createMapCI(ArrayList<CompilationUnit> cList) {
		 for (CompilationUnit c : cList) {
	            List<TypeDeclaration<?>> td = c.getTypes();
	            for (Node n : td) {
	                ClassOrInterfaceDeclaration ci = (ClassOrInterfaceDeclaration) n;
	                mapClassOrInterface.put(ci.getName().toString(), ci.isInterface());	                                                           
	            }
		 }
	}

	public void parseClassElements(TypeDeclaration<?> t) {
		String modifier = "";
		String methodName = "";
		String additions = ",";
		String className = t.getName().toString();
		NodeList<ClassOrInterfaceType> s = ((ClassOrInterfaceDeclaration) t).getImplementedTypes();
		NodeList<ClassOrInterfaceType> e = ((ClassOrInterfaceDeclaration) t).getExtendedTypes();

		List<BodyDeclaration<?>> bodyList = t.getMembers();
		for (BodyDeclaration<?> b : bodyList) {
			if (b instanceof FieldDeclaration) {
				EnumSet<Modifier> mods = ((FieldDeclaration) b).getModifiers();
				String attrName = ((FieldDeclaration) b).getChildNodes().get(1).toString();
				String attrType = ((FieldDeclaration) b).getElementType().toString();
				modifier = checkModifier(mods, "field");
				// if private attribute with getter/setter - display as public
				// attribute
				if (modifier == "-" && getterSetters.contains(attrName.toLowerCase())) {
					modifier = "+";
				}
				// TO DO -- chk instance variable with no corresponding java
				// source file
				if (modifier == "+") {
					attributes.add(modifier + " " + attrName + " : " + attrType);
				}

			} else if (b instanceof ConstructorDeclaration) {
				EnumSet<Modifier> mods = ((ConstructorDeclaration) b).getModifiers();
				modifier = checkModifier(mods, "method");
				String paramFormat = "";
				if (modifier == "+") {
					List<Parameter> parameters = ((MethodDeclaration) b).getParameters();
					String paramType = "", paramName = "";
					if (parameters.size() > 0) {
						paramFormat += "(";
						for (Parameter p : parameters) {

							paramType = p.getType().toString();
							paramName = p.getName().toString();
							if (paramFormat != "(") {
								paramFormat += ",";
							}
							paramFormat += paramName + " : " + paramType;
						}
						paramFormat += ")";
					} else {
						paramFormat += "()";
					}
					methods.add(modifier + " " + methodName + paramFormat);
				}

			} else if (b instanceof MethodDeclaration) {

				methodName = ((MethodDeclaration) b).getName().toString();
				String returnType = ((MethodDeclaration) b).getType().toString();
				EnumSet<Modifier> mods = ((MethodDeclaration) b).getModifiers();
				String paramFormat = "";
				modifier = checkModifier(mods, "method");
				if (modifier != "" && modifier == "+") {
					// if getter/setter, display it as public attribute
					if (methodName.startsWith("get") || methodName.startsWith("set")) {
						getterSetters.add(methodName.substring(3).toLowerCase());
					}
					// other methods
					else {
						List<Parameter> parameters = ((MethodDeclaration) b).getParameters();
						String paramType = "", paramName = "";
						if (parameters.size() > 0) {
							paramFormat += "(";
							for (Parameter p : parameters) {

								paramType = p.getType().toString();
								paramName = p.getName().toString();
								if (paramFormat != "(") {
									paramFormat += ",";
								}
								paramFormat += paramName + " : " + paramType;
							}
							paramFormat += ")";
						} else {
							paramFormat += "()";
						}
						methods.add(modifier + " " + methodName + paramFormat + " : " + returnType);
					}

				}
			}

		}
	}

	public String checkModifier(EnumSet<Modifier> mods, String type) {
		String modifier = "";
		if (mods.contains(Modifier.PUBLIC)) {
			modifier = "+";
		}
		if (type == "field") {
			if (mods.contains(Modifier.PRIVATE)) {
				modifier = "-";
			}
		} else if (type == "method") {

			if (mods.contains(Modifier.PUBLIC) && mods.contains(Modifier.STATIC)) {
				modifier = "+";
			}
		}
		return modifier;
	}
}

