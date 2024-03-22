package compiler.frontend;

import java.util.HashMap;

import org.antlr.v4.runtime.ParserRuleContext;

import ir.core.IRType;

public class SymbolTable {
	private SymbolTableLevel currentLevel;
	private HashMap<ParserRuleContext, SymbolTableLevel> contexts;

	/**
	 * SymbolTable constructor
	 */
	public SymbolTable() {
		// constructor
		this.currentLevel = new SymbolTableLevel();
		this.contexts = new HashMap<ParserRuleContext, SymbolTableLevel>();
	}

	/**
	 * Initialize a new scope in the symbol table
	 * 
	 * @param ctx : current context
	 * @return the new scope created with its corresponding SymbolTableLevel
	 */
	public SymbolTableLevel initializeScope(ParserRuleContext ctx) {
		SymbolTableLevel oldRoot = this.currentLevel;
		this.currentLevel = new SymbolTableLevel();
		this.currentLevel.initializeLevel(oldRoot, ctx);
		contexts.put(ctx, this.currentLevel);

		return this.currentLevel;
	}

	/**
	 * Finalize a scope
	 */
	public void finalizeScope() {
		this.currentLevel = this.currentLevel.getParent();
	}

	/**
	 * Insert a new symbol in the symbol table
	 * 
	 * @param name : new symbol to insert
	 * @return the entry created
	 */
	public SymbolTableEntry insert(String name, IRType type, boolean isFunction) {
		return this.currentLevel.insert(name, type, isFunction);
	}

	/**
	 * Look up an existing symbol in the symbol table
	 * 
	 * @param name : symbol to search for
	 * @return the entry found or null if not found
	 */
	public SymbolTableEntry lookup(String name) {
		return this.currentLevel.lookup(name);
	}
}
