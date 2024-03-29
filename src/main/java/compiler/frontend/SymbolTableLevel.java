package compiler.frontend;

import java.util.ArrayList;
import java.util.HashMap;

import org.antlr.v4.runtime.ParserRuleContext;

import ir.core.IRValue;

public class SymbolTableLevel {
	private HashMap<String, SymbolTableEntry> level;
	private ArrayList<SymbolTableLevel> children;
	private SymbolTableLevel parent;

	/**
	 * SymbolTableLevel constructor
	 */
	public SymbolTableLevel() {
		this.level = new HashMap<String, SymbolTableEntry>();
		this.children = new ArrayList<SymbolTableLevel>();
		this.parent = null;
	}

	/**
	 * Initialize a new level in the SymbolTable
	 * 
	 * @param parent : the parent of the level being created
	 * @param ctx    : the current context
	 * @return the new level created
	 */
	public SymbolTableLevel initializeLevel(SymbolTableLevel parent, ParserRuleContext ctx) {
		this.parent = parent;
		if (this.parent != null) {
			this.parent.children.add(this);
		}

		return this;
	}

	/**
	 * Getter for the parent level
	 * 
	 * @return the parent of the current level
	 */
	public SymbolTableLevel getParent() {
		return this.parent;
	}

	/**
	 * Insert a new symbol in the symbol table level. Fail if symbol already in the
	 * current level
	 * 
	 * @param name : symbol to insert
	 * @return inserted entry
	 */
	public SymbolTableEntry insert(String name, IRValue value, boolean isFunction) {
		assert (!this.level.containsKey(name));

		SymbolTableEntry newEntry = new SymbolTableEntry(name, value, isFunction);
		this.level.put(name, newEntry);

		return newEntry;
	}

	/**
	 * Look up for the given entry in the current level
	 * 
	 * @param name : symbol to look for
	 * @return the corresponding entry or null if not found
	 */
	public SymbolTableEntry lookup(String name) {
		if (this.level.containsKey(name)) {
			return this.level.get(name);
		} else {
			if (this.parent == null) {
				return null;
			} else
				return this.parent.lookup(name);
		}
	}
}
