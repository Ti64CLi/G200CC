package compiler;


import static org.junit.jupiter.api.Assertions.*;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.Test;

import compiler.Compiler;
import compiler.frontend.IRBuilder;
import compiler.frontend.SimpleCPrinter;
import ir.core.IRTopLevel;

class testFrontend {

	private void testPattern(String path) {
		String contentInit = Compiler.readFile(path);
		ParseTree tree = Compiler.parse(contentInit);
		IRTopLevel top = Compiler.frontend(tree);
		assert(true);//Ok if no exception before
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
