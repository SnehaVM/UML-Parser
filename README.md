# UML-Parser
A parser which converts Java source code into UML class diagram and sequence diagram.

Techonology: Java

**UML class parser:**
 Converts Java Source Code into UML Class Diagram. 
 
**Tools/libraries used:**
- JDK
- Java grammar parser : https://github.com/javaparser/javaparser [http://javaparser.org/]
- UML Class Diagram generator: yUML [https://yuml.me/]

**Run the program:**
  Jar is located in: https://github.com/SnehaVM/UML-Parser/blob/master/Jar/UMLParserClass.jar
  
  Command:
   java -jar UMLParserClass.jar ./TestCases/test4 classDiagram.png   
   Argument1: full path to the testcase folder containing java files
   
   Argument2 - name of the output image file 
   

**UML Sequence parser:**
 Performs dynamic analysis of Java Source code to generate a UML Sequence Diagram. Implemented using Aspect oriented programming (AOP).
 
 **Tools/libraries used:**
 - JDK
 - UML Sequence Diagram generator: PlantUML (	http://plantuml.com/), Jar: plantuml.jar
 - aspectjrt.jar
 - aspectjtools.jar 
 
 path to the above libraries :https://github.com/SnehaVM/UML-Parser/tree/master/SequenceDiagramAspect/lib
 
 **Run the program:**
  Jar is located in: https://github.com/SnehaVM/UML-Parser/blob/master/Jar/UMLSequence_Aspectj.jar
  
  command:
  Java -jar UMLSequence_Aspectj.jar
  
  Shell script for compile time weaving : https://github.com/SnehaVM/UML-Parser/blob/master/SequenceDiagramAspect/Seqscript.sh
 
 
 
 

