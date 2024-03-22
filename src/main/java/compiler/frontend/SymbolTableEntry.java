package compiler.frontend;

import ir.core.IRType;

public class SymbolTableEntry {
	public static enum ObjectType {
		FUNCTION,
		VARIABLE
	}

	private final String id;
	private final IRType entryType;
	private final ObjectType objectType;

	public SymbolTableEntry(String id, IRType type, boolean isFunction) {
		this.id = id;
		this.entryType = type;

		if (isFunction) {
			this.objectType = ObjectType.FUNCTION;
		} else {
			this.objectType = ObjectType.VARIABLE;
		}
	}
	
	public String getId() {
		return this.id;
	}
	
	public IRType getEntryType() {
		return this.entryType;
	}
	
	public ObjectType getObjectType() {
		return this.objectType;
	}
}
