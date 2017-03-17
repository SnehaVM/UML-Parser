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
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

public class ParseClass {
	public void parseClassElements(TypeDeclaration<?> t) {
		String className = t.getName().toString();
		List<BodyDeclaration<?>> bodyList = t.getMembers();
		for (BodyDeclaration<?> b : bodyList) {
			if (b instanceof FieldDeclaration) {
				EnumSet<Modifier> mods = ((FieldDeclaration) b).getModifiers();
				if (mods.contains(Modifier.PUBLIC)) {
					// Public attribute
				} else if (mods.contains(Modifier.PRIVATE)) {
					// private attribute

				}
			} else if (b instanceof ConstructorDeclaration) {
				// to do
			} else if (b instanceof MethodDeclaration) {
				// to do
			}

		}
	}
	/*
	 * public static AccessSpecifier getAccessSpecifier(EnumSet<Modifier>
	 * modifiers) { if (modifiers.contains(Modifier.PUBLIC)) { return
	 * AccessSpecifier.PUBLIC; } else if
	 * (modifiers.contains(Modifier.PROTECTED)) { return
	 * AccessSpecifier.PROTECTED; } else if
	 * (modifiers.contains(Modifier.PRIVATE)) { return AccessSpecifier.PRIVATE;
	 * } else { return AccessSpecifier.DEFAULT; } }
	 */

}
