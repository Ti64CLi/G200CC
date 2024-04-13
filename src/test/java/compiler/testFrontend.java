package compiler;

import static org.junit.jupiter.api.Assertions.*;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.Test;

import compiler.Compiler;
import compiler.frontend.IRBuilder;
import compiler.frontend.SimpleCPrinter;
import ir.core.IRTopLevel;
import ir.importExport.IRExport;

class testFrontend {

	private void testPattern(String path) {
		String contentInit = Compiler.readFile(path);
		ParseTree tree = Compiler.parse(contentInit);
		IRTopLevel top = Compiler.frontend(tree);
		SimpleCPrinter astPrinter = new SimpleCPrinter();
		String genContent = astPrinter.visit(tree);
		System.out.println("\nContent of file '" + path + "' :");
		System.out.println(genContent);
		System.out.println("IR representation of file '" + path + "' :");
		System.out.println(IRExport.printIR(top));
		assert (true);// Ok if no exception before
	}
    
    @Test
    void testDoWhile() {
        testPattern("src/test/resources/do_while.sc");
    }


    @Test
    void testSimpleWhile() {
        testPattern("src/test/resources/simple_while.sc");
    }

	@Test
	void testExamplePart2() {
		testPattern("src/test/resources/example_part2.sc");
	}

	@Test
	void testForNoVariables() { testPattern("src/test/resources/bare_for.sc"); }

	@Test
	void testIfWithoutElse() {
		testPattern("src/test/resources/if_no_else.sc");
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
