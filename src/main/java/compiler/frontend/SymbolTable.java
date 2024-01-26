package compiler.frontend;

import org.antlr.v4.runtime.ParserRuleContext;

public class SymbolTable {
	
	public SymbolTable() {
		//constructor
	}
	
	public SymbolTableLevel initializeScope(ParserRuleContext ctx) {
		return null;
	}
	
	public void finalizeScope() {
	}
	
	public SymbolTableEntry insert(String name) {
		return null;
	}
	
	public SymbolTableEntry lookup(String name) {
		return null;
	}
	
}
