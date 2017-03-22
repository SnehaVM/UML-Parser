/**
 * 
 */

/**
 * @author sneha_2 
 *
 */

import java.util.EnumSet;
import java.util.List;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.ReferenceType;
import com.github.javaparser.ast.type.VoidType;

public class ParseClass {
	public void parseClassElements(TypeDeclaration<?> t) {
		String modifier = "";
		String className = t.getName().toString();
		List<BodyDeclaration<?>> bodyList = t.getMembers();
		for (BodyDeclaration<?> b : bodyList) {
			if (b instanceof FieldDeclaration) {
				EnumSet<Modifier> mods = ((FieldDeclaration) b).getModifiers();
				// get the access specifier
				modifier = checkModifier(mods, "field");
				// get the type
				if (modifier != "") {
					List<Node> cnodes = b.getChildNodes();
					for (Node c : cnodes) {
						if (c instanceof PrimitiveType) {
							// to do
						} else if (c instanceof ReferenceType) {
							// to do
						}
					}

				}
			} else if (b instanceof ConstructorDeclaration) {
				// to do
			} else if (b instanceof MethodDeclaration) {
				// to do
				EnumSet<Modifier> mods = ((MethodDeclaration) b).getModifiers();
				modifier = checkModifier(mods, "method");
				if (modifier != "") {
					List<Node> cnodes = b.getChildNodes();
					for (Node c : cnodes) {
						if (c instanceof PrimitiveType) {
							// to do
						} else if (c instanceof VoidType) {
							// to do
						}
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
/*
 * public static AccessSpecifier getAccessSpecifier(EnumSet<Modifier> modifiers)
 * { if (modifiers.contains(Modifier.PUBLIC)) { return AccessSpecifier.PUBLIC; }
 * else if (modifiers.contains(Modifier.PROTECTED)) { return
 * AccessSpecifier.PROTECTED; } else if (modifiers.contains(Modifier.PRIVATE)) {
 * return AccessSpecifier.PRIVATE; } else { return AccessSpecifier.DEFAULT; } }
 */
