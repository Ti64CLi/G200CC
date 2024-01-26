package ir.instruction;

import ir.core.IRType;
import ir.core.IRValue;
import ir.core.IRVisitor;

public class IRConstantInstruction<T extends Number> extends IRInstruction {

    IRType type;
    T value;
    public IRConstantInstruction(IRType type, T value) {
        this.type = type;
        this.value = value;
        this.addResult(type);
    }
    
    public T getValue() {
    	return value;
    }
    
    public void setValue(T v) {
    	value = v;
    }
    
    @Override
    public Object accept(IRVisitor v) {
        return v.visitConstantInstruction(this);
    }
}
