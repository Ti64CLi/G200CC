package compiler;


import static org.junit.jupiter.api.Assertions.*;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.Test;

import compiler.Compiler;
import compiler.frontend.SimpleCPrinter;

class testBackend {

	private void testPattern(String path) {
		String contentInit = Compiler.readFile(path);
		System.out.println("Initial content is : \n" + contentInit);
		ParseTree tree = Compiler.parse(contentInit);
		System.out.println("Parsed !");
    	SimpleCPrinter astPrinter = new SimpleCPrinter();
    	String genContent = astPrinter.visit(tree);
		System.out.println("Re-generated content is : \n" + genContent);
		ParseTree tree2 = Compiler.parse(genContent);
		System.out.println("Parsed !");
		String genContent2 = 	astPrinter.visit(tree2);
		System.out.println("Re-re-generated content is : \n" + genContent2);

		assert(genContent.equals(genContent2));
	}
	
	@Test
	void testParserAdd() {
		testPattern("src/test/resources/add.sc");
	}

	@Test
	void testParserFact() {
		testPattern("src/test/resources/add.sc");
	}
	
	@Test
	void testParserHello() {
		testPattern("src/test/resources/hello.sc");
	}
	
	@Test
	void testParserMax() {
		testPattern("src/test/resources/max.sc");
	}
	
	@Test
	void testParserPower() {
		testPattern("src/test/resources/power.sc");
	}

	@Test
	void testParserSum() {
		testPattern("src/test/resources/sum.sc");
	}
	
	@Test
	void testParserFunctions() {
		testPattern("src/test/resources/functions.sc");
	}
	
	@Test
	void testParserPrint() {
		testPattern("src/test/resources/print.sc");
	}
}
