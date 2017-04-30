
import java.io.*;
import java.util.Stack;
import net.sourceforge.plantuml.*;

public aspect ParseSequence {
	StringBuilder sb = new StringBuilder();
	Stack stack1 = new Stack();

	// 1)Join points consist of things like method calls, method
		// executions,object instantiations,
		// constructor executions, field references and handler executions
	// 2)Pointcuts pick out these join points

	pointcut mainMethod() : execution(* *main(..)) ;

	pointcut methods() : execution(* *(..)) && !execution(* *main(..));

	// get participants and messages
	before(): mainMethod()
		{
		String name = thisEnclosingJoinPointStaticPart.getSignature().getName().toString();
		StackTraceElement[] ste = new Throwable().getStackTrace();
		name = ste[1].getClassName();
		stack1.push(name);
		sb = new StringBuilder("@startuml\n");
		sb.append("autonumber" + "\n");
		sb.append("participant " + name + "\n");
		sb.append("activate " + name + "\n");

	}

	after(): mainMethod()
		{

		if (!stack1.empty()) {
			String classInStack = stack1.pop().toString();
		}
		StackTraceElement[] ste = new Throwable().getStackTrace();
		String current_class = ste[1].getClassName();
		sb.append("deactivate " + current_class + "\n");
		sb.append("@enduml");
		// System.out.println(sb.toString());
		generateDiagram(sb.toString());

	}

	before(): methods()
		{

		String caller = "";
		String callee = "";
		String callSignature = thisJoinPoint.getSignature().toString();
		if (thisJoinPoint.getTarget() == null) {
			callee = thisEnclosingJoinPointStaticPart.getSignature().getName().toString();
			// added start 20/4/17
			StackTraceElement[] ste = new Throwable().getStackTrace();
			callee = ste[1].getClassName();
			// added now ends 20/4/17
		} else {
			callee = thisJoinPoint.getTarget().getClass().getName().toString();
		}
		String returnType = callSignature.substring(0, callSignature.indexOf(" "));
		callSignature = callSignature.substring(callSignature.indexOf(".") + 1);
		String message = callSignature + ":" + returnType;
		if (!stack1.empty()) {
			caller = stack1.peek().toString();
		}
		stack1.push(callee);
		sb.append(caller + " -> " + callee + " : " + message + "\n");
		sb.append("activate " + callee + "\n");
		
	}

	after(): methods()
		{

		if (!stack1.empty()) {
			String cls = stack1.pop().toString();
		}
		String callee = "";
		if (thisJoinPoint.getTarget() == null) {
			callee = thisEnclosingJoinPointStaticPart.getSignature().getName().toString();
			StackTraceElement[] ste = new Throwable().getStackTrace();
			callee = ste[1].getClassName();
		} else {
			callee = thisJoinPoint.getTarget().getClass().getName().toString();
		}
		sb.append("deactivate " + callee + "\n");
		

	}

	private void generateDiagram(String grammar) {
		// String sourceFolder = "C:/Users/sneha_2/Desktop/images";
		String sub = "";
		String currentPath = new File(".").getAbsolutePath();
		currentPath = currentPath.replace(".", "");
		String outputPath = currentPath + "/" + "sequence_image" + ".png";
		// System.out.println(Arrays.toString(stack1.toArray()));		
		String save = grammar.substring(0, grammar.indexOf("Main ->"));				
		sub = grammar.substring(grammar.indexOf("->"));		
		sub = sub.substring(sub.indexOf("Main ->"));		
		grammar = "";
		grammar = save + sub; 		
		SourceStringReader reader = new SourceStringReader(grammar);
		FileOutputStream output;
		try {
			output = new FileOutputStream(outputPath);
			reader.generateImage(output, new FileFormatOption(FileFormat.PNG));
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}