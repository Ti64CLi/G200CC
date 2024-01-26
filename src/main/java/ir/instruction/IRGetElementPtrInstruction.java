package ir.instruction;

import ir.core.IRType;
import ir.core.IRValue;
import ir.core.IRVisitor;

public class IRGetElementPtrInstruction extends IRInstruction {

    private final IRValue base;
    private final IRType elementType;
    private final int index;

    public IRGetElementPtrInstruction(IRValue base, IRType elementType, int index) {
        super();
        this.base = base;
        this.elementType = elementType;
        this.index = index;
        this.addResult(IRType.ADDR);
    }

    public IRValue getBase() {
        return base;
    }

    public IRType getElementType() {
        return elementType;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public Object accept(IRVisitor v) {
        return v.visitGetElementPtrInstruction(this);
    }

}
