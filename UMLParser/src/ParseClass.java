/**
 * 
 */

/**
 * @author sneha_2 
 *
 */

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

import com.github.javaparser.JavaParser;
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
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.body.Parameter;

public class ParseClass {

	private ArrayList<CompilationUnit> cuList = new ArrayList<CompilationUnit>();
	private ArrayList<String> privateFields = new ArrayList<String>();
	private ArrayList<String> getterSetters = new ArrayList<String>();
	HashMap<String, Boolean> mapClassOrInterface = new HashMap<String, Boolean>();
	HashMap<String, String> mapClassConn = new HashMap<String, String>();
	HashMap<String, String> mapFullMembers = new HashMap<String, String>();
	String srcFolder = "";
	String diagramName = "";
	String relations = "";

	public ParseClass(String srcFolder, String diagramName) {
		this.srcFolder = srcFolder;
		this.diagramName = diagramName;
	}

	// Method to traverse the source directory
	public void getJavaSourceFiles() throws FileNotFoundException {
		String ymlGrammar = "";
		File sourceFolder = new File(srcFolder);
		if (sourceFolder.isDirectory()) {
			File[] allFiles = sourceFolder.listFiles();
			if (allFiles != null) {
				for (File f : allFiles) {
					// pass only .java files to the parser
					if (f.getName().endsWith(".java")) {
						CompilationUnit c = JavaParser.parse(f);
						cuList.add(c);
					}
				}
			}
		}
		createMapCI(cuList);
		for (CompilationUnit cu : cuList) {
			NodeList<TypeDeclaration<?>> typeList = cu.getTypes();
			for (TypeDeclaration<?> t : typeList) {
				parseAST(t, cu);
			}
		}
		// {[A-B=-*, [A-C=-, [A-D=-*}
		// [A]-*[B], [A]-1[C],[A]-*[D]
		// [C1] uses -.->[<<interface>>;A1]

		Set<String> keys = mapClassConn.keySet();
		Set<String> memKeys = mapFullMembers.keySet();
		String multiplicity = "";

		// get stored key:values to form yUML grammar
		for (String i : keys) {
			String val = mapClassConn.get(i);
			i = i.replace("[", "");
			String[] classes = i.split("-");
			String typeFirst = checkType(classes[0]);
			String typeSec = checkType(classes[1]);
			if (typeFirst == "class" && typeSec == "class")
				multiplicity += "[" + classes[0] + "]" + val + "[" + classes[1] + "]" + ",";
			else if (typeFirst == "class" && typeSec == "interface")
				multiplicity += "[" + classes[0] + "]" + val + "[" + "<<interface>>;" + classes[1] + "]" + ",";
			else
				multiplicity += "[" + "<<interface>>;" + classes[0] + "]" + val + "[" + "<<interface>>;" + classes[1]
						+ "]" + ",";
		}

		// if only association exists
		if (multiplicity.length() > 0 && relations.length() == 0) {
			for (String i : memKeys) {
				String val = mapFullMembers.get(i);
				if (!mapClassOrInterface.get(i)) {
					if (multiplicity.contains(i)) {
						multiplicity = multiplicity.replaceFirst(Pattern.quote("[" + i + "]"), "[" + val + "]");
					}
				}

			}
			// if both exists - combine
		} else if (multiplicity.length() > 0 && relations.length() > 0) {
			relations += multiplicity;
		}

		// relations other than association
		//TO DO
		if (relations.length() == 0)
			ymlGrammar = multiplicity;
		else if (multiplicity.length() == 0)
			ymlGrammar = relations;
		else {
			ymlGrammar = relations;
			// combine multiplicity and relations
		}

		// save UML diagram to the folder
		drawUML(ymlGrammar);

	}

	// check for 'class' or 'interface'
	public String checkType(String type) {
		if (mapClassOrInterface.containsKey(type))
			if (!mapClassOrInterface.get(type))
				return "class";
			else
				return "interface";
		return "";
	}

	// gets the uml and save to the folder
	public void drawUML(String yUMLGrammar) {
		String outputPath = "";
		String extension = "";
		String link = "https://yuml.me/diagram/plain/class/" + yUMLGrammar + ".png";
		// file extension fix
		int i = diagramName.lastIndexOf('.');
		if (i > 0) {
			extension = diagramName.substring(i + 1);
			if (!(extension.toLowerCase().equals("png") || extension.toLowerCase().equals("jpg"))) {
				diagramName = diagramName.substring(0, i + 1);
				diagramName = diagramName + "png";
			}
		} else {
			diagramName = diagramName + ".png";
		}
		outputPath = srcFolder + "\\" + diagramName;

		try {
			URL url = new URL(link);
			InputStream is = url.openStream();
			OutputStream os = new FileOutputStream(outputPath);
			byte[] b = new byte[2048];
			int length;
			while ((length = is.read(b)) != -1) {
				os.write(b, 0, length);
			}
			is.close();
			os.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// create a map out of compilation units
	public void createMapCI(ArrayList<CompilationUnit> cuList) {
		for (CompilationUnit c : cuList) {
			List<TypeDeclaration<?>> td = c.getTypes();
			for (Node n : td) {
				ClassOrInterfaceDeclaration ci = (ClassOrInterfaceDeclaration) n;
				mapClassOrInterface.put(ci.getName().toString(), ci.isInterface());
			}
		}

	}

	// return class/interface name from compilation unit
	public String getName(CompilationUnit c) {
		String className = "";
		NodeList<TypeDeclaration<?>> typeList = c.getTypes();
		Node n = typeList.get(0);
		ClassOrInterfaceDeclaration ci = (ClassOrInterfaceDeclaration) n;
		if (ci.isInterface()) {
			className = "[" + "<<interface>>;";
		} else {
			className = "";
		}
		className += ci.getName();
		return className;
	}

	// processes the Abstract Syntax Tree
	public void parseAST(TypeDeclaration<?> t, CompilationUnit c) {
		String className = getName(c);
		String modifier = "";
		String methodName = "";
		String relationships = "";
		String fields = "";
		boolean nextField = false;
		boolean addDelimiter = false;
		String functions = "";
		String name = t.getName().toString();
		NodeList<TypeDeclaration<?>> typeList = c.getTypes();
		Node n = typeList.get(0);
		ClassOrInterfaceDeclaration ci = (ClassOrInterfaceDeclaration) n;
		List<BodyDeclaration<?>> bodyList = t.getMembers();
		for (BodyDeclaration<?> b : bodyList) {
			// parses constructor
			if (b instanceof ConstructorDeclaration) {
				String paramType = "", paramName = "";
				// get modifier
				modifier = checkModifier(((ConstructorDeclaration) b).getModifiers(), "method");
				if (modifier == "+") {
					if (addDelimiter) {
						functions += ";";
					}
					functions += modifier + ((ConstructorDeclaration) b).getName() + "(";
					// get constructor parameters
					List<Parameter> parameters = ((ConstructorDeclaration) b).getParameters();
					if (parameters.size() > 0) {
						for (Parameter p : parameters) {
							paramType = p.getType().toString();
							paramName = p.getName().toString();
							functions = functions + paramName + " : " + paramType;
							// find dependency
							if (mapClassOrInterface.containsKey(paramType) && !(mapClassOrInterface.get(name))) {
								String rl = "[" + name + "] uses -.->";
								if (mapClassOrInterface.get(paramType)) {
									rl += "[<<interface>>;" + paramType + "]";
									if (!(relationships.contains(rl))) {
										relationships += rl;
										relationships += ",";
									}
								}
							}
						}
					}
					functions = functions + ")";
					addDelimiter = true;
				}

			} else if (b instanceof MethodDeclaration) {
				methodName = ((MethodDeclaration) b).getName().toString();
				String returnType = ((MethodDeclaration) b).getType().toString();
				String paramType = "", paramName = "";
				modifier = checkModifier(((MethodDeclaration) b).getModifiers(), "method");
				if (modifier != "" && modifier == "+") {
					// if getter/setter, display it as public attribute
					if (methodName.startsWith("get") || methodName.startsWith("set")) {
						getterSetters.add(methodName.substring(3).toLowerCase());
					}
					// other methods
					else {
						if (addDelimiter) {
							functions = functions + ";";
						}
						functions = functions + modifier + methodName + "(";
						List<Node> nodes = ((MethodDeclaration) b).getChildNodes();
						if (nodes.size() > 0) {
							for (Node cnode : nodes) {
								if (cnode instanceof Parameter) {
									paramType = ((Parameter) cnode).getType().toString();
									// formatting brackets to suit yUML grammar
									if (paramType.contains("[") || paramType.contains("<")) {
										paramType = paramType.replace("[", "(*");
										paramType = paramType.replace("]", ")");
										paramType = paramType.replace("<", "(*");
										paramType = paramType.replace("<", ")");
									}
									paramName = ((Parameter) cnode).getNameAsString();
									functions += paramName + " : " + paramType;
									// checking dependency w.r.t parameters
									if (mapClassOrInterface.containsKey(paramType)
											&& !(mapClassOrInterface.get(name))) {
										String rl = "[" + name + "] uses -.->";
										if (mapClassOrInterface.get(paramType)) {
											rl += "[<<interface>>;" + paramType + "]";
											if (!(relationships.contains(rl))) {
												relationships += rl;
												relationships += ",";
											}
										}
									}

								} else {
									// checking dependency within method block
									if (cnode instanceof BlockStmt) {
										String methodBody[] = cnode.toString().split(" ");
										for (String mb : methodBody) {
											// 12/4/2017
											if (mapClassOrInterface.containsKey(mb) && !mapClassOrInterface.get(name)) {
												relationships += "[" + name + "] uses -.->";
												if (mapClassOrInterface.get(mb))
													relationships += "[<<interface>>;" + mb + "]";
												else
													relationships += "[" + mb + "]";
												relationships += ",";
											}

										}
									}
								}

							}
							functions += ") : " + returnType;
							addDelimiter = true;
						}

					}
				}

			} else if (b instanceof FieldDeclaration) {
				String attributeName = "";
				String attributeType = "";
				Object o = b.getChildNodes().get(0);
				if (o instanceof VariableDeclarator) {
					attributeName = ((VariableDeclarator) o).getName().toString();
					attributeType = ((VariableDeclarator) o).getType().toString();
				}
				// formatting brackets to suit yUML grammar
				if (attributeType.contains("[") || attributeType.contains("<")) {
					attributeType = attributeType.replace("[", "(");
					attributeType = attributeType.replace("]", ")");
					attributeType = attributeType.replace("<", "(");
					attributeType = attributeType.replace(">", ")");
				}
				modifier = checkModifier(((FieldDeclaration) b).getModifiers(), "field");
				// if private field with getter/setter - display as public field
				if (modifier == "-") {
					privateFields.add(attributeName);
				}

				String associatedClassifier = "";
				boolean multiplicity_many = false;
				if (attributeType.contains("(")) {
					associatedClassifier = attributeType.substring(attributeType.indexOf("(") + 1,
							attributeType.indexOf(")"));
					multiplicity_many = true;
					if (associatedClassifier.length() == 0 && !(mapClassConn.containsKey(attributeType))) {
						attributeType = attributeType.substring(0, attributeType.indexOf("(") + 1) + "*" + ")";
					}
				} else if (mapClassOrInterface.containsKey(attributeType)) {
					associatedClassifier = attributeType;
				}
				// check association and multiplicity
				String chkAssociation = "";
				if (associatedClassifier.length() > 0 && mapClassOrInterface.containsKey(associatedClassifier)) {
					chkAssociation = "-";
					if (mapClassConn.containsKey(associatedClassifier + "-" + name)) {
						chkAssociation = mapClassConn.get(associatedClassifier + "-" + name);
						if (multiplicity_many)
							chkAssociation = "*" + chkAssociation;
						else
							chkAssociation = "1" + chkAssociation;
						mapClassConn.put(associatedClassifier + "-" + name, chkAssociation);
					} else {
						if (multiplicity_many)
							chkAssociation += "*";
						else
							chkAssociation += "1";
						mapClassConn.put(name + "-" + associatedClassifier, chkAssociation);
					}
				}
				if (modifier == "+" || modifier == "-") {
					if (!mapClassOrInterface.containsKey(associatedClassifier)
							&& !mapClassOrInterface.containsKey(attributeType)) {
						if (nextField)
							fields += "; ";
						fields += modifier + attributeName + " : " + attributeType;
						nextField = true;
					}
				}
			}

		}
		// added for getter/setter 12/4/2017 - start
		for (String pf : privateFields) {
			if (getterSetters.contains(pf.toLowerCase()))
				fields = fields.replace("-" + pf, "+" + pf);
		}
		// added 12/4/2017 - end
		// check inheritance
		if (!ci.getExtendedTypes().isEmpty()) {
			String key = ci.getExtendedTypes().get(0).toString();
			if (mapClassOrInterface.containsKey(key)) {
				relationships += "[" + className + "] " + "-^ " + "[" + ci.getExtendedTypes().get(0) + "]";
			} else {
				relationships += "[" + className + "] " + "-^ " + ci.getExtendedTypes().get(0);
			}
			relationships += ",";
		}
		// check interface implementations
		if (!ci.getImplementedTypes().isEmpty()) {
			List<ClassOrInterfaceType> interfaceList = (List<ClassOrInterfaceType>) ci.getImplementedTypes();
			for (ClassOrInterfaceType intface : interfaceList) {
				relationships += "[" + className + "] " + "-.-^ " + "[" + "<<interface>>;" + intface + "]";
				relationships += ",";
			}
		}
		// Combine className, methods and fields and add to map
		String st = "";
		st += name;
		if (!fields.isEmpty()) {
			st += "|" + fields;
		} else
			st += "|";
		if (!functions.isEmpty()) {
			st += "|" + functions;
		} else
			st += "|";
		
		//TO ADD 

		if (!(relations.lastIndexOf(",") == relations.length() - 1))
			relations += ",";
		relations += relationships;

	}

	// returns the modifier for attributes and method parameters
	private String checkModifier(EnumSet<Modifier> mods, String type) {
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
