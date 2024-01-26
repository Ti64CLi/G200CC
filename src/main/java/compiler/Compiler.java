package compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.List;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import antlr.IRTXTLexer;
import antlr.IRTXTParser;
import antlr.IRTXTParser.IrTopContext;
import antlr.SimpleCLexer;
import antlr.SimpleCParser;
import backend.BackendTopLevel;
import compiler.backend.BackendBuilder;
import compiler.frontend.IRBuilder;
import compiler.frontend.SimpleCPrinter;
import ir.core.IRFunction;
import ir.core.IRTopLevel;
import ir.importExport.IRExport;
import ir.importExport.IRImport;

public class Compiler {

	//Open and read file named fileName
	public static String readFile(String fileName) {
	    File file = new File(fileName);
	    String content = "";
	    try {
			List<String> lines = Files.readAllLines(file.toPath());
			for (String oneLine : lines) {
				content += oneLine + "\n";
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	    return content;
	}

	public static ParseTree parse(String content) {

		//We lex, create tokens and parse the content
		SimpleCLexer lexer = new SimpleCLexer(CharStreams.fromString(content));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SimpleCParser parser = new SimpleCParser(tokens);

        //We obtain the parse tree
        ParseTree tree = parser.translationUnit();
        return tree;

	}

	public static IRTopLevel frontend(ParseTree tree) {
        //We build the IR
        IRTopLevel top = IRBuilder.buildTopLevel(tree);
		return top;
	}

	public static String exportIR(IRTopLevel top) {
		return IRExport.printIR(top);
	}

	public static IRTopLevel importIR(String content) {
		//We lex, create tokens and parse the content
		IRTXTLexer lexer = new IRTXTLexer(CharStreams.fromString(content));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        IRTXTParser parser = new IRTXTParser(tokens);

        //We obtain the parse tree
        IrTopContext tree = parser.irTop();

        //We build the IR
        IRImport builder = new IRImport();
        return builder.visitIrTop(tree);
	}


    public static void main(String[] args) {
    	System.out.println("**** Parsing and printing the entry file : ");
		String content = readFile("src/test/resources/hello.sc");
    	ParseTree tree = Compiler.parse(content);
    	SimpleCPrinter astPrinter = new SimpleCPrinter();
    	System.out.println(astPrinter.visit(tree));

    	System.out.println("**** Building and printing the IR : ");
    	IRTopLevel ir = Compiler.frontend(tree);
    	String exported = exportIR(ir);
        System.out.println(exported);

    	System.out.println("**** Import/Export the IR : ");
    	IRTopLevel secondIr = importIR(exported);
        System.out.println(exportIR(secondIr));

    	System.out.println("**** Backend : ");
    	BackendTopLevel bt = BackendBuilder.lower(secondIr);



    }

}
