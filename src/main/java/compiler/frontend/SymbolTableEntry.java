package compiler.frontend;

import ir.core.IRValue;

public class SymbolTableEntry {
	public static enum ObjectType {
		FUNCTION,
		VARIABLE
	}

	private String name;
	private IRValue value;
	private ObjectType objectType;

	public SymbolTableEntry(String name, IRValue value, boolean isFunction) {
		this.name = name;
		this.value = value;

		if (isFunction) {
			this.objectType = ObjectType.FUNCTION;
		} else {
			this.objectType = ObjectType.VARIABLE;
		}
	}

	public IRValue getIRValue() {
		return this.value;
	}

	public String getName() {
		return this.name;
	}

	public ObjectType getObjectType() {
		return this.objectType;
	}
}
