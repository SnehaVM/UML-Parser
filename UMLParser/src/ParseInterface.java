/**
 * 
 */

/**
 * @author sneha_2 
 *
 */
import com.github.javaparser.ast.body.TypeDeclaration;
public class ParseInterface {
	String ifaceName = "";
	public void modelInterface(TypeDeclaration<?> t)
	{		
		ifaceName = t.getNameAsString();
		//to do
	}
}
